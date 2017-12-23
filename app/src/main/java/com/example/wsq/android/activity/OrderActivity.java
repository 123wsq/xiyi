package com.example.wsq.android.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.fragment.AuditFragment;
import com.example.wsq.android.fragment.DisposeFragment;
import com.example.wsq.android.fragment.FinishOrderFragment;
import com.example.wsq.android.fragment.ServerFeedbackFragment;
import com.example.wsq.android.fragment.ServerFinshFragment;
import com.example.wsq.android.fragment.ServerFllocationFragment;
import com.example.wsq.android.fragment.ServerProgressFragment;
import com.example.wsq.android.fragment.UnAuditFragment;
import com.example.wsq.android.fragment.UserFragment;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup rg_group, rg_group_s;
    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout layout_server, layout_manager;


    int curPage=0;
    SharedPreferences shared;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_layout);
        init();
        initView();

    }

    public void init(){

        //接收传过来的显示页面值
        curPage = getIntent().getIntExtra(UserFragment.FLAG_ORDER_KEY, 0);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);



        switch (curPage){
            case 1:
                enter(UnAuditFragment.getInstance());
                break;
            case 2:
                enter(AuditFragment.getInstance());
                break;
            case 3:
                enter(DisposeFragment.getInstance());
                break;
            case 4:
                enter(FinishOrderFragment.getInstance());
                break;
            case 5:
                enter(ServerFllocationFragment.getInstance());
                break;
            case 6:
                enter(ServerProgressFragment.getInstance());
                break;
            case 7:
                enter(ServerFeedbackFragment.getInstance());
                break;
            case 8:
                enter(ServerFinshFragment.getInstance());
                break;
        }


    }

    public void initView(){
        rg_group = this.findViewById(R.id.rg_group);
        iv_back = this.findViewById(R.id.iv_back);
        tv_title = this.findViewById(R.id.tv_title);
        layout_server = this.findViewById(R.id.layout_server);
        layout_manager = this.findViewById(R.id.layout_manager);
        rg_group_s = this.findViewById(R.id.rg_group_s);

        tv_title.setText("我的订单");
        iv_back.setOnClickListener(this);
        rg_group.setOnCheckedChangeListener(this);
        rg_group_s.setOnCheckedChangeListener(this);

        if (curPage<=4){
            layout_manager.setVisibility(View.VISIBLE);
            layout_server.setVisibility(View.GONE);
        }else {
            layout_manager.setVisibility(View.GONE);
            layout_server.setVisibility(View.VISIBLE);
        }


    }


    public void enter(Fragment fragment){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.ll_order, fragment);
        ft.commit();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        switch (checkedId){
            case R.id.rb_unfinsh:
                enter(UnAuditFragment.getInstance());
                break;
            case R.id.rb_dispose:
                enter(DisposeFragment.getInstance());
                break;
            case R.id.rb_finish:
                enter(AuditFragment.getInstance());
                break;
            case R.id.rb_order:
                enter(FinishOrderFragment.getInstance());
                break;


            case R.id.rb_fllocation:
                enter(ServerFllocationFragment.getInstance());
                break;
            case R.id.rb_progress:
                enter(ServerProgressFragment.getInstance());
                break;
            case R.id.rb_feedback:
                enter(ServerFeedbackFragment.getInstance());
                break;
            case R.id.rb_finish_s:
                enter(ServerFinshFragment.getInstance());
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
