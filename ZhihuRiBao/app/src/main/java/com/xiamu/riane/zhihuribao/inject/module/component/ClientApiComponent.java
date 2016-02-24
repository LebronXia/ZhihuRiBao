package com.xiamu.riane.zhihuribao.inject.module.component;

import com.xiamu.riane.zhihuribao.inject.module.module.ClientApiModule;
import com.xiamu.riane.zhihuribao.service.impl.BaseManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Xiamu on 2016/2/1.
 */
@Singleton
@Component(modules = ClientApiModule.class)
public interface ClientApiComponent {
    void inject(BaseManager manager);

    class Instance {
        private static ClientApiComponent sComponent;

        public static void init(ClientApiComponent component) {
            sComponent = component;
        }

        public static ClientApiComponent get() {
            return sComponent;
        }
    }
}
