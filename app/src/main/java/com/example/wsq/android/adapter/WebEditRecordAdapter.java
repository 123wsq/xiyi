package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.bean.ContentType;
import com.example.wsq.android.bean.WebContentBean;
import com.example.wsq.android.inter.OnRecycViewLongClicklistener;
import com.example.wsq.android.utils.DateUtil;
import com.example.wsq.android.utils.ToastUtis;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/16.
 */

public class WebEditRecordAdapter extends RecyclerView.Adapter<WebEditRecordAdapter.ViewHolder>{

    private Context mContext;
    private List<Map.Entry<String, WebContentBean>> mData;
    private OnRecycViewLongClicklistener clicklistener;
    public WebEditRecordAdapter(Context context, List<Map.Entry<String, WebContentBean>> map, OnRecycViewLongClicklistener clicklistener){
        this.mContext = context;
        this.mData = map;
        this.clicklistener = clicklistener;

    }

    @Override
    public WebEditRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_record_item, parent, false);
        return new WebEditRecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WebEditRecordAdapter.ViewHolder holder, final int position) {

        final WebContentBean bean = mData.get(position).getValue();
        holder.tv_fileType.setText(bean.getType().getName());

        long time = Long.parseLong(bean.getAddTime()+"");
        if (time<1000){
            holder.tv_create_time.setText("");
        }else {
            holder.tv_create_time.setText(DateUtil.onMillisForDate(bean.getAddTime() + "", DateUtil.DATA_FORMAT_1));
        }
        holder.tv_create_content.setText(bean.getContent());


        holder.ll_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(bean.getType() == ContentType.TITLE || bean.getType()== ContentType.CREATE_TIME){
                    ToastUtis.onToast("该项不可编辑");
                }else {
                    clicklistener.onLongClickListener(mData.get(position).getKey(), position, bean.getAddTime());
                }

                return false;
            }
        });
        holder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bean.getType() == ContentType.TITLE || bean.getType()== ContentType.CREATE_TIME){
                    ToastUtis.onToast("该项不可编辑");
                }else {
                    ToastUtis.onToast("长按对此项编辑");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fileType;
        TextView tv_create_time;
        TextView tv_create_content;
        LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_fileType = itemView.findViewById(R.id.tv_fileType);
            tv_create_time = itemView.findViewById(R.id.tv_create_time);
            tv_create_content = itemView.findViewById(R.id.tv_create_content);
            ll_layout = itemView.findViewById(R.id.ll_layout);

//            WebContentBean bean = mData.get(getPosition()).getValue();

        }
    }
}
