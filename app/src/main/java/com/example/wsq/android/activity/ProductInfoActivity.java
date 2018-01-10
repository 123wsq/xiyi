package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.user.CollectActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.IntentFormat;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wsq on 2017/12/18.
 */

public class ProductInfoActivity extends Activity {

    @BindView(R.id.register_webView) WebView register_webView;
    @BindView(R.id.tv_title) TextView tv_title;


    private SharedPreferences shared;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_protocols);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        initView();

    }

    public void initView() {
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_title.setText("资料详情");




        String url = Urls.HOST+Urls.GET_DETAIL+"?"+ResponseKey.TOKEN+"="+shared.getString(Constant.SHARED.TOKEN, "")
                +"&";
        if (getIntent().hasExtra(CollectActivity.COLLECT)){
            url += ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ARTICLE_ID,0);
        }else{
            url += ResponseKey.ID+"="+getIntent().getIntExtra(ResponseKey.ID,0);
        }
        Log.d("显示网页地址", url);


        onSetting(url);
//        getProductInfo();
    }


    public void onSetting(String url){
        register_webView.getSettings().setJavaScriptEnabled(true);
        register_webView.getSettings().setAppCacheEnabled(true);
        register_webView.getSettings().setDatabaseEnabled(true);
        register_webView.getSettings().setDomStorageEnabled(true);
        register_webView.loadUrl(url);
        register_webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistener");

        register_webView.setWebViewClient(new MyWebViewClient());
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view){
        finish();
    }



    class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String all, String img) {
            Logger.d(all+"\n"+img);

            if (TextUtils.isEmpty(all)){
                Toast.makeText(ProductInfoActivity.this, "读取图片失败", Toast.LENGTH_SHORT).show();
                return;
            }

            int position = -1;
            String[] images = all.split(",");
            List<LocalMedia> list = new ArrayList<>();
            for (int i = 0 ; i < images.length; i++){
                LocalMedia media = new LocalMedia();
                media.setPath(images[i]);
                list.add(media);
                if (images[i].equals(img)){
                    position = i;
                }
            }


            if (position == -1){
                position = 0 ;
            }

            PictureSelector.create(ProductInfoActivity.this).externalPicturePreview(position, list);
        }


        @android.webkit.JavascriptInterface
        public void openPdf(String path, String name){

            Logger.d("pdf路径  "+path+"\n"+name);

            Map<String, Object> param = new HashMap<>();
            param.put("url", path);
            param.put("name", name);
            if (path.endsWith(".pdf") || path.endsWith(".PDF")) {

                IntentFormat.startActivity(ProductInfoActivity.this, PdfReadActivity.class, param);
            }else{
                IntentFormat.startActivity(ProductInfoActivity.this, WordReadActivity.class, param);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "var path =''; "+
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"+
                    "path += objs[i].src +\",\" ;"+
                    "var obj = objs[i];"+
                    "    objs[i].onclick=function()  " +
                    "    {  " +
                    "        window.imagelistener.openImage(path, this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "var objpdfs = document.getElementsByTagName(\"a\"); " +
                    "for(var i=0;i<objpdfs.length;i++)  " +
                    "{"+
                    "var objpdf = objpdfs[i];"+
                    "    objpdfs[i].onclick=function()  " +
                    "    {  " +
                    "        window.imagelistener.openPdf(this.href, this.innerText);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "})()");
        }

    }



}
