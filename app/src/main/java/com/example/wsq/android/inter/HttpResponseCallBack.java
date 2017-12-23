package com.example.wsq.android.inter;

import java.util.Map;

/**
 * 请求数据成功时的回调
 * Created by wsq on 2017/12/12.
 */

public interface HttpResponseCallBack {

    /**
     * 成功
     * @param result
     */
    void callBack(Map<String, Object> result);

    /**
     * 失败
     */
    void onCallFail(String msg);

}
