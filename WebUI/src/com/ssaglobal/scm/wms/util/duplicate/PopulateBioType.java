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
package com.ssaglobal.scm.wms.util.duplicate;

import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;

public class PopulateBioType {

	private boolean success;
	//private DataBean dupDataBean;
	private QBEBioBean qbeBioBean;
	private String widgetName;
	
	
	public String getWidgetName(){
		return this.widgetName;
	}
	
	public void setWidgetName(String _widgetName){
		widgetName = _widgetName;
	}
	
	public QBEBioBean getqbeBioBean(){
		return this.qbeBioBean;
	}
	
	public void setqbeBioBean(QBEBioBean _qbeBioBean){
		qbeBioBean = _qbeBioBean;
	}
	
	public boolean getStatus(){
		return this.success;
	}

	public void setStatus(boolean _success){
		this.success=_success;

	}
	
	
}
