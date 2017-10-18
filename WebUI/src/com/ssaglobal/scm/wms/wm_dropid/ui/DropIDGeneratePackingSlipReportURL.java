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

package com.ssaglobal.scm.wms.wm_dropid.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DropIDGeneratePackingSlipReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DropIDGeneratePackingSlipReportURL.class);

	private static final String REPORTID = "CRPT46";
	private final String SERVER_TYPE_BIRT = "BIRT";

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
		String start = "0";
		String end = "ZZZZZZZZZZZZZZZ";

		StateInterface state = context.getState();
		//p_DATABASE, p_SCHEMA, p_OrdStart, p_OrdEnd, p_pOrderDateFrom, p_pOrderDateTo, p_ExtOrdStart, p_ExtOrdEnd
		RuntimeFormInterface header = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_dropid_detail_header_view", state);
		DataBean headerFocus = header.getFocus();
		String dropID = headerFocus.getValue("DROPID") == null ? null
				: headerFocus.getValue("DROPID").toString().toUpperCase();
		if (dropID == null)
		{
			return RET_CANCEL;
		}

		BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("wm_pickdetail", "wm_pickdetail.DROPID = '"
				+ dropID + "'", null));
		if (results == null || results.size() == 0)
		{
			return RET_CANCEL;
		}
		String orderStart = (results.get("0")).getString("ORDERKEY");
		String orderEnd = (results.get("0")).getString("ORDERKEY");

		_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", "Beginning " + orderStart + " = "
				+ orderEnd, SuggestedCategory.NONE);
		for (int i = 0; i < results.size(); i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", i + " " + orderStart + " < "
					+ orderEnd, SuggestedCategory.NONE);
			BioBean selected = results.get("" + i);
			String tempOrderKey = selected.getString("ORDERKEY");
			if (tempOrderKey.compareTo(orderStart) < 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", i + " "
						+ "Updating orderStart based on" + tempOrderKey + ", " + orderStart + " < " + orderEnd, SuggestedCategory.NONE);
				orderStart = tempOrderKey;
			}
			if (tempOrderKey.compareTo(orderEnd) > 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", i + " "
						+ "Updating orderEnd based on" + tempOrderKey + ", " + orderStart + " < " + orderEnd, SuggestedCategory.NONE);
				orderEnd = tempOrderKey;
			}

		}
		_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", "End " + orderStart + " <= "
					+ orderEnd, SuggestedCategory.NONE);
		
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		Date currentDateTime = new Date(System.currentTimeMillis());
		String pOrderDateFrom = "";
		String pOrderDateTo = "";
		
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			pOrderDateFrom = "01/01/1900";
			pOrderDateTo = dateFormat.format(currentDateTime);
		} else {
			pOrderDateFrom = "1900-01-01T00:00:00";
			pOrderDateTo = dateFormat.format(currentDateTime) + "T" + ReportUtil.cognosTimeFormat.format(new Date());
		}
	
		
				
		
		
		HashMap parametersAndValues = new HashMap();
		parametersAndValues.put("p_OrdStart", orderStart);
		parametersAndValues.put("p_OrdEnd", orderEnd);
		parametersAndValues.put("p_pOrderDateFrom", pOrderDateFrom);
		parametersAndValues.put("p_pOrderDateTo", pOrderDateTo);

//		parametersAndValues.put("p_pOrderDateTo", ReportUtil.cognosDateFormat.format(new Date()) + "T"
//				+ ReportUtil.cognosTimeFormat.format(new Date()));

		parametersAndValues.put("p_ExtOrdStart", start);
		parametersAndValues.put("p_ExtOrdEnd", end);
		String reportURL = ReportUtil.retrieveReportURL(state, REPORTID, parametersAndValues);

		_log.debug("LOG_DEBUG_EXTENSION_DropIDGeneratePackingSlipReportURL", reportURL, SuggestedCategory.NONE);

		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put(ReportUtil.REPORTURL, reportURL);
		return RET_CONTINUE;
	}

}
