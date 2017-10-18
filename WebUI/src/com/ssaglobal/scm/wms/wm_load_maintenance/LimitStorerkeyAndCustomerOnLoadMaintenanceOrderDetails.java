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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitStorerkeyAndCustomerOnLoadMaintenanceOrderDetails extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitStorerkeyAndCustomerOnLoadMaintenanceOrderDetails.class);
	
	
	protected int preRenderListForm(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		String storerColName = (String)getParameter("storerColName");
		if(storerColName == null || storerColName.length() == 0)
			storerColName = "STORERKEY";
		
		//Verify owner locking is on.
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		if(ownerLockFlag == null || ownerLockFlag.intValue() == 0)
			return RET_CONTINUE;
				
		//Verify Focus is a BioCollection
		if(!form.getFocus().isBioCollection())
			return RET_CONTINUE;
		//Get pre-filter value of Storer
		//String ownerPreFilter = WSDefaultsUtil.getPreFilterValueByType("STORER", state);
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		BioCollectionBean focus = (BioCollectionBean)form.getFocus();
		String lockedOwnerClause = "";
		if(lockedOwners != null && lockedOwners.size() > 0){
			for(int i = 0; i < lockedOwners.size(); i++){
				if(i == 0)
					lockedOwnerClause += " ("+focus.getBioTypeName()+".STORER = '" + lockedOwners.get(i) + "' ";
				else
					lockedOwnerClause += " OR "+focus.getBioTypeName()+".STORER = '" + lockedOwners.get(i) + "' ";
				if(i == lockedOwners.size() - 1)
					lockedOwnerClause += " OR "+focus.getBioTypeName()+".STORER IS NULL OR "+focus.getBioTypeName()+".STORER = '' OR "+focus.getBioTypeName()+".STORER = ' ') ";
			}	
		}
		
		String lockedCustomerClause = "";
		ArrayList lockedCustomers = WSDefaultsUtil.getLockedCustomers(state);
		if(lockedCustomers != null && lockedCustomers.size() > 0){
			for(int i = 0; i < lockedCustomers.size(); i++){
				if(lockedOwnerClause.length() == 0){
					if(i == 0)
						lockedCustomerClause += focus.getBioTypeName()+".CUSTOMER = '" + lockedCustomers.get(i) + "' ";
					else
						lockedCustomerClause += " OR "+focus.getBioTypeName()+".CUSTOMER = '" + lockedCustomers.get(i) + "' ";
				}
				else{
					if(i == 0)
						lockedCustomerClause += " AND ("+focus.getBioTypeName()+".CUSTOMER = '" + lockedCustomers.get(i) + "' ";
					else
						lockedCustomerClause += " OR "+focus.getBioTypeName()+".CUSTOMER = '" + lockedCustomers.get(i) + "' ";
				}
				
				if(i == lockedCustomers.size() - 1){
					if(lockedOwnerClause.length() == 0){
						lockedCustomerClause += " OR "+focus.getBioTypeName()+".CUSTOMER IS NULL ";
					}
					else{
						lockedCustomerClause += " OR "+focus.getBioTypeName()+".CUSTOMER IS NULL OR "+focus.getBioTypeName()+".CUSTOMER = '' OR "+focus.getBioTypeName()+".CUSTOMER = ' ') ";
					}					
				}
			}	
		}
		
		if(lockedOwnerClause.length() > 0 || lockedCustomerClause.length() > 0){
			try {
				_log.debug("LOG_SYSTEM_OUT","\n\nQuery:"+lockedOwnerClause+lockedCustomerClause+"\n\n",100L);
				focus.filterInPlace(new Query(focus.getBioTypeName(),lockedOwnerClause+lockedCustomerClause,""));
			} catch (EpiDataException e) { 
				e.printStackTrace();
				String args[] = new String[0]; 			
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		form.setFocus(focus);
		return RET_CONTINUE;	
	}
}
