package com.example.wsq.android.activity.user;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.utils.ValidateParam;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/11.
 */


public class ForgetPsdActivity extends Activity{

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.et_tel) EditText et_tel;
    @BindView(R.id.tv_getCode) TextView tv_getCode;
    @BindView(R.id.et_validateCode) EditText et_validateCode;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password1) EditText et_password1;
    @BindView(R.id.et_password2) EditText et_password2;
    @BindView(R.id.tv_ok) TextView  tv_ok;

    private UserService userService;
    private int curLen = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_psd);

        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        userService = new UserServiceImpl();
        tv_ok = this.findViewById(R.id.tv_ok);
        tv_title.setText("忘记密码");

    }


    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 0){
                curLen = 60;
                tv_getCode.setText("获取验证码");
                tv_getCode.setBackgroundColor(getResources().getColor(R.color.defalut_title_color));
                tv_getCode.setClickable(true);
            }else{
                tv_getCode.setText("请耐心等待 "+curLen+"s");
                tv_getCode.setClickable(false);
                tv_getCode.setBackgroundColor(Color.parseColor("#A8A8A8"));
                handler.postDelayed(this, 1000);
            }

        }
    };

    @OnClick({R.id.iv_back, R.id.tv_getCode, R.id.tv_ok})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
//                finish();
                moveTaskToBack(false);
                break;
            case R.id.tv_getCode:


                //验证手机号码
                String tel = et_tel.getText().toString();
                if(!ValidateDataFormat.isMobile(tel)){

                    Toast.makeText(ForgetPsdActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterParam.TEL = tel;
                Map<String, String> map = new HashMap<>();
                map.put(ResponseKey.TEL, tel);

                userService.getValidateCode(this, map, new HttpResponseListener() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        handler.postDelayed(runnable, 1000);
                    }

                    @Override
                    public void onFailure() {

                    }
                });

                break;
            case R.id.tv_ok:
                if(validate()){
                    Map<String, String> param = new HashMap<>();
                    param.put(ResponseKey.USERNAME, et_username.getText().toString());
                    param.put(ResponseKey.NEW_PSD, et_password1.getText().toString());
                    param.put(ResponseKey.TEL, et_tel.getText().toString());
                    param.put(ResponseKey.YZM, et_validateCode.getText().toString());
                    userService.updatePassword(this, param, new HttpResponseListener() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Toast.makeText(ForgetPsdActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();
                            IntentFormat.startActivity(ForgetPsdActivity.this, LoginActivity.class);
                            finish();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                }

                break;
        }
    }

    public boolean validate(){

        //验证手机号码
        String tel = et_tel.getText().toString();
        if(ValidateDataFormat.isMobile(tel)){
        }else{
            Toast.makeText(ForgetPsdActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
            return false;
        }


        //验证密码是否一致
        String password1 = et_password1.getText().toString();
        String password2 = et_password2.getText().toString();
        //检验密码是否为空
        if (ValidateParam.validateParamIsNull(password1, password2)){
            Toast.makeText(ForgetPsdActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //检验密码是否两次输入一致
        if (!password1.equals(password2)){
            Toast.makeText(ForgetPsdActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        //检验密码长度
        if(password1.length()<=16 && password1.length() >= 6){

        }else{
            Toast.makeText(ForgetPsdActivity.this, "密码长度不能为"+password1.length(), Toast.LENGTH_SHORT).show();
            return false;
        }


        //用户名验证
        String userName = et_username.getText().toString();
        //输入的字母是否正确
        if(!userName.matches("[A-Za-z0-9_]+")){
            Toast.makeText(ForgetPsdActivity.this, "用户名只能为数字，字母，下划线", Toast.LENGTH_SHORT).show();
            return false;
        }
        //输入的长度是否正确 6-18
        if(userName.length()>= 6 && userName.length() <= 18){

        }else {
            Toast.makeText(ForgetPsdActivity.this, "用户名长度不能为"+userName.length(), Toast.LENGTH_SHORT).show();
            return false;
        }


        //验证验证码
        String code = et_validateCode.getText().toString();

        if (ValidateParam.validateParamIsNull(code)){
            Toast.makeText(ForgetPsdActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(code.length()!= Constant.CODE_LENGTH){
            Toast.makeText(ForgetPsdActivity.this, "验证码必须为"+Constant.CODE_LENGTH, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
