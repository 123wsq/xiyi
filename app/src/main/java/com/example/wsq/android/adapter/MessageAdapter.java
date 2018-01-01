package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.order.OrderMessageInfoActivity;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.fragment.DeviceFragment;
import com.example.wsq.android.utils.IntentFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context mContext;
    private DeviceFragment mFragment;
    private List<Map<String, Object>> mData;
    public MessageAdapter(Context context, List<Map<String, Object>> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_message_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_model.setText(mData.get(position).get(ResponseKey.XINGHAO).toString());
        holder.tv_content.setText(mData.get(position).get(ResponseKey.DETAIL).toString());
        holder.tv_time.setText(mData.get(position).get(ResponseKey.CREATE_AT).toString());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_model, tv_content, tv_time;
        LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_model = itemView.findViewById(R.id.tv_model);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);

            ll_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            IntentFormat.startActivity(mContext, OrderMessageInfoActivity.class, mData.get(getPosition()));
        }
    }
}
