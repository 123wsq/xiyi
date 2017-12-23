package com.example.wsq.android.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wsq.android.R;

/**
 * Created by wsq on 2017/12/23.
 */

public class SysLoading  extends LinearLayout {

    private View view;
    //自定义动画
    private AnimationDrawable mAnimation;
    //加载失败视图
    private RelativeLayout sys_loading_dialog_fail;
    //加载中图片
    private ImageView sys_loading_dialog_img;
    //加载中文本
    private TextView sys_loading_dialog_tv;
    //加载失败文本
    private TextView sys_loading_dialog_fail_tv;
    //加载时文本
    private String loadingText;

    public SysLoading(Context context) {
        super(context);
        initView(context);
    }

    public SysLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.sys_loading_dialog, this);
        //加载失败视图
        sys_loading_dialog_fail = (RelativeLayout) view.findViewById(R.id.sys_loading_dialog_fail);
        //加载时图片
        sys_loading_dialog_img = (ImageView) view.findViewById(R.id.sys_loading_dialog_img);
        //加载时文本
        sys_loading_dialog_tv = (TextView) view.findViewById(R.id.sys_loading_dialog_tv);
        sys_loading_dialog_fail_tv = (TextView) view.findViewById(R.id.sys_loading_dialog_fail_tv);
    }

    public void setText(String loadingText){
        this.loadingText = loadingText;
    }
    public void showAnim(){

        //设置动画特效
        initData();
    }


    public void stopAnim(){
        mAnimation.stop();
    }






    public void initData() {
        //设置文本
        sys_loading_dialog_tv.setText(loadingText);
        //设置显示
        view.setVisibility(View.VISIBLE);
        //设置加载时图片显示
        sys_loading_dialog_img.setVisibility(View.VISIBLE);
        //设置加载时文本显示
        sys_loading_dialog_tv.setVisibility(View.VISIBLE);
        //设置失败视图隐藏
        sys_loading_dialog_fail.setVisibility(View.GONE);
        //获取动画
        sys_loading_dialog_img.setBackgroundResource(R.drawable.sys_loading);
        //通过ImageView拿到AnimationDrawable
        mAnimation = (AnimationDrawable) sys_loading_dialog_img.getBackground();
        //为了防止只显示第一帧
        sys_loading_dialog_img.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
    }

    //加载失败调用的方法
    public void fialLoad(String failStr, View.OnClickListener listener){
        //动画停止
        if(null != mAnimation && mAnimation.isRunning()){
            mAnimation.stop();
        }
        //失败视图显示
        sys_loading_dialog_fail.setVisibility(View.VISIBLE);
        //设置失败事件监听
        sys_loading_dialog_fail.setOnClickListener(listener);
        //设置失败文本
        sys_loading_dialog_fail_tv.setText(failStr);
        //设置加载时图片隐藏
        sys_loading_dialog_img.setVisibility(View.GONE);
        //设置加载时文本隐藏
        sys_loading_dialog_tv.setVisibility(View.GONE);
    }
}
