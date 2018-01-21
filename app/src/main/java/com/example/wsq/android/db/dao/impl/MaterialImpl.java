package com.example.wsq.android.db.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.db.DbHelper;
import com.example.wsq.android.db.dao.DbKeys;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.example.wsq.android.tools.HtmlContent;
import com.example.wsq.android.utils.DataFormat;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsq on 2018/1/17.
 */

public class MaterialImpl implements DbConInter {

    private DbHelper helper;


    @Override
    public Map<String, WebContentBean> selectAll(Context context, AuthType authType) throws Exception{

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor c = db.query(DbHelper.TABLE_NAME, null, DbKeys.AUTH_TYPE+"=?", new String[]{authType.getIndex()+""}, null, null, DbKeys.ADD_TIME);


        Map<String, WebContentBean> map = new HashMap<>();

        while (c.moveToNext()) {
            WebContentBean bean = new WebContentBean();

            int id = c.getInt(c.getColumnIndex(DbKeys.ID));
            String content = c.getString(c.getColumnIndex(DbKeys.CONTENT));
            long addTime = DataFormat.onStringForLong(c.getString(c.getColumnIndex(DbKeys.ADD_TIME)));
            String fontColor = c.getString(c.getColumnIndex(DbKeys.FONT_COLOR));
            int fontSize = c.getInt(c.getColumnIndex(DbKeys.FONT_SIZE));
            int type = c.getInt(c.getColumnIndex(DbKeys.TYPE));
            String fileName = c.getString(c.getColumnIndex(DbKeys.FILE_NAME));
            int auth_Type = c.getInt(c.getColumnIndex(DbKeys.AUTH_TYPE));

            bean.setId(id);
            bean.setContent(content);
            bean.setAddTime(addTime);
            bean.setFontColor(fontColor);
            bean.setFontSize(fontSize);
            bean.setFileName(fileName);
            if (auth_Type == AuthType.NEWS.getIndex()){
                bean.setAuthType(AuthType.NEWS);
            }else if (auth_Type == AuthType.RESOURCE.getIndex()){
                bean.setAuthType(AuthType.RESOURCE);
            }else if (auth_Type == AuthType.ARTICLES.getIndex()){
                bean.setAuthType(AuthType.ARTICLES);
            }


            if (type == ContentType.TITLE.getIndex()) {
                bean.setHtmlContent(HtmlContent.onTitleHtml(content, Constant.TITLE_COLOR, Constant.TITLE_SIZE));
            } else if (type == ContentType.CREATE_TIME.getIndex()) {
                bean.setHtmlContent(HtmlContent.onCreateTimeHtml(content, Constant.CONTENT_COLOR, Constant.CONTENT_SIZE));
            } else if (type == ContentType.CONTENT_FONT.getIndex()) {
                bean.setHtmlContent(HtmlContent.onContentHtml(content, Constant.CONTENT_COLOR, Constant.CONTENT_SIZE));
            } else if (type == ContentType.CONTENT_IMG.getIndex()) {
                bean.setHtmlContent(HtmlContent.onImageHtml(content));
            } else if (type == ContentType.ADDACHMENT.getIndex()) {
                if (!TextUtils.isEmpty(fileName)){
                    bean.setHtmlContent(HtmlContent.onAddachmentHtml(content, fileName));
                }
            }
            bean.setType(ContentType.getType(type));
            String key = c.getString(c.getColumnIndex(DbKeys.KEY));

            Logger.d("id= "+id +", content = "+content +", addTime= "+addTime+", fontColor = "+fontColor
                    +", fontSize = "+fontSize+", type = "+type +", fileName = "+fileName+", key = "+key );
            map.put(key, bean);
        }
        c.close();
        return map;
    }

    @Override
    public void removeData(Context context, String key, AuthType authType) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_NAME, DbKeys.KEY + "=? and "+DbKeys.AUTH_TYPE+"=? ", new String[]{key, authType.getIndex()+""});
    }

    @Override
    public void updateData(Context context, String key, WebContentBean bean) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbKeys.CONTENT, bean.getContent());
        values.put(DbKeys.FILE_NAME, bean.getFileName());

        db.update(DbHelper.TABLE_NAME, values, DbKeys.KEY + "=? and "+DbKeys.AUTH_TYPE+"=? ", new String[]{key, bean.getAuthType().getIndex()+""});
    }

    @Override
    public void insertData(Context context, String key, WebContentBean bean) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbKeys.ADD_TIME, bean.getAddTime());
        values.put(DbKeys.CONTENT, bean.getContent());
        values.put(DbKeys.KEY, key);
        values.put(DbKeys.FONT_COLOR, bean.getFontColor());
        values.put(DbKeys.FONT_SIZE, bean.getFontSize());
        values.put(DbKeys.FILE_NAME, bean.getFileName());
        values.put(DbKeys.TYPE, bean.getType().getIndex());
        values.put(DbKeys.AUTH_TYPE, bean.getAuthType().getIndex());
        long state = db.insert(DbHelper.TABLE_NAME, null, values);
        Logger.d("id= "+state+"\n======"+bean.toString());
    }

    @Override
    public void removeAll(Context context, AuthType authType) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_NAME, DbKeys.AUTH_TYPE+"=? ", new String[]{authType.getIndex()+""});
    }

    @Override
    public int selectCount(Context context, AuthType type) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();


        Cursor c = db.query(DbHelper.TABLE_NAME, null, DbKeys.AUTH_TYPE+"=? ", new String[]{type.getIndex()+""}, null, null, null);
        int num = c.getCount();
        c.close();
        return num;
    }
}
