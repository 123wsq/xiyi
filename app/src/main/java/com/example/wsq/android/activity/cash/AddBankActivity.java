package com.example.wsq.android.activity.cash;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.BankCardValidate;
import com.example.wsq.android.utils.BankInfo;
import com.example.wsq.android.utils.IntentFormat;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class AddBankActivity extends Activity implements TextWatcher {


    @BindView(R.id.et_backcode)
    EditText et_backcode;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_tel) TextView tv_tel;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_card_type) TextView tv_card_type;
    @BindView(R.id.tv_cordCode_error) TextView tv_cordCode_error;
    @BindView(R.id.tv_next) TextView tv_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_add_bank);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

       String name =  UserFragment.mUserData.get(ResponseKey.NAME).toString();
       String tel = UserFragment.mUserData.get(ResponseKey.TEL).toString();
       tv_name.setText("*** "+name.substring(name.length()-1));
       tv_tel.setText(tel.substring(0, 3)+"****"+tel.substring(tel.length()-4));
        tv_title.setText("添加银行卡");
        et_backcode.addTextChangedListener(this);
    }

    @OnClick({R.id.tv_next, R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_next:
                if (TextUtils.isEmpty(et_backcode.getText().toString())){
                    Toast.makeText(AddBankActivity.this, "请输入卡号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!BankCardValidate.validateCard(et_backcode.getText().toString())){
                    Toast.makeText(AddBankActivity.this, "请输入正确的银行卡号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检测是否是中国工商银行卡

                Map<String, Object> map = new HashMap<>();
                map.put(ResponseKey.BANK_CARD, et_backcode.getText().toString());
                IntentFormat.startActivity(AddBankActivity.this, ValidateCodeActivity.class, map);

                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        if (str.length()>=6){
            String name = BankInfo.getNameOfBank(this, Long.parseLong(str.substring(0, 6)));
            tv_card_type.setText(name);

            if (name.contains("工商银行")){
                tv_cordCode_error.setVisibility(View.GONE);
                tv_next.setClickable(true);
                tv_next.setBackgroundResource(R.drawable.shape_button);
            }else{
                tv_cordCode_error.setVisibility(View.VISIBLE);
                tv_next.setClickable(false);
                tv_next.setBackgroundResource(R.drawable.shape_disable_button);
            }

        }else if(str.length()>0 && str.length() < 6){
            tv_card_type.setText("");
            tv_cordCode_error.setVisibility(View.VISIBLE);
            tv_next.setClickable(false);
            tv_next.setBackgroundResource(R.drawable.shape_disable_button);
        }else{
            tv_cordCode_error.setVisibility(View.GONE);
            tv_next.setClickable(true);
            tv_next.setBackgroundResource(R.drawable.shape_button);
        }
    }
}
