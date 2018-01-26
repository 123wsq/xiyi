package com.example.wsq.android.db.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wsq.android.bean.ArticleBean;
import com.example.wsq.android.bean.ArticleType;
import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.db.DbHelper;
import com.example.wsq.android.db.dao.DbKeys;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/17.
 */

public class MaterialImpl implements DbConInter {

    private DbHelper helper;


    @Override
    public synchronized List<ArticleBean> selectAll(Context context, AuthType authType){

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor c = db.query(DbHelper.TABLE_NAME, null, DbKeys.AUTH_TYPE+"=?", new String[]{authType.getIndex()+""}, null, null, null);


        List<ArticleBean> list = new ArrayList<>();

        while (c.moveToNext()) {
            ArticleBean bean = new ArticleBean();

            int id = c.getInt(c.getColumnIndex(DbKeys.ID));
            String title =c.getString(c.getColumnIndex(DbKeys.TITLE));
            String icon = c.getString(c.getColumnIndex(DbKeys.ICON));
            String description = c.getString(c.getColumnIndex(DbKeys.DESCRIPTION));
            int auth_Type = c.getInt(c.getColumnIndex(DbKeys.AUTH_TYPE));
            int category = c.getInt(c.getColumnIndex(DbKeys.CATEGORY));
            String content = c.getString(c.getColumnIndex(DbKeys.CONTENT));
            String attachment = c.getString(c.getColumnIndex(DbKeys.ATTACHMENT));
            String create_time = c.getString(c.getColumnIndex(DbKeys.CREATE_TIME));
            String update_time = c.getString(c.getColumnIndex(DbKeys.UPDATE_TIME));

            bean.setId(id);
            bean.setContent(content);
            bean.setIcon(icon);
            bean.setTitle(title);
            bean.setDescription(description);
            bean.setAttachment(attachment);
            bean.setCreate_time(create_time);
            bean.setUpdate_time(update_time);
            if (auth_Type == AuthType.NEWS.getIndex()){
                bean.setAuthType(AuthType.NEWS);
            }else if (auth_Type == AuthType.RESOURCE.getIndex()){
                bean.setAuthType(AuthType.RESOURCE);
            }else if (auth_Type == AuthType.ARTICLES.getIndex()){
                bean.setAuthType(AuthType.ARTICLES);
            }


            if (category == ArticleType.ARTICLE.getIndex()) {
                bean.setType(ArticleType.ARTICLE);
            } else if (category == ArticleType.ECAMPLE.getIndex()) {
                bean.setType(ArticleType.ECAMPLE);
            } else if (category == ArticleType.OTHER.getIndex()) {
                bean.setType(ArticleType.OTHER);
            }

            list.add(bean);
        }
        c.close();
        return list;
    }

    @Override
    public synchronized void removeData(Context context, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_NAME, DbKeys.AUTH_TYPE+" = ? ", new String[]{type.getIndex()+""});
    }

    @Override
    public synchronized void updateData(Context context, String key, String content, AuthType authType) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key, content);

        db.update(DbHelper.TABLE_NAME, values, DbKeys.AUTH_TYPE+"=? ", new String[]{authType.getIndex()+""});
    }

    @Override
    public synchronized void insertData(Context context, ArticleBean bean) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbKeys.TITLE, bean.getTitle());
        values.put(DbKeys.CONTENT, bean.getContent());
        values.put(DbKeys.DESCRIPTION, bean.getDescription());
        values.put(DbKeys.ICON, bean.getIcon());
        values.put(DbKeys.CATEGORY, bean.getType() == null ? 0 : bean.getType().getIndex());
        values.put(DbKeys.ATTACHMENT, bean.getAttachment());
        values.put(DbKeys.CREATE_TIME, bean.getCreate_time());
        values.put(DbKeys.UPDATE_TIME, bean.getUpdate_time());
        values.put(DbKeys.AUTH_TYPE, bean.getAuthType() ==null ? 0 : bean.getAuthType().getIndex());
        long state = db.insert(DbHelper.TABLE_NAME, null, values);
        Logger.d("id= "+state+"\n======"+bean.toString());
    }

    @Override
    public int selectCount(Context context, AuthType authType) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor c = db.query(DbHelper.TABLE_NAME, null, DbKeys.AUTH_TYPE+"=?", new String[]{authType.getIndex()+""}, null, null, null);

        return c.getCount();
    }

    /**
     * 查询附件
     * @param context
     * @param type
     * @return
     */
    public List<Map<String, Object>> selectAllAttachment(Context context, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor c = db.query(DbHelper.TABLE_ATTA, null, DbKeys.AUTH_TYPE+"=?", new String[]{type.getIndex()+""}, null, null, null);

        List<Map<String, Object>> list = new ArrayList<>();
        while (c.moveToNext()){
            Map<String, Object> map = new HashMap<>();
            map.put(DbKeys.ID, c.getInt(c.getColumnIndex(DbKeys.ID)));
            map.put(DbKeys.FILE_NAME, c.getString(c.getColumnIndex(DbKeys.FILE_NAME)));
            map.put(DbKeys.FILE_PATH, c.getString(c.getColumnIndex(DbKeys.FILE_PATH)));
            list.add(map);
        }


        return list;
    }

    /**
     * 删除附件
     * @param context
     * @param name
     * @param type
     */
    @Override
    public void removeAttachment(Context context, String name, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_ATTA, DbKeys.AUTH_TYPE+" = ? and "+DbKeys.FILE_NAME+" = ?", new String[]{type.getIndex()+"", name});
    }

    @Override
    public void onClearAttachment(Context context, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_ATTA, DbKeys.AUTH_TYPE+" = ? ", new String[]{type.getIndex()+""});
    }

    /**
     * 插入附件
     * @param context
     * @param fileName
     * @param path
     * @param type
     */
    @Override
    public void insertAttachmentData(Context context, String fileName, String path, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbKeys.FILE_NAME, fileName);
        values.put(DbKeys.FILE_PATH, path);
        values.put(DbKeys.AUTH_TYPE, type.getIndex());
       db.insert(DbHelper.TABLE_ATTA, null, values);
    }


}
