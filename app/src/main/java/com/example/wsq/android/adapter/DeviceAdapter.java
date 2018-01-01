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
import com.example.wsq.android.activity.order.DeviceInfoActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.IntentFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    public DeviceAdapter(Context context, List<Map<String, Object>> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_default_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).get(ResponseKey.TITLE)+"");
        holder.iv_next.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        ImageView iv_next;
        LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_next = itemView.findViewById(R.id.iv_next);
            ll_layout = itemView.findViewById(R.id.ll_layout);

            ll_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            IntentFormat.startActivity(mContext, DeviceInfoActivity.class, mData.get(getPosition()));
        }
    }
}
