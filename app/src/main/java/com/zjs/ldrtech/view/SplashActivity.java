package com.zjs.ldrtech.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zjs.ldrtech.MainActivity;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.utils.SharePrefereceTool;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.splash1).into(imageView);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//        startAmin();
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            public void run(){
                boolean userGuide = SharePrefereceTool.getPrefBoolean(SplashActivity.this,"guide_showed",
                        false);
                if (!userGuide){
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                } else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        };
        timer.schedule(task, 2000);


    }
    /*
    splashActivit动画，显示效果不佳
     */
    private void startAmin() {
        AnimationSet animSet = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(1500);
        scale.setFillAfter(true);

        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(1500);
        alpha.setFillAfter(true);
        animSet.addAnimation(scale);
        animSet.addAnimation(alpha);
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPaper();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relativeLayout.startAnimation(animSet);
    }
    public void jumpNextPaper(){
        boolean userGuide = SharePrefereceTool.getPrefBoolean(SplashActivity.this,"guide_showed",
                false);
        if (!userGuide){
            startActivity(new Intent(SplashActivity.this,GuideActivity.class));
        } else{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }
}
