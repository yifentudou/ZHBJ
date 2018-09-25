package com.potato.zhbj.base;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.potato.zhbj.Constants;
import com.potato.zhbj.R;
import com.potato.zhbj.bean.NewsBean;
import com.potato.zhbj.bean.NewsTabBean;
import com.potato.zhbj.utils.CacheUtil;
import com.potato.zhbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/17/017 11:49
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsBean.TabData mTabData;
    private TextView tv;
    private String mUrll;
    private ArrayList<NewsTabBean.TabTopNewsBean> newsTop;
    private TopNewsViewPager viewPager;
    private TextView tv_topic_title;
    CirclePageIndicator tabIndicator;

    public TabDetailPager(Activity mActivity) {
        super(mActivity);
    }

    public TabDetailPager(Activity mActivity, NewsBean.TabData tabData) {
        super(mActivity);
        mTabData = tabData;
        mUrll = Constants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
//        tv = new TextView(mActivity);
////      tv.setText(mTabData.title);//防止空指针
//        tv.setTextColor(Color.RED);
//        tv.setTextSize(22);
//        tv.setGravity(Gravity.CENTER);
//        return tv;
        View view = View.inflate(mActivity, R.layout.layout_tab_detail, null);
        viewPager = (TopNewsViewPager) view.findViewById(R.id.viewPager);
        tv_topic_title = view.findViewById(R.id.tv_topic_title);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        return view;

    }

    @Override
    public void initData() {
//        tv.setText(mTabData.title);
        String isCache = CacheUtil.getCache(mUrll, mActivity);
        if (!TextUtils.isEmpty(isCache)) {
            getGsonData(isCache);
        } else {
            getDataFromServer();
        }


    }

    private void getDataFromServer() {
        OkGo.<String>get(mUrll).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("tag", "获取tabDetailPager页面的URL成功");
                getGsonData(response.body());
                CacheUtil.setCache(mUrll, response.body(), mActivity);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("tag", "获取tabDetailPager页面的URL失败");
                Log.e("tag", response.message());
            }
        });
    }

    private void getGsonData(String body) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(body, NewsTabBean.class);
        newsTop = newsTabBean.data.topnews;
        if (newsTop != null) {
            viewPager.setAdapter(new TopNewsAdapter());
            tabIndicator.setViewPager(viewPager);
            tabIndicator.setSnap(true);//快照方式
            tabIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //更新新闻标题
                    tv_topic_title.setText(newsTop.get(position).title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            tv_topic_title.setText(newsTop.get(0).title);//默认为0位置数据
            tabIndicator.onPageSelected(0);//默认让第一个选中，解决页面销毁时重新初始化时，Indicator仍然保持之前的状态的bug
        }
    }


    class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return newsTop.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
//            imageView.setImageResource(R.drawable.ic_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片缩放方式，
            String imageUrl = newsTop.get(position).topimage;
            Glide.with(mActivity).load(imageUrl).error(R.drawable.ic_pic_default).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
