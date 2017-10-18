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
package com.ssaglobal.scm.wms.wm_taskdispatch.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import org.apache.axis.utils.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;

public class TaskDispatchAutoFill extends ActionExtensionBase {
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiDataException{
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		QBEBioBean qbe = null;
		BioBean bio = null;
		
		//RM 8584. Task Strategy Configuration changes
		String sourceValue = context.getSourceWidget().getValue().toString();
		if(StringUtils.isEmpty(sourceValue))
		{
			return RET_CANCEL;
		}
		String qryString = "wm_strategy_code.STRATEGYCODE='"+sourceValue+"'";
		Query qry = new Query("wm_strategy_code", qryString, null);
		BioCollectionBean bcb = state.getDefaultUnitOfWork().getBioCollectionBean(qry);
		BioBean temp = bcb.get("0");
		if(focus.isTempBio()){
			qbe = (QBEBioBean)focus;
			qbe.set("DESCR", temp.get("DESCRIPTION").toString());
			qbe.set("TASKTYPE",temp.get("STRATEGYCODE"));
			qbe.set("TTMPICKCODE", temp.get("STRATEGYPICKCODE").toString());
		}else{
			bio = (BioBean)focus;
			bio.set("DESCR", temp.get("DESCRIPTION").toString());
			bio.set("TASKTYPE", temp.get("STRATEGYCODE"));
			bio.set("TTMPICKCODE", temp.get("STRATEGYPICKCODE").toString());
		}
		return RET_CONTINUE;
	}
}