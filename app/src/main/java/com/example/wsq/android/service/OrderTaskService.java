package com.example.wsq.android.service;

import com.example.wsq.android.inter.HttpResponseCallBack;

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
    void onDeviceRepairs(Map<String, String> params, List<Map<String, Object>> list, HttpResponseCallBack callBack) throws  Exception;


    /**
     * 获取订单列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetOrderList(Map<String, String> params, HttpResponseCallBack callBack) throws  Exception;

    /**
     * 获取订单详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void ongetOrderInfo(Map<String, String> params, HttpResponseCallBack callBack) throws  Exception;

    /**
     * 审核
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onAudit(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 服务工程师订单状态
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onOrderStatus(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 提交移交报告
     * @param params
     * @param list
     * @param callBack
     * @throws Exception
     */
    void onSubmitReport(Map<String, String> params, List<Map<String, Object>> list, HttpResponseCallBack callBack) throws Exception;


    /**
     * 获取产品资料信息
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetProductInformation(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 获取资料详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetProductInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取设备列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetDeviceList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 获取设备详情
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetDeviceInfo(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 获取新闻列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetNewsList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

    /**
     * 获取圈内知识列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onGetKnowledgeList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;


    /**
     * 搜索设备列表
     * @param params
     * @param callBack
     * @throws Exception
     */
    void onSearchDeviceList(Map<String, String> params, HttpResponseCallBack callBack) throws Exception;

}
