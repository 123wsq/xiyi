package com.example.wsq.android.activity.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medialib.video.VideoPlayActivity;
import com.example.wsq.android.R;
import com.example.wsq.android.adapter.UploadAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.CustomStartOrderDialog;
import com.example.wsq.android.view.LoddingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_edit) TextView tv_edit;
    @BindView(R.id.tv_ordernum) TextView tv_ordernum;
    @BindView(R.id.tv_companyName) TextView tv_companyName;
    @BindView(R.id.tv_companyAddress) TextView tv_companyAddress;
    @BindView(R.id.tv_time_name) TextView tv_time_name;
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
    @BindView(R.id.ll_order) LinearLayout ll_order;
    @BindView(R.id.tv_server_name) TextView tv_server_name;
    @BindView(R.id.ll_audit_time) LinearLayout ll_audit_time;
    @BindView(R.id.tv_audit_name) TextView tv_audit_name;
    @BindView(R.id.tv_audit_time) TextView tv_audit_time;

    private UploadAdapter mAdapter;
    private List<CameraBean> mData;
    private Map<String, Object> mResultInfo;
    private OrderTaskService orderTaskService;
    private String id = "";
    private String token = "";
    private String role = "";//角色
    private String status = "";
    private LoddingDialog dialog;
    private SharedPreferences shared;
    private CustomDefaultDialog defaultDialog;
    private CustomStartOrderDialog startOrderDialog;
    public static final String UPDATE = "isUpdate";


    @Override
    public int getByLayoutId() {
        return R.layout.layout_order_info;
    }


    public void init() {

        mData = new ArrayList<>();
        mResultInfo = new HashMap<>();

        dialog= new LoddingDialog(this);
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

        initView();

        onInitState();
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

                    onShowDialogServer();

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
                    builder.setTitleColor("#FF0000");
                    builder.setShowCloseDialog(true);
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
            case R.id.tv_edit:  //订单修改
                mResultInfo.put(UPDATE, true);
                IntentFormat.startActivity(this, DeviceWarrantyActivity.class, mResultInfo);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mData.clear();
        getOrderInfo();
    }

    public void onInitState(){

        //当角色是2，3的时候显示
        if (!role.equals("1")){
            ll_order.setVisibility(View.VISIBLE);
        }else{
            ll_order.setVisibility(View.GONE);
        }

        if (status.equals("-1") || status.equals("0")){
            tv_server_name.setText("当前状态");
            tv_serverName.setText(status.equals("-1") ? "待评估": "未审核");
            tv_time_name.setText( "报修时间");
        }else if(status.equals("1") || status.equals("1.1")){
            tv_time_name.setText("审核时间");
        }else if(status.equals("2")){

            if (!role.equals("1")) {
                ll_audit_time.setVisibility(View.VISIBLE);
                tv_audit_name.setText("报修时间");
                tv_time_name.setText("审核时间");
            }else {
                tv_time_name.setText("分配时间");
            }
        }else if(status.equals("3")){
            tv_time_name.setText("开始时间");
        }else if(status.equals("4") || status.equals("5")){
            tv_time_name.setText("完成时间");
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

        orderTaskService.onOrderStatus(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(OrderInfoActivity.this, result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure() {

            }
        });

    }


    /**
     * 审核
     * @param result  未通过时的原因
     * @param type  1 表示通过   2表示不通过
     */
    public void onAudit(String result, int type){
        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));
        param.put(ResponseKey.ID, id);
        param.put(ResponseKey.ACTION, type == 1 ? "pass" : "nopass");
        if (type == 2){
            param.put(ResponseKey.NOPASS, result);
        }
        orderTaskService.onAudit(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(OrderInfoActivity.this, result.get(ResponseKey.MESSAGE).toString(), Toast.LENGTH_SHORT).show();

                if (dialog.isShowing())dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure() {
                if (dialog.isShowing())dialog.dismiss();
            }
        });

    }


    /**
     * 获取订单详情
     */
    public void getOrderInfo(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, token);
        param.put(ResponseKey.ID, id);
        param.put(ResponseKey.JUESE, role);
        orderTaskService.ongetOrderInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mResultInfo.putAll(result);
                tv_ordernum.setText(result.get(ResponseKey.ORDER_NO)+"");
                tv_companyName.setText(result.get(ResponseKey.COMPANY)+"");
                tv_companyAddress.setText(result.get(ResponseKey.COMPANY_ADDRESS)+"");

                tv_device.setText(result.get(ResponseKey.XINGHAO)+"");
                tv_outnum.setText(result.get(ResponseKey.BIANHAO)+"");
                tv_fault_desc.setText(result.get(ResponseKey.DES)+"");

                if (role.equals("1")){
                    tv_upName.setText(result.get(ResponseKey.S_NAME) + "");
                    tv_upTel.setText(result.get(ResponseKey.S_TEL) + "");
                }else {
                    tv_upName.setText(result.get(ResponseKey.NAME) + "");
                    tv_upTel.setText(result.get(ResponseKey.TEL) + "");
                }

                if (status.equals("-1")){
                    tv_repairs_time.setText(result.get(ResponseKey.BAOXIUTIME)+"");
                }else if(status.equals("0")){
                    tv_repairs_time.setText(result.get(ResponseKey.BAOXIUTIME)+"");
                }else if(status.equals("1") || status.equals("1.1")){
                    tv_repairs_time.setText(result.get(ResponseKey.CHECK_TIME)+"");
                }else if(status.equals("2")){
                    tv_audit_time.setText(result.get(ResponseKey.BAOXIUTIME)+"");
                    tv_repairs_time.setText(result.get(ResponseKey.FENPEI_TIME)+"");
                    tv_serverName.setText(result.get(ResponseKey.WNAME)+"");
                }else if(status.equals("3")){
                    tv_repairs_time.setText(result.get(ResponseKey.BEGIN_TIME)+"");
                }else if(status.equals("4") || status.equals("5")){
                    tv_repairs_time.setText(result.get(ResponseKey.OVER_TIME)+"");
                }else if(status.equals("8")){
                    tv_serverName.setText(result.get(ResponseKey.WNAME)+"");
                    tv_repairs_time.setText(result.get(ResponseKey.DONE_TIME)+"");
                }




                //设置图片
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mData.size() !=0 ){
                    mAdapter.notifyDataSetChanged();
                }

                tv_traveling_fee.setText(result.get(ResponseKey.CHAILV)+"元");
                tv_server_fee.setText(result.get(ResponseKey.FUWU)+"元");
                tv_spare_fee.setText(result.get(ResponseKey.BEIJIAN)+"元");
                tv_other_fee.setText(result.get(ResponseKey.QITA)+"元");
                tv_all_fee.setText(result.get(ResponseKey.ZONG)+"元");


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
                    tv_affirm.setText("完成订单");
                    tv_negation.setText("移交订单");
                }else {
                    ll_submit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure() {

            }
        });
        
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
            //将所有的图片添加到list中
            for (int i = 0; i< mData.size(); i ++){
                if (mData.get(i).getType()==2) {
                    LocalMedia media = new LocalMedia();
                    media.setPath(mData.get(i).getFile_path());
                    list.add(media);
                }
            }
            //计算选中的图片位置
            for (int i = 0; i< list.size(); i++){
                if (bean.getFile_path().equals(list.get(i).getPath())){
                    num = i;
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


    private void onShowDialogServer(){
        CustomStartOrderDialog.Builder builder = new CustomStartOrderDialog.Builder(this);
        builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartTask("chuli");
                startOrderDialog.dismiss();
            }
        });
        startOrderDialog = builder.create();
        startOrderDialog.show();
    }
}
