package com.example.wsq.android.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 */

public final class DateUtil {

    public static final String DATA_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String DATA_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATA_FORMAT_2 = "MM-dd HH:mm";

    public static final String DATA_FORMAT_3 = "yyyy/MM/dd";

    public static final String DATA_FORMAT_4 = "dd";

    public static final String DATA_FORMAT_5 = "yyyy-MM-dd";



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

    /**
     * 将时间转换成固定的格式
     * @param format
     * @return
     */
    public static String onDateFormat(String time, String format){
        // TODO Auto-generated method stub

        SimpleDateFormat format1 = new SimpleDateFormat(DATA_FORMAT_1);
        try {
            Date d = format1.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String date = dateFormat.format(d);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return "";
    }

    /**
     * 将时间转换成制定格式
     * @param time
     * @param format
     * @return
     */
    public static String onDateFormat(Date time, String format){
        // TODO Auto-generated method stub

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String date = dateFormat.format(time);
            return date;
    }


    /**
     * 将毫秒转换成天
     * @param millis
     * @return
     */
    public static String onMillisForDay(String millis){


        DateFormat formatter = new SimpleDateFormat(DATA_FORMAT_4);
        long now = Long.parseLong(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        return formatter.format(calendar.getTime())+"";
    }

    /**
     * 将毫秒转换成日期
     * @param millis
     * @return
     */
    public static String onMillisForDate(String millis){


        DateFormat formatter = new SimpleDateFormat(DATA_FORMAT_1);
        long now = Long.parseLong(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        return formatter.format(calendar.getTime())+"";
    }

    /**
     * 将毫秒转换成日期
     * @param millis
     * @return
     */
    public static String onMillisForDate(String millis, String format){


        DateFormat formatter = new SimpleDateFormat(format);
        long now = Long.parseLong(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        return formatter.format(calendar.getTime())+"";
    }

    /**
     *将秒转换成日期时间
     * @param second
     * @param format
     * @return
     */
    public static String OnSecondForDate(String second, String format){

        DateFormat formatter = new SimpleDateFormat(format);
        long now = Long.parseLong(second+"000");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        return formatter.format(calendar.getTime())+"";
    }

}
