package com.example.wsq.android.constant;

/**
 * Created by wsq on 2017/12/11.
 */

public class Urls {

    //协议
    public static final String HOST = "http://xiyicontrol.com";


    //登录
    public static final String LOGIN = "/api/doLogin";

    //注册
    public static final String REGISTER = "/api/doRegister";

    //获取验证码
    public static final String GET_VALIDATE_CODE = "/api/yanzheng";

    //忘记密码
    public static final String UPDATE_PASSWORD = "/api/reset";

    //修改密码
    public static final String  UPDATE_USER_PASSWORD= "/api/member/changePassword";

    //获取用户信息
    public static final String  GET_USER_INFO= "/api/member/profile";

    //修改用户信息
    public static final String  UPDATE_USER_INFO = "/api/member/changeProfile";

    //上传头像
    public static final String UPLOAD_USER_HEADER = "/api/member/userPic";

    //报修
    public static final String  TOREPAIR= "/api/member/torepair";

    //获取订单列表
    public static final String SERVER_ORDER = "/api/member/repairManage";  //服务工程师
    public static final String DEVICE_ORDER = "/api/member/record";  //企业工程师
    public static final String MANAGER_ORDER_INFO= "/api/member/check";  //企业管理工程师

    //获取订单详情
    public static final String GET_ORDER_INFO = "/api/member/recordDetail";

    //审核
    public static final String AUDIT = "/api/member/setStatus";

    //服务工程师订单状态
    public static final String SERVER_STATUS= "/api/member/setOrderStatus";

    //服务工程师订单详情
    public static final String  SERVER_ORDER_INFO= "/api/member/repairDetail";

    //提交移交报告
    public static final String  SUBMIT_REPORT= "/api/member/finishReport";

    //图片上传之后的下载路径
    public static final String GET_IMAGES = "/uploads/images/";

    //获取资料列表
    public static final String GET_PRODUCT = "/api/share";

    //获取资料详情
    public static final String GET_DETAIL = "/api/detail";

    //获取设备
    public static final String GET_DEVICE = "/api/shebei";

    //获取设备详情
    public static final String GET_DEVICE_INFO = "/api/shebei_detail";

    public static final String GET_DEVICE_CHILD_INFO = "/api/s_detail";

    /**
     * 新闻
     */
    public static final String GET_NEWS = "/api/news";

    /**
     * 关于我们
     */
    public static final String ABOUT = "/api/about";

    /**
     * 我的收藏
     */
    public static final String COLLECT = "/api/member/collection";

    /**
     * 取消收藏
     */
    public static final String CANCEL_COLLECT = "/api/member/concal_collect";

    /**
     * 圈内知识
     */
    public static final String KNOWLEDGE = "/api/zhishi";

    /**
     * 获取消息列表
     */
    public static final String  MESSAGE_LIST = "/api/member/message";

    //订单信息详情
    public static final String ORDER_MESSAGE_INFO = "/api/member/message_detail";


    public static final String SEARCH = "/api/search";

    /**
     * 订单处理的个数
     */
    public static final String ORDER_COUNT= "/api/member/order_count";

    /**
     * 在订单没有分配之前修改订单数据
     */
    public static final String ORDER_UPDATE = "/api/member/editRecord";
}
