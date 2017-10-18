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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SetLMCaseIdInSession extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetLMCaseIdInSession.class);
	protected int execute(ActionContext context, ActionResult result)throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_SETLMCASEIDINSESS","Executing SetLoadDetailIdInSession",100L);			
		StateInterface state = context.getState();
		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		com.epiphany.shr.ui.model.data.BioBean bioBean = null;
		HttpSession session = context.getState().getRequest().getSession();
		try
		{
			bioBean = uowb.getBioBean(bioRef);
			String caseId = bioBean.get("CASEID").toString();
			_log.debug("LOG_DEBUG_EXTENSION_SETLMCASEIDINSESS","CASEID:"+caseId,100L);	            
			session.setAttribute("LMCASEID",caseId);	   	            
		}
		catch(BioNotFoundException bioEx)
		{	            
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}						
		_log.debug("LOG_DEBUG_EXTENSION_SETLMCASEIDINSESS","Exiting SetLoadDetailIdInSession",100L);	        
		return RET_CONTINUE;
	}
}