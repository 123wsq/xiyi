package com.example.wsq.android.db.dao.inter;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/18.
 */

public interface SearchDbInter {

    List<Map<String, String>> selectAll(Context context);

    void insertData(Context context, String content);

    void removeData(Context context, String content);

    void onClearAll(Context context);
}
