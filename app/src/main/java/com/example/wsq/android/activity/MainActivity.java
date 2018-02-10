package com.example.wsq.android.activity;


import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.example.wsq.android.activity.order.MessageActivity;
import com.example.wsq.android.activity.user.LoginActivity;
import com.example.wsq.android.activity.user.SettingActivity;
import com.example.wsq.android.constant.Constant;
import com.example.wsq.android.fragment.DeviceFragment;
import com.example.wsq.android.fragment.FaultFragment;
import com.example.wsq.android.fragment.MainFragment;
import com.example.wsq.android.fragment.UserFragment;
import com.example.wsq.android.inter.OnDialogClickListener;
import com.example.wsq.android.service.UserService;
import com.example.wsq.android.service.impl.UserServiceImpl;
import com.example.wsq.android.tools.AppImageLoad;
import com.example.wsq.android.tools.AppImageView;
import com.example.wsq.android.tools.AppStatus;
import com.example.wsq.android.tools.JGIM;
import com.example.wsq.android.utils.IntentFormat;
import com.example.wsq.android.view.CustomDefaultDialog;
import com.example.wsq.android.view.CustomWebViewDialog;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, AMapLocationListener {

    @BindView(R.id.ll_title_search) LinearLayout ll_title_search;
    @BindView(R.id.ll_setting) LinearLayout ll_setting;
    @BindView(R.id.rl_title_back) RelativeLayout rl_title_back;
    @BindView(R.id.ll_location) LinearLayout ll_location;
    @BindView(R.id.tv_location) TextView tv_location;
    @BindView(R.id.iv_setting) ImageView iv_setting;
    @BindView(R.id.rg_menu) RadioGroup rg_menu;
    @BindView(R.id.rb_main) RadioButton rb_main;
    @BindView(R.id.rb_device) RadioButton rb_device;
    @BindView(R.id.rb_fault)  RadioButton rb_fault;
    @BindView(R.id.rb_user) RadioButton rb_user;
    @BindView(R.id.et_search) EditText et_search;
    @BindView(R.id.view_point) View view_point;
    @BindView(R.id.rl_layout) RelativeLayout rl_layout;
    @BindView(R.id.rl_message) RelativeLayout rl_message;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;

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


    private UserService userService;
    private CustomDefaultDialog customDefaultDialog;
    private CustomWebViewDialog webViewDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppStatus.onSetStates(this, 1);
        ButterKnife.bind(this);
        shared = getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE);
        init();

        enter(1, fragments[0]);

        AppImageView.onRadioButtonSelect(this, rb_main, "image_sy_default.png","image_sy_press.png");
        AppImageView.onRadioButtonSelect(this, rb_device, "image_device_default.png","image_device_press.png");
        AppImageView.onRadioButtonSelect(this, rb_fault, "image_gz_default.png","image_gz_press.png");
        AppImageView.onRadioButtonSelect(this, rb_user, "image_user_default.png","image_user_press.png");
        AppImageView.onLayoutBackgroundImage(this, rl_layout, "image_title_background.png");

    }

    public void init(){

        onSettingTitle(false, true,true, true,false, false);
        rg_menu.setOnCheckedChangeListener(this);

        userService = new UserServiceImpl();


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

        JMessageClient.registerEventReceiver(this);

        view_point.setVisibility(shared.getBoolean(Constant.SHARED.MESSAGE, false) ? View.VISIBLE : View.GONE);

        onRegister();


    }


    public void enter(int page, Fragment fragment){

        curShowPage = page;
        getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();

        rl_layout.setVisibility(curShowPage == 4? View.GONE : View.VISIBLE);

        ll_location.setVisibility(curShowPage ==1 ? View.VISIBLE: View.GONE);

        switch (curShowPage){
            case 1:
                et_search.setHint("变频器");

                onSettingTitle(false, true, true, true, false, false);
                break;
            case 2:
                et_search.setHint("想找自己合胃口的，来搜下吧~");
                onSettingTitle(false, true, false, false, false, false);
                break;
            case 3:
                et_search.setHint("想找自己合胃口的，来搜下吧~");
                onSettingTitle(false, true, false, false, false, false);
                break;
            case 4:
                onSettingTitle(false, false, true, true, false, true);
//                AppImageView.onLayoutBackgroundImage(this, rl_layout, "image_default_user.png");
                break;
        }
    }


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
                enter(1,fragments[0]);
                break;
            case R.id.rb_device:
                enter(2, fragments[1]);
                break;
            case R.id.rb_fault:
                enter(3, fragments[2]);
                break;
            case R.id.rb_user:
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
        JMessageClient.unRegisterEventReceiver(this);
        unregisterReceiver(receiver);
    }

    @Override
    public void onLocationChanged(AMapLocation location) {

        if (location.getAddress().length()!= 0) {

            String address = location.getProvince()+location.getCity()+location.getDistrict()
                    +location.getStreet()+location.getStreetNum();
            shared.edit().putString(Constant.SHARED.LOCATION, address).commit();

            tv_location.setText(location.getCity());
            locationClient.onDestroy();
        }
    }

    public void onRegister(){

        IntentFilter filter = new IntentFilter();
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
            }else if(action.equals(MESSAGE_RECEIVED_ACTION)){  //推送过来的消息
                Log.d("接收到的消息", intent.getStringExtra(KEY_MESSAGE));
                shared.edit().putBoolean(Constant.SHARED.MESSAGE, true).commit();
                 view_point.setVisibility(View.VISIBLE);
                 UserFragment fragment = (UserFragment) fragments[3];
                 fragment.onSetShowPoint();
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
                .setSmallIcon(R.drawable.image_app_icon);//设置通知小ICON
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,MessageActivity.class), 0);


        mBuilder.setContentIntent(pendingIntent);
        //获取Notification管理器
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //用这个Notification管理器把Notification弹出去，那个0是id，用来标识这个Notification的
        notificationManager.notify(1, mBuilder.build());
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode== KeyEvent.KEYCODE_BACK){
            CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您确定退出该应用吗？");
            builder.setOkBtn("退出", new OnDialogClickListener() {
                @Override
                public void onClick(CustomDefaultDialog dialog, String result) {
                    JGIM.JGIM_logout();
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setCancelBtn("还想玩", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }



    public void onEvent(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                Logger.d("用户密码修改");
                break;
            case user_logout:
                //用户换设备登录
                Logger.d("用户换设备登录");
//                onCreateDialog();
                Message msg = new Message();
                handler.sendMessage(msg);
                break;
            case user_deleted:
                //用户被删除
                Logger.d("用户被删除");
                break;
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            onCreateDialog();
        }
    };


    public void onCreateDialog(){

        CustomDefaultDialog.Builder builder = new CustomDefaultDialog.Builder(getApplicationContext());
        builder.setTitle("提示");
        builder.setMessage("您的账号已在其他设备行登录，如果不是您本人操作，请及时修改唯一密码");
        builder.setOkBtn("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                IntentFormat.startActivity(MainActivity.this, LoginActivity.class);
                finish();

            }
        });

        customDefaultDialog = builder.create();
        customDefaultDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        customDefaultDialog.show();

    }

    /**
     *
     * @param isShowBackOrTtitle  显示返回按钮和标题
     * @param isShowSearch   显示搜索框
     * @param isShowSettingLayout  显示设置的layout
     * @param isShowMessageBtn  显示消息layout
     * @param isShowNewMessage 显示新消息
     * @param isShowSettingBtn  显示设置按钮
     */
    public void onSettingTitle(boolean isShowBackOrTtitle, boolean isShowSearch, boolean isShowSettingLayout, boolean isShowMessageBtn, boolean isShowNewMessage, boolean isShowSettingBtn){
        rl_title_back.setVisibility(isShowBackOrTtitle ? View.VISIBLE : View.GONE);
        ll_title_search.setVisibility(isShowSearch ? View.VISIBLE : View.INVISIBLE);
        ll_setting.setVisibility(isShowSettingLayout ? View.VISIBLE : View.GONE);
        rl_message.setVisibility(isShowMessageBtn? View.VISIBLE : View.GONE);
        view_point.setVisibility(isShowNewMessage ? View.VISIBLE: View.GONE);
        iv_setting.setVisibility(isShowSettingBtn ? View.VISIBLE : View.GONE);
    }




}
