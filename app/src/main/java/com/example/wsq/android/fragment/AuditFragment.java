package com.example.wsq.android.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.OrderAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
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
 *企业工程师和企业管理工程师
 * 完成订单
 * Created by wsq on 2017/12/14.
 */

public class AuditFragment extends Fragment {

    @BindView(R.id.rv_view) RecyclerView rv_view;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    @BindView(R.id.tv_finish) TextView tv_finish;
    @BindView(R.id.tv_unfinish) TextView tv_unfinish;
    @BindView(R.id.tv_refresh) TextView tv_refresh;
    @BindView(R.id.store_house_ptr_frame) SmartRefreshLayout store_house_ptr_frame;
    private LoddingDialog dialog;

    private SharedPreferences shared;
    private OrderTaskService orderTaskService;
    private List<Map<String, Object>> mList;
    private OrderAdapter mAdapter;
    private int curPage = 1;
    private int total = 1;
    private int unitPage = 15;
    private String  state = "1";

    public static AuditFragment getInstance(){
        return new AuditFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_order_finish, container, false);
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

        mList = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();
        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

    }

    public void initView() {


        dialog = new LoddingDialog(getActivity());

        tv_finish.setBackgroundColor(getResources().getColor(R.color.defalut_title_color));
        tv_finish.setTextColor(getResources().getColor(R.color.color_white));
        tv_unfinish.setBackgroundColor(getResources().getColor(R.color.color_white));
        tv_unfinish.setTextColor(getResources().getColor(R.color.defalut_title_color));

        rv_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_view.setHasFixedSize(true);

        mAdapter = new OrderAdapter(getActivity(), mList);

        rv_view.setAdapter(mAdapter);

        setRefresh();
        getOrderTask(null, 0);
    }


    public void setRefresh(){

        store_house_ptr_frame.setRefreshHeader(new ClassicsHeader(getActivity())
                .setProgressResource(R.drawable.refresh_loadding).setDrawableProgressSize(40));
        store_house_ptr_frame.setRefreshFooter(new ClassicsFooter(getActivity())
        );
        store_house_ptr_frame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mList.clear();
                curPage = 1;
                refreshlayout.resetNoMoreData();
                getOrderTask(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getOrderTask(refreshlayout, 2);
                }

            }
        });
    }

    public void getOrderTask(final RefreshLayout refreshLayout, final int type){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.PAGE, curPage+"");

        param.put(ResponseKey.STATUS, state);
        param.put(ResponseKey.JUESE, shared.getString(Constant.SHARED.JUESE,""));

        orderTaskService.onGetOrderList(getActivity(), param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                unitPage = (int) result.get(ResponseKey.PER_PAGE);
                total = (int) result.get(ResponseKey.TOTAL);

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


    @OnClick({R.id.tv_finish, R.id.tv_unfinish, R.id.tv_refresh})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_finish:
                tv_finish.setBackgroundColor(getResources().getColor(R.color.defalut_title_color));
                tv_finish.setTextColor(getResources().getColor(R.color.color_white));
                tv_unfinish.setBackgroundColor(getResources().getColor(R.color.color_white));
                tv_unfinish.setTextColor(getResources().getColor(R.color.defalut_title_color));
                mList.clear();
                curPage = 1;
                state = "1";
                getOrderTask(null, 0);
                break;
            case R.id.tv_unfinish:
                tv_finish.setBackgroundColor(getResources().getColor(R.color.color_white));
                tv_finish.setTextColor(getResources().getColor(R.color.defalut_title_color));
                tv_unfinish.setBackgroundColor(getResources().getColor(R.color.defalut_title_color));
                tv_unfinish.setTextColor(getResources().getColor(R.color.color_white));

                mList.clear();
                curPage = 1;
                state = "1.1";
                getOrderTask(null, 0);
                break;
            case R.id.tv_refresh:
                curPage = 1;
                getOrderTask(null, 0);
                break;

        }
    }
}
