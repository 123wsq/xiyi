package com.example.wsq.android.activity;

import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/9 0009.
 */

public class CityActvity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    public int getByLayoutId() {
        return R.layout.layout_city_list;
    }

    @Override
    public void init() {

        tv_title.setText("城市");
    }
}
