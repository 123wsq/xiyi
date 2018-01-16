package com.example.wsq.android.activity.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.OrderMessageAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/20.
 */

public class OrderMessageInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_order_model) TextView tv_order_model;
    @BindView(R.id.tv_order_no) TextView tv_order_no;
    @BindView(R.id.tv_order_status) TextView tv_order_status;

    private UserService userService;
    private SharedPreferences shared;
    private String order_no = "";
    private List<Map<String, Object>> mData;
    private OrderMessageAdapter mAdapter;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_order_message_info;
    }

    public void init(){
        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_title.setText("订单详情");
        tv_order_model.setText(getIntent().getStringExtra(ResponseKey.XINGHAO));

        order_no = getIntent().getStringExtra(ResponseKey.ORDER_NO);
        tv_order_no.setText(order_no);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);



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

        userService.onOrderMessageInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.XIAOXI);

                tv_order_status.setText(result.get(ResponseKey.STATUS).toString());
                mData.addAll(list);
                if (mData.size() != 0){
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure() {

            }
        });

    }
}
