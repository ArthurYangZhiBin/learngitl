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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

public class AccessorialDetailObject {
private String base;
private String descrip;
private String gldist;
private String masterUnits;
private String taxGroup;
private String rate;




public String getBase(){
	return this.base;
}
public void setBase(String base){
	this.base = base;
}

public String getDescrip(){
	return this.descrip;
}
public void setDescrip(String descrip){
	this.descrip = descrip;
}
	
public String getGldist(){
	return this.gldist;
}	
public void setGldist(String gldist){
	this.gldist = gldist;
}
public String getMasterUnits(){
	return this.masterUnits;
}
public void setMasterUnits(String masterUnits){
	this.masterUnits = masterUnits;
}
public String getTaxGroup(){
	return this.taxGroup;
}
public void setTaxGroup(String taxGroup){
	this.taxGroup = taxGroup;
}
public String getRate(){
	return this.rate;
}
public void setRate(String rate){
	this.rate = rate;
}


}
