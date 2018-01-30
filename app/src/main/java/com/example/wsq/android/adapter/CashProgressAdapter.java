package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.DeviceFragment;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class CashProgressAdapter extends RecyclerView.Adapter<CashProgressAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private int selectPosition  = 0;
    public CashProgressAdapter(Context context, List<Map<String, Object>> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_cash_progress_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position==0){
            holder.view_top.setVisibility(View.INVISIBLE);
            holder.view_down.setVisibility(View.VISIBLE);
            holder.view_down.setBackgroundResource(R.color.defalut_title_color);
        }else if(position == mData.size() -1){
            holder.view_top.setVisibility(View.VISIBLE);
            holder.view_down.setVisibility(View.INVISIBLE);
            holder.view_top.setBackgroundResource((Boolean)mData.get(position).get("up") == true ? R.color.defalut_title_color : R.color.color_gray);
        }else{
            holder.view_top.setVisibility(View.VISIBLE);
            holder.view_down.setVisibility(View.VISIBLE);
            holder.view_top.setBackgroundResource((Boolean)mData.get(position).get("up") == true ? R.color.defalut_title_color : R.color.color_gray);
            holder.view_down.setBackgroundResource((Boolean)mData.get(position).get("down") == true ? R.color.defalut_title_color : R.color.color_gray);
        }
        if (position == mData.size() -1){
            holder.iv_progress.setImageResource((Boolean)mData.get(position).get("up") == true ? R.drawable.image_cash_progress : R.drawable.image_chsh_unchecked);

        }else{
            holder.iv_progress.setImageResource((Boolean)mData.get(position).get("up") == true ? R.drawable.image_cash_progress : R.drawable.image_celector_uncheck);

        }
        holder.tv_progress.setText(mData.get(position).get("msg")+"");
        holder.tv_apply_time.setText(mData.get(position).get("time")+"");

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view_top, view_down;
        ImageView iv_progress;
        TextView tv_progress;
        TextView tv_apply_time;


        public ViewHolder(View itemView) {
            super(itemView);
            view_top = itemView.findViewById(R.id.view_top);
            view_down = itemView.findViewById(R.id.view_down);
            iv_progress = itemView.findViewById(R.id.iv_progress);
            tv_progress = itemView.findViewById(R.id.tv_progress);
            tv_apply_time = itemView.findViewById(R.id.tv_apply_time);
        }


    }
}
