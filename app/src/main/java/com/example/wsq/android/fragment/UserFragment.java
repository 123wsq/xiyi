package com.example.wsq.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.AboutWeActivity;
import com.example.wsq.android.activity.CollectActivity;
import com.example.wsq.android.activity.DeviceWarrantyActivity;
import com.example.wsq.android.activity.KnowledgeActivity;
import com.example.wsq.android.activity.OrderActivity;
import com.example.wsq.android.activity.UpdatePsdActivity;
import com.example.wsq.android.activity.UserInfoActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ValidateParam;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/7.
 */

public class UserFragment extends Fragment {


    @BindView(R.id.tv_username) TextView tv_username;
    @BindView(R.id.tv_sex) TextView tv_sex;
    @BindView(R.id.tv_role) TextView tv_role;
    @BindView(R.id.ll_company_order) LinearLayout ll_company_order;
    @BindView(R.id.ll_server_order) LinearLayout ll_server_order;
    @BindView(R.id.ll_manager) LinearLayout ll_manager;
    @BindView(R.id.ll_device_server) LinearLayout ll_device_server;
    @BindView(R.id.ll_fee) LinearLayout ll_fee;
    @BindView(R.id.roundImage_header) RoundImageView roundImage_header;

    public static final String FLAG_ORDER_KEY = "flag_order";
    private Map<String, Object> orderMap;
    private UserService userService;

    private SharedPreferences shared;
    private CustomPopup popup;

    private final int httpSec = 1; //获取用户信息
    public static Map<String, Object> mUserData; //用户信息
    private CustomDefaultDialog defaultDialog;

    public static UserFragment getInstance(){
        return new UserFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.layout_fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initView();
    }


    public void init() {

        mUserData = new HashMap<>();
        userService = new UserServiceImpl();
        orderMap = new HashMap<>();
        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        getUserInfo();

    }



    public void initView() {



        /**
         * 显示相关角色下面的订单菜单
         */
        if (shared.getString(Constant.SHARED.JUESE, "").equals("1")){
            ll_company_order.setVisibility(View.GONE);
            ll_server_order.setVisibility(View.VISIBLE);
            ll_manager.setVisibility(View.GONE);
            ll_device_server.setVisibility(View.VISIBLE);
            ll_fee.setVisibility(View.VISIBLE);
        }else{
            ll_company_order.setVisibility(View.VISIBLE);
            ll_server_order.setVisibility(View.GONE);
            ll_manager.setVisibility(View.VISIBLE);
            ll_device_server.setVisibility(View.GONE);
            ll_fee.setVisibility(View.GONE);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case httpSec:
                    Map<String, Object> result = (Map<String, Object>) msg.obj;
                    tv_username.setText(result.get(ResponseKey.USERNAME).toString());

                    //设置头像
                    Glide.with(getActivity()).load(Urls.HOST+result.get(ResponseKey.USER_PIC)).into(roundImage_header);
                    //判断性别是否为空
                    String strSex = result.get(ResponseKey.SEX).toString();
                    if(!ValidateParam.validateParamIsNull(strSex)){
                        int sex = Integer.parseInt(strSex);
                        tv_sex.setText(Constant.SEX[sex-1]);
                    }


                    //判断角色是否为空
                    String strRole = result.get(ResponseKey.JUESE).toString();
                    if (!ValidateParam.validateParamIsNull(strRole)){
                        int role = Integer.parseInt(strRole);
                        tv_role.setText(Constant.ROLE[role-1]);
                    }

                    break;
            }
        }
    };


    @OnClick({ R.id.ll_user_layout, R.id.ll_order_unfinish, R.id.ll_order_handler,
            R.id.ll_order_finish, R.id.ll_order, R.id.ll_order_fllocation, R.id.ll_order_progress,
            R.id.ll_order_feedback,R.id.ll_server_finish, R.id.ll_device_assert, R.id.ll_device_report,
            R.id.ll_device_bank_code, R.id.ll_device_server_share, R.id.ll_server_call,
            R.id.ll_password, R.id.ll_about, R.id.ll_fault, R.id.ll_collect,R.id.ll_manager_shared,
            R.id.ll_manager_upload, R.id.ll_device_knowledge})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_user_layout:
                IntentFormat.startActivity(getActivity(), UserInfoActivity.class);
                break;
            case R.id.ll_order_unfinish:
                orderMap.put(FLAG_ORDER_KEY, 1);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order_handler:
                orderMap.put(FLAG_ORDER_KEY, 3);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order_finish:
                orderMap.put(FLAG_ORDER_KEY, 2);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order:
                orderMap.put(FLAG_ORDER_KEY, 4);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order_fllocation:
                orderMap.put(FLAG_ORDER_KEY, 5);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order_progress:
                orderMap.put(FLAG_ORDER_KEY, 6);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_order_feedback:
                orderMap.put(FLAG_ORDER_KEY, 7);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_server_finish:
                orderMap.put(FLAG_ORDER_KEY, 8);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_device_assert:  //我要报修
                if (!shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
                    IntentFormat.startActivity(getActivity(), DeviceWarrantyActivity.class);
                }else{
                    Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_fault:  //资料
                    Intent intent1 = new Intent();
                    intent1.setAction(Constant.ACTION.USER_PAGE_FAULT);
                    getActivity().sendBroadcast(intent1);
                break;
            case R.id.ll_manager_shared:   //企业 管理工程师分享

            case R.id.ll_manager_upload:  //设备上传
                CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("请至公司官网 "+getResources().getString(R.string.company_net)
                        +" 进行操作，谢谢合作");
                builder.setOkBtn("好的", new OnDialogClickListener() {
                    @Override
                    public void onClick(CustomDefaultDialog dialog, String result) {
                        dialog.dismiss();
                    }
                });
                defaultDialog = builder.create();
                defaultDialog.show();
                break;
            case R.id.ll_device_report:  //写报告
                orderMap.put(FLAG_ORDER_KEY, 7);
                IntentFormat.startActivity(getActivity(), OrderActivity.class, orderMap);
                break;
            case R.id.ll_device_bank_code:  //银行卡

                break;
            case R.id.ll_device_server_share:  //服务工程师   分享

                CustomDefaultDialog.Builder builder1 = new CustomDefaultDialog.Builder(getActivity());
                builder1.setTitle("提示");
                builder1.setMessage("请至公司官网 "+getResources().getString(R.string.company_net)
                        +" 进行操作，谢谢合作");
                builder1.setOkBtn("好的", new OnDialogClickListener() {
                    @Override
                    public void onClick(CustomDefaultDialog dialog, String result) {
                        dialog.dismiss();
                    }
                });
                defaultDialog = builder1.create();
                defaultDialog.show();
                break;
            case R.id.ll_device_knowledge:  //圈内知识
                IntentFormat.startActivity(getActivity(), KnowledgeActivity.class);
                break;
            case R.id.ll_collect:  //我的收藏
                IntentFormat.startActivity(getActivity(), CollectActivity.class);
                break;
            case R.id.ll_server_call:  //客服中心
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_default_popup,null);
                List<String> list = new ArrayList<>();
                list.add(getResources().getString(R.string.server_tel));
                popup = new CustomPopup(getActivity(), view, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {
                        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+result));
                        startActivity(intent);
                    }
                });
                popup.showAtLocation(getActivity().findViewById(R.id.ll_layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_password:  //修改密码

                IntentFormat.startActivity(getActivity(), UpdatePsdActivity.class);
                break;
            case R.id.ll_about:  //关于我们
                IntentFormat.startActivity(getActivity(), AboutWeActivity.class);
                break;


        }
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(){
        //获取用户信息
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        try {
            userService.getUserInfo(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {
                    mUserData.putAll(result);
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = httpSec;
                    handler.sendMessage(msg);
                }

                @Override
                public void onCallFail(String msg) {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
