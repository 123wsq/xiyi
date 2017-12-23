package com.example.wsq.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.loader.GlideImageLoader;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.plugin.banner.Banner;
import com.example.wsq.plugin.banner.BannerConfig;
import com.example.wsq.plugin.banner.Transformer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wsq on 2017/12/19.
 */

public class DeviceChildFragment extends Fragment{

    @BindView(R.id.banner) Banner banner;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_device) TextView tv_device;
    @BindView(R.id.tv_train) TextView tv_train;
    @BindView(R.id.tv_content) TextView tv_content;

    private OrderTaskService orderTaskService;
    private SharedPreferences shared;
    private List<String> mImages;
    private List<String> mTitles;


    public static DeviceChildFragment getInstance(){
        return new DeviceChildFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.layout_device_child, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }


    public void init(){

        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        orderTaskService = new OrderTaskServiceImpl();




        mImages = new ArrayList<>();
        mTitles = new ArrayList<>();


        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.DURATION);
        //banner设置方法全部调用完毕时最后调用
//        banner.start();


        getDeviceInfo();
    }

    /**
     * 获取设备详情
     */
    public void getDeviceInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, getArguments().getString(ResponseKey.ID));
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        try {
            orderTaskService.onGetDeviceInfo(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {


                    tv_name.setText(result.get(ResponseKey.TITLE).toString());
                    tv_device.setText(result.get(ResponseKey.PINPAI).toString());
                    tv_train.setText(result.get(ResponseKey.XILIE).toString());
                    tv_content.setText(Html.fromHtml(result.get(ResponseKey.CONTENT).toString()));
//                    tv_content.loadData(result.get(ResponseKey.CONTENT).toString(),"text/html; charset=UTF-8", null);
                    try {
                        JSONArray jsona = new JSONArray(result.get(ResponseKey.IMGS).toString());
                        for (int i=0; i < jsona.length(); i++){
                            mImages.add(Urls.HOST+Urls.GET_IMAGES+jsona.get(i).toString());
                            mTitles.add("");
                        }
                        if (mImages.size()!= 0 && mTitles.size() == mImages.size()){
                            //设置图片集合
                            banner.setImages(mImages);
                            //设置标题集合（当banner样式有显示title时）
                            banner.setBannerTitles(mTitles);
                            banner.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
