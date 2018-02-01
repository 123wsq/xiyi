package com.example.wsq.android.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.wsq.android.tools.AppImageLoad;
import com.example.wsq.android.utils.ToastUtis;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by wsq on 2017/12/20.
 */

public class MApplication extends Application {

    private String TAG = "XIYI";
    private List<String> mImages;
    @Override
    public void onCreate() {
        super.onCreate();


        mImages = new ArrayList<>();
        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        //使用极光IM实现单点登录
        JMessageClient.setDebugMode(false);
        JMessageClient.init(this, true);

        ToastUtis.getInstance(this);
        mImages.addAll(AppImageLoad.getLoadImages(this));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
