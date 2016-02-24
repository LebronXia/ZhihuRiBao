package com.xiamu.riane.zhihuribao.inject.module.component;

import android.support.annotation.NonNull;

import com.xiamu.riane.zhihuribao.activity.BaseActivity;
import com.xiamu.riane.zhihuribao.inject.module.module.AppLayerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Xiamu on 2016/2/1.
 */
@Singleton
@Component(dependencies = AppLayerModule.class)
public interface AppLayerComponent {

    void inject(BaseActivity baseActivity);

    class Instance{
        private static AppLayerComponent sAppLayerComponent;

        public static void init(@NonNull AppLayerComponent component){
            sAppLayerComponent = component;
        }

        public static AppLayerComponent get(){
            return sAppLayerComponent;
        }
    }
}
