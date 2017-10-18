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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreRenderToLotWidget extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {
	   protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderToLotWidget.class);
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) { 
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Executing PreRenderToLotWidget",100L);
		boolean lottableIsNull= true;
		boolean lotIsNull= true;
		
	    try {
	    
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface form = toolbar.getParentForm(state);
			RuntimeFormInterface lottableSlotForm = state.getRuntimeForm(form.getSubSlot("LottableSlot"), null );

			Iterator widgets = lottableSlotForm.getFormWidgets();
		  		while (widgets.hasNext() ) {
		  			Object obj = widgets.next();
		  			RuntimeFormWidgetInterface lottableWidget = (RuntimeFormWidgetInterface)obj;
		  			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Lottable Widget: "+lottableWidget ,100L);
		  			lottableIsNull= checkNull(lottableWidget);
		  				if(!lottableIsNull)
		  				{break;}
		  		}
			
		  		
	
		    	if(!lottableIsNull && checkNull(widget))
		    	{
		    		//disable TOLOT
		    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Disabling TO LOT",100L);
		    		 widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		    	}

		  					
        } catch(Exception e) {
            
            // Handle Exceptions 
		    e.printStackTrace();		    
		    return RET_CANCEL;
		    
	    } 
    	_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Exiting PreRenderToLotWidget",100L);
	    return RET_CONTINUE;
	}

	private boolean checkNull(RuntimeFormWidgetInterface widget) {
		// TODO Auto-generated method stub
		
		String widgetVal="";

		if(widget !=null)
		{
			widgetVal = widget.getValue() == null ? "" : widget.getValue().toString();
		}
		
		
		if(widgetVal == null) 
		{return true;}
		else if(widgetVal.matches("\\s*"))
		{return true;}
		else{return false;}
	}
}



