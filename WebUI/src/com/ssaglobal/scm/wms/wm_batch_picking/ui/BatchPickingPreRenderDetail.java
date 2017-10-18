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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

public class BatchPickingPreRenderDetail extends FormExtensionBase{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		if(form.getFocus().isTempBio()){
			form.getFormWidgetByName("TYPE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
			form.getFormWidgetByName("ORDERDATE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
			form.getFormWidgetByName("REQUESTEDSHIPDATE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}else{
			form.getFormWidgetByName("TEMPTYPE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
			form.getFormWidgetByName("TEMPORDERDATE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
			form.getFormWidgetByName("TEMPREQUESTEDSHIPDATE").setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		return RET_CONTINUE;
	}
}