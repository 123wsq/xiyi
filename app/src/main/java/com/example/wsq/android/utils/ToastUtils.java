package com.example.wsq.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wsq on 2018/1/11.
 */

public class ToastUtils {


    public static void onToast(Context context, String msg){

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
