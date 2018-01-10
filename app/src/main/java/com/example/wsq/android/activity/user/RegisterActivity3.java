package com.example.wsq.android.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.ProtocolsActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.IdentityCardValidate;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.utils.ValidateParam;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegisterActivity3 extends Activity {


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_protocols) TextView tv_protocols;
    @BindView(R.id.tv_birth) TextView tv_birth;
    @BindView(R.id.tv_register) TextView tv_register;
    @BindView(R.id.tv_getCode) TextView tv_getCode;
    @BindView(R.id.et_sfz) EditText et_sfz;
    @BindView(R.id.et_tel) EditText et_tel;
    @BindView(R.id.et_validateCode) EditText et_validateCode;
    @BindView(R.id.cb_checkBox) CheckBox cb_checkBox;

    private UserService userService;
    private int curLen = 60;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register3);
        ButterKnife.bind(this);
        init();
        initView();
    }

    public void init() {

        userService = new UserServiceImpl();
        tv_title.setText("会员注册");
    }

    public void initView() {

        et_sfz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if (str.length() >= 14){
                    String birth = str.substring(6, 14);

                    tv_birth.setText(birth.substring(0,4)+"-"+birth.substring(4,6)+"-"+birth.substring(6));
                }

            }
        });
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
    @OnClick({R.id.tv_protocols, R.id.iv_back, R.id.tv_register, R.id.tv_getCode})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_protocols:  //点击阅读协议

                    startActivity(new Intent(RegisterActivity3.this, ProtocolsActivity.class));

                break;
            case  R.id.iv_back:
                moveTaskToBack(false);
//                finish();
                break;
            case R.id.tv_register:  //注册
                if (validateParam(0)){
                    if (cb_checkBox.isChecked()) {
                        Map<String, String> map = new HashMap<>();
                        userService.register(this, map, new HttpResponseListener() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                IntentFormat.startActivity(RegisterActivity3.this, LoginActivity.class);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });

                    }else{
                        Toast.makeText(RegisterActivity3.this, "请选择阅读协议", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity3.this, "输入参数格式错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_getCode: //获取验证码
                if(validateParam(1)){
                    Map<String, String> map = new HashMap<>();
                    userService.getValidateCode(this, map, new HttpResponseListener() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            handler.postDelayed(runnable, 1000);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity3.this, "输入参数格式错误", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    /**
     *
     * @param flag  0 表示注册  1 表示获取验证码
     * @return
     */
    public boolean validateParam(int flag){
        if(flag ==0){
            //验证身份证
            String sfz = et_sfz.getText().toString();

            if (TextUtils.isEmpty(sfz)){
                Toast.makeText(RegisterActivity3.this, "身份证号码不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }

            try {
                if (!TextUtils.isEmpty(IdentityCardValidate.IDCard.IDCardValidate(sfz))){
                    Toast.makeText(RegisterActivity3.this, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                Toast.makeText(RegisterActivity3.this, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
            RegisterParam.SFZ = sfz;
            RegisterParam.BIRTH = tv_birth.getText().toString();

            //验证验证码
            String validateCode = et_validateCode.getText().toString();
            if(ValidateParam.validateParamIsNull(validateCode)){
                Toast.makeText(RegisterActivity3.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                RegisterParam.YZM = validateCode;
            }

            if(validateCode.length()!= Constant.CODE_LENGTH){
                Toast.makeText(RegisterActivity3.this, "验证码必须为"+Constant.CODE_LENGTH+"位", Toast.LENGTH_SHORT).show();
            }
        }

        //验证手机号码
        String tel = et_tel.getText().toString();
        if(ValidateDataFormat.isMobile(tel)){
            RegisterParam.TEL = tel;
        }else{
            Toast.makeText(RegisterActivity3.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
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
