package com.potato.zhbj.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.potato.zhbj.R;
import com.potato.zhbj.utils.PrefUtil;

public class SplashActivity extends AppCompatActivity {
public static final String  IS_FIRST_ENTER ="is_first_enter";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout rl_root = findViewById(R.id.rl_root);
        //旋转动画
        RotateAnimation animation1 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(1000);//动画时间
        animation1.setFillAfter(true);//保持动画结束状态
//        //缩放动画
        ScaleAnimation animation2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
//        渐变动画
        AlphaAnimation animation3 = new AlphaAnimation(0, 1);
        animation3.setDuration(1000);
        animation3.setFillAfter(true);
        //动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        set.addAnimation(animation3);
        //启动动画
        rl_root.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirst = PrefUtil.getBoolean(SplashActivity.this, IS_FIRST_ENTER, true);
                Intent intent;
                if (isFirst) {
                    intent = new Intent(getApplicationContext(), GuideActivity.class);

                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
