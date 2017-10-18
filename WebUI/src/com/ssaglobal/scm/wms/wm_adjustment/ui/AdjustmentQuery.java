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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;

public class AdjustmentQuery extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String parentQuery = ""; 
		Query parentQry = null;
		boolean isAdjustment = getParameterBoolean("isAdjustment");
		if(isAdjustment){
			parentQuery = "DPE('SQL','@[wm_adjustment.ADJUSTMENTKEY] in (SELECT DISTINCT ADJUSTMENTKEY FROM ADJUSTMENTDETAIL WHERE REASONCODE != \\'UNRECEIVE\\')')";
			parentQry = new Query("wm_adjustment", parentQuery, null);
		}else{
			//UPDATE: Defect 282270 - Unable to view records in Oracle environments
			parentQuery = "DPE('SQL','@[wm_receiptreversal.ADJUSTMENTKEY] in (SELECT DISTINCT ADJUSTMENTKEY FROM ADJUSTMENTDETAIL WHERE REASONCODE = \\'UNRECEIVE\\')')";
			//END UPDATE
			parentQry = new Query("wm_receiptreversal", parentQuery, null);
		}
		BioCollectionBean parentList = uow.getBioCollectionBean(parentQry);
        boolean showEmptyList = getParameterBoolean("show empty list");
        if(showEmptyList)
            parentList.setEmptyList(true);
        else
            parentList.setEmptyList(false);
		result.setFocus(parentList);
		return RET_CONTINUE;
	}
}