package com.example.wsq.android.activity;

/**
 * Created by wsq on 2017/12/11.
 */

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;

/**
 * 用于在注册的时候展示用户协议
 */
public class ProtocolsActivity extends BaseActivity{

    private WebView register_webView;
    private ImageView iv_back;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_protocols;
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {

        register_webView = this.findViewById(R.id.register_webView);

        iv_back = this.findViewById(R.id.iv_back);

        //声明WebSettings子类
        WebSettings webSettings = register_webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
//        webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        register_webView.loadUrl("http://xiyicontrol.com/api/xieyi");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        register_webView.destroy();
    }
}
