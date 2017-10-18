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
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class TransshipASNDetailPrerenderAction extends FormExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TransshipASNDetailPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","Executing TransshipASNDetailPrerenderAction",100L);		
		RuntimeFormInterface headerForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_detail_view",context.getState());
		if(headerForm == null)
			headerForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_new_detail_view",context.getState());
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","Found Header Form:Null",100L);			
		
		if(headerForm != null){
			Object statusObj = headerForm.getFocus().getValue("STATUS");
			Object verifyFlgObj = headerForm.getFocus().getValue("VERIFYFLG");		
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","statusObj:"+statusObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","verifyFlgObj:"+verifyFlgObj,100L);
			String status = statusObj == null?"":statusObj.toString();
			String verifyFlg = verifyFlgObj == null?"":verifyFlgObj.toString();
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","status:"+status,100L);
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","verifyFlg:"+verifyFlg,100L);					
			
			if(status.equals("9") || verifyFlg.equals("2")){
				form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("DESCRIPTION").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("QTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("OVERQTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("SHORTQTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);				
				form.getFormWidgetByName("lookup_sku").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				form.getFormWidgetByName("lookup_owner").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}
			else{			
				form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("DESCRIPTION").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("QTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("OVERQTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("SHORTQTY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("lookup_sku").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				form.getFormWidgetByName("lookup_owner").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}	
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","ERROR:Header Form Not Found...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","Leaving TransshipASNDetailPrerenderAction",100L);				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETPREREN","Leaving TransshipASNDetailPrerenderAction",100L);		
		return RET_CONTINUE;
	}		
}