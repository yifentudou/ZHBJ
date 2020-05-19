package com.potato.zhbj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.potato.zhbj.R;
import com.potato.zhbj.fragment.ContentFragment;
import com.potato.zhbj.fragment.LeftMenuFragment;

/**
 * Created by li.zhirong on 2018/9/11/011 14:03
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG_LEFT_MENU = "left_menu_fragment";
    public static final String TAG_CONTENT = "content_fragment";
    public SlidingMenu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // configure the SlidingMenu
        Log.e("lee","this is a new branch");

        initSlidingMenu();
        initFragment();

    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.leftMenu, LeftFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.content, ContentFragment(), TAG_CONTENT);
        transaction.commit();
    }

    private void initSlidingMenu() {
        // 200 / 320 * width
        WindowManager manager = getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_left_menu_width);
        menu.setShadowDrawable(R.drawable.slidingmenu_shadow_left_menu);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidingmenu_frame_left_menu);
    }

    //获取leftfragment对象
    public LeftMenuFragment getLeftFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }
    //获取ContentFragment对象
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }

    private Fragment LeftFragment() {
        return new LeftMenuFragment();
    }

    private Fragment ContentFragment() {
        return new ContentFragment();
    }
}
