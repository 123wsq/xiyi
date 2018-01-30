package com.example.wsq.android.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
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
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/20.
 */

public class KnowledgeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_Details) TextView tv_Details;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @BindView(R.id.store_house_ptr_frame)
    SmartRefreshLayout store_house_ptr_frame;
    private LoddingDialog dialog;
    private ProductAdapter mAdapter;
    private OrderTaskService orderTaskService;
    private List<Map<String, Object>> mData;
    private  int curPage = 1;
    private int total = 1;
    private int unitPage = 15;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_collect;
    }

    public void init(){

        dialog = new LoddingDialog(this);
        tv_title.setText("圈内知识");
        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();

        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(this, mData, Constant.INFO_4);
        rv_RecyclerView.setAdapter(mAdapter);
        getKnowdgeList(null, 0);

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
                getKnowdgeList(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){

                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getKnowdgeList(refreshlayout, 2);
                }

            }
        });
    }

    public void getKnowdgeList(final RefreshLayout refreshLayout, final int type){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, curPage+"");
        param.put(ResponseKey.KEYWORDS, "");

        orderTaskService.onGetKnowledgeList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                total = (int) result.get(ResponseKey.TOTAL);
                unitPage = (int) result.get(ResponseKey.PER_PAGE);
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);

                mData.addAll(list);

                if (mData.size()==0){
                    rv_RecyclerView.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);
                }else {
                    rv_RecyclerView.setVisibility(View.VISIBLE);
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
                if (type == 1){
                    refreshLayout.finishRefresh();
                }else if(type ==2 ){
                    refreshLayout.finishLoadmore();
                }
                dialog.dismiss();
            }
        });

    }


    @OnClick({R.id.iv_back, R.id.tv_refresh})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_refresh:

                curPage = 1;
                getKnowdgeList(null, 0);
                break;

        }

    }
}
