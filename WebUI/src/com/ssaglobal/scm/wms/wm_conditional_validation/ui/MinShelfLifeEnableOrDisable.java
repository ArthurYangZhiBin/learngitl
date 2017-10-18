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

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class MinShelfLifeEnableOrDisable extends FormWidgetExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MinShelfLifeEnableOrDisable.class);
	public MinShelfLifeEnableOrDisable(){
	}
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		try {			
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Executing MinShelfLifeEnableOrDisable",100L);
			DataBean focus = state.getFocus(); 
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();	
			
			RuntimeFormWidgetInterface tempValue = currentForm.getFormWidgetByName("TYPE");
			String typeValue = null;
			
	
			
			
				
				Object val= tempValue.getValue();				
				if (val != null)
				{								
					typeValue = val.toString();
					typeValue.trim();
			
					if(typeValue.equals("PICK") || typeValue.equals("SHIP") || typeValue.equals("PREAL")) //AW 07/21/09 Machine:2073672 SDIS:05256 end
						widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else					
						widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						
				}									
			}     
		catch(Exception e) {     
	          // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	    } 	
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Exiting MinShelfLifeEnableOrDisable",100L);
		return RET_CONTINUE;
	}
}