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

import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class FillInBlanksOnPrerenderWidget extends FormWidgetExtensionBase{
	private final static String BLANK = " ";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FillInBlanksOnPrerenderWidget.class);
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget){
		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing FillInBlanksOnPrerenderWidget",100L);
		DataBean focus = state.getFocus();
		if(focus.isTempBio()){
			focus = (QBEBioBean)focus;			
		}else{
			focus = (BioBean)focus;	
		}
		
		Object val = widget.getValue();
		if(widget.getDisplayValue() == null || val.toString().equalsIgnoreCase(""))
		{
			focus.setValue(widget.getName(), BLANK);
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting FillInBlanksOnPrerenderWidget",100L);
		return RET_CONTINUE;
	}
}