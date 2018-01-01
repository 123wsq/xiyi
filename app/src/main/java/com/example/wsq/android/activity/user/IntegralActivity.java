package com.example.wsq.android.activity.user;

import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分页面
 * Created by wsq on 2017/12/29.
 */

public class IntegralActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;

    @Override
    public int getByLayoutId() {

        return R.layout.layout_integral;
    }

    @Override
    public void init() {

        tv_title.setText("积分");

    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
