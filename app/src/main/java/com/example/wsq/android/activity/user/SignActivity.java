package com.example.wsq.android.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.SignCalendarAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.CalendarUtils;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.android.view.SignPopup;

import java.util.ArrayList;
import java.util.Date;
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
    @BindView(R.id.tv_view)
    WebView tv_view;
    @BindView(R.id.tv_time) TextView tv_time;

    private SignCalendarAdapter mAdapter;
    private List<String> calendar;
    private List<Map<String, Object>> mData;
    private UserService userService;
    private LoddingDialog dialog;

    private SignPopup popup;
    private SharedPreferences shared;
    private Map<Integer, Boolean> isSign; //是否签到

    @Override
    public int getByLayoutId() {
        return R.layout.layout_sign;
    }


    @Override
    public void init() {

        tv_title.setText("签到");
        isSign = new HashMap<>();
        userService = new UserServiceImpl();
        mData = new ArrayList<>();

        getCurMonthDay();

        for (int i= 1; i<= calendar.size(); i++){
            isSign.put(i, false);
        }

        dialog = new LoddingDialog(this);

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        rv_calendar.setLayoutManager(new GridLayoutManager(this, 7));
        rv_calendar.setHasFixedSize(true);

        ;

        mAdapter = new SignCalendarAdapter(this, calendar, mData);
        rv_calendar.setAdapter(mAdapter);

        onGetSignList();
    }

    @OnClick({R.id.iv_back, R.id.iv_startSign})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_startSign:

                onSign();

                break;
        }
    }


    /**
     * 签到
     */
    public void onSign(){

        dialog.show();
        Map<String, String> param = new HashMap<>();

        final String date = DateUtil.onDateFormat(new Date(),DateUtil.DATA_FORMAT_3);

        String sDate = Date.parse(date)+"";
//        Logger.d(Date.parse(date)+"");
        param.put(ResponseKey.CREATE_TIME, sDate.substring(0, sDate.length()-3));
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        userService.onSign(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Toast.makeText(SignActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();

                String time = tv_time.getText()+"";
                tv_time.setText((Integer.parseInt(time)+1)+"");

                iv_startSign.setImageResource(R.drawable.image_sign_finish);

                popup = new SignPopup(SignActivity.this);
                popup.setIntegral("1");
                popup.showAtLocation(ll_layout, Gravity.CENTER, 0, 0);

                dialog.dismiss();
                //获取签到列表
                onGetSignList();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 获取签到列表
     */
    public void onGetSignList(){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        userService.onSignList(SignActivity.this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mData.clear();
                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.SIGN_LIST);

                if (list.size() != 0){

                    mData.addAll(list);
                }


                //将选中的日期添加到列表中
                for (int i = 0 ; i< mData.size() ; i++){
                    String  create_Time = mData.get(i).get(ResponseKey.CREATE_TIME)+"000";
                    String sDay = DateUtil.onMillisForDay(create_Time);
                    int day = Integer.parseInt(sDay);
//                    System.out.println(i+"     "+day);
                    isSign.put(day, true);

                }

//                Logger.d(isSign);
                    //获取当天的天数
                int curDay = Integer.parseInt(DateUtil.onMillisForDay(System.currentTimeMillis()+""));
                //判断连续签到的天数
                int num = 0 ;
                for (int i= curDay; i > 0; i--){

                    if (i == curDay && !isSign.get(i)){

                    }else {
                        if (isSign.get(i)) {
                            num++;
                        } else {
                            break;
                        }
                    }
                }
                if (isSign.get(curDay)){
                    iv_startSign.setImageResource(R.drawable.image_sign_finish);
//                    iv_startSign.setClickable(false);
                }

                tv_time.setText(num+"");

                tv_view.loadData(result.get(ResponseKey.GUIZE)+"", "text/html; charset=UTF-8", null);

                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
