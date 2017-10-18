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

package com.ssaglobal.scm.wms.wm_preview_invoice.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_po.ui.ValidatePOBeforeReopenAction;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PreviewInvoiceGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	private static final String REPORT_ID = "CRPT16";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreviewInvoiceGenerateReportURL.class);

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
		_log.debug("LOG_SYSTEM_OUT","\n\t" + "Start of PreviewInvoiceGenerateReportURL" + "\n",100L);
		DataBean focus = result.getFocus();
		if (focus instanceof BioBean)
		{
			_log.debug("LOG_SYSTEM_OUT","focus is a BioBean",100L);
		}
		else if (focus instanceof QBEBioBean)
		{
			_log.debug("LOG_SYSTEM_OUT","focus is a QBEBioBean",100L);
		}
		else if (focus instanceof BioCollectionBean)
		{
			_log.debug("LOG_SYSTEM_OUT","focus is a BioCollectionBean",100L);
		}
		else if (focus == null)
		{
			_log.debug("LOG_SYSTEM_OUT","focus is null",100L);
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT",focus.getClass().getName(),100L);
		}

		/*
		 * pStartDate - begin date
		 * pEndDate – end date
		 * InvoiceStart – Invoice start
		 * InvoiceEnd – Invoice End
		 * pCustomer – Owner Key
		 */

		BioCollectionBean collection = (BioCollectionBean) result.getFocus();
		_log.debug("LOG_SYSTEM_OUT","\n\t Size " + collection.size() + "\n",100L);
		String pCustomerStart = "Z";
		String pCustomerEnd = "A";
		String invoiceStartS = String.valueOf(Long.MAX_VALUE);
		String invoiceEndS = String.valueOf(Long.MIN_VALUE);
		long invoiceStart = Long.MAX_VALUE;
		long invoiceEnd = Long.MIN_VALUE;

		for (int i = 0; i < collection.size(); i++)
		{
			BioBean current = collection.get("" + i);
			if (!isEmpty(current.get("STORERKEY")))
			{
				String currentCustomer = (String) current.get("STORERKEY");
				if (currentCustomer.compareToIgnoreCase(pCustomerStart) < 0)
				{
					//less
					pCustomerStart = currentCustomer;
				}

				if (currentCustomer.compareToIgnoreCase(pCustomerEnd) > 0)
				{
					//greater
					pCustomerEnd = currentCustomer;
				}

			}
			if (!isEmpty(current.get("INVOICEKEY")))
			{
				String currentInvoiceS = (String) current.get("INVOICEKEY");
				long currentInvoice = Long.parseLong(((String) current.get("INVOICEKEY")));
				
				if (currentInvoice < invoiceStart)
				{
					invoiceStart = currentInvoice;
					invoiceStartS = currentInvoiceS;
				}
				if (currentInvoice > invoiceEnd)
				{
					invoiceEnd = currentInvoice;
					invoiceEndS = currentInvoiceS;
				}
			}
		}
	
		//Start Date
		//End Date
		///														cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Infor%20WM%27%5d%2freport%5b%40name%3d%27Customer%20Invoice%27%5d

		//
		//http://usdadvaraveti1.ssainternal.net/				cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Metadata%20for%20WM%204000%27%5d%2freport%5b%40name%3d%27Customer%20Invoice%27%5d	&run.prompt=false&ui.header=false &p_DATABASE=PRD1&p_SCHEMA=wh1		&p_pStartDate=2006-06-01%2000%3a00%3a00.000&p_EndDate=2006-06-30%2000%3a00%3a00.000&p_pInvoice=0000000012&p_pCustomer=TEST&outputLocale=fr
		///														cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Metadata%20for%20WM%204000%27%5d%2freport%5b%40name%3d%27Empty%20Location%20Report%27%5d		

		StateInterface state = context.getState();
		String report_url;
		
//		report_url = ReportUtil.retrieveReportURLStart(state, REPORT_ID);
//		SsaAccessBase appAccess = new SQLDPConnectionFactory();
//		String cognosServerURL = appAccess.getValue("webUIConfig", "cognosServerURL");
//
//		String rpt_id = "CRPT16";
//		Query rptQuery = new Query("wm_pbsrpt_reports","wm_pbsrpt_reports.RPT_ID = '" + rpt_id + "'", null);
//		BioCollectionBean rpts = context.getState().getDefaultUnitOfWork().getBioCollectionBean(rptQuery);
//		String report_url = rpts.get("0").getValue("RPT_URL").toString();
//		
//		//String report_url = "/cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Infor%20WM%27%5d%2freport%5b%40name%3d%27Customer%20Invoice%27%5d";
//		report_url = cognosServerURL + report_url + "&run.prompt=false&ui.header=false&p_DATABASE=";
//		HttpSession session = state.getRequest().getSession();
//		Object objDatabaseName = session.getAttribute("dbDatabase");
//		String databaseName = objDatabaseName == null ? "" : (String) objDatabaseName;
//		report_url = report_url + databaseName;
//
//		report_url = report_url + "&p_SCHEMA=";
//		Object objUserId = session.getAttribute("dbUserName");
//		String userId = objUserId == null ? "" : (String) objUserId;
//		report_url = report_url + userId;

	
		//need to add date 2006-06-01
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		//String pStartDate = "1900-01-01";
		
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		Date currentDateTime = new Date(System.currentTimeMillis());
		String pStartDate = "";
		String pEndDate = "";
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			pStartDate = "01/01/1900";
			pEndDate = dateFormat.format(currentDateTime);
		} else {
			pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
			pEndDate = dateFormat.format(currentDateTime) + "%2000%3a00%3a00.000";
		}
		
		
		
		
		HashMap paramsAndValues = new HashMap();
		paramsAndValues.put("p_pStartDate",pStartDate );
		paramsAndValues.put("p_pEndDate", pEndDate);
		paramsAndValues.put("p_pInvoiceStart", invoiceStartS);
		paramsAndValues.put("p_pInvoiceEnd", invoiceEndS);
		paramsAndValues.put("p_pCustomer",pCustomerEnd);
		
		
		
//		report_url += ReportUtil.retrieveReportURLEnd(state, paramsAndValues);
//		report_url += "&p_pStartDate=" + pStartDate +"%2000%3a00%3a00.000" + "&p_pEndDate=" + pEndDate +"%2000%3a00%3a00.000";
//		report_url += "&p_pInvoiceStart=" + invoiceStartS +"&p_pInvoiceEnd=" + invoiceEndS;
//		report_url += "&p_pCustomer=" + pCustomerEnd;
//		report_url += "&outputLocale=" + state.getLocale().getName();
		
		
		
		report_url = ReportUtil.retrieveReportURL(state, REPORT_ID, paramsAndValues);
		_log.debug("LOG_SYSTEM_OUT","\n\t" + report_url + "\n",100L);
		//Put URL into context
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put("REPORTURL", report_url);
		

		return RET_CONTINUE;
	}

	protected boolean isEmpty(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\t" + "Start of PreviewInvoiceGenerateReportURL" + "\n",100L);
		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
