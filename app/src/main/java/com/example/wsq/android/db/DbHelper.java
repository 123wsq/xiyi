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
    private static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "FAULTS";
    public static final String TABLE_SEARCH = "SEARCH_RECORD";
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql1 = "CREATE TABLE "
                +TABLE_NAME +
                "("+ DbKeys.ID          +   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbKeys.CONTENT          +   " TEXT,    "+
                DbKeys.FILE_NAME        +   " VARCHAR, "+
                DbKeys.TYPE             +   " INTEGER, "+
                DbKeys.FONT_SIZE        +   " INTEGER, "+
                DbKeys.FONT_COLOR       +   " VARCHAR, "+
                DbKeys.ADD_TIME         +   " VARCHAR, "+
                DbKeys.KEY              +   " VARCHAR, "+
                DbKeys.AUTH_TYPE        +   " INTEGER  "+
                ")";
        String sql2 = "CREATE TABLE "   +
                TABLE_SEARCH+
                "("+ DbKeys.ID          +   " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbKeys.CONTENT          +   " TEXT, "+
                DbKeys.CREATE_TIME      +   " VARCHAR, "+
                DbKeys.UPDATE_TIME      +   " VARCHAR"+
                ")";
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
