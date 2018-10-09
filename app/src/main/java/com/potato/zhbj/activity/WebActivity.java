package com.potato.zhbj.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.potato.zhbj.R;

/**
 * Created by li.zhirong on 2018/10/9/009 10:30
 */
public class WebActivity extends AppCompatActivity {
    private ImageButton btn_back;
    private ImageButton btn_share;
    private ImageButton btn_textsize;
    private LinearLayout ll_share;
    private WebView webView;
    private ImageButton btn_menu;
    private WebSettings setting;
    private ProgressBar progressBar;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        btn_back = findViewById(R.id.btn_back);
        btn_share = findViewById(R.id.btn_share);
        btn_textsize = findViewById(R.id.btn_textsize);
        btn_menu = findViewById(R.id.btn_menu);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webview);
        ll_share = findViewById(R.id.ll_share);
        btn_back.setVisibility(View.VISIBLE);
        ll_share.setVisibility(View.VISIBLE);
        btn_menu.setVisibility(View.GONE);
        mUrl = getIntent().getStringExtra("url");
        webView.loadUrl(mUrl);
        setting = webView.getSettings();
        setting.setBuiltInZoomControls(true);//显示缩放按钮
        setting.setUseWideViewPort(true);//支持双击缩放
        setting.setJavaScriptEnabled(true);//打开支持js
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("tag", "onPageStarted: ");
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("tag", "onPageFinished: ");
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("tag", "shouldOverrideUrlLoading: ");
                //在跳转连接时，强制在webview中跳转
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webView.loadUrl(String.valueOf(request.getUrl()));
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("tag", "onProgressChanged:进度 " + newProgress);
            }

            //网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("tag", "onProgressChanged: 网页标题" + title);
            }
        });

    }
}
