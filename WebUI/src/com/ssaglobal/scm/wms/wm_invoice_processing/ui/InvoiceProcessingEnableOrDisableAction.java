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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class InvoiceProcessingEnableOrDisableAction extends FormWidgetExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InvoiceProcessingEnableOrDisableAction.class);
	
	
 	 protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiDataException, UserException{
	
 		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing InvoiceProcessingEnableOrDisableAction",100L);
 	
 		 String queryForm = "wm_invoice_processing_formslot_view";
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
 		//_log.debug("LOG_SYSTEM_OUT","\nFORM NAME: " +headerForm.getName(),100L);
 		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","FORM NAME: " +headerForm.getName(),100L);
 		

 		if(headerForm.getName().equalsIgnoreCase(queryForm))
 		{	
		Query qry = new Query("wm_invoice_processing", "wm_invoice_processing.STATUS ='5'" , null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean newFocus = uow.getBioCollectionBean(qry);				
		//_log.debug("LOG_SYSTEM_OUT","\n\n Pending Invoices: " + newFocus.size(),100L);
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Pending Invoices: " + newFocus.size(),100L);
			if (newFocus.size() >= 1)
			{
			Object val = String.valueOf(newFocus.size());
			state.getServiceManager().getUserContext().put("CANCELCOUNT", val);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
			throw new FormException("WMEXP_INV_POSTORCANCEL", new Object[1]);
			}
			else
			{
			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
			}
		}
 		else
 			widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
 		
 		
 		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting InvoiceProcessingEnableOrDisableAction",100L);
		return RET_CONTINUE;
	}
}