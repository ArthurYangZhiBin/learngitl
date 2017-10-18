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
package com.ssaglobal.scm.wms.wm_order_bio.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;

public class OrderPopupFormPreRender extends FormExtensionBase
{
	public OrderPopupFormPreRender()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException
	{	
		String unallocationType = (String)getParameter("unallocationType");		
		RuntimeFormWidgetInterface widgetLabel = form.getFormWidgetByName("POPUPLABEL");	
		
		if(unallocationType.equalsIgnoreCase("unallocateOrders")){
			Object selectedItemsObj = context.getState().getRequest().getSession().getAttribute("unallocateOrdersSelectedItemsSize");
			if(selectedItemsObj == null){			
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String selectedItemsSize = (String)selectedItemsObj;
			//ArrayList selectedItems = (ArrayList)selectedItemsObj;
			if(selectedItemsSize.equals("0")){
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String[] size = {selectedItemsSize};
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_A",size,context.getState().getLocale()));
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_A",size,context.getState().getLocale()));
		}
		if(unallocationType.equalsIgnoreCase("unallocatePicks")){
			Object selectedItemsObj = context.getState().getRequest().getSession().getAttribute("unallocatePicksSelectedItemsSize");
			if(selectedItemsObj == null){			
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String selectedItemsSize = (String)selectedItemsObj;
			//ArrayList selectedItems = (ArrayList)selectedItemsObj;
			if(selectedItemsSize.equals("0")){
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String[] size = {selectedItemsSize};
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEPICKDETAIL",size,context.getState().getLocale()));
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEPICKDETAIL",size,context.getState().getLocale()));
		}
		if(unallocationType.equalsIgnoreCase("unallocateDetail")){
			Object selectedItemsObj = context.getState().getRequest().getSession().getAttribute("unallocateShipmentOrdersSelectedItemsSize");
			if(selectedItemsObj == null){			
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String selectedItemsSize = (String)selectedItemsObj;
			//ArrayList selectedItems = (ArrayList)selectedItemsObj;
			if(selectedItemsSize.equals("0")){
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATEORDERS_CONF_B",null,context.getState().getLocale()));
				return RET_CONTINUE;
			}
			String[] size = {selectedItemsSize};
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,getTextMessage("WMTXT_UNALLOCATESHIMPENT",size,context.getState().getLocale()));
			widgetLabel.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,getTextMessage("WMTXT_UNALLOCATESHIMPENT",size,context.getState().getLocale()));
		}					
		return RET_CONTINUE;
	}
	
}