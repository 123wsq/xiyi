package com.example.wsq.android.utils;

import java.util.Map;

/**
 * Created by wsq on 2017/12/12.
 */

public class ValidateParam {

    public static boolean validateParamIsNull(String... values){

        for (int i = 0; i < values.length; i++) {
            if (null == values[i] || values[i].length() == 0 ){

                return true;
            }else{

            }
        }
        return false;
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
                if (null == value || value.length()== 0){

                    throw new Exception(values[i]+"为必要参数");
                }
            }else {
                throw new Exception(values[i]+"参数不存在");
            }
        }

    }
}
