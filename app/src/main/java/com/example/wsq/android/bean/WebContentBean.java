package com.example.wsq.android.bean;


import java.io.Serializable;

/**
 * 编辑网页内容属性
 * Created by wsq on 2018/1/13.
 */

public class WebContentBean implements Serializable{

    private int id;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容类型
     */
    private ContentType type;

    /**
     * html内容
     */
    private String htmlContent;

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 字体颜色
     */
    private String fontColor;

    /**
     * 添加时间
     */
    private long addTime;

    /**
     * 保存的key
     */
    private String key;

    /**
     * 文件名
     */
    private String fileName;


    /**
     * 文章类型
     */
    private AuthType authType;



    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }


    @Override
    public String toString() {
        return "[ " +
                "id = "+id+", "+
                "content = " +content+","+
                "type = "+type.getName()+", "+
                "htmlContent = "+htmlContent+", "+
                "fontSize = "+fontSize+", "+
                "fontColor = "+fontColor+", "+
                "addTime = "+addTime+", "+
                "key = "+key+","+
                "fileName = "+fileName+","+
                "]";
    }
}
