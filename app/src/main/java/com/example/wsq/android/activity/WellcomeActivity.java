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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.user.LoginActivity;
import com.example.wsq.android.adapter.WellcomeAdapter;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppImageLoad;
import com.example.wsq.android.utils.DensityUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.im_new_year) ImageView im_new_year;

    private WellcomeAdapter mAdapter;
    private List<View> mDate;
    private SharedPreferences shared;
    private int curLen = 5;
    private UserService userService;
    private RequestOptions options;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_wellcome);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        userService = new UserServiceImpl();

        options = new RequestOptions();
        options.error(R.drawable.image_new_year);
        options.fallback(R.drawable.image_new_year);
        options.placeholder(R.drawable.image_new_year);
        if (shared.getBoolean(Constant.SHARED.ISLOGIN, false)){

            rl_layout_new_year.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 1000);
            String path = shared.getString(Constant.SHARED.WELCOME_PATH, "");
            if (!TextUtils.isEmpty(path)){
                Glide.with(WellcomeActivity.this).load(Urls.HOST+Urls.GET_IMAGES+path).apply(options).into(im_new_year);
            }else{
                im_new_year.setBackgroundResource(R.drawable.image_new_year);
            }

            getImage();
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
                handler.removeCallbacks(runnable);
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

    public void getImage(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, "");
        param.put(ResponseKey.IMG_TYPE, "2");
        userService.onBannerImage(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                try{
                    JSONObject json = new JSONObject(result.get("data")+"");
                    JSONObject jsona =json.optJSONObject(ResponseKey.LIST);
                    shared.edit().putString(Constant.SHARED.WELCOME_PATH, jsona.optString(ResponseKey.IMG_PATH)).commit();
                    Glide.with(WellcomeActivity.this)
                            .load(Urls.HOST+Urls.GET_IMAGES+jsona.optString(ResponseKey.IMG_PATH))
                            .apply(options).into(im_new_year);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
