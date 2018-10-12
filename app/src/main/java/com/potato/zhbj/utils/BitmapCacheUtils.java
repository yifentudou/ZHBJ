package com.potato.zhbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.potato.zhbj.R;

import java.util.IdentityHashMap;

/**
 * Created by li.zhirong on 2018/10/12/012 14:09
 * 自定义三级缓存图片加载工具
 */
public class BitmapCacheUtils {
    private MemoryCacheUtils memoryCacheUtils;
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils localCacheUtils;

    public BitmapCacheUtils() {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(localCacheUtils);
    }

    public void display(ImageView imageView, String url) {
        imageView.setImageResource(R.drawable.ic_pic_default);
        //1.优先从内存中加载图片，速度快

        //2.其次从本地（sdcard）加载图片
        Bitmap bitmap = localCacheUtils.getLocalCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.e("tag", "从本地加载图片辣: " );
            //写内存缓存
            memoryCacheUtils.setMemoryCache(url,bitmap);
            return;
        }
        //3.最后从网络获取图片，速度慢
        mNetCacheUtils.getBitmapFromNet(imageView, url);
    }
}
