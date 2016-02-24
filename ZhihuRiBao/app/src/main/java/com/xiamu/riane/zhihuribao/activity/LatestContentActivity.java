package com.xiamu.riane.zhihuribao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.ActivityEvent;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.fragment.MainFragment;
import com.xiamu.riane.zhihuribao.model.Content;
import com.xiamu.riane.zhihuribao.model.StoriesEntity;
import com.xiamu.riane.zhihuribao.service.DataLayer;
import com.xiamu.riane.zhihuribao.util.Constant;
import com.xiamu.riane.zhihuribao.util.HtmlUtil;
import com.xiamu.riane.zhihuribao.view.RevealBackgroundView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Xiamu on 2015/12/16.
 */
public class LatestContentActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener{
    @Bind(R.id.revealBackgroundView)
    RevealBackgroundView mRevealBackground;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsibleActionView;
    @Bind(R.id.iv)
    ImageView mImageView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nested_view)
    NestedScrollView mNestedScrollView;
    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.cpb_loading)
    ContentLoadingProgressBar mCpbLoading;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;
    private StoriesEntity entity;
    private Content mContent;

    @Inject
    DataLayer mDataLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_content_layout);
        entity = (StoriesEntity) getIntent().getSerializableExtra(MainFragment.EXTRA_STORIES);
        //Log.e("LatestContentActivity", entity.getTitle());
        ButterKnife.bind(this);
        init();
        loadData();
        setupRevealBackground(savedInstanceState);
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mNestedScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //mNestedScrollView.setElevation(0);
        mCollapsibleActionView.setTitle(entity.getTitle());
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //向下兼容
       // mWebView.setElevation(0);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        //设置缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //开启Don storage Api功能
        mWebView.getSettings().setDomStorageEnabled(true);

    }
    /**
     * 显示Loading
     */
    private void showLoading() {
        mCpbLoading.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏Loading
     */
    private void hideLoading() {
        mCpbLoading.setVisibility(View.GONE);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        mRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(Constant.START_LOCATION);
            mRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    mRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            mRevealBackground.setToFinishedFrame();
        }
    }

    private void loadData(){
        //服务端数据源
        Observable<Content> network = getDataLayer().getDailyService().getNews(entity.getId());
        //缓存数据源
        Observable<Content> cache = getDataLayer().getDailyService().getLocalNews(String.valueOf(entity.getId()));

        //输出数据前缓存到本地
        network = network.doOnNext(new Action1<Content>() {
            @Override
            public void call(Content content) {
                getDataLayer().getDailyService().cacheNews(content);
            }
        });

        //默认先从本地获取数据,先从cache取，没有的话再从network取
        Observable<Content> source = Observable.concat(cache,network).first(new Func1<Content, Boolean>() {
            @Override
            public Boolean call(Content content) {
                //本地缓存不为空
                return content != null;
            }
        });

        source.compose(this.<Content>bindUntilEvent(ActivityEvent.PAUSE))//当pause时不发出事件
        .doOnNext(new Action1<Content>() {
            @Override
            public void call(Content content) {
                if (content != null) {
                    getDataLayer().getDailyService().cacheNews(content);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        //解除订阅关系
                        hideLoading();
                        if (mContent == null) {
                            mTvLoadEmpty.setVisibility(View.GONE);
                            mTvLoadError.setVisibility(View.VISIBLE);
                        }
                    }
                 })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Content>() {
                    @Override
                    public void call(Content content) {
                        hideLoading();
                        mContent = content;
                        if (mContent == null) {
                            mTvLoadEmpty.setVisibility(View.VISIBLE);
                        } else {
                            Picasso.with(LatestContentActivity.this)
                                    .load(content.getImage())
                                    .into(mImageView);

                            Log.d("HtmlBody", content.getBody());
                            Log.d("js",content.getCss().get(0));

                            String htmlData = HtmlUtil.createHtmlData(content);
                            mWebView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                            mTvLoadError.setVisibility(View.GONE);
                        }
                        mTvLoadError.setVisibility(View.GONE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        hideLoading();
                    }
                });


    }

    @Override
    public void onStateChange(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.slide_out_to_left);
    }

    public static void start(Context context, StoriesEntity entity, int[] startingLocation){
        Intent intent = new Intent(context, LatestContentActivity.class);
        intent.putExtra(MainFragment.EXTRA_STORIES, entity);
        intent.putExtra(Constant.START_LOCATION, startingLocation);
        context.startActivity(intent);
    }
}
