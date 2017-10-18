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
package com.ssaglobal.scm.wms.wm_physical_parameters.action;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_physical_parameters.ui.PhysicalParametersConstants;

public class PhysicalParametersGeneralAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PhysicalParametersGeneralAction.class);
	//Session Attributes
	private static final String PP_ERROR_MESSAGE = "PP_ERROR_MESSAGE";
	
	//Static widget names
	private static final String PUT_ON_HOLD = "Put Inventory on Hold";
	private static final String REMOVE_FROM_HOLD = "Remove Inventory from Hold";
	private static final String CLEAR = "Clear Physical";
	private static final String RELEASE = "Release PI Counts";
	private static final String BY_LOT = "By Lot";
	private static final String BY_ITEM = "By Item";
	private static final String BY_ID = "By LPN";
	
	//Stored procedures
	private static final String PROC_CLEAR = "NSPCLEARPHYSICAL";
	private static final String PROC_RELEASE = "NSPRELEASEPICOUNT";
	private static final String PROC_PUT_ON_HOLD = "NSPPUTINVENTORYONHOLDFORPHYSICAL";
	private static final String PROC_REMOVE_FROM_HOLD = "NSPREMOVEINVENTORYONHOLDFORPHYSICAL";
	private static final String PROC_BY_LOT = "NSP_FILL_STORERKEYSKU_LOT_A";
	private static final String PROC_BY_ID = "NSP_FILL_LOT_STORERKEYSKU_A";
	private static final String PROC_BY_ITEM = "NSP_FILL_STORERKEYSKULOT_ID_A";
	
	//Navigations
	private final static String DEFAULT_NAV = "menuClickEvent221";
	private final static String ERROR_NAV = "menuClickEvent464";
	private final static String PREALLOC_ERROR_NAV = "menuClickEvent464";
	
	//Static error messages
	private static final String ERROR_MESSAGE = "WMEXP_PUT_INV_ON_HOLD"; 
	private static final String PREALLOC_ERROR_MESSAGE = "WMEXP_PUT_INV_ON_HOLD_PREALLOC"; 
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		String action = context.getActionObject().getName();
		
		String procName = fillProcName(action);
	
		if(procName!=null){
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			actionProperties.setProcedureParameters(null);
			actionProperties.setProcedureName(procName);
			if(action.equals(PUT_ON_HOLD)){
				EXEDataObject actionResult =null;
				try{
					actionResult = WmsWebuiActionsImpl.doAction(actionProperties);
				}catch(WebuiException e){
				}
				String pendingHoldCount = actionResult.getAttribValue(new TextData("PendingHoldDueToAlloc")).toString();
				_log.debug("LOG_SYSTEM_OUT","Pending Hold Count: "+pendingHoldCount,100L);
				
				//jp.answerlink.212484.begin
				String checkPreAlloc = null;
				if (actionResult.getAttribValue(new TextData("CheckPreAlloc"))!=null){
					checkPreAlloc = actionResult.getAttribValue(new TextData("CheckPreAlloc")).toString();
				}
				
				if(checkPreAlloc!=null && checkPreAlloc.equalsIgnoreCase("1")){
					if(existPreallocatepickdetail()){
						//Set error message to session, change navigation 
						StateInterface state = context.getState();
						HttpSession session = state.getRequest().getSession();
						String base = getTextMessage(PREALLOC_ERROR_MESSAGE, new Object[] {}, state.getLocale());
						session.setAttribute(PP_ERROR_MESSAGE, base);
						session.setAttribute(PhysicalParametersConstants.ERROR_ID, PhysicalParametersConstants.ERROR_PREALLOCATION);
						context.setNavigation(PREALLOC_ERROR_NAV);
						return RET_CONTINUE;
					}
				}
				//jp.answerlink.212484.end

				if(!pendingHoldCount.equals("0")){
					//Set error message to session, change navigation 
					StateInterface state = context.getState();
					HttpSession session = state.getRequest().getSession();
					String base = getTextMessage(ERROR_MESSAGE, new Object[] {}, state.getLocale());
					session.setAttribute(PP_ERROR_MESSAGE, base);
					session.setAttribute(PhysicalParametersConstants.ERROR_ID, PhysicalParametersConstants.ERROR_ALLOCATION);
					context.setNavigation(ERROR_NAV);
					return RET_CONTINUE;
				}
				context.setNavigation(DEFAULT_NAV);
			}else{
				try{
					WmsWebuiActionsImpl.doAction(actionProperties);	
				}catch(WebuiException e){
					throw new UserException(e.getMessage(), new Object[] {});
				}
			}
		}
		return RET_CONTINUE;
	}
	
	private String fillProcName(String widget){
		String procName;
		
		//Set procedure name
		if(widget.equals(CLEAR))
			procName = PROC_CLEAR;
		else if(widget.equals(RELEASE))
			procName = PROC_RELEASE;
		else if(widget.equals(PUT_ON_HOLD))
			procName = PROC_PUT_ON_HOLD;
		else if(widget.equals(REMOVE_FROM_HOLD))
			procName = PROC_REMOVE_FROM_HOLD;
 		else if(widget.equals(BY_LOT))
				procName = PROC_BY_LOT;
 		else if(widget.equals(BY_ID))
				procName = PROC_BY_ID;
 		else if(widget.equals(BY_ITEM))
				procName = PROC_BY_ITEM;
		else 
			procName = null;
		
		return procName;
	}
	
	private boolean existPreallocatepickdetail(){
		String stmt = "SELECT a.preallocatePickdetailKey " +
			" FROM preallocatePickdetail a, physicalParameters b " +
			" WHERE a.storerkey BETWEEN b.storerkeymin AND b.storerkeymax " +
			" AND a.sku BETWEEN b.skumin AND b.skumax " +
			" AND a.qty > 0 ";

		System.out.println("///QUERY " + stmt);
		try {
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(stmt);
			
			if (results.getRowCount() > 0)
				return true;
			
		} catch (DPException e) {
			_log.debug("PHYSICAL_PARAMETERS_GENEXT", "", SuggestedCategory.NONE);
			e.printStackTrace();
		}

		return false;
	}

}