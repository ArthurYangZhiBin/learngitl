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

package com.ssaglobal.scm.wms.wm_check_pack.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackResults;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackValidateContainer extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackValidateContainer.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 

	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {


		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface searchForm = FormUtil.findForm(currentRuntimeForm, "wm_check_pack_shell", "wm_check_pack_search_form", state);
		String container = searchForm.getFormWidgetByName("CONTAINER").getDisplayValue().toUpperCase();
		_log.debug("LOG_DEBUG_EXTENSION_CheckPackResultsFormPreRender_preRenderForm", "Searching for " + container, SuggestedCategory.NONE);

		CheckPackResults results = CheckPackUtil.search(context, container);
		if (results.isFoundResults() == false) {
			//throw error
			//blank out forms
			context.setNavigation("noresults");

			//SHow Message
			//			form.getFormWidgetByName(errorWidgetName).setLabel(RuntimeFormWidgetInterface.LABEL_VALUE, errorMessage);
			//			currentRuntimeForm.setError("WMEXP_CHECK_PACK_NO_CONTAINER", new Object[] { container });
			if (results.getType() == null) {
				if(results.isInnerPackage() == true) {
					setError(context, searchForm, "WMEXP_CHECK_PACK_INNER_PACKAGE", new Object[] { container });
				}else {
					setError(context, searchForm, "WMEXP_CHECK_PACK_NO_CONTAINER", new Object[] { container });
				}
			} else {
				setError(context, searchForm, "WMEXP_CHECK_PACK_EMPTY_CONTAINER", new Object[] { container });
			}

		} else {
			result.setFocus(results.getPickDetails());
			context.setNavigation("results");

			setError(context, searchForm, null, new Object[] {});
		}
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private void setError(ActionContext context, RuntimeFormInterface currentRuntimeForm, String error, Object[] parameters) {
		SlotInterface errorSlot = currentRuntimeForm.getSubSlot("error");
		RuntimeFormInterface errorForm = context.getState().getRuntimeForm(errorSlot, null);
		if (errorForm != null) {
			errorForm.setError(error, parameters);
		}
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is
	 * fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
