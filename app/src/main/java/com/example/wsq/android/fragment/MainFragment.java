package com.example.wsq.android.fragment;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.KnowledgeActivity;
import com.example.wsq.android.activity.NewsActivity;
import com.example.wsq.android.activity.order.DeviceWarrantyActivity;
import com.example.wsq.android.activity.order.OrderActivity;
import com.example.wsq.android.activity.user.IntegralActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnPopupListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.loader.GlideImageLoader;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppImageLoad;
import com.example.wsq.android.tools.AppImageView;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.SpreadPopup;
import com.example.wsq.plugin.banner.Banner;
import com.example.wsq.plugin.banner.BannerConfig;
import com.example.wsq.plugin.banner.Transformer;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by wsq on 2017/12/7.
 */

public class MainFragment extends Fragment {


    @BindView(R.id.banner) Banner banner;
    @BindView(R.id.image_sbwx) ImageView image_sbwx;
    @BindView(R.id.image_gcs) ImageView image_gcs;
    @BindView(R.id.iv_image_news) ImageView iv_image_news;
    @BindView(R.id.image_kefu) ImageView image_kefu;
    @BindView(R.id.image_main_qnzs)ImageView image_main_qnzs;

    private List<String> mImages;
    private List<String> mTitles;

    private SharedPreferences shared;
    private CustomPopup popup;
    private SpreadPopup spreadPopup;
    private SpreadPopup redPacketPopup;
    private UserService userService;

    private long startTime;
    private String getPoint;
    public static MainFragment getInstance(){
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();

        AppImageView.onImageView(getActivity(), image_sbwx, "image_sbwh_m.png");
        AppImageView.onImageView(getActivity(), image_gcs, "image_gcs.png");
        AppImageView.onImageView(getActivity(), iv_image_news, "image_news.png");
        AppImageView.onImageView(getActivity(), image_kefu, "image_kefu.png");
        AppImageView.onImageView(getActivity(), image_main_qnzs, "image_main_qnzs.png");
    }


    public void init(){

        shared = getActivity().getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        userService  =  new UserServiceImpl();
        mImages = new ArrayList<>();
        mTitles = new ArrayList<>();

    }

    public void initView() {


        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5*1000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.DURATION);
        //banner设置方法全部调用完毕时最后调用
        String path = shared.getString(Constant.SHARED.BANNER_PATH, "");
        try {
            JSONArray jsona = new JSONArray(path);
            for (int i = 0; i < jsona.length(); i++) {
                mTitles.add("");
                mImages.add(Urls.HOST+Urls.GET_IMAGES+jsona.get(i)+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //设置图片集合
        banner.setImages(mImages);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitles);

        banner.start();

        getBannerImages();

        //显示红包
        if(shared.getString(Constant.SHARED.JUESE, "0").equals("1")) {
            Message msg = new Message();
            msg.arg1 = 0;
            handler.sendMessageDelayed(msg, 1000);
        }
    }

    @OnClick({R.id.ll_device, R.id.ll_engineer, R.id.ll_news, R.id.ll_server, R.id.ll_knowledge})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_device:
                if (!shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
                    IntentFormat.startActivity(getActivity(), DeviceWarrantyActivity.class);
                }else{
                    Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_engineer:

                if (shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
                    Map<String, Object> mapP = new HashMap<>();
                    mapP.put(UserFragment.FLAG_ORDER_KEY, 7);
                    IntentFormat.startActivity(getActivity(), OrderActivity.class, mapP);
                }else{
                    Toast.makeText(getActivity(), "您没有权限", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_news:
                IntentFormat.startActivity(getActivity(), NewsActivity.class);
                break;
            case R.id.ll_server:

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_default_popup,null);
                TextView popup_title = view.findViewById(R.id.tv_title);
                popup_title.setText("联系电话");

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

                        onRequestPermission(result);
                    }
                });
                popup.setTextColor("#3F51B5");
                popup.showAtLocation(getActivity().findViewById(R.id.ll_layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_knowledge:
                IntentFormat.startActivity(getActivity(), KnowledgeActivity.class);
                break;
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0:
                    showSpread();
                    break;
                case 1:
                    spreadPopup.dismiss();
                    openRePack(msg.obj+"");
                    break;
                case 2:
                    spreadPopup.dismiss();
                    break;
            }

        }
    };

    public void showSpread(){

        String day= shared.getString(Constant.SHARED.CUR_DAY, "");
        String cur_day =  DateUtil.onDateFormat(DateUtil.DATA_FORMAT_5);

        if (System.currentTimeMillis() / 1000 > Constant.endTime){
            return;
        }
        if (cur_day.equals(day)){
            return;
        }
        shared.edit().putString(Constant.SHARED.CUR_DAY, cur_day).commit();
        spreadPopup = new SpreadPopup(getActivity(), R.layout.layout_spread_main, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spreadPopup.onShowAnim();
                startTime = System.currentTimeMillis();

               onOpenPacket();
            }
        }, new OnPopupListener() {
            @Override
            public void onStatePopupListner(PopupWindow popupWindow, boolean state) {
                popupWindow.dismiss();

            }
        }, 0);
        spreadPopup.showAtLocation(getActivity().findViewById(R.id.ll_layout), Gravity.CENTER, 0 , 0);
    }

    public void openRePack(String point){

        redPacketPopup = new SpreadPopup(getActivity(), R.layout.layout_spread_main, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFormat.startActivity(getActivity(), IntegralActivity.class);
                redPacketPopup.dismiss();
            }
        },null, 1);
        redPacketPopup.setReardPoint(point);
        redPacketPopup.showAtLocation(getActivity().findViewById(R.id.ll_layout), Gravity.CENTER, 0 , 0);
    }

    /**
     * 获取红包所得积分
     */
    public void onOpenPacket(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.CREAT_AT, (System.currentTimeMillis()/1000)+"");
        userService.onOpenRedPacket(getActivity(), param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {


                long curTime = System.currentTimeMillis();
                Message msg = new Message();
                msg.arg1 = 1;
                msg.obj = result.get(ResponseKey.POINTS)+"";
                if (curTime-startTime < 3*1000){
                    Logger.d("延时"+(curTime-startTime));
                    //当在3s内将数据请求到，则延迟2s后结束动画，然后在显示积分
                    handler.sendMessageDelayed(msg, 2 * 1000);
                }else{
                    Logger.d("没有延时"+(curTime-startTime));
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onFailure() {
                long curTime = System.currentTimeMillis();
                Message msg = new Message();
                msg.arg1 = 2;
                if (curTime-startTime < 3*1000){
                    //当在3s内将数据请求到，则延迟2s后结束动画，然后在显示积分
                    handler.sendMessageDelayed(msg, 2 * 1000);
                }else{
                    handler.sendMessage(msg);
                }
            }
        });

    }


    public void getBannerImages(){
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.IMG_TYPE, "1");
        userService.onBannerImage(getActivity(), param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                    try {
                        JSONObject json = new JSONObject(result.get("data")+"");
                        JSONArray jsona =json.optJSONObject(ResponseKey.LIST).optJSONArray(ResponseKey.IMG_PATH);
                        shared.edit().putString(Constant.SHARED.BANNER_PATH, jsona.toString()).commit();
                        mTitles.clear();
                        mImages.clear();
                        for (int i = 0; i <jsona.length() ; i++) {
                            mTitles.add("");
                            mImages.add(Urls.HOST+Urls.GET_IMAGES+ jsona.get(i));
                        }

//                        Logger.d("获取到的图片  "+mImages);
//                        banner.update(mImages, mTitles);
                        banner.setImages(mImages);
                        banner.setBannerTitles(mTitles);
                        banner.releaseBanner();
                        banner.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void onRequestPermission(final String result){//READ_PHONE_STATE

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE, "手机权限", R.drawable.permission_ic_phone));
        HiPermission.create(getActivity()).permissions(permissions).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Logger.d("用户关闭权限申请");
               ToastUtis.onToast("请在设置中打开通话权限");
            }

            @Override
            public void onFinish() {
                Logger.d("所有权限申请完成");
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+result));
                startActivity(intent);
                popup.dismiss();

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
