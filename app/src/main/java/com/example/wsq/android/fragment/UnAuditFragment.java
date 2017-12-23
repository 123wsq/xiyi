package com.example.wsq.android.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.OrderAdapter;
import com.example.wsq.android.base.BaseFragment;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业工程师和企业管理工程师
 * 未完成订单
 * Created by wsq on 2017/12/14.
 */

public class UnAuditFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView rv_view;
    private LinearLayout ll_nodata;
    private TextView tv_refresh;

    private SharedPreferences shared;
    private OrderTaskService orderTaskService;
    private List<Map<String, Object>> mList;
    private OrderAdapter mAdapter;
    private int curPage = 0;


    public static UnAuditFragment getInstance(){
        return new UnAuditFragment();
    }

    @Override
    public int getLayoutById() {
        return R.layout.layout_order_un;
    }

    @Override
    public void init() {

        mList = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();
        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

    }

    @Override
    public void initView() {
        rv_view = getActivity().findViewById(R.id.rv_view);
        ll_nodata = getActivity().findViewById(R.id.ll_nodata);
        tv_refresh = getActivity().findViewById(R.id.tv_refresh);

        tv_refresh.setOnClickListener(this);

        rv_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_view.setHasFixedSize(true);

        mAdapter = new OrderAdapter(getActivity(), mList);

        rv_view.setAdapter(mAdapter);

        getOrderTask();
    }


    public void getOrderTask(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.PAGE, (curPage+1)+"");

        param.put(ResponseKey.STATUS, "0");
        param.put(ResponseKey.JUESE, shared.getString(Constant.SHARED.JUESE,""));
        try {
            orderTaskService.onGetOrderList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result)  {

                    List< Map<String, Object>> list =  (List< Map<String, Object>>) result.get(ResponseKey.DATA);


                    if (list.size() != 0){

                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mList.size()==0){
                        rv_view.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
                    }else{
                        rv_view.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.GONE);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_refresh:
                getOrderTask();
                break;
        }
    }
}
