package com.example.wsq.android.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.wsq.android.tools.CrashHandler;
import com.example.wsq.plugin.umeng.UmengUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by wsq on 2017/12/20.
 */

public class MApplication extends Application {

    private String TAG = "XIYI";
    @Override
    public void onCreate() {
        super.onCreate();

        //友盟初始化
        UmengUtils.initUmeng(this);


        CrashHandler.getInstance().init(getApplicationContext());

        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        //使用极光IM实现单点登录
        JMessageClient.setDebugMode(false);
        JMessageClient.init(this, true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
