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
package com.infor.scm.wms.util.datalayer.driver;

import com.infor.scm.wms.util.validation.screen.BaseScreenVO;


public class DataLayerUpdateData{
	private String bioName = null;
	private BaseScreenVO screenObject = null;	
	public String getBioName() {
		return bioName;
	}
	public void setBioName(String bioName) {
		this.bioName = bioName;
	}
	
	public BaseScreenVO getScreenObject() {
		return screenObject;
	}
	public void setScreenObject(BaseScreenVO screenObject) {
		this.screenObject = screenObject;
	}
	public DataLayerUpdateData(String tableName, BaseScreenVO screenObject) {
		super();
		this.bioName = tableName;
		this.screenObject = screenObject;
	}
		
	
}