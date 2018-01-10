package com.example.wsq.android.view.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.utils.DensityUtil;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.pathview.PathsView;

/**
 * Created by wsq on 2018/1/9.
 */

public class ClassicsCustomFooter extends LinearLayout implements RefreshFooter{

    private TextView mHeaderText;//标题文本
    private PathsView mArrowView;//下拉箭头

    public ClassicsCustomFooter(Context context) {
        super(context);
        initView(context);
    }
    public ClassicsCustomFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }
    public ClassicsCustomFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }
    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mHeaderText = new TextView(context);
        mArrowView = new PathsView(context);
//        mProgressView
//        mProgressView.setImageDrawable(mProgressDrawable);
        mArrowView.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z");

        addView(mArrowView, DensityUtil.dp2px(context,10), DensityUtil.dp2px(context, 20));
        addView(new View(context), DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 20));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(context, 60));
    }


    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int headHeight, int extendHeight) {
//        mProgressDrawable.start();//开始动画
    }
    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
//        mProgressDrawable.stop();//停止动画
        if (success){
            mHeaderText.setText("加载完成");
        } else {
            mHeaderText.setText("加载失败");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullToUpLoad:
                mHeaderText.setText("别往上拽了,('∧')没有了");
                mArrowView.setVisibility(VISIBLE);//显示下拉箭头
                mArrowView.animate().rotation(180);//还原箭头方向
                break;
            case Loading:
                mHeaderText.setText("正在拼命加载中...");
                mArrowView.setVisibility(GONE);//隐藏箭头
                break;
            case ReleaseToLoad:
                mHeaderText.setText("松开立即加载更多");
                mArrowView.animate().rotation(0);//显示箭头改为朝上
                break;
        }
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {

    }

    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }
}