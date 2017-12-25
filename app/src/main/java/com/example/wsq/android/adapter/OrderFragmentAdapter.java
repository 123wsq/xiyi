package com.example.wsq.android.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wsq on 2017/12/25.
 */

public class OrderFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;
    public OrderFragmentAdapter(FragmentManager fm, List<Fragment> list){
        super(fm);
        this.mFragments = list;

    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
