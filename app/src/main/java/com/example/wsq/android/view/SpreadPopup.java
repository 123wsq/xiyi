package com.example.wsq.android.view;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.OnPopupListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.tools.FrameAnimation;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 推广时显示的页面
 * Created by wsq on 2017/12/12.
 */

public class SpreadPopup extends PopupWindow{


    private int[] mImgResIds = new int[]{
            R.drawable.image_red_packet_open_1,
            R.drawable.image_red_packet_open_2,
            R.drawable.image_red_packet_open_3,
            R.drawable.image_red_packet_open_4,
            R.drawable.image_red_packet_open_5,
            R.drawable.image_red_packet_open_6,
            R.drawable.image_red_packet_open_7,
            R.drawable.image_red_packet_open_8,
            R.drawable.image_red_packet_open_9,
            R.drawable.image_red_packet_open_4,
            R.drawable.image_red_packet_open_10,
            R.drawable.image_red_packet_open_11
    };
    public static final int FLAG_TYPE_PACKAGE = 0;
    public static final int FLAG_TYPE_OPEN_PACKAGE = 1;
    private Activity mContext;
    private View popupView;
    private List<String> mData;
    private OnPopupListener onStatePopupListner;

    private String textColor = "#000000";
    private View.OnClickListener onClickListener;
    private ImageView iv_open;
    private ImageView iv_background;
    private TextView tv_integral;
    private TextView tv_ok;
    private  ImageView iv_close;
    private int curType;
    private FrameAnimation animation;
    private int curLen = 5;


    public SpreadPopup(Activity context,int resource,  View.OnClickListener clickListener, OnPopupListener listener, int type){

        this.mContext = context;
        this.onClickListener = clickListener;
        this.popupView = LayoutInflater.from(context).inflate(resource, null);
        this.onStatePopupListner = listener;
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.curType = type;
        iv_close = popupView.findViewById(R.id.iv_close);
        iv_open = popupView.findViewById(R.id.iv_open);
        iv_background = popupView.findViewById(R.id.iv_background);
        tv_integral = popupView.findViewById(R.id.tv_integral);
        tv_ok = popupView.findViewById(R.id.tv_ok);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        iv_open.setOnClickListener(onClickListener);

        initPopup();
        initView(curType);
    }




    public void initPopup(){

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = mContext.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)(w));
        //
        this.setHeight(h);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果

        this.setAnimationStyle(curType==0 ? R.style.spread_PopupAnimation : R.style.spread_red_pack);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {


        if (curType == FLAG_TYPE_OPEN_PACKAGE){
            iv_background.setBackgroundResource(R.drawable.image_red_package);
        }

        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }


    public void onShowAnim(){
        animation = new FrameAnimation(iv_open, mImgResIds, 150, true);
//        handler.postDelayed(runnable, 1000);
    }

    public void onHideAnim(){
        iv_open.setVisibility(View.GONE);
    }

    private void initView(int type){

        iv_background.setImageResource(type==0 ? R.drawable.image_spread_red_background : R.drawable.image_red_package);
        iv_open.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        tv_integral.setVisibility(type ==0 ? View.GONE : View.VISIBLE);
        tv_ok.setVisibility(type ==0? View.GONE : View.VISIBLE);
        iv_close.setVisibility(type==0 ? View.VISIBLE : View.GONE);
    }


    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 5){

                if (onStatePopupListner != null){
                    onStatePopupListner.onStatePopupListner(SpreadPopup.this, false);
                }
            }else{
                handler.postDelayed(this, 1000);
            }

        }
    };


}
