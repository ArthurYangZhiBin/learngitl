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


package com.ssaglobal.scm.wms.wm_order_bio.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PrintLabelsCloseModal extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrintLabelsCloseModal.class);
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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		super.execute(context,result);
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Executing PrintLabelsCloseModal",100L);		
		StateInterface state = ctx.getState();	
		RuntimeFormInterface popupForm = ctx.getSourceWidget().getForm();		
		String didCancel = (String)getParameter("didCancel"); 
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Got Params didCancel:"+didCancel,100L);		
		state.getRequest().getSession().removeAttribute("didCloseSelectPrinterModal");
		state.getRequest().getSession().removeAttribute("didCancelSelectPrinterModal");	
		state.getRequest().getSession().removeAttribute("RFIDPRINTERNAME");
		state.getRequest().getSession().removeAttribute("NONRFIDPRINTERNAME");	
		if(didCancel != null && didCancel.equals("true")){
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Setting Session Attr didCancelSelectPrinterModal to true",100L);			
			state.getRequest().getSession().setAttribute("didCancelSelectPrinterModal","true");
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Setting Session Attr didCloseSelectPrinterModal to true",100L);			
			state.getRequest().getSession().setAttribute("didCloseSelectPrinterModal","true");
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Setting Session Attr RFIDPRINTERNAME to "+popupForm.getFormWidgetByName("RFIDPRINTER").getValue(),100L);
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Setting Session Attr NONRFIDPRINTERNAME to "+popupForm.getFormWidgetByName("NONRFIDPRINTER").getValue(),100L);			
			state.getRequest().getSession().setAttribute("RFIDPRINTERNAME",popupForm.getFormWidgetByName("RFIDPRINTER").getValue());
			state.getRequest().getSession().setAttribute("NONRFIDPRINTERNAME",popupForm.getFormWidgetByName("NONRFIDPRINTER").getValue());
		}		
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELSCLOSEMOD","Exiting PrintLabelsCloseModal",100L);		
		return RET_CONTINUE;
}
}
