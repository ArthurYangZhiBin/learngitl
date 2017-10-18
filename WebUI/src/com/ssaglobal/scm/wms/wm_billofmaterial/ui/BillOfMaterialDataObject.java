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
package com.ssaglobal.scm.wms.wm_billofmaterial.ui;

public class BillOfMaterialDataObject {

	private String bomKey;
	private String parentStorer;
	private String parentSKU;
	private String storer;
	private String item;
	private String subOwner;
	private String subItem;
	private String reqQty;
	
	public String getBomKey(){
		return this.bomKey;
	}
	public void setBomKey(String bomKey){
		this.bomKey = bomKey;
	}
	public String getParentStorer(){
		return this.parentStorer;
	}
	public void setParentStorer(String parentStorer){
		this.parentStorer = parentStorer;
	}
	public String getParentSKU(){
		return this.parentSKU;
	}
	public void setParentSKU(String parentSKU){
		this.parentSKU = parentSKU;
	}
	public String getStorer(){
		return this.storer;
	}
	public void setStorer(String storer){
		this.storer = storer;
	}
		
	public String getItem(){
		return this.item;
	}
	public void setItem(String item){
		this.item = item;
	}
	public String getSubOwner(){
		return this.subOwner;
	}
	public void setSubOwner(String subOwner){
		this.subOwner = subOwner;
	}
	public String getSubItem(){
		return this.subItem;
	}
	public void setSubItem(String subItem){
		this.subItem = subItem;
	}
	public String getreqQty(){
		return this.reqQty;
	}
	public void setreqQty(String reqQty){
		this.reqQty = reqQty;
	}
}
