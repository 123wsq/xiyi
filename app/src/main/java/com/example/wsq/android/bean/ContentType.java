package com.example.wsq.android.bean;

/**
 * web内容类型
 * Created by wsq on 2018/1/13.
 */

public enum ContentType {

    TITLE("标题",1), CREATE_TIME("创建时间",2), CONTENT_FONT("文字内容", 3), CONTENT_IMG("内容图片", 4),
    ICON("图标",5), INTRODUCTION("简介", 6), ADDACHMENT("附件", 7);

    private String name;

    private int index;

    private  ContentType(String name, int index){
        this.name = name;

        this.index = index;

    }


    // 普通方法
    public static String getName(int index) {
        for (ContentType c : ContentType.values()) {
            if (c.getIndex() == index) {
                return c.name;
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
