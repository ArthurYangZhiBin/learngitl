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
package com.ssaglobal.scm.wms.wm_location.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class LocationCheckDigitComputedAttr implements ComputedAttributeSupport{
	private static String LOCATION = "LOC";
	private static String PROC_NAME = "NSPCHECKDIGIT";
	
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException{
		//Obtain backend computed value and assign to holder
		//Executes stored proceedure name:NSPCHECKDIGIT params:loc
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		//Store parameters for stored proceedure call
		params.add(new TextData(bio.get(LOCATION).toString()));
		//Set actionProperties for stored proceedure call
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName(PROC_NAME);
		EXEDataObject temp=null;
		try{
			//Run stored proceedure
			temp = WmsWebuiActionsImpl.doAction(actionProperties);
		}catch (WebuiException e) {
			e.printStackTrace();
		}
		String checkDigit = ""+temp.getAttribute(temp.getCurrentRow()).value;
		
		//assign value to widget
		return checkDigit;
	}
	
	public boolean supportsSet(String a, String b){
		return true;
	}
	
	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue){
	}
}