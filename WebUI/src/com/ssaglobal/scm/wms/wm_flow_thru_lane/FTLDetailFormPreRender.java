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
package com.ssaglobal.scm.wms.wm_flow_thru_lane;

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

public class FTLDetailFormPreRender extends FormExtensionBase
{
	public FTLDetailFormPreRender()
    {
    }

	/**
	 * This method will check the value of the LaneType and make LaneAssg1 and LaneAssg2
	 * edit only based on what the value of the LaneType is
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
		RuntimeFormWidgetInterface widgetLaneType = form.getFormWidgetByName("LANETYPE");
		RuntimeFormWidgetInterface widgetLaneAssg1 = form.getFormWidgetByName("LANEASSG1");
		RuntimeFormWidgetInterface widgetLaneAssg2 = form.getFormWidgetByName("LANEASSG2");
		
		// If LaneType is 1,2, or 3 make LaneAssg1 editable and if LaneType is 1 make LaneAssg2 editable 
		// else display Receipt Key/Line #
		String widgetLaneTypeValue = "NULL";
		if(widgetLaneType.getValue() != null)
			widgetLaneTypeValue = widgetLaneType.getValue().toString();
		
		if(widgetLaneTypeValue.equals("1") || widgetLaneTypeValue.equals("2") || widgetLaneTypeValue.equals("3")){
			widgetLaneAssg1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
		}
		else{
			widgetLaneAssg1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		
		if(widgetLaneTypeValue.equals("1")){				
			widgetLaneAssg2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
		}
		else{
			widgetLaneAssg2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}

		return RET_CONTINUE;
	}
		
}