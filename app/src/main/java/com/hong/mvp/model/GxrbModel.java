package com.hong.mvp.model;

public class GxrbModel {
    private String banbaoType;
    private String constructInterval;
    private String constructTeam;
    private String densityStraturm;
    private String did;
    private String intelligenceCode;
    private String key;
    private String levelNumber;
    private String nextCircuit;
    private String orderClasses;
    private String reportTime;
    private String stratigraphicPosition;
    private String value;
    private String wellCommonName;
    private String workBrief;
    private String workDate;

    public String getWellCommonName() {
        return this.wellCommonName;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setWellCommonName(String wellCommonName) {
        this.wellCommonName = wellCommonName;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GxrbModel{wellCommonName='");
        stringBuilder.append(this.wellCommonName);
        stringBuilder.append('\'');
        stringBuilder.append(", banbaoType='");
        stringBuilder.append(this.banbaoType);
        stringBuilder.append('\'');
        stringBuilder.append(", constructTeam='");
        stringBuilder.append(this.constructTeam);
        stringBuilder.append('\'');
        stringBuilder.append(", intelligenceCode='");
        stringBuilder.append(this.intelligenceCode);
        stringBuilder.append('\'');
        stringBuilder.append(", reportTime='");
        stringBuilder.append(this.reportTime);
        stringBuilder.append('\'');
        stringBuilder.append(", orderClasses='");
        stringBuilder.append(this.orderClasses);
        stringBuilder.append('\'');
        stringBuilder.append(", workBrief='");
        stringBuilder.append(this.workBrief);
        stringBuilder.append('\'');
        stringBuilder.append(", workDate='");
        stringBuilder.append(this.workDate);
        stringBuilder.append('\'');
        stringBuilder.append(", nextCircuit='");
        stringBuilder.append(this.nextCircuit);
        stringBuilder.append('\'');
        stringBuilder.append(", constructInterval='");
        stringBuilder.append(this.constructInterval);
        stringBuilder.append('\'');
        stringBuilder.append(", densityStraturm='");
        stringBuilder.append(this.densityStraturm);
        stringBuilder.append('\'');
        stringBuilder.append(", stratigraphicPosition='");
        stringBuilder.append(this.stratigraphicPosition);
        stringBuilder.append('\'');
        stringBuilder.append(", key='");
        stringBuilder.append(this.key);
        stringBuilder.append('\'');
        stringBuilder.append(", value='");
        stringBuilder.append(this.value);
        stringBuilder.append('\'');
        stringBuilder.append(", levelNumber='");
        stringBuilder.append(this.levelNumber);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public String getBanbaoType() {
        return this.banbaoType;
    }

    public void setBanbaoType(String banbaoType) {
        this.banbaoType = banbaoType;
    }

    public String getConstructTeam() {
        return this.constructTeam;
    }

    public void setConstructTeam(String constructTeam) {
        this.constructTeam = constructTeam;
    }

    public String getIntelligenceCode() {
        return this.intelligenceCode;
    }

    public void setIntelligenceCode(String intelligenceCode) {
        this.intelligenceCode = intelligenceCode;
    }

    public String getReportTime() {
        return this.reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getOrderClasses() {
        return this.orderClasses;
    }

    public void setOrderClasses(String orderClasses) {
        this.orderClasses = orderClasses;
    }

    public String getWorkBrief() {
        return this.workBrief;
    }

    public void setWorkBrief(String workBrief) {
        this.workBrief = workBrief;
    }

    public String getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getNextCircuit() {
        return this.nextCircuit;
    }

    public void setNextCircuit(String nextCircuit) {
        this.nextCircuit = nextCircuit;
    }

    public String getConstructInterval() {
        return this.constructInterval;
    }

    public void setConstructInterval(String constructInterval) {
        this.constructInterval = constructInterval;
    }

    public String getDensityStraturm() {
        return this.densityStraturm;
    }

    public void setDensityStraturm(String densityStraturm) {
        this.densityStraturm = densityStraturm;
    }

    public String getStratigraphicPosition() {
        return this.stratigraphicPosition;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public void setStratigraphicPosition(String stratigraphicPosition) {
        this.stratigraphicPosition = stratigraphicPosition;
    }

    public GxrbModel() {
    }

    public GxrbModel(String wellCommonName, String banbaoType, String constructTeam, String intelligenceCode, String reportTime, String orderClasses, String workBrief, String workDate, String nextCircuit, String constructInterval, String densityStraturm, String stratigraphicPosition, String key, String value, String levelNumber, String did) {
        this.wellCommonName = wellCommonName;
        this.banbaoType = banbaoType;
        this.constructTeam = constructTeam;
        this.intelligenceCode = intelligenceCode;
        this.reportTime = reportTime;
        this.orderClasses = orderClasses;
        this.workBrief = workBrief;
        this.workDate = workDate;
        this.nextCircuit = nextCircuit;
        this.constructInterval = constructInterval;
        this.densityStraturm = densityStraturm;
        this.stratigraphicPosition = stratigraphicPosition;
        this.key = key;
        this.value = value;
        this.levelNumber = levelNumber;
        this.did = did;
    }

    public String getLevelNumber() {
        return this.levelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        this.levelNumber = levelNumber;
    }
}
