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
package com.ssaglobal.scm.wms.wm_zone.ui;

import com.epiphany.shr.ui.action.ActionInterface;
import com.epiphany.shr.ui.action.ActionObjectInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;

public class ZoneLaborStdPreRender extends FormExtensionBase{
	public ZoneLaborStdPreRender(){
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form){
		if(context.getActionObject().getType()== ActionObjectInterface.FORM_WIDGET_TYPE && 
				context.getActionObject().getName().equalsIgnoreCase("new")){
			RuntimeFormWidgetInterface calcStds = form.getFormWidgetByName("CALCSTDS");
			RuntimeFormWidgetInterface displayActual = form.getFormWidgetByName("DISPLAYACTUAL");
			RuntimeFormWidgetInterface displayStds = form.getFormWidgetByName("DISPLAYSTDS");
			RuntimeFormWidgetInterface displayPERF = form.getFormWidgetByName("DISPLAYPERF");
			form.getFocus().setValue("CALCSTDS", "1");
			calcStds.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			displayActual.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			displayStds.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			displayPERF.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);							
			
			
		}else{
	//		RuntimeFormWidgetInterface calcStds = form.getFormWidgetByName("CALCSTDS");
			DataBean focus = null;
			RuntimeFormWidgetInterface displayActual = form.getFormWidgetByName("DISPLAYACTUAL");
			RuntimeFormWidgetInterface displayStds = form.getFormWidgetByName("DISPLAYSTDS");
			RuntimeFormWidgetInterface displayPERF = form.getFormWidgetByName("DISPLAYPERF");
			focus = form.getFocus();
			
			try {
					String calcStdsStr = focus.getValue("CALCSTDS").toString();
					if("0".equalsIgnoreCase(calcStdsStr)){//not checked
						displayActual.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						displayStds.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						displayPERF.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);					
					}else{//it is "1" which is checked
						displayActual.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						displayStds.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						displayPERF.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);							
					}
		    } 
			catch(Exception e) {     
		          e.printStackTrace();
		          return RET_CANCEL;          
		    } 
		}
		return RET_CONTINUE;
	}
}
