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
package com.ssaglobal.scm.wms.wm_integrationlogs.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_indirect_activity.ui.LDAPUserIDQueryAction;


public class PreSaveIntegrationlogs extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveIntegrationlogs.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		String listSlot1 = "list_slot_1";
		
		RuntimeFormInterface headerForm = state.getRuntimeForm(shell.getSubSlot(listSlot1), null);
		_log.debug("LOG_SYSTEM_OUT","********HEADER FORM = "+ headerForm,100L);
		if (headerForm != null) {
			DataBean focus = headerForm.getFocus();
			if (focus instanceof BioBean){
				BioBean headerBioBean = (BioBean)focus;
				int size = headerBioBean.getUpdatedAttributes().size();
				for (int i=0; i< size; i++){
					String AttrUpdated = headerBioBean.getUpdatedAttributes().get(i).toString();
					_log.debug("LOG_SYSTEM_OUT","Attr Updated = "+ AttrUpdated ,100L);
					String flag = focus.getValue(AttrUpdated).toString();
					if (!(flag.equalsIgnoreCase("10"))){
						throw new FormException("WMEXP_VALIDATEFLAGS", new Object[]{});
					}
				}
			}
		}
		return RET_CONTINUE;
	}
}

