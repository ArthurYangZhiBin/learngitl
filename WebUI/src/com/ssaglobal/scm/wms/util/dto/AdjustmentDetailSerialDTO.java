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
package com.ssaglobal.scm.wms.util.dto;

public class AdjustmentDetailSerialDTO {

	private String serialnumber = null;
	private String adjustmentkey = null;
	private String adjustmentlinenumber =null;
	private String storerkey = null;
	private String sku = null;
	private String lot = null;
	private String id = null;
	private String loc = null;
	private String qty = null;
	private String data2 = null;
	private String data3 = null;
	private String data4 = null;
	private String data5 = null;
	private String grossweight = null;
	private String netweight = null;
	private String serialnumberlong = null;
	
	public String getAdjustmentkey() {
		return adjustmentkey;
	}
	public void setAdjustmentkey(String adjustmentkey) {
		this.adjustmentkey = adjustmentkey;
	}
	public String getAdjustmentlinenumber() {
		return adjustmentlinenumber;
	}
	public void setAdjustmentlinenumber(String adjustmentlinenumber) {
		this.adjustmentlinenumber = adjustmentlinenumber;
	}
	public String getData2() {
		return data2;
	}
	public void setData2(String data2) {
		this.data2 = data2;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	public String getData4() {
		return data4;
	}
	public void setData4(String data4) {
		this.data4 = data4;
	}
	public String getData5() {
		return data5;
	}
	public void setData5(String data5) {
		this.data5 = data5;
	}
	public String getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(String grossweight) {
		this.grossweight = grossweight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public String getNetweight() {
		return netweight;
	}
	public void setNetweight(String netweight) {
		this.netweight = netweight;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getSerialnumberlong() {
		return serialnumberlong;
	}
	public void setSerialnumberlong(String serialnumberlong) {
		this.serialnumberlong = serialnumberlong;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getStorerkey() {
		return storerkey;
	}
	public void setStorerkey(String storerkey) {
		this.storerkey = storerkey;
	}
	
	@Override
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("AdjusmentDetailSerialTmpDTO object:\n");
		message.append( "[serialnumber]:"+serialnumber+"\n");
		message.append( "[adjusmentkey]:"+adjustmentkey+"\n");
		message.append( "[adjustmentlinenumber]:"+adjustmentlinenumber+"\n");
		message.append( "[storerkey]:"+storerkey+"\n");
		message.append( "[sku]:"+sku+"\n");
		message.append( "[data2]:"+data2+"\n");
		message.append( "[data3]:"+data3+"\n");
		message.append( "[data4]:"+data4+"\n");
		message.append( "[data5]:"+data5+"\n");
		message.append( "[qty]:"+qty+"\n");
		message.append( "[netweight]:"+netweight+"\n");
		message.append( "[grossweight]:"+grossweight+"\n");
		message.append( "[serialnumberlong]:"+serialnumberlong+"\n");
		return message.toString();
	}

}
