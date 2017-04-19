package com.zjs.ldrtech.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.trello.rxlifecycle.FragmentEvent;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.adapter.MeiZiAdapter;
import com.zjs.ldrtech.bean.Data;
import com.zjs.ldrtech.lruCache.DiskLruCacheManager;
import com.zjs.ldrtech.net.GankApi;
import com.zjs.ldrtech.view.SlideInOutRightItemAnimator;
import com.zjs.ldrtech.view.SwipeToRefreshLayout;

import java.io.IOException;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by zjs on 2017/4/25 11:17
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
public class MeiziFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MeiziFragment";
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_ly)
    SwipeToRefreshLayout mSwipeRefreshLayout;
    private MeiZiAdapter mAdapter;
    private StaggeredGridLayoutManager mstaggeredGridLayoutManager;

    //当前页数
    private int currentPager = 1;
    //是否刷新状态
    private boolean isLoadingNewData = false;
    //是否载入更多
    private boolean isLoadingMore = false;
    //是否载入全部
    private boolean isAllLoad = false;
    private DiskLruCacheManager mdiskLruCacheManager;



    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        mToolBar.setTitle("妹子");
        mToolBar.setTitleTextColor(Color.WHITE);
        initRecylerView(mRecyclerView);
        initSwipeRefreshLayout(mSwipeRefreshLayout);
        mAdapter = new MeiZiAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new SlideInOutRightItemAnimator(mRecyclerView));
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        try {
            Log.i(TAG, "DiskLruCacheManager 创建");
            mdiskLruCacheManager = new DiskLruCacheManager(getActivity());
            Data data = mdiskLruCacheManager.getAsSerializable(TAG);
            Log.i(TAG, "DiskLruCacheManager 读取");
            if (data != null){
                Log.i(TAG, "获取到缓存数据");
                mAdapter.setData(data.getResults());
                mAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        onRefresh();
    }
	private void initRecylerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mstaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mstaggeredGridLayoutManager);
    }

    private void initSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout) {
        Resources resources = getResources();
        mSwipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.blue_dark),
                resources.getColor(R.color.red_dark),
                resources.getColor(R.color.yellow_dark),
                resources.getColor(R.color.green_dark)
        );
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = false;
        isLoadingNewData = true;
        loadData(1);
		
    }
	int[] lastPositions;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //但RecyclerView滑动到倒数第三个之请求加载更多
            if (lastPositions == null) {
                lastPositions = new int[mstaggeredGridLayoutManager.getSpanCount()];
            }
            int[] lastVisibleItem = mstaggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            int totalItemCount = mAdapter.getItemCount();
            // dy>0 表示向下滑动
            if (lastVisibleItem[0] >= totalItemCount - 4 && dy > 0 && !isLoading() && !isAllLoad) {
                requestMoreData();
            }
        }
    };
	private boolean isLoading() {
        return isLoadingMore || isLoadingNewData;
    }
    private void requestMoreData(){
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = true;
        isLoadingNewData = false;
        loadData(++currentPager);
    }

    private void loadData(int pager) {
        GankApi.getInstance()
                .getWebService()
                .getBenefitsGoods(GankApi.LOAD_LIMIT,pager)
                .compose(this.<Data>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataObservable);
    }

    

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_meizi;
    }




    private Observer<Data> dataObservable = new Observer<Data>(){

        @Override
        public void onCompleted() {

            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingNewData = false;
            isLoadingMore = false;
        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingNewData = false;
            isLoadingMore = false;
            showSnackbar("OnError");
        }

        @Override
        public void onNext(Data data) {
            if(data != null && data.getResults() != null){
                /**
                 * 没有更多数据
                 */
                if (data.getResults().size() < GankApi.LOAD_LIMIT) {
                    isAllLoad = true;
                    showSnackbar("no_more");
                }

                if(isLoadingMore){
                    mAdapter.addData(data.getResults());
                } else if (isLoadingNewData) {
                    isAllLoad = false;
                    mAdapter.setData(data.getResults());
                    mdiskLruCacheManager.put(TAG,data);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    
    

}
