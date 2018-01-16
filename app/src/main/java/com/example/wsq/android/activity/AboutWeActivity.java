package com.example.wsq.android.activity;

import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.utils.AppUtils;
import com.example.wsq.android.utils.IntentFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/19.
 */

public class AboutWeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_version_name)
    TextView tv_version_name;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_about;
    }

    @Override
    public void init() {
        tv_title.setText("关于我们");

        tv_version_name.setText("v "+AppUtils.getLocalVersionName(this));
    }


    @OnClick({R.id.iv_back, R.id.ll_about, R.id.ll_protocols})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_about:
                IntentFormat.startActivity(this, AboutActivity.class);
                break;
            case R.id.ll_protocols:
                IntentFormat.startActivity(this, ProtocolsActivity.class);
                break;
        }
    }
}
