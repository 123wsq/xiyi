package com.example.medialib.tools;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public enum  MediaType {


    IMAGE("MediaStore.Images.Media.EXTERNAL_CONTENT_URI",1),
    Audio("MediaStore.Audio.Media.EXTERNAL_CONTENT_URI",2),
    Video("MediaStore.Video.Media.EXTERNAL_CONTENT_URI", 3),
    ALL("all", 4);


    private String name;

    private int index;

    private  MediaType(String name, int index){
        this.name = name;

        this.index = index;

    }


    // 普通方法
    public static String getName(int index) {
        for (MediaType c : MediaType.values()) {
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
