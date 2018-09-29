package com.potato.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.potato.zhbj.R;
import com.potato.zhbj.utils.PrefUtil;

import org.w3c.dom.Text;

/**
 * Created by li.zhirong on 2018/9/29/029 16:11
 */
public class PullToRefreshListView extends ListView {
    private int measureHeight;
    private int startY = -1;
    private int endY;
    private int dY;
    private View view;
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


    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     * 初始化头布局
     */
    public void initHeaderView() {
        view = View.inflate(getContext(), R.layout.layout_refresh, null);
        tv_refresh_data = view.findViewById(R.id.tv_refresh_data);
        tv_refresh_tip = view.findViewById(R.id.tv_refresh_tip);
        iv_refresh = view.findViewById(R.id.iv_refresh);
        progressBar = view.findViewById(R.id.progressBar);
        initAnimation();
        this.addHeaderView(view);
        //隐藏头布局
        view.measure(0, 0);//先测量
        measureHeight = view.getMeasuredHeight();                  //获得高度
        view.setPadding(0, -measureHeight, 0, 0); //隐藏

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
                    view.setPadding(0, padding, 0, 0);
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
                    view.setPadding(0, 0, 0, 0);
                    //4.进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == PULL_TO_REFRESH) {
                    //隐藏头布局
                    view.setPadding(0, -measureHeight, 0, 0);
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
                iv_refresh.setAnimation(animDown);
                progressBar.setVisibility(INVISIBLE);
                iv_refresh.setVisibility(VISIBLE);
                break;
            case RELEASE_TO_REFERSH:
                tv_refresh_tip.setText("松开刷新");
                iv_refresh.setAnimation(animUp);
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

    /**
     * 1.下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 6.刷新结束，收起控件
     */
    public void onRefreshComplete() {
        view.setPadding(0, -measureHeight, 0, 0);
        mCurrentState = PULL_TO_REFRESH;
        tv_refresh_tip.setText("下拉刷新");
        progressBar.setVisibility(INVISIBLE);
        iv_refresh.setVisibility(VISIBLE);
    }
}
