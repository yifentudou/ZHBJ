package com.potato.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.potato.zhbj.base.BasePager;

/**
 * Created by li.zhirong on 2018/9/12/012 17:57
 * 政务
 */
public class GovPager extends BasePager {
    public GovPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        TextView tv = new TextView(mActivity);
        tv.setText("政务");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        content.addView(tv);
    }
}
