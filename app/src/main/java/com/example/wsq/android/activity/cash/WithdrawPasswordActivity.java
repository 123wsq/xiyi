package com.example.wsq.android.activity.cash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.user.SettingActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.PasswordInputView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class WithdrawPasswordActivity extends Activity implements TextWatcher {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_psdInput)
    PasswordInputView et_psdInput;
    @BindView(R.id.tv_Prompt) TextView tv_Prompt;
    String passowrdOld = "";
    @BindView(R.id.ll_inputPassword)
    LinearLayout ll_inputPassword;
    @BindView(R.id.ll_bankInfo) LinearLayout ll_bankInfo;
    @BindView(R.id.et_backcode)
    EditText et_backcode;
    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_sfz) EditText et_sfz;
    @BindView(R.id.et_tel) EditText et_tel;

    public static final String ACTION = "com.example.wsq.android.activity.cash.WithdrawPasswordActivity";

    /**
     * 1 设置提现
     * 2 确认提现
     * 3 修改提现
     * 4 确认修改提现
     * 5 忘记提现
     * 6 确认忘记提现
     * 7 修改-原密码
     * 8 忘记-原密码
     */
    public static  int flagNum = 1;

    private UserService userService;
    private SharedPreferences shared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_withdraw_password);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        init();
    }

    public void init(){

        flagNum =  getIntent().getIntExtra("type", 0);
        setTitle();
        et_psdInput.setPasswordLength(Constant.PASSWORD_COUNT);
        et_psdInput.addTextChangedListener(this);
        userService = new UserServiceImpl();
        shared  = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        if (flagNum == 8){
            ll_inputPassword.setVisibility(View.GONE);
            ll_bankInfo.setVisibility(View.VISIBLE);
        }else{
            ll_inputPassword.setVisibility(View.VISIBLE);
            ll_bankInfo.setVisibility(View.GONE);
        }
    }
    public void setTitle(){

        switch (flagNum){
            case 1:
                tv_title.setText("设置提现密码");
                tv_Prompt.setText("请输入您的提现密码");
                break;
            case 2:
                tv_title.setText("设置提现密码");
                tv_Prompt.setText("请再次输入您的提现密码");
                break;
            case 3:
                tv_title.setText("修改提现密码");
                tv_Prompt.setText("请输入您新的提现密码");
                break;
            case 4:
                tv_title.setText("修改提现密码");
                tv_Prompt.setText("请确认您新的提现密码");
                break;
            case 5:
                tv_title.setText("忘记提现密码");
                tv_Prompt.setText("请输入您的提现密码");
                break;
            case 6:
                tv_title.setText("忘记提现密码");
                tv_Prompt.setText("请确认您新的提现密码");
                break;
            case 7:
                tv_title.setText("修改提现密码");
                tv_Prompt.setText("请确输入您旧提现密码");
                break;
            case 8:
                tv_title.setText("忘记提现密码");
                tv_Prompt.setText("请确输入您旧提现密码");
                break;
        }
    }

    @OnClick({R.id.tv_next, R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_next:
                onForgetValidateOldPassword();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String str = s.toString();
        if (str.length() ==Constant.PASSWORD_COUNT){
            onPassword(str);
        }

    }

    public void onPassword(String result){
        Map<String, Object> param = new HashMap<>();
        switch (flagNum){
            case 1:
                param.put("type", 2);
                param.put("password", result);
                IntentFormat.startActivity(this, WithdrawPasswordActivity.class, param);
                break;
            case 2:
                onSettingPassword(result);
                break;
            case 3:
                param.put("type", 4);
                param.put("password", result);
                IntentFormat.startActivity(this, WithdrawPasswordActivity.class, param);
                break;
            case 4:
                onSettingPassword(result);
                break;
            case 5:
                param.put("type", 6);
                param.put("password", result);
                IntentFormat.startActivity(this, WithdrawPasswordActivity.class, param);
                break;
            case 6:
                onSettingPassword(result);
                break;
            case 7:
                onUpdateValidateOldPassword(result);
                break;

        }
    }


    /**
     * 设置密码
     * @param password
     */
    public void onSettingPassword(final String password){

        String oldPassword = getIntent().getStringExtra("password");
        if (!password.equals(oldPassword)){
            Toast.makeText(WithdrawPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            IntentFormat.startActivity(WithdrawPasswordActivity.this, SettingActivity.class);
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        param.put(ResponseKey.PAY_PASSWORD, password);  //

        userService.onSettingPayPassword(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(WithdrawPasswordActivity.this,
                        result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                Map<String, Object> param = new HashMap<>();
                IntentFormat.startActivity(WithdrawPasswordActivity.this, SettingActivity.class);

                if (flagNum ==2){
                    Intent intent = new Intent();
                    intent.setAction(ACTION);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure() {

            }
        });



    }


    /**
     * 在修改密码时  验证密码
     * @param password  旧的密码
     */
    public void onUpdateValidateOldPassword(String password){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        param.put(ResponseKey.PAY_PASSWORD, password);  //这里的密码是旧密码

        userService.onUpdatePayPasswordValidate(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Toast.makeText(WithdrawPasswordActivity.this,
                        result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();

                Map<String, Object> param = new HashMap<>();
                param.put("type", 3);
                IntentFormat.startActivity(WithdrawPasswordActivity.this, WithdrawPasswordActivity.class, param);
            }

            @Override
            public void onFailure() {

            }
        });
//
    }

    /**
     * 在忘记密码时  验证密码
     */
    public void onForgetValidateOldPassword(){

        String bankCode = et_backcode.getText().toString();
        //信息验证
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        param.put(ResponseKey.NAME, et_name.getText().toString());  //这里的密码是旧密码
        param.put(ResponseKey.BANK_CARD, et_backcode.getText().toString());
        param.put(ResponseKey.SFZ, et_sfz.getText().toString());
        param.put(ResponseKey.TEL, et_tel.getText().toString());

        userService.onForgetPayPasswordValidate(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Toast.makeText(WithdrawPasswordActivity.this,
                        result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();

                Map<String, Object> param = new HashMap<>();
                param.put("type", 5);
                IntentFormat.startActivity(WithdrawPasswordActivity.this, WithdrawPasswordActivity.class, param);

            }

            @Override
            public void onFailure() {

            }
        });

    }
}
