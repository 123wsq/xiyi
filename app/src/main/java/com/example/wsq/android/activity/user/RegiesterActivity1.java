package com.example.wsq.android.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.tools.RegisterParam;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.utils.ValidateParam;
import com.example.wsq.android.view.CustomPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/11.
 */

public class RegiesterActivity1 extends BaseActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password1)EditText et_password1;
    @BindView(R.id.et_password2)EditText et_password2;
    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_email)EditText et_email;
    @BindView(R.id.tv_sex) TextView tv_sex;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;

    CustomPopup popup;



    @Override
    public int getByLayoutId() {
        return R.layout.layout_register;
    }


    public void init() {

        RegisterParam.SEX = 1;
        tv_title.setText("会员注册");

    }

    @OnClick({R.id.btn_next, R.id.iv_back, R.id.tv_sex})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_next:
                if (validateParam()){
                    startActivity(new Intent(RegiesterActivity1.this, RegiesterActivity2.class));
                }

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sex:
                View view = LayoutInflater.from(RegiesterActivity1.this).inflate(R.layout.layout_default_popup,null);
                List<String> list = new ArrayList<>();

                list.add("男");
                list.add("女");

                popup = new CustomPopup(RegiesterActivity1.this, view, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {

                        RegisterParam.SEX = position+1;
                        tv_sex.setText(result);
                        popup.dismiss();
                    }
                });
                popup.setTitle("选择性别");
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    /**
     * 输入数据验证
     * @return
     */
    public boolean validateParam(){

        //用户名验证
        String userName = et_username.getText().toString();

        //输入的长度是否正确 6-18
        if(userName.length()>= 6 && userName.length() <= 18){
            RegisterParam.USERNAME = userName;
        }else {
            Toast.makeText(RegiesterActivity1.this, "请输入6-18位的用户名", Toast.LENGTH_SHORT).show();
            return false;
        }

        //输入的字母是否正确
        if(!userName.matches("[A-Za-z0-9_]+")){
            Toast.makeText(RegiesterActivity1.this, "用户名只能为数字，字母，下划线", Toast.LENGTH_SHORT).show();
            return false;
        }

        //验证密码是否一致
        String password1 = et_password1.getText().toString();
        String password2 = et_password2.getText().toString();
        //检验密码是否为空
        if (ValidateParam.validateParamIsNull(password1, password2)){
            Toast.makeText(RegiesterActivity1.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //检验密码是否两次输入一致
        if (!password1.equals(password2)){
            Toast.makeText(RegiesterActivity1.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        //检验密码长度
        if(password1.length()<=16 && password1.length() >= 6){
            RegisterParam.PASSWORD = password1;
        }else{
            Toast.makeText(RegiesterActivity1.this, "请输入6-16位的密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        String name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(RegiesterActivity1.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        RegisterParam.NAME = name;

        if (!TextUtils.isEmpty(et_email.getText().toString())) {
            if (ValidateDataFormat.isEmail(et_email.getText().toString())) {
                String email = et_email.getText().toString();
                RegisterParam.EMAIL = email;
            } else {
                Toast.makeText(RegiesterActivity1.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
