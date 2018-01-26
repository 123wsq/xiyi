package com.example.wsq.android.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;

/**
 * Created by wsq on 2018/1/15.
 */

public class CustomStartOrderDialog extends Dialog{

    public CustomStartOrderDialog(@NonNull Context context) {
        super(context);
    }

    public CustomStartOrderDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String mUrl;

        private View.OnClickListener onClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnClickListener(View.OnClickListener listener){

            this.onClickListener = listener;
            return this;
        }

        @SuppressLint("NewApi")
        public CustomStartOrderDialog create(){

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomStartOrderDialog dialog = new CustomStartOrderDialog(context, R.style.orderstyle);
            View layout = inflater.inflate(R.layout.layout_start_order, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageView iv_close = layout.findViewById(R.id.iv_close);


            TextView tv_start_order = layout.findViewById(R.id.tv_start_order);



            if (onClickListener != null) {
                tv_start_order.setOnClickListener(onClickListener);
            }
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            Window dialogWindow = dialog.getWindow();
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            float heightPixels = context.getResources().getDisplayMetrics().heightPixels;
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//             p.height = (int) (heightPixels * 0.7); // 高度设置为屏幕的0.6
            p.width = (int) (widthPixels * 0.8); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
