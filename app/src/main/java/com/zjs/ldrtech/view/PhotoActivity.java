package com.zjs.ldrtech.view;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zjs.ldrtech.base.AppSwipeBackActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zjs.ldrtech.R;
import com.zjs.ldrtech.utils.ImageSave;

/**
 * Created by zjs on 2017/4/25 17:41
 * email:loading652125@163.com
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */
public class PhotoActivity extends AppSwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "PhotoActivity";
    private static final String URL = "URL";
    private PinchImageView pinchImageView;
    private ImageView mBtnBack;
    private ImageView mBtnSave;
    private String mUrl;
    private Bitmap mBitmap;
    public static void startActivity(AppCompatActivity activity,String url,View transitionView) {
        Intent intent = new Intent(activity,PhotoActivity.class);
        intent.putExtra(URL,url);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,transitionView,"image");
        ActivityCompat.startActivity(activity,intent,options.toBundle());
    }
    public static void startActivity1(Context context, String url) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(URL, url);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        pinchImageView = (PinchImageView) findViewById(R.id.meizi_image);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mBtnSave = (ImageView) findViewById(R.id.btn_save);
        mBtnBack.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        Glide.with(this)
                .load(mUrl)
                .asBitmap()
                .error(R.drawable.haoqi)
                .placeholder(R.drawable.haoqi)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        pinchImageView.setImageBitmap(resource);
                        mBitmap = resource;
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                overridePendingTransition(R.anim.out_to_bottom,0);
                break;
            case R.id.btn_save:
                Toast.makeText(this,"保存图片",Toast.LENGTH_SHORT).show();
                ImageSave.with(getApplicationContext())
                        .save(mBitmap)
                        .setImageSaveListener(new ImageSave.ImageSaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(PhotoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(PhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }

    }
}
