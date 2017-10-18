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

package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.ssaglobal.scm.wms.util.LocaleUtil;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNReceiptLineListPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	//SRG Begin: Incident4334565_Defect302819
	String sessionVariable;
	String sessionObjectValue;
	//SRG End: Incident4334565_Defect302819
	
	/**
	 * Called in response to the modifyListValues event on a list form.
	 * Subclasses must override this in order to customize the display values of
	 * a list form
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
	 /**
	 * MODIFICATION HISTORY: 
	 * 01/12/2009 - SCM: Disable list cells on the ASN Receipt detail list view to prevent updates of closed records (SDIS SCM-00000-06251)
	 * 03/04/2009 - AW:	Machine#2246930 SDIS#SCM-00000-06251. Customer requested 2 more fields to be made uneditable for closed records. 
	 */
		try {
			// Add your code here to process the event
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++) {

				RuntimeFormWidgetInterface statusWidget = listRows[i].getFormWidgetByName("STATUSVALUE");
				RuntimeFormWidgetInterface typeWidget = listRows[i].getFormWidgetByName("TYPE");
				RuntimeFormWidgetInterface toidWidget = listRows[i].getFormWidgetByName("TOID");
				RuntimeFormWidgetInterface expectedQtyWidget = listRows[i].getFormWidgetByName("EXPECTEDQTY");
				RuntimeFormWidgetInterface receivedQtyWidget = listRows[i].getFormWidgetByName("RECEIVEDQTY");
				RuntimeFormWidgetInterface poKeyWidget = listRows[i].getFormWidgetByName("POKEY");
				RuntimeFormWidgetInterface toLocWidget = listRows[i].getFormWidgetByName("TOLOC");//AW Machine#2246930 SDIS#SCM-00000-06251
				RuntimeFormWidgetInterface holdCodeWidget = listRows[i].getFormWidgetByName("CONDITIONCODE");//AW Machine#2246930 SDIS#SCM-00000-06251

				// if QtyReceived > 0, Disable POKEY
				NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
				Number doubQtyReceived = nf.parse(receivedQtyWidget.getDisplayValue());
				if (doubQtyReceived.doubleValue() > 0) {
					poKeyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				} else {
					poKeyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}

				final String status = statusWidget.getDisplayValue();
				if (!status.equalsIgnoreCase("0")) {
					toidWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				} else {
					toidWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}

				final String type = typeWidget.getDisplayValue();
				// SCM-00000-06251: Start Update
				if (type.equals("4") || type.equals("5") || status.equalsIgnoreCase("11") || status.equalsIgnoreCase("15")) {
					//SCM-00000-06251: End update
					expectedQtyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					receivedQtyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					toLocWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);//AW Machine#2246930 SDIS#SCM-00000-06251
					holdCodeWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);//AW Machine#2246930 SDIS#SCM-00000-06251
				} else {
					expectedQtyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					receivedQtyWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					toLocWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);//AW Machine#2246930 SDIS#SCM-00000-06251
					holdCodeWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);//AW Machine#2246930 SDIS#SCM-00000-06251
				}

			}
			
			//SRG Begin: Incident4334565_Defect302819: Set the page start value in the session which can be 
			//used in the SaveASNReceipt extension.
			int winStart = form.getWindowStart();			
			String interactionID = context.getState().getInteractionId();
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			sessionObjectValue = "" + winStart;
			HttpSession session = context.getState().getRequest().getSession();
			session.setAttribute(sessionVariable, sessionObjectValue);
			//SRG End: Incident4334565_Defect302819

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
