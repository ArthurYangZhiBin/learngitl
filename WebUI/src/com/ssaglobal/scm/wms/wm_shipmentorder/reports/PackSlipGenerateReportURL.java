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

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import com.agileitp.forte.framework.DataValue;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.ActiveFlag;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.WorkFlowConfigurationHelper;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PackSlipGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickListGenerateReportURL.class);

	private static final String REPORTID = "CRPT46";

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

		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		String pOrderDateFrom= "";
		String pOrderDateTo = "";
		
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			pOrderDateFrom = "01/01/1900";
			pOrderDateTo = dateFormat.format(new Date());
		} else {
			pOrderDateFrom = "1900-01-01" + "T00:00:00";
			pOrderDateTo = dateFormat.format(new Date()) + "T" + ReportUtil.cognosTimeFormat.format(new Date());
		}

		
		
		String orderKey = (String) focus.getValue("ORDERKEY");
		String externalOrderKey = (String) focus.getValue("EXTERNORDERKEY");
		_log.debug("LOG_SYSTEM_OUT","\n\t" + externalOrderKey + "\n",100L);
		//p_DATABASE, p_SCHEMA, p_OrdStart, p_OrdEnd, p_pOrderDateFrom, p_pOrderDateTo, p_ExtOrdStart, p_ExtOrdEnd
		HashMap parametersAndValues = new HashMap();
		parametersAndValues.put("p_OrdStart", orderKey);
		parametersAndValues.put("p_OrdEnd", orderKey);
		parametersAndValues.put("p_pOrderDateFrom", pOrderDateFrom);
		
		parametersAndValues.put("p_pOrderDateTo", pOrderDateTo);

		parametersAndValues.put("p_ExtOrdStart", externalOrderKey);
		parametersAndValues.put("p_ExtOrdEnd", externalOrderKey);
		

		String rpt_id = REPORTID;
		String storer = null;
		String customer = null;

		// get picklistreportid from ORDERS
		// --------------------------------
		String ordRptSel = "SELECT storerkey, consigneekey, packinglistreportid FROM orders "+
							"WHERE ORDERKEY = '"+orderKey+"'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(ordRptSel);
		if ( results.getRowCount() > 0 )
		{
			storer = results.getAttribValue(1).getAsString();
			customer = results.getAttribValue(2).getAsString();
			DataValue rptDV = results.getAttribValue(3);
			if (!rptDV.getNull())
			{
				// Order specified specific rpt_id, go with that
				rpt_id = rptDV.getAsString();
			}
			else
			{
				// Match OWNER and CUSTOMER
				// ------------------------
				String custOwnerReportSel = "SELECT rpt_id FROM storer_reports "+
										"WHERE storerkey = '"+storer+"' "+
										"AND consigneekey = '"+customer+"' "+
										"AND customreporttype = 2 ";
				EXEDataObject results2 = WmsWebuiValidationSelectImpl.select(custOwnerReportSel);
				if ( results2.getRowCount() > 0)
				{
					rpt_id = results2.getAttribValue(1).getAsString();
				}
				else
				{
					// Match OWNER
					// -----------
					String ownerReportSel = "SELECT rpt_id FROM storer_reports "+
											"WHERE storerkey = '"+storer+"' "+
											"AND consigneekey = 'DEFAULT' "+
											"AND customreporttype = 2 ";
					EXEDataObject results3 = WmsWebuiValidationSelectImpl.select(ownerReportSel);
					if ( results3.getRowCount() > 0)
					{
						rpt_id = results3.getAttribValue(1).getAsString();
					}
				}
			}
		}

		//if flag is not active, then suppres printing
		//if flag is active, force printing
		ActiveFlag workflowFlag = WorkFlowConfigurationHelper.getTypeStatus(WorkFlowConfigurationHelper.Type.PRNTPICK, orderKey, context.getState().getTempUnitOfWork());
		
		String reportURL = ReportUtil.retrieveReportURL(state, rpt_id, parametersAndValues, workflowFlag);
		_log.debug("LOG_DEBUG_EXTENSION_OrderSheetGenerateReportURL", reportURL, SuggestedCategory.NONE);

		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put(ReportUtil.REPORTURL, reportURL);
		return RET_CONTINUE;

	}

}
