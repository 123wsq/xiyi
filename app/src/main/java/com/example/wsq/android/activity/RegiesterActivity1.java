package com.example.wsq.android.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * Created by wsq on 2017/12/11.
 */

public class RegiesterActivity1 extends BaseActivity implements View.OnClickListener {

    private Button btn_next;
    private ImageView iv_back;

    private EditText et_username, et_password1, et_password2, et_name, et_email;
    private TextView tv_sex;
    private CustomPopup popup;
    private LinearLayout ll_layout;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_register;
    }

    @Override
    public void init() {

        RegisterParam.SEX = 1;

    }

    @Override
    public void initView() {

        btn_next = this.findViewById(R.id.btn_next);
        iv_back =  this.findViewById(R.id.iv_back);
        et_username =  this.findViewById(R.id.et_username);
        et_password1 =  this.findViewById(R.id.et_password1);
        et_password2 =  this.findViewById(R.id.et_password2);
        et_name =  this.findViewById(R.id.et_name);
        et_email =  this.findViewById(R.id.et_email);
        tv_sex =  this.findViewById(R.id.tv_sex);
        ll_layout = this.findViewById(R.id.ll_layout);

        btn_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_sex.setOnClickListener(this);

    }

    @Override
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
        //输入的字母是否正确
        if(!userName.matches("[A-Za-z0-9_]+")){
            Toast.makeText(RegiesterActivity1.this, "用户名只能为数字，字母，下划线", Toast.LENGTH_SHORT).show();
            return false;
        }
        //输入的长度是否正确 6-18
        if(userName.length()>= 6 && userName.length() <= 18){
            RegisterParam.USERNAME = userName;
        }else {
            Toast.makeText(RegiesterActivity1.this, "用户名长度不能为"+userName.length(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegiesterActivity1.this, "密码长度不能为"+password1.length(), Toast.LENGTH_SHORT).show();
            return false;
        }

        String name = et_name.getText().toString();
        RegisterParam.NAME = name;

         if(ValidateDataFormat.isEmail(et_email.getText().toString())){
             String email = et_email.getText().toString();
             RegisterParam.EMAIL = email;
        }else{
             Toast.makeText(RegiesterActivity1.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
             return false;
         }

        return true;
    }
}
