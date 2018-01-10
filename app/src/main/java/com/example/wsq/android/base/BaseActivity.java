package com.example.wsq.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.android.view.refresh.ClassicsCustomFooter;
import com.example.wsq.android.view.refresh.ClassicsCustomHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.ButterKnife;


/**
 * Created by wsq on 2017/12/11.
 */

public abstract  class BaseActivity extends Activity{


    private LoddingDialog dialog;
    private CustomDefaultDialog defaultDialog;
    private String dialogMessage = "";
    private String dialogTitle = "提示";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(getByLayoutId());
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        init();
    }



    public abstract int getByLayoutId();

    public abstract void init();

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsCustomHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsCustomFooter(context);
            }
        });
    }

    /**
     * 显示加载进度
     */
    public void onShowProgress(){
        if (dialog!= null) {
            dialog = new LoddingDialog(this);
        }
        dialog.show();

    }

    /**
     * 隐藏加载进度
     */
    public void onDimissProgress(){
        if (dialog!= null && dialog.isShowing()){
            dialog.dismiss();;
        }
    }

    /**
     * 显示对话框
     * @param listener
     */
    public void onShowDialog(final OnDialogClickListener listener){
        if (defaultDialog == null){

            CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(TextUtils.isEmpty(dialogMessage) ? "请设置显示内容" : dialogMessage);
            builder.setOkBtn("确定", new OnDialogClickListener() {
                @Override
                public void onClick(CustomDefaultDialog dialog, String result) {
                    if (listener != null) {
                        listener.onClick(dialog, "");
                    }
                    dialog.dismiss();
                }

            });
            builder.setCancelBtn("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            defaultDialog = builder.create();
        }
        defaultDialog.show();
    }


    /**
     * 影藏对话框
     */
    public void onDimissDialog(){

        if (defaultDialog!= null && defaultDialog.isShowing()){
            defaultDialog.dismiss();
        }
    }


    /**
     * 设置对话框显示内容
     * @param message
     */
    public void setDialogMessage(String message){
        this.dialogMessage = message;
    }

    /**
     * 设置对话框标题
     * @param title
     */
    public void setDialogTitle(String title){
        this.dialogTitle = title;
    }

}
