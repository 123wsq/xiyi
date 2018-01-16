package com.example.wsq.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.tools.HtmlContent;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.ToastUtils;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/15.
 */

public class EditWebActivity extends BaseActivity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_Details) TextView tv_Details;
    @BindView(R.id.wv_WebView) WebView wv_WebView;
    @BindView(R.id.et_content) EditText et_content;
    @BindView(R.id.tv_submit) TextView tv_submit;
    @BindView(R.id.iv_add_addachment) ImageView iv_add_addachment;
    @BindView(R.id.layout_style_setting) LinearLayout layout_style_setting;
    @BindView(R.id.ll_insert_image) LinearLayout ll_insert_image;
    @BindView(R.id.ll_insert_addachment) LinearLayout ll_insert_addachment;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;

    private final String [] images= {"http://e.hiphotos.baidu.com/image/pic/item/500fd9f9d72a6059099ccd5a2334349b023bbae5.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/58ee3d6d55fbb2fb4a944f8b444a20a44723dcef.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/a044ad345982b2b713b5ad7d3aadcbef76099b65.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/cdbf6c81800a19d8765f664b38fa828ba61e4624.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/7e3e6709c93d70cfe6a54843f3dcd100baa12b25.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/9213b07eca8065388cef4c5d9cdda144ad348273.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa09022da0bf8f8c5494ee7b16.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/eaf81a4c510fd9f9b27cbe272e2dd42a2834a411.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/71cf3bc79f3df8dc086b64dfc611728b4710282e.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/d50735fae6cd7b896506d1cd042442a7d9330e12.jpg"};


    private static final int RESULT_IMAGE = 0x00000012;
    private final int CREATE_CONTENT = 0x0000001;   //创建内容
    private final int CREATE_ADDACHMENT = 0x0000002;  //创建附件

    private Map<String, WebContentBean> mListRecord;  //操作记录

    private String mContent = "";   //显示内容
    private boolean seetingState = false;
    private LoddingDialog dialog;
    private String[] colors = {"#0000FF", "#00FF00", "#FF0000","#000000"};
    private CustomPopup popup;



    @Override
    public int getByLayoutId() {
        return R.layout.layout_edit_web;
    }

    @Override
    public void init() {
        tv_title.setText("内容编辑");
        tv_Details.setText("编辑记录");
        mListRecord = new HashMap<>();
        dialog = new LoddingDialog(this);

        wv_WebView.setVerticalScrollbarOverlay(true);
        //设置WebView支持JavaScript
        wv_WebView.getSettings().setJavaScriptEnabled(true);
        wv_WebView.loadUrl("file:///android_asset/html/editHtml.html");
        //添加客户端支持
        wv_WebView.setWebChromeClient(new WebChromeClient());

        HtmlContent.onAddTitle(mListRecord, getIntent().getStringExtra(WebKeys.TITLE), "#00FF00", 18);
        HtmlContent.onAddCreateTime(mListRecord, DateUtil.onDateFormat(DateUtil.DATA_FORMAT_1), "#FF0000", 14);



        onInitPopup();
        onTextChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendMessage(new Message());
    }

    @OnClick({R.id.iv_back, R.id.tv_Details, R.id.tv_submit, R.id.iv_add_addachment, R.id.ll_insert_image, R.id.ll_insert_addachment})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_Details:  //编辑记录

                break;
            case R.id.iv_add_addachment:  //显示附件
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow( getWindow().getDecorView().getWindowToken(), 0);

                layout_style_setting.setVisibility(seetingState ? View.GONE : View.VISIBLE);
                seetingState = seetingState ? false :true;
                et_content.clearFocus();
                break;
            case R.id.tv_submit: //开始提交
                Random random = new Random();
                int num = random.nextInt(colors.length-1);
                HtmlContent.onAddContent(mListRecord, et_content.getText().toString(), colors[num], 14);
                Message msg = new Message();
                msg.what = CREATE_CONTENT;
                handler.sendMessage(msg);
                et_content.setText("");
                break;
            case R.id.ll_insert_image:  //插入图片

                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_insert_addachment: //添加附件
                HtmlContent.onAddAddachment(mListRecord,"http://xiyicontrol.com/storage//uploads/file/2017/11/20/369055d4ff33956a7d68511483ae5496.pdf",
                        "369055d4ff33956a7d68511483ae5496.pdf");
                Message msg1 = new Message();
                msg1.what = CREATE_ADDACHMENT;
                handler.sendMessage(msg1);
                break;
        }
    }

    /**
     * 监听输入框
     */
    public void onTextChange(){
        et_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                seetingState = !b;
                if (b) {
                    layout_style_setting.setVisibility(View.GONE);
                }
            }
        });
    }


    public void onInitPopup(){
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
                        PictureSelector.create(EditWebActivity.this)
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
                        PictureSelector.create(EditWebActivity.this)
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case RESULT_IMAGE:
                    // 图片选择结果回调
                    Random random = new Random();
                    int num = random.nextInt(images.length-1);
                    HtmlContent.onAddContentImgs(mListRecord, images[num]);
                    Message msg = new Message();
                    msg.what = CREATE_CONTENT;
                    handler.sendMessage(msg);

                    break;

            }
        }

    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case CREATE_CONTENT:
                    try {
                        mContent = HtmlContent.onCreateHtml(mListRecord);
                    } catch (Exception e) {
                        ToastUtils.onToast(EditWebActivity.this, e.getMessage());
                        e.printStackTrace();
                    }
                    Logger.d(mContent);
                    wv_WebView.loadUrl("javascript:onRefreshContent('" + mContent + "')");
                    break;
                case CREATE_ADDACHMENT:

                    String addachment = HtmlContent.onCreateAddachment(mListRecord);

                    wv_WebView.loadUrl("javascript:onAddAddachment('" + addachment + "')");

                    break;
            }

        }
    };
}
