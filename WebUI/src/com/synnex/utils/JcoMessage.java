package com.synnex.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JcoMessage {
    private String msg;
    private boolean flag=true;
    private String task_name;
    private List<JcoDataMessage> jcoDataMessages=new ArrayList<JcoDataMessage>();
    private List<InterfaceLog> interfaceLogs=new ArrayList<InterfaceLog>();
    
    private Map map=new HashMap();
    
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public List<InterfaceLog> getInterfaceLogs() {
		return interfaceLogs;
	}
	public void setInterfaceLogs(List<InterfaceLog> interfaceLogs) {
		this.interfaceLogs = interfaceLogs;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public List<JcoDataMessage> getJcoDataMessages() {
		return jcoDataMessages;
	}
	public void setJcoDataMessages(List<JcoDataMessage> jcoDataMessages) {
		this.jcoDataMessages = jcoDataMessages;
	}
}
