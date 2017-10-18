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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class OnClickRadioButton extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnClickRadioButton.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing OnClickRadioButton",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		RuntimeFormWidgetInterface widget = context.getSourceWidget();
		
		if(widget.getValue().equals("0"))
		{
			form.getFormWidgetByName("BILLINGFROM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("BILLINGTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("BILLINGFROM").setValue("0");
			form.getFormWidgetByName("BILLINGTO").setValue("ZZZZZZZZZZZZZZZ");
			form.getFormWidgetByName("OWNERFROM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("OWNERTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("OWNERFROM").setValue("");
			form.getFormWidgetByName("OWNERTO").setValue("");
		}
		else if(widget.getValue().equals("1"))
		{
			form.getFormWidgetByName("OWNERFROM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("OWNERTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			if(!WSDefaultsUtil.isOwnerLocked(state)){
				form.getFormWidgetByName("OWNERFROM").setValue("0");
				form.getFormWidgetByName("OWNERTO").setValue("ZZZZZZZZZZZZZZZ");
			}
			else{
				form.getFormWidgetByName("OWNERFROM").setValue(WSDefaultsUtil.getPreFilterValueByType("STORER", state));
				form.getFormWidgetByName("OWNERTO").setValue(WSDefaultsUtil.getPreFilterValueByType("STORER", state));
			}
			form.getFormWidgetByName("BILLINGFROM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("BILLINGTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("BILLINGFROM").setValue("");
			form.getFormWidgetByName("BILLINGTO").setValue("");
		}
		
		 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting OnClickRadioButton",100L);
		return RET_CONTINUE;
	}
}
