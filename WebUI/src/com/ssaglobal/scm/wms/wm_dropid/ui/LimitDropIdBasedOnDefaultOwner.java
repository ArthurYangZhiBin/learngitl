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
package com.ssaglobal.scm.wms.wm_dropid.ui;
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

public class LimitDropIdBasedOnDefaultOwner extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitDropIdBasedOnDefaultOwner.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitDropIdBasedOnDefaultOwner",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}
				
		
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		if(lockedOwners == null || lockedOwners.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","No Locked Owners Assigned. Exiting...",100L);
			return RET_CONTINUE;
		}
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked owner clause...",100L);
		String lockedOwnerClause = "";
		for(int i = 0; i < lockedOwners.size(); i++){
			if(i == 0)
				lockedOwnerClause += " PICKDETAIL.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
			else
				lockedOwnerClause += " OR PICKDETAIL.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked owner clause after iteration "+i+":"+lockedOwnerClause,100L);
		}
		
		BioCollectionBean focus = (BioCollectionBean)result.getFocus();
		try {
//Incident4105712_defect289342.b
//			focus.filterInPlace(new Query("wm_dropid","DPE('SQL','(@[wm_dropid.DROPID] IN (SELECT DROPIDDETAIL.DROPID FROM DROPIDDETAIL, PICKDETAIL WHERE DROPIDDETAIL.CHILDID = PICKDETAIL.CASEID AND ("+lockedOwnerClause+")))')",""));
			focus.filterInPlace(new Query("wm_dropid","DPE('SQL','(@[wm_dropid.DROPID] IN (SELECT DROPIDDETAIL.DROPID FROM DROPIDDETAIL, PICKDETAIL WHERE (DROPIDDETAIL.CHILDID = PICKDETAIL.CASEID OR PICKDETAIL.DROPID = DROPIDDETAIL.DROPID) AND ("+lockedOwnerClause+")))')",""));
//Incident4105712_defect289342.e

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
