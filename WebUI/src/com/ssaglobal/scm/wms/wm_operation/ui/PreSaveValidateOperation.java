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
package com.ssaglobal.scm.wms.wm_operation.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_invoice_processing.ui.EnableActionButtonOnRunBillling;

public class PreSaveValidateOperation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateOperation.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n*** IN PreSaveValidateOperation " +"\n\n",100L);
	  	 _log.debug("LOG_DEBUG_EXTENSION_OPERATION","Executing PreSaveValidateOperation",100L);
		StateInterface state = context.getState();	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);		
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);		
		DataBean detailFocus= detailForm.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		
		if(!detailForm.getName().equals("Blank"))
		{
		if(detailFocus.isTempBio())
		{
			RuntimeFormWidgetInterface primaryKey = detailForm.getFormWidgetByName("OPCLASSKEY");
			String value = primaryKey.getDisplayValue();
			
			String queryString = "wm_operation.OPCLASSKEY='" +value+"'";
			Query query = new Query("wm_operation", queryString, null);
			BioCollectionBean bioCollection = uow.getBioCollectionBean(query);
			
			try{
				if(bioCollection.size() >=1)
				{
					String [] param = new String[1];
					param[0] = value;
					throw new UserException("WMEXP_OPCLASSKEY", param);	
				}
			}
			catch(EpiDataException exp)
			{
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		
		}
		}

		
		
		_log.debug("LOG_DEBUG_EXTENSION_OPERATION","Exiting PreSaveValidateOperation",100L);
		return RET_CONTINUE;
	}
}
