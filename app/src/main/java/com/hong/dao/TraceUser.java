package com.hong.dao;

import java.util.Date;

public class TraceUser {
    private String avatarUrl;
    private Integer followers;
    private Integer following;
    private Date latestTime;
    private String login;
    private String name;
    private String oilfield;
    private Date startTime;
    private Integer traceNum;

    public TraceUser(String login) {
        this.login = login;
    }

    public TraceUser(String login, String name, String avatarUrl, Integer followers, Integer following, Date startTime, Date latestTime, Integer traceNum, String oilfield) {
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.followers = followers;
        this.following = following;
        this.startTime = startTime;
        this.latestTime = latestTime;
        this.traceNum = traceNum;
        this.oilfield = oilfield;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getFollowers() {
        return this.followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return this.following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLatestTime() {
        return this.latestTime;
    }

    public String getOilfield() {
        return this.oilfield;
    }

    public void setOilfield(String oilfield) {
        this.oilfield = oilfield;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public Integer getTraceNum() {
        return this.traceNum;
    }

    public void setTraceNum(Integer traceNum) {
        this.traceNum = traceNum;
    }
}
