package com.example.wsq.android.bean;

/**
 * Created by wsq on 2018/1/15.
 */

public enum FileType {

    PDF(".pdf",1), DOCX(".docx",2), EXCEL(".xls", 3), TXT(".txt", 4),
    JPG(".jpg",5), PNG(".png",6), DOC(".doc",7), PPT(".ppt",8), ZIP(".zip",9),
    RAR(".rar",10), PSD(".psd", 11), MP3(".mp3", 12), EXE(".exe",13),
    MP4(".mp4",14), CHM(".chm",15);

    private String name;

    private int index;

    private  FileType(String name, int index){
        this.name = name;

        this.index = index;

    }


    // 普通方法
    public static String getName(int index) {
        for (FileType c : FileType.values()) {
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
