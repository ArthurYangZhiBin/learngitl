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

public class AssignAccessorialDataObject {


	private String storer;
	private String lot;
	private String item;
	private String sourceKey;
	private String sourceType;
	private String query;
	private String billedUnits;
	
	
	
	public String getStorer(){
		return this.storer;
	}
	public void setStorer(String storer){
		this.storer = storer;
	}

	public String getlot(){
		return this.lot;
	}
	public void setlot(String lot){
		this.lot = lot;
	}
		
	public String getItem(){
		return this.item;
	}	
	public void setItem(String item){
		this.item = item;
	}
	public String getSourceKey(){
		return this.sourceKey;
	}
	public void setSourceKey(String key){
		this.sourceKey = key;
	}
	public String getSourceType(){
		return this.sourceType;
	}
	public void setSourceType(String type){
		this.sourceType = type;
	}
	public String getQuery(){
		return this.query;
	}
	public void setQuery(String query){
		this.query = query;
	}
	public String getBilledUnits(){
		return this.billedUnits;
	}
	public void setBilledUnits(String billedUnits){
		this.billedUnits = billedUnits;
	}

	
}
