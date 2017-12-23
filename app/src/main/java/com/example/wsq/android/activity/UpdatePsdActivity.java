package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.wsq.android.utils.ValidateParam;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 * Created by wsq on 2017/12/19.
 */

public class UpdatePsdActivity extends Activity{

    @BindView(R.id.et_new_password1) EditText et_new_password1;
    @BindView(R.id.et_new_password2) EditText et_new_password2;
    @BindView(R.id.et_old_password) EditText et_old_password;
    @BindView(R.id.tv_title) TextView tv_title;

    private UserService userService;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_update_password);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        tv_title.setText("修改密码");
    }

    @OnClick({R.id.tv_submit_password, R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_submit_password:

                if (validate()){
                submitPassword();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void submitPassword(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.NEW_PSD, et_new_password1.getText().toString());
        param.put(ResponseKey.OLD_PSD, et_old_password.getText().toString());

        try {
            userService.updateUserPassword(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    Toast.makeText(UpdatePsdActivity.this, result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                    if ((int)result.get(ResponseKey.CODE) == 1001){
                        finish();
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(UpdatePsdActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证参数
     * @return
     */
    public boolean validate(){

        String oldPsd = et_old_password.getText().toString();
        String psd1 = et_new_password1.getText().toString();
        String psd2 = et_new_password2.getText().toString();
        //验证旧的密码
        if(ValidateParam.validateParamIsNull(oldPsd)){
            Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if (oldPsd.length() < 6){
                Toast.makeText(this, "旧密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //验证新密码
        if (ValidateParam.validateParamIsNull(psd1, psd2)){
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else{

            if (!psd1.equals(psd2)){
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (psd1.length()<6){
                Toast.makeText(this, "新密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return true;
    }
}
