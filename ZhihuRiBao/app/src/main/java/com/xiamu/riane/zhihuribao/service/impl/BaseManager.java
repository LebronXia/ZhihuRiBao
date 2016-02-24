package com.xiamu.riane.zhihuribao.service.impl;

import com.google.gson.Gson;
import com.xiamu.riane.zhihuribao.inject.module.component.ClientApiComponent;
import com.xiamu.riane.zhihuribao.protocol.ClientApi;

import javax.inject.Inject;

/**
 * Created by Xiamu on 2016/1/29.
 */
public class BaseManager {

    @Inject
    ClientApi mApi;
    @Inject
    Gson mGson;

    public BaseManager(){
        ClientApiComponent.Instance.get().inject(this);
    }

    public ClientApi getApi(){
        return mApi;
    }

    public Gson getGson(){
        return mGson;
    }
}
