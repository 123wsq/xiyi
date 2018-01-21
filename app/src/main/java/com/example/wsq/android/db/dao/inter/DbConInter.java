package com.example.wsq.android.db.dao.inter;

import android.content.Context;

import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.WebContentBean;

import java.util.Map;

/**
 * Created by wsq on 2018/1/17.
 */

public interface DbConInter {

    /**
     * 查询所有的数据
     * @return
     */
    Map<String, WebContentBean> selectAll(Context context, AuthType type) throws Exception;

    /**
     * 删除数据
     * @param key
     */
    void removeData(Context context, String key,  AuthType type);

    /**
     * 更新数据
     * @param bean
     */
    void updateData(Context context, String key,  WebContentBean bean);

    /**
     * 插入数据
     * @param bean
     */
    void insertData(Context context, String key, WebContentBean bean);


    /**
     * 删除所有的数据
     * @param context
     */
    void removeAll(Context context,  AuthType type);

    /**
     * 查询个数
     * @param context
     * @return
     */
    int selectCount(Context context,  AuthType type);

}
