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
package com.ssaglobal.scm.wms.wm_po.ui;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitPOWhenOwnerLocked extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitPOWhenOwnerLocked.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}						
		
		ArrayList lockedVendors = WSDefaultsUtil.getLockedVendors(state);		

		String lockedVendorClause = "";
		
		if(lockedVendors != null && lockedVendors.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Found Locked Vendors:"+lockedVendors,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked vendor clause...",100L);
			lockedVendorClause += " wm_po.SELLERNAME = '' OR wm_po.SELLERNAME = ' ' OR wm_po.SELLERNAME IS NULL";
			for(int i = 0; i < lockedVendors.size(); i++){				
				lockedVendorClause += " OR wm_po.SELLERNAME = '" + lockedVendors.get(i) + "' ";
				_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked vendor clause after iteration "+i+":"+lockedVendorClause,100L);
			}
		}			
		
		String bioQry = lockedVendorClause;				
		
		if(bioQry.length() == 0)
			return RET_CONTINUE;
		
		BioCollectionBean focus = (BioCollectionBean)result.getFocus();
		try {
			focus.filterInPlace(new Query("wm_po",bioQry,""));
		} catch (EpiDataException e) { 
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Error Occured. Exiting...",100L);
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY",e.getErrorMessage(),100L);			
			e.printStackTrace();
			String args[] = new String[0]; 			
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		result.setFocus(focus);
		return RET_CONTINUE;	
	}
}
