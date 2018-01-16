package com.example.wsq.android.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.view.LoddingDialog;
import com.orhanobut.logger.Logger;
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
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/23.
 */

public class FaultActivity extends BaseActivity {

    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    @BindView(R.id.store_house_ptr_frame) SmartRefreshLayout store_house_ptr_frame;

    OrderTaskService orderTaskService;
    private ProductAdapter mAdapter;
    private LoddingDialog dialog;
    private int curPage = 1;
    private List<Map<String, Object>> mData;
    private int total =1;
    private int unitPage = 15;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_device_search;
    }

    public void init(){

        orderTaskService = new OrderTaskServiceImpl();
        mData = new ArrayList<>();
        tv_title.setText("资料列表");

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(this, mData, Constant.INFO_3);
        rv_RecyclerView.setAdapter(mAdapter);

        rv_RecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                Logger.d("点击资料");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        dialog = new LoddingDialog(this);

        setRefresh();
        onStartSearch(null, 0);

    }

    public void setRefresh(){

        store_house_ptr_frame.setRefreshHeader(new ClassicsHeader(this)
                .setProgressResource(R.drawable.refresh_loadding).setDrawableProgressSize(40));
        store_house_ptr_frame.setRefreshFooter(new ClassicsFooter(this)
                );
        store_house_ptr_frame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mData.clear();
                curPage = 1;
                refreshlayout.resetNoMoreData();
                onStartSearch(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){

                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    onStartSearch(refreshlayout, 2);
                }

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
                curPage = 1;
                onStartSearch(null, 0);
                break;
        }
    }

    /**
     * 加载数据
     * @param refreshLayout  用于修改加载状态
     * @param type   判断是加载还是刷新 0表示初始加载   1 表示刷新  2 表示 加载更多
     */
    public void onStartSearch(final RefreshLayout refreshLayout, final int type){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, curPage+"");
        param.put(ResponseKey.KEYWORDS, getIntent().getStringExtra(ResponseKey.KEYWORDS));
        param.put(ResponseKey.CAT, "2");

        orderTaskService.onSearchDeviceList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                total = (int) result.get(ResponseKey.TOTAL);

                unitPage = (int) result.get(ResponseKey.PER_PAGE);
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
//        try {
//            orderTaskService.onSearchDeviceList(param, new HttpResponseCallBack() {
//                @Override
//                public void callBack(Map<String, Object> result) {
//
//
//                }
//
//                @Override
//                public void onCallFail(String msg) {
//                    if (type == 1){
//                        refreshLayout.finishRefresh();
//                    }else if(type ==2 ){
//                        refreshLayout.finishLoadmore();
//                    }
//                    dialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            dialog.dismiss();
//            e.printStackTrace();
//
//        }

    }
}
