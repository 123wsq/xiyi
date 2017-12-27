package com.example.wsq.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.utils.ValidateParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegisterActivity3 extends BaseActivity implements View.OnClickListener {

    private TextView tv_protocols, tv_birth, tv_register, tv_getCode;
    private LinearLayout iv_back;

    private EditText et_sfz, et_tel, et_validateCode;
    private CheckBox cb_checkBox;

    private UserService userService;
    private int curLen = 60;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_register3;
    }

    @Override
    public void init() {

        userService = new UserServiceImpl();
    }

    @Override
    public void initView() {

        tv_protocols = this.findViewById(R.id.tv_protocols);
        iv_back = this.findViewById(R.id.iv_back);
        tv_getCode = this.findViewById(R.id.tv_getCode);

        et_sfz = this.findViewById(R.id.et_sfz);
        et_tel = this.findViewById(R.id.et_tel);
        tv_birth = this.findViewById(R.id.tv_birth);
        et_validateCode = this.findViewById(R.id.et_validateCode);
        cb_checkBox = this.findViewById(R.id.cb_checkBox);
        tv_register = this.findViewById(R.id.tv_register);

        tv_protocols.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
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
                if (str.length() == 14){
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
    @Override
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
                        try {
                            userService.register(map, new HttpResponseCallBack() {
                                @Override
                                public void callBack(Map<String, Object> result) {
                                    IntentFormat.startActivity(RegisterActivity3.this, LoginActivity.class);
                                }

                                @Override
                                public void onCallFail(String msg) {

                                }


                            });
                        } catch (Exception e) {
                            Log.e("本地异常", e.getMessage());
                            e.printStackTrace();
                        }
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
            RegisterParam.SFZ = sfz;
            RegisterParam.BIRTH = tv_birth.getText().toString();

            //验证验证码
            String validateCode = et_validateCode.getText().toString();
            if(ValidateParam.validateParamIsNull(validateCode)){
                Toast.makeText(RegisterActivity3.this, "验证码能不能为空", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                RegisterParam.YZM = validateCode;
            }

            if(validateCode.length()!= Constant.CODE_LENGTH){
                Toast.makeText(RegisterActivity3.this, "验证码必须为"+Constant.CODE_LENGTH, Toast.LENGTH_SHORT).show();
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
