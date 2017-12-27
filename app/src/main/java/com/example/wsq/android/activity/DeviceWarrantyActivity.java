package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.UploadAdapter;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by wsq on 2017/12/13.
 */

public class DeviceWarrantyActivity extends Activity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_tel) TextView tv_tel;
    @BindView(R.id.tv_company) TextView tv_company;
    @BindView(R.id.gridview) GridView gridview;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.et_model) EditText et_model;
    @BindView(R.id.et_num) EditText et_num;
    @BindView(R.id.et_description) EditText et_description;

    private UploadAdapter mAdapter;
    private List<CameraBean> mData;
    private CustomPopup popup;

    private LoddingDialog dialog;
    private SharedPreferences shared;
    private OrderTaskService deviceTaskService;
    private static final int RESULT_VIDEO = 0x00000011;
    public static final int RESULT_IMAGE = 0x00000012;
    public boolean isUpdate = false;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.layout_device_warranty);
        ButterKnife.bind(this);
        init();
        onRegister();
    }


    public void init() {

        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        deviceTaskService = new OrderTaskServiceImpl();


        tv_title.setText("设备维修");


        dialog = new LoddingDialog(this);
        mData = new ArrayList<>();
        CameraBean bean = new CameraBean();
        bean.setType(1);
        bean.setShow(false);

        mData.add(bean);
        mAdapter = new UploadAdapter(this, mData);
        gridview.setAdapter(mAdapter);


        tv_name.setText(shared.getString(Constant.SHARED.NAME, ""));
        tv_tel.setText(shared.getString(Constant.SHARED.TEL, ""));
        tv_company.setText(shared.getString(Constant.SHARED.COMPANY,""));


       initPopup();

       onEdit();

    }

    public void onEdit(){

        intent = getIntent();
        isUpdate = intent.getBooleanExtra(OrderInfoActivity.UPDATE, false);
        if (!isUpdate){
            tv_title.setText("设备维修");
            return;
        }else{
            tv_title.setText("订单修改");
        }

        String imags = intent.getStringExtra(ResponseKey.IMGS);

        // 清空所有数据
        if (!TextUtils.isEmpty(imags)) {
            try {

                JSONArray jsona = new JSONArray(imags);
                for (int i = 0; i < jsona.length(); i++) {
                    List<LocalMedia> list = new ArrayList<>();
                    LocalMedia media = new LocalMedia();
                    media.setCompressPath(Urls.HOST + Urls.GET_IMAGES + jsona.get(i).toString());
                    media.setCompressed(true);

                    list.add(media);
                    if (jsona.get(i).toString().endsWith(".mp4")) {
                        onSetData(3, list);
                    } else {
                        onSetData(2, list);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        et_model.setText(intent.getStringExtra(ResponseKey.XINGHAO));
        et_num.setText(intent.getStringExtra(ResponseKey.BIANHAO));
        et_description.setText(intent.getStringExtra(ResponseKey.DES));
    }


    public void initPopup(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_default_popup,null);
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
                PictureFileUtils.deleteCacheDirFile(DeviceWarrantyActivity.this);
//                FunctionOptions.Builder builder = new FunctionOptions.Builder();
                switch (position){
                    case 0: //相册

                        PictureSelector.create(DeviceWarrantyActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(Constant.IMAGE_COUNT-(mData.size()-1))
                                .isZoomAnim(true)
                                .imageSpanCount(3)
                                .isCamera(false)
                                .previewImage(true)
                                .compress(true)// 是否压缩 true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩
                                .forResult(RESULT_IMAGE);
                        break;
                    case 1: //相机

                        PictureSelector.create(DeviceWarrantyActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .imageFormat(PictureMimeType.PNG)
                                .compress(true)// 是否压缩 true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩

                                .forResult(RESULT_IMAGE);

                        break;
                    case 2: //录像

                        PictureSelector.create(DeviceWarrantyActivity.this)
                                .openCamera(PictureMimeType.ofVideo())
                                .compress(true)// 是否压缩 true or false
                                .recordVideoSecond(10)//视频秒数录制 默认60s int
                                .forResult(RESULT_VIDEO);

                        break;
                    case 3:  //视频

                        PictureSelector.create(DeviceWarrantyActivity.this)
                                .openGallery(PictureMimeType.ofVideo())
                                .maxSelectNum(Constant.IMAGE_COUNT-(mData.size()-1))
                                .imageSpanCount(3)
                                .isCamera(false)
                                .compress(true)// 是否压缩 true or false
                                .forResult(RESULT_VIDEO);
                        break;

                }
                popup.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_repairs})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_repairs:
                //开始报修
                if (isUpdate){

                    onUpdateOrder();
                }else{
                    onSubmitRepairs();
                }

                break;
        }
    }

    @OnItemClick({R.id.gridview})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CameraBean bean = mData.get(position);
        if (bean.getType() == 1){
            popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

        }else if(bean.getType() == 2){
            int num = 0;
            List<LocalMedia> list = new ArrayList<>();
            for (int i = 0; i< mData.size(); i ++){
                if (mData.get(i).getType()==2) {
                    LocalMedia media = new LocalMedia();

                    media.setPath(bean.getFile_path());
                    list.add(media);
                }else{
                    if (i < position){
                        num++;
                    }
                }
            }
            PictureSelector.create(DeviceWarrantyActivity.this).externalPicturePreview(num, list);
        }else if(bean.getType() == 3){

//        PictureSelector.create(DeviceWarrantyActivity.this)
//                .externalPictureVideo(bean.getFile_path());
            Map<String, Object> param = new HashMap<>();
            param.put("URL", bean.getFile_path());
            IntentFormat.startActivity(DeviceWarrantyActivity.this, VideoPlayActivity.class, param);

        }

    }


    /**
     * 验证提交参数
     * @return
     */
    public boolean validate(){
        //验证型号数据是否为空
        String model = et_model.getText().toString();
        if (null == model || model.length()==0){
            Toast.makeText(this, "设备型号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //验证编号数据是否为空
        String num = et_num.getText().toString();
        if (null == num || num.length()==0){
            Toast.makeText(this, "设备编号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //验证编号数据是否为空
        String description = et_description.getText().toString();
        if (null == description || description.length()==0){
            Toast.makeText(this, "设备编号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //验证选择的图片大小
        if (mData.size()==1){
            Toast.makeText(this, "资源文件不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /**
     * 获取图片和视频返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
     * @param type  1 表示相册返回  2 表示拍照返回  3 表示录像  4 视频
     * @param list  多媒体
     */
    public void onSetData(int type, List<LocalMedia> list){

        if (list.size()==0){

        }else if(list.size()+(mData.size()-1)==3){
//            mData.clear();
            mData.remove(mData.size()-1);
            for (int i =0 ; i< list.size(); i ++){
                CameraBean bean = new CameraBean();
                if (list.get(i).isCompressed()){
                    bean.setFile_path(list.get(i).getCompressPath());
                }else{
                    bean.setFile_path(list.get(i).getPath());
                }

                bean.setChange(false);
                bean.setType(type);
                bean.setShow(true);
                mData.add(bean);
            }
        }else{
            mData.remove(mData.size()-1);
            for (int i =0 ; i< list.size(); i ++){
                CameraBean bean = new CameraBean();
                if (list.get(i).isCompressed()){
                    bean.setFile_path(list.get(i).getCompressPath());
                }else{
                    bean.setFile_path(list.get(i).getPath());
                }
                bean.setChange(false);
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
     * 提交报修信息
     */
    public void onSubmitRepairs(){


        if (validate()){

            dialog.show();
            final Map<String, String> param = new HashMap<>();
            param.put(ResponseKey.XINGHAO, et_model.getText().toString()+"");
            param.put(ResponseKey.BIANHAO, et_num.getText().toString()+"");
            param.put(ResponseKey.DES, et_description.getText().toString());



            List<Map<String, Object>> listFile = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getType()!=1) {
                    try {
                        File f = new File(mData.get(i).getFile_path());
                        Map<String, Object> map = new HashMap<>();
                        map.put(ResponseKey.IMGS+(i+1),f);
                        map.put("fileType", mData.get(i).getType() == 2 ?
                                OkhttpUtil.FILE_TYPE_IMAGE : OkhttpUtil.FILE_TYPE_VIDEO);
                        listFile.add(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
            param.put(ResponseKey.IMG_COUNT, (listFile.size())+"");

            try {
                deviceTaskService.onDeviceRepairs(param, listFile,new HttpResponseCallBack() {
                    @Override
                    public void callBack(Map<String, Object> result) {
//                        Toast.makeText(DeviceWarrantyActivity.this, param.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCallFail(String msg) {

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }


    /**
     * 修改报修信息
     */
    public void onUpdateOrder(){


        if (validate()){

            dialog.show();
            final Map<String, String> param = new HashMap<>();
            param.put(ResponseKey.XINGHAO, et_model.getText().toString()+"");
            param.put(ResponseKey.BIANHAO, et_num.getText().toString()+"");
            param.put(ResponseKey.DES, et_description.getText().toString());

            param.put(ResponseKey.ID, intent.getIntExtra(ResponseKey.ID, 0)+"");

            List<Map<String, Object>> listFile = new ArrayList<>();
            int numflag = 1;
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getType()!=1) {
                    try {
                        File f = new File(mData.get(i).getFile_path());
                        if (f.exists()){  //如果文件是从本地读取出来 则表示是新增或修改的图片
                            Logger.d("文件路径    "+f.getAbsolutePath());
                            Map<String, Object> map = new HashMap<>();
                            map.put(ResponseKey.IMGS+(i+1),f);
                            map.put("fileType", mData.get(i).getType() == 2 ?
                                    OkhttpUtil.FILE_TYPE_IMAGE : OkhttpUtil.FILE_TYPE_VIDEO);
                            listFile.add(map);
                        }else{  //为未修改的资源

                            String url = mData.get(i).getFile_path()
                                    .substring(mData.get(i).getFile_path().lastIndexOf("/")+1);
                            param.put(ResponseKey.IMGS + numflag, url);
                            numflag++;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
            param.put(ResponseKey.IMG_COUNT, (numflag-1+listFile.size())+"");

            try {
                deviceTaskService.onUpdateOrder(param, listFile,new HttpResponseCallBack() {
                    @Override
                    public void callBack(Map<String, Object> result) {
//                        Toast.makeText(DeviceWarrantyActivity.this, param.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCallFail(String msg) {

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }


    /**
     * 文件删除是发送的广播
     */
    public void onRegister(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION.IMAGE_DELETE);
        registerReceiver(broadcastReceiver, filter);

    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.ACTION.IMAGE_DELETE)){
                onDeleteData(intent.getStringExtra("filePath"));
            }

        }
    };

    /**
     * 删除数据
     * @param path
     */
    public void onDeleteData(String path){



        for (int i = mData.size()-1; i  >= 0 ; i --) {
            if (mData.get(i).getType() != 1){
                Logger.d(""+path +"        "+mData.get(i).getFile_path());
                if (mData.get(i).getFile_path().equals(path)) {
                    Logger.d("删除路径  "+ path);
                    mData.remove(i);
                }
            }
        }

        //检测最后一个是否是点击添加的按钮
        if (mData.size()!= 0){
            CameraBean bean = mData.get(mData.size()-1);
            if (bean.getType() != 1){  //如果最后一个不是按钮  则添加一个
                CameraBean b = new CameraBean();
                b.setType(1);
                b.setShow(false);
                b.setChange(false);
                mData.add(b);
            }
        }else {
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
}
