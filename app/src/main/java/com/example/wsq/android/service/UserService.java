package com.example.wsq.android.service;

import android.content.Context;

import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/11.
 */

public interface UserService {

    /**
     * 用户登录
     * @param param
     * @throws Exception
     */
    void login(Context context, Map<String, String> param, HttpResponseListener listener);

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
    void register(Context context, Map<String, String> params, HttpResponseListener callBack);


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
    void getValidateCode(Context context, Map<String, String> params, HttpResponseListener callBack);

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
    void updateUserInfo(Context context, Map<String, String> params, HttpResponseListener callBack);


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
    void getCollectList( Context context, Map<String, String> params, HttpResponseListener callBack);

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
    void onMessageList(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取订单信息详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onOrderMessageInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 修改支付密码验证
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onUpdatePayPasswordValidate(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 忘记支付密码验证
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onForgetPayPasswordValidate(Context context, Map<String, String> params, HttpResponseListener callBack);



    /**
     * 设置支付密码
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onSettingPayPassword(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 添加银行卡的获取验证码
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAddBankGetValidateCode(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 添加银行卡
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAddBank(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 我的余额
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onMyMoney(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 添加一个提现
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAddCash(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 申请保证金
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onApplyCashDeposit(Context context, Map<String, String> params, HttpResponseListener callBack) ;


    /**
     * 获取保证金列表
     * @param context
     * @param params
     * @param callBack
     */
    void onApplyCashDepositList(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取保证金详情
     * @param context
     * @param params
     * @param callBack
     */
    void onApplyCashDepositInfo(Context context, Map<String, String> param, HttpResponseListener callBack);


    /**
     * 申请提现列表
     * @param context
     * @param param
     * @param callBack
     */
    void onApplyCashDetailList(Context context, Map<String, String> param, HttpResponseListener callBack);

    /**
     * 账单详情
     * @param context
     * @param param
     * @param callBack
     */
    void onApplyCashDetailInfo(Context context, Map<String, String> param, HttpResponseListener callBack);


    /**
     * 获取当前时间的账单详情
     * @param context
     * @param param
     * @param callBack
     */
    void onCurCashDetailList(Context context, Map<String, String> param, HttpResponseListener callBack);

}
