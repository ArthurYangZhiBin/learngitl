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
package com.ssaglobal.scm.wms.wm_transship_asn;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class TransshipASNHeaderPrerenderAction extends FormExtensionBase{	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TransshipASNHeaderPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","Executing TransshipASNHeaderPrerenderAction",100L);		
		Object statusObj = form.getFocus().getValue("STATUS");
		Object verifyFlgObj = form.getFocus().getValue("VERIFYFLG");		
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","statusObj:"+statusObj,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","verifyFlgObj:"+verifyFlgObj,100L);
		String status = statusObj == null?"":statusObj.toString();
		String verifyFlg = verifyFlgObj == null?"":verifyFlgObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","status::"+statusObj,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","verifyFlg:"+verifyFlg,100L);
		
		if(status.equals("9") || verifyFlg.equals("2")){
			form.getFormWidgetByName("TRANSSHIPASNNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("EXTERNTRANASNKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("PALLETID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("CUSTOMERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("VENDORKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("VERIFYFLG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("WEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("CUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("UDF1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("UDF2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			form.getFormWidgetByName("MEMO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);			
		}
		else{
			form.getFormWidgetByName("TRANSSHIPASNNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("EXTERNTRANASNKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("PALLETID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("CUSTOMERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("VENDORKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("VERIFYFLG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("WEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("CUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("UDF1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("UDF2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("MEMO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
		}	
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNHEADPREREN","Exiting TransshipASNHeaderPrerenderAction",100L);		
		return RET_CONTINUE;
	}		
}