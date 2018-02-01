package com.example.wsq.android.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.SkillAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.SkillBean;
import com.example.wsq.android.inter.OnRecycCheckListener;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务工程师的技能界面
 * Created by Administrator on 2018/2/1 0001.
 */

public class SkillServiceActivity extends BaseActivity{

    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_Details) TextView tv_Details;


    private SkillAdapter mAdapter;
    private List<SkillBean> mData;
    private List<SkillBean> mSelectData;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_skill_service;
    }

    @Override
    public void init() {

        mData = new ArrayList<>();
        List<SkillBean> list = (List<SkillBean>) getIntent().getSerializableExtra("skill");
        String[] array = getResources().getStringArray(R.array.arrays_skill);
        for (int i = 0; i < array.length; i++) {
            SkillBean bean = new SkillBean();
            for (int j = 0 ; j< list.size(); j++){
                if (array[i].equals(list.get(j).getSkillName())){
                    bean.setState(true);
                }
            }
            bean.setSkillName(array[i]);
            mData.add(bean);
        }

        mSelectData = new ArrayList<>();
        tv_title.setText("技能");
        tv_Details.setText("确定");

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 10),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rv_RecyclerView.setHasFixedSize(true);
        mAdapter = new SkillAdapter(this, mData, listener, null, 0);
        rv_RecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.tv_Details,R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_Details:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("skill", (Serializable) mSelectData);
                intent.putExtras(bundle);
                setResult(UserInfoActivity.DEFAULT_SKILL_RESULT, intent);
                finish();
                break;
        }
    }

    OnRecycCheckListener listener = new OnRecycCheckListener() {
        @Override
        public void onCheckChangeListener(String result, int position, boolean state) {

            if (state){
                mSelectData.add(new SkillBean(result));
            }else {

                for (int i = 0 ; i< mSelectData.size() ; i ++){

                    String skillName = mSelectData.get(i).getSkillName();
                    if (skillName.equals(result)){
                        mSelectData.remove(i);
                    }
                }
            }
        }
    };
}
