package com.potato.zhbj.view;

import android.content.Context;

/**
 * Created by li.zhirong on 2018/10/15/015 14:19
 * px to dp
 */
public class DensityUtils {
    public static int dip2px(float dip, Context context){
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);//3.9 -> 3,四舍五入
        return px;
    }
    public static float px2dip(int px, Context context){
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
