package com.example.wsq.android.activity.cash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.CashDepositAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退取保证金页面
 * Created by wsq on 2017/12/26.
 */

public class CashDepositListActivity extends BaseActivity {

    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    @BindView(R.id.tv_deposit_amount) TextView tv_deposit_amount;
    @BindView(R.id.tv_Details) TextView tv_Details;

    private UserService userService;
    private CashDepositAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private SharedPreferences shared;
    private CustomDefaultDialog defaultDialog;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_cash_deposit_list;
    }

    public void init(){

        userService = new UserServiceImpl();
        tv_title.setText("退保证金");
        tv_Details.setText("重新申请");
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 2),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mData = new ArrayList<>();
         mAdapter = new CashDepositAdapter(this, mData, clickListener);
         rv_RecyclerView.setAdapter(mAdapter);



        onGetCashDepositList();
    }

    @OnClick({R.id.iv_back, R.id.tv_Details})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_Details:
                onAlertDialog();
                break;
        }
    }


    /**
     * 获取保证金列表
     */
    public void onGetCashDepositList(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        userService.onApplyCashDepositList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.BAIL_LIST);

                tv_deposit_amount.setText(result.get(ResponseKey.BAOZHENGJIN)+"元");
                if (list.size() != 0){
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }

                if (mData.size() == 0 ){
                    ll_nodata.setVisibility(View.VISIBLE);
                    rv_RecyclerView.setVisibility(View.GONE);
                }else{
                    ll_nodata.setVisibility(View.GONE);
                    rv_RecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    OnDefaultClickListener clickListener = new OnDefaultClickListener() {
        @Override
        public void onClickListener(int position) {
            Map<String, Object> param = new HashMap<>();
            param.put(ResponseKey.BAIL_ID, mData.get(position).get(ResponseKey.BAIL_ID));
            IntentFormat.startActivity(CashDepositListActivity.this,CashDepositInfoActivity.class, param);
        }
    };


    public void onAlertDialog(){

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("一天只能重复申请一次，确定申请吗？");
        builder.setOkBtn("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IntentFormat.startActivity(CashDepositListActivity.this, CashDepositActivity.class);
                dialogInterface.dismiss();
                finish();

            }
        });
        builder.setCancelBtn("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();

    }
}
