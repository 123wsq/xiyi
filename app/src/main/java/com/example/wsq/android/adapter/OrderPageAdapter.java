package com.example.wsq.android.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wsq on 2017/12/14.
 */

public class OrderPageAdapter extends PagerAdapter{

    private Context mContext;
    private List<View> mData;
    public OrderPageAdapter(Context context, List<View> list){
        this.mContext = context;
        this.mData = list;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(mData.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mData.get(position);
//        ImageView image= view.findViewById(R.id.pager_image);
//        image.setImageResource(drawables[position]);
        container.addView(mData.get(position));
        return mData.get(position);
    }
}
