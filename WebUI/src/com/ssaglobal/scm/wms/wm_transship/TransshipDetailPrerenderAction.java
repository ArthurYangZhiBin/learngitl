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
package com.ssaglobal.scm.wms.wm_transship;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class TransshipDetailPrerenderAction extends FormExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TransshipDetailPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		
		_log.debug("LOG_DEBUG_EXTENSION_TRANSSHIPDETPREREN","Executing TransshipDetailPrerenderAction",100L);		
		Object statusObj = form.getFocus().getValue("STATUS");
		_log.debug("LOG_DEBUG_EXTENSION_TRANSSHIPDETPREREN","statusObj:"+statusObj,100L);		
		String status = statusObj == null?"":statusObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_TRANSSHIPDETPREREN","status:"+status,100L);		
		
		if(status.length() > 0){
			if(status.equals("9")){
				form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("QTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("MEMO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("DAMAGE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);						
			}
			else{
				form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("QTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("MEMO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("DAMAGE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);	
			}
		}
		else{
			form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("QTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("MEMO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("DAMAGE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);	
		}
		_log.debug("LOG_DEBUG_EXTENSION_TRANSSHIPDETPREREN","Exiting TransshipDetailPrerenderAction",100L);		
		return RET_CONTINUE;
	}		
}