package com.example.wsq.android.db.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wsq.android.db.DbHelper;
import com.example.wsq.android.db.dao.DbKeys;
import com.example.wsq.android.db.dao.inter.SearchDbInter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/18.
 */

public class SearchDbImpl implements SearchDbInter{

    private DbHelper helper;

    @Override
    public List<Map<String, String>> selectAll(Context context) {

        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(DbHelper.TABLE_SEARCH, null, null, null, null, null, DbKeys.UPDATE_TIME+" desc");

        List<Map<String, String>> list = new ArrayList<>();
        while (c.moveToNext()){
            Map<String, String> map = new HashMap<>();

            map.put(DbKeys.ID, c.getInt(c.getColumnIndex(DbKeys.ID))+"");
            map.put(DbKeys.CONTENT, c.getString(c.getColumnIndex(DbKeys.CONTENT)));
            map.put(DbKeys.CREATE_TIME, c.getString(c.getColumnIndex(DbKeys.CREATE_TIME)));
            map.put(DbKeys.UPDATE_TIME, c.getString(c.getColumnIndex(DbKeys.UPDATE_TIME)));
            list.add(map);
        }
        c.close();
        return list;
    }

    @Override
    public void insertData(Context context, String content) {
        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor c = db.query(DbHelper.TABLE_SEARCH, null, DbKeys.CONTENT+"=?", new String[]{content}, null, null, null);
        if (c.getCount() == 0){
            ContentValues values = new ContentValues();
            values.put(DbKeys.CONTENT, content.trim());
            values.put(DbKeys.CREATE_TIME, System.currentTimeMillis());
            values.put(DbKeys.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DbHelper.TABLE_SEARCH, null, values);
        }else {
            ContentValues values = new ContentValues();
            values.put(DbKeys.UPDATE_TIME, System.currentTimeMillis());
            db.update(DbHelper.TABLE_SEARCH, values, DbKeys.CONTENT+"=?", new String[]{content});
        }
    }

    @Override
    public void removeData(Context context, String content) {
        helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DbHelper.TABLE_SEARCH, DbKeys.CONTENT+"=?", new String[]{content});
    }
}
