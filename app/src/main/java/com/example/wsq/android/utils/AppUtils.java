package com.example.wsq.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by wsq on 2017/12/19.
 */

public class AppUtils {

     /* 获取本地软件版本号
	 */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        localVersion = packageInfo.versionCode;
            Log.d("TAG", "本软件的版本号。。" + localVersion);

        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
