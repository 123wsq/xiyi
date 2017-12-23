package com.example.wsq.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.UploadAdapter;
import com.example.wsq.android.bean.CameraBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.utils.IntentFormat;

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
 * Created by wsq on 2017/12/18.
 */

public class ServerOrderInfoActivity extends Activity{

    @BindView(R.id.tv_title) TextView tv_title; //标题
    @BindView(R.id.iv_back) ImageView iv_back; //返回
    @BindView(R.id.tv_companyName) TextView tv_companyName; //公司名称
    @BindView(R.id.tv_ordernum) TextView tv_ordernum;  //订单编号
    @BindView(R.id.tv_order_start) TextView tv_order_start;  //订单开始时间
    @BindView(R.id.tv_order_end) TextView tv_order_end;  //订单结束时间
    @BindView(R.id.tv_device) TextView tv_device;  //报修设备
    @BindView(R.id.tv_outnum) TextView tv_outnum;  //出厂编号
    @BindView(R.id.tv_fault_desc) TextView tv_fault_desc;  //故障编号
    @BindView(R.id.rv_gridview) GridView rv_gridview;   //显示故障图片或视频
    @BindView(R.id.tv_server_loc) TextView tv_server_loc;  //服务地点
    @BindView(R.id.tv_server_content) TextView tv_server_content; //服务结果及内容
    @BindView(R.id.tv_server_leave) TextView tv_server_leave;  //遗留问题
    @BindView(R.id.rv_gridview_scene) GridView rv_gridview_scene; //现场照片视频
    @BindView(R.id.tv_scene_contact) TextView tv_scene_contact;  //现场联系人
    @BindView(R.id.tv_scene_tel)  TextView tv_scene_tel;  //显示联系人电话
    @BindView(R.id.tv_traveling_fee) TextView tv_traveling_fee;//差旅费
    @BindView(R.id.tv_server_fee) TextView tv_server_fee;//服务费
    @BindView(R.id.tv_spare_fee) TextView tv_spare_fee;//备件费
    @BindView(R.id.tv_other_fee) TextView tv_other_fee;//其他费
    @BindView(R.id.tv_all_fee) TextView tv_all_fee;//总费


    private OrderTaskService orderTaskService;
    private SharedPreferences shared;
    private int id = 0;
    private UploadAdapter mAdapter1, mAdapter2;
    private List<CameraBean> mData1, mData2;
    private Map<String, Object> mResultInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_feedback_order_info);
        ButterKnife.bind(this);

        tv_title.setText("订单详情");

        initView();
    }

    public void initView(){

        mData1 = new ArrayList<>();
        mData2 = new ArrayList<>();
        mResultInfo = new HashMap<>();
        orderTaskService = new OrderTaskServiceImpl();
        Intent intent = getIntent();
        id = intent.getIntExtra(ResponseKey.ID,0);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        //基本信息
        tv_companyName.setText(intent.getStringExtra(ResponseKey.COMPANY));
        tv_ordernum.setText(intent.getStringExtra(ResponseKey.ORDER_NO));
        tv_order_start.setText(intent.getStringExtra(ResponseKey.BEGIN_TIME));
        tv_order_end.setText(intent.getStringExtra(ResponseKey.OVER_TIME));

        //维修信息
        tv_device.setText(intent.getStringExtra(ResponseKey.XINGHAO));
        tv_outnum.setText(intent.getStringExtra(ResponseKey.BIANHAO));



        //故障照片、视频
        mAdapter1 = new UploadAdapter(this, mData1);
        rv_gridview.setAdapter(mAdapter1);

        //现场照片和视频
        mAdapter2 = new UploadAdapter(this, mData2);
        rv_gridview_scene.setAdapter(mAdapter2);

        getServerOrderInfo();


    }

    @OnClick({R.id.iv_back, R.id.tv_transfer})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_transfer:
//                Map<String, Object> map = new HashMap<>();
//                map.put(ResponseKey.ID, id);
                IntentFormat.startActivity(this, FeedbackActivity.class, mResultInfo);
                break;
        }
    }

    public void getServerOrderInfo(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.ID, id + "");
        param.put(ResponseKey.JUESE, shared.getString(Constant.SHARED.JUESE, "0"));
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN,""));

        try {
            orderTaskService.ongetOrderInfo(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    mResultInfo.putAll(result);
                    tv_fault_desc.setText(result.get(ResponseKey.DES).toString());

                    //服务信息
                    tv_server_loc.setText(result.get(ResponseKey.DIDIAN).toString());
                    tv_server_content.setText(result.get(ResponseKey.CONTENT).toString());
                    tv_server_leave.setText(result.get(ResponseKey.YILIU).toString());

                    //上报图片
                    String imags1 = result.get(ResponseKey.R_IMGS).toString();
                    List<String> list1 = new ArrayList<>();
                    try {
                        JSONArray jsona1 = new JSONArray(imags1);
                        for (int i = 0; i < jsona1.length(); i++) {
                            list1.add(jsona1.get(i).toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showImags(mData1, list1, 1);
                    //现场图片
                    String imags2 = result.get(ResponseKey.IMGS).toString();
                    List<String> list2 = new ArrayList<>();
                    try {
                        JSONArray jsona2 = new JSONArray(imags2);
                        for (int i = 0; i < jsona2.length(); i++) {
                            list2.add(jsona2.get(i).toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showImags(mData2, list2, 2);


                    //其他信息
                    tv_scene_contact.setText(result.get(ResponseKey.LXR).toString());
                    tv_scene_tel.setText(result.get(ResponseKey.TEL).toString());

                    //服务费用
                    tv_traveling_fee.setText(result.get(ResponseKey.CHAILV).toString());
                    tv_server_fee.setText(result.get(ResponseKey.FUWU).toString());
                    tv_spare_fee.setText(result.get(ResponseKey.BEIJIAN).toString());
                    tv_other_fee.setText(result.get(ResponseKey.QITA).toString());
                    tv_all_fee.setText(result.get(ResponseKey.ZONG).toString());
                }

                @Override
                public void onCallFail(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImags(List<CameraBean> list, List<String>  array, int type){

        if (array.size()==0){

        }else if(array.size()==3){
            list.clear();
            for (int i =0 ; i< array.size(); i ++){
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST+Urls.GET_IMAGES+array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);
                }else{
                    bean.setType(2);
                }
                bean.setShow(false);
                list.add(bean);
            }
        }else{
            if (list.size()!=0){
                list.remove(list.size()-1);
            }

            for (int i =0 ; i< array.size(); i ++){
                CameraBean bean = new CameraBean();
                bean.setFile_path(Urls.HOST+Urls.GET_IMAGES+array.get(i));
                if (array.get(i).toLowerCase().endsWith(".mp4")) {
                    bean.setType(3);
                }else{
                    bean.setType(2);
                }
                bean.setShow(false);
                list.add(bean);
            }

        }

        if (type == 1){  //故障更新
            mAdapter1.notifyDataSetChanged();
        }else{  //现场更新
            mAdapter2.notifyDataSetChanged();
        }
    }
}
