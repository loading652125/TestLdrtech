package com.zjs.ldrtech.fragment;

import android.content.Intent;
import android.net.Uri;
import android.text.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.base.AppSwipeBackActivity;

import butterknife.BindView;

/**
 * Created by zjs on 2017/5/3 23:57
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
public class WebFragment extends BaseFragment {
    private static final String TAG = "WebFragment";
    public static final String DATA_ID = "data_id";
    public static final String DATA_TITLE = "data_title";
    public static final String DATA_URL = "data_url";
    public static final String DATA_TYPE = "data_type";
    public static final String DATA_WHO = "data_who";

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_view)
    WebView mWebView;

    Toolbar mToolbar;
    private String mId;
    private String mTitle;
    private String mUrl;
    private String mType;
    private String mWho;

    public static WebFragment newInstance(String id,String title, String url,
                                          String type,String who) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_ID,id);
        bundle.putSerializable(DATA_TITLE,title);
        bundle.putSerializable(DATA_URL,url);
        bundle.putSerializable(DATA_TYPE,type);
        bundle.putSerializable(DATA_WHO,who);
        WebFragment webFragment = new WebFragment();
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_web;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mToolbar = (Toolbar) mContaninView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mId = getArguments().getString(DATA_ID);
        mTitle = getArguments().getString(DATA_TITLE);
        mUrl = getArguments().getString(DATA_URL);
        mType = getArguments().getString(DATA_TYPE);
        mWho = getArguments().getString(DATA_WHO);
        initWebView();
        mWebView.loadUrl(mUrl);
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressBar.setProgress(0);
                    }
                } else {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    }
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_web,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_url:
                ClipboardManager clipBoardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                clipBoardManager.setText(mUrl);
                break;
            case R.id.action_open_in_brower:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                getActivity().startActivity(intent);
                break;
            case R.id.action_share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT,mUrl);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(Intent.createChooser(intent1,mTitle));
                break;
            case android.R.id.home:
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    ((AppSwipeBackActivity)getActivity()).scrollToFinishActivity();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
