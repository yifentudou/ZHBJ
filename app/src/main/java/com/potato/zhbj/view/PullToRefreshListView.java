package com.potato.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.potato.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by li.zhirong on 2018/9/29/029 16:11
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private int measureHeight;
    private int startY = -1;
    private int endY;
    private int dY;
    private View headerView;
    private static final int PULL_TO_REFRESH = 1;//下拉刷新
    private static final int RELEASE_TO_REFERSH = 2;//刷新完成
    private static final int STATE_REGRESHING = 3;//正在刷新
    private int mCurrentState = PULL_TO_REFRESH;//默认情况
    private TextView tv_refresh_data;
    private ProgressBar progressBar;
    private ImageView iv_refresh;
    private TextView tv_refresh_tip;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View footerView;
    private int measureFooterHeight;


    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    public void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.layout_refresh_header, null);
        tv_refresh_data = headerView.findViewById(R.id.tv_refresh_data);
        tv_refresh_tip = headerView.findViewById(R.id.tv_refresh_tip);
        iv_refresh = headerView.findViewById(R.id.iv_refresh);
        progressBar = headerView.findViewById(R.id.progressBar);
        initAnimation();
        this.addHeaderView(headerView);
        //隐藏头布局
        headerView.measure(0, 0);//先测量
        measureHeight = headerView.getMeasuredHeight();                  //获得高度
        headerView.setPadding(0, -measureHeight, 0, 0); //隐藏
        getCurrentData();
    }

    /**
     * 加载更多页面
     */
    public void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.layout_refresh_footer, null);
        footerView.measure(0, 0);
        measureFooterHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, measureFooterHeight, 0, 0);
        this.setOnScrollListener(this);//滑动监听
        this.addFooterView(footerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当用户按住头条新闻得viewpager，viewpager事件会被viewpager消费掉，导致startY没有值，因此重新获取
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                if (mCurrentState == STATE_REGRESHING) {//如果正在刷新，跳出循环
                    break;
                }
                endY = (int) ev.getY();
                int firstItem = getFirstVisiblePosition();//必须是第一个item
                dY = endY - startY;
                if (dY > 0 && firstItem == 0) {//必须是下拉，并且是第一个item才显示下拉
                    int padding = dY - measureHeight;//计算当前控件下拉布局得padding值
                    headerView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != RELEASE_TO_REFERSH) {
                        //改为松开刷新
                        mCurrentState = RELEASE_TO_REFERSH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == RELEASE_TO_REFERSH) {
                    mCurrentState = STATE_REGRESHING;
                    refreshState();
                    //完整展示头布局
                    headerView.setPadding(0, 0, 0, 0);
                    //4.进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == PULL_TO_REFRESH) {
                    //隐藏头布局
                    headerView.setPadding(0, -measureHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnimation() {
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    /**
     * 根据当前状态来刷新页面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case PULL_TO_REFRESH:
                tv_refresh_tip.setText("下拉刷新");
                iv_refresh.startAnimation(animDown);
                progressBar.setVisibility(INVISIBLE);
                iv_refresh.setVisibility(VISIBLE);
                break;
            case RELEASE_TO_REFERSH:
                tv_refresh_tip.setText("松开刷新");
                iv_refresh.startAnimation(animUp);
                progressBar.setVisibility(INVISIBLE);
                iv_refresh.setVisibility(VISIBLE);
                break;
            case STATE_REGRESHING:
                tv_refresh_tip.setText("正在刷新...");
                iv_refresh.clearAnimation();//清除箭头动画，否则无法隐藏
                progressBar.setVisibility(VISIBLE);
                iv_refresh.setVisibility(INVISIBLE);
                break;
        }
    }

    /**
     * 定义成员变量，来接收监听事件
     */
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }
private boolean isLoadMore;
    //滑动状态发生改变
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE ) {//空闲时间
            int lastVisiableView = getLastVisiblePosition();
            if (lastVisiableView == getCount() - 1 && !isLoadMore) {//当前没有加载并且是最后一页
                Log.e("tag", "到底了");
                footerView.setPadding(0, 0, 0, 0);
                isLoadMore = true;
                setSelection(getCount() - 1);//将最后一个listview显示再最后一个item上，
                                            // 当显示到最后一个item时，显示加载更多，无需滑动
            //通知主界面加载下一页数据
                if(mListener != null){
                    mListener.onLoadMore();
                }
            }
        }
    }

    //滑动过程回掉
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
        public void onLoadMore();
    }

    /**
     * 6.刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {
        if(!isLoadMore){
        headerView.setPadding(0, -measureHeight, 0, 0);
        mCurrentState = PULL_TO_REFRESH;
        tv_refresh_tip.setText("下拉刷新");
        progressBar.setVisibility(INVISIBLE);
        iv_refresh.setVisibility(VISIBLE);
        if (success) {//只有成功得时候才去刷新时间
            getCurrentData();
        }}
        else{
            //加载更多
            footerView.setPadding(0,-measureFooterHeight,0,0);
            isLoadMore = false;
        }
    }

    /**
     * 获取时间，用于更新时间
     */
    public void getCurrentData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tv_refresh_data.setText(time);
    }
}
