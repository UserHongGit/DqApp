package com.hong.dao;

import java.util.Date;

public class LocalRepo {
    private String avatarurl;
    private String banbaoType;
    private String completeJudgement;
    private String dailyid;
    private String description;
    private Boolean fork;
    private Integer forksCount;
    private long id;
    private String intelligenceCode;
    private String language;
    private String name;
    private String orderClasses;
    private String ownerAvatarUrl;
    private String ownerLogin;
    private Date reportTime;
    private Integer stargazersCount;
    private String teamName;
    private String title;
    private Integer watchersCount;
    private String wellCommonName;
    private String workBrief;
    private Date workDate;

    public LocalRepo(long id) {
        this.id = id;
    }

    public LocalRepo() {
    }

    public LocalRepo(long id, String name, String description, String language, Integer stargazersCount, Integer watchersCount, Integer forksCount, Boolean fork, String ownerLogin, String ownerAvatarUrl, String avatarurl, String title, String dailyid, String wellCommonName, String intelligenceCode, String workBrief, String teamName, Date reportTime, Date workDate, String banbaoType, String completeJudgement, String orderClasses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.dailyid = dailyid;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.forksCount = forksCount;
        this.fork = fork;
        this.ownerLogin = ownerLogin;
        this.ownerAvatarUrl = ownerAvatarUrl;
        this.title = title;
        this.avatarurl = avatarurl;
        this.wellCommonName = wellCommonName;
        this.intelligenceCode = intelligenceCode;
        this.workBrief = workBrief;
        this.teamName = teamName;
        this.reportTime = reportTime;
        this.workDate = workDate;
        this.banbaoType = banbaoType;
        this.completeJudgement = completeJudgement;
        this.orderClasses = orderClasses;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatarurl() {
        return this.avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getBanbaoType() {
        return this.banbaoType;
    }

    public void setBanbaoType(String banbaoType) {
        this.banbaoType = banbaoType;
    }

    public String getCompleteJudgement() {
        return this.completeJudgement;
    }

    public void setCompleteJudgement(String completeJudgement) {
        this.completeJudgement = completeJudgement;
    }

    public String getOrderClasses() {
        return this.orderClasses;
    }

    public void setOrderClasses(String orderClasses) {
        this.orderClasses = orderClasses;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDailyid() {
        return this.dailyid;
    }

    public void setDailyid(String dailyid) {
        this.dailyid = dailyid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWellCommonName() {
        return this.wellCommonName;
    }

    public void setWellCommonName(String wellCommonName) {
        this.wellCommonName = wellCommonName;
    }

    public String getIntelligenceCode() {
        return this.intelligenceCode;
    }

    public void setIntelligenceCode(String intelligenceCode) {
        this.intelligenceCode = intelligenceCode;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStargazersCount() {
        return this.stargazersCount;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public Integer getWatchersCount() {
        return this.watchersCount;
    }

    public void setWatchersCount(Integer watchersCount) {
        this.watchersCount = watchersCount;
    }

    public Integer getForksCount() {
        return this.forksCount;
    }

    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    public Boolean getFork() {
        return this.fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getOwnerLogin() {
        return this.ownerLogin;
    }

    public String getWorkBrief() {
        return this.workBrief;
    }

    public void setWorkBrief(String workBrief) {
        this.workBrief = workBrief;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Date getReportTime() {
        return this.reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerAvatarUrl() {
        return this.ownerAvatarUrl;
    }

    public void setOwnerAvatarUrl(String ownerAvatarUrl) {
        this.ownerAvatarUrl = ownerAvatarUrl;
    }
}
