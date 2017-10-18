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
package com.ssaglobal.scm.wms.wm_receiptreversal;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeNormalForm;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class LimitASNToDefaultOwner extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LimitASNToDefaultOwner.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Executing LimitStorerKeyFilterToDefault",100L);		
		StateInterface state = context.getState();
		
		//Verify owner locking is on.		
		if(!WSDefaultsUtil.isOwnerLocked(state)){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","Owner is not locked. Exiting...",100L);
			return RET_CONTINUE;
		}
				
		RuntimeNormalForm form = null;
		form = (RuntimeNormalForm)state.getCurrentRuntimeForm();
		
		if(form == null){
			_log.error("LOG_DEBUG_EXTENSION_LIMITSTORKEY","form not found",100L);
			return RET_CANCEL;
		}
					
		ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
		if(lockedOwners == null || lockedOwners.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","No Locked Owners Assigned. Exiting...",100L);
			return RET_CONTINUE;
//			String args[] = new String[0]; 			
//			String errorMsg = getTextMessage("WMEXP_NO_LOCKED_OWNERS",args,state.getLocale());
//			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","creating locked owner clause...",100L);
		String lockedOwnerClause = "";
		for(int i = 0; i < lockedOwners.size(); i++){
			if(i == 0)
				lockedOwnerClause += " receipt.STORERKEY = '" + lockedOwners.get(i) + "' ";
			else
				lockedOwnerClause += " OR receipt.STORERKEY = '" + lockedOwners.get(i) + "' ";
			_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","locked owner clause after iteration "+i+":"+lockedOwnerClause,100L);
		}				
		//Check widget. If its value is an order number that belongs to an owner other than the default then throw an error.					
		String asnValue = null;
		asnValue = context.getSourceWidget().getDisplayValue();
		_log.debug("LOG_DEBUG_EXTENSION_LIMITSTORKEY","got asnValue:"+asnValue,100L);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
		Query qry = new Query("receipt","receipt.RECEIPTKEY = '"+asnValue+"' AND ("+lockedOwnerClause+")","");
		BioCollectionBean orders = uow.getBioCollectionBean(qry);
		
		try {
			if(orders == null || orders.size() == 0){
				RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
		    	String WidgetName = context.getSourceWidget().getName();							
				String[] ErrorParem = new String[2];	     	   
	     	   ErrorParem[0] = "";
				for(int i = 0; i < lockedOwners.size(); i++){
					if(i == 0)
						ErrorParem[0] += lockedOwners.get(i);
					else
						ErrorParem[0] += ", " + lockedOwners.get(i);
				}	
	     	   	ErrorParem[1] = form.getFormWidgetByName(WidgetName).getLabel("label",state.getUser().getLocale()).replaceAll(":","");		
	     	   	FieldException UsrExcp = new FieldException(FormName, WidgetName,"WMEXP_LIMIT_ORDER_DATA_ENTRY", ErrorParem);
	     	   	form.getFormWidgetByName("STORERKEY").setDisplayValue(null);
	     	   	form.getFocus().setValue("STORERKEY", null);
	     	   	throw UsrExcp;
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
