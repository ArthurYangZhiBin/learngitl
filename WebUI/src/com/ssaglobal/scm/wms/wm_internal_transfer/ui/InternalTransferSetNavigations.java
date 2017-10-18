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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InternalTransferSetNavigations extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferSetNavigations.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","***Executing InternalTransferSetNavigations" ,100L);
		
		String val= "";
		StateInterface state = context.getState();
		try{
			if(state.getServiceManager().getUserContext().containsKey("POSTFINALIZE")){
				val = (String)state.getServiceManager().getUserContext().get("POSTFINALIZE");
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","***Setting navigations" ,100L);
				context.setNavigation(val);				
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","***Exiting InternalTransferSetNavigations" ,100L);
			}else{
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","***Nothing Posted!!!" ,100L);
			}
		}catch (NullPointerException e){
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","***Throwing exception - Null Pointer" ,100L);
			state.getServiceManager().getUserContext().remove("POSTFINALIZE");
			throw(e);			
		}	
		return RET_CONTINUE;
	}
}