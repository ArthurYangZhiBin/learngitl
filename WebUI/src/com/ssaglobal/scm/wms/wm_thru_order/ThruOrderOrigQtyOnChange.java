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
package com.ssaglobal.scm.wms.wm_thru_order;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ThruOrderOrigQtyOnChange extends ActionExtensionBase
{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ThruOrderOrigQtyOnChange.class);
	public ThruOrderOrigQtyOnChange()
	{
		
	}		
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ThruOrderOrigQtyOnChange",100L);		
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		StateInterface state = context.getState();
		Object origQtyObj = form.getFormWidgetByName("ORIGINALQTY");		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got origQtyObj:"+origQtyObj,100L);		
		if(origQtyObj == null ){
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_SYSTEM_ERROR",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		origQtyObj = ((RuntimeWidget)origQtyObj).getValue();
		if(origQtyObj == null){
			state.getRequest().getSession().removeAttribute("THRUORDERORIGQTY");
		}
		else{
			String origQtyStr = origQtyObj.toString();	
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got OrigQtyStr:"+origQtyStr,100L);		
			state.getRequest().getSession().setAttribute("THRUORDERORIGQTY",origQtyStr);
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ThruOrderOrigQtyOnChange",100L);		
		return RET_CONTINUE;
	} 
}