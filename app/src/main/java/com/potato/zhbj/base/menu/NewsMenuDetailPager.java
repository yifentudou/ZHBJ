package com.potato.zhbj.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.potato.zhbj.R;
import com.potato.zhbj.activity.MainActivity;
import com.potato.zhbj.base.BaseMenuDetailPager;
import com.potato.zhbj.base.TabDetailPager;
import com.potato.zhbj.bean.NewsBean;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/14/014 17:41
 * 菜单详情页--新闻
 * <p>
 * ViewPagerIndicator使用流程
 * 1.引入库
 * 2.解决support-v4冲突（让两个版本一致）
 * 3.从例子程序中拷贝布局文件
 * 4.从例子程序中拷贝相关代码（指示器和viewpager绑定；重写getPagerTitle返回标题）
 * 5.在清单文件中增加样式
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    private ArrayList<NewsBean.TabData> mChildren;//页签网络数据
    private ArrayList<TabDetailPager> mTabPagers;//页签页面集合
    ViewPager vp_detail;
    TabPageIndicator tabIndicator;

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsBean.TabData> children) {
        super(mActivity);
        this.mChildren = children;
    }

    public NewsMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_news_detail, null);
        vp_detail = view.findViewById(R.id.vp_detail);
        tabIndicator = (TabPageIndicator) view.findViewById(R.id.tabIndicator);
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("tag", "当前位置是：" + position);
        if (position == 0) {
            //开启侧边栏
            setSlidingMenuEnabled(true);
        } else {
            //关闭侧边栏
            setSlidingMenuEnabled(false);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setSlidingMenuEnabled(boolean enabled) {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.menu;
        if (enabled) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    public class NewsMenuDetailPagerAdapter extends PagerAdapter {
        //指示器的标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            NewsBean.TabData data = mChildren.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager pager = mTabPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void initData() {
        //初始化页签
        mTabPagers = new ArrayList<>();
        for (int i = 0; i < mChildren.size(); i++) {
            TabDetailPager mTabPager = new TabDetailPager(mActivity, mChildren.get(i));
            mTabPagers.add(mTabPager);
        }
        vp_detail.setAdapter(new NewsMenuDetailPagerAdapter());
        tabIndicator.setViewPager(vp_detail);//绑定viewpager和tab,必须在viewpager有数据之后绑定
        tabIndicator.setOnPageChangeListener(this);
    }
}
