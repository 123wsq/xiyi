package com.example.wsq.android.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.example.wsq.android.view.CustomDefaultDialog;
import com.luck.picture.lib.dialog.CustomDialog;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class ShowDialog {

    /**
     * 显示dialog
     * @param context
     * @param ok  确认
     * @param cancel  取消
     * @param title 标题
     * @param message 内容
     * @param okClickListener   确认按钮事件
     * @param cancelClickListener  取消按钮事件
     */
    public static void onShowDialog(Context context, String ok, String cancel, String title, String message, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener) {

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(ok) && okClickListener != null){
            builder.setOkBtn(ok, okClickListener);
        }
        if (!TextUtils.isEmpty(cancel) && cancelClickListener != null) {
            builder.setCancelBtn(cancel, cancelClickListener);
        }
        builder.create().show();
    }
}
