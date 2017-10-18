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
package com.ssaglobal.scm.wms.wm_indirect_activity_setup.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreSaveValidateIndirectActivitySetup extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateIndirectActivitySetup.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
		
		
		_log.debug("LOG_DEBUG_EXTENSION_INDIRECT_ACTIVITY_CONFIG","**Executing PreSaveValidateIndirectActivitySetup ",100L);
		String field = getParameter("fieldForNonNegValid").toString();
		
		StateInterface state = context.getState();		
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		//_log.debug("LOG_SYSTEM_OUT","*** shellForm:" +shellForm.getName(),100L);
		
		SlotInterface slot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface form = state.getRuntimeForm(slot, null);
		DataBean focus = state.getRuntimeForm(slot, null).getFocus();
		_log.debug("LOG_DEBUG_EXTENSION_INDIRECT_ACTIVITY_CONFIG","Detail Form: "+form.getName(),100L);
		
		if(focus instanceof QBEBioBean)
		{
			focus = focus; 
		}
		else if(focus instanceof BioBean)
		{
			focus = focus;
		}
		
		Object duration= focus.getValue(field);
		if (duration != null && !duration.toString().equalsIgnoreCase(""))
		{
			int durationVal = Integer.parseInt(duration.toString());		
				if(durationVal < 1)
				{
					String [] param= new String[1];
					param[0]= field;
					String errorMsg = getTextMessage("WM_EXP_NONNEG",param,state.getLocale());
					_log.debug("LOG_DEBUG_EXTENSION_INDIRECT_ACTIVITY_CONFIG","**Invalid duration- Exiting",100L);						
					throw new UserException(errorMsg,new Object[0]);
				}
		}
		
		return RET_CONTINUE;
	}
}
