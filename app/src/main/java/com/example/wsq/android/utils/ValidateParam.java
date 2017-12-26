package com.example.wsq.android.utils;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by wsq on 2017/12/12.
 */

public class ValidateParam {

    public static boolean validateParamIsNull(String... values){

        for (int i = 0; i < values.length; i++) {
            if (TextUtils.isEmpty(values[i]) ){

                return true;
            }else{

            }
        }
        return false;
    }

    public static void validateParam(String... values) throws Exception{

        for (int i = 0; i < values.length; i++) {
            if (TextUtils.isEmpty(values[i]) ){
                throw  new Exception("必要参数不能为空");
            }
        }
    }

    /**
     * 验证必要参数是否为空
     * @param param
     * @param values
     * @throws Exception
     */
    public static void validateParam(Map<String, String> param, String... values) throws Exception{

        for (int i = 0; i < values.length; i++) {

            if (param.containsKey(values[i])){

                String value = param.get(values[i]);
                if (TextUtils.isEmpty(value)){

                    throw new Exception(values[i]+"为必要参数");
                }
            }else {
                throw new Exception(values[i]+"参数不存在");
            }
        }

    }
}
