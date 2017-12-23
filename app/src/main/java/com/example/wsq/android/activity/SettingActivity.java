package com.example.wsq.android.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;

/**
 * Created by wsq on 2017/12/13.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_setting;
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {

        iv_back = this.findViewById(R.id.iv_back);
        tv_title = this.findViewById(R.id.tv_title);
        tv_title.setText("我的设置");
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
