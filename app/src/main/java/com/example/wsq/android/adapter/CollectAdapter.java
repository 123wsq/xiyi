package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.CollectActivity;
import com.example.wsq.android.activity.ProductInfoActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.utils.IntentFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wsq on 2017/12/18.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.MyViewHolder>{

    private int [] images = {R.drawable.images_product_1,R.drawable.images_product_2,R.drawable.images_product_3,
            R.drawable.images_product_4, R.drawable.images_product_5, R.drawable.images_product_6};
    Context mContext;
    List<Map<String, Object>> mData;
    Random mRandom;
    private Map<Integer, Boolean> isSelect;
    private boolean isEditCollect = false;
    private String curCollect = "1";//0 表示为资料中进入   1表示我的收藏中进入  因为在显示资料的时候传入的id是不一样的

    public CollectAdapter(Context context, List<Map<String, Object>> list){

        this.mContext = context;
        this.mData = list;
        mRandom = new Random();
        isSelect = new HashMap<>();


    }

    @Override
    public CollectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectAdapter.MyViewHolder holder = new CollectAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_product_item, parent,
                false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(CollectAdapter.MyViewHolder holder, int position) {

        if (!isSelect.containsKey(mData.get(position).get(ResponseKey.ID))){
            isSelect.put((int)mData.get(position).get(ResponseKey.ID), false);
        }
        holder.tv_time.setText(mData.get(position).get(ResponseKey.UPDATE_AT).toString()+"");
        holder.tv_content.setText(mData.get(position).get(ResponseKey.DES).toString()+"");
        holder.tv_product_title.setText(mData.get(position).get(ResponseKey.TITLE).toString()+"");
        int num = mRandom.nextInt(mData.size());
        String url = mData.get(position).get(ResponseKey.THUMB).toString();
        if (!TextUtils.isEmpty(url)){
            RequestOptions options = new RequestOptions();
            options.error(R.drawable.image_no);
            options.fallback(R.drawable.image_no);
            options.placeholder(R.drawable.image_no);
            Glide.with(mContext)
                    .load(Urls.HOST+Urls.GET_IMAGES+url)
                    .apply(options)
                    .into(holder.iv_product);
//            Glide.with(mContext).load(Urls.HOST+Urls.GET_IMAGES+url).into(holder.iv_product);
        }else{
            holder.iv_product.setImageResource(images[num]);
        }

        if (isEditCollect) {
            holder.cb_checkBox.setVisibility(View.VISIBLE);
            //判断是否选中状态
            int id = (int) mData.get(position).get(ResponseKey.ID);
            Log.d("选中状态", isSelect.get(id) + "");
            holder.cb_checkBox.setChecked(isSelect.get(id));
        }else{
            holder.cb_checkBox.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private ImageView iv_product;
        private TextView tv_time, tv_content, tv_product_title;
        private LinearLayout ll_product_Info;
        private CheckBox cb_checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_product = itemView.findViewById(R.id.iv_product);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_product_title = itemView.findViewById(R.id.tv_product_title);
            ll_product_Info = itemView.findViewById(R.id.ll_product_Info);
            cb_checkBox = itemView.findViewById(R.id.cb_checkBox);
            ll_product_Info.setOnClickListener(this);
            cb_checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            Map<String, Object> map = mData.get(getPosition());
            map.put(CollectActivity.COLLECT, curCollect);
            IntentFormat.startActivity(mContext, ProductInfoActivity.class, map);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isSelect.put((int)mData.get(getPosition()).get(ResponseKey.ID), isChecked);
        }
    }


    /**
     * 设置是可编辑的
     * @param isEdit
     */
    public void onEditCollect( boolean isEdit){
        this.isEditCollect = isEdit;
        notifyDataSetChanged();
    }
    /**
     * 设置全部选中
     * @param isAll
     */
    public void onAllSelected(boolean isAll){

        if (isAll){
            for (int i = 0; i < mData.size(); i++) {
                isSelect.put((int)mData.get(i).get(ResponseKey.ID), true);
            }
        }else{
            for (int i = 0; i < mData.size(); i++) {
                isSelect.put((int)mData.get(i).get(ResponseKey.ID), false);
            }
        }

        notifyDataSetChanged();
    }


    /**
     * 获取选中的项
     * @return
     */
    public List<String> getSelected(){
        List<String> list = new ArrayList<>();
        Iterator<Map.Entry<Integer, Boolean>> it = isSelect.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Integer, Boolean> entry = it.next();
            if (entry.getValue()){

                list.add(entry.getKey()+"");
            }
        }
        return list;
    }
}
