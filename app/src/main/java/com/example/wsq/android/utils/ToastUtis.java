package com.example.wsq.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wsq on 2018/1/11.
 */

public class ToastUtis {


    public static ToastUtis toast;
    private static Context mContext;

    public ToastUtis(){

    }

    public static ToastUtis getInstance(Context context){

        if (toast == null) {
            synchronized (context) {
                mContext = context;
                toast = new ToastUtis();
            }
        }
        return toast;
    }

    /**
     * 显示Toast
     * @param msg
     */
    public static void onToast( String msg){

        if (mContext!=null){
            Toast.makeText(mContext, msg , Toast.LENGTH_SHORT).show();
        }

    }

    public static void onToast( String msg, int time){

        if (mContext!=null){
            Toast.makeText(mContext, msg , time).show();
        }

    }
}
