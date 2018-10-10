package com.potato.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.potato.zhbj.utils.PrefUtil;

/**
 * Created by li.zhirong on 2018/10/9/009 10:30
 */
public class WebActivity extends AppCompatActivity implements View.OnClickListener {
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

        btn_back.setOnClickListener(this);
        btn_textsize.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        btn_back.setVisibility(View.VISIBLE);
        ll_share.setVisibility(View.VISIBLE);
        btn_menu.setVisibility(View.GONE);
        mUrl = getIntent().getStringExtra("url");
        webView.loadUrl(mUrl);
        setting = webView.getSettings();
        initWebView();


    }

    /**
     * 初始化webview相关设置
     */
    private void initWebView() {
        //默认显示
        currentItem = PrefUtil.getInt(this, "CHOOSE_TEXTSIZE", 2);
        chooseDefaultTextsize(currentItem);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                break;
            case R.id.btn_textsize:
                //展示选择字体得弹窗
                showChooseDialog();
                break;
        }
    }

    /**
     * 字体设置弹框
     */
    private int chooseItem;//纪录临时选中得字体大小
    private int currentItem;//纪录最终选中得字体大小

    public static int TEXT_SIZE_LARGEST = 140;
    public static int TEXT_SIZE_LARGE = 120;
    public static int TEXT_SIZE_MEDIUM = 100;
    public static int TEXT_SIZE_SMALL = 80;
    public static int TEXT_SIZE_SMALLEST = 60;

    private void showChooseDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体选择");
        String[] textSizes = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(textSizes, currentItem, (dialog, which) -> {
            chooseItem = which;
        });
        builder.setPositiveButton("确定", (dialog, which) -> {
            switch (chooseItem) {
                case 0:
                    setting.setTextZoom(TEXT_SIZE_LARGEST);
                    break;
                case 1:
                    setting.setTextZoom(TEXT_SIZE_LARGE);
                    break;
                case 2:
                    setting.setTextZoom(TEXT_SIZE_MEDIUM);
                    break;
                case 3:
                    setting.setTextZoom(TEXT_SIZE_SMALL);
                    break;
                case 4:
                    setting.setTextZoom(TEXT_SIZE_SMALLEST);
                    break;
                default:
                    setting.setTextZoom(TEXT_SIZE_MEDIUM);
                    break;
            }
            currentItem = chooseItem;
            PrefUtil.setInt(this, "CHOOSE_TEXTSIZE", currentItem);
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }

    private void chooseDefaultTextsize(int Item) {
        switch (Item) {
            case 0:
                setting.setTextZoom(TEXT_SIZE_LARGEST);
                break;
            case 1:
                setting.setTextZoom(TEXT_SIZE_LARGE);
                break;
            case 2:
                setting.setTextZoom(TEXT_SIZE_MEDIUM);
                break;
            case 3:
                setting.setTextZoom(TEXT_SIZE_SMALL);
                break;
            case 4:
                setting.setTextZoom(TEXT_SIZE_SMALLEST);
                break;
            default:
                setting.setTextZoom(TEXT_SIZE_MEDIUM);
                break;
        }
    }
}
