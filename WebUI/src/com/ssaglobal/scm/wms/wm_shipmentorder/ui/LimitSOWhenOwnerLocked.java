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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

//Import 3rd party packages and classes
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitSOWhenOwnerLocked extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitSOWhenOwnerLocked.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}						
		
		ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
		ArrayList lockedCarriers = WSDefaultsUtil.getLockedCarriers(state);
		ArrayList lockedBillTo = WSDefaultsUtil.getLockedBillTo(state);
		String lockedCustomerClause = "";
		String lockedCarrierClause = "";
		String lockedBillToClause = "";
		if(lockedCustomers != null && lockedCustomers.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Found Locked Customers:"+lockedCustomers,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked customer clause...",100L);
			lockedCustomerClause += " wm_orders.CONSIGNEEKEY = '' OR wm_orders.CONSIGNEEKEY = ' ' OR wm_orders.CONSIGNEEKEY IS NULL";
			for(int i = 0; i < lockedCustomers.size(); i++){				
				lockedCustomerClause += " OR wm_orders.CONSIGNEEKEY = '" + lockedCustomers.get(i) + "' ";
				_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked customer clause after iteration "+i+":"+lockedCustomerClause,100L);
			}
		}
		if(lockedCarriers != null && lockedCarriers.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Found Locked Carriers:"+lockedCarriers,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked carrier clause...",100L);
			lockedCarrierClause += " wm_orders.CarrierCode = '' OR wm_orders.CarrierCode = ' ' OR wm_orders.CarrierCode IS NULL";
			for(int i = 0; i < lockedCarriers.size(); i++){				
				lockedCarrierClause += " OR wm_orders.CarrierCode = '" + lockedCarriers.get(i) + "' ";
				_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked customer clause after iteration "+i+":"+lockedCarrierClause,100L);
			}
		}
		if(lockedBillTo != null && lockedBillTo.size() > 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Found Locked Carriers:"+lockedBillTo,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked carrier clause...",100L);
			lockedBillToClause += " wm_orders.BILLTOKEY = '' OR wm_orders.BILLTOKEY = ' ' OR wm_orders.BILLTOKEY IS NULL";
			for(int i = 0; i < lockedBillTo.size(); i++){				
				lockedBillToClause += " OR wm_orders.BILLTOKEY = '" + lockedBillTo.get(i) + "' ";
				_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked customer clause after iteration "+i+":"+lockedBillToClause,100L);
			}
		}			
		String bioQry = "";
				
		if(lockedCustomerClause.length() > 0){	
			//Changed from lockedCarrierClause to lockedCustomerClause: Caused error for locked Customers
			bioQry = "("+lockedCustomerClause+")";
			//End update
		}
		
		if(lockedCarrierClause.length() > 0){
			if(bioQry.length() > 0)
				bioQry += " AND ("+lockedCarrierClause+")";
			else
				bioQry = "("+lockedCarrierClause+")";
		}
		if(lockedBillToClause.length() > 0){
			if(bioQry.length() > 0)
				bioQry += " AND ("+lockedBillToClause+")";
			else
				bioQry = "("+lockedBillToClause+")";
		}
		
		if(bioQry.length() == 0)
			return RET_CONTINUE;
		
		BioCollectionBean focus = (BioCollectionBean)result.getFocus();
		try {
			focus.filterInPlace(new Query("wm_orders", bioQry, ""));
		} catch (EpiDataException e) { 
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY", "Error Occured. Exiting...", 100L);
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY", e.getErrorMessage(), 100L);			
			e.printStackTrace();
			String args[] = new String[0]; 			
			String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
		result.setFocus(focus);
		return RET_CONTINUE;	
	}
}
