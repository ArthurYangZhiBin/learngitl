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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.util.Iterator;

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

public class OnClickSelectAll extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnClickSelectAll.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing OnClickSelectAll",100L);
		
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		RuntimeFormWidgetInterface widgetSource = context.getSourceWidget();
		RuntimeFormInterface shellForm = form.getParentForm(state);
		
		RuntimeFormInterface subSlotForm= state.getRuntimeForm(form.getSubSlot("chargetype"), null);
		DataBean focus = subSlotForm.getFocus();
	
		
		Iterator widgets = subSlotForm.getFormWidgets();
	  		while (widgets.hasNext() ) {
	  			Object obj = widgets.next();
	  			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)obj;
	  			if(!widget.getName().startsWith("place"))
	  				{
	  				
	  				if(widgetSource.getName().equals("SELECTALL"))
	  				{
	  				widget.setValue("1");
	  				widget.setDisplayValue("1");
	  				}
	  				else if(widgetSource.getName().equals("UNSELECTALL"))
	  				{
	  					widget.setValue("0");
		  				widget.setDisplayValue("0");		  	
	  				}
	  				}
	  			}
	  		 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting OnClickSelectAll",100L);
		return RET_CONTINUE;
	}
}
