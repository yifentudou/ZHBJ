package com.potato.zhbj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
        initSlidingMenu();
        initFragment();

    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.leftMenu, getLeftFragment(),TAG_LEFT_MENU);
        transaction.replace(R.id.content, getContentFragment(),TAG_CONTENT);
        transaction.commit();
    }

    private void initSlidingMenu() {
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

    private Fragment getLeftFragment() {
        return new LeftMenuFragment();
    }

    private Fragment getContentFragment() {
        return new ContentFragment();
    }
}
