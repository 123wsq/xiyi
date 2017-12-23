package com.example.wsq.android.view;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;

import com.example.wsq.android.R;


public class LoddingDialog extends Dialog {
	private String str="加载中...";
	SysLoading loadingView;
	public LoddingDialog(Context context, int theme) {
		super(context, R.style.FullScreenDialogAct);
		init(context);
	}

	public LoddingDialog(Context context) {
		super(context, R.style.FullScreenDialogAct);
		init(context);
	}
	private void init(Context context){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		loadingView  = new SysLoading(context);
		setContentView(loadingView);

	}

	@Override
	public void show() {
		super.show();
		if (loadingView!= null){
			loadingView.showAnim();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (loadingView!= null){
			loadingView.stopAnim();
		}
	}

	public void setText(String str){
		if (!TextUtils.isEmpty(str)){
			loadingView.setText(str);
		}
	}
}
