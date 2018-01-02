package com.example.wsq.android.activity.order;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;

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
 * 反馈报告界面
 * Created by wsq on 2017/12/15.
 */

public class FeedbackActivity extends Activity{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_away_back) TextView tv_away_back;
    @BindView(R.id.et_server_loc) TextView et_server_loc;
    @BindView(R.id.et_contact) TextView et_contact;
    @BindView(R.id.et_contact_tel) TextView et_contact_tel;
    @BindView(R.id.et_results_1) TextView et_results_1;
    @BindView(R.id.et_results_2) TextView et_results_2;
    @BindView(R.id.iv_back) LinearLayout iv_back;
    @BindView(R.id.gridview) GridView gridview;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.order_no) TextView order_no;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.layout_feedback);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);
        initView();
        onRegister();
    }


    public void initView() {
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        deviceTaskService = new OrderTaskServiceImpl();
        id = getIntent().getIntExtra(ResponseKey.ID,0);

        tv_title.setText("反馈报告");


        intent = getIntent();

        order_no.setText(intent.getStringExtra(ResponseKey.ORDER_NO));
        et_server_loc.setText(intent.getStringExtra(ResponseKey.DIDIAN));
        et_contact.setText(intent.getStringExtra(ResponseKey.S_NAME));
        et_contact_tel.setText(intent.getStringExtra(ResponseKey.S_TEL));
        et_results_1.setText(intent.getStringExtra(ResponseKey.CONTENT));
        et_results_2.setText(intent.getStringExtra(ResponseKey.YILIU));

        mData = new ArrayList<>();

        mAdapter = new UploadAdapter(this, mData);
        gridview.setAdapter(mAdapter);

//        String str = intent.getStringExtra(ResponseKey.R_IMGS);
//        List<String> list0 = new ArrayList<>();
//        if (str!= null || str.length()!=0 ){
//            try {
//                JSONArray jsona = new JSONArray(str);
//                for (int i = 0; i < jsona.length(); i++) {
//                    String s = jsona.get(i).toString();
//                    list0.add(s);
//                }
//                updateImags(list0, false);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else{
//            onSetData(0, null);
//        }

        initPopup();
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
                PictureFileUtils.deleteCacheDirFile(FeedbackActivity.this);
//                FunctionOptions.Builder builder = new FunctionOptions.Builder();
                switch (position){
                    case 0: //相册

                        PictureSelector.create(FeedbackActivity.this)
                                .openGallery(PictureMimeType.ofImage())
//                                .theme(R.sty)
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


    @OnClick({R.id.iv_back, R.id.tv_away_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_away_back:
                onSubmit();
                break;
        }
    }



    @OnItemClick({R.id.gridview})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CameraBean bean = mData.get(position);
        if (bean.getType() == 1){
            popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
        }else if(bean.getType() == 2){
            List<LocalMedia> list = new ArrayList<>();
            for (int i = 0; i< mData.size(); i ++){
                if (mData.get(i).getType()==2) {
                    LocalMedia media = new LocalMedia();
                    media.setPath(bean.getFile_path());
                    list.add(media);
                }
            }
            PictureSelector.create(FeedbackActivity.this).externalPicturePreview(position, list);
        }else if(bean.getType() == 3){
            PictureSelector.create(FeedbackActivity.this)
                    .externalPictureVideo(bean.getFile_path());

        }
    }


    /**
     * 提交反馈信息
     */
    public void onSubmit(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, id+"");
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, "")+"");
        param.put(ResponseKey.DIDIAN,et_server_loc.getText().toString());
        param.put(ResponseKey.LXR, et_contact.getText().toString());
        param.put(ResponseKey.TEL, et_contact_tel.getText().toString());
        param.put(ResponseKey.CONTENT, et_results_1.getText().toString());
        param.put(ResponseKey.YILIU, et_results_2.getText().toString());

        List<Map<String, Object>> listFile = new ArrayList<>();
        String img = intent.getStringExtra(ResponseKey.R_IMGS);
        int numflag = 1;
        try {
            JSONArray jsona = new JSONArray(img);

                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).getType() != 1) {

                        if (mData.get(i).isChange()){
                            File  f = new File(mData.get(i).getFile_path());
                            Map<String, Object> map = new HashMap<>();
                            map.put(ResponseKey.IMGS+(i+1),f);
                            map.put("fileType", mData.get(i).getType() == 2 ?
                                    OkhttpUtil.FILE_TYPE_IMAGE : OkhttpUtil.FILE_TYPE_VIDEO);
                            listFile.add(map);
                        }else{

                            for (int m = 0 ; m < jsona.length(); m++) {

                                if (mData.get(i).getFile_path().endsWith(jsona.get(m).toString())){
                                    param.put(ResponseKey.IMGS + numflag, jsona.get(m).toString()+"");
                                    numflag++;
                                }
                            }

                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        param.put(ResponseKey.IMG_COUNT, (numflag-1+listFile.size())+"");
        try {
            deviceTaskService.onSubmitReport(param, listFile, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(FeedbackActivity.this, "必要参数不能为空", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {

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
    }

    /**
     * 设置数据
     * @param type  1 表示相册返回  2 表示拍照返回  3 表示录像  4 视频
     * @param list  多媒体
     */
    public void onSetData(int type, List<LocalMedia> list){


        if (list.size()==0){
            CameraBean bean = new CameraBean();
            bean.setType(1);
            bean.setShow(false);
             bean.setChange(false);
            mData.add(bean);

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
     *
     * @param array
     * @param isChange   文件是否发生变化
     */
    public void updateImags(List<String>  array, boolean isChange){

        if (array.size()==0){

        }else if(array.size()==3){
            mData.clear();
            for (int i =0 ; i< array.size(); i ++){
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST+Urls.GET_IMAGES+array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);

                }else{
                    bean.setType(2);
                }
                bean.setChange(isChange);
                bean.setShow(true);
                mData.add(bean);
            }
        }else{
            if (mData.size()!=0){
                mData.remove(mData.size()-1);
            }

            for (int i =0 ; i< array.size(); i ++){
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST+Urls.GET_IMAGES+array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);
                }else{
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


        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getFile_path().equals(path)){
                mData.remove(i);
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
