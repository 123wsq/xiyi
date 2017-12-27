package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.BankInfo;
import com.example.wsq.android.view.LoddingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class WithdrawActivity extends Activity{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_withdraw_enabled)
    TextView tv_withdraw_enabled;
    @BindView(R.id.tv_bank) TextView tv_bank;
    @BindView(R.id.tv_card_type) TextView tv_card_type;

    private LoddingDialog dialog;

    private UserService userService;
    private SharedPreferences shared;
    private double enabledMoney = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_withdraw);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        dialog = new LoddingDialog(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        tv_title.setText("提现");
        tv_withdraw_enabled.setText("可用余额为"
                +BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+"");

        enabledMoney = Double.parseDouble(BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+"");
        String bankcard = BalanceActivity.mData.get(ResponseKey.BANK_CARD)+"";
        if (!TextUtils.isEmpty(bankcard)) {
            String str[] = BankInfo.getNameOfBank(bankcard.toCharArray(), 0).split("_");
            tv_bank.setText(str[0] + " (" + bankcard.substring(bankcard.length() - 4) + ")");
            tv_card_type.setText(str[1]);
        }
    }

    @OnClick({R.id.tv_submit, R.id.tv_withdraw_all, R.id.iv_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                startWithdraw();
                break;
            case R.id.tv_withdraw_all:
                et_money.setText(enabledMoney+"");
                break;
        }

    }


    /**
     * 开始提现
     */
    public void startWithdraw(){



        if (TextUtils.isEmpty(et_money.getText().toString())){
            Toast.makeText(WithdrawActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            return;
        }
        double money = Double.parseDouble(et_money.getText().toString());
        if (money % 100 != 0){
            Toast.makeText(WithdrawActivity.this, "提现金额必须为100元的整数倍", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.MONEY, et_money.getText().toString());

        try {
            userService.onAddCash(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    Toast.makeText(WithdrawActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
