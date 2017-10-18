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
package com.infor.scm.waveplanning.wp_saved_filters.action;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class WPDeleteFilterDetails extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPDeleteFilterDetails.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Executing WPSaveFilter",100L);			
		StateInterface state = context.getState();	
		
		//Get details list form
		ArrayList tabs = new ArrayList();
		tabs.add("wp_saved_filters_filter_detail_tab");
		RuntimeListFormInterface filterDetailsForm = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_saved_filters_filter_detail_list",tabs,state);				
		if(filterDetailsForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Found filterDetailsForm Form:"+filterDetailsForm.getName(),100L);			
		else{
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Found filterDetailsForm Form Form:Null",100L);
			return RET_CONTINUE;
		}
				
		//Get selected items
		ArrayList selectedItems = filterDetailsForm.getSelectedItems();
		if(selectedItems == null || selectedItems.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_DELETEFILTER","Nothing seleced... Exiting!",100L);
			return RET_CONTINUE;
		}
		
		//Delete selected items
		for(int i = 0; i < selectedItems.size(); i++){
			try {
				((Bio)selectedItems.get(i)).delete();
			} catch (EpiDataException e) {
				_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while deleteing record",100L);										
				_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
		try {
			state.getDefaultUnitOfWork().saveUOW();			
		} catch (EpiException e) {										
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while deleteing record",100L);										
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Exiting WPSaveFilter",100L);
		return RET_CONTINUE;
		
		
	}	
	
}