package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.utils.DateUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnDefaultClickListener mListener;
    public ReceiptsAdapter(Context context, List<Map<String, Object>> list, OnDefaultClickListener clickListener){
        this.mContext = context;
        this.mData = list;
        this.mListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_receipts_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_order_num.setText(mData.get(position).get(ResponseKey.ORDER_NO)+"");
        holder.tv_device_model.setText(mData.get(position).get(ResponseKey.XINGHAO)+"");
        String time = DateUtil.OnSecondForDate(
                mData.get(position).get(ResponseKey.CREAT_AT)+"", DateUtil.DATA_FORMAT_1);
        holder.tv_finish_time.setText(time);
        holder.tv_money.setText(mData.get(position).get(ResponseKey.MOENY)+"");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_order_num;
        TextView tv_finish_time;
        TextView tv_device_model;
        TextView tv_money;
        LinearLayout ll_layout;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_finish_time = itemView.findViewById(R.id.tv_finish_time);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_device_model = itemView.findViewById(R.id.tv_device_model);
            tv_money = itemView.findViewById(R.id.tv_money);

            ll_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mListener.onClickListener(getPosition());
        }
    }
}
