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
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.OrderMessageAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/20.
 */

public class OrderMessageInfoActivity extends Activity{

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_order_model) TextView tv_order_model;
    @BindView(R.id.tv_order_no) TextView tv_order_no;
    @BindView(R.id.tv_order_status) TextView tv_order_status;

    private UserService userService;
    private SharedPreferences shared, sharedMsg;
    private String order_no = "";
    private List<Map<String, Object>> mData;
    private OrderMessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_order_message_info);
        ButterKnife.bind(this);
        init();
    }

    public void init(){
        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        sharedMsg = getSharedPreferences(Constant.SHARED_MSG, Context.MODE_PRIVATE);
        tv_title.setText("订单详情");
        tv_order_model.setText(getIntent().getStringExtra(ResponseKey.XINGHAO));

        order_no = getIntent().getStringExtra(ResponseKey.ORDER_NO);
        tv_order_no.setText(order_no);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        sharedMsg.edit().putBoolean(getIntent().getIntExtra(ResponseKey.ID,0)+"", true).commit();

        mAdapter = new OrderMessageAdapter(this, mData);

        rv_RecyclerView.setAdapter(mAdapter);
        getMessageInfo();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void getMessageInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN,shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.ORDER_NO, order_no);

        try {
            userService.onOrderMessageInfo(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.XIAOXI);

                    tv_order_status.setText(result.get(ResponseKey.STATUS).toString());
                    mData.addAll(list);
                    if (mData.size() != 0){
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
