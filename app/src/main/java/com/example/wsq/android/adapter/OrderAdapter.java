package com.example.wsq.android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.activity.order.OrderInfoActivity;
import com.example.wsq.android.activity.order.ServerOrderInfoActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.IntentFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mData;
    private SharedPreferences shared;

    public OrderAdapter(Context context, List<Map<String, Object>> list){
        this.mData = list;
        this.mContext = context;
        shared = context.getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
    }


    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_order_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {

        holder.tv_ordernum.setText(mData.get(position).get(ResponseKey.ORDER_NO).toString());
        holder.tv_model.setText(mData.get(position).get(ResponseKey.XINGHAO).toString());
        holder.tv_outnum.setText(mData.get(position).get(ResponseKey.BIANHAO).toString());
        holder.tv_time.setText(mData.get(position).get(ResponseKey.CREATE).toString());
        String status = mData.get(position).get(ResponseKey.STATUS).toString();

        if (shared.getString(Constant.SHARED.JUESE,"").equals("1")){
            if (status.equals("2")){
                holder.tv_status.setText("已分配");
            }else if (status.equals("3")){
                holder.tv_status.setText("处理中");
            }else if (status.equals("4")){
                holder.tv_status.setText("完成(待提交完成反馈报告)");
            }else if (status.equals("5")){
                holder.tv_status.setText("已移交(待提交移交反馈报告)");
            }else if (status.equals("6")){
                holder.tv_status.setText("已提交完成反馈(待审核)");
            }else if (status.equals("6.1")){
                holder.tv_status.setText("重写完成反馈(需重写)");
            }else if (status.equals("7")){
                holder.tv_status.setText("已提交移交反馈");
            }else if (status.equals("7.1")){
                holder.tv_status.setText("重写移交反馈报告(待审核)");
            }else if (status.equals("8")){
                holder.tv_status.setText("已结束");
            }else if (status.equals("8.1") || status.equals("8.2")){
                holder.tv_status.setText("已完成");
            }
        }else if(shared.getString(Constant.SHARED.JUESE,"").equals("2")
                || shared.getString(Constant.SHARED.JUESE,"").equals("3")){

            if (status.equals("-1")){
                holder.tv_status.setText("待评估");
            }else if (status.equals("0")){
                holder.tv_status.setText("未审核");
            }else if (status.equals("1")){
                holder.tv_status.setText("已通过");
            }else if (status.equals("1.1")){
                holder.tv_status.setText("未通过");
            }else if (status.equals("2")){
                holder.tv_status.setText("处理中");
            }else if (status.equals("2")){
                holder.tv_status.setText("完成");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_model, tv_ordernum, tv_outnum, tv_time, tv_status;
        private LinearLayout ll_order_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_ordernum = itemView.findViewById(R.id.tv_ordernum);
            tv_outnum = itemView.findViewById(R.id.tv_outnum);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_order_layout = itemView.findViewById(R.id.ll_order_layout);
            ll_order_layout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

//            Log.i("您点击的是：", getPosition()+"");
            Map<String, Object> map = mData.get(getPosition());
            if (shared.getString(Constant.SHARED.JUESE, "").endsWith("1")){

                if (map.get(ResponseKey.STATUS).equals("6")
                        ||map.get(ResponseKey.STATUS).equals("6.1")
                        ||map.get(ResponseKey.STATUS).equals("7")
                        ||map.get(ResponseKey.STATUS).equals("7.1")
                        || map.get(ResponseKey.STATUS).equals("8")
                        || map.get(ResponseKey.STATUS).equals("8.1")
                        || map.get(ResponseKey.STATUS).equals("8.2")){
                    IntentFormat.startActivity(mContext, ServerOrderInfoActivity.class, map);
                }else {
                    IntentFormat.startActivity(mContext, OrderInfoActivity.class, map);
                }
            }else {
                IntentFormat.startActivity(mContext, OrderInfoActivity.class, map);
            }
        }
    }

}
