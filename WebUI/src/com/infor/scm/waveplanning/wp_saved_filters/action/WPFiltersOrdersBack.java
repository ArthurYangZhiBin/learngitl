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
import java.util.Hashtable;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class WPFiltersOrdersBack extends SaveAction{
	public static final String SESSION_KEY_FILTER_BIO_REF_TABLE = "wp.saved.filters.orders.back";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPFiltersOrdersBack.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Executing WPFiltersOrdersBack",100L);		
		StateInterface state = context.getState();							
		String interactionId = context.getState().getInteractionId();		
		Hashtable filterPKTable = (Hashtable)state.getRequest().getSession().getAttribute(SESSION_KEY_FILTER_BIO_REF_TABLE);
		if(filterPKTable == null){
			System.out.println("\n\nHere1\n\n");
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Could not get filter bio ref table from session...",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SAVED_FILTERS_CANNOT_GO_BACK_ODR",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		BioRef filterBioRef = (BioRef)filterPKTable.get(interactionId);
		if(filterBioRef == null){
			System.out.println("\n\nHere2\n\n");
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Could not get filter key from hashtable...",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SAVED_FILTERS_CANNOT_GO_BACK_ODR",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();	
		try {			
			result.setFocus(uow.getBioBean(filterBioRef));
		} catch (BioNotFoundException e) {
			System.out.println("\n\nHere3\n\n");
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Could not get bio using bioref...",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SAVED_FILTERS_CANNOT_GO_BACK_ODR",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		
		return RET_CONTINUE;	
	}
}
