package com.xiamu.riane.zhihuribao.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.activity.LatestContentActivity;
import com.xiamu.riane.zhihuribao.activity.MainActivity;
import com.xiamu.riane.zhihuribao.adapter.MainNewsItemAdapter;
import com.xiamu.riane.zhihuribao.model.Before;
import com.xiamu.riane.zhihuribao.model.Latest;
import com.xiamu.riane.zhihuribao.model.StoriesEntity;
import com.xiamu.riane.zhihuribao.util.Constant;
import com.xiamu.riane.zhihuribao.util.HttpUtils;
import com.xiamu.riane.zhihuribao.view.LunboView;

import java.util.List;

/**
 * Created by Xiamu on 2015/11/24.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    private ListView lv_news;
    private Latest latest;
    private Before before;
    private SwipeRefreshLayout sr;
    private LunboView lunboView;
    private String date;
    private List<Latest> items;
    private boolean isLoading = false;
    private MainNewsItemAdapter mAdapter;
    private Handler handler = new Handler();
    public static final String EXTRA_STORIES = "extra_stories";

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.main_news_layout,container,false);
        lv_news = (ListView) view.findViewById(R.id.lv_news);
        sr = (SwipeRefreshLayout)view.findViewById(R.id.srl_news_list);
        sr.setOnRefreshListener(this);
        sr.setColorSchemeColors(getResources().getColor(R.color.light_toolbar));
        View header = inflater.inflate(R.layout.lunboview,lv_news,false);
        lunboView = (LunboView) header.findViewById(R.id.lunbo);
        lunboView.setOnItemClickListener(new LunboView.OnItemClickListener() {
            @Override
            public void click(View v, Latest.TopStoriesEntity entity) {
                //点击跳转
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                StoriesEntity storiesEntity = new StoriesEntity();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                LatestContentActivity.start(getActivity(), storiesEntity, startingLocation);
                mActivity.overridePendingTransition(0, 0);
            }
        });
        lv_news.addHeaderView(header);
        mAdapter = new MainNewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);

            }
        });
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动改变时
            //空闲SCROLL_STATE_IDLE 、滑动SCROLL_STATE_TOUCH_SCROLL和惯性滑动SCROLL_STATE_FLING
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    //((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    setSwipeRefreshEnable(enable);
                }
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                    loadMore(Constant.BEFORE + date);
                }
            }
        });

        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoriesEntity entity = (StoriesEntity)parent.getAdapter().getItem(position);
                Log.e("MainFragment", entity.getTitle());
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] = view.getWidth() / 2;
                LatestContentActivity.start(getActivity(), entity, startingLocation);
                mActivity.overridePendingTransition(0,0);
            }
        });
        return view;
    }

    @Override
    public void onRefresh() {
        loadFirst();
    }

    @Override
    protected void initData() {
        super.initData();
        loadFirst();
    }

    //第一次下载
    private void loadFirst(){
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.e("MainFragment", responseString);
                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",'" + responseString + "')");
                    db.close();
                    parseLateestJson(responseString);
                    hideProgress();
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date =" + Constant.LATEST_COLUMN,null);
            if (cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLateestJson(json);
        }
            cursor.close();
            db.close();
        }
    }

    private void parseLateestJson(String responseString){
        Gson gson = new Gson();
        latest = gson.fromJson(responseString, Latest.class);
        date = latest.getDate();
        lunboView.setTopStoriesEntities(latest.getTop_stories());
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = latest.getStories();
                //增加今日要闻的字
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日头条");
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }

    //下载更多
    private void loadMore(final String url){
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    //下载成功 保存到数据库了
                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
                    //如果id一样，用其他的数据覆盖掉
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",'" + responseString + "')");
                    db.close();
                    parseBeforeJson(responseString);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                }
                //                @Override
//                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//                }

//                @Override
//                public void onSuccess(int i, Header[] headers, String responseString) {
//                    //下载成功 保存到数据库了
//                    SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getWritableDatabase();
//                    //如果id一样，用其他的数据覆盖掉
//                    db.execSQL("replace into CacheList(date,json) values(" + date + ",'" + responseString + "')");
//                    db.close();
//                    parseBeforeJson(responseString);
//                }

            });
        } else {
            SQLiteDatabase db = ((MainActivity)mActivity).getCacheDbHelper().getReadableDatabase();
            //查询当前日期的数据
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date<" + date, null);
                isLoading = false;
                //底部弹出框
                Snackbar sb = Snackbar.make(lv_news, "没有更多的离线内容了", Snackbar.LENGTH_SHORT);
                //背景调色
                sb.show();
            }
            cursor.close();
            db.close();
        }

    }

    //解释json
    private void parseBeforeJson(String responseString){
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null){
            isLoading = false;
            return;
        }
        date = before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = before.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle(converDate(date));
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;   //没有东西下载设置为false
            }
        });
    }

        public void setSwipeRefreshEnable(boolean enable){
            sr.setEnabled(enable);
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



    //格式化日期
    private String converDate(String date){
        String result = date.substring(0,4);
        result += "年";
        result += date.substring(4,6);
        result += "月";
        result = date.substring(6,8);
        result += "日";
        return  result;
    }
}
