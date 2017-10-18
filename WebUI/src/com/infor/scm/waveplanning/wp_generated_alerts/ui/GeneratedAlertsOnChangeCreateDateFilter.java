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
package com.infor.scm.waveplanning.wp_generated_alerts.ui;

import java.util.Calendar;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class GeneratedAlertsOnChangeCreateDateFilter extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		System.out.println("\n\n\n\n\n\n\n\n\n *****execute");

		RuntimeFormWidgetInterface widget = context.getSourceWidget();
		Calendar calVal = widget.getCalendarValue();
		//.getValue().toString());
		System.out.println("\nWidget: " +widget.getName());
		System.out.println("\nMonth: " +calVal.get(Calendar.MONTH));
		System.out.println("Day: " +calVal.get(Calendar.DAY_OF_MONTH));
		System.out.println("Year: " +calVal.get(Calendar.YEAR));
		
		context.getServiceManager().getUserContext().put(widget.getName(), calVal);
		
		return RET_CONTINUE;
	}
}

