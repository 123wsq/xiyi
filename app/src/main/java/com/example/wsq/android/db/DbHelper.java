package com.example.wsq.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wsq.android.db.dao.DbKeys;

/**
 * Created by wsq on 2018/1/17.
 */

public class DbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "xiyi.db";
    private static final int DB_VERSION = 8;
    public static final String TABLE_NAME = "FAULTS";
    public static final String TABLE_SEARCH = "SEARCH_RECORD";
    public static final String TABLE_ATTA = "ATTACHMENT";
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql1 = "CREATE TABLE "
                +TABLE_NAME +
                "("+ DbKeys.ID          +   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbKeys.CONTENT          +   " TEXT,    "+
                DbKeys.TITLE            +   " VARCHAR, "+
                DbKeys.ICON             +   " VARCHAR, "+
                DbKeys.DESCRIPTION      +   " VARCHAR, "+
                DbKeys.CATEGORY         +   " INTEGER, "+
                DbKeys.ATTACHMENT       +   " VARCHAR, "+
                DbKeys.AUTH_TYPE        +   " INTEGER,  "+
                DbKeys.CREATE_TIME      +   " VARCHAR, "+
                DbKeys.UPDATE_TIME      +   " VARCHAR "+
                ")";
        String sql2 = "CREATE TABLE "   +
                TABLE_SEARCH+
                "("+ DbKeys.ID          +   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbKeys.CONTENT          +   " TEXT, "+
                DbKeys.CREATE_TIME      +   " VARCHAR, "+
                DbKeys.UPDATE_TIME      +   " VARCHAR"+
                ")";

        String sql3 = "CREATE TABLE "   +
                TABLE_ATTA+
                "("+ DbKeys.ID          +   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbKeys.FILE_NAME        +   " VARCHAR, "+
                DbKeys.FILE_PATH        +   " VARCHAR, "+
                DbKeys.AUTH_TYPE        +   " INTEGER "+
                ")";
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_SEARCH;
        String sql3 = "DROP TABLE IF EXISTS " + TABLE_ATTA;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        onCreate(sqLiteDatabase);
    }
}
