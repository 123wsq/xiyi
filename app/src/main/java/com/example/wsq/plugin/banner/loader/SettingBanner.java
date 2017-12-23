package com.example.wsq.plugin.banner.loader;

import com.example.wsq.android.loader.GlideImageLoader;
import com.example.wsq.plugin.banner.Banner;
import com.example.wsq.plugin.banner.BannerConfig;
import com.example.wsq.plugin.banner.Transformer;

import java.util.List;

/**
 * Created by wsq on 2017/12/11.
 */

public class SettingBanner {

    public static void setWellComeView(Banner banner, List<Integer> images, List<String> titles){

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
    }
}
