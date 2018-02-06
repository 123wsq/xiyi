package com.example.wsq.android.activity.cash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnPasswordClickListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.AmountUtils;
import com.example.wsq.android.utils.BankInfo;
import com.example.wsq.android.utils.DataFormat;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ToastUtils;
import com.example.wsq.android.view.CustomPasswordDialog;
import com.example.wsq.android.view.LoddingDialog;
import com.orhanobut.logger.Logger;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 体现页面
 * Created by wsq on 2017/12/26.
 */

public class WithdrawActivity extends BaseActivity {


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
    private CustomPasswordDialog passwordDialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_withdraw;
    }

    public void init(){

        userService = new UserServiceImpl();
        dialog = new LoddingDialog(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        tv_title.setText("提现");
        tv_withdraw_enabled.setText("可体现金额为 "
                +BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+" 元");


        enabledMoney = Double.parseDouble(BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+"");
        String bankcard = BalanceActivity.mData.get(ResponseKey.BANK_CARD)+"";
        if (!TextUtils.isEmpty(bankcard)) {
            String[] str = BankInfo.getNameOfBank(this, Long.parseLong(bankcard.substring(0, 6))).split("-");
            tv_bank.setText(str[0] + " (" + bankcard.substring(bankcard.length() - 4) + ")");
            if (str.length==3){
                tv_card_type.setText(str[2]);
            }

        }
        onTextChangeListener();

    }

    @OnClick({R.id.tv_submit, R.id.tv_withdraw_all, R.id.iv_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                onInputPasswordDialog();
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
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.MONEY, et_money.getText().toString());

        userService.onAddCash(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(WithdrawActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

            @Override
            public void onFailure() {
                if (dialog.isShowing())dialog.dismiss();
            }
        });

    }

    private void onValidatePayPassword(String password){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        param.put(ResponseKey.PAY_PASSWORD, password);  //这里的密码是旧密码

        userService.onUpdatePayPasswordValidate(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                startWithdraw();
            }

            @Override
            public void onFailure() {

                ToastUtils.onToast(WithdrawActivity.this, "支付密码错误");
            }
        });
    }

    public void onTextChangeListener(){
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                double amountNew = DataFormat.onStringForFloat(editable.toString());
                double amountOld = DataFormat.onStringForFloat(BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+"");
                Logger.d(amountNew+"=============="+ amountOld);
                if (amountNew > amountOld){
                    tv_withdraw_enabled.setText("金额已超过可提现余额");
                    tv_withdraw_enabled.setTextColor(Color.RED);
                }else{
                    tv_withdraw_enabled.setText("可体现金额为 "
                            +BalanceActivity.mData.get(ResponseKey.CASH_MONEY)+" 元");
                    tv_withdraw_enabled.setTextColor(Color.parseColor("#9A9A9A"));
                }
            }
        });
    }

    public void onInputPasswordDialog(){
        CustomPasswordDialog.Builder builder = new CustomPasswordDialog.Builder(this);
        builder.setOnClickListener(new OnPasswordClickListener() {
            @Override
            public void onClick(CustomPasswordDialog dialog, String result) {
                dialog.dismiss();
                onValidatePayPassword(result);
            }
        });
        passwordDialog = builder.create();
        passwordDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (passwordDialog != null && passwordDialog.isShowing())
            passwordDialog.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
