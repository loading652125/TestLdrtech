package com.zjs.ldrtech.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro2;
import com.zjs.ldrtech.MainActivity;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.utils.SlideImageUtil;
import com.zjs.ldrtech.utils.SharePrefereceTool;

public class MaterialGuideActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppIntro do not need setContentView
//        setContentView(R.layout.material_guide1);
        addSlide(SlideImageUtil.newInstance(R.drawable.guide1));
        addSlide(SlideImageUtil.newInstance(R.drawable.guide2));
        addSlide(SlideImageUtil.newInstance(R.drawable.guide3));
        addSlide(SlideImageUtil.newInstance(R.drawable.guide4));
        showSkipButton(true);
        setVibrate(true);
        setVibrateIntensity(30);


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        startActivity(new Intent(MaterialGuideActivity.this, MainActivity.class));
        SharePrefereceTool.setPrefBoolean(MaterialGuideActivity.this, "guide_showed", true);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        startActivity(new Intent(MaterialGuideActivity.this, MainActivity.class));
        SharePrefereceTool.setPrefBoolean(MaterialGuideActivity.this, "guide_showed", true);
        finish();
    }
}
