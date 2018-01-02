package com.example.wsq.android.activity.cash;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
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

/**
 * Created by wsq on 2017/12/26.
 */

public class ValidateCodeActivity extends Activity{

    @BindView(R.id.tv_getCode)TextView tv_getCode;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_validateCode)
    EditText et_validateCode;

    @BindView(R.id.tv_tel) TextView tv_tel;


    private int curLen = 60;
    private UserService userService;
    SharedPreferences shared;
    private LoddingDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_sms_validate);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        tv_tel.setText(UserFragment.mUserData.get(ResponseKey.TEL).toString());
        tv_title.setText("获取验证码");
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        dialog = new LoddingDialog(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_getCode,  R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                moveTaskToBack(false);
                break;
            case R.id.tv_getCode:
                getValidateCode();
                break;
            case R.id.tv_submit:

                String code = et_validateCode.getText().toString();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(ValidateCodeActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                addBankCard();
                break;
        }
    }

    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 0){
                curLen = 60;
                tv_getCode.setText("获取验证码");
//                tv_getCode.setBackgroundColor(getResources().getColor(R.color.defalut_title_color));
                tv_getCode.setTextColor(getResources().getColor(R.color.blue));
                tv_getCode.setClickable(true);
            }else{
                tv_getCode.setText("请耐心等待 "+curLen+"s");
                tv_getCode.setClickable(false);
//                tv_getCode.setBackgroundColor(Color.parseColor("#A8A8A8"));
                tv_getCode.setTextColor(getResources().getColor(R.color.color_gray));
                handler.postDelayed(this, 1000);
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void addBankCard(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        param.put(ResponseKey.TEL, UserFragment.mUserData.get(ResponseKey.TEL).toString());
        param.put(ResponseKey.CODE, et_validateCode.getText().toString());
        param.put(ResponseKey.BANK_NAME, UserFragment.mUserData.get(ResponseKey.NAME).toString());
        param.put(ResponseKey.BANK_CARD, getIntent().getStringExtra(ResponseKey.BANK_CARD));

        userService.onAddBank(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(ValidateCodeActivity.this,
                        result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                IntentFormat.startActivity(ValidateCodeActivity.this, BankActivity.class);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure() {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });


    }

    /**
     * 获取验证码
     */
    public void getValidateCode(){
        Map<String, String> param = new HashMap<>();

        param.put(ResponseKey.ID, shared.getString(Constant.SHARED.ID, ""));
        userService.onAddBankGetValidateCode(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(ValidateCodeActivity.this,
                        result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable, 1000);
            }

            @Override
            public void onFailure() {

            }
        });


    }
}
