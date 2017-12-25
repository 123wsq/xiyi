package com.example.wsq.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.LoddingDialog;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wsq on 2017/12/11.
 */

public class LoginActivity extends BaseActivity implements OnClickListener {


    private TextView tv_register;

    private  TextView tv_forget_pwd;
    private EditText et_username, et_password;

    private TextView btn_login;
    private ImageView im_eye;
    private CheckBox cb_checkBox;


    private UserService userService;
    SharedPreferences shared;
    private boolean isEyes = false;

    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_login;
    }

    @Override
    public void init() {
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        shared.edit().putBoolean(Constant.SHARED.ISLOGIN, true).commit();
        dialog = new LoddingDialog(this );
    }

    @Override
    public void initView() {
        tv_register = this.findViewById(R.id.tv_register);
        tv_forget_pwd = this.findViewById(R.id.tv_forget_pwd);
        et_username = this.findViewById(R.id.et_username);
        et_password = this.findViewById(R.id.et_password);
        im_eye = this.findViewById(R.id.im_eye);
        btn_login = this.findViewById(R.id.btn_login);
        cb_checkBox = this.findViewById(R.id.cb_checkBox);

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tv_register.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        im_eye.setOnClickListener(this);
        cb_checkBox.setOnClickListener(this);

        cb_checkBox.setChecked(true);
        et_username.setText(shared.getString(Constant.SHARED.USERNAME, ""));
        et_password.setText(shared.getString(Constant.SHARED.PASSWORD, ""));


    }


    @Override
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
                    map.put("username",username);
                    map.put("password",password);
                    try {

                        userService.login(map, new HttpResponseCallBack() {
                            @Override
                            public void callBack(Map<String, Object> result) {

                                if ((int)result.get(ResponseKey.CODE) == 1000){
                                    Toast.makeText(LoginActivity.this,
                                            result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(result.containsKey(ResponseKey.DATA)&& result.get(ResponseKey.DATA) instanceof Map){

                                    Map<String,Object> data = (Map<String, Object>) result.get(ResponseKey.DATA);

                                    shared.edit().putString(Constant.SHARED.TOKEN, data.get(ResponseKey.TOKEN).toString()).commit();
                                    shared.edit().putString(Constant.SHARED.JUESE, data.get(ResponseKey.JUESE).toString()).commit();
                                    shared.edit().putString(Constant.SHARED.NAME, data.get(ResponseKey.NAME).toString()).commit();
                                    shared.edit().putString(Constant.SHARED.TEL, data.get(ResponseKey.TEL).toString()).commit();
                                    shared.edit().putString(Constant.SHARED.COMPANY, data.get(ResponseKey.COMPANY).toString()).commit();
                                    IntentFormat.startActivity(LoginActivity.this, MainActivity.class);

                                    /**
                                     * 极光推送
                                     */
                                    JPushInterface.setAlias(LoginActivity.this, 0,data.get(ResponseKey.ID).toString());
                                    finish();

                                }


                            }

                            @Override
                            public void onCallFail(String msg) {
                                dialog.dismiss();
                            }


                        });
                    } catch (Exception e) {
                        e.printStackTrace();

                    }finally {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    break;

            }
    }
}
