package com.example.wsq.android.activity.share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.db.dao.impl.MaterialImpl;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.HtmlContent;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.ToastUtils;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/15.
 */

public class EditWebActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_Details)
    TextView tv_Details;
    @BindView(R.id.wv_WebView)
    WebView wv_WebView;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.iv_add_addachment)
    ImageView iv_add_addachment;
    @BindView(R.id.layout_style_setting)
    LinearLayout layout_style_setting;
    @BindView(R.id.ll_insert_image)
    LinearLayout ll_insert_image;
    @BindView(R.id.ll_insert_addachment)
    LinearLayout ll_insert_addachment;
    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    @BindView(R.id.ll_insert_content)
    LinearLayout ll_insert_content;


    private static final int RESULT_IMAGE = 0x00000012;
    private static final int RESULT_ATTACHMENT = 0x00000013;
    private final int LOAD_ALL_WEB = 0x0000112;  //加载所有
    private final int REQUEST_CODE = 0x0000113;  //初始化
    private final int CREATE_CONTENT = 0x0000001;   //创建内容
    private final int CREATE_ADDACHMENT = 0x0000002;  //创建附件
    private final int INIT_DATA = 0x000123;

    private final int RESET_EDIT = 10101;  //编辑
    private final int RESET_REMOVE = 10102;  //删除
    private final int RESET_INSERT = 10103;  //删除

    private Map<String, WebContentBean> mListRecord;  //操作记录

    private String mContent = "";   //显示内容
    private boolean seetingState = false;
    private LoddingDialog dialog;
    private CustomPopup popup;
    private boolean isEdit = false; //表示是否是编辑
    private String editKey = "";
    private boolean isInsertState = false; //表示是否是插入内容的状态
    private long addTime = -1;
    private long upTime = -1;
    private long insertTime = -1;
    private DbConInter conInter;
    private UserService userService;
    private SharedPreferences shared;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_edit_web;
    }

    @Override
    public void init() {
        tv_title.setText("内容编辑");
        tv_Details.setText("编辑");
        conInter = new MaterialImpl();
        mListRecord = new HashMap<>();
        dialog = new LoddingDialog(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        userService = new UserServiceImpl();

        wv_WebView.setVerticalScrollbarOverlay(true);
        //设置WebView支持JavaScript
        wv_WebView.getSettings().setJavaScriptEnabled(true);

        //添加客户端支持
        wv_WebView.setWebChromeClient(new WebChromeClient());


        onInitPopup();
        onTextChange();


        Message msg = new Message();
        msg.what = INIT_DATA;
        handler.sendMessageDelayed(msg, 1* 1000);
    }



    @OnClick({R.id.iv_back, R.id.tv_Details, R.id.tv_submit, R.id.iv_add_addachment, R.id.ll_insert_image, R.id.ll_insert_addachment})
    public void onClick(View view) {

        try {
            switch (view.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.tv_Details:  //编辑记录


                    Map<String, WebContentBean> map = new HashMap<>();
                    //将附件删除,标题和创建时间
                    Iterator<Map.Entry<String, WebContentBean>> it = mListRecord.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, WebContentBean> entry = it.next();

                        String key = entry.getKey();
                        WebContentBean bean = entry.getValue();
                        if (bean.getType() != ContentType.ADDACHMENT
                                && bean.getType() != ContentType.TITLE
                                && bean.getType() != ContentType.CREATE_TIME) {

                            map.put(key, bean);
                        }
                    }

                    Logger.d(map.size());
                    Intent intent = new Intent(this, EditRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) map);
                    intent.putExtras(bundle);

                    startActivityForResult(intent, REQUEST_CODE);

                    break;
                case R.id.iv_add_addachment:  //显示附件
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                    layout_style_setting.setVisibility(seetingState ? View.GONE : View.VISIBLE);
                    seetingState = seetingState ? false : true;
                    et_content.clearFocus();
                    break;
                case R.id.tv_submit: //开始提交

                    HtmlContent.onAddContent(this, mListRecord, isEdit ? editKey : "",
                            insertTime, et_content.getText().toString(), Constant.CONTENT_COLOR,
                            Constant.CONTENT_SIZE, AuthType.ARTICLES);


                    Message msg = new Message();
                    msg.what = CREATE_CONTENT;
                    handler.sendMessage(msg);
                    et_content.setText("");

                    break;
                case R.id.ll_insert_image:  //插入图片

                    popup.showAtLocation(ll_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                case R.id.ll_insert_addachment: //添加附件
//
                    new LFilePicker()
                            .withActivity(EditWebActivity.this)
                            .withRequestCode(RESULT_ATTACHMENT)
                            .start();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听输入框
     */
    public void onTextChange() {
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


    public void onInitPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.layout_default_popup, null);
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
                switch (position) {
                    case 0:
                        PictureSelector.create(EditWebActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(5)
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

        Message msg = new Message();
        switch (requestCode) {
            case RESULT_IMAGE:  //上传内容图片
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                List<String> list = new ArrayList<>();
                for (LocalMedia media : selectList) {
//
                    list.add(media.getCompressPath());
                }
                onUpLoadFile(getFileList(list), ContentType.CONTENT_IMG);
                break;
            case RESULT_ATTACHMENT:  //上传附件
//
                List<String> list2 = data.getStringArrayListExtra(com.leon.lfilepickerlibrary.utils.Constant.RESULT_INFO);
                onUpLoadFile(getFileList(list2), ContentType.ADDACHMENT);
                break;
            case REQUEST_CODE:   //内容编辑

                switch (resultCode) {
                    case EditRecordActivity.FLAG_EDIT:   //修改
                        msg.obj = data.getStringExtra("key");
                        msg.what = RESET_EDIT;
                        break;
                    case EditRecordActivity.FLAG_BACK:  //返回 删除
                        mListRecord.clear();
                        try {
                            mListRecord.putAll(conInter.selectAll(EditWebActivity.this, AuthType.ARTICLES));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        msg.obj = data.getStringExtra("key");
                        msg.what = LOAD_ALL_WEB;
                        break;
                    case EditRecordActivity.FLAG_INSERT:  //插入内容

                        addTime = data.getLongExtra("time", -1);
                        upTime = data.getLongExtra("upTime", -1);

                        Logger.d("addTime = " + addTime + "===== upTime = " + upTime);
                        msg.what = RESET_INSERT;
                        break;
                        case EditRecordActivity.FLAG_FINISH:
                            finish();
                            break;
                }
                break;


        }
        handler.sendMessage(msg);


    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case INIT_DATA:

                    wv_WebView.loadUrl("file:///android_asset/html/editHtml.html");
                    try {
                        mListRecord.putAll(conInter.selectAll(EditWebActivity.this, AuthType.ARTICLES));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (mListRecord.size() == 0) {
                        HtmlContent.onAddTitle(EditWebActivity.this, mListRecord,
                                getIntent().getStringExtra(WebKeys.TITLE), Constant.TITLE_COLOR, Constant.TITLE_SIZE, AuthType.ARTICLES);
                        HtmlContent.onAddIcon(EditWebActivity.this,
                                getIntent().getStringExtra(WebKeys.ICON), AuthType.ARTICLES);
                        HtmlContent.onAddArticlesType(EditWebActivity.this,
                                getIntent().getIntExtra(WebKeys.ARTICLES, 0)+"", AuthType.ARTICLES);
                        HtmlContent.onAddInteroduction(EditWebActivity.this,
                                getIntent().getStringExtra(WebKeys.INTRODUCTION), AuthType.ARTICLES);
                        HtmlContent.onAddCreateTime(EditWebActivity.this, mListRecord,
                                DateUtil.onDateFormat(DateUtil.DATA_FORMAT_1), Constant.CONTENT_COLOR, Constant.CONTENT_SIZE, AuthType.ARTICLES);
                    }


                    Message mesg = new Message();
                    mesg.what = LOAD_ALL_WEB;
                    handler.sendMessage(mesg);
                    break;
                case LOAD_ALL_WEB:

                    Message message1 = new Message();
                    message1.what = CREATE_CONTENT;
                    handler.sendMessage(message1);

                    Message message2 = new Message();
                    message2.what = CREATE_ADDACHMENT;
                    handler.sendMessageDelayed(message2, 1 * 1000);

                    break;
                case CREATE_CONTENT:
                    try {
                        mContent = HtmlContent.onCreateHtml(mListRecord);
                    } catch (Exception e) {
                        ToastUtils.onToast(EditWebActivity.this, e.getMessage());
                        e.printStackTrace();
                    }
                    Logger.d(mContent);
                    onEditState();
                    wv_WebView.loadUrl("javascript:onRefreshContent('" + mContent + "')");
                    break;
                case CREATE_ADDACHMENT:

                    String addachment = HtmlContent.onCreateAddachment(mListRecord);
                    onEditState();
                    wv_WebView.loadUrl("javascript:onAddAddachment('" + addachment + "')");

                    break;
                case RESET_EDIT:
                    String key = (String) msg.obj;
                    WebContentBean bean = mListRecord.get(key);
                    String content = bean.getContent();
                    et_content.setText(content);
                    isEdit = true;
                    editKey = key;
                    Logger.d("", bean.toString());
                    break;
//                case RESET_REMOVE:
//                    String removeKey = (String) msg.obj;
//                    Logger.d("删除内容的key   = " + removeKey);
//                    if (removeKey.length() > 0) {
//                        String[] keys = removeKey.split(",");
//
//                        for (int i = 0; i < keys.length; i++) {
//                            mListRecord.remove(keys[i]);
//                        }
//                        Message message3 = new Message();
//                        message3.what = CREATE_CONTENT;
//                        handler.sendMessage(message3);
//
//                        Message message4 = new Message();
//                        message4.what = CREATE_ADDACHMENT;
//                        handler.sendMessage(message4);
//                    }
//
//                    break;
                case RESET_INSERT:
                    isInsertState = true;
                    ll_insert_content.setVisibility(View.VISIBLE);

                    if (upTime > 60 * 1000) {
                        insertTime = addTime - (addTime - upTime) / 2;
                    } else {
                        insertTime = addTime - 10 * 1000;
                    }

                    break;
            }

        }
    };


    /**
     * 获取文件列表
     *
     * @param list
     * @return
     */
    public List<Map<String, Object>> getFileList(List<String> list) {

        List<Map<String, Object>> fileList = new ArrayList<>();

        int num = 0;
        for (String str : list) {
            Map<String, Object> map = new HashMap<>();
            num++;
            map.put("fileType", OkhttpUtil.FILE_TYPE_FILE);
            File file = new File(str);
            map.put(ResponseKey.IMGS + num, file);
            fileList.add(map);
        }
        return fileList;
    }


    /**
     * 开始上传文件
     *
     * @param filelist
     * @param type
     */
    public void onUpLoadFile(List<Map<String, Object>> filelist, final ContentType type) {
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.IMG_COUNT, filelist.size() + "");
        userService.onUploadFile(this, param, filelist, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Message msg = new Message();
                if (type == ContentType.ADDACHMENT) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.IMGS);
                    for (Map<String, Object> map : list){

                        HtmlContent.onAddAddachment(EditWebActivity.this, mListRecord,
                                Urls.HOST + Urls.GET_IMAGES + map.get(ResponseKey.IMGS),
                                map.get(ResponseKey.IMGS)+"",AuthType.ARTICLES);
                    }
//

                    msg.what = CREATE_ADDACHMENT;
                    handler.sendMessage(msg);
                } else if (type == ContentType.CONTENT_IMG) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.IMGS);

                    for (Map<String, Object> map : list) {
                        HtmlContent.onAddContentImgs(EditWebActivity.this, mListRecord,
                                Urls.HOST + Urls.GET_IMAGES + map.get(ResponseKey.IMGS),
                                System.currentTimeMillis(), AuthType.ARTICLES);
                    }

                    msg.what = CREATE_CONTENT;
                    handler.sendMessage(msg);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 删除文件
     *
     * @param str
     */
    public void onRemoveFile(String[] str) {

        dialog.show();
        Map<String, String> param = new HashMap<>();

        userService.onRemoveFile(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                dialog.dismiss();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    private void onEditState() {
        isEdit = false;
        isInsertState = false;
        insertTime = -1;
        upTime = -1;
        addTime = -1;
        ll_insert_content.setVisibility(View.GONE);
    }


}
