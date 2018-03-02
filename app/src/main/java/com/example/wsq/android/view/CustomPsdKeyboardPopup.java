package com.example.wsq.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.OnInputKeyBoardListener;
import com.example.wsq.android.utils.DensityUtil;

/**
 * Created by Administrator on 2018/2/26 0026.
 */

public class CustomPsdKeyboardPopup extends PopupWindow implements TextWatcher {


    private LinearLayout ll_keyboard_delete, ll_popup_close;
    private PasswordInputView et_psdInput;
    private int[] layoutIds={R.id.ll_keyboard_0 , R.id.ll_keyboard_1, R.id.ll_keyboard_2, R.id.ll_keyboard_3, R.id.ll_keyboard_4,
            R.id.ll_keyboard_5, R.id.ll_keyboard_6, R.id.ll_keyboard_7, R.id.ll_keyboard_8, R.id.ll_keyboard_9};
    private LinearLayout[] keycodeLayouts;
    private int [] tv_keycodeId = {R.id.tv_keycode_0, R.id.tv_keycode_1, R.id.tv_keycode_2, R.id.tv_keycode_3, R.id.tv_keycode_4,
            R.id.tv_keycode_5, R.id.tv_keycode_6, R.id.tv_keycode_7, R.id.tv_keycode_8, R.id.tv_keycode_9};
    private TextView[] tv_keycode;

    private Activity mContext;
    private OnInputKeyBoardListener mListener;
    private View popupView;

    public CustomPsdKeyboardPopup(Activity context, OnInputKeyBoardListener listener){
        this.mContext = context;
        this.mListener = listener;

        onInitView();

        initPopup();
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
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = 0;
//
//        if (h / 2 <= DensityUtil.dp2px(mContext, (3* 50) + 90 )) {
//            height = h / 2;
//        } else {
            height = DensityUtil.dp2px(mContext, 356);
//        }

        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.style_pop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

//        this.setOnDismissListener(new PoponDismissListener());

        popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

//					dismiss();

                return false;
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {


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
    public void onInitView(){

        popupView = LayoutInflater.from(mContext).inflate(R.layout.layout_input_psd_view, null, false);

        ll_popup_close = popupView.findViewById(R.id.ll_popup_close);
        et_psdInput = popupView.findViewById(R.id.et_psdInput);
        et_psdInput.setEnabled(false);
        et_psdInput.setClickable(false);
        tv_keycode = new TextView[tv_keycodeId.length];
        for (int i = 0; i < tv_keycodeId.length; i++) {
            tv_keycode[i] = popupView.findViewById(tv_keycodeId[i]);
        }

        keycodeLayouts = new LinearLayout[layoutIds.length];
        for (int i = 0; i < layoutIds.length; i++) {
            keycodeLayouts[i] = popupView.findViewById(layoutIds[i]);

            keycodeLayouts[i].setOnClickListener(new OnKeyBoardListener(i));
        }
        ll_keyboard_delete = popupView.findViewById(R.id.ll_keyboard_delete);

        ll_keyboard_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_psdInput.getText().toString();
                if (str.length() > 0){
                    et_psdInput.setText(str.substring(0, str.length()-1));
                }
            }
        });
        ll_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        et_psdInput.addTextChangedListener(this);
    }

    private  class OnKeyBoardListener implements View.OnClickListener{
        private int keyCode;

        public OnKeyBoardListener(int code){
            this.keyCode = code;
        }

       @Override
       public void onClick(View view) {
            String str = et_psdInput.getText().toString();
           et_psdInput.setText(str+ keyCode);
       }
   }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String str = editable.toString();
        if (str.length()==6){
            if (mListener!=null){
                mListener.onResultListener(str);
            }
        }
    }
}
