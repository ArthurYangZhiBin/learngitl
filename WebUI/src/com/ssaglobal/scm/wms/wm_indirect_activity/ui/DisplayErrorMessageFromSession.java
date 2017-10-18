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

package com.ssaglobal.scm.wms.wm_indirect_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DisplayErrorMessageFromSession extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisplayErrorMessageFromSession.class);
	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "START OF ERROR MESSAGE DISPLAY" + "\n", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		String sessionVariable = getParameterString("ERRORMESSAGEVARIABLE");
		HttpSession session = state.getRequest().getSession();
		Object error = session.getAttribute(sessionVariable);
		
		if (error != null)
		{
			session.removeAttribute(sessionVariable);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n" + error.toString(), SuggestedCategory.NONE);
			throw new UserException(error.toString(), new Object[] {});
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "No message in session" + "\n", SuggestedCategory.NONE);
		}
		
		//Try from UserContext
		error = userContext.get(sessionVariable);
		if (error != null)
		{
			userContext.remove(sessionVariable);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n\n" + error.toString(), SuggestedCategory.NONE);
			throw new UserException(error.toString(), new Object[] {});
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "No message in userContext" + "\n", SuggestedCategory.NONE);
		}

		return RET_CONTINUE;
	}

}
