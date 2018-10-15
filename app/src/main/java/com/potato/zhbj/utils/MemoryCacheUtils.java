package com.potato.zhbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by li.zhirong on 2018/10/12/012 17:53
 * 内存缓存
 */
public class MemoryCacheUtils {
//    private HashMap<String, Bitmap> mMemoryCache = new HashMap<>();
    //使用软引用（垃圾回收器更倾向于回收软引用和弱引用的对象）
//    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();

     private   LruCache<String,Bitmap> mLruCache;
     //lru :least recently use最近最少使用算法，最近最少使用的对象回收掉，从而保证内存不会超出内存范围，google建议使用

    public MemoryCacheUtils() {
        long maxMemotyCache = Runtime.getRuntime().maxMemory();//分配给app的最大内存数量
        Log.e("tag", "MemoryCacheUtils: " + maxMemotyCache);
        mLruCache = new LruCache<String,Bitmap>((int) (maxMemotyCache / 8)){
            //返回每个对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();//计算图片的大小，每行字节数*高度
                return byteCount;
            }
        };
    }
    //写缓存
    public void setMemoryCache(String url, Bitmap bitmap) {
//        SoftReference<Bitmap> bitmapSoftReference = new SoftReference<Bitmap>(bitmap);
//        mMemoryCache.put(url, bitmapSoftReference);
        mLruCache.put(url,bitmap);
    }



    //读缓存
    public Bitmap getMemoryCache(String url) {
//        SoftReference<Bitmap> bitmapSoftReference = mMemoryCache.get(url);
//        if (bitmapSoftReference != null) {
//            Bitmap bitmap = bitmapSoftReference.get();
//            return bitmap;
//        }
//        return null;
        return mLruCache.get(url);
    }
}
