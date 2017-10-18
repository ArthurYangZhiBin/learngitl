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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class OnChangeLot extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnChangeLot.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{

		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing OnChangeLot " ,100L);
		String toOrFrom = getParameter("toOrFrom").toString();
		final String storerKey = toOrFrom +"STORERKEY";
		final String sku= toOrFrom +"SKU";
		final String BLANK= "  ";
	
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface subForm = state.getCurrentRuntimeForm();		
		RuntimeFormInterface detailForm = subForm.getParentForm(state);
		RuntimeFormInterface shellForm = null;
		shellForm = detailForm.getParentForm(state);
		
		if(!shellForm.getName().equals("wms_list_shell"))
		{
			shellForm = detailForm.getParentForm(state).getParentForm(state);
		}

		
		DataBean focus = state.getFocus();
		QBEBioBean qbe = null;
		BioBean bio = null;
		

			
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,null);
		DataBean headerFocus = headerForm.getFocus();
		
		
		//get lottableSlot
	
		RuntimeFormInterface subFormLottable = state.getRuntimeForm(detailForm.getSubSlot("LottableSlot"), null);
		
		
		String owner= (String)headerFocus.getValue(storerKey);
		
		
		RuntimeFormWidgetInterface lot = context.getSourceWidget();
			if((lot !=null) ||(!lot.getDisplayValue().matches("[ \t\n\r]+")))
			{
    		Iterator subWidgets = subFormLottable.getFormWidgets();
  			subFormLottable.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
    		subFormLottable.getFormWidgetByName("LOTTABLE01").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
	  		/*while (subWidgets.hasNext() ) {
	  			Object subObj = subWidgets.next();
	  			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)subObj;
	  			_log.debug("LOG_SYSTEM_OUT","\n\n^^^^^Disabling: " +widget.getName(),100L);
	  			//widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, "true");
	  			//widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

	  			}			
			*/
			}
			else{
				/*query*/	
			}
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting OnChangeLot " ,100L);
		return RET_CONTINUE;
	}
}
