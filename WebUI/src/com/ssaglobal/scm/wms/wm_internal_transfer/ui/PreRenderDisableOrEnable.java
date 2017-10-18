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
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreRenderDisableOrEnable extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferPreRederDetailListForm.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PreRenderDisableOrEnable " ,100L);
	    if(!form.getFocus().isTempBio())
	    {
	    	RuntimeFormWidgetInterface toLot= null;
	    	boolean disableToLot= false;
	    	boolean lotIsNull= true;
	    	boolean lottableIsNull= true;
	    	
	    	if(form.getName().equals("wm_internal_transfer_detail_detail_to_slot"))
	    	{
	    	  	toLot= form.getFormWidgetByName("TOLOT");
	    	  	lotIsNull= checkNull(toLot);
	    	}
	    	else if(form.getName().equals("wm_internal_transfer_detail_detail_lottable_slot"))
	    	{
		    	Iterator widgets = form.getFormWidgets();
  		  		while (widgets.hasNext() ) {
  		  			Object obj = widgets.next();
  		  			RuntimeFormWidgetInterface lottableWidget = (RuntimeFormWidgetInterface)obj;
  		  				lottableIsNull= checkNull(lottableWidget);
  		  				if(!lottableIsNull)
  		  				{break;}
  
  		  		}
	    	}
	    	
	    		
	    	if(lotIsNull && lottableIsNull)
	    	{/*do nothing*/
	    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Do Nothing " ,100L);
	    	}
	    	else if(!lotIsNull)
	    	{
	    		//Disable LOTTABLES
	    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Disable Lottables" ,100L);
	    		getWidget(form, context, true);	    				    	
	    	}
	    	else if(!lottableIsNull)
	    	{
	    		//disable TOLOT
	    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Disable To Lot" ,100L);
	    		getWidget(form, context, false);
	    	}
	    	else
	    	{
	    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","In else" ,100L);
	    		
	    	}
	    	
	    }
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PreRenderDisableOrEnable " ,100L);
		return RET_CONTINUE;
    }

	private void getWidget(RuntimeNormalFormInterface currentForm, UIRenderContext context, boolean flag) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing getWidget() " ,100L);
		String formToFind= null;
		StateInterface state= context.getState();
		
		if(currentForm.getName().equals("wm_internal_transfer_detail_detail_lottable_slot") )
			formToFind="ToSlot";		
		else if(currentForm.getName().equals("wm_internal_transfer_detail_detail_to_slot"))
			formToFind= "LottableSlot";
		
		RuntimeFormInterface detailForm= currentForm.getParentForm(state);   
		SlotInterface slot= detailForm.getSubSlot(formToFind);
		RuntimeFormInterface subForm = state.getRuntimeForm(slot, null);
		if(flag)
		{
	    	Iterator widgets = subForm.getFormWidgets();
		  		while (widgets.hasNext() ) {
		  			Object obj = widgets.next();
		  			RuntimeFormWidgetInterface lottableWidget = (RuntimeFormWidgetInterface)obj;
		  			lottableWidget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		  		}
		}
		else
		{
					RuntimeFormWidgetInterface lottWidget = subForm.getFormWidgetByName("TOLOT");
	  				lottWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	  				lottWidget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
	  			
	  		}
			
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting getWidget() " ,100L);
		}


	private boolean checkNull(RuntimeFormWidgetInterface widget) {
		// TODO Auto-generated method stub
		if(widget.equals(null))
		{return true;}
		else if(widget.getDisplayValue().matches("\\s*"))
		{return true;}
		else{return false;}
		
	}
}
