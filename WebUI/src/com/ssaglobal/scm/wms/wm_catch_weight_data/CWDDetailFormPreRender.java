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
package com.ssaglobal.scm.wms.wm_catch_weight_data;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

/**
 * The CWDDetailFormPreRender will check the value of the IOFlag and display different
 * labels for the SOURCEKEY and SOURCELINENUMBER based on what the value of the IOFlag is
 * 
 * The preRenderFormOnInit event triggers the CWDDetailFormPreRender.preRenderForm() method.
 * 
 * CWDDetailFormPreRender uses no parameters
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CWDDetailFormPreRender extends FormExtensionBase
{
	public CWDDetailFormPreRender()
    {
    }

	/**
	 * This method will will check the value of the IOFlag and display different
	 * labels for the SOURCEKEY and SOURCELINENUMBER based on what the value of the IOFlag is
	 * 
	 * This method is driven by no properties
	 * 
	 * <P>
	 * 
	 * @param context
	 *            The UIRenderContext for this extension
	 * @param form
	 *            The RuntimeNormalFormInterface for this extension's form
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {	
		//get form widgets
		RuntimeFormWidgetInterface widgetSourceKey = form.getFormWidgetByName("SOURCEKEY");
		RuntimeFormWidgetInterface widgetLineNumber = form.getFormWidgetByName("SOURCELINENUMBER");
		RuntimeFormWidgetInterface widgetioFlag = form.getFormWidgetByName("IOFLAG");
		String args[] = new String[0]; 
		String orderNumberLabel = getTextMessage("ORDER_NUMBER_COLON",args,context.getState().getLocale());
		String lineNumberLabel = getTextMessage("LINE_NUMBER_COLON",args,context.getState().getLocale());
		String receiptNumberLabel = getTextMessage("RECEIPT_NUMBER_COLON",args,context.getState().getLocale());
		// If IOFlag is "O" display labels Order Key/Line #
		// else display Receipt Key/Line #
		if(widgetioFlag.getValue().toString().equals("O")){
			widgetSourceKey.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,orderNumberLabel);
			widgetSourceKey.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,orderNumberLabel);
			widgetLineNumber.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,lineNumberLabel);
			widgetLineNumber.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,lineNumberLabel);
		}
		else{				
			widgetSourceKey.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,receiptNumberLabel);
			widgetSourceKey.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,receiptNumberLabel);
			widgetLineNumber.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,lineNumberLabel);
			widgetLineNumber.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,lineNumberLabel);
		}

		return RET_CONTINUE;
	}
		
}