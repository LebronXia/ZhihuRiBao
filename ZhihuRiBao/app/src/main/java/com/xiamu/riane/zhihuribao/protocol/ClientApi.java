package com.xiamu.riane.zhihuribao.protocol;

import com.xiamu.riane.zhihuribao.model.Content;
import com.xiamu.riane.zhihuribao.model.Latest;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Xiamu on 2016/1/29.
 */
public interface ClientApi {

    //获取启动页面图片
    String URL_GET_START_IMAGE = "start-image/1080*1776";

    //获取最新日报新闻列表
    String URL_GET_LATEST_NEWS = "news/latest";

    //获取新闻
    String URL_GET_NEWS = "news/{newsId}";

    /**
     * 获取今日日报列表
     * @return
     */
    @GET(URL_GET_LATEST_NEWS)
    Observable<Latest> getTodayNews();

    /**
     * 获取新闻
     * @param newsId
     * @return
     */
    @GET(URL_GET_NEWS)
    Observable<Content> getNews(@Path("newsId") long newsId);


}
