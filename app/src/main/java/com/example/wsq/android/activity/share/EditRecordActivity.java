package com.example.wsq.android.activity.share;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.WebEditRecordAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.WebKeys;
import com.example.wsq.android.db.dao.impl.MaterialImpl;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.inter.OnRecycViewLongClicklistener;
import com.example.wsq.android.inter.PopupItemListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.HtmlContent;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.CustomPopup;
import com.example.wsq.android.view.LoddingDialog;
import com.example.wsq.plugin.okhttp.OkhttpUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/16.
 */

public class EditRecordActivity extends BaseActivity{

    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;


//    public static final int FLAG_REMOVE = 0X0000101;
    public static final int FLAG_EDIT = 102;
    public static final int FLAG_BACK = 103;
    public static final int FLAG_INSERT = 104;
    public static final int FLAG_FINISH = 105;

    private UserService userService;
    private LoddingDialog dialog;
    private WebEditRecordAdapter mAdapter;
    private List<Map.Entry<String, WebContentBean>> mData;

    private CustomPopup popup;
    private String removeKey = ""; //删除的key
    private DbConInter conInter;
    private SharedPreferences shared;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_edit_record;
    }

    @Override
    public void init() {

        tv_title.setText("编辑记录");
        mData = new ArrayList<>();
        userService = new UserServiceImpl();
        dialog = new LoddingDialog(this);
        conInter = new MaterialImpl();
        Map<String, WebContentBean> map = (Map<String, WebContentBean>) getIntent().getSerializableExtra("data");
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);


        //排序
        List<Map.Entry<String, WebContentBean>> newMap = new ArrayList<Map.Entry<String, WebContentBean>>(map.entrySet());
        Collections.sort(newMap, new Comparator<Map.Entry<String, WebContentBean>>() {
            public int compare(Map.Entry<String, WebContentBean> o1, Map.Entry<String, WebContentBean> o2) {
                return (o1.getValue().getAddTime()+"").toString().compareTo(o2.getValue().getAddTime()+"");
            }
        });


        mData.addAll(newMap);
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);
        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 10),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));

        mAdapter = new WebEditRecordAdapter(this, mData, clicklistener);
        rv_RecyclerView.setAdapter(mAdapter);

    }

    @OnClick({R.id.iv_back, R.id.start_create})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                onResultActivity(FLAG_BACK, removeKey, -1, -1);
                break;
            case R.id.start_create:  //开始创建
                onCreateAlertDialog();
                break;
        }
    }

    OnRecycViewLongClicklistener clicklistener = new OnRecycViewLongClicklistener() {
        @Override
        public void onLongClickListener(String key, int position, long addTime) {

            onInitPopup(key, position, addTime);

        }
    };


    /**
     * 初始化
     * @param key
     * @param pos
     * @param addTime
     */
    public void onInitPopup(final String key, final int pos, final long addTime){
        View v = LayoutInflater.from(this).inflate(R.layout.layout_default_popup,null);
        List<String> list = new ArrayList<>();
        WebContentBean bean = mData.get(pos).getValue();

//        if (bean.getType() != ContentType.CONTENT_IMG){
            list.add("编辑");
//        }
//        if (bean.getType() != ContentType.CREATE_TIME && bean.getType() != ContentType.TITLE) {
            list.add("删除");
//        }
        list.add("插入");
        popup = new CustomPopup(this, v, list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        }, new PopupItemListener() {
            @Override
            public void onClickItemListener(int position, String result) {
                switch (position){
                    case 0:

                        onResultActivity(FLAG_EDIT, key, -1, -1);
                        break;
                    case 1:

                        conInter.removeData(EditRecordActivity.this, key,AuthType.ARTICLES);
                        mData.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //判断前后时间差
                        //取选中的上一个的时间差
                        long num = -1;
                        if (pos>0){
                            num = mData.get(pos -1).getValue().getAddTime();
                        }else if(pos == 0){
                            num = 0;
                        }



                        onResultActivity(FLAG_INSERT, "", addTime, num);
                        break;
                }
                popup.dismiss();
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode== KeyEvent.KEYCODE_BACK){
            onResultActivity(FLAG_BACK, removeKey, -1, -1);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreateAlertDialog(){
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

    /**
     * 开始创建资料
     */
    public void onCreateWeb() throws Exception {

        dialog.show();

        //取出所有的内容
        Map<String, WebContentBean> map = conInter.selectAll(EditRecordActivity.this, AuthType.ARTICLES);


        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.TITLE, map.get(WebKeys.TITLE).getContent());
        param.put(ResponseKey.CID, map.get(WebKeys.ARTICLES).getContent()+"");
//        param.put(ResponseKey.THUMB, map.get(WebKeys.ICON).getContent());
        param.put(ResponseKey.CAT, AuthType.ARTICLES.getIndex()+"");
        param.put(ResponseKey.DES, map.get(WebKeys.INTRODUCTION).getContent());

        //定义内容
        String conetnt = "";
        String addachment= "";

        //排序
        List<Map.Entry<String, WebContentBean>> newMap = new ArrayList<Map.Entry<String, WebContentBean>>(map.entrySet());

        Collections.sort(newMap, new Comparator<Map.Entry<String, WebContentBean>>() {
            public int compare(Map.Entry<String, WebContentBean> o1, Map.Entry<String, WebContentBean> o2) {
                return (o1.getValue().getAddTime()+"").toString().compareTo(o2.getValue().getAddTime()+"");
            }
        });

        for (Map.Entry<String, WebContentBean> entry : newMap){
            WebContentBean bean = entry.getValue();
            if (bean.getType() == ContentType.CONTENT_FONT){

                if (TextUtils.isEmpty(conetnt)){
                    conetnt = HtmlContent.onContentHtml(bean.getContent(), Constant.CONTENT_COLOR, Constant.CONTENT_SIZE);
                }else {
                    conetnt += HtmlContent.onContentHtml(bean.getContent(), Constant.CONTENT_COLOR, Constant.CONTENT_SIZE);
                }
            }else if (bean.getType() == ContentType.CONTENT_IMG){

                if (TextUtils.isEmpty(conetnt)){
                    conetnt = HtmlContent.onImageHtml(bean.getContent());
                }else {
                    conetnt += HtmlContent.onImageHtml(bean.getContent());
                }
            }else if (bean.getType() == ContentType.ADDACHMENT){

                if (TextUtils.isEmpty(addachment)){
                    addachment = HtmlContent.onAddachmentHtml(bean.getContent(), bean.getFileName());
                }else {
                    addachment += HtmlContent.onAddachmentHtml(bean.getContent(), bean.getFileName());
                }
            }
        }
//        Iterator<Map.Entry<String, WebContentBean>> it = map.entrySet().iterator();
//
//        while (it.hasNext()){
//            Map.Entry<String, WebContentBean> entry = it.next();
//
//        }


        param.put(ResponseKey.CONTENT, conetnt + "</br>" + addachment);

        List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> images = new HashMap<>();
        images.put("fileType", OkhttpUtil.FILE_TYPE_IMAGE);
        File file = new File(map.get(WebKeys.ICON).getContent());
        Logger.d("icon的本地路径   "+map.get(WebKeys.ICON).getContent());
        images.put(ResponseKey.THUMB, file);
        fileList.add(images);

        userService.onCreateShare(this, param, fileList, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                conInter.removeAll(EditRecordActivity.this, AuthType.ARTICLES);
                onResultActivity(FLAG_FINISH, "", -1, -1);
                if (dialog.isShowing()) dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure() {

                if (dialog.isShowing()) dialog.dismiss();
            }
        });


    }

    /**
     *
     * @param resultCode 返回码
     * @param key  选中的key
     * @param addTime  选中的时间
     * @param upTime   选中上一个的时间，在插入数据的时候进行计算时间
     */
    public void onResultActivity(int resultCode, String key, long addTime, long upTime){

        Intent intent = new Intent();
        intent.putExtra("key", key);
        if (resultCode == FLAG_INSERT){
            intent.putExtra("time", addTime);
            intent.putExtra("upTime", upTime);
        }
        setResult(resultCode, intent);
        finish();
    }
}
