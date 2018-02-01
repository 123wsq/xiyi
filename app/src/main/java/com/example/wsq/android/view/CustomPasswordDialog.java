package com.example.wsq.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.inter.OnPasswordClickListener;
import com.example.wsq.android.tools.SoftKeyboardStateHelper;
import com.example.wsq.android.utils.DensityUtil;
import com.orhanobut.logger.Logger;


/**
 * Created by wsq on 2016/5/6.
 */
public class CustomPasswordDialog extends Dialog {

    public CustomPasswordDialog(Context context) {
        super(context);
    }

    public CustomPasswordDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        /*按钮坚挺事件*/
        private OnPasswordClickListener okListenerInput;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setOnClickListener(OnPasswordClickListener listener){
            this.okListenerInput = listener;
            return this;
        }




        public CustomPasswordDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomPasswordDialog dialog = new CustomPasswordDialog(context, R.style.style_pop);
            View layout = inflater.inflate(R.layout.layout_password_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            final PasswordInputView et_psdInput = layout.findViewById(R.id.et_psdInput);

            showSoftInputFromWindow((Activity) context, et_psdInput);
//            et_psdInput.setOnKeyListener(new MyOnKeyListener(dialog));

            SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(et_psdInput);
            softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
                @Override
                public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                    Logger.d("键盘打开");
                }

                @Override
                public void onSoftKeyboardClosed() {
                        Logger.d("键盘关闭");
                }
            });

            et_psdInput.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String str = editable.toString();
                    if (str.length() == Constant.PASSWORD_COUNT){
//                        onPassword(str);
                        if (okListenerInput != null){
                            okListenerInput.onClick(dialog, str);
                        }
                    }
                }
            });

            et_psdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    Logger.d("焦点    "+hasFocus);
                }
            });



            Window dialogWindow = dialog.getWindow();
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (widthPixels); // 宽度设置为屏幕的0.65
            p.height = (int) DensityUtil.dp2px(context, 150);
//            p.gravity = Gravity.BOTTOM;
            dialogWindow.setAttributes(p);
            dialog.setContentView(layout);

            return dialog;
        }




    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private static class MyOnKeyListener implements View.OnKeyListener{
        private CustomPasswordDialog dialog;
        public MyOnKeyListener(CustomPasswordDialog dialog){
            this.dialog = dialog;
        }
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            Logger.d("键盘响应事件   " + keyCode );
            if( keyCode == KeyEvent.ACTION_DOWN){
                if (dialog.isShowing())dialog.dismiss();
                return true;
            }
            return false;
        }
    }

}
