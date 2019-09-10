package com.hong.http.model;



public class ZyjdPicEntity {
	private String originalpicname; // 原始图片名称
	private String presentpicname; // 现图片名称
	private String jdid; // 监督ID
	private String uploader; // 上传人
	private String uploadtime; // 上传时间
	private String picpath; // 图片路径
	// 确定数据类型，set&get
	

	public String getOriginalpicname() {
		return originalpicname;
	}

	public ZyjdPicEntity() {
		super();
	}

	public ZyjdPicEntity(String originalpicname, String presentpicname,
			String jdid, String uploader, String uploadtime, String picpath) {
		super();
		this.originalpicname = originalpicname;
		this.presentpicname = presentpicname;
		this.jdid = jdid;
		this.uploader = uploader;
		this.uploadtime = uploadtime;
		this.picpath = picpath;
	}

	public void setOriginalpicname(String originalpicname) {
		this.originalpicname = originalpicname;
	}

	public String getPresentpicname() {
		return presentpicname;
	}

	public void setPresentpicname(String presentpicname) {
		this.presentpicname = presentpicname;
	}

	public String getJdid() {
		return jdid;
	}

	public void setJdid(String jdid) {
		this.jdid = jdid;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

}
