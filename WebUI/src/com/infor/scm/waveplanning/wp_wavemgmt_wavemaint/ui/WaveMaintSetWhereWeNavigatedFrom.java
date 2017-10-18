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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

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

public class WaveMaintSetWhereWeNavigatedFrom extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	public static final String WAVEMGMT_ORIGIN_NORMAL = "NORMAL";

	public static final String WAVEMGMT_ORIGIN_LIST = "LIST";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintSetWhereWeNavigatedFrom.class);

	private String CONTEXT_WAVEMGMT_ORIGIN = "WAVEMGMT_ORIGIN";

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
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetWhereWeNavigatedFrom_execute",
				"Entering WaveMaintSetWhereWeNavigatedFrom", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		CONTEXT_WAVEMGMT_ORIGIN += state.getInteractionId();
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetWhereWeNavigatedFrom_execute", "Variable " + CONTEXT_WAVEMGMT_ORIGIN, SuggestedCategory.NONE);
		RuntimeFormInterface currentToolbar = state.getCurrentRuntimeForm();
		currentToolbar.getName();
		RuntimeFormInterface parentForm = currentToolbar.getParentForm(state);
		parentForm.getName();
		
		if(parentForm.isListForm())
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetWhereWeNavigatedFrom_execute", "Setting " + CONTEXT_WAVEMGMT_ORIGIN + " to " + WAVEMGMT_ORIGIN_LIST, SuggestedCategory.NONE);
			userContext.put(CONTEXT_WAVEMGMT_ORIGIN, WAVEMGMT_ORIGIN_LIST);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetWhereWeNavigatedFrom_execute", "Setting " + CONTEXT_WAVEMGMT_ORIGIN + " to " + WAVEMGMT_ORIGIN_NORMAL, SuggestedCategory.NONE);
			userContext.put(CONTEXT_WAVEMGMT_ORIGIN, WAVEMGMT_ORIGIN_NORMAL);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetWhereWeNavigatedFrom_execute",
				"Leaving WaveMaintSetWhereWeNavigatedFrom", SuggestedCategory.NONE);

		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
