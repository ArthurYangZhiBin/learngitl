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

import java.util.Date;

import com.ssaglobal.scm.wms.wm_serial_number.ui.ISerialNumber;

public class CCDetailSerialTmpDTO implements ISerialNumber{

	private Date addDate=null;
	private String addWho=null;
	private String ccDetailKey=null;
	private String ccKey=null;
	private Date editDate=null;
	private String editWho=null;
	private String id = null;
	private String loc = null;
	private String lot = null;
	private String qty = null;
	private String data2 = null;
	private String data3 = null;
	private String data4 = null;
	private String data5 = null;
	private String grossweight = null;
	private String netweight = null;

	private String serialnumber = null;
	private String serialnumberlong = null;
	private String sku = null;
	private String storerkey=null;
	private String whseid =null;
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	public String getAddWho() {
		return addWho;
	}
	public void setAddWho(String addWho) {
		this.addWho = addWho;
	}
	public String getCcDetailKey() {
		return ccDetailKey;
	}
	public void setCcDetailKey(String ccDetailKey) {
		this.ccDetailKey = ccDetailKey;
	}
	public String getCcKey() {
		return ccKey;
	}
	public void setCcKey(String ccKey) {
		this.ccKey = ccKey;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public String getEditWho() {
		return editWho;
	}
	public void setEditWho(String editWho) {
		this.editWho = editWho;
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
	public String getWhseid() {
		return whseid;
	}
	public void setWhseid(String whseid) {
		this.whseid = whseid;
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
	public String getNetweight() {
		return netweight;
	}
	public void setNetweight(String netweight) {
		this.netweight = netweight;
	}
	
	
	@Override
	public String toString() {
		String msg = "CCDetailSerailTmpDTO:\n";
		msg += "cckey:"+this.ccKey+"\n";
		msg += "ccdetailkey:"+this.ccDetailKey+"\n";
		msg += "data2:"+this.data2+"\n";
		msg += "data3:"+this.data3+"\n";
		msg += "data4:"+this.data4+"\n";
		msg += "data5:"+this.data5+"\n";
		msg += "netweight:"+this.netweight+"\n";
		msg += "serialnumber:"+this.serialnumber+"\n";
		msg += "serialnumberlong:"+this.serialnumberlong+"\n";
		msg += "sku:"+this.sku+"\n";
		msg += "storerkey:"+this.storerkey+"\n";
		msg += "lot:"+this.lot+"\n";
		msg += "loc:"+this.loc+"\n";
		msg += "id:"+this.id+"\n";
		
		return msg;
	}

}
