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

package com.ssaglobal.scm.wms.wm_oppertunistic_allocation;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_flow_thru_lane.ValidateFlowThruLane;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class EligibleOrdersGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EligibleOrdersGenerateReportURL.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{		

		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_opportunistic_allocation_list_view",state);
		if(listForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_ELIGIBLEORDERSGENREP","Found List Form:"+listForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_ELIGIBLEORDERSGENREP","Found List Form:Null",100L);	
		
		if(listForm == null){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		ArrayList selectedItems = listForm.getAllSelectedItems();
		if(selectedItems == null || selectedItems.size() != 1){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SELECT_ONE_ITEM",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
	 	HttpSession session = state.getRequest().getSession();
		Object objConnection = session.getAttribute("dbConnectionName");
		String connectionName = objConnection==null?"":(String)objConnection;

		   
		BioBean selection = (BioBean) selectedItems.get(0);	
		HashMap params = new HashMap();
		params.put("p_storer",selection.get("STORERKEY"));
		params.put("p_sku",selection.get("SKU"));
		params.put("p_uid",UserUtil.getUserId(state));
		params.put("p_conn",connectionName);
		String reportUrl = ReportUtil.retrieveReportURL(state,"CRPT42",params);
		ReportUtil.appendDbConnToReportUrl(reportUrl,state);
		ReportUtil.appendPLocaleToReportUrl(reportUrl,state);		
		_log.debug("LOG_DEBUG_EXTENSION_ELIGIBLEORDERSGENREP","reportUrl:"+reportUrl,100L);
		
		//Put URL into context
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ reportUrl,100L);
		userCtx.put("REPORTURL", reportUrl);
		

		return RET_CONTINUE;
	}	
}
