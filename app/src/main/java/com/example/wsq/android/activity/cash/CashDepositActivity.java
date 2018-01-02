package com.example.wsq.android.activity.cash;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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
import com.example.wsq.android.tools.AppStatus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 保证金页面
 * Created by wsq on 2017/12/26.
 */

public class CashDepositActivity extends Activity implements TextWatcher {

    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.tv_num)
    TextView tv_num;


    private UserService userService;
    private SharedPreferences shared;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cash_deposit);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        et_description.addTextChangedListener(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @OnClick({R.id.iv_back, R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                    onSubmit();
                break;
        }
    }

    public void onSubmit(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.MESSAGE, et_description.getText().toString());

        userService.onApplyCashDeposit(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(CashDepositActivity.this,
                        result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure() {

            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Editable editable = et_description.getText();
        int num = et_description.getText().length();
        if (num > 50){

            tv_num.setText("50 / 50");
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0,50);
            et_description.setText(newStr);
            editable = et_description.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if(selEndIndex > newLen)
            {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }else{
            tv_num.setText(num+" / 50");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {



    }
}
