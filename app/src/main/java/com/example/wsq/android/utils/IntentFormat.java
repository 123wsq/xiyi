package com.example.wsq.android.utils;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsq on 2017/12/12.
 */

public class IntentFormat {

    public static void startActivity(Context context, Class classes){

        Map<String, Object> map = new HashMap<>();
        startActivity(context, classes, map);
    }

    public static void startActivity(Context context, Class classes, Map<String, Object> param){

        Intent intent = ParamFormat.onMapToIntent(param);
        intent.setClass(context, classes);
        context.startActivity(intent);
    }
}
