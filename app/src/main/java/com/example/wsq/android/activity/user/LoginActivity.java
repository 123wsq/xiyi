package com.example.wsq.android.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.MainActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.LoddingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by wsq on 2017/12/11.
 */

public class LoginActivity extends Activity implements OnClickListener {


    @BindView(R.id.tv_register) TextView tv_register;

    @BindView(R.id.tv_forget_pwd)  TextView tv_forget_pwd;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password)EditText et_password;

    @BindView(R.id.btn_login) TextView btn_login;
    @BindView(R.id.im_eye) ImageView im_eye;
    @BindView(R.id.cb_checkBox) CheckBox cb_checkBox;


    private UserService userService;
    SharedPreferences shared;
    private boolean isEyes = false;

    private LoddingDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_login);

        ButterKnife.bind(this);
        init();
        initView();
    }

    public void init() {
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        shared.edit().putBoolean(Constant.SHARED.ISLOGIN, true).commit();
        dialog = new LoddingDialog(this );
    }

    public void initView() {

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        cb_checkBox.setChecked(true);
        et_username.setText(shared.getString(Constant.SHARED.USERNAME, ""));
        et_password.setText(shared.getString(Constant.SHARED.PASSWORD, ""));


    }


    @OnClick({R.id.tv_register, R.id.tv_forget_pwd, R.id.im_eye, R.id.btn_login})
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_register:

                    startActivity(new Intent(LoginActivity.this, RegiesterActivity1.class));
                    break;
                case  R.id.tv_forget_pwd:
                    startActivity(new Intent(LoginActivity.this, ForgetPsdActivity.class));
                    break;
                case R.id.im_eye:
                    if (isEyes){
                        isEyes = false;
                        im_eye.setImageResource(R.drawable.image_eye_close);
                        //设置显示密码
                        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }else {
                        isEyes = true;
                        im_eye.setImageResource(R.drawable.image_eye_open);
                        //设置影藏密码
                        et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    break;
                case R.id.btn_login:

                    onLogin();
                    break;

            }
    }

    /**
     * 开始登录
     */
    public void onLogin(){
        dialog.show();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        //检测复选框的状态
        if(cb_checkBox.isChecked()){
            shared.edit()
                    .putString(Constant.SHARED.USERNAME, username)
                    .putString(Constant.SHARED.PASSWORD, password)
                    .commit();

        }else{
            shared.edit()
                    .putString(Constant.SHARED.USERNAME, "")
                    .putString(Constant.SHARED.PASSWORD, "")
                    .commit();
        }
        Map<String, String> map = new HashMap<>();
        map.put(ResponseKey.USERNAME,username);
        map.put(ResponseKey.PASSWORD,password);

        userService.login(this, map, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Map<String,Object> data = (Map<String, Object>) result.get(ResponseKey.DATA);

                shared.edit().putString(Constant.SHARED.TOKEN, data.get(ResponseKey.TOKEN).toString()).commit();
                shared.edit().putString(Constant.SHARED.JUESE, data.get(ResponseKey.JUESE).toString()).commit();
                shared.edit().putString(Constant.SHARED.NAME, data.get(ResponseKey.NAME).toString()).commit();
                shared.edit().putString(Constant.SHARED.TEL, data.get(ResponseKey.TEL).toString()).commit();
                shared.edit().putString(Constant.SHARED.COMPANY, data.get(ResponseKey.COMPANY).toString()).commit();
                shared.edit().putString(Constant.SHARED.ID, data.get(ResponseKey.ID).toString()).commit();
                IntentFormat.startActivity(LoginActivity.this, MainActivity.class);

                /**
                 * 极光推送
                 */
                JPushInterface.setAlias(LoginActivity.this, 0,data.get(ResponseKey.ID).toString());
                finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });
    }



}