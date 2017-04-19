package com.zjs.ldrtech.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zjs on 2017/4/18 23:34
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

/*
 *AppIntro中布局使用fragment,
 * 如果是图片直接返回全屏图片
 * 如果是布局则inflate返回
 */
public class IntroSlideUtil extends Fragment {

    private static final String IMAGE_RES_ID = "imageResId";
    private int imageId;
    private static final String LAYOUT_RES_ID = "layoutResId";
    private int layoutId;
    private static final String FLAG_LAYOUT = "flag";
    private boolean flag = false;

    public static IntroSlideUtil newInstance(int imageResId) {
        IntroSlideUtil sample = new IntroSlideUtil();

        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID, imageResId);
        sample.setArguments(args);
        return sample;
    }

    public static IntroSlideUtil newInstance(int layoutResId, boolean isLayout) {
        IntroSlideUtil sample = new IntroSlideUtil();

        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES_ID, layoutResId);
        args.putBoolean(FLAG_LAYOUT, isLayout);
        sample.setArguments(args);
        return sample;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(IMAGE_RES_ID)) {
            imageId = getArguments().getInt(IMAGE_RES_ID);
        } else if (getArguments() != null && getArguments().containsKey(LAYOUT_RES_ID)) {
            layoutId = getArguments().getInt(LAYOUT_RES_ID);
            flag = getArguments().getBoolean(FLAG_LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (flag) {
            return inflater.inflate(layoutId, container, false);
        } else {
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(imageId).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Log.e("tag", getContext().toString());
            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }
    }
}
