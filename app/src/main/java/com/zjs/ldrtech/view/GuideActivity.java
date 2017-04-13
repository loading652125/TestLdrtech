package com.zjs.ldrtech.view;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.zjs.ldrtech.MainActivity;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.utils.SharePrefereceTool;
import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private ViewPager guideViewpager;
    private LinearLayout llpointGroup;
    private Button btnStart;
    private int mPointWidth;
    ArrayList<ImageView> mImageViewList;
    private static final int[] gImage = new int[]{R.drawable.splash2,R.drawable.splash3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guideViewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        llpointGroup = (LinearLayout) findViewById(R.id.ll_point_grounp);
        btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePrefereceTool.setPrefBoolean(GuideActivity.this,"guide_showed",true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        initView();
        guideViewpager.setAdapter(new GuideAdapter());
        guideViewpager.addOnPageChangeListener(new GuidePageListener());
    }

    private void initView() {
        mImageViewList = new ArrayList<>();
        for(int i = 0; i < gImage.length; i++){
            ImageView image = new ImageView(this);
            Glide.with(this).load(gImage[i]).into(image);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViewList.add(image);
        }
    }
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return gImage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    class GuidePageListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == gImage.length -1){
                btnStart.setVisibility(View.VISIBLE);
            }else{
                btnStart.setVisibility(View.INVISIBLE);
            }
            Log.e("pagernumber",position+"");

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
