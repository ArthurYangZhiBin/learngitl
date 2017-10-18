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
package com.ssaglobal.scm.wms.wm_inventory_move.ui;

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InventoryMoveSetListMarker extends FormExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryMoveSetListMarker.class);

	String sessionVariable;
	String sessionObjectValue;
	
	public InventoryMoveSetListMarker(){
	}
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface listForm){
		//Find window start integer and set to the session for other extensions
		//to increase efficiency of hasBeenUpdated loops
		int winStart = listForm.getWindowStart();
		_log.debug("LOG_SYSTEM_OUT","WINDOW START: "+winStart,100L);
		String interactionID = context.getState().getInteractionId();
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		sessionObjectValue = "" + winStart;
		HttpSession session = context.getState().getRequest().getSession();
		session.setAttribute(sessionVariable, sessionObjectValue);
		return RET_CONTINUE;
	}
}