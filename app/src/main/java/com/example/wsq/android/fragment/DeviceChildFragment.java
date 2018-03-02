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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.loader.GlideImageLoader;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.plugin.banner.Banner;
import com.example.wsq.plugin.banner.BannerConfig;
import com.example.wsq.plugin.banner.Transformer;
import com.example.wsq.plugin.banner.listener.OnBannerListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

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
    @BindView(R.id.webView)
    WebView webView;
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

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                List<LocalMedia> list = new ArrayList<>();
                for (int i = 0 ; i < mImages.size(); i++){

                    LocalMedia media = new LocalMedia();
                    media.setPath(mImages.get(i));
                    list.add(media);
                }
                PictureSelector.create(getActivity()).externalPicturePreview(position, list);
            }
        });


        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
//        webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        getDeviceInfo();
    }

    /**
     * 获取设备详情
     */
    public void getDeviceInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, getArguments().getString(ResponseKey.ID));
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        orderTaskService.onGetDeviceInfo(getActivity(), param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                tv_name.setText(result.get(ResponseKey.TITLE).toString());
                tv_device.setText(result.get(ResponseKey.PINPAI).toString());
                tv_train.setText(result.get(ResponseKey.XILIE).toString());
                String str = result.get(ResponseKey.CONTENT).toString();
                Logger.d(str.replace("\\",""));
                tv_content.setText(Html.fromHtml(str.replace("\\","")));


//                iv_device_icon
//                webView.loadData(result.get(ResponseKey.CONTENT).toString(),"text/html; charset=UTF-8", null);
//                    tv_content.loadData(result.get(ResponseKey.CONTENT).toString(),"text/html; charset=UTF-8", null);
                try {
                    JSONArray jsona = new JSONArray(result.get(ResponseKey.IMGS).toString());
                    for (int i=0; i < jsona.length(); i++){
//                        Glide.with(getActivity()).load(Urls.HOST + Urls.GET_IMAGES + jsona.get(i).toString()).into(iv_device_icon);
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
            public void onFailure() {

            }
        });

    }
}
