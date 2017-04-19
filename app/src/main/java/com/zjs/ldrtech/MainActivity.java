package com.zjs.ldrtech;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.zjs.ldrtech.constants.AppConstant;
import com.zjs.ldrtech.fragment.DayFragment;
import com.zjs.ldrtech.fragment.MeiziFragment;
import com.zjs.ldrtech.utils.SharePrefereceTool;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int mCurrentUIIndex = 0;
    private static final int TODAYUI = 0;
    private static final int MEIZITUUI = 1;
    private Fragment mdayFragment;
    private Fragment mMeiZiFragment;
    private Fragment mCurrentFragment;
    private long firstKeyDown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateUI();

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //白天模式
            case R.id.light:
                SharePrefereceTool.setBoolean(MainActivity.this, AppConstant.ISNIGHT, false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
            //夜间模式
            case R.id.night:
                SharePrefereceTool.setBoolean(MainActivity.this, AppConstant.ISNIGHT, true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                break;
            //今日推荐
            case R.id.today:
                mCurrentUIIndex = TODAYUI;
                item.setChecked(true);
                updateUI();
                break;
            //妹子图
            case R.id.meizi:
                mCurrentUIIndex = MEIZITUUI;
                item.setChecked(true);
                updateUI();
                break;
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void updateUI() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (mCurrentUIIndex) {
            case MEIZITUUI:
                if (mMeiZiFragment == null) {
                    mMeiZiFragment = new MeiziFragment();
                }
                switchFragment(mMeiZiFragment);
                break;
            case TODAYUI:
                if (mdayFragment == null) {
                    mdayFragment = new DayFragment();
                }
                switchFragment(mdayFragment);
                break;
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.content, fragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if ((System.currentTimeMillis() - firstKeyDown) > 2000) {
                    Toast.makeText(getApplicationContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
                    firstKeyDown = System.currentTimeMillis();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else {
                       finish();
                    }
                    overridePendingTransition(0, R.anim.out_to_bottom);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
