package com.xiamu.riane.zhihuribao.service.impl;

import com.xiamu.riane.zhihuribao.model.Content;
import com.xiamu.riane.zhihuribao.service.DataLayer;
import com.xiamu.riane.zhihuribao.util.SpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Xiamu on 2016/1/31.
 */
public class DailyManager extends BaseManager implements DataLayer.DailyService{
    @Override
    public Observable<Content> getNews(long newsId) {
        return getApi().getNews(newsId);
    }

    @Override
    public Observable<Content> getLocalNews(final String id) {
        return Observable.create(new Observable.OnSubscribe<Content>(){
            @Override
            public void call(Subscriber<? super Content> subscriber) {
                try{
                    subscriber.onStart();
                    String json = SpUtil.find(id);
                    Content content = getGson().fromJson(json, Content.class);
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                } catch (Exception e){
                    subscriber.onError(e);
                }

            }
        });
    }

    @Override
    public void cacheNews(Content content) {
        SpUtil.saveOrUpdate(String.valueOf(content.getId()), getGson().toJson(content));
    }
}
