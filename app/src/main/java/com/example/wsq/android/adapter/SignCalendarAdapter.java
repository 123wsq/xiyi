package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsq.android.R;

import java.util.List;

/**
 * Created by wsq on 2017/12/19.
 */

public class SignCalendarAdapter extends RecyclerView.Adapter<SignCalendarAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mData;
    private int selectPosition  = 0;
    public SignCalendarAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_day.setText(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);

        }

    }
}
