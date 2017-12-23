package com.example.wsq.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tag.androidtagview.TagContainerLayout;
import com.example.tag.androidtagview.TagView;
import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.IntentFormat;

import java.util.HashMap;
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

    private int curPage = 0;
    private String[]  tagArrays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_search);
        ButterKnife.bind(this);
        init();
    }

    public void init(){


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
//            Logger.d("您点击了搜索");
            if (curPage ==1 || curPage ==2) {
                Map<String, Object> map = new HashMap<>();
                map.put(ResponseKey.KEYWORDS, et_search.getText().toString());
                IntentFormat.startActivity(SearchActivity.this, DeviceListActivity.class, map);
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onTagClick(int position, String text) {
        et_search.setText(tagArrays[position]);
    }

    @Override
    public void onTagLongClick(int position, String text) {

    }

    @Override
    public void onTagCrossClick(int position) {

    }
}
