package com.potato.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by li.zhirong on 2018/10/12/012 14:17
 */
public class NetCacheUtils {
    private HttpURLConnection connection;
    private ImageView imageViews;
    private String imageUrl;
    private LocalCacheUtils localCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils) {
        this.localCacheUtils = localCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        new BitmapTask().execute(imageView, url);
    }

    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {
        //主线程，预加载
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "onPreExecute: ");
        }

        //子线程，可以直接异步加载
        @Override
        protected Bitmap doInBackground(Object[] objects) {
            imageViews = (ImageView) objects[0];
            imageUrl = (String) objects[1];
            imageViews.setTag(imageUrl);//打标记，将当前url和imageview绑在一起
            //开始下载图片
            Bitmap bitmap = download(imageUrl);
            Log.e("tag", "从本地加载图片辣: ");
            return bitmap;
        }

        //主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.e("tag", "onProgressUpdate: ");
        }

        //主线程,可直接更新ui
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("tag", "onPostExecute: ");
            if (bitmap != null) {
                String url = (String) imageViews.getTag();
                if (url.equals(imageUrl)) {
                    //给imageview设置图片
                    //由于listview重用的机制，导致imageview被多个item共用，从而错误的图片设置给了imageview对象
                    //所以在此处判断是否是正确的url
                    imageViews.setImageBitmap(bitmap);
                    //写本地缓存
                    localCacheUtils.setLocalCache(url,bitmap);
                }

            }
            super.onPostExecute(bitmap);
        }
    }

    private Bitmap download(String url) {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);//连接超时
            connection.setReadTimeout(5000);//读取超时
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream inputStream = connection.getInputStream();
                //根据输入流生成一个bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

}
