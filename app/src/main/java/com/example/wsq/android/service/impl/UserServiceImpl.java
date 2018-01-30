package com.example.wsq.android.service.impl;

import android.content.Context;
import android.widget.Toast;

import com.example.wsq.android.activity.user.LoginActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.tools.OkHttpRequest;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.SystemUtils;
import com.example.wsq.android.utils.ValidateParam;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/11.
 */

public class UserServiceImpl implements UserService{


    /**
     * 用户登录数据
     * @param param
     * @throws Exception
     */
    @Override
    public void login(final Context context, Map<String, String> param, final HttpResponseListener callBack) {



        try {

            ValidateParam.validateParam(param, ResponseKey.USERNAME, ResponseKey.PASSWORD);

            OkHttpRequest.sendHttpPost(Urls.LOGIN, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
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
     * 验证手机  获取积分
     * @param context
     * @param param
     */
    @Override
    public void validatePhone(final Context context, Map<String, String> param) {

        try {

            ValidateParam.validateParam(param, ResponseKey.DEVICE_TYPE, ResponseKey.DEVICE_ID);

            OkHttpRequest.sendHttpPost(Urls.DEVICE_DESC, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    RegisterParam.POINT = result.get(ResponseKey.POINT)+"";
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户注册
     * @param params
     * @throws Exception
     */
    @Override
    public void register(final Context context, Map<String, String> params, final HttpResponseListener callBack){

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
        params.put(ResponseKey.DIQU, RegisterParam.DIQU);
        params.put(ResponseKey.DEVICE_TYPE, "1"); //设备类型   1 android 2 ios
        params.put(ResponseKey.DEVICE_ID, SystemUtils.getIMEI(context));
        params.put(ResponseKey.POINT, RegisterParam.POINT);

        try {

            OkHttpRequest.sendHttpPost(Urls.REGISTER, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();

        }

    }

    /**
     * 忘记密码
     * @param params
     * @throws Exception
     */
    @Override
    public void updatePassword(final Context context, Map<String, String> params, final HttpResponseListener callBack) {


        try {

            OkHttpRequest.sendHttpPost(Urls.UPDATE_PASSWORD, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();

        }

    }

    /**
     * 获取验证码
     * @param param
     * @throws Exception
     */
    @Override
    public void getValidateCode(final Context context, Map<String, String> param, final HttpResponseListener callBack){
        param.put(ResponseKey.JUESE, RegisterParam.JUESE+"");
        param.put(ResponseKey.TEL, RegisterParam.TEL);

        //必填参数验证
        try {

            ValidateParam.validateParam(param, ResponseKey.TEL);

            OkHttpRequest.sendHttpPost(Urls.GET_VALIDATE_CODE, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
//                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 获取主页面的轮播图
     * @param context
     * @param param
     * @param callBack
     */
    @Override
    public void getMainBannerImages(final Context context, Map<String, String> param, final HttpResponseCallBack callBack) {

        //必填参数验证
        try {

//            ValidateParam.validateParam(param, ResponseKey.TEL);

            OkHttpRequest.sendHttpPost(Urls.GET_VALIDATE_CODE, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.callBack(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onCallFail("请求失败");
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onCallFail("必要参数未填写");
        }
    }

    /**
     * 获取用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void getUserInfo(final  Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN);
            OkHttpRequest.sendHttpPost(Urls.GET_USER_INFO, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();

        }
    }

    /**
     * 修改用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void updateUserInfo(final Context context, Map<String, String> params, final HttpResponseListener callBack){


        try {

            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN,ResponseKey.NAME, ResponseKey.SEX);

            OkHttpRequest.sendHttpPost(Urls.UPDATE_USER_INFO, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 修改用户密码
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void updateUserPassword(final Context context,Map<String, String> params, final HttpResponseListener callBack) {


        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN,ResponseKey.OLD_PSD, ResponseKey.NEW_PSD,ResponseKey.TOKEN);

            OkHttpRequest.sendHttpPost(Urls.UPDATE_USER_PASSWORD, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();

        }
    }

    /**
     * 上传用户头像
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void uploadHeader(final Context context, Map<String, String> params, List<Map<String, Object>> list,  final HttpResponseListener callBack) {

        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN);

            OkHttpRequest.uploadGetFile(Urls.UPLOAD_USER_HEADER, params,list,  new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {
                    if (callBack != null) {
                        callBack.onSuccess(result);
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    if (callBack != null) {
                        callBack.onFailure();
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            if (callBack != null) {
                callBack.onFailure();
            }

        }

    }

    /**
     * 我的收藏列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void getCollectList(final Context context,Map<String, String> params, final HttpResponseListener callBack){




        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.PAGE);
            OkHttpRequest.sendHttpPost(Urls.COLLECT, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 取消收藏
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onCancelCollect(final Context context,Map<String, String> params, final HttpResponseListener callBack){


        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.IDS);

            OkHttpRequest.sendHttpPost(Urls.CANCEL_COLLECT, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();

        }


    }

    /**
     * 获取消息列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onMessageList(final Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.PAGE);
            OkHttpRequest.sendHttpPost(Urls.MESSAGE_LIST, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取订单信息详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onOrderMessageInfo(final Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {
            //必填参数验证
            ValidateParam.validateParam(params,ResponseKey.TOKEN, ResponseKey.ORDER_NO);
            OkHttpRequest.sendHttpGet(Urls.ORDER_MESSAGE_INFO, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 修改支付密码验证
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onUpdatePayPasswordValidate(final Context context, Map<String, String> params, final HttpResponseListener callBack){



        try {

            ValidateParam.validateParam(params, ResponseKey.ID, ResponseKey.PAY_PASSWORD);

            OkHttpRequest.sendHttpGet(Urls.UPDATE_PAY_PSD, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }

    }

    /**
     * 忘记支付密码验证
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onForgetPayPasswordValidate(final Context context, Map<String, String> params, final HttpResponseListener callBack){



        try {

            ValidateParam.validateParam(params, ResponseKey.ID, ResponseKey.NAME, ResponseKey.BANK_CARD, ResponseKey.SFZ, ResponseKey.TEL);

            OkHttpRequest.sendHttpGet(Urls.FORGET_PAY_PSD, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }

    }

    /**
     * 设置支付密码
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onSettingPayPassword(final Context context, Map<String, String> params, final HttpResponseListener callBack) {



        try {

            ValidateParam.validateParam(params, ResponseKey.ID, ResponseKey.PAY_PASSWORD);

            OkHttpRequest.sendHttpGet(Urls.SETTING_PAY_PSD, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }
    }

    /**
     * 添加银行卡  获取验证码
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onAddBankGetValidateCode(final Context context, Map<String, String> params, final HttpResponseListener callBack)  {

        try {

            ValidateParam.validateParam(params, ResponseKey.ID);

            OkHttpRequest.sendHttpGet(Urls.ADD_BANK_VALIDATE, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }
    }

    /**
     * 添加银行卡
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onAddBank(final Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(params, ResponseKey.ID, ResponseKey.TEL, ResponseKey.CODE,
                    ResponseKey.BANK_NAME, ResponseKey.BANK_CARD);

            OkHttpRequest.sendHttpGet(Urls.ADD_BANK, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }
    }

    /**
     * 我的余额
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onMyMoney(final Context context, Map<String, String> params, final HttpResponseListener callBack){


        try {

            ValidateParam.validateParam(params, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.MY_MONEY, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }
    }

    /**
     * 添加一个提现
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onAddCash(final Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(params, ResponseKey.TOKEN, ResponseKey.MONEY);

            OkHttpRequest.sendHttpGet(Urls.ADD_CASH, params, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {

                    onExitApp(context, msg);

                    Logger.d(msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            callBack.onFailure();
        }
    }

    /**
     * 申请保证金
     * @param params
     * @param callBack
     * @throws Exception
     */
    @Override
    public void onApplyCashDeposit(final Context context, Map<String, String> params, final HttpResponseListener callBack){

        try {

            ValidateParam.validateParam(params, ResponseKey.TOKEN, ResponseKey.MESSAGE);

            OkHttpRequest.sendHttpGet(Urls.ADD_BAIL, params, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "请填写退保证金的原因", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取保证金列表
     * @param context
     * @param params
     * @param callBack
     */
    @Override
    public void onApplyCashDepositList(final Context context, Map<String, String> params, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(params, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.BAIL_LIST, params, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 获取保证金详情
     * @param context
     * @param param
     * @param callBack
     */
    @Override
    public void onApplyCashDepositInfo(final Context context, Map<String, String> param, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.BAIL_DETAIL, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 申请提现列表
     * @param context
     * @param param
     * @param callBack
     */
    @Override
    public void onApplyCashDetailList(final Context context, Map<String, String> param, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.CASH_LIST, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提现详情
     * @param context
     * @param param
     * @param callBack
     */
    @Override
    public void onApplyCashDetailInfo(final Context context, Map<String, String> param, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN, ResponseKey.PAY_ID);

            OkHttpRequest.sendHttpGet(Urls.CASH_DETAIL, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前时间的账单详情
     * @param context
     * @param param
     * @param callBack
     */
    @Override
    public void onCurCashDetailList(final Context context, Map<String, String> param, final HttpResponseListener callBack) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN, ResponseKey.YEAR, ResponseKey.MONTH);

            OkHttpRequest.sendHttpGet(Urls.SEARCH_CASH_LIST, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    callBack.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    callBack.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户签到
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void onSign(final Context context, Map<String, String> param, final HttpResponseListener listener) {


        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN, ResponseKey.CREATE_TIME);

            OkHttpRequest.sendHttpGet(Urls.SIGN, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            listener.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 积分记录
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void onIntegralRecord(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.POINT_LIST, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            listener.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取签到列表
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void onSignList(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.SIGN_LIST, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            listener.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 收入列表
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void getReceiptsList(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.RECEIPTS_LIST, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            listener.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 月收入详情  按照时间帅选
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void getReceiptsInfo(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {

            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.SEARCH_BROKERAGE, param, new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            listener.onFailure();
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取上传资料记录
     * @param context
     * @param param
     */
    @Override
    public void onGetShareRecordList(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {
            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.MY_ARTICLES, param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {
                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {

                    onExitApp(context, msg);
                    listener.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建资料
     * @param context
     * @param param
     */
    @Override
    public void onCreateShare(final Context context, Map<String, String> param,List<Map<String, Object>> fileList, final HttpResponseListener listener) {

        try {
            ValidateParam.validateParam(param, ResponseKey.TOKEN, ResponseKey.TITLE, ResponseKey.CID,
                    ResponseKey.CAT, ResponseKey.DES, ResponseKey.CONTENT);

            OkHttpRequest.uploadPostFile(Urls.ADD_ARTICLES, param, fileList,new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {
                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除资料
     * @param context
     * @param param
     */
    @Override
    public void onRemoveShare(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {
            ValidateParam.validateParam(param, ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.DELETE_ARTUCLES, param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {
                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    listener.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交编辑信息
     * @param context
     * @param param
     */
    @Override
    public void onSubmitShare(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {
            ValidateParam.validateParam(param, "");

            OkHttpRequest.sendHttpGet("", param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {
                    listener.onSuccess(result);
                }

                @Override
                public void onCallFail(String msg) {

                    onExitApp(context, msg);
                    listener.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void onUploadFile(final Context context, Map<String, String> param, List<Map<String, Object>> list, final HttpResponseListener listener) {

        try {
            //必填参数验证
            ValidateParam.validateParam(param,ResponseKey.TOKEN);

            OkHttpRequest.uploadPostFile(Urls.UPLOAD_FILE, param,list,  new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {
                    if (listener != null) {
                        listener.onSuccess(result);
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    if (listener != null) {
                        listener.onFailure();
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onFailure();
            }

        }
    }

    /**
     * 删除文件
     * @param context
     * @param param
     * @param listener
     */
    @Override
    public void onRemoveFile(final Context context, Map<String, String> param, final HttpResponseListener listener) {

        try {
            //必填参数验证
            ValidateParam.validateParam(param,ResponseKey.TOKEN);

            OkHttpRequest.sendHttpGet(Urls.DELETE_FILE, param,  new HttpResponseCallBack(){
                @Override
                public void callBack(Map<String, Object> result) {

                    if (listener != null) {
                        listener.onSuccess(result);
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    onExitApp(context, msg);
                    if (listener != null) {
                        listener.onFailure();
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "必要参数未填写", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onFailure();
            }

        }
    }
    public void onExitApp(Context context, String msg){
        if (msg.equals("2004")) {
            IntentFormat.startActivity(context, LoginActivity.class);
        }
    }
}
