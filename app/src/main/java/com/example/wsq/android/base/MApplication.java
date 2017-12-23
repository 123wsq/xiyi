package com.example.wsq.android.base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wsq on 2017/12/20.
 */

public class MApplication extends Application{

    private String TAG = "XIYI";
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.getInstance().init(getApplicationContext());

//        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(
//                getApplicationContext(), "5a3ca74c8f4a9d72fd000322", "",
//                MobclickAgent.EScenarioType.E_UM_NORMAL,true
//        );
//
//        MobclickAgent.startWithConfigure(config);
//        MobclickAgent.setCatchUncaughtExceptions(true);  //打开错误统计
//        MobclickAgent.setDebugMode( true );


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        LoggConfiguration configuration = new LoggConfiguration.Buidler()
//                .setDebug(true)
//                .setTag(TAG)
//                .build();
//        Logg.init(configuration);
    }
}
