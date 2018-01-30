package com.example.wsq.android.activity.cash;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ReceiptsAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.inter.OnWheelViewCalendarListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.view.CustomCalendarPopup;
import com.example.wsq.android.view.LoddingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/15.
 */

public class ReceiptsActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.ll_no_Record) LinearLayout ll_no_Record;
    @BindView(R.id.tv_receipts_amount) TextView tv_receipts_amount;
    @BindView(R.id.tv_receipts_name) TextView tv_receipts_name;

    private CustomCalendarPopup calendarPopup;
    private SharedPreferences shared;
    private UserService userService;
    private ReceiptsAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_receipts;
    }

    @Override
    public void init() {

        tv_title.setText("收入明细");
        dialog = new LoddingDialog(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        userService = new UserServiceImpl();

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);
        mData = new ArrayList<>();


        mAdapter = new ReceiptsAdapter(this, mData, clickListener);
        rv_RecyclerView.setAdapter(mAdapter);


        getAllReceiptsLit();
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
            getetCurReceoptsInfo(year, month);
        }
    };

    OnDefaultClickListener clickListener = new OnDefaultClickListener() {
        @Override
        public void onClickListener(int position) {
            Map<String, Object> param = new HashMap<>();
            param.put(ResponseKey.PAY_ID, mData.get(position).get(ResponseKey.PAY_ID));
//            IntentFormat.startActivity(ReceiptsActivity.this, CashDepositInfoActivity.class, param);

        }
    };

    /**
     * 获取所有收入列表
     */
    public void getAllReceiptsLit(){

        dialog.show();

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        userService.getReceiptsList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.BROKERAGE_LIST);
                tv_receipts_amount.setText(result.get(ResponseKey.BROKERAGE_MOENY)+"");
                tv_receipts_name.setText("总收入");
                if (list.size() != 0 ){
                    mData.addAll(list);
                }
                mAdapter.notifyDataSetChanged();
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


    /**
     * 获取当前月的收入列表
     * @param year
     * @param month
     */
    public void getetCurReceoptsInfo(String year, String month){
            dialog.show();
        Map<String, String> param = new HashMap<>();
            param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
            param.put(ResponseKey.MONTH,month);
            param.put(ResponseKey.YEAR, year);

        userService.getReceiptsInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.BROKERAGE_LIST);
                tv_receipts_amount.setText(result.get(ResponseKey.BROKERAGE_MOENY)+"");
                tv_receipts_name.setText("月收入");
                if (list.size() != 0 ){
                    mData.addAll(list);
                }
                mAdapter.notifyDataSetChanged();
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
}
