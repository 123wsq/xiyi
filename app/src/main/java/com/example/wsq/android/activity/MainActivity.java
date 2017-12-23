package com.example.wsq.android.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.wsq.android.R;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.fragment.DeviceFragment;
import com.example.wsq.android.fragment.FaultFragment;
import com.example.wsq.android.fragment.MainFragment;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.utils.IntentFormat;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, AMapLocationListener {

    @BindView(R.id.ll_title_location) LinearLayout ll_title_location;
    @BindView(R.id.rl_title_back) RelativeLayout rl_title_back;
    @BindView(R.id.iv_setting) ImageView iv_setting;
    @BindView(R.id.rg_menu) RadioGroup rg_menu;
    @BindView(R.id.tv_location) TextView tv_location;
    @BindView(R.id.rb_fault)  RadioButton rb_fault;
    @BindView(R.id.et_search) EditText et_search;

    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private int curShowPage = 1;

    MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private SharedPreferences shared;
    private Fragment[] fragments = {MainFragment.getInstance(), DeviceFragment.getInstance(),
            FaultFragment.getInstance(), UserFragment.getInstance()};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        init();

        enter(1, fragments[0]);
//        addFragment();
//        showFragment(0);

    }

    public void init(){

        ll_title_location.setVisibility(View.VISIBLE);
        rl_title_back.setVisibility(View.GONE);
        rg_menu.setOnCheckedChangeListener(this);



        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setGpsFirst(true);
//        locationOption.setHttpTimeOut(2*1000);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationClient.setLocationOption(locationOption);


        // 启动定位
        locationClient.startLocation();

        onRegister();
    }

    public void enter(int page, Fragment fragment){

        curShowPage = page;
        getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();

        switch (curShowPage){
            case 1:
                et_search.setHint("A2变速发动机");
                iv_setting.setVisibility(View.GONE);
                ll_title_location.setVisibility(View.VISIBLE);
                break;
            case 2:
                et_search.setHint("请输入搜索设备的名称");
                iv_setting.setVisibility(View.GONE);
                ll_title_location.setVisibility(View.VISIBLE);
                break;
            case 3:
                et_search.setHint("找的有点迷茫，来搜索下~");
                iv_setting.setVisibility(View.GONE);
                ll_title_location.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_setting.setVisibility(View.VISIBLE);
                ll_title_location.setVisibility(View.GONE);
                break;
        }
        rl_title_back.setVisibility(View.GONE);
    }

    /**
     * 首先将Fragment添加到一个集合中
     */
//    public void addFragment(){
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        int i = 0;
//        for (Fragment fragment : fragments){
//            fragmentTransaction.add(R.id.main_layout, fragment).addToBackStack(i+"");
//            i++;
//        }
//    }



    /**
     * 确认显示和隐藏
     * @param position
     */
//    public void showFragment(int position){
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        for (int i =0 ; i< fragments.length; i++){
//            if (position ==i){
//                fragmentTransaction.show(fragments[position]);
//            }else {
//                fragmentTransaction.hide(fragments[i]);
//            }
//        }
//        fragmentTransaction.commit();
//    }

    @OnClick({R.id.iv_setting, R.id.iv_message, R.id.et_search})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_setting:
                IntentFormat.startActivity(this, SettingActivity.class);
                break;
            case R.id.iv_message:
                IntentFormat.startActivity(this, MessageActivity.class);
                break;
            case R.id.et_search:
                Map<String, Object> map = new HashMap<>();
                map.put("page", curShowPage);
                IntentFormat.startActivity(this, SearchActivity.class, map);
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_main:
//                showFragment(0);
                enter(1,fragments[0]);
                break;
            case R.id.rb_device:
//                showFragment(1);
                enter(2, fragments[1]);
                break;
            case R.id.rb_fault:
//               showFragment(2);
                enter(3, fragments[2]);
                break;
            case R.id.rb_user:
//                showFragment(3);
                enter(4, fragments[3]);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        locationClient.onDestroy();
        locationClient = null;
        locationOption = null;

        unregisterReceiver(receiver);
    }

    @Override
    public void onLocationChanged(AMapLocation location) {

        if (location.getAddress().length()!= 0) {

            shared.edit().putString(Constant.SHARED.LOCATION, location.getAddress());
            tv_location.setText(location.getAddress());
            locationClient.onDestroy();
        }
    }

    public void onRegister(){

        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constant.ACTION.USER_PAGE);
        filter.addAction(Constant.ACTION.USER_PAGE_FAULT);
        filter.addAction(Constant.ACTION.USER_MAIN);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(receiver, filter);
    }
    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d("action:====", action);
             if(action.equals(Constant.ACTION.USER_PAGE_FAULT)){
                enter(3, fragments[2]);
                rb_fault.setChecked(true);
            }else if(action.equals(Constant.ACTION.USER_MAIN)){
                rl_title_back.setVisibility(View.GONE);
                iv_setting.setVisibility(View.GONE);
                ll_title_location.setVisibility(View.VISIBLE);
            }else if(action.equals(MESSAGE_RECEIVED_ACTION)){

                Log.d("接收到的消息", intent.getStringExtra(KEY_MESSAGE));


                setNotification(intent.getStringExtra(KEY_MESSAGE));

            }
        }
    }


    /**
     * 设置通知栏信息
     * @param message
     */
    public void setNotification(String message){

        Notification.Builder mBuilder = new Notification.Builder(MainActivity.this);
        mBuilder.setContentTitle("蜥蜴")//设置通知栏标题
                .setContentText(message) //<span style="font-family:Arial;">/设置通知栏显示内容</span>
//                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
//                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.icon_login);//设置通知小ICON
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,MessageActivity.class), 0);


        mBuilder.setContentIntent(pendingIntent);
        //获取Notification管理器
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //用这个Notification管理器把Notification弹出去，那个0是id，用来标识这个Notification的
        notificationManager.notify(1, mBuilder.build());
//        notificationManager.notify("1", 1, mBuilder.build());
    }
}
