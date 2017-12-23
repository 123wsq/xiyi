package com.example.wsq.android.service.impl;

import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.tools.OkHttpRequest;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.ValidateParam;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/11.
 */

public class UserServiceImpl implements UserService{

    /**
     * 用户登录数据
     * @param params
     * @throws Exception
     */
    @Override
    public void login(Map<String, String> params, final HttpResponseCallBack callBack) throws Exception {

        OkHttpRequest.sendPost(Urls.LOGIN, params, callBack);
    }

    /**
     * 用户登出
     * @param params
     * @throws Exception
     */
    @Override
    public void loginOut(Map<String, String> params) throws Exception {



    }

    /**
     * 用户注册
     * @param params
     * @throws Exception
     */
    @Override
    public void register(Map<String, String> params, final HttpResponseCallBack callBack) throws Exception {

        params.put(ResponseKey.USERNAME, RegisterParam.USERNAME);
        params.put(ResponseKey.PASSWORD, RegisterParam.PASSWORD);
        params.put(ResponseKey.COMPANY, RegisterParam.COMPANY);
        params.put(ResponseKey.JUESE, RegisterParam.JUESE+"");
        params.put(ResponseKey.NAME, RegisterParam.NAME);
        params.put(ResponseKey.SEX, RegisterParam.SEX+"");
        params.put(ResponseKey.TEL, RegisterParam.TEL);
        params.put(ResponseKey.YZM, RegisterParam.YZM);
        params.put(ResponseKey.BIRTH, RegisterParam.BIRTH);
        params.put(ResponseKey.SFZ, RegisterParam.SFZ);
        params.put(ResponseKey.XUELI, RegisterParam.XUELI+"");
        params.put(ResponseKey.BUMEN, RegisterParam.BUMEN);
        params.put(ResponseKey.QQ, RegisterParam.QQ);
        params.put(ResponseKey.WEIXIN, RegisterParam.WEIXIN);
        params.put(ResponseKey.EMAIL, RegisterParam.EMAIL);
        params.put(ResponseKey.SFZ1, RegisterParam.SFZ1);
        params.put(ResponseKey.SFZ2, RegisterParam.SFZ2);


        OkHttpRequest.sendPost(Urls.REGISTER, params, callBack);

    }

    /**
     * 忘记密码
     * @param params
     * @throws Exception
     */
    @Override
    public void updatePassword(Map<String, String> params, final HttpResponseCallBack callBack) throws Exception {

        OkHttpRequest.sendPost(Urls.UPDATE_PASSWORD, params, callBack);

    }

    /**
     * 获取验证码
     * @param params
     * @throws Exception
     */
    @Override
    public void getValidateCode(Map<String, String> params, final HttpResponseCallBack callBack) throws Exception {
        params.put(ResponseKey.JUESE, RegisterParam.JUESE+"");
        params.put(ResponseKey.TEL, RegisterParam.TEL);

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TEL);

        OkHttpRequest.sendPost(Urls.GET_VALIDATE_CODE, params, callBack);

    }

    /**
     * 获取用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void getUserInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {
        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN);

        OkHttpRequest.sendPost(Urls.GET_USER_INFO, params, callBack);
    }

    /**
     * 修改用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void updateUserInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN,ResponseKey.NAME, ResponseKey.SEX);

        OkHttpRequest.sendPost(Urls.UPDATE_USER_INFO, params, callBack);
    }

    /**
     * 修改用户密码
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void updateUserPassword(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN,ResponseKey.OLD_PSD, ResponseKey.NEW_PSD,ResponseKey.TOKEN);

        OkHttpRequest.sendPost(Urls.UPDATE_USER_PASSWORD, params, callBack);
    }

    /**
     * 上传用户头像
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void uploadHeader(Map<String, String> params, List<Map<String, Object>> list,  HttpResponseCallBack callBack) throws Exception {

        ValidateParam.validateParam(params,ResponseKey.TOKEN);

        OkHttpRequest.uploadGetFile(Urls.UPLOAD_USER_HEADER, params, list, callBack);
    }

    /**
     * 我的收藏列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void getCollectList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.PAGE);

        OkHttpRequest.sendPost(Urls.COLLECT, params, callBack);
    }

    /**
     * 取消收藏
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onCancelCollect(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.IDS);

        OkHttpRequest.sendGet(Urls.CANCEL_COLLECT, params, callBack);
    }

    /**
     * 获取消息列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onMessageList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.PAGE);

        OkHttpRequest.sendGet(Urls.MESSAGE_LIST, params, callBack);
    }

    /**
     * 获取订单信息详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onOrderMessageInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception {

        //必填参数验证
        ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.ORDER_NO);

        OkHttpRequest.sendGet(Urls.ORDER_MESSAGE_INFO, params, callBack);
    }
}
