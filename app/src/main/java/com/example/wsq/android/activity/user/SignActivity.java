package com.example.wsq.android.activity.user;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.SignCalendarAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.utils.CalendarUtils;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.android.view.SignPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 签到页面
 * Created by wsq on 2017/12/28.
 */

public class SignActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_calendar) RecyclerView rv_calendar;
    @BindView(R.id.iv_startSign) ImageView iv_startSign;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;

    private SignCalendarAdapter mAdapter;
    private List<String> calendar;
    private UserService userService;
    private LoddingDialog dialog;

    private SignPopup popup;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_sign;
    }

    @Override
    public void init() {

        tv_title.setText("签到");
        getCurMonthDay();
        dialog = new LoddingDialog(this);


        rv_calendar.setLayoutManager(new GridLayoutManager(this, 7));
        rv_calendar.setHasFixedSize(true);

        mAdapter = new SignCalendarAdapter(this, calendar);
        rv_calendar.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back, R.id.iv_startSign})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_startSign:

                popup = new SignPopup(SignActivity.this);
                popup.showAtLocation(ll_layout, Gravity.CENTER, 0, 0);

                break;
        }
    }


    public void onSign(){

        dialog.show();
        Map<String, String> param = new HashMap<>();

        userService.onSign(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                iv_startSign.setImageResource(R.drawable.image_sign_finish);

                dialog.dismiss();
            }

            @Override
            public void onFailure() {

                dialog.dismiss();
            }
        });
    }

    /**
     * 获取当前月的天数
     */
    public void getCurMonthDay(){
        calendar = new ArrayList<>();

        int count = CalendarUtils.getMonthDays();
        //判断这个月是从星期几开始的
        int startDay = CalendarUtils.getMonthFirstWeek();

        for (int i = 0 ;i< startDay-1; i++){
            calendar.add("");
        }

        for (int i =0 ;i< count; i++){
            calendar.add((i+1)+"");
        }

    }



}
