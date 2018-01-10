package com.example.wsq.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.tools.AppStatus;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/5.
 */

public class WordReadActivity extends BaseActivity{

    @BindView(R.id.register_webView) WebView register_webView;
    @BindView(R.id.tv_title) TextView tv_title;

    private SharedPreferences shared;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_protocols;
    }

    @Override
    public void init() {
        AppStatus.onSetStates(this);

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        String url  = getIntent().getStringExtra("url");
        tv_title.setText(getIntent().getStringExtra("name"));

        Logger.d("word文件地址： "+url);
        register_webView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src="+url);
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
