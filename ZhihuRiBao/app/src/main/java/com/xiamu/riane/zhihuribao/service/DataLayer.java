package com.xiamu.riane.zhihuribao.service;

import com.xiamu.riane.zhihuribao.model.Content;

import rx.Observable;

/**
 * Created by Xiamu on 2016/1/29.
 */
public class DataLayer {
    DailyService mDailyService;

    public DataLayer(DailyService dailyService){
        mDailyService = dailyService;
    }

    public DailyService getDailyService(){
        return mDailyService;
    }

    public interface DailyService{

        /**
         * 获取新闻
         * @param newsId
         * @return
         */
        Observable<Content> getNews(long newsId);

        /**
         * 获取本地新闻
         * @param id
         * @return
         */
        Observable<Content> getLocalNews(final String id);

        /**
         * 缓存新闻
         * @param content
         */
        void cacheNews(final Content content);

    }
}
