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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SetFocusAfterSave extends com.epiphany.shr.ui.action.ActionExtensionBase{
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(SetFocusAfterSave.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{

	_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing SetFocusAfterSave",100L);
	StateInterface state = context.getState();
	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
	BioCollectionBean newFocus= null;
	
	if(state.getServiceManager().getUserContext().containsKey("QUERY"))
	{
		String val = (String)state.getServiceManager().getUserContext().get("QUERY");		
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +val,100L);
  		Query bioQry = new Query("wm_accessorial_charges", val, null);
		newFocus = uowb.getBioCollectionBean(bioQry);
	}
	else
	{
		//_log.debug("LOG_SYSTEM_OUT","\n\n*** NADA....",100L);
		
	}
	
	result.setFocus(newFocus);
	//state.getServiceManager().getUserContext().remove("QUERY");
	_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting SetFocusAfterSave",100L);
	return RET_CONTINUE;
	}
	
}
