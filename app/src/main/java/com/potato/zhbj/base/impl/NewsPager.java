package com.potato.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
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

import java.util.ArrayList;

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
        OkGo.<String>get(Constants.CATEGORY_URL)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tag", response.body());
                        getGsonData(response.body());
                        //写缓存
                        CacheUtil.setCache(Constants.CATEGORY_URL, response.body(), mActivity);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("tag", response.message());
                        Toast.makeText(mActivity, response.message(), Toast.LENGTH_LONG).show();
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
