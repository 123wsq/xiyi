package com.example.wsq.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wsq on 2017/12/28.
 */

public class CalendarUtils {

    /**
     * 计算每月多少天
     *
     */
    public static int getMonthDays(){

        return getMonthdays(getYear(), getMonth());
    }

    /**
     * 计算每月多少天
     *
     */
    public static int getMonthdays(int year, int month) {

        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return 31;
                case 2:
                    if (leayyear) {
                       return 29;
                    } else {
                        return 28;
                    }
                case 4:
                case 6:
                case 9:
                case 11:
                    return 30;
            }
        }

        return 0;
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getYear() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getMonth() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getDay() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }


    /**
     * 获取系统当前时间
     * @param formart  yyyy-MM-dd hh:MM:ss
     * @return
     */
    public static String getSysDate(String formart){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formart);
        return sdf.format(date);
    }

    /*
     * 获取当前是星期几
     */
    public static int  getWeek(){
        return getWeek(getYear(), getMonth(), getDay());
    }

    /**
     * 获取这个月的第一天是星期几
     * @return
     */
    public static int  getMonthFirstWeek(){
        return getWeek(getYear(), getMonth(), 1);
    }
    public static int getWeek(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//指定年份
        calendar.set(Calendar.MONTH, month - 1); //指定月份 Java月份从0开始算
        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天

            //获取指定年份月份中指定某天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, day);  //指定日
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);  //在日期中 每周日是这周的第一天

        switch (dayOfWeek) {
            case 1:
//                weekTextView.setText("星期日");
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;

    }
}
