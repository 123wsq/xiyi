package com.example.wsq.android.bean;

/**
 * 资料类型
 * Created by wsq on 2018/1/19.
 */

public enum AuthType {


    ARTICLES("分享资料",13), RESOURCE("企业资源管理",2), NEWS("新闻内容", 1);

    private String name;

    private int index;

    private AuthType(String name, int index){
        this.name = name;

        this.index = index;

    }


    // 获取名称
    public static String getName(int index) {
        for (AuthType c : AuthType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    /**
     * 通过index获取对象
     * @param index
     * @return
     */
    public static AuthType getType(int index){
        for (AuthType c : AuthType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
