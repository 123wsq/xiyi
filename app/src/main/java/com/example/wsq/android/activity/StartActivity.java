package com.example.wsq.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.utils.IntentFormat;


/**
 * Created by wsq on 2017/12/12.
 */

public class StartActivity extends BaseActivity{
    @Override
    public int getByLayoutId() {
        return R.layout.layout_start;
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {

        handler.post(thread);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SharedPreferences shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
            if(shared.getBoolean(Constant.SHARED.ISLOGIN, false)){
                IntentFormat.startActivity(StartActivity.this, LoginActivity.class);
            }else{
                IntentFormat.startActivity(StartActivity.this, WellcomeActivity.class);
            }
            finish();
        }
    };

    Runnable thread = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessageDelayed(0, 1*1000);
        }
    };
}
