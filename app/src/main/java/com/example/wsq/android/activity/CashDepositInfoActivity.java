package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.BankInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class CashDepositInfoActivity extends Activity{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_money) TextView tv_money;
    @BindView(R.id.tv_cash_state) TextView tv_cash_state;
    @BindView(R.id.tv_apply_time) TextView tv_apply_time;
    @BindView(R.id.tv_apply_time_i) TextView tv_apply_time_i;
    @BindView(R.id.tv_apply_name) TextView tv_apply_name;
    @BindView(R.id.tv_content) TextView tv_content;
    @BindView(R.id.ll_apply_content) LinearLayout ll_apply_content;
    @BindView(R.id.ll_Details_info) LinearLayout ll_Details_info;
    @BindView(R.id.tv_apply_num) TextView tv_apply_num;
    @BindView(R.id.tv_create_time) TextView tv_create_time;
    @BindView(R.id.tv_apply_success) TextView tv_apply_success;
    @BindView(R.id.tv_progress) TextView tv_progress;

    SharedPreferences shared;
    private UserService userService;
    private int payId = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cash_deposit_info);
        ButterKnife.bind(this);
        init();
    }

    public void init(){


        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);


        payId = getIntent().getIntExtra(ResponseKey.PAY_ID, 0);
        if (payId == 0) {  //表示退保金页面
            tv_title.setText("退保证金");
            getCashDepositInfo();
        }else{
            tv_title.setText("账单详情");
            onGetBillDetailsInfo();
        }
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取保证金详情
     */
    public void getCashDepositInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.BAIL_ID, getIntent().getIntExtra(ResponseKey.BAIL_ID, 0)+"");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        userService.onApplyCashDepositInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.CASH_LIST);

                setViewData(data, 1);

            }

            @Override
            public void onFailure() {

            }
        });

    }


    /**
     * 获取账单详情
     */
    public void onGetBillDetailsInfo(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.PAY_ID, payId+"");

        userService.onApplyCashDetailInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.CASH_LIST);

                setViewData(data, 2);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 设置页面显示数据
     * @param data
     * @param type  1 表示申请保证金   2 表示提现
     */
    public void setViewData(Map<String, Object> data, int type){

        tv_money.setText(data.get(ResponseKey.MONEY)+"");
        int state = (int)data.get(ResponseKey.STATE);
        if (state ==0){
            tv_cash_state.setText("待审核");
        }else if(state ==1){
            tv_cash_state.setText("审核成功");
        }else{
            tv_cash_state.setText("已驳回，请联系客服");
        }

        tv_apply_time.setText(data.get(ResponseKey.CREAT_AT)+"");
        tv_apply_time_i.setText(data.get(ResponseKey.CREAT_AT)+"");
        String bankCode = data.get(ResponseKey.BANK_NUMBER)+"";
        String[] bankType = BankInfo.getNameOfBank(bankCode.toCharArray(), 0).split("_");
        tv_apply_name.setText(bankType[0]+" ("+bankCode.substring(bankCode.length()-4)+") "+
                data.get(ResponseKey.BANK_NAME_C)+"");

        if (type == 1 ){
            ll_apply_content.setVisibility(View.VISIBLE);
            tv_content.setText(data.get(ResponseKey.MESSAGE)+"");
            tv_apply_success.setText("申请成功");
            tv_progress.setText("业务处理中");
        }else if(type == 2){
            ll_Details_info.setVisibility(View.VISIBLE);
            tv_create_time.setText(data.get(ResponseKey.CREAT_AT)+"");
            tv_apply_num.setText(data.get(ResponseKey.PAY_SN)+"");
            tv_apply_success.setText("提现成功");
            tv_progress.setText("银行处理中");
        }
    }
}
