package com.example.wsq.android.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.user.LoginActivity;
import com.example.wsq.android.adapter.WellcomeAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 进入到欢迎页面
 * Created by wsq on 2017/12/11.
 */

public class WellcomeActivity  extends Activity {

    @BindView(R.id.viewPager) ViewPager viewPager;

    @BindView(R.id.layout_Indicator) LinearLayout layout_Indicator;
    @BindView(R.id.rl_layout_page)
    RelativeLayout rl_layout_page;
    @BindView(R.id.rl_layout_new_year) RelativeLayout rl_layout_new_year;
    @BindView(R.id.tv_break) TextView tv_break;

    private WellcomeAdapter mAdapter;
    private List<View> mDate;
    private SharedPreferences shared;
    private int curLen = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_wellcome);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);


        if (shared.getBoolean(Constant.SHARED.ISLOGIN, false)){

            rl_layout_new_year.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 1000);
        }else {
            rl_layout_page.setVisibility(View.VISIBLE);
            getImages();
            drawIndicator();
            mAdapter = new WellcomeAdapter(this, mDate);
            viewPager.setAdapter(mAdapter);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int cout  = layout_Indicator.getChildCount();
                for (int i = 0 ;  i< cout ; i ++){
                    ImageView view = (ImageView) layout_Indicator.getChildAt(i);

                    view.setImageResource(i==position ? R.drawable.image_indicator_selector :R.drawable.image_indicator_default);
                    TextView text_btn =  mDate.get(position).findViewById(R.id.text_btn);

                    text_btn.setVisibility(position == mDate.size()-1 ? View.VISIBLE : View.GONE);

                    if(position ==  mDate.size()-1){
                        text_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(WellcomeActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void getImages(){

        mDate=  new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0 ; i < 4; i++){

            mDate.add(inflater.inflate(R.layout.layout_wellcome_page, null));
        }


    }

    public  void drawIndicator(){

        for (int i = 0 ; i<mDate.size(); i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 5));
            params.leftMargin = DensityUtil.dp2px(this, 10);
            imageView.setLayoutParams(params);

            imageView.setImageResource(i==0 ? R.drawable.image_indicator_selector :R.drawable.image_indicator_default);

            layout_Indicator.addView(imageView);
        }

    }

    @OnClick({R.id.tv_break})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_break:
                startActivity(new Intent(WellcomeActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 0){
                startActivity(new Intent(WellcomeActivity.this, LoginActivity.class));
                finish();
            }else{
                tv_break.setText("跳过 "+curLen+"s");
                handler.postDelayed(this, 1000);
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
