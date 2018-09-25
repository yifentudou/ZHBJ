package com.potato.zhbj.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by li.zhirong on 2018/9/19/019 15:25
 * 头条新闻自定义viewpager
 */
public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int dx;
    private int dy;

    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 上下滑动拦截
     * 向左滑动，并且是最后一个页面，事件拦截
     * 向右滑动，并且是第一个页面，事件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) ev.getX();
                endY = (int) ev.getY();

                dx = endX - startX;
                dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    //左右滑动
                    if (dx > 0) {
                        //向右滑动
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //向左滑动
                        int count = getAdapter().getCount();
                        if (getCurrentItem() == count - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
