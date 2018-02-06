package com.example.wsq.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsq.android.R;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.CalendarUtils;
import com.example.wsq.android.utils.DataFormat;
import com.example.wsq.android.utils.DateUtil;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class SignCalendarAdapter extends RecyclerView.Adapter<SignCalendarAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mCander;
    private List<Map<String, Object>> mData;
    private int selectPosition  = 0;
    public SignCalendarAdapter(Context context, List<String> list, List<Map<String, Object>> data){
        this.mContext = context;
        this.mCander = list;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        Logger.d(mData.size());
        //

        for (int i = 0 ; i <mData.size(); i ++){
            String create_time = mData.get(i).get(ResponseKey.CREATE_TIME)+"000";

            String sDay = DateUtil.onMillisForDay(create_time);
            int startDay = CalendarUtils.getMonthFirstWeek();

//            Logger.d(sDay);
            int day = Integer.parseInt(sDay);

            Logger.d(day+"============"+mCander.get(position)+"============="+DataFormat.onStringForInteger(mCander.get(position)));
            if (day == (DataFormat.onStringForInteger(mCander.get(position)))){
                holder.tv_day.setBackgroundResource(R.drawable.shape_sign);
                holder.tv_day.setTextColor(Color.WHITE);
            }

        }

        holder.tv_day.setText(mCander.get(position));
    }


    @Override
    public int getItemCount() {
        return mCander.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);

        }

    }
}
