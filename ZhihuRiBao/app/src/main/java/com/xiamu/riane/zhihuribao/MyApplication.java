package com.xiamu.riane.zhihuribao;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xiamu.riane.zhihuribao.inject.module.component.AppLayerComponent;
import com.xiamu.riane.zhihuribao.inject.module.component.DaggerAppLayerComponent;
import com.xiamu.riane.zhihuribao.service.impl.DataLayerManager;
import com.xiamu.riane.zhihuribao.util.SpUtil;

import java.io.File;

/**
 * Created by Xiamu on 2015/11/12.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        SpUtil.init(PreferenceManager.getDefaultSharedPreferences(this));
        DataLayerManager.init();
        AppLayerComponent.Instance.init(DaggerAppLayerComponent.builder().build());
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }
}
