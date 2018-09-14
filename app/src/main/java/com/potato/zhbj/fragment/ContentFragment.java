package com.potato.zhbj.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.potato.zhbj.R;
import com.potato.zhbj.activity.MainActivity;
import com.potato.zhbj.base.BasePager;
import com.potato.zhbj.base.impl.GovPager;
import com.potato.zhbj.base.impl.HomePager;
import com.potato.zhbj.base.impl.NewsPager;
import com.potato.zhbj.base.impl.ServicePager;
import com.potato.zhbj.base.impl.SettingPager;
import com.potato.zhbj.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/12/012 15:58
 */
public class ContentFragment extends BaseFragment {
    private ArrayList<BasePager> mPagers;
    private NoScrollViewPager mViewPager;
    private RadioGroup rg_tab;

    @Override
    protected void initData() {
        mPagers = new ArrayList<>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsPager(mActivity));
        mPagers.add(new ServicePager(mActivity));
        mPagers.add(new GovPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ContentPagerAdapter());
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);//禁止平滑
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_service:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });

        mPagers.get(0).initData();//默认加载第一页情况
        setSlidingMenuEnabled(false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager = mPagers.get(position);
                basePager.initData();
                if (position == 0 || position == mPagers.size() - 1) {
                    setSlidingMenuEnabled(false);
                } else {
                    setSlidingMenuEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 开启或关闭侧边栏
     *
     * @param enabled
     */
    private void setSlidingMenuEnabled(boolean enabled) {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.menu;
        if (enabled) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_tab = view.findViewById(R.id.rg_tab);
        return view;
    }

    class ContentPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            View view = basePager.rootView;//获取当前页面的布局
//          basePager.initData();//初始化数据，viewpager会默认加载下一页，为了节省流量，和性能优化，不在此处调用初始化方法
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    //获取新闻中心页面
    public NewsPager getNewsPager() {
        return (NewsPager) mPagers.get(1);
    }
}
