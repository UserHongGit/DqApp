package com.hong.mvp.model.sgcs;

import java.util.Date;
import java.util.List;

public class GxEntity {
	private String eng_code;
	private Integer spid; // 标准工序ID
	private String spname; // 工序名称
	private String customname;//自定义工序施工内容模板
	private String oilfield; // 适用油田
	private Integer state; // 当前状态（默认0，备选1）
	private String buildcontent; // 施工内容拼接串
	private String buildcontentTMP; // 施工内容拼接串
	private String sels;//列表
	private Date intime;
	private int user_id;//用户（用于模板定制）
	private String aothname;
	private String is_extra;
	private String is_use;
	private String customid;
	
	private Integer id; // 施工参数ID
	private String param; // 施工参数名称
	private String remark; // 备注
	private String datatype;//数据类型
	private String units;//计量单位
	private String tempdata;//模板数据
	private String paramdata;//录入数据
	private String bzgs;//标准工时
	
	private int row_num; // 序号
	private int endRow; // 结束行
	private int startRow; // 开始行
	private List<GxEntity> entityList;
	public String getEng_code() {
		return eng_code;
	}
	public void setEng_code(String eng_code) {
		this.eng_code = eng_code;
	}
	public Integer getSpid() {
		return spid;
	}
	public void setSpid(Integer spid) {
		this.spid = spid;
	}
	public String getSpname() {
		return spname;
	}
	public void setSpname(String spname) {
		this.spname = spname;
	}
	public String getOilfield() {
		return oilfield;
	}
	public void setOilfield(String oilfield) {
		this.oilfield = oilfield;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getBuildcontent() {
		return buildcontent;
	}
	public void setBuildcontent(String buildcontent) {
		this.buildcontent = buildcontent;
	}
	public String getBuildcontentTMP() {
		return buildcontentTMP;
	}
	public void setBuildcontentTMP(String buildcontentTMP) {
		this.buildcontentTMP = buildcontentTMP;
	}
	public String getSels() {
		return sels;
	}
	public void setSels(String sels) {
		this.sels = sels;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRow_num() {
		return row_num;
	}
	public void setRow_num(int row_num) {
		this.row_num = row_num;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public List<GxEntity> getEntityList() {
		return entityList;
	}
	public void setEntityList(List<GxEntity> entityList) {
		this.entityList = entityList;
	}
	public Date getIntime() {
		return intime;
	}
	public void setIntime(Date intime) {
		this.intime = intime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getTempdata() {
		return tempdata;
	}
	public void setTempdata(String tempdata) {
		this.tempdata = tempdata;
	}
	public String getParamdata() {
		return paramdata;
	}
	public void setParamdata(String paramdata) {
		this.paramdata = paramdata;
	}
	public String getAothname() {
		return aothname;
	}
	public void setAothname(String aothname) {
		this.aothname = aothname;
	}
	public String getIs_extra() {
		return is_extra;
	}
	public void setIs_extra(String is_extra) {
		this.is_extra = is_extra;
	}
	public String getIs_use() {
		return is_use;
	}
	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}
	public String getCustomid() {
		return customid;
	}
	public void setCustomid(String customid) {
		this.customid = customid;
	}
	public String getBzgs() {
		return bzgs;
	}
	public void setBzgs(String bzgs) {
		this.bzgs = bzgs;
	}
	public String getCustomname() {
		return customname;
	}
	public void setCustomname(String customname) {
		this.customname = customname;
	}
	

}