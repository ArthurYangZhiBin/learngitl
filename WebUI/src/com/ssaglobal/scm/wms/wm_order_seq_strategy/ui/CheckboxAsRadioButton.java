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

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
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


public class CheckboxAsRadioButton extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckboxAsRadioButton.class);
	protected int execute(ActionContext context, ActionResult result)
	throws EpiException
{

	  	 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Executing CheckboxAsRadioButton",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		
		DataBean currentFormFocus = currentForm.getFocus();
		if (currentFormFocus instanceof BioBean)
		{
			currentFormFocus = (BioBean) currentFormFocus;
		}
		
		
		ArrayList widgetNames = (ArrayList) getParameter("widgetToEnable");
		String currCB = (String)getParameter("currentWidgetName");
		String pgNum = (String)getParameter("priorityGroupNum");
		String priorityFlag = "PRIORITYGROUP" +pgNum +"FLAG";
		
		RuntimeFormWidgetInterface currWidget= currentForm.getFormWidgetByName(currCB); 
		currWidget.setValue("1");
		
		
		RuntimeFormWidgetInterface priorFlag = currentForm.getFormWidgetByName(priorityFlag);
		
		
		setPriorityFlag(priorFlag, currCB, currentFormFocus);	
		uncheckCheckedOne(currentForm, currCB, pgNum, currentFormFocus);
		enableWidgets(currentForm, widgetNames);
			
		
	  	 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Exiting CheckboxAsRadioButton",100L);
		return RET_CONTINUE;
}

	private void enableWidgets(RuntimeFormInterface currentForm, ArrayList widgetNames) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < widgetNames.size(); i++){
			//_log.debug("LOG_SYSTEM_OUT","\n\nwidgetNames:"+widgetNames+"\n\n",100L);
             
			if(!widgetNames.get(i).equals("NONE"))
			{
			RuntimeFormWidgetInterface widget = currentForm.getFormWidgetByName(widgetNames.get(i).toString());
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
		}
	}

	private void uncheckCheckedOne(RuntimeFormInterface currentForm, String currCB, String pgNum, DataBean currentFormFocus) {
		// TODO Auto-generated method stub
		RuntimeFormWidgetInterface shipWidget= currentForm.getFormWidgetByName("SHIPORDCHECK");
		RuntimeFormWidgetInterface priorityWidget= currentForm.getFormWidgetByName("PRIORCHECK");
		RuntimeFormWidgetInterface orderWidget= currentForm.getFormWidgetByName("ORDDATECHECK");
		RuntimeFormWidgetInterface reqShipWidget= currentForm.getFormWidgetByName("REQSHIPCHECK");
		RuntimeFormWidgetInterface orderTypeWidget= currentForm.getFormWidgetByName("ORDTYPECHECK");
		
		String priorFrom = "PRIORITYFROM0" +pgNum;
		String priorTo = "PRIORITYTO0" +pgNum;
		String orderFrom = "ORDERDATESTARTDAYS0" +pgNum;
		String orderTo = "ORDERDATEENDDAYS0" +pgNum;
		String shipFrom = "REQSHIPDATESTARTDAYS0" +pgNum;
		String shipTo = "REQSHIPDATEENDDAYS0" +pgNum;
		String orderType = "ORDERTYPEINCLUDE0" +pgNum;
		//String BLANK= " ";
		currCB.trim();
		//Object blank= null;
		RuntimeFormWidgetInterface priorStart = currentForm.getFormWidgetByName(priorFrom);
		RuntimeFormWidgetInterface priorEnd = currentForm.getFormWidgetByName(priorTo);
		RuntimeFormWidgetInterface orderStart = currentForm.getFormWidgetByName(orderFrom);
		RuntimeFormWidgetInterface orderend = currentForm.getFormWidgetByName(orderTo);
		RuntimeFormWidgetInterface shipStart = currentForm.getFormWidgetByName(shipFrom);
		RuntimeFormWidgetInterface shipEnd = currentForm.getFormWidgetByName(shipTo);
		RuntimeFormWidgetInterface orderT = currentForm.getFormWidgetByName(orderType);
		
		
		if(!(shipWidget.getName().equals(currCB)) && shipWidget.getValue().equals("1"))
		{
			shipWidget.setValue("0");
		}
		else if(!(priorityWidget.getName().equals(currCB)) && priorityWidget.getValue().equals("1"))
		{
		
			priorityWidget.setValue("0");
	
			currentForm.getFormWidgetByName(priorFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			currentForm.getFormWidgetByName(priorTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

		}
		else if(!(orderWidget.getName().equals(currCB)) && orderWidget.getValue().equals("1"))
		{
			orderWidget.setValue("0");
			//currentFormFocus.setValue(orderFrom, BLANK);
			//currentFormFocus.setValue(orderTo, BLANK);
			
			currentForm.getFormWidgetByName(orderFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			currentForm.getFormWidgetByName(orderTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		else if(!(reqShipWidget.getName().equals(currCB)) && reqShipWidget.getValue().equals("1"))
		{
			reqShipWidget.setValue("0");
			//currentFormFocus.setValue(shipFrom, BLANK);
			//currentFormFocus.setValue(shipTo, BLANK);

			currentForm.getFormWidgetByName(shipFrom).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			currentForm.getFormWidgetByName(shipTo).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

		}
		else if(!(orderTypeWidget.getName().equals(currCB)) && orderTypeWidget.getValue().equals("1"))
		{
			orderTypeWidget.setValue("0");
			//currentFormFocus.setValue(orderType, "");
			currentForm.getFormWidgetByName(orderType).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			
		}

	}

	private void setPriorityFlag(RuntimeFormWidgetInterface priorFlag, String currCB, DataBean currentFormFocus) {
		// TODO Auto-generated method stub
	  	 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Executing setPriorityFlag",100L);
		currCB.trim();
		String str = currCB.substring(0, 4);
		
		if(str.equals("SHIP"))
		{
		 currentFormFocus.setValue(priorFlag.getName(), "5");
		}
		else if(str.equals("PRIO"))
		{
			currentFormFocus.setValue(priorFlag.getName(), "1");
		}
		else if(str.equals("ORDD"))
		{
			currentFormFocus.setValue(priorFlag.getName(), "2");
		}
		else if(str.equals("REQS"))
		{
			currentFormFocus.setValue(priorFlag.getName(), "3");
		}
		else if(str.equals("ORDT"))
		{
			currentFormFocus.setValue(priorFlag.getName(), "4");
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Leaving setPriorityFlag",100L);
		
	}
}
