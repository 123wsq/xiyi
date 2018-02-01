package com.example.wsq.android.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.wsq.android.constant.Constant;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class AppImageLoad {
    public static String defaultPath = "image/default_image/";

    public static String getPath(Context context){
        SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        String path = shared.getString(Constant.SHARED.IMAGE_PATH,"");

        if (TextUtils.isEmpty(path)){
            path = defaultPath;
        }
        return path;
    }
    /**
     * 加载assets文件下面的所有图片
     * @return
     */
    public static List<String> getLoadImages(Context context){
        List<String> list = new ArrayList<>();
        if (context == null){
            return list;
        }


        //读取文件
        try {
            String path  =getPath(context);
            String [] arrays = context.getResources().getAssets().list(path);

            for (int i = 0; i < arrays.length; i++) {
                list.add(path+"/"+arrays[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
