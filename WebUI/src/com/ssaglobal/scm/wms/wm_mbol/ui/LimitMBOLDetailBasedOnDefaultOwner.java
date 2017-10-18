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
package com.ssaglobal.scm.wms.wm_mbol.ui;
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
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitMBOLDetailBasedOnDefaultOwner extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitMBOLDetailBasedOnDefaultOwner.class);
	protected int preRenderListForm(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {	
		
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}
				
		//Verify Focus Is Not Empty
		DataBean focus = form.getFocus();		
		if(focus == null || !focus.isBioCollection())
			return RET_CONTINUE;
		
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		if(lockedOwners == null || lockedOwners.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","No Locked Owners Assigned. Exiting...",100L);
			return RET_CONTINUE;

		}
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked owner clause...",100L);
		String lockedOwnerOrderClause = "";
		String lockedOwnerPalletClause = "";
		for(int i = 0; i < lockedOwners.size(); i++){
			if(i == 0){
				lockedOwnerOrderClause += " ORDERS.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
				lockedOwnerPalletClause += " PALLETDETAIL.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
			}
			else{
				lockedOwnerOrderClause += " OR ORDERS.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
				lockedOwnerPalletClause += " OR PALLETDETAIL.STORERKEY = \\'" + lockedOwners.get(i) + "\\' ";
			}
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked owner order clause after iteration "+i+":"+lockedOwnerOrderClause,100L);
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked owner pallet clause after iteration "+i+":"+lockedOwnerPalletClause,100L);
		}		
				
		try {			
			((BioCollectionBean)focus).filterInPlace(new Query("wm_mboldetail","DPE('SQL','(@[wm_mboldetail.PALLETKEY] IN (SELECT PALLETKEY FROM PALLETDETAIL WHERE  ("+lockedOwnerPalletClause+")) OR @[wm_mboldetail.PALLETKEY] IS NULL OR @[wm_mboldetail.PALLETKEY] = \\'\\') AND (@[wm_mboldetail.ORDERKEY] IN (SELECT ORDERKEY FROM ORDERS WHERE  ("+lockedOwnerOrderClause+")) OR @[wm_mboldetail.ORDERKEY] IS NULL OR @[wm_mboldetail.ORDERKEY] = \\'\\')')",""));			
		} catch (EpiDataException e) { 
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Error Occured. Exiting...",100L);
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY",e.getErrorMessage(),100L);			
			e.printStackTrace();
			String args[] = new String[0]; 			
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		form.setFocus(focus);
		return RET_CONTINUE;
	}
	
}
