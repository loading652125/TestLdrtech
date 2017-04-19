package com.zjs.ldrtech.fragment;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.FragmentEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.adapter.DayFragmentAdapter;
import com.zjs.ldrtech.bean.GankDay;
import com.zjs.ldrtech.lruCache.DiskLruCacheManager;
import com.zjs.ldrtech.net.GankApi;
import com.zjs.ldrtech.view.SlideInOutRightItemAnimator;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2017/5/3 21:49
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
public class DayFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DayFragment";
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingtoolbarlayout)
    CollapsingToolbarLayout collapsingtoolbarlayout;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.day_recycleview)
    RecyclerView dayRecycleview;

    private int year;
    private int month;
    private int day;
    private LinearLayoutManager mLinearLayoutManager;
    private DayFragmentAdapter mDayFragmentAdapter;
    private DiskLruCacheManager mDiskLruCacheManager;
    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_day;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        initUI();
        mDayFragmentAdapter = new DayFragmentAdapter(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        load(year, month, day -1);
        load(year, month, day);
        try {
            mDiskLruCacheManager = new DiskLruCacheManager(getActivity(),"ldrtech");
            GankDay gankday = mDiskLruCacheManager.getAsSerializable(TAG);
            if (gankday != null) {
                loadUIByGankday(gankday);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        load(year, monthOfYear + 1, dayOfMonth);

    }
    public void load(final int year, final int month, final int day) {
        GankApi.getInstance().getWebService().getGoodsByDay(year,month,day)
                .compose(this.<GankDay>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankDay>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showSnackbar("错误");
                    }

                    @Override
                    public void onNext(GankDay gankDay) {
                        if (gankDay != null && gankDay.results != null &&
                                gankDay.results.Android != null) {
                            collapsingtoolbarlayout.setTitle(year + "年" + month + "月" + day + "日");
                            mDiskLruCacheManager.put(TAG, gankDay);
                            loadUIByGankday(gankDay);
                        }
                        else {
                            showSnackbar("今天可能没有更新");
                        }
                    }
                });
    }

    private void loadUIByGankday(GankDay gankDay) {
        Glide.with(DayFragment.this)
                .load(gankDay.results.福利.get(0).getUrl())
                .centerCrop()
                .crossFade()
                .error(R.drawable.haoqi)
                .into(backImg);
        mDayFragmentAdapter.setData(gankDay.results);
        dayRecycleview.setAdapter(mDayFragmentAdapter);
        dayRecycleview.setItemAnimator(new SlideInOutRightItemAnimator(dayRecycleview));
    }

    private void initUI() {
        collapsingtoolbarlayout.setTitle("今日热门");
        backImg.setImageResource(R.drawable.haoqi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarColor = ((AppCompatActivity)getActivity()).getWindow()
                    .getStatusBarColor();
            collapsingtoolbarlayout.setContentScrimColor(statusBarColor);
            if (statusBarColor != ((AppCompatActivity)getActivity())
                    .getWindow().getStatusBarColor()){
                ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                        ((AppCompatActivity) getActivity()).getWindow().getStatusBarColor(),
                        statusBarColor);
                statusBarColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatActivity) getActivity())
                                    .getWindow().setStatusBarColor(
                                            (int) animation.getAnimatedValue());
                        }
                    }
                });
                statusBarColorAnim.setDuration(1000L);
                statusBarColorAnim.setInterpolator(
                        new AccelerateInterpolator());
                statusBarColorAnim.start();
            }
        }
        dayRecycleview.setHasFixedSize(true);
        dayRecycleview.setItemAnimator(new DefaultItemAnimator());
        mLinearLayoutManager = new LinearLayoutManager(dayRecycleview.getContext());
        dayRecycleview.setLayoutManager(mLinearLayoutManager);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        DayFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
                datePickerDialog.show(getActivity().getFragmentManager(),
                        "DatePickerDialog");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
