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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class LMUnitsListPrerenderAction extends FormExtensionBase{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LMUnitsListPrerenderAction.class);
	
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_LMUNITSLSTPREREN","Executing LMUnitsListPrerenderAction",100L);		

		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		//String caseId = (String)session.getAttribute("LMCASEID");
		//		if(caseId == null){			
		//			_log.debug("LOG_DEBUG_EXTENSION_LMUNITSLSTPREREN","load detail not selected...",100L);
		//			_log.debug("LOG_DEBUG_EXTENSION_LMUNITSLSTPREREN","Exiting LMUnitsListPrerenderAction",100L);			
		//			return RET_CONTINUE;
		//		}
		String loadStopId = (String) session.getAttribute("LOADSTOPID");
		//_log.debug("LOG_DEBUG_EXTENSION_LMUNITSLSTPREREN","Got case id "+caseId+" from session...",100L);
		//Query query = new Query("wm_loadunitdetail","wm_loadunitdetail.LOADSTOPID = '"+loadStopId.toUpperCase()+"' AND wm_loadunitdetail.UNITID = '"+caseId.toUpperCase()+"'","");
		String qryString = "wm_loadunitdetail.LOADSTOPID = '" + loadStopId.toUpperCase() + "' ";
		Query query = new Query("wm_loadunitdetail", qryString, "");
		DataBean focus = form.getFocus();
		if (focus instanceof BioCollectionBean) {
			String queryExpression = ((BioCollectionBean) focus).getQuery().getQueryExpression();
			if (queryExpression != null && !queryExpression.contains(qryString.trim())) {
				((BioCollectionBean) focus).filterInPlace(query);
			}
		}
		//		form.setFocus(focus);
		_log.debug("LOG_DEBUG_EXTENSION_LMUNITSLSTPREREN","Exiting LMUnitsListPrerenderAction",100L);
		return RET_CONTINUE;
	}	
}