package com.example.wsq.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.view.LoddingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/19.
 */

public class NewsActivity extends Activity{

    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @BindView(R.id.tv_refresh) TextView tv_refresh;

    private ProductAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private OrderTaskService orderTaskService;
    private int curPage = 0;
    private LoddingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_news);
        ButterKnife.bind(this);
        init();
    }

    public void init (){

        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl() ;

        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);
        dialog = new LoddingDialog(this);
        tv_title.setText("新闻列表");
        mAdapter = new ProductAdapter(this, mData);

        rv_RecyclerView.setAdapter(mAdapter);
        getNewsList();
    }

    public void getNewsList(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, (curPage+1)+"");
        param.put(ResponseKey.KEYWORDS, "");

        orderTaskService.onGetNewsList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);
                if (list.size() != 0){
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();

                }
                if (mData.size()!=0){
                    ll_nodata.setVisibility(View.GONE);
                    rv_RecyclerView.setVisibility(View.VISIBLE);
                }else {
                    ll_nodata.setVisibility(View.VISIBLE);
                    rv_RecyclerView.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.tv_refresh})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_refresh:
                getNewsList();
                break;
        }
    }
}
