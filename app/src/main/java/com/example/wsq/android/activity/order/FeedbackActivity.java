package com.example.wsq.android.activity.order;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.UploadAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.utils.BitmapUtils;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.ImageUtil;
import com.example.wsq.android.utils.ScreenUtils;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.utils.ValidateDataFormat;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


/**
 * 反馈报告界面
 * Created by wsq on 2017/12/15.
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_away_back)
    TextView tv_away_back;
    @BindView(R.id.et_server_loc)
    TextView et_server_loc;
    @BindView(R.id.et_contact)
    TextView et_contact;
    @BindView(R.id.et_contact_tel)
    TextView et_contact_tel;
    @BindView(R.id.et_results_1)
    EditText et_results_1;
    @BindView(R.id.et_results_2)
    EditText et_results_2;
    @BindView(R.id.iv_back)
    LinearLayout iv_back;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.ll_layout)
    LinearLayout ll_layout;
    @BindView(R.id.order_no)
    TextView order_no;


    private OrderTaskService deviceTaskService;
    private List<CameraBean> mData;
    private CustomPopup popup;
    private SharedPreferences shared;

    private static final int RESULT_VIDEO = 0x00000011;
    public static final int RESULT_IMAGE = 0x00000012;

    private int id;
    private Intent intent;
    private static final int REQUEST_CODE = 0x00000011;

    private UploadAdapter mAdapter;
    private LoddingDialog dialog;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_feedback;
    }

    @Override
    public void init(){
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        deviceTaskService = new OrderTaskServiceImpl();
        id = getIntent().getIntExtra(ResponseKey.ID, 0);

        tv_title.setText("反馈报告");
        dialog = new LoddingDialog(this);

        intent = getIntent();

        order_no.setText(intent.getStringExtra(ResponseKey.ORDER_NO));
        et_server_loc.setText(intent.getStringExtra(ResponseKey.DIDIAN));
        et_results_1.setText(intent.getStringExtra(ResponseKey.CONTENT));
        et_results_2.setText(intent.getStringExtra(ResponseKey.YILIU));

        mData = new ArrayList<>();

        CameraBean bean = new CameraBean();
        bean.setType(1);
        bean.setShow(false);
        mData.add(bean);
        mAdapter = new UploadAdapter(this, mData);
        gridview.setAdapter(mAdapter);

       onRegister();
        initPopup();
    }


    public void initPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_default_popup, null);
        List<String> list = new ArrayList<>();
        String[] arrys = Constant.ACTION_CAMERA;
        for (int i = 0; i < arrys.length; i++) {
            list.add(arrys[i]);
        }
        popup = new CustomPopup(this, view, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        }, new PopupItemListener() {
            @Override
            public void onClickItemListener(int position, String result) {
                //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                PictureFileUtils.deleteCacheDirFile(FeedbackActivity.this);
//                FunctionOptions.Builder builder = new FunctionOptions.Builder();
                switch (position) {
                    case 0: //相册

                        PictureSelector.create(FeedbackActivity.this)
                                .openGallery(PictureMimeType.ofImage())
//                                .theme(R.sty)
                                .maxSelectNum(Constant.IMAGE_COUNT - (mData.size() - 1))
                                .isZoomAnim(true)
                                .imageSpanCount(3)
                                .isCamera(false)
                                .previewImage(true)
                                .compress(true)// 是否压缩 true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩
                                .forResult(RESULT_IMAGE);
                        break;
                    case 1: //相机

                        PictureSelector.create(FeedbackActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .imageFormat(PictureMimeType.PNG)
                                .compress(true)// 是否压缩 true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩

                                .forResult(RESULT_IMAGE);

                        break;
                    case 2: //录像

                        PictureSelector.create(FeedbackActivity.this)
                                .openCamera(PictureMimeType.ofVideo())
                                .compress(true)// 是否压缩 true or false
                                .recordVideoSecond(10)//视频秒数录制 默认60s int
                                .forResult(RESULT_VIDEO);

                        break;
                    case 3:  //视频

                        PictureSelector.create(FeedbackActivity.this)
                                .openGallery(PictureMimeType.ofVideo())
                                .maxSelectNum(Constant.IMAGE_COUNT - (mData.size() - 1))
                                .imageSpanCount(3)
                                .isCamera(false)
                                .compress(true)// 是否压缩 true or false
                                .forResult(RESULT_VIDEO);
                        break;

                }
                popup.dismiss();
            }
        });
        popup.setTextColor("#0168D2");
        popup.setTitle("请选择");
    }


    @OnClick({R.id.iv_back, R.id.tv_away_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_away_back:
                if (onValidateParam()) {
                    onSubmit();
                }
                break;
        }
    }


    @OnItemClick({R.id.gridview})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CameraBean bean = mData.get(position);
        if (bean.getType() == 1) {

            onRequestPermission();
//            popup.showAtLocation(ll_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
        } else if (bean.getType() == 2) {
            List<LocalMedia> list = new ArrayList<>();
            //将所有的图片添加到list中
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getType() == 2) {
                    LocalMedia media = new LocalMedia();
                    media.setPath(mData.get(i).getFile_path());
                    list.add(media);
                }
            }
            //计算选中的图片位置
            int num = 0;
            for (int i = 0; i < list.size(); i++) {
                if (bean.getFile_path().equals(list.get(i).getPath())) {
                    num = i;
                }
            }
            PictureSelector.create(FeedbackActivity.this).externalPicturePreview(position, list);
        } else if (bean.getType() == 3) {
            PictureSelector.create(FeedbackActivity.this)
                    .externalPictureVideo(bean.getFile_path());

        }
    }


    /**
     * 提交反馈信息
     */
    public void onSubmit() {

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, id + "");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, "") + "");
        param.put(ResponseKey.DIDIAN, et_server_loc.getText().toString());
        param.put(ResponseKey.LXR, et_contact.getText().toString());
        param.put(ResponseKey.TEL, et_contact_tel.getText().toString());
        param.put(ResponseKey.CONTENT, et_results_1.getText().toString());
        param.put(ResponseKey.YILIU, et_results_2.getText().toString());

        List<Map<String, Object>> listFile = new ArrayList<>();
        int numflag = 1;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getType() != 1) {
                File f = new File(mData.get(i).getFile_path());
                Map<String, Object> map = new HashMap<>();
                map.put(ResponseKey.IMGS + (i + 1), f);
                map.put("fileType", mData.get(i).getType() == 2 ?
                        OkhttpUtil.FILE_TYPE_IMAGE : OkhttpUtil.FILE_TYPE_VIDEO);
                listFile.add(map);
            }
        }


        param.put(ResponseKey.IMG_COUNT, listFile.size() + "");
        deviceTaskService.onSubmitReport(this, param, listFile, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing())dialog.dismiss();
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
                    onSetData(2, selectList);
                    break;
                case RESULT_VIDEO:
                    onSetData(3, selectList);
                    break;
            }
        }

    }

    /**
     * 设置数据
     *
     * @param type 1 表示相册返回  2 表示拍照返回  3 表示录像  4 视频
     * @param list 多媒体
     */
    public void onSetData(int type, List<LocalMedia> list) {

        Logger.d("选择的图片" + list.size());

        if (list.size() == 0) {
            CameraBean bean = new CameraBean();
            bean.setType(1);
            bean.setShow(false);
            bean.setChange(false);
            mData.add(bean);

        } else if (list.size() + (mData.size() - 1) == 3) {
//            mData.clear();
            mData.remove(mData.size() - 1);
            for (int i = 0; i < list.size(); i++) {
                CameraBean bean = new CameraBean();
                String path = "";
                if (list.get(i).isCompressed()) {
                    path = list.get(i).getCompressPath();
                } else {
                   path = list.get(i).getPath();
                }
                if (type ==2){
                    bean.setFile_path(onBitmapCompress(path));
                }else {
                    bean.setFile_path(path);
                }

                bean.setType(type);
                bean.setShow(true);
                mData.add(bean);
            }
        } else {
            mData.remove(mData.size() - 1);
            for (int i = 0; i < list.size(); i++) {
                CameraBean bean = new CameraBean();
                String path = "";
                if (list.get(i).isCompressed()) {
                    path = list.get(i).getCompressPath();
                } else {
                    path = list.get(i).getPath();
                }
                if (type ==2){
                    bean.setFile_path(onBitmapCompress(path));
                }else {
                    bean.setFile_path(path);
                }
                bean.setType(type);
                bean.setShow(true);
                mData.add(bean);
            }
            CameraBean bean = new CameraBean();
            bean.setType(1);
            bean.setShow(false);
            mData.add(bean);
        }

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 图片添加水印和质量压缩
     * @param path
     * @return
     */
    public  String onBitmapCompress(String path){
        //得到该路径下的图片bitmap
        Bitmap bitmap = BitmapUtils.getLocalImage(path);
        Bitmap newBitmap = ImageUtil.drawTextToLeftBottom(FeedbackActivity.this, bitmap,
                new String[]{ shared.getString(Constant.SHARED.LOCATION, ""), DateUtil.onDateFormat(DateUtil.DATA_FORMAT)},
                20, Color.RED, 20, 10);
        //只对图片进行质量压缩
//        Bitmap commBitmap = ImageUtil.compressImage(newBitmap, 200);
        //对图片进行大小 质量压缩
//        Bitmap commBitmap = ImageUtil.zoomImage(newBitmap, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this), 200);
        File file = BitmapUtils.saveImage(newBitmap);
//                        String savePath = BitmapUtils.addBitmapWatermark(DeviceWarrantyActivity.this, path);
        return file.getAbsolutePath();
    }

    /**
     * @param array
     * @param isChange 文件是否发生变化
     */
    public void updateImags(List<String> array, boolean isChange) {

        if (array.size() == 0) {

        } else if (array.size() == 3) {
            mData.clear();
            for (int i = 0; i < array.size(); i++) {
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST + Urls.GET_IMAGES + array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);

                } else {
                    bean.setType(2);
                }
                bean.setChange(isChange);
                bean.setShow(true);
                mData.add(bean);
            }
        } else {
            if (mData.size() != 0) {
                mData.remove(mData.size() - 1);
            }

            for (int i = 0; i < array.size(); i++) {
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST + Urls.GET_IMAGES + array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);
                } else {
                    bean.setType(2);
                }
                bean.setChange(isChange);
                bean.setShow(true);
                mData.add(bean);
            }
            CameraBean bean = new CameraBean();
            bean.setType(1);
            bean.setShow(false);
            bean.setChange(isChange);
            mData.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }


    public void onRegister() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION.IMAGE_DELETE);
        registerReceiver(broadcastReceiver, filter);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.ACTION.IMAGE_DELETE)) {
                onDeleteData(intent.getStringExtra("filePath"));
            }

        }
    };

    /**
     * 删除数据
     *
     * @param path
     */
    public void onDeleteData(String path) {


        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getFile_path().equals(path)) {
                mData.remove(i);
            }
        }

        //检测最后一个是否是点击添加的按钮
        if (mData.size() != 0) {
            CameraBean bean = mData.get(mData.size() - 1);
            if (bean.getType() != 1) {  //如果最后一个不是按钮  则添加一个
                CameraBean b = new CameraBean();
                b.setType(1);
                b.setShow(false);
                b.setChange(false);
                mData.add(b);
            }
        } else {
            CameraBean b = new CameraBean();
            b.setType(1);
            b.setShow(false);
            b.setChange(false);
            mData.add(b);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public boolean onValidateParam() {

        String serverLoc = et_server_loc.getText().toString();
        if (TextUtils.isEmpty(serverLoc)) {
            ToastUtis.onToast("服务地点不能为空");
            return false;
        }
        String contact = et_contact.getText().toString();
        if (TextUtils.isEmpty(contact)) {
            ToastUtis.onToast("现场联系人不能为空");
            return false;
        }
        String contact_tel = et_contact_tel.getText().toString();
        if (TextUtils.isEmpty(contact_tel)) {
            ToastUtis.onToast("现场联系人电话不能为空");
            return false;
        }

        if (!ValidateDataFormat.isMobile(contact_tel)) {
            ToastUtis.onToast("输入的手机号有误");
            return false;
        }
        String serContent = et_results_1.getText().toString();
        if (TextUtils.isEmpty(serContent)) {
            ToastUtis.onToast("现场处理结果不能为空");
            return false;
        }

        String ylContent = et_results_2.getText().toString();
        if (TextUtils.isEmpty(ylContent)) {
            ToastUtis.onToast("遗留问题不能为空");
            return false;
        }



        if (mData!=null && mData.size() <= 1) {
            ToastUtis.onToast("现场资料不能为空");
            return false;
        }

        return true;
    }

    public void onRequestPermission(){//READ_PHONE_STATE
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
//            } else {
//                onValidatePhone();
//            }
//        }
        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "手机权限", R.drawable.permission_ic_phone));
        permissions.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "手机权限", R.drawable.permission_ic_phone));
        HiPermission.create(this).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
                finish();
            }

            @Override
            public void onFinish() {
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//                Logger.d("所有权限申请完成");

            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }
}
