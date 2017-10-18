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

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;



public class NavigateBackToRunBilling extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(NavigateBackToRunBilling.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing NavigateBackToRunBilling",100L);
		String val= "";
		StateInterface state = context.getState();
		
		try
		{
			if(state.getServiceManager().getUserContext().containsKey("NAVFORCANCEL"))
				val = (String)state.getServiceManager().getUserContext().get("NAVFORCANCEL");

		}
		catch (NullPointerException e)
		{
			state.getServiceManager().getUserContext().remove("NAVFORCANCEL");
			throw(e);
			
		}
		
		
		if(val.equals("YES"))
		{	
		context.setNavigation("closeModalDialog64");	
		}
		else if(val.equals("OK"))
		{
	
		QBEBioBean tempBio;
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		try {
			tempBio = uow.getQBEBioWithDefaults("wm_invoice_processing");
		} catch (DataBeanException e) {
			e.printStackTrace();
			state.getServiceManager().getUserContext().remove("NAVFORCANCEL");
			throw new UserException("ERROR_CREATE_TMP_BIO", new String[] { "wm_invoice_processing" });
		}
		
		context.setNavigation("closeModalDialog91");
		result.setFocus(tempBio);
		
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","\n\n****IN HERE",100L);
			context.setNavigation("closeModalDialog114");
		}
		
		state.getServiceManager().getUserContext().remove("NAVFORCANCEL");
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting NavigateBackToRunBilling",100L);
		return RET_CONTINUE;
	}
}
