package com.hong.mvp.model;

import java.util.Date;

/**
 * 对应服务器StaProcess
 */
public class StaProcess {
    /**
         *  字段注释 : 标识id
         *   表字段 : SPID
     */
    private Short spid;

    /**
         *  字段注释 : 工序
         *   表字段 : SPNAME
     */
    private String spname;

    /**
         *  字段注释 : 油田
         *   表字段 : OILFIELD
     */
    private String oilfield;

    /**
         *  字段注释 : 当前状态（默认0，备选1）
         *   表字段 : STATE
     */
    private Short state;

    /**
         *  字段注释 : 施工内容模版
         *   表字段 : BUILDCONTENT
     */
    private String buildcontent;

    /**
         *   表字段 : USER_ID
     */
    private Long userId;

    /**
         *   表字段 : INTIME
     */
    private Date intime;

    /**
         *  字段注释 : 措施类别
         *   表字段 : ENG_CODE
     */
    private String engCode;

    /**
         *  字段注释 : 别名(用途)
         *   表字段 : AOTHNAME
     */
    private String aothname;

    /**
         *  字段注释 : 标准工序/附加工序（1标准2附加）
         *   表字段 : IS_EXTRA
     */
    private String isExtra;

    /**
         *  字段注释 : 是否报废（1正常2报废）
         *   表字段 : IS_USE
     */
    private String isUse;

    /**
         *   表字段 : BZGS
     */
    private String bzgs;

    public Short getSpid() {
        return spid;
    }

    public void setSpid(Short spid) {
        this.spid = spid;
    }

    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname == null ? null : spname.trim();
    }

    public String getOilfield() {
        return oilfield;
    }

    public void setOilfield(String oilfield) {
        this.oilfield = oilfield == null ? null : oilfield.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getBuildcontent() {
        return buildcontent;
    }

    public void setBuildcontent(String buildcontent) {
        this.buildcontent = buildcontent == null ? null : buildcontent.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getEngCode() {
        return engCode;
    }

    public void setEngCode(String engCode) {
        this.engCode = engCode == null ? null : engCode.trim();
    }

    public String getAothname() {
        return aothname;
    }

    public void setAothname(String aothname) {
        this.aothname = aothname == null ? null : aothname.trim();
    }

    public String getIsExtra() {
        return isExtra;
    }

    public void setIsExtra(String isExtra) {
        this.isExtra = isExtra == null ? null : isExtra.trim();
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse == null ? null : isUse.trim();
    }

    public String getBzgs() {
        return bzgs;
    }

    public void setBzgs(String bzgs) {
        this.bzgs = bzgs == null ? null : bzgs.trim();
    }
}