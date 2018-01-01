package com.example.wsq.android.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/12.
 */

public class ParamFormat {

    public static Map<String, Object> onJsonToMap(String string) throws  Exception{

        JSONObject jsonObject = new JSONObject(string);
        int count = jsonObject.length();
        Map<String, Object> map = new HashMap<>();
        Iterator<String> iterator =  jsonObject.keys();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                JSONArray jsona = (JSONArray) value;
                for (int i = 0; i < jsona.length(); i++){

                   list.add(onJsonToMap(jsona.get(i).toString()));
                }
                map.put(key, list);
//            }else if(value instanceof JSONObject){
//                map.put(key, onJsonToMap(value.toString()));
            }else{
                if (TextUtils.isEmpty(value+"") ||value.equals("null") ){
                    map.put(key, "");
                }else {
                    map.put(key, value);
                }

            }


        }
        return map;
    }

    /**
     * 最新
     * @param string
     * @return
     * @throws Exception
     */
    public static Map<String, Object> onAllJsonToMap(String string) throws  Exception{

        JSONObject jsonObject = new JSONObject(string);
        int count = jsonObject.length();
        Map<String, Object> map = new HashMap<>();
        Iterator<String> iterator =  jsonObject.keys();
        while (iterator.hasNext()){
            String key = iterator.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                JSONArray jsona = (JSONArray) value;
                for (int i = 0; i < jsona.length(); i++){

                    list.add(onAllJsonToMap(jsona.get(i).toString()));
                }
                map.put(key, list);
            }else if(value instanceof JSONObject){
                map.put(key, onJsonToMap(value.toString()));
            }else{
                if (TextUtils.isEmpty(value+"") || value.equals("null")){
                    map.put(key, "");
                }else {
                    map.put(key, value);
                }

            }


        }
        return map;
    }

    public static Intent onMapToIntent(Map<String, Object> param){

        Intent intent = new Intent();

        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();

        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();

            if (value instanceof Integer){
                intent.putExtra(entry.getKey(), (int)entry.getValue());
            }else if (value instanceof String){
                intent.putExtra(entry.getKey(), entry.getValue()+"");
            }else if (value instanceof Boolean){
                intent.putExtra(entry.getKey(), (Boolean) entry.getValue());
            }else {
                Log.d("intent===", "未知数据类型"+entry.getKey());
            }
        }
        return intent;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
