package com.example.wsq.android.utils;

import java.text.SimpleDateFormat;

/**
 */

public final class DateUtil {

    public static final String DATA_FORMAT = "yyyy/MM/dd HH:mm:ss";
    /**
     * 将当前时间转换成固定的格式
     * @param format
     * @return
     */
    public static String onDateFormat(String format){
        // TODO Auto-generated method stub

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(new java.util.Date());


        return date;
    }

}
