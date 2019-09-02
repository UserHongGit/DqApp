package com.hong.mvp.model;

import java.io.Serializable;

public class AddModel implements Serializable {
    private String content;//内容
    private String cs1,cs2,isOver,sj1,sj2;
    private String pdid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCs1() {
        return cs1;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getSj1() {
        return sj1;
    }

    public String getPdid() {
        return pdid;
    }

    public void setPdid(String pdid) {
        this.pdid = pdid;
    }

    public void setSj1(String sj1) {
        this.sj1 = sj1;
    }

    public String getSj2() {
        return sj2;
    }

    @Override
    public String toString() {
        return "AddModel{" +
                "content='" + content + '\'' +
                ", cs1='" + cs1 + '\'' +
                ", cs2='" + cs2 + '\'' +
                ", isOver='" + isOver + '\'' +
                ", sj1='" + sj1 + '\'' +
                ", sj2='" + sj2 + '\'' +
                ", pdid='" + pdid + '\'' +
                '}';
    }

    public void setSj2(String sj2) {
        this.sj2 = sj2;
    }

}
