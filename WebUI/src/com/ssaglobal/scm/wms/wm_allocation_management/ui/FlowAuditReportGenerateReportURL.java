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

package com.ssaglobal.scm.wms.wm_allocation_management.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
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

public class FlowAuditReportGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	//private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	private final String SERVER_TYPE_BIRT = "BIRT";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(FlowAuditReportGenerateReportURL.class);
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
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\nREPORT 1\n\n\n\n\n",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_header_detail_view",state);
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_RECEIVINGREPSGENREP","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_RECEIVINGREPSGENREP","Found Detail Form:Null",100L);	
		
		if(detailForm == null){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_REPORT_GEN_FROM_DETAIL",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		
		BioBean selection = (BioBean) detailForm.getFocus();	
		HashMap params = new HashMap();
		String startDate = null;
		String endDate = null;
		BioCollection orders = selection.getBioCollection("ORDERS");
		
		DateFormat dateFormat;
		//added for date format compatibility in BIRT
		dateFormat = ReportUtil.retrieveDateFormat(state);			
		
		if(orders != null && orders.size() > 0){
			BioCollection filteredOrders = orders.filter(new Query("wm_orders","","wm_orders.DELIVERYDATE"));
			GregorianCalendar gc = null;
			for(int i = 0; i < filteredOrders.size(); i++){
				gc = (GregorianCalendar)orders.elementAt(i).get("DELIVERYDATE"); 
				if(gc != null)
					break;
			}
			if(gc == null)
				gc = new GregorianCalendar();
			
			if (ReportUtil.isBIRT(state) == true) {
				startDate = dateFormat.format(gc.getTime());
			} else {
				startDate = dateFormat.format(gc.getTime()) + "T" + timeFormat.format(gc.getTime());
			}
						
		}
		else{
			GregorianCalendar gc = new GregorianCalendar();
			if (ReportUtil.isBIRT(state) == true) {
				startDate = dateFormat.format(gc.getTime());
			} else {
				startDate = dateFormat.format(gc.getTime()) + "T" + timeFormat.format(gc.getTime());
			}
		}
					
		GregorianCalendar gc = new GregorianCalendar();
		if (ReportUtil.isBIRT(state) == true) {
			endDate = dateFormat.format(gc.getTime());
		} else {
			endDate = dateFormat.format(gc.getTime()) + "T" + timeFormat.format(gc.getTime());
		}
		
		params.put("postart",selection.get("POKEY"));
		params.put("poend",selection.get("POKEY"));
		params.put("p_StartDate",startDate);
		params.put("p_EndDate",endDate);		
		String reportUrl = ReportUtil.retrieveReportURL(state,"CRPT21",params);
		ReportUtil.appendDbConnToReportUrl(reportUrl,state);
		//ReportUtil.appendPLocaleToReportUrl(reportUrl,state);		
		
		_log.debug("LOG_DEBUG_EXTENSION_RECEIVINGREPSGENREP","reportUrl:"+reportUrl,100L);
		
		//Put URL into context
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put("REPORTURL", reportUrl);
		

		return RET_CONTINUE;
	}	
}
