package com.example.wsq.android.inter;

import java.util.Map;

/**
 * Created by wsq on 2017/12/26.
 */

public interface HttpResponseListener {

    /**
     * 请求
     * @param params
     */
    void onSuccess(Map<String, Object> result);

    /**
     * 失败
     * @param params
     */
    void onFailure();


}
