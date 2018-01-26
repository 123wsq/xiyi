package com.example.wsq.android.db.dao.inter;

import android.content.Context;

import com.example.wsq.android.bean.ArticleBean;
import com.example.wsq.android.bean.AuthType;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/17.
 */

public interface DbConInter {

    /**
     * 查询所有的数据
     * @return
     */
    List<ArticleBean> selectAll(Context context, AuthType type);

    /**
     * 删除数据
     * @param context
     */
    void removeData(Context context,  AuthType type);

    /**
     * 更新数据
     * @param context
     * @param key
     * @param content
     */
    void updateData(Context context, String key,  String content, AuthType authType);

    /**
     * 插入数据
     * @param bean
     */
    void insertData(Context context, ArticleBean bean);


    /**
     * 获取资料个数
     * @param context
     * @param authType
     * @return
     */
    int selectCount(Context context, AuthType authType);




    /**
     * 查询所有的数据
     * @return
     */
    List<Map<String, Object>> selectAllAttachment(Context context, AuthType type);

    /**
     * 删除数据
     * @param context
     */
    void removeAttachment(Context context, String name,  AuthType type);

    /**
     * 清空附件表
     */
    void onClearAttachment(Context context, AuthType type);
    /**
     * 插入数据
     * @param context
     */
    void insertAttachmentData(Context context, String fileName, String path, AuthType type);


}
