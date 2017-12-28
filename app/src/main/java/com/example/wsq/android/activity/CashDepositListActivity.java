package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.CashDepositAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.IntentFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class CashDepositListActivity extends Activity{

    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private UserService userService;
    private CashDepositAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private SharedPreferences shared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cash_deposit_list);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        tv_title.setText("退保证金");
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mData = new ArrayList<>();
         mAdapter = new CashDepositAdapter(this, mData, clickListener);
         rv_RecyclerView.setAdapter(mAdapter);



        onGetCashDepositList();
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
     * 获取保证金列表
     */
    public void onGetCashDepositList(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        userService.onApplyCashDepositList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.BAIL_LIST);

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
}
