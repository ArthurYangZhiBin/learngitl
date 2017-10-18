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
package com.ssaglobal.scm.wms.wm_conditional_validation.ui;

public class ConditionalValidationDataObject {

	private String condValidKey;
	private String storer;
	private String customer;
	private String type;
	private String item;
	private String minShelfLife;
	
	
	public String getCondValidKey(){
		return this.condValidKey;
	}
	public void setCondValidKey(String condValidKey){
		this.condValidKey = condValidKey;
	}
	public String getStorer(){
		return this.storer;
	}
	public void setStorer(String storer){
		this.storer = storer;
	}
	public String getCustomer(){
		return this.customer;
	}
	public void setCustomer(String customer){
		this.customer = customer;
	}
	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
		
	public String getItem(){
		return this.item;
	}
	public void setItem(String item){
		this.item = item;
	}
	public String getMinShelfLife(){
		return this.minShelfLife;
	}
	public void setMinShelfLife(String minShelfLife){
		this.minShelfLife = minShelfLife;
	}
	
}
