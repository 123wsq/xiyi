package com.example.wsq.android.service;

import com.example.wsq.android.inter.HttpResponseCallBack;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/11.
 */

public interface UserService {

    /**
     * 用户登录
     * @param params
     * @throws Exception
     */
    void login(Map<String, String> params, HttpResponseCallBack callBack) throws  Exception;

    /**
     * 用户登出
     * @param params
     * @throws Exception
     */
    void loginOut(Map<String, String> params) throws Exception;

    /**
     * 用户注册
     * @param params
     * @throws Exception
     */
    void register(Map<String, String> params, HttpResponseCallBack callBack) throws  Exception;


    /**
     * 修改密码
     * @param params
     * @throws Exception
     */
    void updatePassword(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取手机验证码
     * @param params
     * @throws Exception
     */
    void getValidateCode(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    void getUserInfo(Map<String, String> params, HttpResponseCallBack callBack) throws  Exception;

    /**
     * 修改用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    void updateUserInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 修改用户密码
     * @param params
     * @param callBack
     * @throws Exception
     */
    void updateUserPassword(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 上传头像
     * @param params
     * @param callBack
     * @throws Exception
     */
    void uploadHeader(Map<String, String> params, List<Map<String, Object>> list,  HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取我的收藏列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void getCollectList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 取消收藏
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onCancelCollect(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 获取消息列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onMessageList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取订单信息详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onOrderMessageInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;
}
