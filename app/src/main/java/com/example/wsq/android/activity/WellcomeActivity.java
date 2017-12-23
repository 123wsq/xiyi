package com.example.wsq.android.activity;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.WellcomeAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 进入到欢迎页面
 * Created by wsq on 2017/12/11.
 */

public class WellcomeActivity  extends BaseActivity {

    private ViewPager viewPager;
    private WellcomeAdapter mAdapter;
    private LinearLayout layout_Indicator;

    private List<View> mDate;



    @Override
    public int getByLayoutId() {
        return R.layout.layout_wellcome;
    }

    @Override
    public void init() {



    }


    @Override
    public void initView() {

        viewPager = this.findViewById(R.id.viewPager);
        layout_Indicator = this.findViewById(R.id.layout_Indicator);

        getImages();
        drawIndicator();
        mAdapter = new WellcomeAdapter(this, mDate);
        viewPager.setAdapter(mAdapter);

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

}
