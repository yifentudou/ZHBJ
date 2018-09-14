package com.potato.zhbj.utils;

import android.content.ContentProvider;
import android.content.Context;

/**
 * Created by li.zhirong on 2018/9/14/014 14:35
 * 网络缓存工具类
 */
public class CacheUtil {

    /**
     * 以url为key,以json为value,保存在本地
     */
    public static void setCache(String key, String value, Context context) {
        //也可以用文件缓存，以MD5(url：含有文件名违法的字符)为文件名，以json数据为内容，SharedPreferences配置文件可读性变差
        PrefUtil.setString(context, key, value);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @param context
     * @return
     */
    public static String getCache(String key, Context context) {
        return PrefUtil.getString(context, key, null);
    }
}
