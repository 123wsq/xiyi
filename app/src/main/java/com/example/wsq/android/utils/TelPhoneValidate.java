package com.example.wsq.android.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import com.example.wsq.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class TelPhoneValidate {


    /**
     * 查询符合的手机号码
     * @param str
     */
    public static List<String> checkCellphone(String str){
        // 将给定的正则表达式编译到模式中
        //String num = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}";
        List<String> list = new ArrayList<String>();
        String num = "[1][345789]\\d{9}";
        Pattern pattern = Pattern.compile(num);
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        while(matcher.find()){
            //查找到符合的即输出
            list.add(matcher.group());
//            System.out.println("查询到一个符合的手机号码："+matcher.group());
        }
        return list;
    }


    public static  void onGetTelCode(Context context, String content, TextView textView,int colorId){

        SpannableString sp = new SpannableString(content);
        Linkify.addLinks(sp,Linkify.PHONE_NUMBERS);
        List<String> listTel = checkCellphone(content);
        for (int i = 0; i < listTel.size(); i++) {
            int startTel = content.indexOf(listTel.get(i));
            sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)), startTel, startTel + 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(sp);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
