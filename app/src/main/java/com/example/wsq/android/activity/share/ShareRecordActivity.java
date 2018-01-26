package com.example.wsq.android.activity.share;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.ProductAdapter;
import com.example.wsq.android.base.BaseActivity;
import com.example.wsq.android.bean.AuthType;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.db.dao.impl.MaterialImpl;
import com.example.wsq.android.db.dao.inter.DbConInter;
import com.example.wsq.android.inter.HttpResponseListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.utils.ToastUtis;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.LoddingDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wsq on 2018/1/16.
 */

public class ShareRecordActivity extends BaseActivity{
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.iv_add) ImageView iv_add;
    @BindView(R.id.rv_RecyclerView) SwipeMenuRecyclerView rv_RecyclerView;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;
    @BindView(R.id.tv_content)TextView tv_content;

    private DbConInter conInter;
    private ProductAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private LoddingDialog dialog;
    private UserService userService;
    private SharedPreferences shared;

    @Override
    public int getByLayoutId() {
        return R.layout.layout_share_record;
    }

    @Override
    public void init() {

        tv_title.setText("我的资料");
        tv_content.setText("您还没有编辑过资料呢~");
        mData = new ArrayList<>();
        dialog = new LoddingDialog(this);
        userService = new UserServiceImpl();
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        iv_add.setImageResource(R.drawable.image_edit);
        iv_add.setVisibility(View.VISIBLE);
        conInter = new MaterialImpl();

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 10),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ProductAdapter(this, mData, Constant.INFO_3);


        rv_RecyclerView.setItemViewSwipeEnabled(false);
        rv_RecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        rv_RecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
//        rv_RecyclerView.set
        rv_RecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGetShareRecord();
    }

    @OnClick({R.id.iv_back, R.id.iv_add, R.id.tv_refresh})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_add:

                if (conInter.selectCount(this, AuthType.ARTICLES) > 0){
                    onAlertDialog();
                }else {
                    IntentFormat.startActivity(this, EditWebSettingActivity.class);
                }
                break;
            case R.id.tv_refresh:
                onGetShareRecord();
                break;
        }
    }



    public void onAlertDialog(){

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您还有未编辑完的资料，是否继续编辑");
        builder.setOkBtn("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                IntentFormat.startActivity(ShareRecordActivity.this, WebEditActivity.class);
                dialogInterface.dismiss();
            }
        });
        builder.setCancelBtn("重新编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                conInter.removeData(ShareRecordActivity.this, AuthType.ARTICLES);
                conInter.onClearAttachment(ShareRecordActivity.this, AuthType.ARTICLES);
                IntentFormat.startActivity(ShareRecordActivity.this, EditWebSettingActivity.class);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;


            SwipeMenuItem deleteItem = new SwipeMenuItem(ShareRecordActivity.this)
                    .setBackgroundColor(Color.RED)
//                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到左侧。
        }
    };

    /**
     * 点击事件
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener(){
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {

            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                onRemove(adapterPosition);
//                Toast.makeText(ShareRecordActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                //
//                Toast.makeText(ShareRecordActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();

            }
        }
    };

    /**
     * 获取自定义资料列表
     */
    public void onGetShareRecord(){

        dialog.show();
        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        userService.onGetShareRecordList(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.ARTICLES_LIST);
                mData.clear();
                mData.addAll(list);

                if (mData.size() > 0){
                    rv_RecyclerView.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.GONE);
                }else{
                    rv_RecyclerView.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
                if(dialog.isShowing() ) dialog.dismiss();
            }

            @Override
            public void onFailure() {
                ToastUtis.onToast("请求失败");
                if(dialog.isShowing() ) dialog.dismiss();
            }
        });
    }

    /**
     * 删除资料
     * @param position
     */
    public void onRemove( final int position){
        final Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.TOKEN, shared.getString(Constant.SHARED.TOKEN, ""));
        param.put(ResponseKey.ID, mData.get(position).get(ResponseKey.ID)+"");
        userService.onRemoveShare(this, param, new HttpResponseListener() {
            @Override
            public void onSuccess(Map<String, Object> result) {

                mData.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
