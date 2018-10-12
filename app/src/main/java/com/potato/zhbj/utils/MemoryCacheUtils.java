package com.potato.zhbj.utils;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by li.zhirong on 2018/10/12/012 17:53
 * 内存缓存
 */
public class MemoryCacheUtils {
    private HashMap<String, Bitmap> mMemoryCache = new HashMap<>();

    //写缓存
    public void setMemoryCache(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    //读缓存
    public Bitmap getMemoryCache(String url) {
        return mMemoryCache.get(url);
    }
}
