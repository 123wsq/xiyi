package com.example.wsq.android.tools;


import com.orhanobut.logger.Logger;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * 使用极光IM实现单点登录
 * Created by wsq on 2018/1/3.
 */

public class JGIM {

    //判断用户是否存在
    private static boolean userExists = false;
    //用户是否登录成功
    private static boolean isLogin = false;
    //用户注册是否成功
    private static boolean isSuccess = false;



    /**
     * 获取用户信息
     * @param username
     * @return
     */
    public static boolean onUserExists(String username){


        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

                if (null != userInfo){

                    userExists = true;
                }
                Logger.d("获取用户信息：  "+i+"---------"+s);

            }
        });
        return userExists;
    }


    /**
     * 登录IM
     * @param username
     * @param password
     */
    public static boolean JGIM_Login(String username, String password){

        Logger.d("登录      "+System.currentTimeMillis());
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {

                Logger.d("极光登录：  "+i+"---------"+s +"=========="+ System.currentTimeMillis());
                if (i == 0){

                    isLogin = true;
                }

            }
        });
        return isLogin;
    }

    /**
     * 退出登录状态
     */
    public static void JGIM_logout(){

        JMessageClient.logout();
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public static boolean JGIM_Register(final String username, final String password){



        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    isSuccess = true;
                    JGIM_Login(username, password);
                }
                Logger.d("极光注册：  "+i +"----------"+s);
            }
        });
        return isSuccess;
    }


    /**
     * 获取用户信息
     * @return
     */
    public static UserInfo JGIM_userInfo(){

        UserInfo userInfo = JMessageClient.getMyInfo();
        return userInfo;
    }

    public static void JGIM_logoutEvent(){

//        JMessageClient.
    }
}
