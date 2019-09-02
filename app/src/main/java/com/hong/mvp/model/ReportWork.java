package com.hong.mvp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReportWork {
    /**
     *  字段注释 : 油田
     *   表字段 : OILFIELD
     */
    private String oilfield;

    /**
     *   表字段 : ID
     */
    private BigDecimal id;

    /**
     *  字段注释 : 班报ID
     *   表字段 : BB_ID
     */
    private String bbId;

    /**
     *  字段注释 : 井号
     *   表字段 : WELL_COMMON_NAME
     */
    private String wellCommonName;

    /**
     *  字段注释 : 施工步骤唯一id
     *   表字段 : DID
     */
    private String did;

    /**
     *  字段注释 : 资质编码
     *   表字段 : INTELLIGENCE_CODE
     */
    private String intelligenceCode;

    /**
     *  字段注释 : 施工队伍
     *   表字段 : TEAM_NAME
     */
    private String teamName;

    /**
     *  字段注释 : 施工性质（作业项目）
     *   表字段 : WORK_BRIEF
     */
    private String workBrief;

    /**
     *  字段注释 : 班次
     *   表字段 : ORDER_CLASSES
     */
    private String orderClasses;

    /**
     *  字段注释 : 申报日期
     *   表字段 : REPORT_TIME
     */
    private Date reportTime;

    /**
     *  字段注释 : 开工日期
     *   表字段 : WORK_DATE
     */
    private Date workDate;

    /**
     *  字段注释 : 下步工序
     *   表字段 : NEXT_CIRCUIT
     */
    private String nextCircuit;

    /**
     *  字段注释 : 施工井段
     *   表字段 : CONSTRUCT_INTERVAL
     */
    private String constructInterval;

    /**
     *  字段注释 : 厚度/层数
     *   表字段 : DENSITY_STRATURM
     */
    private String densityStraturm;

    /**
     *  字段注释 : 层位
     *   表字段 : STRATIGRAPHIC_POSITION
     */
    private String stratigraphicPosition;

    /**
     *  字段注释 : 层号
     *   表字段 : LEVEL_NUMBER
     */
    private String levelNumber;

    /**
     *  字段注释 : 工序id
     *   表字段 : SPID
     */
    private Integer spid;

    /**
     *  字段注释 : 施工步骤工序id
     *   表字段 : PDID
     */
    private Integer pdid;

    /**
     *  字段注释 : 工序分类：施工步骤定制工序，附加工序
     *   表字段 : GX_TYPE
     */
    private String gxType;

    /**
     *  字段注释 : 施工工序
     *   表字段 : CIRCUIT_NAME
     */
    private String circuitName;

    /**
     *  字段注释 : 起始时间——小时
     *   表字段 : BEGIN_TIME_HOUR
     */
    private Integer beginTimeHour;

    /**
     *  字段注释 : 起始时间——分钟
     *   表字段 : BEGIN_TIME_MINUTE
     */
    private Integer beginTimeMinute;

    /**
     *  字段注释 : 截止时间——分钟
     *   表字段 : END_TIME_MINUTE
     */
    private Integer endTimeMinute;

    /**
     *  字段注释 : 截止时间——小时
     *   表字段 : END_TIME_HOUR
     */
    private Integer endTimeHour;

    /**
     *  字段注释 : 工作内容
     *   表字段 : WORK_CONTENT
     */
    private String workContent;

    /**
     *  字段注释 : 工序是否完成
     *   表字段 : IS_COMPLETE
     */
    private String isComplete;

    /**
     *  字段注释 : 记录
     *   表字段 : RECORD_NAME
     */
    private String recordName;

    /**
     *  字段注释 : 班长
     *   表字段 : CLASS_MONITOR
     */
    private String classMonitor;

    /**
     *  字段注释 : 当班干部
     *   表字段 : WATCH_CADRE
     */
    private String watchCadre;

    /**
     *  字段注释 : 录入员
     *   表字段 : REDACTION_NAME
     */
    private String redactionName;

    /**
     *  字段注释 : 班报类型
     *   表字段 : BANBAO_TYPE
     */
    private String banbaoType;

    /**
     *  字段注释 : 试油井段
     *   表字段 : TEST_OIL_INTERVAL
     */
    private String testOilInterval;

    /**
     *  字段注释 : 层位/层号
     *   表字段 : CENG_WEI_HAO
     */
    private String cengWeiHao;

    /**
     *  字段注释 : 试油层序/设计层数
     *   表字段 : TEST_OIL_CENGXU
     */
    private String testOilCengxu;

    /**
     *  字段注释 : 试油方式
     *   表字段 : TEST_OIL_METHOD
     */
    private String testOilMethod;

    /**
     *   表字段 : OUTPUT_YE_VOLUME
     */
    private BigDecimal outputYeVolume;

    /**
     *   表字段 : OUTPUT_OIL_VOLUME
     */
    private BigDecimal outputOilVolume;

    /**
     *   表字段 : OUTPUT_GAS_VOLUME
     */
    private BigDecimal outputGasVolume;

    /**
     *   表字段 : FREE_WATER_CONTENT
     */
    private BigDecimal freeWaterContent;

    /**
     *   表字段 : EMULSIFIED_WATER
     */
    private BigDecimal emulsifiedWater;

    /**
     *   表字段 : CRUDE_OIL_WATER
     */
    private BigDecimal crudeOilWater;

    /**
     *   表字段 : COMPOSITE_WATER_CUT
     */
    private BigDecimal compositeWaterCut;

    /**
     *   表字段 : CUMULATIVE_YE_PRODUCTION
     */
    private BigDecimal cumulativeYeProduction;

    /**
     *   表字段 : CUMULATIVE_OIL_PRODUCTION
     */
    private BigDecimal cumulativeOilProduction;

    /**
     *   表字段 : CUMULATIVE_GAS_PRODUCTION
     */
    private BigDecimal cumulativeGasProduction;

    /**
     *   表字段 : CUMULATIVE_WATER_PRODUCTION
     */
    private BigDecimal cumulativeWaterProduction;

    /**
     *  字段注释 : 2015-5-8 11:19:39罐池液面高度改为备注
     *   表字段 : HEIGHT_LEVEL
     */
    private String heightLevel;

    /**
     *   表字段 : CHOU_CI
     */
    private Integer chouCi;

    /**
     *   表字段 : CHOU_SHEN
     */
    private BigDecimal chouShen;

    /**
     *   表字段 : PRODUCING_FLUID_LEVEL
     */
    private BigDecimal producingFluidLevel;

    /**
     *   表字段 : SUBMERGENCE_DEPTH
     */
    private BigDecimal submergenceDepth;

    /**
     *   表字段 : PUMP_PRESSURE
     */
    private String pumpPressure;

    /**
     *   表字段 : JU_SHEN
     */
    private BigDecimal juShen;

    /**
     *   表字段 : NITROGEN_GAS_DELIVERY
     */
    private BigDecimal nitrogenGasDelivery;

    /**
     *   表字段 : NITROGEN_GAS_VOLUME
     */
    private BigDecimal nitrogenGasVolume;

    /**
     *   表字段 : OIL_PRESSURE
     */
    private BigDecimal oilPressure;

    /**
     *   表字段 : CASING_PRESSURE
     */
    private BigDecimal casingPressure;

    /**
     *   表字段 : GLIB_TALKER
     */
    private BigDecimal glibTalker;

    /**
     *   表字段 : ROTATE_SPEED
     */
    private Integer rotateSpeed;

    /**
     *   表字段 : LIQUIDITY
     */
    private String liquidity;

    /**
     *   表字段 : DENSITY
     */
    private BigDecimal density;

    /**
     *   表字段 : CUBIC_CAPACITY
     */
    private BigDecimal cubicCapacity;

    /**
     *   表字段 : OIL1
     */
    private BigDecimal oil1;

    /**
     *   表字段 : GAS1
     */
    private BigDecimal gas1;

    /**
     *   表字段 : WATER1
     */
    private BigDecimal water1;

    /**
     *   表字段 : OIL2
     */
    private BigDecimal oil2;

    /**
     *   表字段 : GAS2
     */
    private BigDecimal gas2;

    /**
     *   表字段 : WATER2
     */
    private BigDecimal water2;

    /**
     *   表字段 : MONITORING
     */
    private String monitoring;

    /**
     *   表字段 : YES_NO
     */
    private String yesNo;

    /**
     *  字段注释 : 完井判断
     *   表字段 : COMPLETE_JUDGEMENT
     */
    private String completeJudgement;

    /**
     *   表字段 : FANGS
     */
    private String fangs;

    /**
     *   表字段 : REPORT_TIME1
     */
    private Date reportTime1;

    /**
     *  字段注释 : 完层判断
     *   表字段 : COMPLETE_CW
     */
    private String completeCw;

    /**
     *   表字段 : JKWD
     */
    private String jkwd;

    /**
     *  字段注释 : 监督
     *   表字段 : SUPERVISOR
     */
    private String supervisor;

    /**
     *  字段注释 : 第一种、第二种排液标识
     *   表字段 : FLAG
     */
    private String flag;

    /**
     *   表字段 : CREATE_USER
     */
    private String createUser;

    /**
     *   表字段 : CREATE_TIME
     */
    private Date createTime;


    //辅助项
    private String tab;
    private String reportTime3,workDate3,endDate3,userid;
    private List<ReportWork> entity_list= new ArrayList<ReportWork>();
    private String spid_pdid;

    public String getUserid() {
        return userid;
    }

    public String getSpid_pdid() {
        return spid_pdid;
    }

    public void setSpid_pdid(String spid_pdid) {
        this.spid_pdid = spid_pdid;
    }

    public List<ReportWork> getEntity_list() {
        return entity_list;
    }

    public void setEntity_list(List<ReportWork> entity_list) {
        this.entity_list = entity_list;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWorkDate3() {
        return workDate3;
    }

    public void setWorkDate3(String workDate3) {
        this.workDate3 = workDate3;
    }

    public String getEndDate3() {
        return endDate3;
    }

    public void setEndDate3(String endDate3) {
        this.endDate3 = endDate3;
    }

    public String getReportTime3() {
        return reportTime3;
    }

    public void setReportTime3(String reportTime3) {
        this.reportTime3 = reportTime3;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getOilfield() {
        return oilfield;
    }

    public void setOilfield(String oilfield) {
        this.oilfield = oilfield == null ? null : oilfield.trim();
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getBbId() {
        return bbId;
    }

    public void setBbId(String bbId) {
        this.bbId = bbId == null ? null : bbId.trim();
    }

    public String getWellCommonName() {
        return wellCommonName;
    }

    public void setWellCommonName(String wellCommonName) {
        this.wellCommonName = wellCommonName == null ? null : wellCommonName.trim();
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did == null ? null : did.trim();
    }

    public String getIntelligenceCode() {
        return intelligenceCode;
    }

    public void setIntelligenceCode(String intelligenceCode) {
        this.intelligenceCode = intelligenceCode == null ? null : intelligenceCode.trim();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public String getWorkBrief() {
        return workBrief;
    }

    public void setWorkBrief(String workBrief) {
        this.workBrief = workBrief == null ? null : workBrief.trim();
    }

    public String getOrderClasses() {
        return orderClasses;
    }

    public void setOrderClasses(String orderClasses) {
        this.orderClasses = orderClasses == null ? null : orderClasses.trim();
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getNextCircuit() {
        return nextCircuit;
    }

    public void setNextCircuit(String nextCircuit) {
        this.nextCircuit = nextCircuit == null ? null : nextCircuit.trim();
    }

    public String getConstructInterval() {
        return constructInterval;
    }

    public void setConstructInterval(String constructInterval) {
        this.constructInterval = constructInterval == null ? null : constructInterval.trim();
    }

    public String getDensityStraturm() {
        return densityStraturm;
    }

    public void setDensityStraturm(String densityStraturm) {
        this.densityStraturm = densityStraturm == null ? null : densityStraturm.trim();
    }

    public String getStratigraphicPosition() {
        return stratigraphicPosition;
    }

    public void setStratigraphicPosition(String stratigraphicPosition) {
        this.stratigraphicPosition = stratigraphicPosition == null ? null : stratigraphicPosition.trim();
    }

    public String getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        this.levelNumber = levelNumber == null ? null : levelNumber.trim();
    }


    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }


    public Integer getPdid() {
        return pdid;
    }

    public void setPdid(Integer pdid) {
        this.pdid = pdid;
    }

    public String getGxType() {
        return gxType;
    }

    public void setGxType(String gxType) {
        this.gxType = gxType == null ? null : gxType.trim();
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName == null ? null : circuitName.trim();
    }

    public Integer getBeginTimeHour() {
        return beginTimeHour;
    }

    public void setBeginTimeHour(Integer beginTimeHour) {
        this.beginTimeHour = beginTimeHour;
    }

    public Integer getBeginTimeMinute() {
        return beginTimeMinute;
    }

    public void setBeginTimeMinute(Integer beginTimeMinute) {
        this.beginTimeMinute = beginTimeMinute;
    }

    public Integer getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(Integer endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public Integer getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(Integer endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent == null ? null : workContent.trim();
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete == null ? null : isComplete.trim();
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName == null ? null : recordName.trim();
    }

    public String getClassMonitor() {
        return classMonitor;
    }

    public void setClassMonitor(String classMonitor) {
        this.classMonitor = classMonitor == null ? null : classMonitor.trim();
    }

    public String getWatchCadre() {
        return watchCadre;
    }

    public void setWatchCadre(String watchCadre) {
        this.watchCadre = watchCadre == null ? null : watchCadre.trim();
    }

    public String getRedactionName() {
        return redactionName;
    }

    public void setRedactionName(String redactionName) {
        this.redactionName = redactionName == null ? null : redactionName.trim();
    }

    public String getBanbaoType() {
        return banbaoType;
    }

    public void setBanbaoType(String banbaoType) {
        this.banbaoType = banbaoType == null ? null : banbaoType.trim();
    }

    public String getTestOilInterval() {
        return testOilInterval;
    }

    public void setTestOilInterval(String testOilInterval) {
        this.testOilInterval = testOilInterval == null ? null : testOilInterval.trim();
    }

    public String getCengWeiHao() {
        return cengWeiHao;
    }

    public void setCengWeiHao(String cengWeiHao) {
        this.cengWeiHao = cengWeiHao == null ? null : cengWeiHao.trim();
    }

    public String getTestOilCengxu() {
        return testOilCengxu;
    }

    public void setTestOilCengxu(String testOilCengxu) {
        this.testOilCengxu = testOilCengxu == null ? null : testOilCengxu.trim();
    }

    public String getTestOilMethod() {
        return testOilMethod;
    }

    public void setTestOilMethod(String testOilMethod) {
        this.testOilMethod = testOilMethod == null ? null : testOilMethod.trim();
    }

    public BigDecimal getOutputYeVolume() {
        return outputYeVolume;
    }

    public void setOutputYeVolume(BigDecimal outputYeVolume) {
        this.outputYeVolume = outputYeVolume;
    }

    public BigDecimal getOutputOilVolume() {
        return outputOilVolume;
    }

    public void setOutputOilVolume(BigDecimal outputOilVolume) {
        this.outputOilVolume = outputOilVolume;
    }

    public BigDecimal getOutputGasVolume() {
        return outputGasVolume;
    }

    public void setOutputGasVolume(BigDecimal outputGasVolume) {
        this.outputGasVolume = outputGasVolume;
    }

    public BigDecimal getFreeWaterContent() {
        return freeWaterContent;
    }

    public void setFreeWaterContent(BigDecimal freeWaterContent) {
        this.freeWaterContent = freeWaterContent;
    }

    public BigDecimal getEmulsifiedWater() {
        return emulsifiedWater;
    }

    public void setEmulsifiedWater(BigDecimal emulsifiedWater) {
        this.emulsifiedWater = emulsifiedWater;
    }

    public BigDecimal getCrudeOilWater() {
        return crudeOilWater;
    }

    public void setCrudeOilWater(BigDecimal crudeOilWater) {
        this.crudeOilWater = crudeOilWater;
    }

    public BigDecimal getCompositeWaterCut() {
        return compositeWaterCut;
    }

    public void setCompositeWaterCut(BigDecimal compositeWaterCut) {
        this.compositeWaterCut = compositeWaterCut;
    }

    public BigDecimal getCumulativeYeProduction() {
        return cumulativeYeProduction;
    }

    public void setCumulativeYeProduction(BigDecimal cumulativeYeProduction) {
        this.cumulativeYeProduction = cumulativeYeProduction;
    }

    public BigDecimal getCumulativeOilProduction() {
        return cumulativeOilProduction;
    }

    public void setCumulativeOilProduction(BigDecimal cumulativeOilProduction) {
        this.cumulativeOilProduction = cumulativeOilProduction;
    }

    public BigDecimal getCumulativeGasProduction() {
        return cumulativeGasProduction;
    }

    public void setCumulativeGasProduction(BigDecimal cumulativeGasProduction) {
        this.cumulativeGasProduction = cumulativeGasProduction;
    }

    public BigDecimal getCumulativeWaterProduction() {
        return cumulativeWaterProduction;
    }

    public void setCumulativeWaterProduction(BigDecimal cumulativeWaterProduction) {
        this.cumulativeWaterProduction = cumulativeWaterProduction;
    }

    public String getHeightLevel() {
        return heightLevel;
    }

    public void setHeightLevel(String heightLevel) {
        this.heightLevel = heightLevel == null ? null : heightLevel.trim();
    }

    public Integer getChouCi() {
        return chouCi;
    }

    public void setChouCi(Integer chouCi) {
        this.chouCi = chouCi;
    }

    public BigDecimal getChouShen() {
        return chouShen;
    }

    public void setChouShen(BigDecimal chouShen) {
        this.chouShen = chouShen;
    }

    public BigDecimal getProducingFluidLevel() {
        return producingFluidLevel;
    }

    public void setProducingFluidLevel(BigDecimal producingFluidLevel) {
        this.producingFluidLevel = producingFluidLevel;
    }

    public BigDecimal getSubmergenceDepth() {
        return submergenceDepth;
    }

    public void setSubmergenceDepth(BigDecimal submergenceDepth) {
        this.submergenceDepth = submergenceDepth;
    }

    public String getPumpPressure() {
        return pumpPressure;
    }

    public void setPumpPressure(String pumpPressure) {
        this.pumpPressure = pumpPressure == null ? null : pumpPressure.trim();
    }

    public BigDecimal getJuShen() {
        return juShen;
    }

    public void setJuShen(BigDecimal juShen) {
        this.juShen = juShen;
    }

    public BigDecimal getNitrogenGasDelivery() {
        return nitrogenGasDelivery;
    }

    public void setNitrogenGasDelivery(BigDecimal nitrogenGasDelivery) {
        this.nitrogenGasDelivery = nitrogenGasDelivery;
    }

    public BigDecimal getNitrogenGasVolume() {
        return nitrogenGasVolume;
    }

    public void setNitrogenGasVolume(BigDecimal nitrogenGasVolume) {
        this.nitrogenGasVolume = nitrogenGasVolume;
    }

    public BigDecimal getOilPressure() {
        return oilPressure;
    }

    public void setOilPressure(BigDecimal oilPressure) {
        this.oilPressure = oilPressure;
    }

    public BigDecimal getCasingPressure() {
        return casingPressure;
    }

    public void setCasingPressure(BigDecimal casingPressure) {
        this.casingPressure = casingPressure;
    }

    public BigDecimal getGlibTalker() {
        return glibTalker;
    }

    public void setGlibTalker(BigDecimal glibTalker) {
        this.glibTalker = glibTalker;
    }

    public Integer getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(Integer rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity == null ? null : liquidity.trim();
    }

    public BigDecimal getDensity() {
        return density;
    }

    public void setDensity(BigDecimal density) {
        this.density = density;
    }

    public BigDecimal getCubicCapacity() {
        return cubicCapacity;
    }

    public void setCubicCapacity(BigDecimal cubicCapacity) {
        this.cubicCapacity = cubicCapacity;
    }

    public BigDecimal getOil1() {
        return oil1;
    }

    public void setOil1(BigDecimal oil1) {
        this.oil1 = oil1;
    }

    public BigDecimal getGas1() {
        return gas1;
    }

    public void setGas1(BigDecimal gas1) {
        this.gas1 = gas1;
    }

    public BigDecimal getWater1() {
        return water1;
    }

    public void setWater1(BigDecimal water1) {
        this.water1 = water1;
    }

    public BigDecimal getOil2() {
        return oil2;
    }

    public void setOil2(BigDecimal oil2) {
        this.oil2 = oil2;
    }

    public BigDecimal getGas2() {
        return gas2;
    }

    public void setGas2(BigDecimal gas2) {
        this.gas2 = gas2;
    }

    public BigDecimal getWater2() {
        return water2;
    }

    public void setWater2(BigDecimal water2) {
        this.water2 = water2;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring == null ? null : monitoring.trim();
    }

    public String getYesNo() {
        return yesNo;
    }

    public void setYesNo(String yesNo) {
        this.yesNo = yesNo == null ? null : yesNo.trim();
    }

    public String getCompleteJudgement() {
        return completeJudgement;
    }

    public void setCompleteJudgement(String completeJudgement) {
        this.completeJudgement = completeJudgement == null ? null : completeJudgement.trim();
    }

    public String getFangs() {
        return fangs;
    }

    public void setFangs(String fangs) {
        this.fangs = fangs == null ? null : fangs.trim();
    }

    public Date getReportTime1() {
        return reportTime1;
    }

    public void setReportTime1(Date reportTime1) {
        this.reportTime1 = reportTime1;
    }

    public String getCompleteCw() {
        return completeCw;
    }

    public void setCompleteCw(String completeCw) {
        this.completeCw = completeCw == null ? null : completeCw.trim();
    }

    public String getJkwd() {
        return jkwd;
    }

    public void setJkwd(String jkwd) {
        this.jkwd = jkwd == null ? null : jkwd.trim();
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor == null ? null : supervisor.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}