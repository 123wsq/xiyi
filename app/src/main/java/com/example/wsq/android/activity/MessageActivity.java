package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.MessageAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
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
 * Created by wsq on 2017/12/20.
 */

public class MessageActivity extends Activity{

    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @BindView(R.id.store_house_ptr_frame)
    SmartRefreshLayout store_house_ptr_frame;

    private int curPage = 1;
    private int total = 1;
    private int unitPage = 15;
    private UserService userService;
    private SharedPreferences shared;
    private MessageAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private LoddingDialog dialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);
        ButterKnife.bind(this);

        init();

    }


    public void init(){

        dialog = new LoddingDialog(this);


        mData = new ArrayList<>();

        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new MessageAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);

        setRefresh();
        getMessageList(null, 0);
    }

    @OnClick({R.id.iv_back, R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_refresh:
                curPage = 1;
                getMessageList(null, 0);
                break;
        }
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
                getMessageList(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){

                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getMessageList(refreshlayout, 2);
                }

            }
        });
    }


    /**
     * 获取消息列表
     */
    public void getMessageList(final RefreshLayout refreshLayout, final int type){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.PAGE, (curPage+1)+"");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        try {
            userService.onMessageList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    total = (int) result.get(ResponseKey.TOTAL);
                    unitPage = (int) result.get(ResponseKey.PER_PAGE);
                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);

                    mData.addAll(list);

                    if (mData.size() != 0){
                        mAdapter.notifyDataSetChanged();
                        rv_RecyclerView.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.GONE);
                    }else{
                        rv_RecyclerView.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
                    }

                    if (type == 1){
                        refreshLayout.finishRefresh();
                    }else if(type ==2 ){
                        refreshLayout.finishLoadmore();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onCallFail(String msg) {
                    if (type == 1){
                        refreshLayout.finishRefresh();
                    }else if(type ==2 ){
                        refreshLayout.finishLoadmore();
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }
}
