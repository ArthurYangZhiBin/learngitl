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
package com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui;

public class InventoryReqToCountDataObject {

	private String requestKey;
	private String processFlag;
	private String ownerStart;
	private String ownerEnd;
	private String itemStart;
	private String itemEnd;
	private String locStart;
	private String locEnd;
	private String zoneStart;
	private String zoneEnd;
	private String areaStart;
	private String areaEnd;
	
	
	public String getRequestKey(){
		return this.requestKey;
	}
	public void setRequestKey(String requestKey){
		this.requestKey = requestKey;
	}
	public String getProcessFlag(){
		return this.processFlag;
	}
	public void setProcessFlag(String processFlag){
		this.processFlag = processFlag;
	}
	public String getOwnerStart(){
		return this.ownerStart;
	}
	public void setOwnerStart(String ownerStart){
		this.ownerStart = ownerStart;
	}
	public String getOwnerEnd(){
		return this.ownerEnd;
	}
	public void setOwnerEnd(String ownerEnd){
		this.ownerEnd = ownerEnd;
	}
	public String getItemStart(){
		return this.itemStart;
	}
	public void setItemStart(String itemStart){
		this.itemStart = itemStart;
	}		
	public String getItemEnd(){
		return this.itemEnd;
	}
	public void setItemEnd(String itemEnd){
		this.itemEnd = itemEnd;
	}
	public String getLocStart(){
		return this.locStart;
	}
	public void setLocStart(String locStart){
		this.locStart = locStart;
	}
	public String getLocEnd(){
		return this.locEnd;
	}
	public void setLocEnd(String locEnd){
		this.locEnd = locEnd;
	}
	public String getZoneStart(){
		return this.zoneStart;
	}
	public void setZoneStart(String zoneStart){
		this.zoneStart = zoneStart;
	}
	public String getZoneEnd(){
		return this.zoneEnd;
	}
	public void setZoneEnd(String zoneEnd){
		this.zoneEnd = zoneEnd;
	}
	public String getAreaStart(){
		return this.areaStart;
	}
	public void setAreaStart(String areaStart){
		this.areaStart = areaStart;
	}
	public String getAreaEnd(){
		return this.areaEnd;
	}
	public void setAreaEnd(String areaEnd){
		this.areaEnd = areaEnd;
	}
}

