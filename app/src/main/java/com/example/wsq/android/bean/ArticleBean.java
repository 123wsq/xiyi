package com.example.wsq.android.bean;

/**
 * 文章
 * Created by wsq on 2018/1/25.
 */

public class ArticleBean {

    int id;

    String title;

    String icon;

    ArticleType type;

    String content;

    String description;

    String attachment;

    AuthType authType;

    String create_time;

    String update_time;

    public ArticleBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "[id = "+id+
                ", title = "+title+
                ", description = "+description+
                ", icon = "+icon+
                ", ArticleType = "+type.getName()+
                ", content = "+content+
                ", attachment = "+attachment+
                ", create_time = "+create_time+
                ", update_time = "+update_time+
                ", AuthType = "+authType.getName()+"]";
    }
}
