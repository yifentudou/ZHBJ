package com.potato.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.potato.zhbj.R;

/**
 * Created by li.zhirong on 2018/9/12/012 17:47
 * 5个标签页的基类
 */
public class BasePager {
    public View rootView;//当前页的布局文件
    public Activity mActivity;
    public TextView tv_title;
    public ImageButton btn_menu;
    public FrameLayout content;//空的帧布局，要动态添加布局

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        rootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_base, null);
        tv_title = view.findViewById(R.id.tv_title);
        btn_menu = view.findViewById(R.id.btn_menu);
        content = view.findViewById(R.id.content);
        return view;
    }

    //初始化数据
    public void initData() {
    }
}
