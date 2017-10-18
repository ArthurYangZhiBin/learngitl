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
package com.ssaglobal.scm.wms.wm_order_seq_strategy.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreRenderOrderSeqStrategyHeaderDetail extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderOrderSeqStrategyHeaderDetail.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {	
		 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Executing PreRenderOrderSeqStrategyHeaderDetail",100L);
		String pgNum = (String)getParameter("priorityGroupNum");
		String priorityFlag = "PRIORITYGROUP" +pgNum +"FLAG";
		
		
	    DataBean formFocus = form.getFocus();
		if (formFocus instanceof BioBean)
		{
			formFocus = (BioBean)formFocus;
		}
	    
	    
	    if(!formFocus.isTempBio())
	    {	    
	    	//get checkbox widgets from header form
	    	RuntimeFormWidgetInterface priorFlagTemp = form.getFormWidgetByName(priorityFlag);	    	
	    	setCheckbox(priorFlagTemp, form, pgNum);	    	
	    }
	    else if(formFocus.isTempBio())
	    {
	    	if(pgNum.equals("1"))
	    	{
	    	form.getFormWidgetByName("SHIPORDCHECK").setValue("1");
	    	formFocus.setValue(priorityFlag, "5");
	    	}
	    	else if(pgNum.equals("2") || pgNum.equals("3"))
	    	{
	    		form.getFormWidgetByName("SHIPORDCHECK").setValue("0");
		    	formFocus.setValue(priorityFlag, "0");	
	    	}
	    		
	    }
	   
		 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Exiting PreRenderOrderSeqStrategyHeaderDetail",100L);
		return RET_CONTINUE;
    
    }

	private void setCheckbox(RuntimeFormWidgetInterface priorFlagTemp, RuntimeNormalFormInterface form, String pgNum) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","In setCheckbox",100L);
		String priorFrom = "PRIORITYFROM0" +pgNum;
		String priorTo = "PRIORITYTO0" +pgNum;
		String orderFrom = "ORDERDATESTARTDAYS0" +pgNum;
		String orderTo = "ORDERDATEENDDAYS0" +pgNum;
		String shipFrom = "REQSHIPDATESTARTDAYS0" +pgNum;
		String shipTo = "REQSHIPDATEENDDAYS0" +pgNum;
		String orderType = "ORDERTYPEINCLUDE0" +pgNum;
		
		//form.getFormWidgetByName("OPPORDERSTRATEGYKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		if(priorFlagTemp.getValue().equals("5"))
		{
			form.getFormWidgetByName("SHIPORDCHECK").setValue("1");
		}
		else if(priorFlagTemp.getValue().equals("1"))
		{
			form.getFormWidgetByName("PRIORCHECK").setValue("1");
			form.getFormWidgetByName(priorFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName(priorTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);

		}
		else if(priorFlagTemp.getValue().equals("2"))
		{
			form.getFormWidgetByName("ORDDATECHECK").setValue("1");
			form.getFormWidgetByName(orderFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName(orderTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);

		}
		else if(priorFlagTemp.getValue().equals("3"))
		{
			form.getFormWidgetByName("REQSHIPCHECK").setValue("1");
			form.getFormWidgetByName(shipFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName(shipTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);

		}
		else if(priorFlagTemp.getValue().equals("4"))
		{
			form.getFormWidgetByName("ORDTYPECHECK").setValue("1");
			form.getFormWidgetByName(orderType).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Leaving setCheckbox",100L);
	}

	
}
