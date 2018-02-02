package com.example.wsq.android.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.parse.sax.SaxHandler;
import com.example.wsq.android.tools.AppImageLoad;
import com.example.wsq.android.utils.ToastUtis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by wsq on 2017/12/20.
 */

public class MApplication extends Application {

    public static boolean DEBUG = true;
    private String TAG = "XIYI";
    private List<String> mImages;
    //public static String defaultPath = "image/default_image/";
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

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constant.SHARED.IMAGE_PATH, "image/20180215/").commit();
//        sharedPreferences.edit().putString(Constant.SHARED.IMAGE_PATH, "").commit();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
