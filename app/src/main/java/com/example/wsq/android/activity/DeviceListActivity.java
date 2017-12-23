package com.example.wsq.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.view.LoddingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/23.
 */

public class DeviceListActivity extends Activity{

    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    private OrderTaskService orderTaskService;

    private ProductAdapter mAdapter;
    private LoddingDialog dialog;
    private int curPage = 0;
    private List<Map<String, Object>> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_device_search);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        orderTaskService = new OrderTaskServiceImpl();
        mData = new ArrayList<>();
        tv_title.setText("设备列表");

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);

        dialog = new LoddingDialog(this);
        dialog.show();
        onStartSearch();
    }

    @OnClick({R.id.iv_back, R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
            finish();
            break;
            case R.id.tv_refresh:
                onStartSearch();
                break;
        }
    }

    public void onStartSearch(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, (curPage+1)+"");
        param.put(ResponseKey.KEYWORDS, getIntent().getStringExtra(ResponseKey.KEYWORDS));
        param.put(ResponseKey.CAT, "1");

        try {
            orderTaskService.onSearchDeviceList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);


                    if (list.size()!=0){
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mData.size() ==0){
                        ll_nodata.setVisibility(View.VISIBLE);
                        rv_RecyclerView.setVisibility(View.GONE);
                    }else{
                        ll_nodata.setVisibility(View.GONE);
                        rv_RecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }
}
