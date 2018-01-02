package com.example.wsq.android.activity.user;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.cash.WithdrawPasswordActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.AppUtils;
import com.example.wsq.android.utils.CacheUtil;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.SwitchView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/13.
 */

public class SettingActivity extends Activity implements SwitchView.OnStateChangedListener {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_setting_Withdraw_psd)
    TextView tv_setting_Withdraw_psd;
    @BindView(R.id.tv_update_Withdraw_psd) TextView tv_update_Withdraw_psd;
    @BindView(R.id.tv_tv_forget_Withdraw_psd) TextView tv_tv_forget_Withdraw_psd;
    @BindView(R.id.ll_Withdraw)
    LinearLayout ll_Withdraw;
    @BindView(R.id.tv_cacheSize) TextView tv_cacheSize;
    @BindView(R.id.tv_version) TextView tv_version;
    @BindView(R.id.sv_switchBtn) SwitchView sv_switchBtn;


    private String payPassword;
    private String bankCard = "";

    SharedPreferences shared;

    private CustomDefaultDialog defaultDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_setting);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        initView();
        onRegister();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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


        try {
            tv_cacheSize.setText(CacheUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_version.setText(AppUtils.getLocalVersionName(this));
        sv_switchBtn.setOnStateChangedListener(this);

    }

    @OnClick({R.id.iv_back, R.id.tv_setting_Withdraw_psd,
            R.id.tv_update_Withdraw_psd, R.id.tv_tv_forget_Withdraw_psd, R.id.ll_cache_data})
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
            case R.id.ll_cache_data:

                CustomDefaultDialog.Builder builder1 = new CustomDefaultDialog.Builder(SettingActivity.this);
                builder1.setTitle("提示");
                builder1.setMessage("清理缓存后，将需要花费更多的时间加载页面，您确定要清除吗？");
                builder1.setOkBtn("确定", new OnDialogClickListener() {
                    @Override
                    public void onClick(CustomDefaultDialog dialog, String result) {

                        CacheUtil.clearAllCache(SettingActivity.this);
                        dialog.dismiss();
                        try {
                            tv_cacheSize.setText(CacheUtil.getTotalCacheSize(SettingActivity.this));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder1.setCancelBtn("点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder1.create().show();

                break;
        }
    }

    public void onRegister(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WithdrawPasswordActivity.ACTION);
        registerReceiver(receiver, filter);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tv_setting_Withdraw_psd.setVisibility(View.GONE);
            tv_update_Withdraw_psd.setVisibility(View.VISIBLE);
            tv_tv_forget_Withdraw_psd.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void toggleToOn(SwitchView view) {

        onInitDialog();
        view.setOpened(false);
    }

    @Override
    public void toggleToOff(SwitchView view) {

        onInitDialog();
        view.setOpened(false);
    }


    public void onInitDialog(){

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("暂时还不可用");
        builder.setOkBtn("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        CustomDefaultDialog dialog = builder.create();
        dialog.show();
    }
}