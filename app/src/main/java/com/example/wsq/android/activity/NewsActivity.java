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
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.tools.RecyclerViewDivider;
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
    @BindView(R.id.store_house_ptr_frame)
    SmartRefreshLayout store_house_ptr_frame;

    private ProductAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private OrderTaskService orderTaskService;
    private int curPage = 1;
    private int total = 1;
    private int unitPage = 15;
    private LoddingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layout_news);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init (){

        mData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl() ;

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);
        dialog = new LoddingDialog(this);
        tv_title.setText("新闻列表");
        mAdapter = new ProductAdapter(this, mData);

        rv_RecyclerView.setAdapter(mAdapter);
        setRefresh();

        getNewsList(null, 0);
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
                getNewsList(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){

                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getNewsList(refreshlayout, 2);
                }

            }
        });
    }
    public void getNewsList(final RefreshLayout refreshLayout, final int type){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, curPage+"");
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

                if (type == 1){
                    refreshLayout.finishRefresh();
                }else if(type ==2 ){
                    refreshLayout.finishLoadmore();
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure() {


                if (type == 2 && curPage>1){
                    curPage --;
                }
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
                curPage = 1;
                getNewsList(null, 0);
                break;
        }
    }
}
