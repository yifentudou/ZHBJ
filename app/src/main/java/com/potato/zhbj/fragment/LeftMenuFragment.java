package com.potato.zhbj.fragment;

import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.potato.zhbj.R;
import com.potato.zhbj.activity.MainActivity;
import com.potato.zhbj.base.impl.NewsPager;
import com.potato.zhbj.bean.NewsBean;
import com.viewpagerindicator.TabPageIndicator;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/12/012 14:59
 */
public class LeftMenuFragment extends BaseFragment {

    private ListView listView;

    private ArrayList<NewsBean.NewsBeanData> mNewsBeanData;
    private int mCurrentItem;

    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left, null);
        listView = view.findViewById(R.id.listview);
        return view;
    }

    //给侧边栏数据
    public void setMenuData(ArrayList<NewsBean.NewsBeanData> data) {
        mCurrentItem = 0;//当前选中的位置归0

        //更新页面
        mNewsBeanData = data;
        final leftMenuAdapter mAdapter = new leftMenuAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentItem = position;//更新当前选中的位置
                mAdapter.notifyDataSetChanged();//刷新listview
                toggle();//收起侧边栏

                //侧边栏发生改变，更改新闻中心的frameLayout
                setCurrentDetailPager(position);

            }
        });
    }

    private void setCurrentDetailPager(int position) {
        //获取新闻中心的对象
        MainActivity mainActivity = (MainActivity) mActivity;
        //获取ContentFragment
        ContentFragment contentFragment = mainActivity.getContentFragment();
        //获取newsPager
        NewsPager newsPager = contentFragment.getNewsPager();
        //修改新闻中心的framelayout布局
        newsPager.setCurrentMenuDetailPager(position);


    }

    private void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.menu;
        slidingMenu.toggle();//如果当前状态是开，反之亦然

    }

    public class leftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsBeanData.size();
        }

        @Override
        public NewsBean.NewsBeanData getItem(int position) {
            return mNewsBeanData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.layout_item, null);
            TextView tv_item = view.findViewById(R.id.tv_item);
            NewsBean.NewsBeanData newsBeanData = getItem(position);
            tv_item.setText(newsBeanData.title);
            if (position == mCurrentItem) {
                tv_item.setEnabled(true);
            } else {
                tv_item.setEnabled(false);
            }
            return view;
        }
    }
}
