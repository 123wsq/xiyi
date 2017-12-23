package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;

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
    @BindView(R.id.tv_edit) TextView tv_edit;
    @BindView(R.id.ll_collect_buttom) LinearLayout ll_collect_buttom;
    @BindView(R.id.cb_all_select)
    CheckBox cb_all_select;

    private CollectAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private UserService userService;
    private SharedPreferences shared;
    private int curPage = 0;
    private boolean isEditCollect = false;
    public static final String COLLECT = "COLLECT";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_collect);

        ButterKnife.bind(this);

        init();
    }

    public void init(){

        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_title.setText("我的收藏");
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new CollectAdapter(this, mData);
        rv_RecyclerView.setAdapter(mAdapter);
//        mAdapter.setCollect();
        getCollectData();
    }

    @OnCheckedChanged({R.id.cb_all_select})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

        mAdapter.onAllSelected(isChecked);
    }
    @OnClick({R.id.iv_back, R.id.tv_refresh, R.id.tv_finish, R.id.tv_edit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
            break;
            case R.id.tv_refresh:

                getCollectData();
                break;
            case R.id.tv_edit:
                if (isEditCollect){
                    isEditCollect = false;
                    tv_edit.setText("编辑");
                    ll_collect_buttom.setVisibility(View.GONE);
                    cb_all_select.setChecked(false);
                    mAdapter.onEditCollect(true);
                    mAdapter.onAllSelected(false);
                }else {
                    isEditCollect = true;
                    tv_edit.setText("取消");
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
                tv_edit.setText("编辑");
                ll_collect_buttom.setVisibility(View.GONE);
                mAdapter.onAllSelected(false);
                mAdapter.onEditCollect(false);

                break;
        }
    }

    /**
     * 获取收藏列表
     */
    public void getCollectData(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.PAGE, (curPage+1)+"");

        try {
            userService.getCollectList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

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
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(CollectActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取消收藏
     */
    public void onCancelCollect(){

        Map<String, String> param = new HashMap<>();
        final List<String> selectData = mAdapter.getSelected();

        if (selectData.size()==0){

            return ;
        }
        JSONArray jsona = new JSONArray();
        for (int i = 0; i < selectData.size(); i++) {
//            JSONObject json = new JSONObject();
//            try {
//                json.put(ResponseKey.ID, selectData.get(i));
//                jsona.put(json);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            jsona.put(selectData.get(i));
        }

        param.put(ResponseKey.IDS, jsona.toString());
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        try {
            userService.onCancelCollect(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    Toast.makeText(CollectActivity.this, result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                    if ((int)result.get(ResponseKey.CODE)==1001){


                        //必须要从后往前删除  防止顺序时删除多个导致对应的索引不一致
                        for (int i= mData.size() -1 ; i > 0; i --){

                            if (selectData.contains(mData.get(i).get(ResponseKey.ID)+"")){
                                mData.remove(i);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(CollectActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
