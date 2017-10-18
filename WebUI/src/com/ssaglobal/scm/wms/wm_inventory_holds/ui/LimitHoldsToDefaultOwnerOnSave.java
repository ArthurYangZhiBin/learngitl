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
package com.ssaglobal.scm.wms.wm_inventory_holds.ui;
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

public class LimitHoldsToDefaultOwnerOnSave extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitHoldsToDefaultOwnerOnSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}
		
		String widgetName = (String)getParameter("WidgetName");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		String formName = (String)getParameter("FormName");		
		RuntimeNormalForm form = null;
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param widgetName:"+widgetName,100L);
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Got Param FormName:"+formName,100L);
		
		//Form name is required
		if(formName != null && formName.length() > 0)
			form = (RuntimeNormalForm)FormUtil.findForm(state.getCurrentRuntimeForm(), "", formName,tabs , state);
		
		if(form == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","form not found",100L);
			return RET_CANCEL;
		}
					
		
		//Check widget. If its value is an order number that belongs to an owner other than the default then throw an error.
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		if(lockedOwners == null || lockedOwners.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","No Locked Owners Assigned. Exiting...",100L);
			return RET_CONTINUE;
//			String args[] = new String[0]; 			
//			String errorMsg = getTextMessage("WMEXP_NO_LOCKED_OWNERS",args,state.getLocale());
//			throw new UserException(errorMsg,new Object[0]);
		}
		String lockedOwnerClause = "";
		for(int i = 0; i < lockedOwners.size(); i++){
			if(i == 0)
				lockedOwnerClause += " wm_lotxlocxid.STORERKEY = '" + lockedOwners.get(i) + "' ";
			else
				lockedOwnerClause += " OR wm_lotxlocxid.STORERKEY = '" + lockedOwners.get(i) + "' ";
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked owner clause after iteration "+i+":"+lockedOwnerClause,100L);
		}				
		DataBean focus = form.getFocus();		
		String lotValue = (String)focus.getValue(widgetName);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		Query qry = new Query("wm_lotxlocxid","wm_lotxlocxid.LOT = '"+lotValue+"' AND ("+lockedOwnerClause+")","");
		BioCollectionBean orders = uow.getBioCollectionBean(qry);
		
		try {
			if(orders == null || orders.size() == 0){
							
				String args[] = new String[2]; 
				args[0] = "";
				for(int i = 0; i < lockedOwners.size(); i++){
					if(i == 0)
						args[0] += lockedOwners.get(i);
					else
						args[0] += ", " + lockedOwners.get(i);
				}	
				args[1] = form.getFormWidgetByName(widgetName).getLabel("label",state.getUser().getLocale()).replaceAll(":","");;
				String errorMsg = getTextMessage("WMEXP_LIMIT_LOT_DATA_ENTRY",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 			
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		
		return RET_CONTINUE;	
	}
}
