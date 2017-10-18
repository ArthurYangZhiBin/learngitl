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
package com.ssaglobal.scm.wms.wm_accumulated_charges.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreRenderAccumulatedCharges extends FormExtensionBase{
protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderAccumulatedCharges.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing PreRenderAccumulatedCharges",100L);
		
		String widgetSource = context.getActionObject().getName();		
		ArrayList widgetNames = (ArrayList) getParameter("WidgetsToEnable");
		
		DataBean focus= form.getFocus();		
		if(focus.isTempBio())
		{focus= (QBEBioBean)focus;}
		else
		{focus= (BioBean)focus;}
		
		
		Object objStatus = focus.getValue("STATUS");				
		if(!widgetSource.equals("NEW"))
		{
		String statusVal = form.getFormWidgetByName("STATUS").getValue().toString();
		String sysGenVal = form.getFormWidgetByName("SYSTEMGENERATEDCHARGE").getValue().toString();
		
		if(!widgetSource.equalsIgnoreCase("Adjust"))
		{							
				if(statusVal.equals("0") && sysGenVal.equals("0.000000"))
					enableWidgets(form, widgetNames);
				else
					disableWidgets(form, widgetNames);
		}
		else
		{
			//BioBean focus = (BioBean)form.getFocus();
			focus.setValue("DEBIT", "0");
			focus.setValue("CREDIT", "0");
			form.getFormWidgetByName("DEBIT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("CREDIT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			form.getFormWidgetByName("DESCRIP").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
		}
		}
		else
		{
			focus.setValue("DEBIT", "0");
			focus.setValue("CREDIT", "0");
			enableWidgets(form, widgetNames);
		}
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting PreRenderAccumulatedCharges",100L);
		return RET_CONTINUE;
	}

	private void disableWidgets(RuntimeNormalFormInterface form, ArrayList widgetNames) {
		// TODO Auto-generated method stub
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing disableWidgets",100L);
	for(int i = 0; i < widgetNames.size(); i++){
			
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetNames.get(i).toString());						
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				}
	  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting disableWidgets",100L);
	}

	private void enableWidgets(RuntimeNormalFormInterface form, ArrayList widgetNames) {
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing enableWidgets",100L);
		// TODO Auto-generated method stub
		for(int i = 0; i < widgetNames.size(); i++){
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetNames.get(i).toString());						
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				}
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting enableWidgets",100L);
	}
}
