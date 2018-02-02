package com.example.wsq.android.activity.user;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.ProtocolsActivity;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.IdentityCardValidate;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.SystemUtils;
import com.example.wsq.android.utils.ToastUtils;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.utils.ValidateParam;
import com.example.wsq.android.view.LoddingDialog;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegisterActivity3 extends BaseActivity {


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
    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_register3;
    }

    public void init() {

        userService = new UserServiceImpl();
        tv_title.setText("会员注册");
        dialog = new LoddingDialog(this);
        onTextChange();
        onValidatePhone();
    }

    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 0){
                curLen = 60;
                tv_getCode.setText("获取验证码");
                    tv_getCode.setBackgroundColor(R.drawable.shape_button);
                tv_getCode.setClickable(true);
            }else{
                tv_getCode.setText("请耐心等待 "+curLen+"s");
                tv_getCode.setClickable(false);
                
                tv_getCode.setBackgroundColor(R.drawable.shape_disable_button);

                handler.postDelayed(this, 1000);
            }

        }
    };
    @OnClick({R.id.tv_protocols, R.id.iv_back, R.id.tv_register, R.id.tv_getCode})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_protocols:  //点击阅读协议

                    startActivity(new Intent(RegisterActivity3.this,  ProtocolsActivity.class));

                break;
            case  R.id.iv_back:
                moveTaskToBack(false);
//                finish();
                break;
            case R.id.tv_register:  //注册

                onRegister();
                break;
            case R.id.tv_getCode: //获取验证码
                tv_getCode.setClickable(false);
                getValidateCode();

                break;
        }
    }

    /**
     * 文本输入监听
     */
    public void onTextChange(){

        cb_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    tv_register.setBackgroundResource(R.drawable.shape_button);
                    tv_register.setClickable(true);
                }else{
                    tv_register.setBackgroundResource(R.drawable.shape_disable_button);
                    tv_register.setClickable(false);
                }
            }
        });
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
        et_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length()==11){
                    tv_getCode.setBackgroundResource(R.drawable.shape_button);
                    tv_getCode.setClickable(true);
                }else{
                    tv_getCode.setBackgroundResource(R.drawable.shape_disable_button);
                    tv_getCode.setClickable(false);
                }
            }
        });
    }


    /**
     *
     * @param flag  0 表示注册  1 表示获取验证码
     * @return
     */
    public boolean validateParam(int flag){
        if (flag == 0) {
            //验证身份证
            String sfz = et_sfz.getText().toString();

            if (TextUtils.isEmpty(sfz)) {
                ToastUtis.onToast("身份证号码不能为空");
                return false;
            }

            try {
                if (!TextUtils.isEmpty(IdentityCardValidate.IDCard.IDCardValidate(sfz))) {
                    ToastUtis.onToast("请输入正确的身份证号码");
                    return false;
                }
            } catch (ParseException e) {
                ToastUtis.onToast("请输入正确的身份证号码");
                e.printStackTrace();
                return false;
            }
            RegisterParam.SFZ = sfz;
            RegisterParam.BIRTH = tv_birth.getText().toString();

            //验证验证码
            String validateCode = et_validateCode.getText().toString();
            if(ValidateParam.validateParamIsNull(validateCode)){
                ToastUtis.onToast( "验证码不能为空");
                return false;
            }else{
                RegisterParam.YZM = validateCode;
            }

            if(validateCode.length()!= Constant.CODE_LENGTH){
                ToastUtis.onToast( "验证码必须为"+Constant.CODE_LENGTH+"位");
                return false;
            }
        }
            //验证手机号码
            String tel = et_tel.getText().toString();
            if (TextUtils.isEmpty(tel)){
                ToastUtis.onToast("请输入手机号码");
                return false;
            }
            if(ValidateDataFormat.isMobile(tel)){
                RegisterParam.TEL = tel;
            }else{
                ToastUtis.onToast( "输入的手机号有误");
                return false;
            }







        return true;
    }

    /**
     * 用户注册
     */
    public void onRegister(){

        if (validateParam(0)){
            if (cb_checkBox.isChecked()) {
                dialog.show();
                Map<String, String> map = new HashMap<>();
                userService.register(this, map, new HttpResponseListener() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {

                        ToastUtils.onToast(RegisterActivity3.this, result.get(ResponseKey.MESSAGE)+"");
                        IntentFormat.startActivity(RegisterActivity3.this,  LoginActivity.class);
                        finish();
                        if(dialog.isShowing())dialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        if(dialog.isShowing())dialog.dismiss();
                    }
                });

            }else{
                ToastUtis.onToast( "请选择阅读协议");
            }
        }
    }

    /**
     * 验证手机  获取积分
     */
    public void onValidatePhone(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.DEVICE_TYPE, "1");
        param.put(ResponseKey.DEVICE_ID, SystemUtils.getIMEI(this));

        userService.validatePhone(this, param);
    }


    /**
     * 获取验证码
     */
    public void getValidateCode(){

        if(validateParam(1)){
            Map<String, String> map = new HashMap<>();
            userService.getValidateCode(this, map, new HttpResponseListener() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    handler.postDelayed(runnable, 1000);
                }

                @Override
                public void onFailure() {
                    tv_getCode.setClickable(true);
                }
            });

        }else{
            tv_getCode.setClickable(true);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
