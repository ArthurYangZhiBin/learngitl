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
import org.apache.commons.lang.StringUtils;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_check_pack.action.CheckPackSetPrinters.Printer;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackVoid extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackVoid.class);
	private static final String KSHIP_TRANSACTION_VOID = "Void";

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
		RuntimeFormInterface searchForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface resultsForm = FormUtil.findForm(searchForm, "wm_check_pack_shell", "wm_check_pack_results_form", state);


		//printer
		Printer printerObj = (Printer) (SessionUtil.getInteractionSessionAttribute(CheckPackSetPrinters.PRINTERS, state) == null ? null : SessionUtil.getInteractionSessionAttribute(
				CheckPackSetPrinters.PRINTERS, state));
		String labelPrinter = printerObj.getLabelPrinter();
		SessionUtil.setInteractionSessionAttribute(CheckPackSetPrinters.PRINTERS, printerObj, state);

		String transactiontype = KSHIP_TRANSACTION_VOID;
		String id = searchForm.getFormWidgetByName("CONTAINER").getDisplayValue().toUpperCase();
		String type = resultsForm.getFormWidgetByName("TYPE").getDisplayValue();
		RuntimeFormWidgetInterface scacWidget = resultsForm.getFormWidgetByName("CARRIER");
		String scacCode = (String) scacWidget.getValue();
		RuntimeFormWidgetInterface serviceWidget = resultsForm.getFormWidgetByName("SERVICES");
		String service = (String) serviceWidget.getValue();
		String copies = (String) resultsForm.getFormWidgetByName("NUM_LABELS").getValue();
		String weight = (String) resultsForm.getFormWidgetByName("WEIGHT").getValue();
		String height = (String) resultsForm.getFormWidgetByName("HEIGHT").getValue();
		String length = (String) resultsForm.getFormWidgetByName("LENGTH").getValue();
		String width = (String) resultsForm.getFormWidgetByName("WIDTH").getValue();
		String orderkey = resultsForm.getFormWidgetByName("ORDER").getDisplayValue();
		String source = "CHECKPACK";

		//TODO: VALIDATION
		spsInstallFlagValidation(state);

		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData(labelPrinter));
		params.add(new TextData(transactiontype));
		params.add(new TextData(id));
		params.add(new TextData(type));
		params.add(new TextData(scacCode));
		params.add(new TextData(service));
		params.add(new TextData(copies));
		params.add(new TextData(weight));
		params.add(new TextData(height));
		params.add(new TextData(length));
		params.add(new TextData(width));
		params.add(new TextData(orderkey));
		params.add(new TextData(source));

		for (int i = 0; i < params.size(); i++) {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackVoid_execute", "SPS Parameters " + i + " " + params.get(i), SuggestedCategory.NONE);
		}

		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("NSPRFSPS");
		EXEDataObject spResult = null;
		try {
			spResult = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
		
		DataValue errorMsg = spResult.getAttribValue(new TextData("ErrorMsg"));
		if(errorMsg != null && !StringUtils.isBlank(errorMsg.getAsString())){
			throw new UserException(errorMsg.getAsString(), new Object[]{});
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
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

	private void spsInstallFlagValidation(StateInterface state) throws UserException {
		//Ensure SPS_INSTALLED
		NSQLConfigUtil spsInstalled = new NSQLConfigUtil(state, "SPS_INSTALLED");
		if (spsInstalled.isOff() == true) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "SPS_INSTALLED is not on", SuggestedCategory.NONE);
			throw new UserException("WMEXP_SPS_NOT_INSTALLED", new Object[] {});
	
		}
	}
}
