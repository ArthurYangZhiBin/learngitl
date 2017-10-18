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

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_file_upload.FileUploadPopupCCF;

public class FTLFormLaneTypeWidgetOnChange extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FTLFormLaneTypeWidgetOnChange.class);

	public FTLFormLaneTypeWidgetOnChange()
    {
    }
	protected int execute(ActionContext context, ActionResult result){			
		RuntimeFormInterface form = context.getSourceWidget().getForm();	
		DataBean focus = form.getFocus();
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting New!!!\n\n",100L);
//		get form widgets
		RuntimeFormWidgetInterface widgetLaneType = form.getFormWidgetByName("LANETYPE");
		RuntimeFormWidgetInterface widgetLaneAssg1 = form.getFormWidgetByName("LANEASSG1");
		RuntimeFormWidgetInterface widgetLaneAssg2 = form.getFormWidgetByName("LANEASSG2");
		
		// If LaneType is 1,2, or 3 make LaneAssg1 editable and if LaneType is 1 make LaneAssg2 editable 
		// else display Receipt Key/Line #			
		widgetLaneAssg2.setDisplayValue("");
		if(focus != null)
			focus.setValue("LANEASSG2","");
		String widgetLaneTypeValue = "NULL";
		if(widgetLaneType.getValue() != null)
			widgetLaneTypeValue = widgetLaneType.getValue().toString();
		
		if(!widgetLaneTypeValue.equals("5")){
			widgetLaneAssg1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				
		}
		else{
			widgetLaneAssg1.setDisplayValue("");
			if(focus != null)
				focus.setValue("LANEASSG1","");
			widgetLaneAssg1.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		
		if(widgetLaneTypeValue.equals("1")){				
			widgetLaneAssg2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
		}
		else{
			widgetLaneAssg2.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		focus.save();
		return RET_CONTINUE;
	}		
		
}