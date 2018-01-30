package com.example.wsq.android.activity.cash;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.BillDetailsAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.inter.OnWheelViewCalendarListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomCalendarPopup;
import com.example.wsq.android.view.LoddingDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * 账单明细页面
 * Created by wsq on 2017/12/26.
 */

public class BillDetailsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.ll_no_Record)
    LinearLayout ll_no_Record;
    @BindView(R.id.rg_group)
    RadioGroup rg_group;
    @BindView(R.id.rb_all)
    RadioButton rb_all;


    private CustomCalendarPopup calendarPopup;
    private SharedPreferences shared;
    private UserService userService;
    private BillDetailsAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_bill_details;
    }


    public void init(){

        tv_title.setText("账单明细");
        dialog = new LoddingDialog(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        userService = new UserServiceImpl();

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);
        mData = new ArrayList<>();

        rg_group.setOnCheckedChangeListener(this);

        mAdapter = new BillDetailsAdapter(this, mData, clickListener);
        rv_RecyclerView.setAdapter(mAdapter);

        rb_all.setChecked(true);
//        onGetBillInfo();
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

            mData.clear();
            onGetCurBillInfo(year, month);
        }
    };

    OnDefaultClickListener clickListener = new OnDefaultClickListener() {
        @Override
        public void onClickListener(int position) {
            Map<String, Object> param = new HashMap<>();
            param.put(ResponseKey.PAY_ID, mData.get(position).get(ResponseKey.PAY_ID));
            IntentFormat.startActivity(BillDetailsActivity.this, CashDepositInfoActivity.class, param);

        }
    };


    /**
     * 获取所有账单详情
     */
    public void onGetBillInfo(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        userService.onApplyCashDetailList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.CASH_LIST);
                mData.addAll(list);
                if (mData.size() != 0){
                    rv_RecyclerView.setVisibility(View.VISIBLE);
                    ll_no_Record.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }else{
                    rv_RecyclerView.setVisibility(View.GONE);
                    ll_no_Record.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 获取当前时间账单详情
     */
    public void onGetCurBillInfo(String year, String month){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.YEAR, year);
        param.put(ResponseKey.MONTH, month);

        userService.onCurCashDetailList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.CASH_LIST);
                mData.addAll(list);
                if (mData.size() != 0){
                    rv_RecyclerView.setVisibility(View.VISIBLE);
                    ll_no_Record.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }else{
                    rv_RecyclerView.setVisibility(View.GONE);
                    ll_no_Record.setVisibility(View.VISIBLE);
                }
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_all:
                 mData.clear();
                onGetBillInfo();
                break;
            case R.id.rb_cur_month:
                mData.clear();
                Calendar calendar = Calendar.getInstance();
                String year = calendar.get(Calendar.YEAR)+"";
                String month = (calendar.get(Calendar.MONTH)+1)+"";
                onGetCurBillInfo(year, month);
                break;
        }
    }
}
