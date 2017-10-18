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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class CalcDebitOnChange extends ActionExtensionBase{
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(CalcDebitOnChange.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing CalcDebitOnChange",100L);
		String widget= "";
		String sourceName="";
		double sourceVal, val, debitVal; 
		StateInterface state = context.getState();	
		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		sourceName= sourceWidget.getName();
		
		
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		
		DataBean focus= currentForm.getFocus();
		if(focus.isTempBio())
		{focus = (QBEBioBean)focus;}
		else
		{focus = (BioBean)focus;}
		
		
		if(sourceWidget.getName().equals("BILLEDUNITS"))
			widget = "RATE";
		if(sourceWidget.getName().equals("RATE"))
			widget = "BILLEDUNITS";
		
				
		if(sourceWidget != null)
		{

		Object value = focus.getValue(widget);
		if(value !=null && !value.toString().equalsIgnoreCase(""))
			{
				String valString = value.toString();
				String sourceString = focus.getValue(sourceName).toString();
				sourceVal = Double.parseDouble(sourceString);
				debitVal= sourceVal * Double.parseDouble(valString);
				
				focus.setValue("DEBIT", Double.toString(debitVal));
				
			}
	
		}

		
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting CalcDebitOnChange",100L);		
		return RET_CONTINUE;
	}	
}
