package com.example.wsq.android.service;

import android.content.Context;

import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDownloadListener;

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
     * 验证手机
     * @param context
     * @param params
     * @param callBack
     */
    void validatePhone(Context context, Map<String, String> params);
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
    void updatePassword(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取手机验证码
     * @param params
     * @throws Exception
     */
    void getValidateCode(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 获取主页轮播图片
     * @param context
     * @param param
     * @param callBack
     */
    void getMainBannerImages(Context context, Map<String, String> param , HttpResponseCallBack callBack);
    /**
     * 获取用户信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    void getUserInfo(Context context, Map<String, String> params, HttpResponseListener callBack);

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
    void updateUserPassword(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 上传头像
     * @param params
     * @param callBack
     * @throws Exception
     */
    void uploadHeader(Context context, Map<String, String> params, List<Map<String, Object>> list,  HttpResponseListener callBack);

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
    void onCancelCollect(Context context, Map<String, String> params, HttpResponseListener callBack);


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
    void onOrderMessageInfo(Context context, Map<String, String> params, HttpResponseListener callBack);

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
    void onAddBankGetValidateCode(Context context, Map<String, String> params, HttpResponseListener callBack) ;


    /**
     * 添加银行卡
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAddBank(Context context, Map<String, String> params, HttpResponseListener callBack) ;


    /**
     * 我的余额
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onMyMoney(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 添加一个提现
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAddCash(Context context, Map<String, String> params, HttpResponseListener callBack) ;


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


    /**
     * 用户签到
     * @param context
     * @param param
     * @param listener
     */
     void onSign(Context context, Map<String, String> param, HttpResponseListener listener);


    /**
     * 积分记录
     * @param context
     * @param param
     * @param listener
     */
    void onIntegralRecord(Context context, Map<String, String> param, HttpResponseListener listener);


    /**
     * 签到列表
     * @param context
     * @param param
     * @param listener
     */
    void onSignList(final Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 收入列表
     * @param context
     * @param param
     * @param listener
     */
    void getReceiptsList(final Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 收入明细
     * @param context
     * @param param
     * @param listener
     */
    void getReceiptsInfo(final  Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 获取自己上传资料的记录
     * @param context
     * @param param
     */
    void onGetShareRecordList(final Context context, Map<String, String> param, final HttpResponseListener listener);

    /**
     * 创建资料分享
     * @param context
     * @param param
     */
    void onCreateShare(final Context context, Map<String, String> param, List<Map<String, Object>> fileList, final HttpResponseListener listener);


    /**
     * 删除资料分享
     * @param context
     * @param param
     */
    void onRemoveShare(final Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 提交编辑的资料信息
     * @param context
     * @param param
     */
    void onSubmitShare(final Context context, Map<String, String> param, final HttpResponseListener listener);

    /**
     * 上传文件
     * @param context
     * @param param
     * @param listener
     */
    void onUploadFile(final Context context, Map<String, String> param, List<Map<String, Object>> list, final HttpResponseListener listener);


    /**
     * 删除文件
     * @param context
     * @param param
     * @param listener
     */
    void onRemoveFile(final Context context, Map<String, String> param, final HttpResponseListener listener);

    /**
     * 打开红包
     * @param context
     * @param param
     * @param listener
     */
    void onOpenRedPacket(final Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 获取首页轮播图
     * @param context
     * @param param
     * @param listener
     */
    void onBannerImage(final Context context, Map<String, String> param, final HttpResponseListener listener);


    /**
     * 检查更新app的显示样式
     * @param context
     * @param param
     * @param listener
     */
    void onCheckAppStyle(final  Context context, Map<String, String> param, final HttpResponseListener listener);
    /**
     * 文件下载
     * @param context
     * @param param
     * @param listener
     */
    void onDownloadFile(final Context context, String path,  Map<String, String> param, final OnDownloadListener listener);


    /**
     * 文件下载
     * @param context
     * @param path 是全路径，不需要拼接
     * @param param
     * @param listener
     */
    void onDownloadPDF(final Context context, String path,  Map<String, String> param, final OnDownloadListener listener);
}
