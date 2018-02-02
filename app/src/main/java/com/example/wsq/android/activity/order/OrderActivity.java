package com.example.wsq.android.activity.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.OrderFragmentAdapter;
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
import com.example.wsq.android.tools.AppImageView;
import com.example.wsq.android.tools.AppStatus;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_group) RadioGroup rg_group;
    @BindView(R.id.rg_group_s) RadioGroup rg_group_s;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.layout_server) LinearLayout layout_server;
    @BindView(R.id.layout_manager) LinearLayout layout_manager;
    @BindView(R.id.vp_viewPager) ViewPager vp_viewPager;
    @BindView(R.id.rb_unfinsh) RadioButton rb_unfinsh;
    @BindView(R.id.rb_finish) RadioButton rb_finish;
    @BindView(R.id.rb_dispose) RadioButton rb_dispose;
    @BindView(R.id.rb_order) RadioButton rb_order;
    @BindView(R.id.rb_fllocation) RadioButton rb_fllocation;
    @BindView(R.id.rb_progress) RadioButton rb_progress;
    @BindView(R.id.rb_feedback) RadioButton rb_feedback;
    @BindView(R.id.rb_finish_s) RadioButton rb_finish_s;

    int curPage=0;
    SharedPreferences shared;
    private List<Fragment> mData;
    private OrderFragmentAdapter mAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_layout);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        init();
        initView();
        setIcon();
    }

    public void init(){

        //接收传过来的显示页面值
        curPage = getIntent().getIntExtra(UserFragment.FLAG_ORDER_KEY, 0);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);




        rg_group.setOnCheckedChangeListener(this);
        rg_group_s.setOnCheckedChangeListener(this);

        Logger.d("服务工程师进入   "+curPage);
        switch (curPage){
            case 1:
                enter(UnAuditFragment.getInstance());
                rb_unfinsh.setChecked(true);
                break;
            case 2:
                enter(AuditFragment.getInstance());
                rb_finish.setChecked(true);
                break;
            case 3:
                enter(DisposeFragment.getInstance());
                rb_dispose.setChecked(true);
                break;
            case 4:
                enter(FinishOrderFragment.getInstance());
                rb_order.setChecked(true);
                break;
            case 5:
                enter(ServerFllocationFragment.getInstance());
                rb_fllocation.setChecked(true);
                break;
            case 6:
                enter(ServerProgressFragment.getInstance());
                rb_progress.setChecked(true);
                break;
            case 7:
                enter(ServerFeedbackFragment.getInstance());
                rb_feedback.setChecked(true);
                break;
            case 8:
                enter(ServerFinshFragment.getInstance());
                rb_finish_s.setChecked(true);
                break;
        }


    }

    public void initView(){


        tv_title.setText("我的订单");


        if (curPage <= 4){
            layout_manager.setVisibility(View.VISIBLE);
            layout_server.setVisibility(View.GONE);
        }else {
            layout_manager.setVisibility(View.GONE);
            layout_server.setVisibility(View.VISIBLE);
        }


    }


    public void enter(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.ll_order, fragment).commit();
    }




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        switch (checkedId){
            case R.id.rb_unfinsh:
                enter(UnAuditFragment.getInstance());
//                vp_viewPager.setCurrentItem(0);
                break;
            case R.id.rb_dispose:
                enter(DisposeFragment.getInstance());
//                vp_viewPager.setCurrentItem(1);
                break;
            case R.id.rb_finish:
                enter(AuditFragment.getInstance());
//                vp_viewPager.setCurrentItem(2);
                break;
            case R.id.rb_order:
                enter(FinishOrderFragment.getInstance());
//                vp_viewPager.setCurrentItem(3);
                break;

            case R.id.rb_fllocation:
                enter(ServerFllocationFragment.getInstance());
//                vp_viewPager.setCurrentItem(0);
                break;
            case R.id.rb_progress:
                enter(ServerProgressFragment.getInstance());
//                vp_viewPager.setCurrentItem(1);
                break;
            case R.id.rb_feedback:
                enter(ServerFeedbackFragment.getInstance());
//                vp_viewPager.setCurrentItem(2);
                break;
            case R.id.rb_finish_s:
                enter(ServerFinshFragment.getInstance());
//                vp_viewPager.setCurrentItem(3);
                break;

        }
    }


    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position){

        if (!shared.getString(Constant.SHARED.JUESE, "").equals("1")){
                switch (position){
                    case 0:
                        rb_unfinsh.setChecked(true);
                        break;
                    case 1:
                        rb_finish.setChecked(true);
                        break;
                    case 2:
                        rb_dispose.setChecked(true);
                        break;
                    case 3:
                        rb_order.setChecked(true);
                        break;
                }
        }else{
            switch (position){
                case 0:
                    rb_fllocation.setChecked(true);
                    break;
                case 1:
                    rb_progress.setChecked(true);
                    break;
                case 2:
                    rb_feedback.setChecked(true);
                    break;
                case 3:
                    rb_finish_s.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }


    public void setIcon(){
        if (shared.getString(Constant.SHARED.JUESE, "").equals("1")) {

            AppImageView.onRadioButton(this, rb_fllocation, "image_my_approved.png",22, 22);
            AppImageView.onRadioButton(this, rb_progress, "image_clz.png",22, 22);
            AppImageView.onRadioButton(this, rb_feedback, "image_feedback.png",22, 22);
            AppImageView.onRadioButton(this, rb_finish_s, "image_tab_finish.png",22, 22);
        }else {
            //企业-管理工程师
            AppImageView.onRadioButton(this, rb_unfinsh, "image_tab_untreated.png",22, 22);
            AppImageView.onRadioButton(this, rb_finish, "image_tab_treated.png",22, 22);
            AppImageView.onRadioButton(this, rb_dispose, "image_clz.png",22, 22);
            AppImageView.onRadioButton(this, rb_order, "image_tab_finish.png",22, 22);

        }
    }

}
