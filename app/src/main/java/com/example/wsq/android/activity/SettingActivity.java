package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/13.
 */

public class SettingActivity extends Activity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_setting_Withdraw_psd)
    TextView tv_setting_Withdraw_psd;
    @BindView(R.id.tv_update_Withdraw_psd) TextView tv_update_Withdraw_psd;
    @BindView(R.id.tv_tv_forget_Withdraw_psd) TextView tv_tv_forget_Withdraw_psd;
    @BindView(R.id.ll_Withdraw)
    LinearLayout ll_Withdraw;
    private String payPassword;
    private String bankCard = "";

    SharedPreferences shared;

    private CustomDefaultDialog defaultDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_setting);
        ButterKnife.bind(this);

        initView();

    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    public void initView() {

        tv_title.setText("我的设置");

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        payPassword = UserFragment.mUserData.get(ResponseKey.PAY_PASSWORD)+"";
        bankCard = UserFragment.mUserData.get(ResponseKey.BANK_CARD)+"";
        if (shared.getString(Constant.SHARED.JUESE, "").equals("1")){
            if (TextUtils.isEmpty(payPassword)){
                tv_setting_Withdraw_psd.setVisibility(View.VISIBLE);
            }else {
                tv_setting_Withdraw_psd.setVisibility(View.GONE);
                tv_update_Withdraw_psd.setVisibility(View.VISIBLE);
                tv_tv_forget_Withdraw_psd.setVisibility(View.VISIBLE);
            }
        }else{
            ll_Withdraw.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.iv_back, R.id.tv_setting_Withdraw_psd,
            R.id.tv_update_Withdraw_psd, R.id.tv_tv_forget_Withdraw_psd})
    public void onClick(View v) {
        Map<String, Object> param = new HashMap<>();
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_setting_Withdraw_psd:  //设置提现密码
                param.put("type", 1);
                IntentFormat.startActivity(SettingActivity.this, WithdrawPasswordActivity.class, param);
                break;
            case R.id.tv_update_Withdraw_psd:  //修改提现密码
                param.put("type", 7);
                IntentFormat.startActivity(SettingActivity.this, WithdrawPasswordActivity.class, param);
                break;
            case R.id.tv_tv_forget_Withdraw_psd:  //忘记提现密码

                CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(SettingActivity.this);
                builder.setMessage("请先去【银行卡】 管理中心绑定一张新的银行卡");
                builder.setOkBtn("好的", new OnDialogClickListener() {
                    @Override
                    public void onClick(CustomDefaultDialog dialog, String result) {
                        dialog.dismiss();
                    }
                });
                defaultDialog = builder.create();
                if (TextUtils.isEmpty(bankCard)){
                    defaultDialog.show();
                }else {
                    param.put("type", 8);
                    IntentFormat.startActivity(SettingActivity.this,
                            WithdrawPasswordActivity.class, param);

                }

                break;
        }
    }
}
