package com.example.wsq.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.view.LoddingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资料
 * Created by wsq on 2017/12/15.
 */

public class FaultFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_type_group) RadioGroup rg_type_group;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.store_house_ptr_frame) SmartRefreshLayout store_house_ptr_frame;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;

    private OrderTaskService orderTaskService;
    private int curPage = 1;
    private int total = 1;
    private int unitPage = 15;
    private String cat = "all";
    private ProductAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private LoddingDialog dialog;

    public static FaultFragment getInstance(){

        return new FaultFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.layout_fault_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }

    public void init() {

        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();


    }

    public void initView() {

        dialog = new LoddingDialog(getActivity());

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 10),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        rg_type_group.setOnCheckedChangeListener(this);

        mAdapter = new ProductAdapter(getActivity(), mData, Constant.INFO_3);
        rv_RecyclerView.setAdapter(mAdapter);

        setRefresh();
        getData(null , 0);
    }

    @OnClick({R.id.tv_refresh})
    public void onClick(){
        getData(null, 0);
    }


    public void setRefresh(){

        store_house_ptr_frame.setRefreshHeader(new ClassicsHeader(getActivity())
                .setProgressResource(R.drawable.refresh_loadding).setDrawableProgressSize(40));
        store_house_ptr_frame.setRefreshFooter(new ClassicsFooter(getActivity()));

//        store_house_ptr_frame.setL
        store_house_ptr_frame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mData.clear();
                curPage = 1;
                refreshlayout.resetNoMoreData();
                getData(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getData(refreshlayout, 2);
                }

            }
        });
    }

    /**
     * 获取资料列表
     * @param refreshLayout
     * @param type
     */
    public void getData(final RefreshLayout refreshLayout, final int type){

        dialog.show();
        Map<String , String> param = new HashMap<>();
        param.put(ResponseKey.CAT, cat);
        param.put(ResponseKey.PAGE, curPage+"");

        orderTaskService.onGetProductInformation(getActivity(), param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                total = (int) result.get(ResponseKey.TOTAL);
                unitPage = (int) result.get(ResponseKey.PER_PAGE);
                List<Map<String, Object>> list = (List<Map<String, Object>>)result.get(ResponseKey.DATA);

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
                if (type == 1){
                    refreshLayout.finishRefresh();
                }else if(type ==2 ){
                    refreshLayout.finishLoadmore();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        curPage = 1;
        mData.clear();
        switch (checkedId){
            case R.id.rb_all:
                cat = "all";
                getData(null, 0);
                break;
            case R.id.rb_data:
                cat = "21";
                getData(null, 0);
                break;
            case R.id.rb_case:
                cat = "27";
                getData(null, 0);
                break;
            case R.id.rb_other:
                cat = "28";
                getData(null, 0);
                break;
        }
    }
}
