package com.example.wsq.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;

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

public class KnowledgeActivity extends Activity{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_edit) TextView tv_edit;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private ProductAdapter mAdapter;
    private OrderTaskService orderTaskService;
    private List<Map<String, Object>> mData;
    private  int curPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_collect);

        ButterKnife.bind(this);

        init();
    }
    public void init(){

        tv_title.setText("圈内知识");
        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();

        tv_edit.setVisibility(View.GONE);
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);
        getKnowdgeList();

    }

    public void getKnowdgeList(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, (curPage+1)+"");
        param.put(ResponseKey.KEYWORDS, "");

        try {
            orderTaskService.onGetKnowledgeList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);

                    mData.addAll(list);

                    if (mData.size()==0){
                        rv_RecyclerView.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
                    }else {
                        rv_RecyclerView.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(KnowledgeActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_refresh})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_refresh:

                getKnowdgeList();
                break;

        }

    }
}
