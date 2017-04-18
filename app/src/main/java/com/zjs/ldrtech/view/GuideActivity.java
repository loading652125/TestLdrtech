package com.zjs.ldrtech.view;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zjs.ldrtech.MainActivity;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.utils.DensityUtil;
import com.zjs.ldrtech.utils.SharePrefereceTool;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private static final int[] guideImage = new int[]{R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3, R.drawable.guide4};
    ArrayList<ImageView> guideImageList = new ArrayList<>();
    private LinearLayout llPointGroup;
    private ViewPager viewPager;
    private ImageView ivWhitePoint;
    private int leftMax;
    private Button btSkip;
    private Button btStartNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        ivWhitePoint = (ImageView) findViewById(R.id.iv_white_point);
        btSkip = (Button) findViewById(R.id.bt_skip);
        btStartNow = (Button) findViewById(R.id.bt_start_now);
    }

    private void initData() {
        final int widthdpi = DensityUtil.dip2px(this, 10f);
        for (int i = 0; i < guideImage.length; i++) {
            ImageView point = new ImageView(GuideActivity.this);
            point.setBackgroundResource(R.drawable.circle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);
            if (i != 0) {
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            llPointGroup.addView(point);
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(GuideActivity.this).load(guideImage[i]).asBitmap().into(imageView);
            guideImageList.add(imageView);
        }
    }

    private void initListener() {
        ivWhitePoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListen());
        viewPager.setAdapter(new GuideAdapter());
        viewPager.addOnPageChangeListener(new MyOnpagerChangeListener());
        btSkip.setOnClickListener(new MyButtonOnclickListener());
        btStartNow.setOnClickListener(new MyButtonOnclickListener());
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guideImage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(guideImageList.get(position));
            return guideImageList.get(position);
        }
    }

    private class MyOnGlobalLayoutListen implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            ivWhitePoint.getViewTreeObserver()
                    .removeOnGlobalLayoutListener(MyOnGlobalLayoutListen.this);
            leftMax = llPointGroup.getChildAt(1).getLeft()
                    - llPointGroup.getChildAt(0).getLeft();
        }
    }

    private class MyOnpagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int leftMargin = (int) (position * leftMax + (positionOffset * leftMax));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivWhitePoint
                    .getLayoutParams();
            params.leftMargin = leftMargin;
        }

        @Override
        public void onPageSelected(int position) {
            if (position == guideImage.length - 1) {
                btSkip.setVisibility(View.INVISIBLE);
                btStartNow.setVisibility(View.VISIBLE);
            } else {
                btSkip.setVisibility(View.VISIBLE);
                btStartNow.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            SharePrefereceTool.setPrefBoolean(GuideActivity.this, "guide_showed", true);
            finish();
        }
    }

}
