package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.LoddingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额界面
 * Created by wsq on 2017/12/26.
 */

public class BalanceActivity extends Activity{

    public static  Map<String, Object> mData;
    @BindView(R.id.tv_money_amount)
    TextView tv_money_amount;
    @BindView(R.id.tv_money) TextView tv_money;

    private UserService userService;
    private SharedPreferences shared;
    private LoddingDialog dialog;
    private String bailState = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_balance);
        ButterKnife.bind(this);
        init();
        onGetMoney();
    }

    public void init(){

        mData = new HashMap<>();
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_money_amount.setText(UserFragment.mUserData.get(ResponseKey.MONEY_AMOUNT)+"");
        dialog = new LoddingDialog(this);

    }

    @OnClick({R.id.iv_back, R.id.tv_bill_Details, R.id.ll_bank_manager, R.id.ll_deposit, R.id.ll_withdraw})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_bill_Details:  //账单明细
                IntentFormat.startActivity(this, BillDetailsActivity.class);
                break;
            case R.id.ll_bank_manager:  //银行卡管理
                IntentFormat.startActivity(this, BankActivity.class);
                break;
            case R.id.ll_deposit:// 保证金
                if (TextUtils.isEmpty(bailState)) {
                    IntentFormat.startActivity(this, CashDepositActivity.class);
                }else{
                    IntentFormat.startActivity(this, CashDepositListActivity.class);
                }
                break;
            case R.id.ll_withdraw:   //提现
                IntentFormat.startActivity(this, WithdrawActivity.class);
                break;
        }
    }

    public void onGetMoney(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        try {
            userService.onMyMoney(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    mData.putAll(result);
                    tv_money_amount.setText(result.get(ResponseKey.MY_MONEY)+"");
                    tv_money.setText("(可提现金额"+
                            result.get(ResponseKey.CASH_MONEY)+"元，保证金金额"+
                            result.get(ResponseKey.DEPOSIT_MOENY)+"元整)");

                    bailState = result.get(ResponseKey.BAIL_STATE)+"";
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
}
