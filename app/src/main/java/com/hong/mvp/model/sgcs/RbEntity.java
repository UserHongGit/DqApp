package com.hong.mvp.model.sgcs;

import java.util.Date;
import java.util.List;

public class RbEntity {
	private String did; // 数据存储位
	private Integer pro_param_id; // 施工参数id
	private String  paramname;//施工参数名称
	private Integer spid; // 工序id
	private String customid;
	private Integer pdid; // 工序数据id
	private Integer abc; // 工序数据id
	private String spname;//工序名称
	private String content; // 内容
	private String buildcontent; // 施工内容
	private String buildcontenttmp; // 施工内容模板
	private String tool_name;
	private String model;
	private String unit;
	private Integer numb;
	private String product_unit;
	private String prepare_unit;
	private String note;
	private String bzhsg;
	private Integer xh;
	private String sfwc;
	private String order_classes ;//班次	
	private String report_time ;//上报日期
	private String create_user;
	private Date create_time;
	private String ip;
	private String oilfield;
	private String last_user;
	private Date last_time;
	
	private String process;
	private String starttime;// 工序开始时间
	private String endtime;// 工序结束
	private List<RbEntity> entityList;
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public Integer getPro_param_id() {
		return pro_param_id;
	}
	public void setPro_param_id(Integer pro_param_id) {
		this.pro_param_id = pro_param_id;
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
	public String getSpname() {
		return spname;
	}
	public void setSpname(String spname) {
		this.spname = spname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<RbEntity> getEntityList() {
		return entityList;
	}
	public void setEntityList(List<RbEntity> entityList) {
		this.entityList = entityList;
	}
	public String getBuildcontent() {
		return buildcontent;
	}
	public void setBuildcontent(String buildcontent) {
		this.buildcontent = buildcontent;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getTool_name() {
		return tool_name;
	}
	public void setTool_name(String tool_name) {
		this.tool_name = tool_name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getNumb() {
		return numb;
	}
	public void setNumb(Integer numb) {
		this.numb = numb;
	}
	public String getProduct_unit() {
		return product_unit;
	}
	public void setProduct_unit(String product_unit) {
		this.product_unit = product_unit;
	}
	public String getPrepare_unit() {
		return prepare_unit;
	}
	public void setPrepare_unit(String prepare_unit) {
		this.prepare_unit = prepare_unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getXh() {
		return xh;
	}
	public void setXh(Integer xh) {
		this.xh = xh;
	}
	public String getBzhsg() {
		return bzhsg;
	}
	public void setBzhsg(String bzhsg) {
		this.bzhsg = bzhsg;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getCustomid() {
		return customid;
	}
	public void setCustomid(String customid) {
		this.customid = customid;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOilfield() {
		return oilfield;
	}
	public void setOilfield(String oilfield) {
		this.oilfield = oilfield;
	}

	public Date getLast_time() {
		return last_time;
	}
	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}
	public Integer getAbc() {
		return abc;
	}
	public void setAbc(Integer abc) {
		this.abc = abc;
	}
	public String getSfwc() {
		return sfwc;
	}
	public void setSfwc(String sfwc) {
		this.sfwc = sfwc;
	}
	public String getParamname() {
		return paramname;
	}
	public void setParamname(String paramname) {
		this.paramname = paramname;
	}
	public String getBuildcontenttmp() {
		return buildcontenttmp;
	}
	public void setBuildcontenttmp(String buildcontenttmp) {
		this.buildcontenttmp = buildcontenttmp;
	}
	public String getOrder_classes() {
		return order_classes;
	}
	public void setOrder_classes(String order_classes) {
		this.order_classes = order_classes;
	}
	public String getReport_time() {
		return report_time;
	}
	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}

	public String getLast_user() {
		return last_user;
	}

	public void setLast_user(String last_user) {
		this.last_user = last_user;
	}
}