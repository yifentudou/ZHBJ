package com.potato.zhbj.base.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.zhbj.Constants;
import com.potato.zhbj.base.BasePager;
import com.potato.zhbj.bean.NewsBean;

import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.security.auth.callback.Callback;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by li.zhirong on 2018/9/12/012 17:57
 * 新闻中心
 */
public class NewsPager extends BasePager {
    public NewsPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        TextView tv = new TextView(mActivity);
        tv.setText("新闻中心");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        content.addView(tv);
        tv_title.setText("新闻");
        btn_menu.setVisibility(View.VISIBLE);

        //请求服务器，获取数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        //xUtils3请求网络
        RequestParams params = new RequestParams(Constants.CATEGORY_URL);
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("tag", result );
                getGsonData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("tag", ex.getMessage() );
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getGsonData(String result) {
        Gson gson = new Gson();
        NewsBean newsBean =  gson.fromJson(result, NewsBean.class);
    }
}
