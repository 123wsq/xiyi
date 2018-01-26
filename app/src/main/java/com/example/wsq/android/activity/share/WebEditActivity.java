package com.example.wsq.android.activity.share;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.AttachmentAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.ArticleBean;
import com.example.wsq.android.bean.ArticleType;
import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.FileType;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.db.dao.DbKeys;
import com.example.wsq.android.db.dao.impl.MaterialImpl;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnAttachmentDeleteListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.gx.richtextlibrary.OnRemoveImageChangeListener;
import com.gx.richtextlibrary.RichEditText;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/24.
 */

public class WebEditActivity extends BaseActivity {


    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    @BindView(R.id.edit_richtext)
    RichEditText edit_richtext;
    @BindView(R.id.tv_Details)
    TextView tv_Details;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;


    private static final int RESULT_IMAGE = 0x00000012;
    private static final int RESULT_ATTACHMENT = 0x00000013;
    private CustomPopup popup;
    private LoddingDialog dialog;
    private SharedPreferences shared;
    private UserService userService;
    private DbConInter conInter;
    private AttachmentAdapter mAdapter;
    private List<Map<String, Object>> mAttachMentData;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_web_edit;
    }

    @Override
    public void init() {
        dialog = new LoddingDialog(this);
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        tv_Details.setText("保存");
        tv_title.setText("内容编辑");
        mAttachMentData = new ArrayList<>();
        conInter = new MaterialImpl();
        onInitPopup();

        edit_richtext.onRemoveImageListener(new OnRemoveImageChangeListener() {
            @Override
            public void onRemoveImageListener(String url) {
                onRemoveFile(url, ContentType.CONTENT_IMG);
            }
        });

        List<ArticleBean> list = conInter.selectAll(this, AuthType.ARTICLES);
        if (list.size() > 0) {
            ArticleBean bean = list.get(0);
            //显示内容
            edit_richtext.showContent(edit_richtext, bean.getContent());
            //显示附件
            List<Map<String, Object>> lists = conInter.selectAllAttachment(WebEditActivity.this, AuthType.ARTICLES);
            if (lists.size() != 0) {
                mAttachMentData.addAll(lists);
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
            }
        } else {
            ArticleBean bean = new ArticleBean();
            bean.setTitle(getIntent().getStringExtra(WebKeys.TITLE));
            bean.setIcon(getIntent().getStringExtra(WebKeys.ICON));
            bean.setType(ArticleType.getType(getIntent().getIntExtra(WebKeys.ARTICLES, 0)));
            bean.setDescription(getIntent().getStringExtra(WebKeys.INTRODUCTION));
            bean.setAuthType(AuthType.ARTICLES);
            conInter.insertData(WebEditActivity.this, bean);
        }


        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(this, R.color.color_line)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new AttachmentAdapter(this, mAttachMentData, new OnAttachmentDeleteListener() {
            @Override
            public void onDeleteAttachmentListener(int position, Map<String, Object> map) {

                onRemoveFile(map.get(ResponseKey.PATH) + "", ContentType.ADDACHMENT);
            }
        });

        rv_RecyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.iv_back, R.id.iv_insertImage, R.id.iv_insert_attachment, R.id.tv_Details})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (conInter != null) {
                    conInter.updateData(WebEditActivity.this, DbKeys.CONTENT,
                            edit_richtext.buildHtml(edit_richtext), AuthType.ARTICLES);
                }
                break;
            case R.id.iv_insertImage:  //插入图片
                popup.showAtLocation(ll_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.iv_insert_attachment:  //插入附件
                new LFilePicker()
                        .withActivity(WebEditActivity.this)
                        .withRequestCode(RESULT_ATTACHMENT)
                        .start();
                break;
            case R.id.tv_Details:  //保存

                onCreateAlertDialog();
                break;
        }
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
                        PictureSelector.create(WebEditActivity.this)
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
                        PictureSelector.create(WebEditActivity.this)
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

        switch (requestCode) {
            case RESULT_IMAGE:  //上传内容图片
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                List<String> list = new ArrayList<>();

                for (LocalMedia media : selectList) {
                    list.add(media.getCompressPath());

                }
                Logger.d("选择的图片" + list);
                onUpLoadFile(getFileList(list), ContentType.CONTENT_IMG);
                break;
            case RESULT_ATTACHMENT:
                List<String> list2 = data.getStringArrayListExtra(com.leon.lfilepickerlibrary.utils.Constant.RESULT_INFO);
                onUpLoadFile(getFileList(list2), ContentType.ADDACHMENT);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (conInter != null) {
                conInter.updateData(WebEditActivity.this, DbKeys.CONTENT,
                        edit_richtext.buildHtml(edit_richtext), AuthType.ARTICLES);
            }
        }


        return super.onKeyDown(keyCode, event);

    }

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

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.IMGS);
                updateAttachment(list, 0, type);

                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
            }
        });
    }


    /**
     * @param list
     * @param flag 0 表示上传  1 表示删除
     * @param type
     */
    public void updateAttachment(List<Map<String, Object>> list, int flag, ContentType type) {


        JSONArray jsona = new JSONArray();
        for (Map<String, Object> map : list) {
            String imageName = map.get(ResponseKey.IMGS) + "";
            if (type == ContentType.CONTENT_IMG) {
                if (flag == 0) {
                    edit_richtext.insertImage(Urls.HOST + Urls.GET_IMAGES + imageName);
                } else if (flag == 1) {
                    //更新本地数据内容
                    conInter.updateData(WebEditActivity.this, DbKeys.CONTENT,
                            edit_richtext.buildHtml(edit_richtext), AuthType.ARTICLES);
                }
            } else if (type == ContentType.ADDACHMENT) {
                if (flag == 0) {
                    conInter.insertAttachmentData(WebEditActivity.this, imageName,
                            Urls.HOST + Urls.GET_IMAGES + imageName, AuthType.ARTICLES);
                } else if (flag == 1) {
                    conInter.removeAttachment(WebEditActivity.this, imageName, AuthType.ARTICLES);
                }
            }
        }


        //查询所有的附件
        List<Map<String, Object>> mapList = conInter.selectAllAttachment(WebEditActivity.this, AuthType.ARTICLES);
        if (mapList.size() != 0) {
            mAttachMentData.clear();
            mAttachMentData.addAll(mapList);
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 删除文件
     *
     * @param url
     */
    public void onRemoveFile(String url, final ContentType type) {

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.IMG_COUNT, "1");

        int num = url.lastIndexOf("/");
        param.put(ResponseKey.IMGS + 1, url.substring(num + 1));

        userService.onRemoveFile(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.IMGS);

                updateAttachment(list, 1, type);

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

    /**
     * 开始创建资料
     */
    public void onCreateWeb() {

        dialog.show();

        //取出所有的内容
        List<ArticleBean> map = conInter.selectAll(WebEditActivity.this, AuthType.ARTICLES);
        ArticleBean bean = map.get(0);

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.TITLE, bean.getTitle());
        param.put(ResponseKey.CID, bean.getType().getIndex() + "");
//        param.put(ResponseKey.THUMB, map.get(WebKeys.ICON).getContent());
        param.put(ResponseKey.CAT, bean.getAuthType().getIndex() + "");
        param.put(ResponseKey.DES, bean.getDescription());

        List<Map<String, Object>> attaList = conInter.selectAllAttachment(WebEditActivity.this, AuthType.ARTICLES);
        String adda = "";
        for (Map<String, Object> attaMap : attaList){

            adda +=onAddachmentHtml(attaMap.get(DbKeys.FILE_PATH)+"", attaMap.get(DbKeys.FILE_NAME)+"");
        }


        param.put(ResponseKey.CONTENT, edit_richtext.buildHtml(edit_richtext) + "</br> <div style=\"margin-top: 20px; \">" + adda + "</div>");

        List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> images = new HashMap<>();
        images.put("fileType", OkhttpUtil.FILE_TYPE_IMAGE);
        File file = new File(bean.getIcon());
        Logger.d("icon的本地路径   " + bean.getIcon());
        images.put(ResponseKey.THUMB, file);
        fileList.add(images);

        userService.onCreateShare(this, param, fileList, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                conInter.removeData(WebEditActivity.this, AuthType.ARTICLES);
                conInter.onClearAttachment(WebEditActivity.this, AuthType.ARTICLES);
                if (dialog.isShowing()) dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()) dialog.dismiss();
            }
        });


    }


    public void onCreateAlertDialog() {
        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("提交之后将不再支持修改，您确认已经完成编辑");
        builder.setOkBtn("开始提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    onCreateWeb();
                } catch (Exception e) {
                    if (dialog.isShowing()) dialog.dismiss();
                    e.printStackTrace();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setCancelBtn("点错啦", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }


    public String onAddachmentHtml(String path, String fileName) {
        String iconPath = "http://xiyicontrol.com/vendor/ueditor/dialogs/attachment/fileTypeImages/";
        String icon = "";
        if (fileName.toLowerCase().endsWith(FileType.DOC.getName())
                || fileName.toLowerCase().endsWith(FileType.DOCX.getName())
                || fileName.toLowerCase().endsWith(FileType.PSD.getName())) {
            icon = iconPath + "icon_doc.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.PDF.getName())) {
            icon = iconPath + "icon_pdf.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.EXCEL.getName())) {
            icon = iconPath + "icon_xls.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.PNG.getName())
                || fileName.toLowerCase().endsWith(FileType.PNG.getName())) {
            icon = iconPath + "icon_jpg.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.TXT.getName())) {
            icon = iconPath + "icon_txt.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.EXE.getName())) {
            icon = iconPath + "icon_exe.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.MP3.getName())) {
            icon = iconPath + "icon_mp3.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.MP4.getName())) {
            icon = iconPath + "icon_mv.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.RAR.getName())
                || fileName.toLowerCase().endsWith(FileType.RAR.getName())) {
            icon = iconPath + "icon_rar.gif";
        } else if (fileName.toLowerCase().endsWith(FileType.PPT.getName())) {
            icon = iconPath + "icon_ppt.gif";
        } else {
            icon = iconPath + "icon_default.png";
        }

        String mContent = "<div><img src=\"" + icon + "\" style=\"width: 20px; height: 20px;\"><a href=\"" + path + "\" style=\"margin-left: 10px\">" + fileName + "</a></div>";
        return mContent;
    }
}
