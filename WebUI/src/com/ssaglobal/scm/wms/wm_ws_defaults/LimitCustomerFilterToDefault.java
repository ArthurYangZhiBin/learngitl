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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;

public class LimitCustomerFilterToDefault extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitCustomerFilterToDefault.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		if(ownerLockFlag == null || ownerLockFlag.intValue() == 0)
			return RET_CONTINUE;
		
		ArrayList widgetNames = (ArrayList)getParameter("WidgetNames");
		
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param WidgetNames:"+widgetNames,100L);		
			
		//If no widget names were passed in then limit all widgets named STORERKEY
		//Bio attribute
		if(widgetNames == null)
			widgetNames = new ArrayList();
		if(widgetNames.size() == 0){
			widgetNames.add("STORERKEY");
		}
				
		String delimitedCustomerList = "";		
		ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
		if(lockedCustomers == null || lockedCustomers.size() == 0)
			return RET_CONTINUE;
		delimitedCustomerList += "'"+lockedCustomers.get(0)+"'";
		for(int i = 1; i < lockedCustomers.size(); i++){
			delimitedCustomerList += ",'"+lockedCustomers.get(i)+"'";
		}
		
		//Verify Focus Is Not Empty		
		if(result.getFocus() == null || !result.getFocus().isBioCollection())
			return RET_CONTINUE;
				
		BioCollectionBean focus = (BioCollectionBean)result.getFocus();
		for(int i = 0; i < widgetNames.size(); i++){
			String innerQuery = focus.getBioTypeName()+"."+widgetNames.get(i)+" IN ("+delimitedCustomerList+")";			
			Query qry = new Query(focus.getBioTypeName(),innerQuery,"");
			try {
				focus.filterInPlace(qry);
			} catch (EpiDataException e) {
				_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Error Occured. Exiting...",100L);
				_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY",e.getErrorMessage(),100L);			
				e.printStackTrace();
				String args[] = new String[0]; 			
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
		return RET_CONTINUE;	
	}
}
