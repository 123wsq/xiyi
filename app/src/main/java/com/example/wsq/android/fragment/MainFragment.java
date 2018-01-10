package com.example.wsq.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.order.DeviceWarrantyActivity;
import com.example.wsq.android.activity.KnowledgeActivity;
import com.example.wsq.android.activity.NewsActivity;
import com.example.wsq.android.activity.order.OrderActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.loader.GlideImageLoader;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.plugin.banner.Banner;
import com.example.wsq.plugin.banner.BannerConfig;
import com.example.wsq.plugin.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/7.
 */

public class MainFragment extends Fragment {


    @BindView(R.id.banner) Banner banner;


    private List<String> mImages;
    private List<String> mTitles;
    private SharedPreferences shared;
    private CustomPopup popup;

    public static MainFragment getInstance(){
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }


    public void init(){

        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        mImages = new ArrayList<>();
        mTitles = new ArrayList<>();
        mTitles.add("");
        mTitles.add("");
        mTitles.add("");
        mImages.add("http://aimg8.dlszyht.net.cn/ev_user_module_content_tmp/2017_09_15/tmp1505445769_1525182_s.jpg");
        mImages.add("http://aimg8.dlszyht.net.cn/ev_user_module_content_tmp/2017_09_15/tmp1505445784_1525182_s.jpg");
        mImages.add("http://aimg8.dlszyht.net.cn/ev_user_module_content_tmp/2017_09_15/tmp1505445797_1525182_s.jpg");
    }

    public void initView() {


        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mImages);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5*1000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.DURATION);
        //banner设置方法全部调用完毕时最后调用
        banner.start();


    }

    @OnClick({R.id.ll_device, R.id.ll_engineer, R.id.ll_news, R.id.ll_server, R.id.ll_knowledge})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_device:
                if (!shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
                    IntentFormat.startActivity(getActivity(), DeviceWarrantyActivity.class);
                }else{
                    Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_engineer:

                if (shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
                    Map<String, Object> mapP = new HashMap<>();
                    mapP.put(UserFragment.FLAG_ORDER_KEY, 7);
                    IntentFormat.startActivity(getActivity(), OrderActivity.class, mapP);
                }else{
                    Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_news:
                IntentFormat.startActivity(getActivity(), NewsActivity.class);
                break;
            case R.id.ll_server:

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_default_popup,null);
                TextView popup_title = view.findViewById(R.id.tv_title);
                popup_title.setText("提示");

                List<String> list = new ArrayList<>();
                list.add(getResources().getString(R.string.server_tel));
                popup = new CustomPopup(getActivity(), view, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {

                        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+result));
                        startActivity(intent);
                        popup.dismiss();
                    }
                });
                popup.setTextColor("#3F51B5");
                popup.showAtLocation(getActivity().findViewById(R.id.ll_layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_knowledge:
                IntentFormat.startActivity(getActivity(), KnowledgeActivity.class);
                break;
        }
    }
}
