package com.potato.zhbj.base;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.potato.zhbj.Constants;
import com.potato.zhbj.R;
import com.potato.zhbj.bean.NewsBean;

/**
 * Created by li.zhirong on 2018/9/17/017 11:49
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsBean.TabData mTabData;
    private TextView tv;
    String mUrll;
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
        View view = View.inflate(mActivity, R.layout.layout_tab_detail,null);
        return view;

    }

    @Override
    public void initData() {
//        tv.setText(mTabData.title);
//        getDataFromServer();
    }

    private void getDataFromServer() {

    }

    class TopNewsAdapter extends PagerAdapter{

      @Override
      public int getCount() {
          return 0;
      }

      @Override
      public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
          return view == object;
      }

      @NonNull
      @Override
      public Object instantiateItem(@NonNull ViewGroup container, int position) {
          return super.instantiateItem(container, position);
      }

      @Override
      public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
          container.removeView((View) object);
      }
  }
}
