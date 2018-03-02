package com.example.wsq.android.activity.cash;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.CashProgressAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.BankInfo;
import com.example.wsq.android.utils.DateUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class CashDepositInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_money) TextView tv_money;
    @BindView(R.id.tv_cash_state) TextView tv_cash_state;
    @BindView(R.id.tv_apply_name) TextView tv_apply_name;
    @BindView(R.id.tv_content) TextView tv_content;
    @BindView(R.id.tv_create_time) TextView tv_create_time;
    @BindView(R.id.ll_apply_content) LinearLayout ll_apply_content;
    @BindView(R.id.ll_Details_info) LinearLayout ll_Details_info;
    @BindView(R.id.tv_apply_num) TextView tv_apply_num;
    @BindView(R.id.tv_reject) TextView tv_reject;
    @BindView(R.id.ll_reject_content) LinearLayout ll_reject_content;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;

    SharedPreferences shared;
    private UserService userService;
    private int payId = 0;
    private CashProgressAdapter mAdapter;
    private List<Map<String, Object>> mData;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_cash_deposit_info;
    }

    public void init(){


        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);



        payId = getIntent().getIntExtra(ResponseKey.PAY_ID, 0);
        if (payId == 0) {  //表示退保金页面
            tv_title.setText("退保证金");
            getCashDepositInfo();
        }else{
            tv_title.setText("账单详情");
            onGetBillDetailsInfo();
        }

        mAdapter = new CashProgressAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);
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
        mData.clear();


        tv_money.setText(data.get(ResponseKey.MONEY)+"");

        String bankCode = data.get(ResponseKey.BANK_NUMBER)+"";

        try {
            String str = BankInfo.getNameOfBank(this, Long.parseLong(bankCode.substring(0, 6)));
            String[] bankType = str.split("-");
            tv_apply_name.setText(bankType[0] + " (" + bankCode.substring(bankCode.length() - 4) + ") " +
                    data.get(ResponseKey.BANK_NAME_C) + "");
        }catch (Exception e){
            e.printStackTrace();
        }

        int state = (int)data.get(ResponseKey.STATE);
        if (state ==0){
            tv_cash_state.setText("处理中");
        }else if(state ==1){
            tv_cash_state.setText("审核成功");
        }else if(state == 2){
            tv_cash_state.setText("已驳回，请联系客服");
            ll_reject_content.setVisibility(View.VISIBLE);
            if (type ==1){
                Logger.d(data.get(ResponseKey.AUDIT_MESSAGE)+"    =======1");
                tv_reject.setText(data.get(ResponseKey.AUDIT_MESSAGE)+"");
            }else if(type ==2){
                //refuse_txt
                tv_reject.setText(data.get(ResponseKey.REFUSE_TXT)+"");
            }

        }else if(state ==3 ){
            tv_cash_state.setText("已到账");

        }


        tv_create_time.setText(data.get(ResponseKey.CREAT_AT)+"");
        if (type == 1 ){
            ll_apply_content.setVisibility(View.VISIBLE);
            tv_content.setText(data.get(ResponseKey.MESSAGE)+"");

            onSetData(true, true, "申请成功",data.get(ResponseKey.CREAT_AT)+"", true);
            onSetData(true, state==2 ? true: false, "业务处理中",data.get(ResponseKey.CREAT_AT)+"", true);
        }else if(type == 2){
            ll_Details_info.setVisibility(View.VISIBLE);
//            tv_create_time.setText(data.get(ResponseKey.CREAT_AT)+"");
            tv_apply_num.setText(data.get(ResponseKey.PAY_SN)+"");
            onSetData(true, true, "提现成功",data.get(ResponseKey.CREAT_AT)+"", true);
            onSetData(true, state==2 ? true: false, "银行处理中",data.get(ResponseKey.CREAT_AT)+"", true);
        }
        if (state == 1 || state ==0){
            onSetData(false, false, "审核通过", "预计1-7个工作日内到账", false);
            onSetData(false, false, "已到账", "", false);
        }else if(state == 2){
            onSetData(true, false, "审核未通过", "预计1-7个工作日内到账", true);
            onSetData(false, false, "已到账", "", false);
        }else if(state == 3){
            onSetData(true, true, "审核通过", "预计1-7个工作日内到账", true);
            onSetData(true, false, "已到账", "", true);
        }

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置状态显示
     * @param upState
     * @param downState
     * @param msg
     * @param time
     */
    public void onSetData(boolean upState, boolean downState, String msg, String time, boolean state){
        Map<String, Object> map = new HashMap<>();
        map.put("up", upState);
        map.put("down", downState);
        map.put("msg", msg);
        map.put("time", time);
        map.put("state", state);

        mData.add(map);
    }
}
