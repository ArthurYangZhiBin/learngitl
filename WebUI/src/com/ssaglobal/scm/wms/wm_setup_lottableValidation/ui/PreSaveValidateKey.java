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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.model.data.DataBean;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class PreSaveValidateKey extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateKey.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
		
		_log.debug("LOG_SYSTEM_OUT","\n\n*******PreSaveValidateKey ************\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing PreSaveValidateKey",100L);
	
		StateInterface state = context.getState();		
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();
        
		if(headerForm.getFocus().isTempBio())
		{
			RuntimeFormWidgetInterface key = headerForm.getFormWidgetByName("LOTTABLEVALIDATIONKEY");
			Object val = key.getValue();
			if (val != null)
			{				
				if(val.toString().length() <= 10)
				{
					
					//[^a-zA-Z0-9_]. 
                     if(val.toString().matches("[a-zA-Z0-9]+"))
                     {
                    	 String keyVal= val.toString().toUpperCase();
                    	 //check for duplicates
                    	 String query = "SELECT * " + "FROM LOTTABLEVALIDATION " 
                    	 + "WHERE (LOTTABLEVALIDATIONKEY = '" + keyVal + "') ";
			   
                    	 EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
                    	 	//Duplicate record
                    	 	if (results.getRowCount() >= 1)
                    	 	{	
                    	 		String [] param = new String[1];
                    	 		param[0] = keyVal;
                    	 		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Record Exists",100L);
                    	 		throw new UserException("WMEXP_LOTTABLE_VALIDATION_KEY", param);			
                    	 	}                    	 		 				        		
                     }
                     else 
                     {
                    	 String [] param = new String[1];
                    	 param[0] = val.toString();
                    	 throw new UserException("WMEXP_SPECIAL_CHARS", param);
					}
				}
				else
				{
					//invalid length
					String [] param = new String[1];
		    		param[0] = val.toString();
					throw new UserException("WMEXP_KEY_LENGTH", param);
				}
			}
		
		}
	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting PreSaveValidateKey",100L);
			return RET_CONTINUE;
	}	
}
