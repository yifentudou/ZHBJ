package com.potato.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by li.zhirong on 2018/10/12/012 15:02
 * 本地缓存
 */
public class LocalCacheUtils {
    public static final String LOCAl_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";

    //写缓存
    public void setLocalCache(String url, Bitmap bitmap) {
        File dir = new File(LOCAl_CACHE_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        try {
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(dir, fileName);
            //当前图片压缩到本地,参1：图片格式，参2：压缩质量，参3：输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读本地缓存
    public Bitmap getLocalCache(String url) {
        try {
            File cacheFile = new File(LOCAl_CACHE_PATH, MD5Encoder.encode(url));
            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
