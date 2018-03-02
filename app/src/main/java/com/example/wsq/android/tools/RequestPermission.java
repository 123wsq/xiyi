package com.example.wsq.android.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public class RequestPermission {

    /**
     *
     * @param activity
     * @param permission   Manifest.permission.CALL_PHONE
     * @param REQUEST_FLAG
     */
    public void onSinglePermissionRequest(Activity activity, String permission, int REQUEST_FLAG){

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_FLAG);
        }
    }
}
