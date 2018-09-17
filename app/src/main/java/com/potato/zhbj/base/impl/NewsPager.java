package com.potato.zhbj.base.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.zhbj.Constants;
import com.potato.zhbj.activity.MainActivity;
import com.potato.zhbj.base.BaseMenuDetailPager;
import com.potato.zhbj.base.BasePager;
import com.potato.zhbj.base.menu.InteractMenuDetailPager;
import com.potato.zhbj.base.menu.NewsMenuDetailPager;
import com.potato.zhbj.base.menu.PhotoMenuDetailPager;
import com.potato.zhbj.base.menu.TopicMenuDetailPager;
import com.potato.zhbj.bean.NewsBean;
import com.potato.zhbj.fragment.LeftMenuFragment;
import com.potato.zhbj.utils.CacheUtil;

import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by li.zhirong on 2018/9/12/012 17:57
 * 新闻中心
 */
public class NewsPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> menuList;//菜单详情页集合
    private NewsBean newsBean;

    public NewsPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
//        TextView tv = new TextView(mActivity);
//        tv.setText("新闻中心");
//        tv.setTextColor(Color.RED);
//        tv.setTextSize(22);
//        tv.setGravity(Gravity.CENTER);
//        content.addView(tv);
        tv_title.setText("新闻");
        btn_menu.setVisibility(View.VISIBLE);
        //先判断又没有缓存，如果有，则获取缓存
        String isCache = CacheUtil.getCache(Constants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(isCache)) {
            Log.e("tag", "写入缓存");
            getGsonData(isCache);
        } else {
            //请求服务器，获取数据
            getDataFromServer();
        }
        setCurrentMenuDetailPager(0);
    }

    private void getDataFromServer() {
        //xUtils3请求网络
        RequestParams params = new RequestParams(Constants.CATEGORY_URL);
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("tag", result);
                getGsonData(result);
                //写缓存
                CacheUtil.setCache(Constants.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("tag", ex.getMessage());
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
        newsBean = gson.fromJson(result, NewsBean.class);
        //拿到mainActivity
        MainActivity mainActivity = (MainActivity) mActivity;
        //拿到leftFragment
        LeftMenuFragment leftFragment = mainActivity.getLeftFragment();
        leftFragment.setMenuData(newsBean.data);
        //初始化菜单详情页
        menuList = new ArrayList<>();
        menuList.add(new NewsMenuDetailPager(mActivity, newsBean.data.get(0).children));
        menuList.add(new TopicMenuDetailPager(mActivity));
        menuList.add(new PhotoMenuDetailPager(mActivity));
        menuList.add(new InteractMenuDetailPager(mActivity));

    }

    //设置新闻中心的菜单详情页
    public void setCurrentMenuDetailPager(int position) {
        //重新给framelayout添加内容
        BaseMenuDetailPager baseMenuDetailPager = menuList.get(position);
        View view = baseMenuDetailPager.mRootView;//当前布局的页面
        content.removeAllViews();//清除view
        content.addView(view);//给帧布局添加布局
        baseMenuDetailPager.initData();//初始化数据
        tv_title.setText(newsBean.data.get(position).title);//更新标题
    }
}
