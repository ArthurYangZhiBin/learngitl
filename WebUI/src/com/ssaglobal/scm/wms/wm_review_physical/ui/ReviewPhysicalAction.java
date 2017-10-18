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
package com.ssaglobal.scm.wms.wm_review_physical.ui;


import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class ReviewPhysicalAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReviewPhysicalAction.class);
	
	//Static widget names
	private static final String BY_LOT = "wm_review_physical_fill_physical_by_lot";
	private static final String BY_ITEM = "wm_review_physical_fill_physical_by_item";
	private static final String BY_ID = "wm_review_physical_fill_physical_by_lpn";
	
	//Stored procedures
	private static final String PROC_BY_LOT = "NSP_FILL_STORERKEYSKU_LOT_A";
	private static final String PROC_BY_ID = "NSP_FILL_STORERKEYSKULOT_ID_A";
	private static final String PROC_BY_ITEM = "NSP_FILL_LOT_STORERKEYSKU_A";
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		String action = context.getActionObject().getName();
		String procName = fillProcName(action);
		if(procName!=null){
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			actionProperties.setProcedureParameters(null);
			actionProperties.setProcedureName(procName);
				try{
					WmsWebuiActionsImpl.doAction(actionProperties);	
				}catch(WebuiException e){
					throw new UserException(e.getMessage(), new Object[] {});
				}
		}
		return RET_CONTINUE;
	}
	
	private String fillProcName(String widget){
		String procName;
		
		//Set procedure name
 		if(widget.equals(BY_LOT))
				procName = PROC_BY_LOT;
 		else if(widget.equals(BY_ID))
				procName = PROC_BY_ID;
 		else if(widget.equals(BY_ITEM))
				procName = PROC_BY_ITEM;
		else 
			procName = null;
		
		return procName;
	}
}
