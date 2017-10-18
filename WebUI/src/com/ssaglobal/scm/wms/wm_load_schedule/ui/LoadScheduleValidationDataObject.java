/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_load_schedule.ui;

public class LoadScheduleValidationDataObject {
	private String loadScheduleKey;
	private String lineNumber;
	private String owner;
	private String scheduleType;
	private String dayOfWeek;
	private String route;
	private String outbound;
	private String orderHandlingType;
	private String stop;
	private String customer;
	
	
	
	public String getLoadScheduleKey(){
		return this.loadScheduleKey;
	}
	public void setLoadScheduleKey(String loadScheduleKey){
		this.loadScheduleKey = loadScheduleKey;
	}
	public String getLineNumber(){
		return this.lineNumber;
	}
	public void setLineNumber(String lineNumber){
		this.lineNumber = lineNumber;
	}
	public String getOwner(){
		return this.owner;
	}
	public void setOwner(String owner){
		this.owner = owner;
	}
	public String getScheduleType(){
		return this.scheduleType;
	}
	public void setScheduleType(String scheduleType){
		this.scheduleType = scheduleType;
	}
		
	public String getDayOfWeek(){
		return this.dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek){
		this.dayOfWeek = dayOfWeek;
	}
	public String getRoute(){
		return this.route;
	}
	public void setRoute(String route){
		this.route = route;
	}
	public String getOutbound(){
		return this.outbound;
	}
	public void setOutbound(String outbound){
		this.outbound = outbound;
	}
	public String getOrderHandlingType(){
		return this.orderHandlingType;
	}
	public void setOrderHandlingType(String orderHandlingType){
		this.orderHandlingType = orderHandlingType;
	}
	public String getStop(){
		return this.stop;
	}
	public void setStop(String stop){
		this.stop = stop;
	}
	public String getCustomer(){
		return this.customer;
	}
	public void setCustomer(String customer){
		this.customer = customer;
	}
	
}
