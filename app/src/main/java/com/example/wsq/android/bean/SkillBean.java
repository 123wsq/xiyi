package com.example.wsq.android.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class SkillBean implements Serializable{

    String skillName;

    boolean state;

    public SkillBean() {
    }
    public SkillBean(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
