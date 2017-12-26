package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class CashDepositInfoActivity extends Activity{

    @BindView(R.id.tv_title)
    TextView tv_title;

    SharedPreferences shared;
    private UserService userService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cash_deposit_info);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        tv_title.setText("退保证金");
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        getCashDepositInfo();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取保证金详情
     */
    public void getCashDepositInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.BAIL_ID, getIntent().getIntExtra(ResponseKey.BAIL_ID, 0)+"");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        userService.onApplyCashDepositInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

            }
        });

    }
}
