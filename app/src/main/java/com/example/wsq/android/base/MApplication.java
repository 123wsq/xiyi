package com.example.wsq.android.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

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
//        CrashHandler.getInstance().init(getApplicationContext());



        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this, true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
//
//    static {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
//            @NonNull
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                return new ClassicsHeader(context);
//            }
//        });
//
//        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
//            @NonNull
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                return new ClassicsFooter(context);
//            }
//        });
//    }
}
