package com.example.wsq.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wsq.android.R;
import com.example.wsq.android.bean.SkillBean;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.inter.OnDefaultClickListener;
import com.example.wsq.android.inter.OnRecycCheckListener;
import com.example.wsq.android.utils.DensityUtil;
import com.example.wsq.android.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wsq on 2017/12/19.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder>{

    private Context mContext;
    private List<SkillBean> mData;
    private OnRecycCheckListener mListener;
    private OnDefaultClickListener onDefaultClickListener;
    private int mType; //0 添加  1显示
    private Map<String, Boolean> isSelect;

    public SkillAdapter(Context context, List<SkillBean> arrays, OnRecycCheckListener listener, OnDefaultClickListener defaultClickListener, int type){
        this.mContext = context;
        this.mData = arrays;
        this.mListener = listener;
        this.onDefaultClickListener = defaultClickListener;
        this.mType = type;
        isSelect = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_skill_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cb_skill.setText(mData.get(position).getSkillName());
        isSelect.put(mData.get(position).getSkillName(), mData.get(position).isState());
        holder.cb_skill.setChecked(mData.get(position).isState());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.cb_skill.getLayoutParams();
        onSetParam(params, position);
        holder.cb_skill.setLayoutParams(params);
        holder.cb_skill.setEnabled(mType == 0 ? true : false);
        holder.ll_skill_remove.setVisibility(mType==0 ? View.GONE : View.VISIBLE);
        holder.cb_skill.setTextSize(mType ==0 ? 15 : 11);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        CheckBox cb_skill;
        LinearLayout ll_skill_remove;
        ImageView iv_skill_remove;

        public ViewHolder(View itemView) {
            super(itemView);
            cb_skill = itemView.findViewById(R.id.cb_skill);
            iv_skill_remove = itemView.findViewById(R.id.iv_skill_remove);
            ll_skill_remove = itemView.findViewById(R.id.ll_skill_remove);
            cb_skill.setOnCheckedChangeListener(this);
            iv_skill_remove.setOnClickListener(this);


        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {


            if (mListener==null)return;

            isSelect.put(mData.get(getPosition()).getSkillName(), checked);
            if (checked){

                Iterator<Map.Entry<String, Boolean>> it = isSelect.entrySet().iterator();

                int selectCount = 0;
                while (it.hasNext()){
                    Map.Entry<String, Boolean> entry = it.next();
                    if (entry.getValue()){
                        selectCount++;
                    }
                }
                Logger.d("当前选中个数: "+ selectCount);
                if (selectCount > Constant.SKILL_COUNT){
                    ToastUtils.onToast(mContext,"您最多只能选"+Constant.SKILL_COUNT+"个哟~");
                    compoundButton.setChecked(false);
                    isSelect.put(mData.get(getPosition()).getSkillName(),false);
                    return;
                }
                compoundButton.setTextColor(Color.WHITE);
            }else{
                compoundButton.setTextColor(Color.parseColor("#393939"));

            }

            mListener.onCheckChangeListener(mData.get(getPosition()).getSkillName(),getPosition(),  checked);
        }

        @Override
        public void onClick(View view) {
            if (onDefaultClickListener != null ){
                onDefaultClickListener.onClickListener(getPosition());
            }
        }
    }

    public void onSetParam (LinearLayout.LayoutParams params, int position){
        if (mType == 0 ) {
            if (position % 2 == 0) {
                params.setMargins(DensityUtil.dp2px(mContext, 40),
                        DensityUtil.dp2px(mContext, 10),
                        DensityUtil.dp2px(mContext, 18),
                        DensityUtil.dp2px(mContext, 10));
            } else {
                params.setMargins(DensityUtil.dp2px(mContext, 18),
                        DensityUtil.dp2px(mContext, 10),
                        DensityUtil.dp2px(mContext, 40),
                        DensityUtil.dp2px(mContext, 10));
            }
        }else{
            if (position % 4 ==0){
                params.setMargins(DensityUtil.dp2px(mContext, 0),
                        DensityUtil.dp2px(mContext, 0),
                        DensityUtil.dp2px(mContext, 5),
                        DensityUtil.dp2px(mContext, 10));
            }else if(position % 4== 3){
                params.setMargins(DensityUtil.dp2px(mContext, 5),
                        DensityUtil.dp2px(mContext, 0),
                        DensityUtil.dp2px(mContext, 0),
                        DensityUtil.dp2px(mContext, 10));
            }else{
                params.setMargins(DensityUtil.dp2px(mContext, 5),
                        DensityUtil.dp2px(mContext, 0),
                        DensityUtil.dp2px(mContext, 5),
                        DensityUtil.dp2px(mContext, 10));
            }
        }
    }
}
