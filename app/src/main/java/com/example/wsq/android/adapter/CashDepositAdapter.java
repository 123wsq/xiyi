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

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class CashDepositAdapter extends RecyclerView.Adapter<CashDepositAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnDefaultClickListener mClickListener;
    public CashDepositAdapter(Context context, List<Map<String, Object>> list, OnDefaultClickListener clickListener){
        this.mContext = context;
        this.mData = list;
        this.mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_cash_deposit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_money.setText(mData.get(position).get(ResponseKey.MONEY)+"");
        holder.tv_apply_time.setText(mData.get(position).get(ResponseKey.CREAT_AT)+"");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_money;
        TextView tv_apply_time;
        LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_apply_time = itemView.findViewById(R.id.tv_apply_time);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            ll_layout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            mClickListener.onClickListener(getPosition());
        }
    }
}
