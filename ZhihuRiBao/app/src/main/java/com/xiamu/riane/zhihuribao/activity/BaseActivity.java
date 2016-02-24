package com.xiamu.riane.zhihuribao.activity;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.xiamu.riane.zhihuribao.inject.module.component.AppLayerComponent;
import com.xiamu.riane.zhihuribao.service.DataLayer;

import javax.inject.Inject;

/**
 * Created by Xiamu on 2016/2/4.
 */
public class BaseActivity extends RxAppCompatActivity{
    @Inject
    DataLayer mDataLayer;

    public BaseActivity(){
        AppLayerComponent.Instance.get().inject(this);
    }

    public DataLayer getDataLayer(){
        return mDataLayer;
    }
}
