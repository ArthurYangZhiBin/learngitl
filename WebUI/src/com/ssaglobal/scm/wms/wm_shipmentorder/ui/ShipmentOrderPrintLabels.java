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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class ShipmentOrderPrintLabels extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderPrintLabels.class);
	private final static String ORDER = "ORDERKEY";
	private final static String MESSAGE = "SO_LABEL_MESSAGE";
	private final static String LABEL_MESSAGE = "WMEXP_LABEL_PRINT";
	private final static String IS_ONE = "IS_ONE";
	private final static String RFIDFLAG = "RFIDFLAG";
	private final static String ASSIGN = "ASSIGNMENT";
	private final static String STD = "STANDARD";
	private final static String RFID = "RFID";
	private final static String COPIES = "COPIES";
	private final static String KEYTYPE = "ORD_ASSIGNMENT";
	private final static String PROC_NAME = "NSPPrintWaveLabel";
	private final static String ERROR_MESSAGE_COPIES = "WMEXP_PRINT_ERROR";
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		//Get print parameters and call stored procedure NSPPrintWaveLabel
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		String order = session.getAttribute(ORDER).toString();
		String base = getTextMessage(LABEL_MESSAGE, new Object[] {order}, state.getLocale());
		session.setAttribute(MESSAGE, base);
		if(context.getActionObject().getName().equals("OK")){
			RuntimeFormInterface form = state.getCurrentRuntimeForm();
			String assignment = null;
			String printername = null;
			String rfidprintername = null;
			String copies = null;
			String rfidFlag = session.getAttribute(RFIDFLAG).toString();
			String isOne = session.getAttribute(IS_ONE).toString(); 		

			String[] parameter = new String[1];
			
			if(isOne.equalsIgnoreCase("1")){
				assignment = form.getFormWidgetByName(ASSIGN).getValue().toString();
				if(assignment.equalsIgnoreCase("")){
					//Throw Exception
					parameter[0] = colonStrip(readLabel(form.getFormWidgetByName(ASSIGN)));
					throw new FormException(ERROR_MESSAGE_COPIES, parameter);
				}
			}else{
				//assignment = " ";
			}
			if(rfidFlag.equalsIgnoreCase("1")){
				rfidprintername = form.getFormWidgetByName(RFID).getValue().toString();
				if(rfidprintername.equalsIgnoreCase("")){
					//Throw Exception
					parameter[0] = colonStrip(readLabel(form.getFormWidgetByName(RFID)));
					throw new FormException(ERROR_MESSAGE_COPIES, parameter);	
				}
			}else{
				printername = form.getFormWidgetByName(STD).getValue().toString();
				if(printername.equalsIgnoreCase("")){
					//Throw Exception
					parameter[0] = colonStrip(readLabel(form.getFormWidgetByName(STD)));
					throw new FormException(ERROR_MESSAGE_COPIES, parameter);
				}
			}

			copies = form.getFormWidgetByName(COPIES).getValue().toString();
			try{
				int temp = Integer.parseInt(copies);
				if(temp<1){
					//Throw Exception
					parameter[0] = colonStrip(readLabel(form.getFormWidgetByName(COPIES)));
					throw new FormException(ERROR_MESSAGE_COPIES, parameter);
				}
			}catch(NumberFormatException e){
				//Throw Exception
				parameter[0] = colonStrip(readLabel(form.getFormWidgetByName(COPIES)));
				throw new FormException(ERROR_MESSAGE_COPIES, parameter);
			}
			//Set parameters for stored procedure
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array(); 
			params.add(new TextData(KEYTYPE));	//keyType
			params.add(new TextData(order));	//theKey
			params.add(new TextData(printername));	//printername
			params.add(new TextData(rfidprintername));	//rfidprintername
			params.add(new TextData(copies)); //copies
			params.add(new TextData(assignment));	//assignment
			_log.debug("LOG_DEBUG_EXTENSION", "SENDING "+PROC_NAME+" KEYTYPE("+KEYTYPE+"), theKey("+order+"), printername("+printername+"), rfidprintername("+rfidprintername+"), copies("+copies+"), assignment("+assignment+")", SuggestedCategory.NONE);;
			//Initiate stored procedure
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName(PROC_NAME);
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);	
			} catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
		}

		//Remove session attributes
		session.removeAttribute(ORDER);
		session.removeAttribute(IS_ONE);
		session.removeAttribute(RFIDFLAG);
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}