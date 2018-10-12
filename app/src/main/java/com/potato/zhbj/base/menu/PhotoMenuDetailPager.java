package com.potato.zhbj.base.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.potato.zhbj.Constants;
import com.potato.zhbj.R;
import com.potato.zhbj.base.BaseMenuDetailPager;
import com.potato.zhbj.bean.PhotosBean;
import com.potato.zhbj.utils.BitmapCacheUtils;
import com.potato.zhbj.utils.CacheUtil;
import com.potato.zhbj.utils.PrefUtil;

import java.util.List;

/**
 * Created by li.zhirong on 2018/9/14/014 17:41
 * 菜单详情页--组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {
    GridView gridView;
    ListView listView;
    List<PhotosBean.DataBean.NewsBean> photosNews;
    ImageButton btn_photos;

    public PhotoMenuDetailPager(Activity mActivity, ImageButton btn_photos) {
        super(mActivity);
        btn_photos.setOnClickListener(this);
        this.btn_photos = btn_photos;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_photos, null);
        listView = view.findViewById(R.id.listview);
        gridView = view.findViewById(R.id.gridview);

//        TextView tv = new TextView(mActivity);
//        tv.setText("菜单详情页--组图");
//        tv.setTextColor(Color.RED);
//        tv.setTextSize(22);
//        tv.setGravity(Gravity.CENTER);

        return view;
    }

    @Override
    public void initData() {
        String cache = PrefUtil.getString(mActivity, Constants.PHOTOS_URL, "");
        if (TextUtils.isEmpty(cache)) {
            getDataFromServer();
        } else {
            getGsonData(cache);
        }

    }

    private void getDataFromServer() {
        OkGo.<String>get(Constants.PHOTOS_URL).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("tag", "组图集请求得成功信息：" + response.body());
                getGsonData(response.body());
                //写缓存
                CacheUtil.setCache(Constants.PHOTOS_URL, response.body(), mActivity);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("tag", "组图集请求得失败信息：" + response.message());
                Toast.makeText(mActivity, response.message(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getGsonData(String body) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(body, PhotosBean.class);
        photosNews = photosBean.getData().getNews();
        if (photosNews != null) {
            listView.setAdapter(new PhotoAdapter());
            gridView.setAdapter(new PhotoAdapter());
        }
    }

    private boolean isListView = true;//标记当前是否是ListView

    @Override
    public void onClick(View v) {
        if (isListView) {
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            btn_photos.setImageResource(R.drawable.ic_list_type);
            isListView = false;
        } else {
            listView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            btn_photos.setImageResource(R.drawable.ic_grid_type);
            isListView = true;
        }
    }

    class PhotoAdapter extends BaseAdapter {
        BitmapCacheUtils bitmapCacheUtils;

        public PhotoAdapter() {
            bitmapCacheUtils = new BitmapCacheUtils();
        }

        @Override
        public int getCount() {
            return photosNews.size();
        }

        @Override
        public PhotosBean.DataBean.NewsBean getItem(int position) {
            return photosNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_big_pic, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_image = convertView.findViewById(R.id.iv_image);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PhotosBean.DataBean.NewsBean item = getItem(position);
            viewHolder.tv_title.setText(item.getTitle());
            String urls = item.getListimage().replace("10.0.2.2", "192.168.50.245");
            //自定义三级缓存图片加载
            bitmapCacheUtils.display(viewHolder.iv_image, urls);
//            Glide.with(mActivity).load(urls).placeholder(R.drawable.ic_pic_default).into(viewHolder.iv_image);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView iv_image;
        public TextView tv_title;
    }
}
