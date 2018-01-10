package com.example.wsq.android.utils;

import android.text.TextUtils;

/**
 * Created by wsq on 2018/1/10.
 */

public class DataFormat {

    /**
     * 将字符串转换成数字
     * @param str
     * @return
     */
    public static int onStringForInteger(String str){

        if (TextUtils.isEmpty(str)){
            return -1;
        }

        if (!ValidateDataFormat.isNumber(str)){

            return -1;
        }
        double d = Double.parseDouble(str);
        return (int)d;
    }

    /**
     * 将字符串转换成小数
     * @param str
     * @return
     */
    public static double onStringForFloat(String str){

        if (TextUtils.isEmpty(str)){
            return -1.0f;
        }

        if (!ValidateDataFormat.isNumber(str)){

            return -1.0f;
        }
        double d = Double.parseDouble(str);
        return d;
    }
}
