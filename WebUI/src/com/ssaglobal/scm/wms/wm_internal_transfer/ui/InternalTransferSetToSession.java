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

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InternalTransferSetToSession extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferSetToSession.class);
	
	protected int execute(ActionContext context, ActionResult result) throws FormException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing InternalTransferSetToSession " ,100L);
		StateInterface state = context.getState();
		String bioRefString = state.getBucketValueString("listTagBucket");

		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioBean bioBean = null;

		try{
			bioBean = uowb.getBioBean(bioRef);
			result.setFocus(bioBean);
		}catch(BioNotFoundException bioEx){
			_logger.error(bioEx);
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing exception " ,100L);
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}
		HttpSession session = state.getRequest().getSession();
		session.setAttribute("BIO_REF", bioRef);

		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting InternalTransferSetToSession " ,100L);
		return RET_CONTINUE;
	}
}