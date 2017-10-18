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

package com.ssaglobal.scm.wms.wm_shipmentorder.reports;

// Import 3rd party packages and classes

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.ReportUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrderSheetGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OrderSheetGenerateReportURL.class);

	private static final String REPORTID = "CRPT43";
	

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();
		String headerSlot = getParameter("SLOT") == null ? "list_slot_1" : getParameterString("SLOT");
		DataBean focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(headerSlot), null).getFocus();

		//Order Sheet Parameters
		//p_DATABASE, p_SCHEMA,  p_ordStart, p_ordEnd, p_pStartDate, p_pEndDate, outputLocale
		String orderKey = (String) focus.getValue("ORDERKEY");

		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		String pStartDate= "";
		String pEndDate = "";
		
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			pStartDate = "01/01/1900";
			pEndDate = dateFormat.format(new Date());
		} else {
			pStartDate = "1900-01-01" + "T00:00:00";
			pEndDate = dateFormat.format(new Date()) + "T" + ReportUtil.cognosTimeFormat.format(new Date());
		}
		
		
		
		HashMap parametersAndValues = new HashMap();
//SCM-00000-04707		parametersAndValues.put("p_ordStart", orderKey);
//SCM-00000-04707		parametersAndValues.put("p_ordEnd", orderKey);
		parametersAndValues.put("p_OrdStart", orderKey);			//SCM-00000-04707
		parametersAndValues.put("p_OrdEnd", orderKey);				//SCM-00000-04707
		parametersAndValues.put("p_pStartDate", pStartDate);
		parametersAndValues.put("p_pEndDate", pEndDate);
		

		String reportURL = ReportUtil.retrieveReportURL(state, REPORTID, parametersAndValues);
		_log.debug("LOG_DEBUG_EXTENSION_OrderSheetGenerateReportURL", reportURL, SuggestedCategory.NONE);

		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put(ReportUtil.REPORTURL, reportURL);
		return RET_CONTINUE;

	}

}
