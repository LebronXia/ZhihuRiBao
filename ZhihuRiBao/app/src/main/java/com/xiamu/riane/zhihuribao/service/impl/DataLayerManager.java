package com.xiamu.riane.zhihuribao.service.impl;

import com.xiamu.riane.zhihuribao.inject.module.component.ClientApiComponent;
import com.xiamu.riane.zhihuribao.inject.module.component.DaggerClientApiComponent;

/**
 * @author lsxiao
 * @date 2015-11-03 22:28
 */
public class DataLayerManager {
    public static void init() {
        ClientApiComponent.Instance.init(DaggerClientApiComponent.builder().build());
    }
}
