package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.DataFormat;
import com.example.wsq.android.utils.DateUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2018/1/5.
 */

public class IntegralRecordAdapter  extends RecyclerView.Adapter<IntegralRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mData;

    public IntegralRecordAdapter(Context context, List<Map<String, Object>> list){
        this.mContext = context;
        this.mData = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_integral_record_item, parent, false);
        return new IntegralRecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int state = (int) mData.get(position).get(ResponseKey.STATE);
        String str = mData.get(position).get(ResponseKey.POINTS_ACCOUNT)+"";
        int num = DataFormat.onStringForInteger(str);;
        if (state==1){
            holder.tv_integral_type.setText("签到 +" + num);
        }else if(state ==2 ){
            holder.tv_integral_type.setText("服务单 +" + num);
        }else if(state ==3 ){
            holder.tv_integral_type.setText("注册 +" +num);
        }else if(state ==4 ){
            holder.tv_integral_type.setText("兑换 -" +num);
        }else if(state ==5 ){
            holder.tv_integral_type.setText("红包 +" +num);
        }


        holder.tv_integral_time.setText(
                DateUtil.onMillisForDate(mData.get(position).get(ResponseKey.CREAT_AT)+"000", DateUtil.DATA_FORMAT_5));


        int integral = DataFormat.onStringForInteger(mData.get(position).get(ResponseKey.SUM_POINTS)+"");
        holder.tv_integral_num.setText(integral+" 分");

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_integral_type;
        private TextView tv_integral_time, tv_integral_num;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_integral_type = itemView.findViewById(R.id.tv_integral_type);
            tv_integral_time = itemView.findViewById(R.id.tv_integral_time);
            tv_integral_num = itemView.findViewById(R.id.tv_integral_num);
        }
    }
}
