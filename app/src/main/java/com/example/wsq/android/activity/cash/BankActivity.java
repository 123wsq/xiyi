package com.example.wsq.android.activity.cash;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.BankInfo;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class BankActivity extends Activity{

    @BindView(R.id.ll_no_bank) LinearLayout ll_no_bank;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.iv_add) ImageView iv_add;
    @BindView(R.id.tv_cardCode) TextView tv_cardCode;
    @BindView(R.id.ll_bank_info) LinearLayout ll_bank_info;
    @BindView(R.id.tv_bank) TextView tv_bank;
    @BindView(R.id.tv_card_type) TextView tv_card_type;

    private String bankCode;
    private CustomDefaultDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_bank);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        tv_title.setText("银行卡管理");
        bankCode = UserFragment.mUserData.get(ResponseKey.BANK_CARD)+"";


        if (TextUtils.isEmpty(bankCode)){
            iv_add.setVisibility(View.VISIBLE);
            ll_no_bank.setVisibility(View.VISIBLE);
            ll_bank_info.setVisibility(View.GONE);

        }else{
            iv_add.setVisibility(View.GONE);
            ll_no_bank.setVisibility(View.GONE);
            ll_bank_info.setVisibility(View.VISIBLE);

            String str = "";

            for (int i = 0 ;i< bankCode.length(); i ++){

                if (i % 5 != 0 ){
                    if (i < bankCode.length()-5){
                        str += "*";
                    }else {
                        str += bankCode.substring(i, i + 1);
                    }
                }else{
                    str +=" ";
                }
            }
            tv_cardCode.setText(str);

            String bankType = BankInfo.getNameOfBank(this, Long.parseLong(bankCode.substring(0, 6)));
            String[] str1 = bankType.split("-");
            if (str1.length==1){
                tv_bank.setText(str1[0]);
            }else if(str1.length==3){
                tv_bank.setText(str1[0]);
                tv_card_type.setText(str1[2]);
            }


        }


        initDialog();
    }

    public void initDialog(){
        CustomDefaultDialog.Builder builder =  new CustomDefaultDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("更换银行卡，会覆盖掉之前添加的银行卡，您确定还要继续吗？");
        builder.setOkBtn("继续", new OnDialogClickListener() {
            @Override
            public void onClick(CustomDefaultDialog dialog, String result) {
                IntentFormat.startActivity(BankActivity.this, AddBankActivity.class);
                dialog.dismiss();
            }
        });
        builder.setCancelBtn("不了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog = builder.create();
    }

    @OnClick({R.id.tv_redresh, R.id.iv_back, R.id.iv_add, R.id.tv_reseat_bank})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_redresh:

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                IntentFormat.startActivity(this, AddBankActivity.class);
                break;
            case R.id.tv_reseat_bank:

                dialog.show();
                break;
        }
    }
}