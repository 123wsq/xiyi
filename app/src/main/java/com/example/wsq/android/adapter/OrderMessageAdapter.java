package com.example.wsq.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.TelPhoneValidate;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/18.
 */

public class OrderMessageAdapter extends RecyclerView.Adapter<OrderMessageAdapter.MyViewHolder>{

    Context mContext;
    List<Map<String, Object>> mData;

    public OrderMessageAdapter(Context context, List<Map<String, Object>> list){

        this.mContext = context;
        this.mData = list;


    }

    @Override
    public OrderMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderMessageAdapter.MyViewHolder holder = new OrderMessageAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_order_message_item, parent,
                false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(OrderMessageAdapter.MyViewHolder holder, int position) {

        TelPhoneValidate.onGetTelCode(mContext, mData.get(position).get(ResponseKey.DETAIL).toString()+"", holder.tv_content, R.color.default_bule);
        holder.tv_time.setText(mData.get(position).get(ResponseKey.CREATE_AT).toString()+"");

        if (mData.size() != 1) {
            if (position == 0) {
                holder.view_top.setVisibility(View.INVISIBLE);
                holder.view_down.setVisibility(View.VISIBLE);
            } else if (mData.size() - 1 == position) {
                holder.view_top.setVisibility(View.VISIBLE);
                holder.view_down.setVisibility(View.INVISIBLE);
            } else {
                holder.view_top.setVisibility(View.VISIBLE);
                holder.view_down.setVisibility(View.VISIBLE);
            }
        }else{
            holder.view_top.setVisibility(View.INVISIBLE);
            holder.view_down.setVisibility(View.INVISIBLE);
        }
        if (position == 0){
            holder.tv_content.setTextColor(mContext.getResources().getColor(R.color.RED));
            holder.iv_cur_state.setVisibility(View.VISIBLE);
            holder.iv_state.setVisibility(View.GONE);
        }else{
            holder.tv_content.setTextColor(Color.parseColor("#1fa301"));
            holder.iv_cur_state.setVisibility(View.GONE);
            holder.iv_state.setVisibility(View.VISIBLE);
        }



    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_time, tv_content;
        private View view_top, view_down;
        private ImageView iv_state, iv_cur_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            view_down = itemView.findViewById(R.id.view_down);
            view_top = itemView.findViewById(R.id.view_top);
            iv_cur_state = itemView.findViewById(R.id.iv_cur_state);
            iv_state = itemView.findViewById(R.id.iv_state);

        }

    }
}
