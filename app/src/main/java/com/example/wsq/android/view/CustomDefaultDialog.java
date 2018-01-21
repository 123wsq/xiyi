package com.example.wsq.android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.example.wsq.android.inter.OnDialogClickListener;


/**
 * Created by wsq on 2016/5/6.
 */
public class CustomDefaultDialog extends Dialog {

    public CustomDefaultDialog(Context context) {
        super(context);
    }

    public CustomDefaultDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        /*按钮坚挺事件*/
        private OnClickListener okListener, cancelListener;
        private OnDialogClickListener okListenerInput;

        private String ok, cancel, message;
        private boolean isInput = false;
        private String inputHint;
        private String mTitleColor = "#000000";
        private boolean isCloseDiaglog = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setIsShowInput(boolean isInput) {
            this.isInput = isInput;
            return this;
        }

        public Builder setHintInput(String inputHint) {
            this.inputHint = inputHint;
            return this;
        }

        public Builder setTitleColor(String titleColor) {
            this.mTitleColor = titleColor;
            return this;
        }

        public Builder setShowCloseDialog(boolean isShow) {
            this.isCloseDiaglog = isShow;
            return this;
        }

        public Builder setOkBtn(String okStr, OnDialogClickListener listener) {
            this.ok = okStr;
            this.okListenerInput = listener;
            return this;
        }

        public Builder setOkBtn(String okStr, OnClickListener listener) {
            this.ok = okStr;
            this.okListener = listener;
            return this;
        }

        public Builder setCancelBtn(String cancelStr, OnClickListener listener) {
            this.cancel = cancelStr;
            this.cancelListener = listener;
            return this;
        }

        public CustomDefaultDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDefaultDialog dialog = new CustomDefaultDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.layout_default_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            //设置标题栏
            TextView tv_title = (TextView) layout.findViewById(R.id.title);
            tv_title.setText(title);
            tv_title.setTextColor(Color.parseColor(mTitleColor));
            tv_title.getPaint().setFakeBoldText(true);

            //右上角关闭按钮
            ImageView iv_close = layout.findViewById(R.id.iv_close);
            iv_close.setVisibility(isCloseDiaglog ? View.VISIBLE : View.GONE);

            //显示的内容
            TextView dialog_message = (TextView) layout.findViewById(R.id.dialog_message);
            dialog_message.setText(message);
            dialog_message.setVisibility(isInput ? View.GONE : View.VISIBLE);

            //显示输入框
            final EditText tv_inputMessage = layout.findViewById(R.id.tv_inputMessage);
            tv_inputMessage.setVisibility(isInput ? View.VISIBLE : View.GONE);
            tv_inputMessage.setHint(isInput ? inputHint : "");


            //按钮点击显示
            LinearLayout dialog_btn_layout = (LinearLayout) layout.findViewById(R.id.dialog_btn_layout);

            //按钮中间的线
            View dialig_view = layout.findViewById(R.id.dialig_view);
            // set the content message
            dialog_btn_layout.setVisibility(View.VISIBLE);



            Button dialog_ok = (Button) layout.findViewById(R.id.dialog_ok);
            Button dialog_cancel = (Button) layout.findViewById(R.id.dialog_cancel);
            dialog_ok.setText(ok +"");
            dialog_cancel.setText(cancel +"");

            //关闭按钮事件
            if (isCloseDiaglog){
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }

            /**
             *
             *确认按钮事件
             */
            if (okListener != null){
                dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            //带有输入框的dialog事件
            if (okListenerInput !=null){
                dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        okListenerInput.onClick(dialog, tv_inputMessage.getText().toString() + "");
                    }
                });
            }

            //取消按钮
            if (cancelListener !=null){
                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            //判断显示按钮  1.当两个按钮都存在的情况下    并且不需要返回值的时候
            if (okListener!=null && cancelListener!= null){
                dialog_ok.setBackgroundResource(R.drawable.shape_dialog_left);
                dialog_cancel.setBackgroundResource(R.drawable.shape_dialog_right);
                dialog_cancel.setVisibility(View.VISIBLE);
                dialog_ok.setVisibility(View.VISIBLE);
                dialig_view.setVisibility(View.VISIBLE);
            }
            // 2 当两个按钮都存在  并且需要返回值的情况
            if (okListenerInput !=null && cancelListener!= null){
                dialog_ok.setBackgroundResource(R.drawable.shape_dialog_left);
                dialog_cancel.setBackgroundResource(R.drawable.shape_dialog_right);
                dialog_cancel.setVisibility(View.VISIBLE);
                dialog_ok.setVisibility(View.VISIBLE);
                dialig_view.setVisibility(View.VISIBLE);
            }

            /**
             * 只需要一个按钮的情况
             * 1 只需要确定按钮
             */

             if ((okListener != null || okListenerInput != null) &&  cancelListener == null) {
                dialog_ok.setBackgroundResource(R.drawable.shape_dialog_buttom);
                dialog_ok.setVisibility(View.VISIBLE);
                dialog_cancel.setVisibility(View.GONE);
                dialig_view.setVisibility(View.GONE);
             }
            //只需要取消按钮
            if ((okListener == null && okListenerInput == null) &&  cancelListener != null) {
                dialog_cancel.setBackgroundResource(R.drawable.shape_dialog_buttom);
                dialog_cancel.setVisibility(View.VISIBLE);
                dialog_ok.setVisibility(View.GONE);
                dialig_view.setVisibility(View.GONE);
            }



            Window dialogWindow = dialog.getWindow();
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (widthPixels * 0.8); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            dialog.setContentView(layout);

            return dialog;
        }


    }

}
