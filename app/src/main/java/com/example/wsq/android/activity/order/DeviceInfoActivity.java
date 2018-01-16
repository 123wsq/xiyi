package com.example.wsq.android.activity.order;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.DeviceChildFragment;
import com.example.wsq.android.fragment.DeviceInfoFragment;
import com.example.wsq.android.tools.AppStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/19.
 */

public class DeviceInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_radioGroup) RadioGroup rg_radioGroup;
    @BindView(R.id.tv_device) RadioButton tv_device;



    @Override
    public int getByLayoutId() {
        return R.layout.layout_device_info;
    }

    public void init(){

//        enter(DeviceChildFragment.getInstance());

        rg_radioGroup.setOnCheckedChangeListener(this);
        tv_device.setChecked(true);

    }
    @OnClick({R.id.iv_back})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                    finish();
                break;
        }
    }

    public void enter(Fragment fragment){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(ResponseKey.ID, getIntent().getIntExtra(ResponseKey.ID, 0)+"");
        fragment.setArguments(bundle);
        ft.replace(R.id.ll_layout, fragment);
        ft.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.tv_device:
                enter(DeviceChildFragment.getInstance());
                break;
            case R.id.tv_info:
                enter(DeviceInfoFragment.getInstance());
                break;
        }
    }
}
