package com.example.wsq.android.activity.order;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderInfoActivity extends Activity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_edit) TextView tv_edit;
    @BindView(R.id.tv_ordernum) TextView tv_ordernum;
    @BindView(R.id.tv_companyName) TextView tv_companyName;
    @BindView(R.id.tv_repairs_time)TextView tv_repairs_time;
    @BindView(R.id.tv_device) TextView tv_device;
    @BindView(R.id.tv_outnum) TextView tv_outnum;
    @BindView(R.id.tv_fault_desc) TextView tv_fault_desc;
    @BindView(R.id.tv_upName) TextView tv_upName;
    @BindView(R.id.tv_upTel) TextView tv_upTel;
    @BindView(R.id.tv_serverName) TextView tv_serverName;
    @BindView(R.id.tv_traveling_fee) TextView tv_traveling_fee;
    @BindView(R.id.tv_server_fee) TextView tv_server_fee;
    @BindView(R.id.tv_spare_fee) TextView tv_spare_fee;
    @BindView(R.id.tv_other_fee) TextView tv_other_fee;
    @BindView(R.id.tv_all_fee) TextView tv_all_fee;
    @BindView(R.id.tv_affirm) TextView tv_affirm;
    @BindView(R.id.tv_negation) TextView tv_negation;
    @BindView(R.id.tv_transfer) TextView tv_transfer;
    @BindView(R.id.rv_gridview) GridView rv_gridview;
    @BindView(R.id.ll_fee) LinearLayout ll_fee;
    @BindView(R.id.ll_submit) LinearLayout ll_submit ;

    private UploadAdapter mAdapter;
    private List<CameraBean> mData;
    private Map<String, Object> mResultInfo;
    private OrderTaskService orderTaskService;
    private String id = "";
    private String token = "";
    private String role = "";//角色
    private String status = "";
    private SharedPreferences shared;
    private CustomDefaultDialog defaultDialog;

    public static final String UPDATE = "isUpdate";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_info);
        AppStatus.onSetStates(this);
        ButterKnife.bind(this);

        init();

        initView();
    }


    public void init() {

        mData = new ArrayList<>();
        mResultInfo = new HashMap<>();

        orderTaskService = new OrderTaskServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        id = getIntent().getIntExtra(ResponseKey.ID,0 )+"";
        token = shared.getString(Constant.SHARED.TOKEN, "");
        role = shared.getString(Constant.SHARED.JUESE,"");
        status = getIntent().getStringExtra(ResponseKey.STATUS);

        tv_edit.setVisibility(status.equals("-1") ? View.VISIBLE : View.GONE);

        rv_gridview.setOnItemClickListener(this);

        if(role.equals("1") && getIntent().getStringExtra(ResponseKey.STATUS).equals("4")){
            tv_transfer.setVisibility(View.VISIBLE);
            tv_transfer.setText("填写反馈报告");
        }else if(role.equals("1") && getIntent().getStringExtra(ResponseKey.STATUS).equals("5")){
            tv_transfer.setVisibility(View.VISIBLE);
            tv_transfer.setText("填写移交反馈报告");
        }

    }

    public void initView() {


        mAdapter = new UploadAdapter(this, mData);
        rv_gridview.setAdapter(mAdapter);

        tv_title.setText("订单详情");

        getOrderInfo();

        //设置服务费用显示情况  1.当角色为企业工程师的时候是不能显示费用
        if (role.equals("2")
                ||status.equals("-1")//待评估的订单不能显示费用
                ){
            ll_fee.setVisibility(View.GONE);
        }else{
            ll_fee.setVisibility(View.VISIBLE);
        }
        //当为服务工程师的时候，在已移交中，需要填写移交报告
//        if (role.equals("1")
//                && status.equals("7.1")){
//            tv_transfer.setVisibility(View.VISIBLE);
//        }



    }

    @OnClick({R.id.iv_back, R.id.tv_affirm, R.id.tv_negation, R.id.tv_transfer, R.id.tv_edit})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_affirm:
                if (role.equals("1")
                        && status.equals("2")){ //服务工程师

                    onStartTask("chuli");

                }else if (role.equals("1")
                        && status.equals("3")){ //服务工程师

                    onCreateDialog("确定完成订单？", "wancheng");

                }else{  //企业管理工程师
                    onAudit("", 1);
                }

                break;
            case R.id.tv_negation:
                if (role.equals("1")
                        && status.equals("3")){ //服务工程师
                    onCreateDialog("确定移交订单吗？", "yijiao");
                }else {
                    CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(OrderInfoActivity.this);
                    builder.setIsShowInput(true);
                    builder.setTitle("审核不通过");
                    builder.setHintInput("说一说您拒绝人家的原因~");
                    builder.setOkBtn("确认", new OnDialogClickListener() {
                        @Override
                        public void onClick(CustomDefaultDialog dialog, String result) {

                            onAudit(result, 2);
                            dialog.dismiss();
                        }
                    });
                    defaultDialog = builder.create();
                    defaultDialog.show();
                }
                break;
            case R.id.tv_transfer:
//                Map<String, Object> map = new HashMap<>();
//                map.put(ResponseKey.ID, id);
                IntentFormat.startActivity(this, FeedbackActivity.class, mResultInfo);
                finish();
                break;
            case R.id.tv_edit:
                mResultInfo.put(UPDATE, true);
                IntentFormat.startActivity(this, DeviceWarrantyActivity.class, mResultInfo);
                break;
        }

    }

    public void onCreateDialog(String msg , final String action){

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(OrderInfoActivity.this);
        builder.setIsShowInput(false);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setOkBtn("确认", new OnDialogClickListener() {
            @Override
            public void onClick(CustomDefaultDialog dialog, String result) {

                onStartTask(action);
                dialog.dismiss();
            }
        });
         builder.setCancelBtn("取消", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                defaultDialog.dismiss();
             }
         });
        defaultDialog = builder.create();
        defaultDialog.show();
    }

    /**
     * 服务工程师开始任务
     */
    public void onStartTask(String action){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, token);
        param.put(ResponseKey.ID, id);
        param.put(ResponseKey.ACTION, action);
        try {
            orderTaskService.onOrderStatus(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {


                    Toast.makeText(OrderInfoActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 审核
     * @param result  未通过时的原因
     * @param type  1 表示通过   2表示不通过
     */
    public void onAudit(String result, int type){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));
        param.put(ResponseKey.ID, id);
        param.put(ResponseKey.ACTION, type == 1 ? "pass" : "nopass");
        if (type == 2){
            param.put(ResponseKey.NOPASS, result);
        }
        try {
            orderTaskService.onAudit(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    Toast.makeText(OrderInfoActivity.this, result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取订单详情
     */
    public void getOrderInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, token);
        param.put(ResponseKey.ID, id);
        param.put(ResponseKey.JUESE, role);

        try {
            orderTaskService.ongetOrderInfo(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    mResultInfo.putAll(result);
                    tv_ordernum.setText(result.get(ResponseKey.ORDER_NO).toString()+"");
                    tv_companyName.setText(result.get(ResponseKey.COMPANY).toString()+"");
                    tv_repairs_time.setText(result.get(ResponseKey.BAOXIUTIME).toString()+"");

                    tv_device.setText(result.get(ResponseKey.XINGHAO).toString()+"");
                    tv_outnum.setText(result.get(ResponseKey.BIANHAO).toString()+"");
                    tv_fault_desc.setText(result.get(ResponseKey.DES).toString()+"");

                    if (role.equals("1")){

                        tv_upName.setText(result.get(ResponseKey.S_NAME).toString() + "");
                        tv_upTel.setText(result.get(ResponseKey.S_TEL).toString() + "");
                    }else {
                        tv_upName.setText(result.get(ResponseKey.NAME).toString() + "");
                        tv_upTel.setText(result.get(ResponseKey.TEL).toString() + "");
                    }
                    String imags = result.get(ResponseKey.IMGS)+"";
                    if (TextUtils.isEmpty(imags) || imags.equals("null")) {
                        imags = result.get(ResponseKey.R_IMGS)+"";
                    }

                    try {
                        JSONArray jsona = new JSONArray(imags);

                        List<String> list = new ArrayList<>();

                        for (int i = 0; i < jsona.length(); i++) {

                            list.add(jsona.get(i).toString());
                        }
                             showImags( list);
//                            CameraBean bean = new CameraBean();
//
//
//
//                            for (int j=0; j< Constant.PIC.length; j++){
//
//                                if (jsona.get(i).toString().endsWith(Constant.PIC[j])){
//                                    bean.setType(2);
//                                }
//                            }
//                            if (bean.getType()==0){
//                                bean.setType(3);
//                            }
//                            bean.setFile_path(Urls.HOST+Urls.GET_IMAGES+jsona.get(i).toString());
//                            bean.setShow(false);
//                            bean.setChange(false);
//                            mData.add(bean);
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mData.size() !=0 ){
                        mAdapter.notifyDataSetChanged();
                    }

                    tv_traveling_fee.setText(result.get(ResponseKey.CHAILV).toString()+"元");
                    tv_server_fee.setText(result.get(ResponseKey.FUWU).toString()+"元");
                    tv_spare_fee.setText(result.get(ResponseKey.BEIJIAN).toString()+"元");
                    tv_other_fee.setText(result.get(ResponseKey.QITA).toString()+"元");
                    tv_all_fee.setText(result.get(ResponseKey.ZONG).toString()+"元");


                    //判断当前角色
                    if (role.equals("3")
                            && result.get(ResponseKey.STATUS).toString().equals("0")){
                        ll_submit.setVisibility(View.VISIBLE);
                        tv_affirm.setText("通过");
                        tv_negation.setText("不通过");
                    }else if(role.equals("1")
                            && result.get(ResponseKey.STATUS).toString().equals("2")){
                        ll_submit.setVisibility(View.VISIBLE);
                        tv_affirm.setText("开始任务");
                        tv_negation.setText("总计:  " + result.get(ResponseKey.ZONG)+"元");
                        tv_negation.setBackgroundColor(Color.parseColor("#676565"));
                        tv_negation.setClickable(false);
                    }else if(role.equals("1")
                            && result.get(ResponseKey.STATUS).toString().equals("3")){
                        ll_submit.setVisibility(View.VISIBLE);
                        tv_affirm.setText("完成任务");
                        tv_negation.setText("移交订单");
                    }else {
                        ll_submit.setVisibility(View.INVISIBLE);
                    }
                }


                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showImags( List<String>  array){

        mData.clear();
        for (int i =0 ; i< array.size(); i ++) {
            CameraBean bean = new CameraBean();
            bean.setFile_path(Urls.HOST + Urls.GET_IMAGES + array.get(i));

            for (int j = 0; j < Constant.PIC.length; j++) {

                if (array.get(i).toString().endsWith(Constant.PIC[j])) {
                    bean.setType(2);
                }
            }
            if (bean.getType() == 0) {
                bean.setType(3);
            }
            bean.setFile_path(Urls.HOST + Urls.GET_IMAGES + array.get(i).toString());
            bean.setShow(false);
            bean.setChange(false);
            mData.add(bean);

        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CameraBean bean = mData.get(position);
        if(bean.getType() == 2){
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
            PictureSelector.create(OrderInfoActivity.this).externalPicturePreview(num, list);
        }else if(bean.getType() == 3){

//        PictureSelector.create(OrderInfoActivity.this)
//                .externalPictureVideo(bean.getFile_path());
            Map<String, Object> param = new HashMap<>();
            param.put("URL", bean.getFile_path());
            IntentFormat.startActivity(OrderInfoActivity.this, VideoPlayActivity.class, param);

        }
    }
}
