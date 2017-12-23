package com.example.wsq.android.bean;

/**
 * Created by wsq on 2017/12/13.
 */

public class CameraBean {

    public String file_name;

    public String file_path;

    public int type = 0;  //1表示添加按钮 2表示图片  3表示视频

    public boolean isShow;  //是否显示删除按钮

    public boolean isChange; //是否发生变化  主要用于修改

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
