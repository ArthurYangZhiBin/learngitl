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
package com.ssaglobal.scm.wms.wm_accessorial;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateAccessorialDelete extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateAccessorialDelete.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VAD","Executing ValidateAccessorialDelete",100L);		
		StateInterface state = context.getState();			
		RuntimeListFormInterface headerForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_accessorial_header_list_view",state);		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VAD","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VAD","Found Header Form:Null",100L);			
		if(headerForm != null){			
			ArrayList selectedItems = headerForm.getSelectedItems();
			if(selectedItems == null || selectedItems.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_VAD","Leaving ValidateAccessorialDelete",100L);				
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}else{
				Iterator headerBioBeanIter = selectedItems.iterator();
				try
				{
					BioBean bio;
					for(; headerBioBeanIter.hasNext();){            	 
						bio = (BioBean)headerBioBeanIter.next();
						String accessorialKeyStr = bio.getString("ACCESSORIALKEY");	
						if(accessorialKeyStr.equals("XXXXXXXXXX")){
							String args[] = new String[0];							
							String errorMsg = getTextMessage("WMEXP_CANNOT_DELETE_DEFAULT_ACCESSORIAL",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
				catch(EpiDataException ex)
				{
					ex.printStackTrace();
					_log.debug("LOG_DEBUG_EXTENSION_VAD","Leaving ValidateAccessorialDelete",100L);
					String args[] = new String[0];							
					String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);	
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VAD","Leaving ValidateAccessorialDelete",100L);
		return RET_CONTINUE;
		
	}	
}