package com.potato.zhbj;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by li.zhirong on 2018/9/13/013 17:27
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
