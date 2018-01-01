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
import com.example.wsq.android.activity.order.DeviceListActivity;
import com.example.wsq.android.activity.FaultActivity;
import com.example.wsq.android.activity.SearchActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.utils.IntentFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/25.
 */

public class SearchRecordAdapter extends RecyclerView.Adapter<SearchRecordAdapter.MyViewHolder>{


    private Context mContext;
    private List<Map<String, String>> mData;
    SharedPreferences shared;
    public SearchRecordAdapter(Context context, List<Map<String, String>> list){
        this.mContext = context;
        this.mData = list;
        shared = mContext.getSharedPreferences(Constant.SHARED_RECORD, Context.MODE_PRIVATE);
    }

    @Override
    public SearchRecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchRecordAdapter.MyViewHolder holder = new SearchRecordAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.layout_search_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchRecordAdapter.MyViewHolder holder, int position) {

        holder.tv_search_name.setText(mData.get(position).get("content"));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_search_name;
        LinearLayout ll_search_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_search_name = itemView.findViewById(R.id.tv_search_name);
            ll_search_delete = itemView.findViewById(R.id.ll_search_delete);
            ll_search_delete.setOnClickListener(this);
            tv_search_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_search_delete:
                    shared.edit().remove(mData.get(getPosition()).get("key")).commit();
                    mData.remove(getPosition());
                    notifyDataSetChanged();
                    break;
                case R.id.tv_search_name:
                    if (SearchActivity.curPage==1 || SearchActivity.curPage==2) {
                        Map<String, Object> param = new HashMap<>();
                        param.put(ResponseKey.KEYWORDS, mData.get(getPosition()).get("content"));
                        IntentFormat.startActivity(mContext, DeviceListActivity.class, param);
                    }else {
                        Map<String, Object> param = new HashMap<>();
                        param.put(ResponseKey.KEYWORDS, mData.get(getPosition()).get("content"));
                        IntentFormat.startActivity(mContext, FaultActivity.class, param);
                    }
                    break;
            }

        }
    }
}
