package com.example.wsq.plugin.umeng;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by wsq on 2018/1/12.
 * 统计配置初始化
 */

public class UmengUtils {

    /**
     * 在Application中做的初始化
     */
    public static void initUmeng(Context context) {
        MobclickAgent.setDebugMode(true);//开启调试模式（如果不开启debug运行不会上传umeng统计）
        MobclickAgent.openActivityDurationTrack(false);
//        AnalyticsConfig.setChannel(Common.getChannel());
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, "wandoujia");

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);


        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);

        /**
         * Android统计SDK从V4.6版本开始内建错误统计，不需要开发者再手动集成。
         * SDK通过Thread.UncaughtExceptionHandler 捕获程序崩溃日志，并在程序下次启动时发送到服务器。
         * 如不需要错误统计功能，可通过此方法关闭
         */
        MobclickAgent.setCatchUncaughtExceptions(true);

        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onResume加入
     *
     * @param context
     */
    public static void onResumeToActivity(Context context) {
        MobclickAgent.onPageStart(context.getClass().getName());
        MobclickAgent.onResume(context);
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onPause加入
     *
     * @param context
     */
    public static void onPauseToActivity(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getName());
        MobclickAgent.onPause(context);
    }

    /**
     * 在BaseFragment中的onResume加入
     *
     * @param context
     */
    public static void onResumeToFragment(Context context) {
        MobclickAgent.onPageStart(context.getClass().getName());
    }

    /**
     * 在BaseFragment中的onPause加入
     *
     * @param context
     */
    public static void onPauseToFragment(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getName());
    }

    /**
     * 在登录成功的地方调用
     *
     * @param userId 用户id
     */
    public static void onLogin(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }

    /**
     * 在退出登录的地方调用
     */
    public static void onLogout() {
        MobclickAgent.onProfileSignOff();
    }
}
