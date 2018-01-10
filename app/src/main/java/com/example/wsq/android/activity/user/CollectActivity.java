package com.example.wsq.android.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.CollectAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.android.view.refresh.ClassicsCustomFooter;
import com.example.wsq.android.view.refresh.ClassicsCustomHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 我的收藏
 * Created by wsq on 2017/12/20.
 */

public class CollectActivity extends Activity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    @BindView(R.id.tv_Details) TextView tv_Details;
    @BindView(R.id.ll_collect_buttom) LinearLayout ll_collect_buttom;
    @BindView(R.id.cb_all_select) CheckBox cb_all_select;
    @BindView(R.id.store_house_ptr_frame)
    SmartRefreshLayout store_house_ptr_frame;

    private LoddingDialog dialog;
    private CollectAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private UserService userService;
    private SharedPreferences shared;
    private int curPage = 1;
    private int total = 1;
    private int unitPage = 15;
    private boolean isEditCollect = false;
    public static final String COLLECT = "COLLECT";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_collect);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        init();
    }

    public void init(){

        dialog = new LoddingDialog(this);
        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        tv_Details.setText("编辑");
        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 10),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_title.setText("我的收藏");
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new CollectAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);

        setRefresh();
        getCollectData(null, 0);
    }

    public void setRefresh(){

//        store_house_ptr_frame.setRefreshHeader(new ClassicsHeader(this)
//                .setProgressResource(R.drawable.refresh_loadding).setDrawableProgressSize(40));
        store_house_ptr_frame.setRefreshHeader(new ClassicsCustomHeader(this));
//        store_house_ptr_frame.setRefreshFooter(new ClassicsFooter(this)
//                .setArrowResource(R.drawable.refresh_loadding).setDrawableArrowSize(50));
//        store_house_ptr_frame.setRefreshFooter(new ClassicsFooter(this));
        store_house_ptr_frame.setRefreshFooter(new ClassicsCustomFooter(this));

        store_house_ptr_frame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mData.clear();
                curPage = 1;
                refreshlayout.resetNoMoreData();
                getCollectData(refreshlayout, 1 );
            }
        });
        store_house_ptr_frame.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                if (curPage == (total % unitPage ==0 ? total/unitPage : total/unitPage +1)){

                    refreshlayout.finishLoadmoreWithNoMoreData();
                }else {
                    curPage++;
                    getCollectData(refreshlayout, 2);
                }

            }
        });
    }
    @OnCheckedChanged({R.id.cb_all_select})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

        mAdapter.onAllSelected(isChecked);
    }
    @OnClick({R.id.iv_back, R.id.tv_refresh, R.id.tv_finish, R.id.tv_Details})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
            break;
            case R.id.tv_refresh:
                curPage = 1;
                getCollectData(null, 0);
                break;
            case R.id.tv_Details:
                if (isEditCollect){
                    isEditCollect = false;
                    tv_Details.setText("编辑");
                    ll_collect_buttom.setVisibility(View.GONE);
                    cb_all_select.setChecked(false);
                    mAdapter.onEditCollect(true);
                    mAdapter.onAllSelected(false);
                }else {
                    isEditCollect = true;
                    tv_Details.setText("完成");
                    ll_collect_buttom.setVisibility(View.VISIBLE);
                    mAdapter.onEditCollect(false);
                    cb_all_select.setChecked(false);
                    mAdapter.onAllSelected(false);
                }
                mAdapter.onEditCollect(isEditCollect);
                break;
            case R.id.tv_finish:

                onCancelCollect();
                isEditCollect = false;
                tv_Details.setText("编辑");
                ll_collect_buttom.setVisibility(View.GONE);
                mAdapter.onAllSelected(false);
                mAdapter.onEditCollect(false);

                break;
        }
    }

    /**
     * 获取收藏列表
     */
    public void getCollectData(final RefreshLayout refreshLayout, final int type){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.PAGE, curPage+"");

        userService.getCollectList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                total = (int) result.get(ResponseKey.TOTAL);
                unitPage = (int) result.get(ResponseKey.PER_PAGE);
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.DATA);

                if (list.size()!= 0){
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                if (mData.size()==0){
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

    /**
     * 取消收藏
     */
    public void onCancelCollect(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        final List<String> selectData = mAdapter.getSelected();

        if (selectData.size()==0){

            dialog.dismiss();
            return ;
        }
        JSONArray jsona = new JSONArray();
        for (int i = 0; i < selectData.size(); i++) {

            jsona.put(selectData.get(i));
        }

        param.put(ResponseKey.IDS, jsona.toString());
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        userService.onCancelCollect(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(CollectActivity.this, result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
//                if ((int)result.get(ResponseKey.CODE)==1001){
//
//
//                    //必须要从后往前删除  防止顺序时删除多个导致对应的索引不一致
//                    for (int i= mData.size() -1 ; i > 0; i --){
//
//                        if (selectData.contains(mData.get(i).get(ResponseKey.ID)+"")){
//                            mData.remove(i);
//                        }
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }
                dialog.dismiss();
                mData.clear();
                curPage = 1;
                getCollectData(null, 0);

            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });

    }


}
