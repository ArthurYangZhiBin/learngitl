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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackSetPrinters extends com.epiphany.shr.ui.action.ActionExtensionBase {

	public static final String PRINTERS = "CHECK_PACK_PRINTERS";

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
		RuntimeFormInterface initialForm = state.getCurrentRuntimeForm();
		RuntimeFormWidgetInterface reportPrnWidgt = initialForm.getFormWidgetByName("REPORTPRINTER");
		RuntimeFormWidgetInterface labelPrnWidgt = initialForm.getFormWidgetByName("LABELPRINTER");

		String reportPrinter = reportPrnWidgt.getValue() == null ? "" : (String) reportPrnWidgt.getValue();
		String labelPrinter = labelPrnWidgt.getValue() == null ? "" : (String) labelPrnWidgt.getValue();
		if (StringUtils.isEmpty(reportPrinter)) {
			throw new UserException("WMEXP_REQ", new Object[] { FormUtil.getWidgetLabel(context, reportPrnWidgt) });
		}
		if (StringUtils.isEmpty(labelPrinter)) {
			throw new UserException("WMEXP_REQ", new Object[] { FormUtil.getWidgetLabel(context, labelPrnWidgt) });
		}
		Printer printers = new Printer();
		printers.setLabelPrinter(labelPrinter);
		printers.setReportPrinter(reportPrinter);

		SessionUtil.setInteractionSessionAttribute(PRINTERS, printers, state);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	class Printer {
		private String reportPrinter;
		private String labelPrinter;

		public String getReportPrinter() {
			return reportPrinter;
		}

		public void setReportPrinter(String reportPrinter) {
			this.reportPrinter = reportPrinter;
		}

		public String getLabelPrinter() {
			return labelPrinter;
		}

		public void setLabelPrinter(String labelPrinter) {
			this.labelPrinter = labelPrinter;
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
