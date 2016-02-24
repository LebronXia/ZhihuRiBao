package com.xiamu.riane.zhihuribao.inject.module.module;

import com.xiamu.riane.zhihuribao.service.DataLayer;
import com.xiamu.riane.zhihuribao.service.impl.DailyManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Xiamu on 2016/2/1.
 */
@Module
public class AppLayerModule {

    /**
     * 初始化DataLayer，将Dailymanager加入构造函数
     * @return
     */
    @Singleton
    @Provides
    public DataLayer providerDataLayer(){
        return new DataLayer(new DailyManager());
    }
}
