package com.example.wsq.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;


/**
 * Created by wsq on 2017/12/18.
 */

public class ProductInfoActivity extends BaseActivity{

    private WebView register_webView;
    private TextView tv_title;
    private ImageView iv_back;
    private SharedPreferences shared;
    private OrderTaskService orderTaskService;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_protocols;
    }

    @Override
    public void init() {

        orderTaskService = new OrderTaskServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void initView() {

        register_webView = this.findViewById(R.id.register_webView);
        tv_title = this.findViewById(R.id.tv_title);
        iv_back = this.findViewById(R.id.iv_back);

        tv_title.setText("资料详情");

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

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String url = Urls.HOST+Urls.GET_DETAIL+"?"+ResponseKey.TOKEN+"="+shared.getString(Constant.SHARED.TOKEN, "")
                +"&";
        if (getIntent().hasExtra(CollectActivity.COLLECT)){
            url += ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ARTICLE_ID,0);
        }else{
            url += url+ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ID,0);
        }
//        if (getIntent().getStringExtra(CollectActivity.COLLECT).equals("1")){
//            url += ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ARTICLE_ID,0);
//        }else{
//            url += url+ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ID,0);
//        }
        Log.d("显示网页地址", url);
        register_webView.loadUrl(url);
//        getProductInfo();
    }


}
