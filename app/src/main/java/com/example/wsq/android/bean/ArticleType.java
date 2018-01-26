package com.example.wsq.android.bean;

/**
 * Created by wsq on 2018/1/25.
 */

public enum  ArticleType {

    ARTICLE("产品资料",21), ECAMPLE("经典案例",27), OTHER("其他", 28);

    private String name;

    private int index;

    private  ArticleType(String name, int index){
        this.name = name;
        this.index = index;

    }

    // 普通方法
    public static ArticleType getType(int index) {
        for (ArticleType c : ArticleType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

    // 普通方法
    public static String getName(int index) {
        for (ArticleType c : ArticleType.values()) {
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
