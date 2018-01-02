package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tag.androidtagview.TagContainerLayout;
import com.example.tag.androidtagview.TagView;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.order.DeviceListActivity;
import com.example.wsq.android.adapter.SearchRecordAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.IntentFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by wsq on 2017/12/23.
 */

public class SearchActivity extends Activity implements TagView.OnTagClickListener {

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.rv_search_Record) RecyclerView rv_search_Record;
    @BindView(R.id.tl_Tag_layout) TagContainerLayout tl_Tag_layout;
    @BindView(R.id.ll_hot) LinearLayout ll_hot;

    public static  int curPage = 0;
    private String[]  tagArrays;
    SharedPreferences shared;
    private SearchRecordAdapter mAdapter;
    private List<Map<String, String>> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        shared = getSharedPreferences(Constant.SHARED_RECORD, Context.MODE_PRIVATE);
        mData = new ArrayList<>();
        curPage = getIntent().getIntExtra("page",0);
        switch (curPage){
            case 1:
                et_search.setHint("搜索装备");
                ll_hot.setVisibility(View.VISIBLE);
                tagArrays = getResources().getStringArray(R.array.searchTag);
                tl_Tag_layout.setTags(tagArrays);
                tl_Tag_layout.setOnTagClickListener(this);
                break;
            case 2:
                et_search.setHint("请输入您要搜索的设备名称");
                ll_hot.setVisibility(View.GONE);
                break;
            case 3:
                et_search.setHint("请输入您所关注的资料");
                ll_hot.setVisibility(View.GONE);
                break;
        }
        rv_search_Record.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_search_Record.setLayoutManager(new LinearLayoutManager(this));
        rv_search_Record.setHasFixedSize(true);

        getInputContent();
        mAdapter = new SearchRecordAdapter(this, mData);

        rv_search_Record.setAdapter(mAdapter);
    }

    public void getInputContent(){
        Map<String, String> data = (Map<String, String>) shared.getAll();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()){
            Map<String, String> map = new HashMap<>();
            Map.Entry<String, String> entry = it.next();
            map.put("content", entry.getValue());
            map.put("key", entry.getKey());
            mData.add(map);
        }

    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @OnEditorAction({R.id.et_search})
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.KEYWORDS, et_search.getText().toString());
            String content = et_search.getText().toString();
            //判断看搜索的内容是否已经存在
            Map<String, String> data = (Map<String, String>) shared.getAll();
            Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
            boolean isExit = false;
            while (it.hasNext()){
                Map.Entry<String, String> entry = it.next();
                if (!TextUtils.isEmpty(content)){
                    if (content.equals(entry.getValue())){
                        isExit = true;
                    }
                }
            }
            if (!isExit){
                shared.edit().putString(System.currentTimeMillis()+"",
                        content ).commit();
            }

            if (curPage ==1 || curPage ==2) {
                IntentFormat.startActivity(SearchActivity.this, DeviceListActivity.class, map);


            }else{
                IntentFormat.startActivity(SearchActivity.this, FaultActivity.class, map);
            }
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onTagClick(int position, String text) {
        et_search.setText(tagArrays[position]);
        if (curPage ==1 || curPage ==2) {
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.KEYWORDS, tagArrays[position]);
            IntentFormat.startActivity(SearchActivity.this, DeviceListActivity.class, map);
            shared.edit().putString(System.currentTimeMillis()+"",
                    et_search.getText().toString()).commit();
            finish();
        }
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }
}
