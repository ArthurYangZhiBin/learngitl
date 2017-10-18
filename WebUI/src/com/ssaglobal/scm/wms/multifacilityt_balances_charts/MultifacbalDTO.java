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
package com.ssaglobal.scm.wms.multifacilityt_balances_charts;

public class MultifacbalDTO {

	private String owner=null;
	private String onhand =null;
	private String allocated =null;
	private String qtyintransit=null;
	private String picked=null;
	private String item=null;
	private String serialkey=null;
	private String warehouse=null;
	private String distributioncenter=null;
	private String division=null;
	private String avaliable=null;
	
	public String getAllocated() {
		return allocated;
	}
	public void setAllocated(String allocated) {
		this.allocated = allocated;
	}
	public String getDistributioncenter() {
		return distributioncenter;
	}
	public void setDistributioncenter(String distributioncenter) {
		this.distributioncenter = distributioncenter;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getOnhand() {
		return onhand;
	}
	public void setOnhand(String onhand) {
		this.onhand = onhand;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPicked() {
		return picked;
	}
	public void setPicked(String picked) {
		this.picked = picked;
	}
	public String getQtyintransit() {
		return qtyintransit;
	}
	public void setQtyintransit(String qtyintransit) {
		this.qtyintransit = qtyintransit;
	}
	public String getSerialkey() {
		return serialkey;
	}
	public void setSerialkey(String serialkey) {
		this.serialkey = serialkey;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getAvaliable() {
		return avaliable;
	}
	public void setAvaliable(String avaliable) {
		this.avaliable = avaliable;
	}
	
	
}
