package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.OnAttachmentDeleteListener;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnAttachmentDeleteListener mListener;
    private int selectPosition  = 0;
    public AttachmentAdapter(Context context, List<Map<String, Object>> list, OnAttachmentDeleteListener listener){
        this.mContext = context;
        this.mData = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_addachment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_atta_name.setText(mData.get(position).get(ResponseKey.NAME)+"");

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_atta_name;
        ImageView iv_atta_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_atta_name = itemView.findViewById(R.id.tv_atta_name);
            iv_atta_delete = itemView.findViewById(R.id.iv_atta_delete);

            iv_atta_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            selectPosition = getPosition();
            if (mListener!= null){
                mListener.onDeleteAttachmentListener(selectPosition, mData.get(selectPosition));
            }
        }
    }
}
