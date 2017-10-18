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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class CheckDigitPopulate extends FormWidgetExtensionBase {
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws UserException{ 
		if(!(widget.getForm().getFocus().isTempBio())){
			//initialize widget value holder *INITIALIZE TO NULL*
			String location = state.getCurrentRuntimeForm().getFormWidgetByName("LOC").getDisplayValue();
			
			//Obtain backend computed value and assign to holder
			//Executes stored proceedure name:NSPCHECKDIGIT params:loc
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			//Store parameters for stored proceedure call
			params.add(new TextData(location));
			//Set actionProperties for stored proceedure call
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPCHECKDIGIT");
			EXEDataObject temp=null;
			try{
				//Run stored proceedure
				temp = WmsWebuiActionsImpl.doAction(actionProperties);
			}catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
			String checkDigit = ""+temp.getAttribute(temp.getCurrentRow()).value;
			
			//assign value to widget
			widget.setValue(checkDigit);
		}
		widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, "true");
	    
	    return RET_CONTINUE;	
	}
}