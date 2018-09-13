package com.potato.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.potato.zhbj.base.BasePager;

/**
 * Created by li.zhirong on 2018/9/12/012 17:57
 * 设置
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        TextView tv = new TextView(mActivity);
        tv.setText("设置");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        content.addView(tv);
        tv_title.setText("设置");
        btn_menu.setVisibility(View.GONE);
    }
}
