package com.synnex.utils;

import java.sql.Date;

public class InterfaceLog {
	private String int_name;
	private String exe_user;
	private String pk_id;
	private String pk_id2;
	private String exe_result;
	private String message;
	private Date exe_date;
	private int seq;
	public String getInt_name() {
		return int_name;
	}
	public void setInt_name(String int_name) {
		this.int_name = int_name;
	}
	public String getExe_user() {
		return exe_user;
	}
	public void setExe_user(String exe_user) {
		this.exe_user = exe_user;
	}
	public String getPk_id() {
		return pk_id;
	}
	public void setPk_id(String pk_id) {
		this.pk_id = pk_id;
	}
	public String getPk_id2() {
		return pk_id2;
	}
	public void setPk_id2(String pk_id2) {
		this.pk_id2 = pk_id2;
	}
	public String getExe_result() {
		return exe_result;
	}
	public void setExe_result(String exe_result) {
		this.exe_result = exe_result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getExe_date() {
		return exe_date;
	}
	public void setExe_date(Date exe_date) {
		this.exe_date = exe_date;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
}
