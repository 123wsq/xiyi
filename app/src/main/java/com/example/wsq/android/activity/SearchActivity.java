package com.example.wsq.android.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.wsq.android.activity.order.DeviceListActivity;
import com.example.wsq.android.adapter.SearchRecordAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.db.dao.impl.SearchDbImpl;
import com.example.wsq.android.db.dao.inter.SearchDbInter;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.IntentFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by wsq on 2017/12/23.
 */

public class SearchActivity extends BaseActivity implements TagView.OnTagClickListener {

    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.rv_search_Record) RecyclerView rv_search_Record;
    @BindView(R.id.tl_Tag_layout) TagContainerLayout tl_Tag_layout;
    @BindView(R.id.ll_hot) LinearLayout ll_hot;
    @BindView(R.id.tv_clear_all) TextView tv_clear_all;

    public static  int curPage = 0;
    private List<String>  tagArrays;
    private SearchRecordAdapter mAdapter;
    private List<Map<String, String>> mData;
    private SearchDbInter searchDbInter;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_search;
    }

    public void init(){
        tagArrays = new ArrayList<>();
        searchDbInter = new SearchDbImpl();
        mData = new ArrayList<>();
        curPage = getIntent().getIntExtra("page",0);
        switch (curPage){
            case 1:
                et_search.setHint("搜索装备");
                ll_hot.setVisibility(View.VISIBLE);
                String[] arrays = getResources().getStringArray(R.array.searchTag);
                //int[] color = {TagBackgroundColor, TabBorderColor, TagTextColor}
                int[] col = {Color.parseColor("#E5E5E5"), Color.parseColor("#F2F2F2"), Color.parseColor("#565656")};
                List<int[]> list = new ArrayList<>();
                for (int i =0 ; i< arrays.length; i++){
                    tagArrays.add(arrays[i]);
                    list.add(col);
                }

                tl_Tag_layout.setTags(tagArrays, list);
                tl_Tag_layout.setOnTagClickListener(this);
                break;
            case 2:
                et_search.setHint("请输入您要搜索的设备名称");
                ll_hot.setVisibility(View.GONE);
                break;
            case 3:
                et_search.setHint("请输入您要搜索的资料名称");
                ll_hot.setVisibility(View.GONE);
                break;
        }
//        tl_Tag_layout.setTagBackgroundColor(getResources().getColor(R.color.default_content_color_2));
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

        mData.clear();
        List<Map<String, String>>  list = searchDbInter.selectAll(this);
        mData.addAll(list);

    }

    @OnClick({R.id.tv_cancel, R.id.tv_clear_all})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_clear_all:
                searchDbInter.onClearAll(this);
                getInputContent();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnEditorAction({R.id.et_search})
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.KEYWORDS, et_search.getText().toString());
            String content = et_search.getText().toString();
            searchDbInter.insertData(this, content);

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
        et_search.setText(tagArrays.get(position));
        if (curPage ==1 || curPage ==2) {
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.KEYWORDS, tagArrays.get(position));
            IntentFormat.startActivity(SearchActivity.this, DeviceListActivity.class, map);

            searchDbInter.insertData(this, et_search.getText()+"");
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
