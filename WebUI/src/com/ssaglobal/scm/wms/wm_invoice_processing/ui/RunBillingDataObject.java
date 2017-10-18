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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RunBillingDataObject implements Serializable{

	private String ownerStart;
	private String ownerEnd;
	private String billingStart;
	private String billingEnd;
	private String chargeType;
	private Date date;
	
	
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
	public String getBillingStart(){
		return this.billingStart;
	}
	public void setBillingStart(String billingStart){
		this.billingStart = billingStart;
	}
	public String getBillingEnd(){
		return this.billingEnd;
	}
	public void setBillingEnd(String billingEnd){
		this.billingEnd = billingEnd;
	}
	public String getChargeType(){
		return this.chargeType;
	}
	public void setChargeType(String chargeType){
		this.chargeType = chargeType;
	}
	public Date getDate(){
		return this.date;
	}
	public void setDate(Date date){
		this.date = date;
	}
}
