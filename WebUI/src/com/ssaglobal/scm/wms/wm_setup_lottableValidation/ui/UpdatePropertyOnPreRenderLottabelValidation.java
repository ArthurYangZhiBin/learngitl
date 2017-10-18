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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class UpdatePropertyOnPreRenderLottabelValidation extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UpdatePropertyOnPreRenderLottabelValidation.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing UpdatePropertyOnPreRenderLottabelValidation",100L);
	    String visibleTemp = null;
	    String reqTemp = null;
	   
	    if(!form.getFocus().isTempBio())
	    {
	    	for (int i=1; i<=12; i++)
	    	{
	    		if(i >= 10)
	    		{
	    			visibleTemp= "SHOWLOTTABLE" + Integer.toString(i) + "ONRFRECEIPT";
	    			reqTemp = "LOTTABLE" + Integer.toString(i) + "ONRFRECEIPTMANDATORY";
	    		}
	    		else
	    		{
	    			visibleTemp= "SHOWLOTTABLE0" + Integer.toString(i) + "ONRFRECEIPT";
	    			reqTemp = "LOTTABLE0" + Integer.toString(i) + "ONRFRECEIPTMANDATORY";
	    		}
	    
	    		//get checkbox widgets from header form
	    		RuntimeFormWidgetInterface visibleCheckboxTemp = form.getFormWidgetByName(visibleTemp);
	    		RuntimeFormWidgetInterface reqCheckboxTemp = form.getFormWidgetByName(reqTemp);
	    		checkIfDisable(reqCheckboxTemp, visibleCheckboxTemp);
	    	}
	    }	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting UpdatePropertyOnPreRenderLottabelValidation",100L);
		return RET_CONTINUE;
    
    }

	private void checkIfDisable(RuntimeFormWidgetInterface req, RuntimeFormWidgetInterface visible) {
		// TODO Auto-generated method stub
		if(req.getValue().equals("1"))
		{
			visible.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}
		
	}   
}
