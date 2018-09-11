package com.potato.zhbj.activity;

import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.potato.zhbj.R;

import java.util.ArrayList;

/**
 * Created by li.zhirong on 2018/9/11/011 13:57
 * 新手引导页
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager vp_guide;
    private ArrayList<ImageView> mImagesList;
    //引导页图片id
    private int[] mImagesId = new int[]{R.mipmap.ic_guide1, R.mipmap.ic_guide2, R.mipmap.ic_guide3};
    private LinearLayout ll_container;
    private ImageView iv_point_red;
    private int pointDis;
    private Button btn_guide_enter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vp_guide = findViewById(R.id.vp_guide);
        ll_container = findViewById(R.id.ll_container);
        iv_point_red = findViewById(R.id.iv_point_red);
        btn_guide_enter = findViewById(R.id.btn_guide_enter);
        initData();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动中的回调
                Log.e("dis", "当前位置：" + position + "位置偏移百分比：" + positionOffset);
                //更新小红点距离
                int marginLeft = (int) (pointDis * positionOffset) + position * pointDis;//计算小红点当前的距离
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_point_red.getLayoutParams();
                params.leftMargin = marginLeft;//修改左边距
                iv_point_red.setLayoutParams(params);//重新设置布局参数
            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中
                if (position == mImagesList.size() - 1){
                    btn_guide_enter.setVisibility(View.VISIBLE);
                }else{
                    btn_guide_enter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态发生变化的问题
            }
        });

        //测量两个小原点之间的距离，
        //onMeasure -> Layout(位置确定) ->onDraw
        //setLayoutContent未完成之前不能Layout(位置确定)
        //pointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
        //Log.e("dis", "setLayoutContent未完成时，Layout不能(位置确定)，距离 = " + pointDis);

        //视图树，在监听layout方法完成后，进行测量距离
        iv_point_red.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //layout会执行多次，只测一次
                iv_point_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
                Log.e("dis", "setLayoutContent完成时，监听Layout(位置确定)后，距离 = " + pointDis);
            }
        });
    }

    //初始化数据
    private void initData() {
        mImagesList = new ArrayList<>();
        for (int i = 0; i < mImagesId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImagesId[i]);
            mImagesList.add(imageView);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);
            //初始化布局参数，布局包裹内容，父控件是谁，就是谁声明的布局参数
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                lp.leftMargin = 10; //从第二个点开始设置左边距
            }
            point.setLayoutParams(lp);//设置布局参数
            ll_container.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImagesList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        //初始化item
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = mImagesList.get(position);
            container.addView(view);
            return view;
        }

        //销毁item
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
