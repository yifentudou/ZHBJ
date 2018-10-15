package com.potato.zhbj.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by li.zhirong on 2018/10/12/012 17:53
 * 内存缓存
 */
public class MemoryCacheUtils {
//    private HashMap<String, Bitmap> mMemoryCache = new HashMap<>();
    //使用软引用
    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();

    //写缓存
    public void setMemoryCache(String url, Bitmap bitmap) {
        SoftReference<Bitmap> bitmapSoftReference = new SoftReference<Bitmap>(bitmap);
        mMemoryCache.put(url, bitmapSoftReference);
    }

    //读缓存
    public Bitmap getMemoryCache(String url) {
        SoftReference<Bitmap> bitmapSoftReference =  mMemoryCache.get(url);
        if(bitmapSoftReference != null){
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }
        return null;
    }
}
