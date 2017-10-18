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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.util.SessionUtil;

public class InternalTransferSerialNumberRenderQuery extends ActionExtensionBase{
	protected static String TABLE = "wm_internal_transfer_serial_temp";
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	protected static String TRANSFER_LINE_NO = "TRANSFERLINENUMBER";
	protected static String INSERT = "insert";
	protected static String UPDATE = "update";
	protected static String PARENT = "parent";
	protected static String EDIT = "EDIT";
		
	protected int execute(ActionContext context, ActionResult result){
		StateInterface state = context.getState();
		String whereClause = TABLE+"."+TRANSFER_KEY+"='"+SessionUtil.getInteractionSessionAttribute(TRANSFER_KEY, state).toString()
			+"' AND "+TABLE+"."+TRANSFER_LINE_NO+"='"+SessionUtil.getInteractionSessionAttribute(TRANSFER_LINE_NO, state)+"'";
		Query qry = new Query(TABLE, whereClause, null);
		BioCollectionBean focus = state.getDefaultUnitOfWork().getBioCollectionBean(qry);
		result.setFocus(focus);
		if(context.getSourceWidget().getName().equals(EDIT)){
			context.setNavigation(PARENT);
		}else{
			if(context.getSourceWidget().getForm().getFocus().isTempBio()){
				context.setNavigation(INSERT);
			}else{
				context.setNavigation(UPDATE);
			}
		}
		return RET_CONTINUE;
	}
}