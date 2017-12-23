package com.example.wsq.android.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.base.BaseFragment;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资料
 * Created by wsq on 2017/12/15.
 */

public class FaultFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_type_group;
    private RecyclerView rv_RecyclerView;

    private OrderTaskService orderTaskService;
    private int curPage = 0;
    private ProductAdapter mAdapter;
    private List<Map<String, Object>> mData;

    public static FaultFragment getInstance(){

        return new FaultFragment();
    }

    @Override
    public int getLayoutById() {
        return R.layout.layout_fault_list;
    }

    @Override
    public void init() {

        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();

    }

    @Override
    public void initView() {


        rg_type_group = getActivity().findViewById(R.id.rg_type_group);
        rv_RecyclerView = getActivity().findViewById(R.id.rv_RecyclerView);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        rg_type_group.setOnCheckedChangeListener(this);

        mAdapter = new ProductAdapter(getActivity(), mData);
        rv_RecyclerView.setAdapter(mAdapter);

        getData("all");
    }


    public void getData(String cat){

        Map<String , String> param = new HashMap<>();
        param.put(ResponseKey.CAT, cat);
        param.put(ResponseKey.PAGE, (curPage+1)+"");

        try {
            orderTaskService.onGetProductInformation(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {


                    List<Map<String, Object>> list = (List<Map<String, Object>>)result.get(ResponseKey.DATA);
                mData.addAll((List<Map<String, Object>>)result.get(ResponseKey.DATA));

                mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        curPage = 0;
        mData.clear();
        switch (checkedId){
            case R.id.rb_all:
                getData("all");
                break;
            case R.id.rb_data:
                getData("21");
                break;
            case R.id.rb_case:
                getData("27");
                break;
            case R.id.rb_other:
                getData("28");
                break;
        }
    }
}
