package com.xiamu.riane.zhihuribao.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.activity.MainActivity;
import com.xiamu.riane.zhihuribao.activity.NewsContentActivity;
import com.xiamu.riane.zhihuribao.adapter.NewsItemAdapter;
import com.xiamu.riane.zhihuribao.model.News;
import com.xiamu.riane.zhihuribao.model.StoriesEntity;
import com.xiamu.riane.zhihuribao.util.Constant;
import com.xiamu.riane.zhihuribao.util.HttpUtils;

/**
 * Created by Xiamu on 2015/11/21.
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private ImageLoader mImageLoader;   //图片加载
    private ListView lv_news;    //列表
    private ImageView iv_title;
    private TextView tv_title;
    private SwipeRefreshLayout sr;
    private String urlId;
    private String title;
    private News news;
    private NewsItemAdapter mAdapter;
    public static final String EXTRA_STORIES = "extra_stories";


    public NewsFragment(String id,String title){
        urlId = id;
        this.title = title;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)mActivity).setToolbarTitle(title);
        Log.e("NewsFragment", "实现了   bianhua....");
        View view = inflater.inflate(R.layout.news_layout,container,false);
        mImageLoader = ImageLoader.getInstance();
        lv_news = (ListView) view.findViewById(R.id.lv_news);
        View header = LayoutInflater.from(mActivity).inflate(
                R.layout.news_header, lv_news, false
        );
        iv_title = (ImageView) header.findViewById(R.id.iv_title);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        sr = (SwipeRefreshLayout)view.findViewById(R.id.srl_news_list);
        sr.setOnRefreshListener(this);
        lv_news.addHeaderView(header);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoriesEntity entity = (StoriesEntity)parent.getAdapter().getItem(position);
                Log.e("MainFragment", entity.getTitle());
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] = view.getWidth() / 2;
                NewsContentActivity.start(getActivity(), entity, startingLocation);
                mActivity.overridePendingTransition(0,0);
            }
        });
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    setSwipeRefreshEnable(enable);
                }
            }
        });
        return view;
    }

    public void setSwipeRefreshEnable(boolean enable){
        sr.setEnabled(enable);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(Constant.THEMENEWS + urlId, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.e("NewsFragment", "获取数据成功---");
                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(urlId)) + ",'" + responseString + "')");
                    db.close();
                    parseJson(responseString);
                    hideProgress();
                }

//                @Override
//                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//                }
//
//                @Override
//                public void onSuccess(int i, Header[] headers, String responseString) {
//                    Log.e("NewsFragment", "获取数据成功---");
//                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
//                    db.execSQL("replace into CacheList(date,json) values(" + (Constant.BASE_COLUMN + Integer.parseInt(urlId)) + ",'" + responseString + "')");
//                    db.close();
//                    parseJson(responseString);
//                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Constant.BASE_COLUMN + Integer.parseInt(urlId)), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }
    }

    private void parseJson(String responseString){
        Gson gson = new Gson();
        news = gson.fromJson(responseString, News.class);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        tv_title.setText(news.getDescription());
        Log.e("NewsFragment" , news.getDescription());
        mImageLoader.displayImage(news.getImage(), iv_title, options);
        mAdapter = new NewsItemAdapter(mActivity, news.getStories());
        lv_news.setAdapter(mAdapter);
    }

    public void showProgress() {
        sr.post(new Runnable() {
            @Override
            public void run() {
                //刷新动画
                sr.setRefreshing(true);
            }
        });
    }

    public void hideProgress() {
        sr.post(new Runnable() {
            @Override
            public void run() {
                sr.setRefreshing(false);
            }
        });
    }
}


