package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.MessageAdapter;
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

public class MessageActivity extends Activity{

    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private int curPage = 0;
    private UserService userService;
    private SharedPreferences shared;
    private MessageAdapter mAdapter;
    private List<Map<String, Object>> mData;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);
        ButterKnife.bind(this);

        init();
        getMessageList();
    }


    public void init(){

        mData = new ArrayList<>();

        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new MessageAdapter(this, mData);

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
     * 获取消息列表
     */
    public void getMessageList(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, (curPage+1)+"");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        try {
            userService.onMessageList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);

                    mData.addAll(list);

                    if (mData.size() != 0){
                        mAdapter.notifyDataSetChanged();
                        rv_RecyclerView.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.GONE);
                    }else{
                        rv_RecyclerView.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
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
