package com.example.administrator.face.bean;

/**
 *学生表  
 * */
public class Student {
	private int sid;//学生id
	private String sname;//学生姓名
	private String spassword;//学生密码
	private String sphone;//学生电话
	private String spic;//学生头像路径、
	private String ssex;//学生性别
	private String spicid;//学生头像id
	private String sclass;//学生所在班级
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSpassword() {
		return spassword;
	}
	public void setSpassword(String spassword) {
		this.spassword = spassword;
	}
	public String getSphone() {
		return sphone;
	}
	public void setSphone(String sphone) {
		this.sphone = sphone;
	}
	public String getSpic() {
		return spic;
	}
	public void setSpic(String spic) {
		this.spic = spic;
	}
	public String getSsex() {
		return ssex;
	}
	public void setSsex(String ssex) {
		this.ssex = ssex;
	}
	public String getSpicid() {
		return spicid;
	}
	public void setSpicid(String spicid) {
		this.spicid = spicid;
	}
	public String getSclass() {
		return sclass;
	}
	public void setSclass(String sclass) {
		this.sclass = sclass;
	}
}
