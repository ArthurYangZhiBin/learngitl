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
package com.ssaglobal.scm.wms.uiextensions;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.error.UnitOfWorkErrorAfterCommitException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;


public class HeaderOnlySave extends ActionExtensionBase{
	   protected static ILoggerCategory _log = LoggerFactory.getInstance(HeaderOnlySave.class);
	   
	   
	   public HeaderOnlySave() { 
	        _log.info("EXP_1","HeaderDetailSave Instantiated!!!",  SuggestedCategory.NONE);
	    }
	   
		protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	
			
	_log.debug("LOG_SYSTEM_OUT","\n\n#### it is in HeaderOnlySave******",100L); 		
	StateInterface state= null;
	//Get user entered criteria

		try{
			state = context.getState();			 
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			 
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			_log.debug("LOG_SYSTEM_OUT","\n\n*** SHELLFORM: " +shellForm.getName(),100L); 
			
			//get header data
		    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if(headerForm.isListForm()){
		    	String [] desc = new String[1];
		    	desc[0] = "";
		    	throw new UserException("List_Save_Error",desc);			
			}
			DataBean headerFocus = headerForm.getFocus();
			

			
			BioBean headerBioBean = null;
				if (headerFocus.isTempBio()) {//it is for insert header
			    _log.debug("LOG_SYSTEM_OUT","inserting header ******",100L);
			    headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
			    
				} else {//it is for update header
			    _log.debug("LOG_SYSTEM_OUT","updating header ******",100L);
			    headerBioBean = (BioBean)headerFocus;
				} 
			 			 
			uow.saveUOW(true);
			uow.clearState();
		    result.setFocus(headerBioBean);
		}
		//catch(Exception e){
			//e.printStackTrace();
		//}
		catch(UnitOfWorkException ex)
		{
			
				
			Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
			_log.debug("LOG_SYSTEM_OUT","\t\n\n\n\n^^^^^&&&Nested " + nested.getClass().getName(),100L);
			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity				
				throwUserException(ex, reasonCode, null);
			}
			else if(nested instanceof UnitOfWorkErrorAfterCommitException)
			{
				String error = nested.getMessage();
				throwUserException(ex, error, null);				
			}
			else
			{
				throwUserException(ex, "ERROR", null);
			}
		
		}	
			
//UnitOfWorkErrorAfterCommitException	
		return RET_CONTINUE;
		}
		
		
}		