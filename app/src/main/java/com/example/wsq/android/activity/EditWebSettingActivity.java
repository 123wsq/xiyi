package com.example.wsq.android.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.utils.BitmapUtils;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.view.CustomPopup;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/15.
 */

public class EditWebSettingActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_web_title) EditText tv_web_title;
    @BindView(R.id.tv_web_info) EditText tv_web_info;
    @BindView(R.id.tv_info_num) TextView tv_info_num;
    @BindView(R.id.tv_icon_name) TextView tv_icon_name;
    @BindView(R.id.ll_upload_icon) LinearLayout ll_upload_icon;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.iv_icon) ImageView iv_icon;


    private CustomPopup popup;
    public static final int RESULT_IMAGE = 0x00000012;
    private String iconPath;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_web_setting;
    }

    @Override
    public void init() {

        tv_title.setText("创建资料");

        onTextChange();
    }

    @OnClick({R.id.iv_back, R.id.ll_upload_icon, R.id.tv_create_web})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
            finish();
            break;
            case R.id.ll_upload_icon:
                View v = LayoutInflater.from(this).inflate(R.layout.layout_default_popup,null);
                List<String> list = new ArrayList<>();
                list.add("相册");
                list.add("拍照");
                popup = new CustomPopup(this, v, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {
                        switch (position){
                            case 0:
                                PictureSelector.create(EditWebSettingActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(1)
                                        .isZoomAnim(true)
                                        .imageSpanCount(3)
                                        .isCamera(false)
                                        .previewImage(true)
                                        .compress(true)// 是否压缩 true or false
                                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                                        .forResult(RESULT_IMAGE);
                                break;
                            case 1:
                                PictureSelector.create(EditWebSettingActivity.this)
                                        .openCamera(PictureMimeType.ofImage())
                                        .imageFormat(PictureMimeType.PNG)
                                        .compress(true)// 是否压缩 true or false
                                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                                        .forResult(RESULT_IMAGE);

                                break;
                        }
                        popup.dismiss();
                    }
                });

                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_create_web:
                    onStartCreate();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case RESULT_IMAGE:
                    // 图片选择结果回调
                    iconPath = selectList.get(0).getCompressPath();
                    iv_icon.setImageBitmap(BitmapUtils.getLocalImage(iconPath));
                    break;

            }
        }
    }


    public void onStartCreate(){

        if (!onValidate()){
            return;
        }

        Map<String, Object> map = new HashMap<>();

        map.put(WebKeys.TITLE, tv_web_title.getText().toString());
        map.put(WebKeys.INTRODUCTION, tv_web_info.getText().toString());
        map.put(WebKeys.ICON, "");
        IntentFormat.startActivity(this, EditWebActivity.class, map);
        finish();

    }



    public void onTextChange(){

        tv_web_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Editable editable = tv_web_info.getText();
                int num = tv_web_info.getText().length();
                if (num > 50){

                    tv_info_num.setText("50 / 50");
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0,50);
                    tv_web_info.setText(newStr);
                    editable = tv_web_info.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex > newLen)
                    {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }else{
                    tv_info_num.setText(num+" / 50");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }


    public  boolean onValidate(){
        String web_title = tv_web_title.getText().toString();

        if (TextUtils.isEmpty(web_title)){
            ToastUtis.onToast(this, "标题不能为空");
            return false;
        }

        String web_info = tv_web_info.getText().toString();
        if (TextUtils.isEmpty(web_info)){
            ToastUtis.onToast(this, "资料简介不能为空");
            return false;
        }

        return true;
    }
}
