package com.potato.zhbj.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.potato.zhbj.base.BaseMenuDetailPager;

/**
 * Created by li.zhirong on 2018/9/14/014 17:41
 * 菜单详情页--互动
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        TextView tv = new TextView(mActivity);
        tv.setText("菜单详情页--互动");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}
