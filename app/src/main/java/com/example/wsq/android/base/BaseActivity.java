package com.example.wsq.android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import butterknife.ButterKnife;


/**
 * Created by wsq on 2017/12/11.
 */

public abstract  class BaseActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(getByLayoutId());
        ButterKnife.bind(this);

        init();
    }



    public abstract int getByLayoutId();

    public abstract void init();


}
