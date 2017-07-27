package com.example.administrator.face.bean;

/**
 * 管理员表
 * */
public class Manager {
	private int mid;//管理员id
	private String mname;//管理员登录名
	private String mpassword;//管理员密码
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMpassword() {
		return mpassword;
	}
	public void setMpassword(String mpassword) {
		this.mpassword = mpassword;
	}
	
}
