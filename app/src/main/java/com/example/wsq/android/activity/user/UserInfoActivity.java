package com.example.wsq.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.order.DeviceWarrantyActivity;
import com.example.wsq.android.adapter.SkillAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.SkillBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.inter.OnRecycCheckListener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.BitmapUtils;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.FileSizeUtil;
import com.example.wsq.android.utils.ImageUtil;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ToastUtils;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.android.view.RoundImageView;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2017/12/13.
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_username) TextView tv_username;
    @BindView(R.id.tv_sex) TextView tv_sex;
    @BindView(R.id.tv_tel) TextView tv_tel;
    @BindView(R.id.tv_xueli) TextView tv_xueli;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.image_header) RoundImageView image_header;
    @BindView(R.id.et_company) EditText et_company;
    @BindView(R.id.et_bumen) EditText et_bumen;
    @BindView(R.id.et_email) EditText et_email;
    @BindView(R.id.et_jieshao) EditText et_jieshao;
    @BindView(R.id.et_hope) EditText et_hope;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.ll_hope) LinearLayout ll_hope;
    @BindView(R.id.ll_skill) LinearLayout ll_skill;
    @BindView(R.id.ll_jieshao ) LinearLayout ll_jieshao;
    @BindView(R.id.ll_bumen ) LinearLayout ll_bumen;
    @BindView(R.id.tv_ratio) TextView tv_ratio;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;

    public static final int DEFAULT_SKILL_RESULT = 0x000001;  //数据返回

    private UserService userService;
    private SharedPreferences shared;
    private int sex = 1;
    private int xueli = 1;
    private CustomPopup popup;

    private String headerImage = "";
    private LoddingDialog dialog;
    private RequestOptions options;
    private SkillAdapter mAdapter;
    private List<SkillBean> mData;


    @Override
    public int getByLayoutId() {
        return R.layout.layout_user_info;
    }

    public void init() {

        userService = new UserServiceImpl();
        mData = new ArrayList<>();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);

        tv_title.setText("个人资料");
        dialog = new LoddingDialog(this);

        tv_username.setText(UserFragment.mUserData.get(ResponseKey.USERNAME)+"");
        //设置头像
        options = new RequestOptions();
        options.error(R.drawable.image_header_bg);
        options.fallback(R.drawable.image_header_bg);
        options.placeholder(R.drawable.image_header_bg);
        String pic = UserFragment.mUserData.get(ResponseKey.USER_PIC)+"";
        if (!TextUtils.isEmpty(pic)) {
            Glide.with(this)
                    .load(Urls.HOST + pic)
                    .apply(options)
                    .into(image_header);
        }
        tv_ratio.setText("资料完整度"+ UserFragment.mUserData.get(ResponseKey.RATIO)+"%");
        String name = UserFragment.mUserData.get(ResponseKey.NAME)+"";
        tv_name.setText( "**"+name.substring(name.length()-1));

        String tel = UserFragment.mUserData.get(ResponseKey.TEL)+"";
        if (!TextUtils.isEmpty(tel)){
            tv_tel.setText(tel.substring(0, 3)+"****"+tel.substring(7)+" 已验证");
        }


        et_bumen.setText(UserFragment.mUserData.get(ResponseKey.BUMEN)+"");
        et_company.setText(UserFragment.mUserData.get(ResponseKey.COMPANY)+"");
        et_email.setText(UserFragment.mUserData.get(ResponseKey.EMAIL)+"");
        et_jieshao.setText(UserFragment.mUserData.get(ResponseKey.JIESHAO)+"");
        String sSex = UserFragment.mUserData.get(ResponseKey.SEX)+"";

        if (null != sSex || sSex.length()!=0){
            sex = Integer.parseInt(sSex);
            tv_sex.setText(Integer.parseInt(sSex)==1 ? "男" : "女");
        }

        String sEduc = UserFragment.mUserData.get(ResponseKey.XUELI)+"";
        if (null != sEduc || sEduc.length()!=0){
            xueli = Integer.parseInt(sEduc);
            tv_xueli.setText(Constant.EDUCATION[Integer.parseInt(sEduc)-1]+"");
        }
        et_hope.setText(UserFragment.mUserData.get(ResponseKey.DIQU)+"");





        //判断角色  角色为服务工程师的时候  是需要期望地区和技能  别的时候是需要部门和介绍
        if(shared.getString(Constant.SHARED.JUESE,"0").equals("1")){
            ll_hope.setVisibility(View.VISIBLE);
            ll_bumen.setVisibility(View.GONE);
            ll_jieshao.setVisibility(View.GONE);
            ll_skill.setVisibility(View.VISIBLE);
            String skill = UserFragment.mUserData.get(ResponseKey.JINENG)+"";

            try {
                JSONArray json = new JSONArray(skill);

                for (int i = 0; i < json.length(); i++) {
                    mData.add(new SkillBean(json.get(i)+""));
                }
            } catch (JSONException e) {
                ToastUtils.onToast(UserInfoActivity.this, "请重新完善您的技能");
                e.printStackTrace();
            }
//            rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
//                    this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 10),
//                    ContextCompat.getColor(this, R.color.default_backgroud_color)));
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) rv_RecyclerView.getLayoutParams();
            param.height = DensityUtil.dp2px(this, mData.size() > 4 ? 50 * 2 : 50);
            rv_RecyclerView.setLayoutParams(param);
            rv_RecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            rv_RecyclerView.setHasFixedSize(true);
            mAdapter = new SkillAdapter(this, mData, null, listener, 1);
            rv_RecyclerView.setAdapter(mAdapter);
        }else {
            ll_hope.setVisibility(View.GONE);
            ll_bumen.setVisibility(View.VISIBLE);
            ll_jieshao.setVisibility(View.VISIBLE);
            ll_skill.setVisibility(View.GONE);
        }
    }

    OnDefaultClickListener listener = new OnDefaultClickListener() {
        @Override
        public void onClickListener(int position) {

            mData.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    };


    @OnClick({R.id.iv_back,R.id.tv_sex, R.id.tv_xueli, R.id.btn_save, R.id.image_header, R.id.iv_skill_add})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_skill_add:
                Intent intent = new Intent(UserInfoActivity.this, SkillServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("skill", (Serializable) mData);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200);
                break;
            case R.id.image_header:

//

                View view = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.layout_default_popup,null);
                List<String> list = new ArrayList<>();
                list.add("选择本地图片");
                list.add("拍照");
                popup = new CustomPopup(UserInfoActivity.this, view, list, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {

                        switch (position){
                            case 0:
                                PictureSelector.create(UserInfoActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
//                                .theme(R.sty)
                                        .maxSelectNum(1)
                                        .isZoomAnim(true)
                                        .imageSpanCount(3)
                                        .isCamera(false)
                                        .previewImage(true)
                                        .compress(true)// 是否压缩 true or false
                                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                                        .forResult(DeviceWarrantyActivity.RESULT_IMAGE);
                                break;
                            case 1:
                                PictureSelector.create(UserInfoActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .imageFormat(PictureMimeType.PNG)
                                .compress(true)// 是否压缩 true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩
                                .forResult(DeviceWarrantyActivity.RESULT_IMAGE);
                                break;
                        }

                        popup.dismiss();
                    }
                });
                popup.setTitle("上传头像");
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.tv_sex:

                View view1 = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.layout_default_popup,null);
                List<String> list1 = new ArrayList<>();

                list1.add("男");
                list1.add("女");

                popup = new CustomPopup(UserInfoActivity.this, view1, list1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener() {
                    @Override
                    public void onClickItemListener(int position, String result) {

                        tv_sex.setText(result);
                        sex = position+1;
                        popup.dismiss();
                    }
                });
                popup.setTitle("性别");
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.tv_xueli:

                View view2 = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.layout_default_popup,null);
                List<String> list2 = new ArrayList<>();
                list2.add("高中及以下");
                list2.add("专科");
                list2.add("本科");
                list2.add("研究生");
                list2.add("博士及以上");
                popup = new CustomPopup(UserInfoActivity.this, view2, list2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                }, new PopupItemListener(){
                    @Override
                    public void onClickItemListener(int position, String result) {
                        tv_xueli.setText(result);
                        xueli = position+1;
                        popup.dismiss();
                    }
                });
                popup.setTitle("学历");
                popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_save:
                updateUserInfo();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            switch (requestCode) {
                case DeviceWarrantyActivity.RESULT_IMAGE:
                    // 图片选择结果回调
                    for (int i = 0; i < selectList.size(); i++) {
                        headerImage = selectList.get(i).getCompressPath();
                    }
                    if (!TextUtils.isEmpty(headerImage)){
                        uploadHeader();
                    }

                    break;

            }
        }else if(resultCode == DEFAULT_SKILL_RESULT){
            List<SkillBean> arrays = (List<SkillBean>) data.getSerializableExtra("skill");


            mData.clear();
            mData.addAll(arrays);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) rv_RecyclerView.getLayoutParams();
            param.height = DensityUtil.dp2px(this, mData.size() > 4 ? 50 *2 : 50 );
            rv_RecyclerView.setLayoutParams(param);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 修改用户信息
     */
    public void updateUserInfo(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.SEX, sex+"");
        param.put(ResponseKey.NAME, UserFragment.mUserData.get(ResponseKey.NAME)+"");
        param.put(ResponseKey.TEL, UserFragment.mUserData.get(ResponseKey.TEL)+"");
        param.put(ResponseKey.XUELI, xueli+"");
        param.put(ResponseKey.EMAIL, et_email.getText().toString());
        param.put(ResponseKey.SFZ, UserFragment.mUserData.get(ResponseKey.SFZ)+"");
        param.put(ResponseKey.COMPANY, et_company.getText().toString());
        JSONArray jsona = new JSONArray();
        for (int i = 0; i < mData.size(); i++) {
            jsona.put(mData.get(i).getSkillName());
        }
        String jn ="";
        if (jsona.length() > 0){
            jn =jsona.toString();
        }
        param.put(ResponseKey.JINENG, jn);
        param.put(ResponseKey.DIQU, et_hope.getText().toString());
        param.put(ResponseKey.BUMEN, et_bumen.getText().toString());
        param.put(ResponseKey.JIESHAO, et_jieshao.getText().toString());

        userService.updateUserInfo(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                Toast.makeText(UserInfoActivity.this,
                        result.get(ResponseKey.MESSAGE)+"", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                finish();
            }
            @Override
            public void onFailure() {

                dialog.dismiss();
            }
        });

    }

    /**
     * 上传用户头像
     */
    public void uploadHeader(){
        Map<String, String> param = new HashMap<>();

        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("fileType", OkhttpUtil.FILE_TYPE_IMAGE);
        File f = new File(headerImage);
        if (f.length()> 8 * 1024* 1024){
            ToastUtis.onToast("单个文件不能超过8M，"+f.getName()+"为 "+ FileSizeUtil.FormetFileSize(f.length()));
            return;
        }
//        File f = new File(Constant.FILE_PATH+"/123456.png");
//        onBitmapCompress(f.getAbsolutePath());
        map.put(ResponseKey.FILE, f);
        list.add(map);
        userService.uploadHeader(this, param, list, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                ToastUtis.onToast(result.get(ResponseKey.MESSAGE)+"");
            }

            @Override
            public void onFailure() {

            }
        });
        Glide.with(this)
                .load(headerImage)
                .apply(options)
                .into(image_header);
    }

    /**
     * 图片添加水印和质量压缩
     * @param path
     * @return
     */
    public  String onBitmapCompress(final String path){
        //得到该路径下的图片bitmap
        Bitmap bitmap = BitmapUtils.getLocalImage(path);
        Bitmap newBitmap = ImageUtil.drawTextToLeftBottom(UserInfoActivity.this, bitmap,
                new String[]{ shared.getString(Constant.SHARED.LOCATION, ""), DateUtil.onDateFormat(DateUtil.DATA_FORMAT)},
                20, Color.RED, 20, 10);
        //只对图片进行质量压缩
//        Bitmap commBitmap = ImageUtil.compressImage(newBitmap, 100);
        //对图片进行大小 质量压缩
//        Bitmap commBitmap = ImageUtil.zoomImage(newBitmap, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this), 100);
        File file = BitmapUtils.saveImage(newBitmap);
        return file.getAbsolutePath();
    }
}
