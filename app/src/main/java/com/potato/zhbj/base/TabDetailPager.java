package com.potato.zhbj.base;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.potato.zhbj.R;
import com.potato.zhbj.bean.NewsBean;
import com.potato.zhbj.bean.NewsTabBean;
import com.potato.zhbj.utils.CacheUtil;
import com.potato.zhbj.utils.PrefUtil;
import com.potato.zhbj.view.PullToRefreshListView;
import com.potato.zhbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.potato.zhbj.Constants.SERVER_URL;

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
    private PullToRefreshListView listview;
    private ArrayList<NewsTabBean.TabNewsBean> newsList;
    private String mMoreUrl;
    private NewsListAdapter listAdapter;

    public TabDetailPager(Activity mActivity) {
        super(mActivity);
    }

    public TabDetailPager(Activity mActivity, NewsBean.TabData tabData) {
        super(mActivity);
        mTabData = tabData;
        mUrll = SERVER_URL + mTabData.url;
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
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);

        View itemListHeader = View.inflate(mActivity, R.layout.list_item_header, null);
        viewPager = (TopNewsViewPager) itemListHeader.findViewById(R.id.viewPager);
        tv_topic_title = itemListHeader.findViewById(R.id.tv_topic_title);
        tabIndicator = itemListHeader.findViewById(R.id.tabIndicator);
        listview.addHeaderView(itemListHeader);
        //5.前端界面设置回调
        listview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                //判断是否有下一页数据
                if (!TextUtils.isEmpty(mMoreUrl)) {
                    //有下一页数据
                    getMoreDataFromServer();
                } else {
                    //没有更多数据时，隐藏控件
                    listview.onRefreshComplete(false);
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_LONG).show();
                }
            }
        });
        listview.setOnItemClickListener((parent, view1, position, id) -> {
                    int headNum = listview.getHeaderViewsCount();//去掉头布局数量
                    Log.e("tag", "第" + (position - headNum) + "item被点击了");
                    NewsTabBean.TabNewsBean newItem = newsList.get(position);
                    //纪录阅读id
                    String read_id = PrefUtil.getString(mActivity, "read_id", "");
                    if (!read_id.contains(newItem.id + "")) {
                        read_id = read_id + newItem.id + ",";
                        PrefUtil.setString(mActivity, "read_id", read_id);
                    }
                    //要将被点击得item文字颜色改为灰色，view对象就是当前被点击得布局
                    TextView tv_text = view1.findViewById(R.id.tv_text);//性能高
                    tv_text.setTextColor(Color.GRAY);
//                    listAdapter.notifyDataSetChanged();//性能低
                }
        );
        return view;

    }

    /**
     * 获取下一页数据
     */
    private void getMoreDataFromServer() {
        OkGo.<String>get(mMoreUrl).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("tag", "获取下一页tabDetailPager页面的URL成功");
                getGsonData(response.body(), true);
//                CacheUtil.setCache(mUrll, response.body(), mActivity);
                listview.onRefreshComplete(true);//收起下拉刷新控件
            }

            @Override
            public void onError(Response<String> response) {
                Log.e("tag", "获取下一页tabDetailPager页面的URL失败");
                //   Log.e("tag", response.message());
                Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_LONG).show();
                listview.onRefreshComplete(false);//收起下拉刷新控件
            }
        });
    }

    @Override
    public void initData() {
//        tv.setText(mTabData.title);
        String isCache = CacheUtil.getCache(mUrll, mActivity);
        if (!TextUtils.isEmpty(isCache)) {
            getGsonData(isCache, false);
        } else {
            getDataFromServer();
        }


    }

    private void getDataFromServer() {
        OkGo.<String>get(mUrll).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("tag", "获取tabDetailPager页面的URL成功");
                getGsonData(response.body(), false);
                CacheUtil.setCache(mUrll, response.body(), mActivity);
                listview.onRefreshComplete(true);//收起下拉刷新控件
            }

            @Override
            public void onError(Response<String> response) {
                Log.e("tag", "获取tabDetailPager页面的URL失败");
                //   Log.e("tag", response.message());
                Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_LONG).show();
                listview.onRefreshComplete(false);//收起下拉刷新控件
            }
        });
    }

    private void getGsonData(String body, boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(body, NewsTabBean.class);
        newsTop = newsTabBean.data.topnews;
        String moreUrll = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrll)) {
            mMoreUrl = SERVER_URL + moreUrll;
        } else {
            mMoreUrl = null;
        }
        if (!isMore) {
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
            //新闻列表
            newsList = newsTabBean.data.news;
            if (newsList != null) {
                listAdapter = new NewsListAdapter();
                listview.setAdapter(listAdapter);
            }
        } else {
            ArrayList<NewsTabBean.TabNewsBean> moreNews = newsTabBean.data.news;
            newsList.addAll(moreNews);
            listAdapter.notifyDataSetChanged();
        }

    }

    class NewsListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public NewsTabBean.TabNewsBean getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.layout_left_image, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_img = convertView.findViewById(R.id.iv_img);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_text);
                viewHolder.tv_data = convertView.findViewById(R.id.tv_data);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.TabNewsBean news = getItem(position);
            //模拟器用不起10.0.2.2,使用本机ip模拟
            String urls = news.listimage.replace("10.0.2.2", "192.168.50.245");
            viewHolder.tv_title.setText(news.title);
            viewHolder.tv_data.setText(news.pubdate);
            String read_id = PrefUtil.getString(mActivity, "read_id", "");
            //历史纪录
            if(read_id.contains(news.id + "")){
                viewHolder.tv_title.setTextColor(Color.GRAY);
            }else{
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

            Glide.with(mActivity).load(urls).error(R.drawable.ic_pic_default).into(viewHolder.iv_img);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView iv_img;
        public TextView tv_title;
        public TextView tv_data;
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
            String imageUrl = newsTop.get(position).topimage.replace("10.0.2.2", "192.168.50.245");
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
