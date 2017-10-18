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

package com.ssaglobal.scm.wms.wm_assigned_work.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class AssignedWorkSort extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignedWorkSort.class);

	private static final String DESCENDING_FIRST = "Descending First";
	private static final String ASCENDING_FIRST = "Ascending First";
	private static final String AWSORTING = "AWSORTING";

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

		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String id = state.getInteractionId();
		
		String contextKey = AWSORTING + id;
		Object currentSorting = userContext.get(contextKey);
		if(currentSorting == null)
		{
			Object formSorting = form.getProperty("sorting");
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkSort", "No sorting in context, putting form default in: " + formSorting, SuggestedCategory.NONE);
			userContext.put(contextKey, formSorting);
		}
		else if(ASCENDING_FIRST.equals(currentSorting))
		{
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkSort", "Acending First in context, going to switch to Descending", SuggestedCategory.NONE);
			userContext.put(contextKey, DESCENDING_FIRST);
			form.setProperty("sorting", DESCENDING_FIRST);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_AssignedWorkSort", "Descending First in context, going to switch to Ascending", SuggestedCategory.NONE);
			userContext.put(contextKey, ASCENDING_FIRST);
			form.setProperty("sorting", ASCENDING_FIRST);
		}

		return RET_CONTINUE;
	}

	
}
