package com.example.wsq.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wsq.android.R;
import com.example.wsq.android.adapter.DefaultAdapter;
import com.example.wsq.android.adapter.DeviceAdapter;
import com.example.wsq.android.constant.ResponseKey;
import com.example.wsq.android.inter.HttpResponseCallBack;
import com.example.wsq.android.service.OrderTaskService;
import com.example.wsq.android.service.impl.OrderTaskServiceImpl;
import com.example.wsq.android.tools.RecyclerViewDivider;
import com.example.wsq.android.utils.ParamFormat;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 设备
 * Created by wsq on 2017/12/19.
 */

public class DeviceFragment extends Fragment{


    @BindView(R.id.rv_deviceList) RecyclerView rv_deviceList;
    @BindView(R.id.rv_device) RecyclerView rv_device;
    private View mRootView;

    private OrderTaskService orderTaskService;
    private DefaultAdapter defaultAdapter;
    private DeviceAdapter deviceAdapter;
    private List<Map<String, Object>> leftData;
    private List<Map<String, Object>> rightData;

    public static DeviceFragment getInstance(){
        return new DeviceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.layout_device_list, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leftData = new ArrayList<>();
        rightData = new ArrayList<>();
        orderTaskService = new OrderTaskServiceImpl();

        rv_deviceList.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, 2,
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_deviceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_deviceList.setHasFixedSize(true);


        rv_device.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, 10,
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));

        rv_device.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_device.setHasFixedSize(true);


        defaultAdapter = new DefaultAdapter(DeviceFragment.this, leftData);
        rv_deviceList.setAdapter(defaultAdapter);

        deviceAdapter = new DeviceAdapter(getActivity(), rightData);
        rv_device.setAdapter(deviceAdapter);


        getDeviceList();
    }


    public void getDeviceList(){
        Map<String, String> param = new HashMap<>();

        try {
            orderTaskService.onGetDeviceList(param, new HttpResponseCallBack() {
                @Override
                public void callBack(Map<String, Object> result) {

                    List<Map<String, Object>> list1 = (List<Map<String, Object>>) result.get(ResponseKey.DATA);
                    //设置左边显示的列表
                    leftData.addAll(list1);
                    defaultAdapter.notifyDataSetChanged();

                    //设置右边显示的列表
                    if (leftData.size()!=0){
                        updateData(list1.get(0).get(ResponseKey.SHEBEI).toString());
                    }
                }

                @Override
                public void onCallFail(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当点击左边的时候，更新右边的数据
     * @param result
     */
    public void updateData(String result){

        rightData.clear();
        try {
            JSONArray jsona = new JSONArray(result);

            List<Map<String, Object>> list = new ArrayList<>();

            for (int i = 0 ; i< jsona.length(); i ++){

                rightData.add(ParamFormat.onJsonToMap(jsona.getJSONObject(i).toString()));
            }
            deviceAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
