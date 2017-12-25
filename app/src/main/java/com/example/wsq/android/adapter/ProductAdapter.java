package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wsq.android.R;
import com.example.wsq.android.activity.ProductInfoActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.constant.Urls;
import com.example.wsq.android.utils.IntentFormat;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wsq on 2017/12/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    private int [] images = {R.drawable.images_product_1,R.drawable.images_product_2,R.drawable.images_product_3,
            R.drawable.images_product_4, R.drawable.images_product_5, R.drawable.images_product_6};
    Context mContext;
    List<Map<String, Object>> mData;
    Random mRandom;

    public ProductAdapter(Context context, List<Map<String, Object>> list){

        this.mContext = context;
        this.mData = list;
        mRandom = new Random();
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProductAdapter.MyViewHolder holder = new ProductAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_product_item, parent,
                false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {

        holder.tv_time.setText(mData.get(position).get(ResponseKey.UPDATE_AT).toString()+"");
        holder.tv_content.setText(mData.get(position).get(ResponseKey.DES).toString()+"");
        holder.tv_product_title.setText(mData.get(position).get(ResponseKey.TITLE).toString()+"");
        int num = mRandom.nextInt(mData.size());
        String url = mData.get(position).get(ResponseKey.THUMB).toString();
        if (!TextUtils.isEmpty(url)){
            Glide.with(mContext).load(Urls.HOST+Urls.GET_IMAGES+url).into(holder.iv_product);
        }else{
            holder.iv_product.setImageResource(images[num]);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_product;
        private TextView tv_time, tv_content, tv_product_title;
        private LinearLayout ll_product_Info;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_product = itemView.findViewById(R.id.iv_product);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_product_title = itemView.findViewById(R.id.tv_product_title);
            ll_product_Info = itemView.findViewById(R.id.ll_product_Info);
            ll_product_Info.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            IntentFormat.startActivity(mContext, ProductInfoActivity.class, mData.get(getPosition()));
        }
    }


}
