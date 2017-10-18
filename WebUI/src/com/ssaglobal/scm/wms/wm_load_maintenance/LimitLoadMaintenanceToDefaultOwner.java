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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.data.general.WaferMapDataset;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitLoadMaintenanceToDefaultOwner extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitLoadMaintenanceToDefaultOwner.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
			
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitLoadMaintenanceToDefaultOwner",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state))
			return RET_CONTINUE;
				
		//Verify Focus Is Not Empty
		DataBean focus = result.getFocus();		
		if(focus == null || !focus.isBioCollection())
			return RET_CONTINUE;
		
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
		
		if(lockedOwners == null || lockedOwners.size() == 0){
			if(lockedCustomers == null || lockedCustomers.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","No Locked Owners or Customers Found. Exiting...",100L);	
				return RET_CONTINUE;
			}
		}
		
		
		String lockedOwnerClause = "";
		String lockedCustomerClause = "";
		for(int i = 0; i < lockedOwners.size(); i++){			
			
			if(i == 0)
				lockedOwnerClause += " (LOADORDERDETAIL.STORER = \\'" + lockedOwners.get(i) + "\\' ";
			else
				lockedOwnerClause += " OR LOADORDERDETAIL.STORER = \\'" + lockedOwners.get(i) + "\\' ";
			
			if(i == lockedOwners.size() - 1){				
				lockedOwnerClause += " OR LOADORDERDETAIL.STORER IS NULL OR LOADORDERDETAIL.STORER = \\'\\' OR LOADORDERDETAIL.STORER = \\' \\')";				
			}
		}
		
		for(int i = 0; i < lockedCustomers.size(); i++){			
			if(lockedOwnerClause.length() == 0){
				if(i == 0)
					lockedCustomerClause += " (LOADORDERDETAIL.CUSTOMER = \\'" + lockedCustomers.get(i) + "\\' ";
				else
					lockedCustomerClause += " OR LOADORDERDETAIL.CUSTOMER = \\'" + lockedCustomers.get(i) + "\\' ";
			}
			else{
				if(i == 0)
					lockedCustomerClause += " AND (LOADORDERDETAIL.CUSTOMER = \\'" + lockedCustomers.get(i) + "\\' ";
				else
					lockedCustomerClause += " OR LOADORDERDETAIL.CUSTOMER = \\'" + lockedCustomers.get(i) + "\\' ";
			}
			if(i == lockedCustomers.size() - 1){				
				lockedCustomerClause += " OR LOADORDERDETAIL.CUSTOMER IS NULL OR LOADORDERDETAIL.CUSTOMER = \\'\\' OR LOADORDERDETAIL.CUSTOMER = \\' \\')";				
			}
		}
				
		
		//String innerQuery = " SELECT LOADID FROM LOADSTOP WHERE LOADSTOP.LOADSTOPID IN (SELECT LOADSTOPID FROM LOADORDERDETAIL WHERE "+lockedOwnerClause+")";
		String innerQuery = " SELECT LOADID FROM LOADSTOP LEFT OUTER JOIN LOADORDERDETAIL ON LOADSTOP.LOADSTOPID = LOADORDERDETAIL.LOADSTOPID WHERE LOADORDERDETAIL.LOADSTOPID IS NULL OR LOADSTOP.LOADSTOPID IN (SELECT LOADSTOPID FROM LOADORDERDETAIL WHERE "+lockedOwnerClause+lockedCustomerClause+")";					
		Query qry = new Query("wm_loadhdr_auto_pk","DPE('SQL','@[wm_loadhdr_auto_pk.LOADID] in ("+innerQuery+")')","");
				
		
		try {
			((BioCollectionBean)focus).filterInPlace(qry);
			
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 			
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		result.setFocus(focus);
		return RET_CONTINUE;	
	}
}
