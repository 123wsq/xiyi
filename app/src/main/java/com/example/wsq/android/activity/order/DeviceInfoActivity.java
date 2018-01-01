package com.example.wsq.android.activity.order;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.DeviceChildFragment;
import com.example.wsq.android.fragment.DeviceInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/19.
 */

public class DeviceInfoActivity extends Activity{


    @BindView(R.id.iv_back) ImageView iv_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_device_info);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        enter(DeviceChildFragment.getInstance());

    }

    @OnClick({R.id.iv_back, R.id.tv_device, R.id.tv_info})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                    finish();
                break;
            case R.id.tv_device:
                enter(DeviceChildFragment.getInstance());
                break;
            case R.id.tv_info:
                enter(DeviceInfoFragment.getInstance());
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
}
