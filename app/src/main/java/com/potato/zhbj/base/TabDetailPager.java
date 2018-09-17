package com.potato.zhbj.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.potato.zhbj.bean.NewsBean;

/**
 * Created by li.zhirong on 2018/9/17/017 11:49
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsBean.TabData mTabData;
    private TextView tv;
    public TabDetailPager(Activity mActivity) {
        super(mActivity);
    }

    public TabDetailPager(Activity mActivity, NewsBean.TabData tabData) {
        super(mActivity);
        mTabData = tabData;
    }

    @Override
    public View initView() {
        tv = new TextView(mActivity);
//      tv.setText(mTabData.title);//防止空指针
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public void initData() {
        tv.setText(mTabData.title);
    }
}
