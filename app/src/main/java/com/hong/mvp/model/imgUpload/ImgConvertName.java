package com.hong.mvp.model.imgUpload;

public class ImgConvertName {
    private String jcid,  jcxm1, jcxm2, jcxm3, tab,  prefix;

    public String getJcid() {
        return jcid;
    }

    public void setJcid(String jcid) {
        this.jcid = jcid;
    }

    public String getJcxm1() {
        return jcxm1;
    }

    public void setJcxm1(String jcxm1) {
        this.jcxm1 = jcxm1;
    }

    @Override
    public String toString() {
        return "ImgConvertName{" +
                "jcid='" + jcid + '\'' +
                ", jcxm1='" + jcxm1 + '\'' +
                ", jcxm2='" + jcxm2 + '\'' +
                ", jcxm3='" + jcxm3 + '\'' +
                ", tab='" + tab + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }

    public ImgConvertName() {
    }

    public ImgConvertName(String jcid, String jcxm1, String jcxm2, String jcxm3, String tab, String prefix) {
        this.jcid = jcid;
        this.jcxm1 = jcxm1;
        this.jcxm2 = jcxm2;
        this.jcxm3 = jcxm3;
        this.tab = tab;
        this.prefix = prefix;
    }

    public String getJcxm2() {
        return jcxm2;
    }

    public void setJcxm2(String jcxm2) {
        this.jcxm2 = jcxm2;
    }

    public String getJcxm3() {
        return jcxm3;
    }

    public void setJcxm3(String jcxm3) {
        this.jcxm3 = jcxm3;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
