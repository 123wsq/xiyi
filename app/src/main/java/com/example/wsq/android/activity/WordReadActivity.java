package com.example.wsq.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.view.LoddingDialog;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by wsq on 2018/1/5.
 */

public class WordReadActivity extends BaseActivity implements AdvancedWebView.Listener{

    @BindView(R.id.register_webView)
    AdvancedWebView register_webView;
    @BindView(R.id.tv_title) TextView tv_title;

    private SharedPreferences shared;
    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_readword;
    }

    @Override
    public void init() {

        AppStatus.onSetStates(this);

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        dialog = new LoddingDialog(this);
        String url  = getIntent().getStringExtra("url");
        tv_title.setText(getIntent().getStringExtra("name"));

        register_webView.setListener(this,  this);
        Logger.d("word文件地址： "+url);
        //http://api.idocv.com/view/url?url=http%3A%2F%2Fapi.idocv.com%2Fdata%2Fdoc%2Fmanual.docx
        register_webView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=".concat(url));
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        dialog.show();
    }

    @Override
    public void onPageFinished(String url) {

        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
