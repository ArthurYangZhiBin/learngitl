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


package com.ssaglobal.scm.wms.wm_check_pack.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.metadata.DropdownContents;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.ReportPrintUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_check_pack.action.CheckPackSetPrinters.Printer;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackPreRender;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckPackProceed extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String KSHIP_TRANSACTION_SHIP = "Ship";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackProceed.class);
	
	/** The Constant SMALLORLTL_LTL. */
	public static final int SMALLORLTL_LTL = 1;

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		RuntimeFormInterface searchForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface resultsForm = FormUtil.findForm(searchForm, "wm_check_pack_shell", "wm_check_pack_results_form", state);

		if (resultsForm == null) {
			return RET_CANCEL;
		}
		
		//Serial Validation
		validateSerials(state, searchForm);

		//printer
		Printer printerObj = (Printer) (SessionUtil.getInteractionSessionAttribute(CheckPackSetPrinters.PRINTERS, state) == null ? null : SessionUtil.getInteractionSessionAttribute(
				CheckPackSetPrinters.PRINTERS, state));
		String reportPrinter = printerObj.getReportPrinter();
		String labelPrinter = printerObj.getLabelPrinter();
		SessionUtil.setInteractionSessionAttribute(CheckPackSetPrinters.PRINTERS, printerObj, state);

		String transactiontype = KSHIP_TRANSACTION_SHIP;
		String id = searchForm.getFormWidgetByName("CONTAINER").getDisplayValue().toUpperCase();
		String type = resultsForm.getFormWidgetByName("TYPE").getDisplayValue();
		RuntimeFormWidgetInterface scacWidget = resultsForm.getFormWidgetByName("CARRIER");
		String scacCode = (String) scacWidget.getValue();
		RuntimeFormWidgetInterface serviceWidget = resultsForm.getFormWidgetByName("SERVICES");
		String service = (String) serviceWidget.getValue();
		String copies = (String) resultsForm.getFormWidgetByName("NUM_LABELS").getValue();
		String weight = (String) resultsForm.getFormWidgetByName("WEIGHT").getValue();
		String height = (String) resultsForm.getFormWidgetByName("HEIGHT").getValue();
		String length = (String) resultsForm.getFormWidgetByName("LENGTH").getValue();
		String width = (String) resultsForm.getFormWidgetByName("WIDTH").getValue();
		String orderkey = resultsForm.getFormWidgetByName("ORDER").getDisplayValue();
		String source = "CHECKPACK";

		//TODO: VALIDATION
		spsInstallFlagValidation(state);
		//Ensure that a carrier with SCACCODE and SERVICE is defined
		carrierValidation(state, scacWidget, scacCode, serviceWidget, service);

		//TODO: Print REPORT
		String contPackingListRpt = (String) resultsForm.getFormWidgetByName("CONTAINERPACKINGLIST").getValue();
		String masterPackingListRpt = (String) resultsForm.getFormWidgetByName("MASTERPACKINGLIST").getValue();

		if ("1".equals(contPackingListRpt)) {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackProceed_execute", "Printing Container Packing List", SuggestedCategory.NONE);
			printContPackingList(type, id, reportPrinter, state);
		}

		if ("1".equals(masterPackingListRpt)) {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackProceed_execute", "Printing Master Packing List", SuggestedCategory.NONE);
			printMasterPackingList(orderkey, reportPrinter, state);
		}

		/*
		 * INSERT INTO !SCHEMA!.SPROCEDUREMAP
		 * (THEPROCNAME,THEDOMAIN,THEFUNCTIONTYPE
		 * ,COMPOSITE,PARAMETERS,STARTTRANSACTION,ADDWHO,EDITWHO)
		 * VALUES(
		 * 'NSPRFSPS','com.ssaglobal.scm.wms.service.spsintegration','SPSHelper','SPSHelper',
		 * 'printerid,transactiontype,id,idtype,scaccode,servicetype,copies,weight,height,length,width,orderkey,source',
		 * 'TRUE','INSTALL','INSTALL');
		 */
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData(labelPrinter));
		params.add(new TextData(transactiontype));
		params.add(new TextData(id));
		params.add(new TextData(type));
		params.add(new TextData(scacCode));
		params.add(new TextData(service));
		params.add(new TextData(copies));
		params.add(new TextData(weight));
		params.add(new TextData(height));
		params.add(new TextData(length));
		params.add(new TextData(width));
		params.add(new TextData(orderkey));
		params.add(new TextData(source));
		for (int i = 0; i < params.size(); i++) {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackProceed_execute", i + " " + params.get(i), SuggestedCategory.NONE);
		}

		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("NSPRFSPS");
		EXEDataObject spResult = null;
		try {
			spResult = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}
		
		DataValue errorMsg = spResult.getAttribValue(new TextData("ErrorMsg"));
		if(errorMsg != null && !StringUtils.isBlank(errorMsg.getAsString())){
			throw new UserException(errorMsg.getAsString(), new Object[]{});
		}

		String rate = "";
		DataValue rawRate = spResult.getAttribValue(new TextData("ShippingRate"));
		if (rawRate != null)
		{
			rate = rawRate.getAsString();
		}
		SessionUtil.setInteractionSessionAttribute(CheckPackPreRender.CHECK_RATE, rate, state);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private void validateSerials(StateInterface state,
			RuntimeFormInterface searchForm) throws EpiDataException,
			UserException {
		RuntimeFormInterface pickDetailForm = FormUtil.findForm(searchForm, "wm_check_pack_shell", "wm_check_pack_results_pickdetail_form", state);
		DataBean pickFocus = pickDetailForm.getFocus();
		if(pickFocus instanceof BioCollectionBean) {
			BioCollectionBean pickDetailListFocus = ((BioCollectionBean) pickFocus);
			for (int i = 0; i < pickDetailListFocus.size(); i++) {
				BioBean pickDetailFocus = pickDetailListFocus.get("" + i);
				String sku = (String) pickDetailFocus.getValue("SKU");
				String owner = (String) pickDetailFocus.getValue("STORERKEY");
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + owner + "' and sku.SKU = '" + sku + "'", null));
				BioBean skuBioBean = rs.get("" + 0);
				String ocdflag = skuBioBean.getString("OCDFLAG");
				String sNumEndToEnd = skuBioBean.getString("SNUM_ENDTOEND");
				if ("1".equals(sNumEndToEnd) || "1".equals(ocdflag)) {
					// check to make sure all the serial numbers are entered
					String orderKey = (String) pickDetailFocus
							.getValue("ORDERKEY");
					String orderLine = (String) pickDetailFocus
							.getValue("ORDERLINENUMBER");
					String pickDetailKey = (String) pickDetailFocus
							.getValue("PICKDETAILKEY");
					// for each pick, see if there are serial number records
					String query = "(lotxiddetail.IOFLAG = 'O' and lotxiddetail.SOURCEKEY = '"
							+ orderKey
							+ "' and lotxiddetail.SOURCELINENUMBER = '"
							+ orderLine
							+ "' and lotxiddetail.IOFLAG = 'O'"
							+ " and lotxiddetail.PICKDETAILKEY = '"
							+ pickDetailKey
							+ "' and NOT( lotxiddetail.SERIALNUMBERLONG IS NULL OR lotxiddetail.SERIALNUMBERLONG = '"
							+ QueryHelper.escape("") + "' ) )";
					_log.debug("CheckPackGetSerials_execute", "Query " + query,
							SuggestedCategory.APP_EXTENSION);
					rs = tuow.getBioCollectionBean(new Query("lotxiddetail",
							query, null));

					Double serialQty = BioAttributeUtil.getDouble(
							pickDetailFocus, "QTY");
					_log.debug("CheckPackProceed_execute", "Expected "
							+ serialQty + " got " + rs.size(),
							SuggestedCategory.APP_EXTENSION);
					if (serialQty.intValue() != rs.size()) {
						throw new UserException("WMEXP_SN_REQ_ITEM",
								new Object[] { sku });
					}
				}

			}
		}
	}

	/*
	 * public static void PrintReportCmd(BioCollectionBean rptsBioCollection,
	 * StateInterface state, HashMap paramsAndValues, String serverType, String
	 * printer)
	 */

	private void printMasterPackingList(String orderkey, String reportPrinter, StateInterface state) throws EpiDataException {
		/*
		 * master packing list => packing list, CRPT46
		 * p_OrdStart ORDER MIN
		 * p_OrdEnd ORDER MAX
		 * p_pOrderDateFrom 1/1/2000
		 * p_pOrderDateTo +Week
		 * p_ExtOrdStart 0
		 * p_ExtOrderEnd ZZZZZZZZZZ
		 */
		//parameters
		HashMap paramsAndValues = new HashMap();
		if (CheckPackUtil.CHECK_PACK_MULTIPLE.equals(orderkey)) {
			RuntimeFormInterface pickDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_check_pack_shell", "wm_check_pack_results_pickdetail_form", state);
			DataBean focus = pickDetailForm.getFocus();

			if (focus instanceof BioCollectionBean) {
				Object[] orders = ((BioCollectionBean) focus).getAttributes(new String[] { "ORDERKEY" });
				Set<String> orderTrack = new TreeSet<String>();
				for (int i = 0; i < orders.length; i++) {
					Object[] tmp = (Object[]) orders[i];
					orderkey = (String) tmp[0];
					orderTrack.add(orderkey);
				}

				//get range
				paramsAndValues.put("p_OrdStart", ((TreeSet) orderTrack).first());
				paramsAndValues.put("p_OrdEnd", ((TreeSet) orderTrack).last());
			} else {
				paramsAndValues.put("p_OrdStart", orderkey);
				paramsAndValues.put("p_OrdEnd", orderkey);
			}

		} else {
			paramsAndValues.put("p_OrdStart", orderkey);
			paramsAndValues.put("p_OrdEnd", orderkey);
		}

		DateFormat dateFormat = ReportUtil.retrieveDateFormat(state);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, 7);
		Date endDate = now.getTime();
		String pStartDate = "";
		String pEndDate = "";

		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			pStartDate = "01/01/1900";
			pEndDate = dateFormat.format(endDate);
		} else {
			pStartDate = "1900-01-01" + "%2000%3a00%3a00.000";
			pEndDate = dateFormat.format(endDate) + "%2000%3a00%3a00.000";
		}

		paramsAndValues.put("p_pOrderDateFrom", pStartDate);
		paramsAndValues.put("p_pOrderDateTo", pEndDate);
		paramsAndValues.put("p_ExtOrdStart", "0");
		paramsAndValues.put("p_ExtOrderEnd", "ZZZZZZZZZZ");

		printReport("CRPT46", paramsAndValues, reportPrinter, state);

	}

	private void printContPackingList(String type, String id, String reportPrinter, StateInterface state) throws EpiDataException {
		/*
		 * container packing list => dropid caseid report, CRPT78
		 * p_TYPE (DROP/CASE)
		 * p_ID
		 */
		HashMap paramsAndValues = new HashMap();
		paramsAndValues.put("p_Type", type);
		paramsAndValues.put("p_ID", id);
		printReport("CRPT78", paramsAndValues, reportPrinter, state);
	}

	private void printReport(String rptID, HashMap paramsAndValues, String reportPrinter, StateInterface state) throws EpiDataException {
		Query rptQuery = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.RPT_ID = '" + rptID + "'", null);
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rptsBioCollection = tuow.getBioCollectionBean(rptQuery);
		ReportPrintUtil.PrintReportCmd(rptsBioCollection, state, paramsAndValues, ReportUtil.getReportServerType(state), reportPrinter);

	}

	private void spsInstallFlagValidation(StateInterface state) throws UserException {
		//Ensure SPS_INSTALLED
		NSQLConfigUtil spsInstalled = new NSQLConfigUtil(state, "SPS_INSTALLED");
		if (spsInstalled.isOff() == true) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "SPS_INSTALLED is not on", SuggestedCategory.NONE);
			throw new UserException("WMEXP_SPS_NOT_INSTALLED", new Object[] {});

		}
	}

	private void carrierValidation(StateInterface state, RuntimeFormWidgetInterface scacWidget, String scacCode, RuntimeFormWidgetInterface serviceWidget, String service) throws EpiDataException,
			UserException, EpiException {
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		//Need to query the spscarrier table first to see if this is a ltl carrier
		BioCollectionBean carriers = tuow.getBioCollectionBean(new Query("wm_spscarrier", "wm_spscarrier.SCAC_CODE = '" + scacCode + "'", null));
		if(carriers.size() > 0) {
			for(int i = 0; i < carriers.size(); i++) {
				BioBean spsCarrier = carriers.get("" + i);
				int smallOrLTL = BioAttributeUtil.getInt(spsCarrier, "SMALLORLTL");
				if(smallOrLTL == SMALLORLTL_LTL) {
					throw new UserException("WMEXP_SPS_CP_NO_LTL", new Object[]{});
				}
			}
		}
		
		
		
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_spscarrier", "wm_spscarrier.SCAC_CODE = '" + scacCode + "' and wm_spscarrier.SPSTYPE = '" + service + "'", null));
		if (rs.size() == 0) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "No carrier with " + scacCode + " and " + service + " defined", SuggestedCategory.NONE);
			throw new UserException("WMEXP_SPS_NO_CARRIER",
					new Object[] { getDropdownLabel(scacWidget), getDependentDropdownLabel(serviceWidget, scacCode) });
		}
		String carrier = null;
		int smallOrLtl = 0;
		for (int i = 0; i < rs.size(); i++) {
			smallOrLtl = BioAttributeUtil.getInt(rs.get("" + i), "SMALLORLTL");
			carrier = BioAttributeUtil.getString(rs.get("" + i), "CARRIER");
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackProceed_execute", "Carrier " + carrier, SuggestedCategory.NONE);
		}
		if(smallOrLtl == SMALLORLTL_LTL) {
			throw new UserException("WMEXP_SPS_CP_NO_LTL", new Object[]{});
		}
		rs = tuow.getBioCollectionBean(new Query("wm_storer", "wm_storer.STORERKEY = '" + carrier + "' and wm_storer.KSHIP_CARRIER = '1'", null));
		if (rs.size() == 0) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "Carrier " + carrier + " is not a KSHIP_CARRIER", SuggestedCategory.NONE);
			throw new UserException("WMEXP_SPS_NOT_KSHIP_CARRIER", new Object[] { carrier });
		}
	}

	private Object getDependentDropdownLabel(RuntimeFormWidgetInterface widget, String depVal) throws EpiException {
		String label = (String) widget.getValue();
		String value = (String) widget.getValue();
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		list.add(depVal);
		List[] dropdownContentsLabelsAndValues = widget.getDropdownContentsLabelsAndValues(list);
		List values = dropdownContentsLabelsAndValues[0];
		List labels = dropdownContentsLabelsAndValues[1];
		for (int i = 0; i < values.size(); i++) {
			if (value.equals(values.get(i))) {
				label = (String) labels.get(i);
				break;
			}
		}
		return label;
	}

	private Object getDropdownLabel(RuntimeFormWidgetInterface widget) throws EpiException {
		String label = (String) widget.getValue();

		DropdownContents widgetContents = widget.getDropdownContents();
		DropdownContentsContext widgetContext = widget.getDropdownContext();
		label = widgetContents.getLabel(widgetContext, label);

		return label;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is
	 * fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
