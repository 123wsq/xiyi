package com.example.wsq.android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.inter.OnDialogClickListener;


/**
 * Created by wsq on 2016/5/6.
 */
public class CustomDefaultDialog extends Dialog{

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
        private OnClickListener  okListener, cancelListener;
        private OnDialogClickListener okListener1;


        private String ok, cancel, message;
        private boolean isInput = false;
        private String inputHint;


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


        public Builder setOkBtn(String okStr, OnDialogClickListener listener){
                this.ok = okStr;
                this.okListener1 = listener;
            return this;
        }
        public Builder setOkBtn(String okStr, OnClickListener listener){
            this.ok = okStr;
            this.okListener = listener;
            return this;
        }

        public Builder setCancelBtn(String cancelStr, OnClickListener listener){
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
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);

            LinearLayout dialog_btn_layout = (LinearLayout) layout.findViewById(R.id.dialog_btn_layout);
            TextView dialog_message = (TextView) layout.findViewById(R.id.dialog_message);
            final EditText tv_inputMessage = layout.findViewById(R.id.tv_inputMessage);
            View dialig_view = layout.findViewById(R.id.dialig_view);
            // set the content message
                dialog_btn_layout.setVisibility(View.VISIBLE);
                dialog_message.setVisibility(View.VISIBLE);
                Button dialog_ok = (Button)layout.findViewById(R.id.dialog_ok);
                Button dialog_cancel = (Button)layout.findViewById(R.id.dialog_cancel);
                dialog_message.setText(message);
                dialog_ok.setText(ok);
                if (okListener1!= null || okListener!= null){
                    dialog_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            if (okListener1 != null) {
                                okListener1.onClick(dialog, tv_inputMessage.getText().toString() + "");
                            }
                            if (okListener != null) {
                                okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }
                        }
                    });
                }
                dialog_cancel.setText(cancel);
                if (cancelListener != null){
                    dialog_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
                if ((okListener ==null && okListener1==null) && cancelListener!=null){
                    dialog_cancel.setBackgroundResource(R.drawable.shape_dialog_buttom);
                    dialog_ok.setVisibility(View.GONE);
                    dialig_view.setVisibility(View.GONE);
                }else if (okListener !=null ||okListener1!=null && cancelListener==null){
                    dialog_ok.setBackgroundResource(R.drawable.shape_dialog_buttom);
                    dialog_cancel.setVisibility(View.GONE);
                    dialig_view.setVisibility(View.GONE);
                }
                if (okListener != null && cancelListener != null){
                    dialog_cancel.setVisibility(View.VISIBLE);
                    dialog_ok.setVisibility(View.VISIBLE);
                }

                if (isInput){
                    tv_inputMessage.setVisibility(View.VISIBLE);
                    tv_inputMessage.setHint(inputHint);
                    dialog_message.setVisibility(View.GONE);
                }else {
                    tv_inputMessage.setVisibility(View.GONE);
                    dialog_message.setVisibility(View.VISIBLE);
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
