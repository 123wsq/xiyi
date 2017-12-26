package com.example.wsq.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.OnWheelViewCalendarListener;
import com.example.wsq.android.view.CustomCalendarPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/26.
 */

public class BillDetailsActivity extends Activity{

    @BindView(R.id.tv_title) TextView tv_title;


    private CustomCalendarPopup calendarPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_bill_details);
        ButterKnife.bind(this);
        init();
    }


    public void init(){

        tv_title.setText("账单明细");
    }

    @OnClick({R.id.iv_back, R.id.iv_calendar})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_calendar:

                calendarPopup = new CustomCalendarPopup(this, listener);

                calendarPopup.showAtLocation(this.findViewById(R.id.ll_layout),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

        }
    }


    OnWheelViewCalendarListener listener = new OnWheelViewCalendarListener() {
        @Override
        public void onWheelViewChange(String year, String month, String day) {

        }
    };
}
