package com.example.wsq.android.constant;

/**
 * Created by wsq on 2017/12/11.
 */

public class Constant {

    public static final String SECRET  = "sadofjasodfkf45sdf54";

    public static final String SHARED_NAME = "XIYI";
    public static final String SHARED_RECORD = "search_Record";
    public static final String SHARED_MSG = "MESSAGE_STATE";

    public static final String[] SEX = {"男", "女"};
    public static final String[] ROLE = {"服务工程师", "企业工程师", "企业管理工程师"};
    public static final String [] EDUCATION = {"高中及以下","专科","本科","研究生","博士及以上"};
    public static final String [] ACTION_CAMERA = {"相册","相机","录像","视频"};
    public static final String[] PIC = {"JPG","JPEG","PNG","jpg","jpeg","png"};

    //验证码长度
    public static final int CODE_LENGTH = 6;
    public static final int PASSWORD_COUNT = 6;

    //表示显示的图片多少
    public static final int IMAGE_COUNT = 3;

    public static class ACTION{
        public static final String USER_PAGE = "com.example.wsq.android.fragment.UserFragment";
        public static final String USER_PAGE_FAULT = "page_fault";
        public static final String USER_MAIN = "com.example.wsq.android.fragment.MainFragment";
        public static final String SERVER = "com.example.wsq.android.SERVER";
        public static final String IMAGE_DELETE= "IMAGE_DELETE";
    }

    public static class SHARED{
        public static final String ISLOGIN= "isLogin";

        public static final String USERNAME = "username";

        public static final String PASSWORD = "password";

        public static final String TOKEN = "token";

        public static final String JUESE = "juese";

        public static final String NAME = "name";

        public static final String TEL = "tel";

        public static final String LOCATION= "location";

        public static final String COMPANY = "company";

        public static final String ID = "uId";
    }



}
