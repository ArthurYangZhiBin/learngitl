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
package com.ssaglobal.scm.wms.wm_serial_inventory.ui;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import java.util.Iterator;

public class SerialInventoryRestoreFilter extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerialInventoryRestoreFilter.class);

	private static String SESSION_KEY_DO_FILTER = "session.key.do.filter";

	private boolean widgetIsInContext(RuntimeFormWidgetInterface widget) 
	{
		String widgetType = widget.getType().toLowerCase();
		String widgetName = widget.getName().toLowerCase();
		if(widget !=null)
		{
			if (!widgetType.equalsIgnoreCase("text"))
				{return false;}
			if (!widgetName.matches(".+end$") && !widgetName.matches(".+start$"))
				{return false;}
		}
		//_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Widget: "+widget ,100L);
		return true;
	}
	

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException 
	{		
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if(context.getState().getRequest().getSession().getAttribute(SESSION_KEY_DO_FILTER) != null)
		{
			Iterator widgets = form.getFormWidgets();
			while (widgets.hasNext() ) 
			{
				Object obj = widgets.next();
				RuntimeFormWidgetInterface formWidget = (RuntimeFormWidgetInterface)obj;
				String widgetName = formWidget.getName().toLowerCase();
				if (widgetIsInContext(formWidget))
				{
					formWidget.setDisplayValue((String)userContext.get("user.ctx.key."+widgetName));
					formWidget.setValue((String)userContext.get("user.ctx.key."+widgetName));
				}
			}
		}
		else
		{
			context.getState().getRequest().getSession().setAttribute(SESSION_KEY_DO_FILTER,"true");
		}
		return RET_CONTINUE;
	}	
}
