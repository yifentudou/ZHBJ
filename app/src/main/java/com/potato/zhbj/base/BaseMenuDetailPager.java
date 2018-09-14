package com.potato.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by li.zhirong on 2018/9/14/014 17:36
 * 菜单详情页基类
 */
public abstract class BaseMenuDetailPager {
    public final View mRootView;//菜单详情页的根目录
    public Activity mActivity;

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView =initView();
    }

    public abstract View initView();//初始化布局，必须由子类实现

    public void initData() {
    }
}
