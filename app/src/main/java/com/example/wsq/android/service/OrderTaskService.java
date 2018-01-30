package com.example.wsq.android.service;

import android.content.Context;

import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/14.
 */

public interface OrderTaskService {


    /**
     * 设备报修
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onDeviceRepairs(Context context, Map<String, String> params, List<Map<String, Object>> list, HttpResponseListener callBack);


    /**
     * 获取订单列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetOrderList(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取订单详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void ongetOrderInfo(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 审核
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAudit(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 服务工程师订单状态
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onOrderStatus(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 提交移交报告
     * @param params
     * @param list
     * @param callBack
     * @throws Exception
     */
    void onSubmitReport(Context context, Map<String, String> params, List<Map<String, Object>> list, HttpResponseListener callBack);


    /**
     * 获取产品资料信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetProductInformation(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 获取资料详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetProductInfo(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取设备列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetDeviceList(Context context,Map<String, String> params, HttpResponseListener callBack);


    /**
     * 获取设备详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetDeviceInfo(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 获取新闻列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetNewsList(Context context, Map<String, String> params, HttpResponseListener callBack);

    /**
     * 获取圈内知识列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetKnowledgeList(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 搜索设备列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onSearchDeviceList(Context context,Map<String, String> params, HttpResponseListener callBack);


    /**
     * 获取处理订单个数
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetOrderCount(Context context, Map<String, String> params, HttpResponseListener callBack);


    /**
     * 修改订单
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onUpdateOrder(Context context, Map<String, String> params, List<Map<String, Object>> list, HttpResponseListener callBack);

}
