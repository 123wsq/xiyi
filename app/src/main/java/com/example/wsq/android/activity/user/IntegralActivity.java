package com.example.wsq.android.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.IntegralRecordAdapter;
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
 * 积分页面
 * Created by wsq on 2017/12/29.
 */

public class IntegralActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.wv_WebView) WebView wv_WebView;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_integral_title) TextView tv_integral_title;
    @BindView(R.id.tv_integral) TextView tv_integral;

    private UserService userService;
    private IntegralRecordAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private SharedPreferences shared;

    @Override
    public int getByLayoutId() {

        return R.layout.layout_integral;
    }

    @Override
    public void init() {

        tv_title.setText("积分");
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        wv_WebView.loadUrl("file:///android_asset/html/integral.html");

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.color_line)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mData = new ArrayList<>();
        mAdapter = new IntegralRecordAdapter(this, mData);

        rv_RecyclerView.setAdapter(mAdapter);
        userService = new UserServiceImpl();


        onGetIntegralRecord();
        onShowView(2);
    }

    @OnClick({R.id.iv_back, R.id.tv_integral_rule, R.id.tv_integral_record})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_integral_rule: //积分规则
                onShowView(1);
                break;
            case R.id.tv_integral_record:  //积分记录
                onShowView(2);
                break;
        }
    }

    /**
     *
     * @param type  1 规则   2 记录
     */
    public void onShowView( int type){

        tv_integral_title.setText(type == 1 ? "积分规则" : "积分记录");
        wv_WebView.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        rv_RecyclerView.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
    }

    /**
     * 获取积分记录
     */
    public void onGetIntegralRecord(){

        Map<String, String> param = new HashMap<>();

        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        userService.onIntegralRecord(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                mData.clear();
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.POINTS_LIST);
                mData.addAll(list);

                tv_integral.setText(result.get(ResponseKey.MEMBER_POINTS)+"分");

                wv_WebView.loadData(result.get(ResponseKey.GUIZE)+"", "text/html; charset=UTF-8", null);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
            }
        });
    }
}
