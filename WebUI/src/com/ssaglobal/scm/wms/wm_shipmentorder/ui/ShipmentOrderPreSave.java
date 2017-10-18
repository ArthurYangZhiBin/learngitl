/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

/*******************************************************************
* Modification History
* 09/26/2007 	HC	bugAware:7372
* 					The validation on open Qty should be done after 
* 					converting the qty in eaches.
* 10/10/2007	HC	bugAware:7390
* 					The condition was added to check if the open quantity
* 					was modified before doing the UOM conversion.
* 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
*						Changed code to allow qty values for the Currency Locale
*						something other than dollar.
* 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
* 		     		   UOM conversion is now done in the front end.
*                      Changes were made accordingly.

*******************************************************************/

//Import 3rd party packages and classes
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_pickdetail.ui.CWCDValidationUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ShipmentOrderPreSave.
 */
public class ShipmentOrderPreSave extends ActionExtensionBase{
	

	/** The Constant WMEXP_ORDER_SHORT_SHIP_REASON_LIST. */
	private static final String WMEXP_ORDER_SHORT_SHIP_REASON_LIST = "WMEXP_ORDER_SHORT_SHIP_REASON_LIST";

	/** The Constant WMEXP_ORDER_SHORT_SHIP_REASON. */
	private static final String WMEXP_ORDER_SHORT_SHIP_REASON = "WMEXP_ORDER_SHORT_SHIP_REASON";

	/** The Constant SO_LIST_ERROR_MSG. */
	public static final String SO_LIST_ERROR_MSG = "SO_LIST_ERROR_MSG";

	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderPreSave.class);

	//Static form reference variables
	/** The Constant SLOT_1. */
	private static final String SLOT_1 = "list_slot_1";
	
	/** The Constant SLOT_2. */
	private static final String SLOT_2 = "list_slot_2";
	
	/** The Constant BLANK. */
	private static final String BLANK = "Blank";
	
	/** The Constant TAB_GROUP_SLOT. */
	private static final String TAB_GROUP_SLOT = "tbgrp_slot";
	
	/** The Constant TAB_0. */
	private static final String TAB_0 = "tab 0";
	
	/** The Constant TAB_1. */
	private static final String TAB_1 = "tab 1";
	
	/** The Constant PICKDETAIL_TAB. */
	private static final String PICKDETAIL_TAB = "tab 8";
	
	/** The Constant TOGGLE_SLOT. */
	private static final String TOGGLE_SLOT = "wm_shipmentorderdetail_toggle_view";
	
	/** The Constant PD_TOGGLE_SLOT. */
	private static final String PD_TOGGLE_SLOT = "wm_shipmentorder_pickdetail_toggle_view";
	
	/** The Constant DETAIL_LIST_SLOT. */
	private static final String DETAIL_LIST_SLOT = "LIST_SLOT";
	
	/** The Constant LIST_TAB. */
	private static final String LIST_TAB = "wm_shipmentorderdetail_list_view";
	
	/** The Constant DETAIL_TAB. */
	private static final String DETAIL_TAB = "wm_shipmentorderdetail_detail_view";
	
	/** The Constant WM_SHIPMENTORDER_DETAIL1_VIEW. */
	private static final String WM_SHIPMENTORDER_DETAIL1_VIEW = "wm_shipmentorder_detail1_view";
	
	/** The Constant WM_LIST_SHELL_SHIPMENTORDER. */
	private static final String WM_LIST_SHELL_SHIPMENTORDER = "wm_list_shell_shipmentorder";
	
	/** The Constant WM_PICKDETAIL_DETAIL_VIEW. */
	private static final String WM_PICKDETAIL_DETAIL_VIEW = "wm_pickdetail_detail_view";
	
	/** The Constant PICK_STATUS_PICKED. */
	private static final String PICK_STATUS_PICKED = "5";
	
	/** The Constant WMS_LIST_SHELL. */
	private static final String WMS_LIST_SHELL = "wms_list_shell";
	
	//Static attribute names
	/** The Constant KEY. */
	private static final String KEY = "ORDERKEY";
	
	/** The Constant LINE_NO. */
	private static final String LINE_NO = "ORDERLINENUMBER";
	
	/** The Constant PDKEY. */
	private static final String PDKEY = "PICKDETAILKEY";
	
	/** The Constant UOM. */
	private static final String UOM = "UOM";
	
	/** The Constant OPEN_QTY. */
	private static final String OPEN_QTY = "OPENQTY";
	
	/** The Constant ALLOC_QTY. */
	private static final String ALLOC_QTY = "QTYALLOCATED";
	
	/** The Constant SHIPPED_QTY. */
	private static final String SHIPPED_QTY = "SHIPPEDQTY";
	
	/** The Constant PACK. */
	private static final String PACK = "PACKKEY";
	
	/** The Constant STATUS. */
	private static final String STATUS = "STATUS";
	
	/** The Constant MIN_SHIP_PERCENT. */
	private static final String MIN_SHIP_PERCENT = "MINSHIPPERCENT";
	
	/** The Constant SHELF_LIFE. */
	private static final String SHELF_LIFE = "SHELFLIFE";
	
	/** The Constant QTY. */
	private static final String QTY = "QTY";
	
	/** The Constant CASEID. */
	private static final String CASEID = "CASEID";
	
	/** The Constant CARTONID. */
	private static final String CARTONID = "CARTONID";
	
	/** The Constant DROPID. */
	private static final String DROPID = "DROPID";
	
	/** The Constant TABLE. */
	private static final String TABLE = "wm_orders";
	
	/** The Constant ACTION_TYPE_DELETE_DETAIL_LINE. */
	private static final String ACTION_TYPE_DELETE_DETAIL_LINE = "DELETE_DETAIL_LINE";
	
	/** The Constant TRANS_MODE. */
	private static final String TRANS_MODE = "TRANSPORTATIONMODE"; //AW 07/06/2009	Machine: 1928911 SDIS: SCM-00000-04388
	
	/** The Constant HUNDREDWEIGHT. */
	private static final String HUNDREDWEIGHT = "HUNDREDWEIGHT";
	
	/** The Constant CUBICMETER. */
	private static final String CUBICMETER = "CUBICMETER";
	
	//Static exception names
	/** The Constant ERROR_MESSAGE_PERCENT. */
	private static final String ERROR_MESSAGE_PERCENT = "WMEXP_SO_PERCENT_VALIDATION";
	
	/** The Constant ERROR_MESSAGE_UNDER_ZERO. */
	private static final String ERROR_MESSAGE_UNDER_ZERO = "WMEXP_NOT_GREATER_THAN_ZERO";
	
	/** The Constant ERROR_MESSAGE_NON_NEGATIVE. */
	private static final String ERROR_MESSAGE_NON_NEGATIVE = "WMEXP_NONNEG";
	
	/** The Constant ERROR_MESSAGE_LIST_NON_NEGATIVE. */
	private static final String ERROR_MESSAGE_LIST_NON_NEGATIVE = "WMEXP_NONNEGATIVE_VALIDATION";
	
	/** The Constant ERROR_MESSAGE_EXISTS. */
	private static final String ERROR_MESSAGE_EXISTS = "WMEXP_RP_EXISTS";
	
	/** The Constant ERROR_MESSAGE_ALLOC_QTY_EXISTS. */
	private static final String ERROR_MESSAGE_ALLOC_QTY_EXISTS = "WMEXP_ALLOC_QTY_EXISTS";
	
	/** The Constant ERROR_MESSAGE_INVALID_STATUS. */
	private static final String ERROR_MESSAGE_INVALID_STATUS = "WMEXP_RFDEFAULTPACK_VALIDATION";
	
	
	/** The nf. */
	NumberFormat nf;
	
	/** The state. */
	StateInterface state;
	
	/** The key. */
	String key;
	
	/** The parameter. */
	String[] parameter = new String[1];
	
	/**
	 * Instantiates a new shipment order pre save.
	 */
	public ShipmentOrderPreSave() {
	}
	
	/**
	 * Instantiates a new shipment order pre save.
	 *
	 * @param state the state
	 */
	public ShipmentOrderPreSave(StateInterface state) {
		super();		
		this.state = state;	
	}
	
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 07/06/2009	AW	Machine: 1928911 SDIS: SCM-00000-04388
	 * Applying patch originally applied to 902 Warranty.
	 * On the shipment order screen in the Loading Tab, the
	 * transportation mode needs to be entered only if the
	 * TAB is used which is not desirable.
	 * Changes tagged as: //AW 07/06/2009	Machine: 1928911 SDIS: SCM-00000-04388
	 *
	 * @param context the context
	 * @param result the result
	 * @return the int
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 * @throws EpiException the epi exception
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException, EpiException{				
		nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
		
		state = context.getState();
		
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SLOT_1), null);
		DataBean headerFocus = header.getFocus(), pickDetailFormFocus = null;
		// Bugaware 8569. Shipment Order usability - fields not editable from list view
		if (headerFocus instanceof BioCollectionBean) {
			boolean error = false;
			ArrayList<String> suspendedOrders = new ArrayList<String>();
			// saving from initial list view
			// Prevent saving if the order is suspended
			for (int i = 0; i < ((BioCollectionBean) headerFocus).size(); i++) {
				BioBean order = ((BioCollectionBean) headerFocus).get("" + i);
				if (order.getUpdatedAttributes().size() > 0) {
					// something has been updated
					String dbSuspendedIndicatorValue = order.getValue("SuspendedIndicator") == null ? "0" : order.getValue("SuspendedIndicator").toString();
					if ("1".equals(dbSuspendedIndicatorValue)) {
						error = true;
						suspendedOrders.add((String) order.getValue("ORDERKEY"));
						order.setValue("PRIORITY", 3);
					}
				}
			}
			if (error == true) {
				context.setNavigation("errorClickEvent1749");
				SessionUtil.setInteractionSessionAttribute(SO_LIST_ERROR_MSG, suspendedOrders, state);
				state.getDefaultUnitOfWork().clearState();
				return RET_CANCEL_EXTENSIONS;
			}
			return RET_CONTINUE;
		}
		RuntimeFormInterface detail = null;
		SlotInterface headerTabSlot = header.getSubSlot(TAB_GROUP_SLOT);
		RuntimeFormWidgetInterface order = state.getRuntimeForm(headerTabSlot, TAB_0).getFormWidgetByName(KEY);
		key = order.getDisplayValue();
		//Verify unique primary key
		if(headerFocus.isTempBio()){
			//Order header is new record
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			//Set order key to uppercase (Rationalization)
			key = key.toUpperCase();
			
			//Compile and run query for duplicate orderkey
			String queryString = TABLE+"."+KEY+"='"+key+"'";
			Query qry = new Query(TABLE, queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			if(list.size()>0){
				//Duplicate key found, throw exception
				parameter[0] = colonStrip(readLabel(order))+": "+key;
				throw new FormException(ERROR_MESSAGE_EXISTS, parameter);
			}
			
			String route = BioAttributeUtil.getString(headerFocus, "ROUTE");
			if(!StringUtils.isEmpty(route)) {
				headerFocus.setValue("ROUTE", route.trim());
			}
			String door = BioAttributeUtil.getString(headerFocus, "DOOR");
			if(!StringUtils.isEmpty(door)) {
				headerFocus.setValue("DOOR", door.trim());
			}
			
			
			headerFocus.setValue("ORDERSID", GUIDFactory.getGUIDStatic());
			//Detail in list slot 2
			detail = state.getRuntimeForm(shell.getSubSlot(SLOT_2), null);
			if(detail.getFocus().isTempBio())
				detail.getFocus().setValue("ORDERDETAILID", GUIDFactory.getGUIDStatic());
	
			//jp.answerlink.150316.begin
			//Per BAs request, removed Parcel as default of TransportationMode, now defaults to NULL
			//checkAndSetTransMode(headerFocus); //AW 07/06/2009	Machine: 1928911 SDIS: SCM-00000-04388
			//jp.answerlink.150316.end
//mark ma added to fix bug # 8247
			SlotInterface tabgrp = state.getRuntimeForm(shell.getSubSlot(SLOT_2), null).getSubSlot(TAB_GROUP_SLOT);
			RuntimeFormInterface detailGeneral = state.getRuntimeForm(tabgrp, TAB_0);
			RuntimeFormInterface detail2 = state.getRuntimeForm(tabgrp, TAB_1);
			if(tabgrp != null && detailGeneral != null && detail2 != null)
					validateOrderDetailFormValues(headerFocus, detailGeneral, detail2, state.getDefaultUnitOfWork()); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
//end ****************************		
		
		
		
		}else{
			//Changes for Suspended Order Processing
			//Stop Save if the Order has the SuspendedIndicator flag set to 1 in the DB
			//Allow Save if the current SuspendedIndicator flag set to 0
			String dbSuspendedIndicatorValue = headerFocus.getValue("SuspendedIndicator", true) == null ? "0" : headerFocus.getValue("SuspendedIndicator", true).toString();
			String currentSuspendedIndicatorValue = headerFocus.getValue("SuspendedIndicator") == null ? "0" : headerFocus.getValue("SuspendedIndicator").toString();
			if (dbSuspendedIndicatorValue.equals("1") && currentSuspendedIndicatorValue.equals("1"))
			{
				NSQLConfigUtil allowPickingSuspendedOrders = new NSQLConfigUtil(state, "ALLOWPICKINGSUSPENDEDORDERS");
				if (allowPickingSuspendedOrders.isOn()) {
					
					// if anything else has been updated, throw error
					if (hasAnythingBeenUpdatedBesidesPicks(headerFocus) == true) {
						_log.debug(	"LOG_DEBUG_EXTENSION_ShipmentOrderPreSave_execute",
									"Save stopped because something other than Pick has been modified when the allowPickingSuspendedOrders is on",
									SuggestedCategory.NONE);
						throw new UserException("WMEXP_SO_SUSPEND", new Object[] {});
					}
					// verify that only PICK status has changed
					if (pickDetailStatusSetToPick(headerFocus) == true) {
						// don't throw error - allow save
					} else {
						_log.debug(	"LOG_DEBUG_EXTENSION_ShipmentOrderPreSave_execute",
									"Save stopped because Picks haven't been modified to Picked",
									SuggestedCategory.NONE);
						throw new UserException("WMEXP_SO_SUSPEND", new Object[] {});
					}
				}
				else {
					// else
					_log.debug(	"LOG_DEBUG_EXTENSION_ShipmentOrderPreSave_execute",
								"Save stopped because SuspendedIndicator flag set to 1 in the DB",
								SuggestedCategory.NONE);
					//
					throw new UserException("WMEXP_SO_SUSPEND", new Object[] {});
				}
			}
			
			String route = BioAttributeUtil.getString(headerFocus, "ROUTE");
			if(((BioBean)headerFocus).hasBeenUpdated("ROUTE") && !StringUtils.isEmpty(route)) {
				headerFocus.setValue("ROUTE", route.trim());
			}
			String door = BioAttributeUtil.getString(headerFocus, "DOOR");
			if(((BioBean)headerFocus).hasBeenUpdated("DOOR") && !StringUtils.isEmpty(door)) {
				headerFocus.setValue("DOOR", door.trim());
			}
			
			
			
			//Check Pick detail tab
			RuntimeFormInterface pickDetail = state.getRuntimeForm(headerTabSlot, PICKDETAIL_TAB); 
			SlotInterface pdToggleSlot = pickDetail.getSubSlot(PD_TOGGLE_SLOT);
			int pdFormNumber = state.getSelectedFormNumber(pdToggleSlot);
			if(pdFormNumber==1){
				//Form view present for Pick Details of Shipment Order 
				//Pick Detail Detail Validation
				//Get Detail Tab Focus
				SlotInterface pdSlot = state.getRuntimeForm(pdToggleSlot, DETAIL_TAB).getSubSlot(TAB_GROUP_SLOT);
				RuntimeFormInterface pdDetail = state.getRuntimeForm(pdSlot, TAB_0);
				pickDetailFormFocus = pdDetail.getFocus();
				pickDetailValidation(pickDetailFormFocus, pdDetail, state.getRuntimeForm(pdSlot, TAB_1));
			}else{
				//Pick Detail List Validation
				RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(pdToggleSlot, LIST_TAB);
				if(!isNull(listForm)){
					String[] nonNegListMessage = new String[1];
					nonNegListMessage[0] = "";
					BioCollectionBean listFormCollection = (BioCollectionBean) listForm.getFocus();
					try {
						for(int i = 0; i < listFormCollection.size(); i++){
							BioBean pickDetailBean = (BioBean) listFormCollection.elementAt(i);
							if(pickDetailBean.hasBeenUpdated(STATUS) || pickDetailBean.hasBeenUpdated(QTY) || pickDetailBean.hasBeenUpdated(DROPID)){
								int status = readStatus(pickDetailBean, listForm);
								String lot = isNull(pickDetailBean.get("LOT")) ? "" : pickDetailBean.get("LOT").toString();
								String loc = isNull(pickDetailBean.get("LOC")) ? "" : pickDetailBean.get("LOC").toString();
								String id = isNull(pickDetailBean.get("ID")) ? "" : pickDetailBean.get("ID").toString();
								if(status < 5){
									statusCombinationValidation(pickDetailBean, lot, loc, id);
								}							
								if(pickDetailBean.hasBeenUpdated(QTY)){
									String qtyValue = pickDetailBean.get(QTY).toString();
									String caseIDValue = pickDetailBean.get(CASEID).toString();
									if(qtyValue.startsWith("-")){
										RuntimeFormWidgetInterface qty = listForm.getFormWidgetByName(QTY);
										RuntimeFormWidgetInterface caseID = listForm.getFormWidgetByName(CASEID);
										if(nonNegListMessage[0].equalsIgnoreCase("")){		
											nonNegListMessage[0]+="("+colonStrip(readLabel(caseID))+", "+colonStrip(readLabel(qty))+":  "+caseIDValue+", "+qtyValue;
										}else{
											nonNegListMessage[0]+="; "+caseIDValue+", "+qtyValue;
										}
									}
								}
								if(pickDetailBean.hasBeenUpdated(DROPID)){
									String dropIdVal = pickDetailBean.get(DROPID)==null ? "" : pickDetailBean.get(DROPID).toString().toUpperCase();
									pickDetailBean.set(DROPID, dropIdVal);
								}

								//Quantity Validation
								quantityValidation(listForm, false, lot, loc, id);
								
								//Open Qty Validation
								openQtyValidation(pickDetailBean, false, listForm);
								
								//CWCD List Validation
								CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.SO);
								cwcd.cwcdListValidation(pickDetailBean, status);
							}			
						}
					} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
						// RM Defect269239
						e.printStackTrace();
						_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
						_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
					}
					if(!nonNegListMessage[0].equalsIgnoreCase("")){
						nonNegListMessage[0]+=")";
						throw new FormException(ERROR_MESSAGE_LIST_NON_NEGATIVE, nonNegListMessage);
					}
					try {
						for(int index=0; index<listFormCollection.size(); index++){
							//Get Status for dirty list rows 
							BioBean bean = (BioBean) listFormCollection.elementAt(index);
							if(bean.hasBeenUpdated(STATUS)){
								int status = Integer.parseInt(bean.get(STATUS).toString());
								if((5 <= status) && (status < 9)){
									String query = "SELECT CONFIGKEY, NSQLVALUE FROM NSQLCONFIG WHERE CONFIGKEY = 'DOMOREWHENPICKED' AND NSQLVALUE = '0'";
									EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
									//Continue with validation if NSQLCONFIG (DOMOREWHENPICKED) not present or (present and enabled)
									if((results.getRowCount() == 0)){
										Object toLoc = bean.get("TOLOC");
										Object loc = bean.get("LOC");
										//ToLocation is empty validation
										if (isEmpty(toLoc)){
											//Validate current Location is of type PICKTO
											String locValue = isEmpty(loc) ?  "" : loc.toString();
											toLocValidation(bean, locValue);	
										}else{
											//Set ToLocation to Location and set ToLocation to empty
											bean.set("LOC", toLoc.toString());
											bean.set("TOLOC", "");
										}
									}
								}
							}	
						}
					} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
						// RM Defect269239
						e.printStackTrace();
						_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
						_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
					}
				}
			}
			
			//Detail in toggle form in list slot 2
			SlotInterface toggleSlot = state.getRuntimeForm(shell.getSubSlot(SLOT_2), null).getSubSlot(TOGGLE_SLOT);
			int formNumber = state.getSelectedFormNumber(toggleSlot);
			//Validate detail list form updates 
			if(formNumber==0){
				detail = state.getRuntimeForm(state.getRuntimeForm(toggleSlot, LIST_TAB).getSubSlot(DETAIL_LIST_SLOT), null);
				validateOrderDetailListValues(headerFocus, detail, state.getDefaultUnitOfWork());//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			}else{
				detail = state.getRuntimeForm(state.getRuntimeForm(toggleSlot, DETAIL_TAB).getSubSlot(TAB_GROUP_SLOT), TAB_0);
				RuntimeFormInterface detail2 = state.getRuntimeForm(detail.getParentForm(state).getSubSlot(TAB_GROUP_SLOT), TAB_1);
				validateOrderDetailFormValues(headerFocus, detail, detail2, state.getDefaultUnitOfWork());//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			}
		}
		return RET_CONTINUE;
	}

	//AW 07/06/2009	Machine: 1928911 SDIS: SCM-00000-04388
	/**
	 * Check and set trans mode.
	 *
	 * @param headerFocus the header focus
	 */
	private void checkAndSetTransMode(DataBean headerFocus) {
			// TODO Auto-generated method stub
			
			if(headerFocus.isTempBio())
			{
				QBEBioBean qbe = (QBEBioBean)headerFocus;
				if(qbe.get(TRANS_MODE) == null)
					qbe.set(TRANS_MODE, "1");
				
			}
			
		}
	//end AW 08/14/2008	Machine: 1928911 SDIS: SCM-00000-04388

	/**
	 * Pick detail status set to pick.
	 *
	 * @param headerFocus the header focus
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean pickDetailStatusSetToPick(DataBean headerFocus) throws EpiDataException {
		// first check for New PICKDETAIL
		// if that status is not pick, that's an error
		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("tab 8");
		tabs.add("tab 0");
		tabs.add(DETAIL_TAB);
		RuntimeFormInterface pickDetailDetailView = FormUtil.findForm(	state.getCurrentRuntimeForm(),
																		WMS_LIST_SHELL,
																		WM_PICKDETAIL_DETAIL_VIEW,
																		tabs,
																		state);
		if (pickDetailDetailView != null && !pickDetailDetailView.isListForm()) {
			DataBean pickDetail = pickDetailDetailView.getFocus();
			String status = pickDetail.getValue("STATUS") == null ? "0" : pickDetail.getValue("STATUS").toString();
			if (!PICK_STATUS_PICKED.equals(status)) {
				return false;
			}
			
		}
		// then examine pickdetail records
		// ensure the status is picked
		// if the pickdetail has been modified and status is not part of the attributes throw error
		BioCollectionBean pickDetails = headerFocus.getValue("PICKDETAIL") == null ? null : ((BioCollectionBean) headerFocus.getValue("PICKDETAIL"));
		if (pickDetails != null && pickDetails.size() != 0) {
			for (int i = 0; i < pickDetails.size(); i++) {
				BioBean pickDetail = pickDetails.get("" + i);
				List updatedAttributes = pickDetail.getUpdatedAttributes();
				if (updatedAttributes.contains("STATUS")) {
					String status = pickDetail.getString("STATUS") == null ? "0" : pickDetail.getString("STATUS").toString();
					if (!PICK_STATUS_PICKED.equals(status)) {
						return false;
					}
				}
				else {
					// If a pick is being updated, status has to be modified, else it is an error
					return false;
				}
			}
		}

		

		return true;
	}
	
	/**
	 * Checks for anything been updated besides picks.
	 *
	 * @param headerFocus the header focus
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean hasAnythingBeenUpdatedBesidesPicks(DataBean headerFocus) throws EpiDataException {

		List orderUpdatedAttributes = ((BioBean)headerFocus).getUpdatedAttributes();
		if(orderUpdatedAttributes != null && orderUpdatedAttributes.size() > 0)
		{
			// modified orders
			return true;
		}
		// check PICKDETAIL
		// BioCollectionBean pickDetails = headerFocus.getValue("PICKDETAIL") == null ? null : ((BioCollectionBean)
		// headerFocus.getValue("PICKDETAIL"));
		// if (pickDetails != null) {
		//
		// }
		// check TASKDETAIL
		BioCollectionBean taskDetails = headerFocus.getValue("TASKDETAIL") == null ? null : ((BioCollectionBean) headerFocus.getValue("TASKDETAIL"));
		if (taskDetails != null && taskDetails.size() != 0) {
			for (int i = 0; i < taskDetails.size(); i++) {
				BioBean taskDetail = taskDetails.get("" + i);
				List taskUpdatedAttributes = taskDetail.getUpdatedAttributes();
				if (taskUpdatedAttributes != null && taskUpdatedAttributes.size() > 0) {
					return true;
				}
			}
		}
		// check DEMANDALLOCATION
		BioCollectionBean demandAllocations = headerFocus.getValue("DEMANDALLOCATION") == null ? null : ((BioCollectionBean) headerFocus.getValue("DEMANDALLOCATION"));
		if (demandAllocations != null && demandAllocations.size() != 0) {
			for (int i = 0; i < demandAllocations.size(); i++) {
				BioBean demandAllocation = demandAllocations.get("" + i);
				List demandAllocationUpdatedAttributes = demandAllocation.getUpdatedAttributes();
				if (demandAllocationUpdatedAttributes != null && demandAllocationUpdatedAttributes.size() > 0) {
					return true;
				}
			}
		}
		// check PREALLOCATEPICKDETAIL
		BioCollectionBean preAllocatePickDetails = headerFocus.getValue("PREALLOCATEPICKDETAIL") == null ? null : ((BioCollectionBean) headerFocus.getValue("PREALLOCATEPICKDETAIL"));
		if (preAllocatePickDetails != null && preAllocatePickDetails.size() != 0) {
			for (int i = 0; i < preAllocatePickDetails.size(); i++) {
				BioBean preAllocatePickDetail = preAllocatePickDetails.get("" + i);
				List preAllocatePickDetailAttributes = preAllocatePickDetail.getUpdatedAttributes();
				if (preAllocatePickDetailAttributes != null && preAllocatePickDetailAttributes.size() > 0) {
					return true;
				}
			}
		}
		// Check OrderDetails
		BioCollectionBean orderDetails = headerFocus.getValue("ORDER_DETAIL") == null ? null : ((BioCollectionBean) headerFocus.getValue("ORDER_DETAIL"));
		if (orderDetails != null && orderDetails.size() != 0) {
			for (int i = 0; i < orderDetails.size(); i++) {
				BioBean orderDetail = orderDetails.get("" + i);
				List orderDetailUpdatedAttributes = orderDetail.getUpdatedAttributes();
				if (orderDetailUpdatedAttributes != null && orderDetailUpdatedAttributes.size() > 0) {
					return true;
				}
				
				BioCollectionBean vasDetails = orderDetail.getValue("ORDERDETAILXVASBIO") == null ? null : ((BioCollectionBean) orderDetail.getValue("ORDERDETAILXVASBIO"));
				if (vasDetails != null && vasDetails.size() != 0) {
					for (int j = 0; j < vasDetails.size(); j++) {
						BioBean vasDetail = vasDetails.get("" + j);
						List vasUpdatedAttributes = vasDetail.getUpdatedAttributes();
						if (vasUpdatedAttributes != null && vasUpdatedAttributes.size() > 0) {
							return true;
						}
					}
				}
			}
		}
		
		// Rigorous Checking of the UI now
		// RuntimeFormInterface orderHeaderForm = FormUtil.findForm( state.getCurrentRuntimeForm(),
		// WMS_LIST_SHELL,
		// "wm_shipmentorder_header_view",
		// state);
		//		
		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("tab 8");
		tabs.add("tab 0");
		tabs.add(DETAIL_TAB);
		// Get Order Header
		// look at tabs where you can make a new record
		// PickDetail (tab 8) (tab 0)
		// RuntimeFormInterface pickDetailDetailView = FormUtil.findForm( state.getCurrentRuntimeForm(),
		// "wms_list_shell",
		// "wm_pickdetail_detail_view",
		// tabs,
		// state);
		// if (pickDetailDetailView != null && !pickDetailDetailView.isListForm()) {
		// if (pickDetailDetailView.getFocus() != null && pickDetailDetailView.getFocus().isTempBio()) {
		// return true;
		// }
		// }
		// // - CWCD (tab 2) - Detail Toggle (pickdetail_detail_view)
		// tabs.add("tab 2");
		// tabs.add("pickdetail_detail_view");
		// RuntimeFormInterface cwcdDetailView = FormUtil.findForm(state.getCurrentRuntimeForm(),
		// "wms_list_shell",
		// "wm_pickdetail_catchweight_data_detail_view",
		// tabs,
		// state);
		// if (cwcdDetailView != null && !cwcdDetailView.isListForm()) {
		// if (cwcdDetailView.getFocus() != null && cwcdDetailView.getFocus().isTempBio()) {
		// // return true;
		// }
		// }
		
		// get the focus of forms
		// if QBEBioBean, adding a new record

		// Get Order Detail Form
		tabs.clear();
		tabs.add(SLOT_2);
		tabs.add(TAB_GROUP_SLOT);
		tabs.add(TOGGLE_SLOT);
		tabs.add(DETAIL_TAB);
		
		tabs.add("tab 0");
		RuntimeFormInterface orderDetailForm = FormUtil.findForm(	state.getCurrentRuntimeForm(),
																	WM_LIST_SHELL_SHIPMENTORDER,
																	WM_SHIPMENTORDER_DETAIL1_VIEW,
																	tabs, 
																	state);
		if (orderDetailForm != null && !orderDetailForm.isListForm()) {
			if (orderDetailForm.getFocus() != null && orderDetailForm.getFocus().isTempBio()) {
				return true;
			}
		}
		// VAS Detail (tab 5)
		// - Detail (wm_shipmentorderdetail_detail_view)
		
		tabs.add("tab 5");
		tabs.add(DETAIL_TAB);
		RuntimeFormInterface vasDetail = FormUtil.findForm(	state.getCurrentRuntimeForm(),
															WMS_LIST_SHELL,
															"wm_shipmentorder_vasdetail_detail_view",
															tabs,
															state);
		if (vasDetail != null && !vasDetail.isListForm()) {
			if (vasDetail.getFocus() != null && vasDetail.getFocus().isTempBio()) {
				return true;
			}
		}
		return false;
	}	
	
	//Build error message
	/**
	 * Builds the error.
	 *
	 * @param state the state
	 * @param lot the lot
	 * @param loc the loc
	 * @param id the id
	 * @return the string
	 * @throws EpiDataException the epi data exception
	 */
	private String buildError(StateInterface state, String lot, String loc, String id) throws EpiDataException{
		String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
		if(!isEmpty(lot)){
			base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
		}
		if(!isEmpty(loc)){
			base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
		}
		if(!isEmpty(id)){
			base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
		}
		return base;
	}
	
	//Remove commas from numerical string
	/**
	 * Comma strip.
	 *
	 * @param number the number
	 * @return the string
	 */
	private static String commaStrip(String number){
		NumberFormat nf = NumberFormat.getInstance();
		String numberS;
		try{
			numberS = nf.parse(number).toString();
		} catch(ParseException e){
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
	
	//Remove colons from label values
	/**
	 * Colon strip.
	 *
	 * @param label the label
	 * @return the string
	 */
	private static String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	//Non null verification
	/**
	 * Checks if is null.
	 *
	 * @param attributeValue the attribute value
	 * @return true, if is null
	 * @throws EpiDataException the epi data exception
	 */
	private boolean isNull(Object attributeValue) throws EpiDataException{
		if(attributeValue == null){
			return true;
		}else{
			return false;
		}
	}
	
	//Identifies empty objects
	/**
	 * Checks if is empty.
	 *
	 * @param attributeValue the attribute value
	 * @return true, if is empty
	 * @throws EpiDataException the epi data exception
	 */
	private boolean isEmpty(Object attributeValue) throws EpiDataException{
		if(attributeValue == null){
			return true;
		}else if(attributeValue.toString().matches("\\s*")){
			return true;
		}else{
			return false;
		}
	}

	//Validation for pick detail
	/**
	 * Status combination validation.
	 *
	 * @param pickDetailFormFocus the pick detail form focus
	 * @param lot the lot
	 * @param loc the loc
	 * @param id the id
	 * @throws DPException the dP exception
	 * @throws UserException the user exception
	 */
	private void statusCombinationValidation(DataBean pickDetailFormFocus, String lot, String loc, String id) throws DPException, UserException{
		String query="SELECT * FROM LOTXLOCXID WHERE LOT='"+lot+"' AND LOC='"+loc+"' AND ID='"+id+"'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if(results.getRowCount() == 0){
			//display error
			String[] parameters = new String[1];
			parameters[0] = pickDetailFormFocus.getValue("CASEID").toString();
			throw new UserException("WMEXP_PICK_COMBINATION", parameters);
		}
	}
	
	/**
	 * Pick detail validation.
	 *
	 * @param focus the focus
	 * @param form the form
	 * @param form2 the form2
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	private void pickDetailValidation(DataBean focus, RuntimeFormInterface form, RuntimeFormInterface form2) throws UserException, EpiDataException{
		//Determine focus subclass: QBEBioBean(New) or BioBean(Existing)
		boolean isInsert;
		String dropIdVal = form2.getFormWidgetByName(DROPID).getDisplayValue();
		dropIdVal = dropIdVal==null ? "" : dropIdVal.toUpperCase();
		
		//jp.6963.begin
		String caseIdVal = (String) form2.getFormWidgetByName(CASEID).getValue();
		if(caseIdVal==null){
			KeyGenBioWrapper keyGen = new KeyGenBioWrapper();
//			caseIdVal = keyGen.getKey(CASEID);
			caseIdVal = keyGen.getKey(CARTONID);
		}
		//jp.6963.end

		if(focus.isTempBio()){
			//New record
			_log.debug("LOG_DEBUG_EXTENSION", "!Insert", SuggestedCategory.NONE);
			isInsert = true;
			//set PickHeaderKey
			setPickHeaderKey(focus, form);
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(DROPID, dropIdVal);
			
			//jp.6963.begin
			if(caseIdVal!=null)
				qbe.set(CASEID, caseIdVal);
			//jp.6963.end

		}else{
			//Existing record
			_log.debug("LOG_DEBUG_EXTENSION", "!Update", SuggestedCategory.NONE);
			isInsert = false;
			focus = focus;
			BioBean bio = (BioBean)focus;
			bio.set(DROPID, dropIdVal);
		}
		
		//Status validation
		int status = readStatus(focus, form); //Integer.parseInt(focus.getValue("STATUS").toString());
		String lot = isNull(focus.getValue("LOT")) ? "" : focus.getValue("LOT").toString();
		String loc = isNull(focus.getValue("LOC")) ? "" : focus.getValue("LOC").toString();
		String id = isNull(focus.getValue("ID")) ? "" : focus.getValue("ID").toString();
		if (status < 5){
			statusCombinationValidation(focus, lot, loc, id);
		}
		
		//Quantity Validation
		quantityValidation(form, isInsert, lot, loc, id);
		
		// Open Qty Validation
		openQtyValidation(focus, isInsert, form);

		//ToLoc Validation
		toLocValidation(focus, loc);
		
		//CWCD Detail Validation
		CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.SO);
		cwcd.cwcdDetailValidation(state, focus, status);
	}
	
	//Fill PickHeader Key for Pick Detail inserts
	/**
	 * Sets the pick header key.
	 *
	 * @param pickDetailFormFocus the pick detail form focus
	 * @param pickDetailForm the pick detail form
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws DPException the dP exception
	 */
	private void setPickHeaderKey(DataBean pickDetailFormFocus, RuntimeFormInterface pickDetailForm) throws EpiDataException, UserException, DPException{
		_log.debug("LOG_DEBUG_EXTENSION", " Start of PickHeaderKey Generation", SuggestedCategory.NONE);
		Object tempPickHeaderKey = pickDetailFormFocus.getValue("PICKHEADERKEY");
		if(isEmpty(tempPickHeaderKey) && orderValidation(pickDetailFormFocus, pickDetailForm)){
			String orderNumber = pickDetailFormFocus.getValue("ORDERKEY") == null ? null
					: pickDetailFormFocus.getValue("ORDERKEY").toString().toUpperCase();
			String query = "SELECT ORDERKEY, PICKHEADERKEY FROM PICKDETAIL WHERE ORDERKEY = '"+orderNumber+"'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			String pickHeaderKey;
			if(results.getRowCount() >= 1){
				//Using PickHeaderKey from another record that shares same Order
				_log.debug("LOG_DEBUG_EXTENSION", "Using previous PickHeaderKey", SuggestedCategory.NONE);
				pickHeaderKey = results.getAttribValue(2).getAsString();
			}else{
				//use keygen
				_log.debug("LOG_DEBUG_EXTENSION", "Using Keygen", SuggestedCategory.NONE);
				pickHeaderKey = new KeyGenBioWrapper().getKey("PICKHEADERKEY");
			}
			_log.debug("LOG_DEBUG_EXTENSION", "!Setting PickHeaderKey to " + pickHeaderKey, SuggestedCategory.NONE);
			pickDetailFormFocus.setValue("PICKHEADERKEY", pickHeaderKey);
		}
	}
	
	/**
	 * Order validation.
	 *
	 * @param pickDetailFormFocus the pick detail form focus
	 * @param pickDetailForm the pick detail form
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws DPException the dP exception
	 */
	private boolean orderValidation(DataBean pickDetailFormFocus, RuntimeFormInterface pickDetailForm) throws EpiDataException, UserException, DPException{
		Object tempOrderValue = pickDetailFormFocus.getValue("ORDERKEY");
		if(isEmpty(tempOrderValue)){
			throw new UserException("WMEXP_PICK_ORDER", new Object[] {});
		}else{
			String orderNumber = tempOrderValue == null ? null : tempOrderValue.toString().toUpperCase();
			String query = "SELECT * FROM ORDERDETAIL WHERE ORDERKEY = '" + orderNumber + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "??Query " + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() == 0){
				//display error
				String[] parameters = new String[1];
				parameters[0] = orderNumber;
				throw new UserException("WMEXP_PICK_ORDER", parameters);
			}else{
				//pickDetailForm.getFormWidgetByName("ORDERKEY").setProperty("label image", null);
				_log.debug("LOG_DEBUG_EXTENSION", "Order Validation passed", SuggestedCategory.NONE);
				return true;
			}
		}
	}
	
	/**
	 * Quantity validation.
	 *
	 * @param pickDetailForm the pick detail form
	 * @param isInsert the is insert
	 * @param lot the lot
	 * @param loc the loc
	 * @param id the id
	 * @throws DPException the dP exception
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void quantityValidation(RuntimeFormInterface pickDetailForm, boolean isInsert, String lot, String loc, String id) throws DPException, EpiDataException, UserException{
		//Start of Quantity Validation
		DataBean pickDetailFormFocus = pickDetailForm.getFocus();

		//Retrieve Qty and QtyAllocated
		double availableQty = 0;
		String query = "SELECT QTY, QTYALLOCATED FROM LOTXLOCXID WHERE LOT='"+lot+"' AND LOC='"+loc+"' AND ID='"+id+"'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if(results.getRowCount() == 1){
			double qty = Double.parseDouble(results.getAttribValue(1).getAsString());
			double qtyAllocated = Double.parseDouble(results.getAttribValue(2).getAsString());
			availableQty = qty - qtyAllocated;
		}

		if(isInsert){
			//Insert Quantity Validation
			double quantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if(!(isEmpty(tempQuantity))){
				quantity = Double.parseDouble(tempQuantity.toString());
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if(quantity > availableQty){
				//Construct error message
				String base = buildError(state, lot, loc, id);
				throw new UserException(base, new Object[] {});
			}
			if(quantity<=0){
				String[] params = new String[1];
				params[0] = colonStrip(readLabel(pickDetailForm.getFormWidgetByName("QTY")));
				throw new UserException("WMEXP_NOT_GREATER_THAN_ZERO", params);
			}
		}else{
			//Update Quantity Validation
			double quantity = 0;
			double originalQuantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if(!(isEmpty(tempQuantity))){
				quantity = Double.parseDouble(tempQuantity.toString());
				//Retrieve original value
				Object tempOldQuantity = pickDetailFormFocus.getValue("QTY", true);
				if(!isEmpty(tempOldQuantity)){
					originalQuantity = Double.parseDouble(tempOldQuantity.toString());					
				}
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", " Quantity is blank", SuggestedCategory.NONE);
			}
			if(quantity > availableQty + originalQuantity){
				String base = buildError(state, lot, loc, id);
				throw new UserException(base, new Object[] {});
			}
		}
	}
	
	//Open Qty Validation
	/**
	 * Open qty validation.
	 *
	 * @param focus the focus
	 * @param isInsert the is insert
	 * @param form the form
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void openQtyValidation(DataBean focus, boolean isInsert, RuntimeFormInterface form) throws EpiDataException, UserException{
		//Open Qty Validation
		String orderLineNumber = focus.getValue(LINE_NO).toString();
		//Get Open Qty
		String query = "SELECT "+SHIPPED_QTY+", "+OPEN_QTY+" FROM ORDERDETAIL WHERE "+KEY+"='"+key+"' AND "+LINE_NO+"='"+orderLineNumber+"'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		double shippedQty = 0, openQty = 0;
		if(results.getRowCount() == 1){
			shippedQty = Double.parseDouble(results.getAttribValue(1).getAsString());
			openQty = Double.parseDouble(results.getAttribValue(2).getAsString());
		}

		//Get Qty Entered
		double qty = Double.parseDouble(focus.getValue(QTY).toString());
		if(qty<0){
			parameter[0]=colonStrip(readLabel(form.getFormWidgetByName(QTY)));
			throw new FormException(ERROR_MESSAGE_NON_NEGATIVE, parameter);
		}
		
		//Get Sum All PickDetail Qty From DB
		query = "SELECT SUM(QTY) FROM PICKDETAIL ";
		if(!isInsert){
			query = query+"WHERE "+PDKEY+"<>'"+focus.getValue(PDKEY).toString()+"'";
		}
		query = query+" GROUP BY "+KEY+", "+LINE_NO+" HAVING ("+KEY+"='"+key+"') AND ("+LINE_NO+"='"+orderLineNumber+"')";
		results = WmsWebuiValidationSelectImpl.select(query);
		double sumQty = 0;
		if(results.getRowCount() == 1){
			sumQty = Double.parseDouble(results.getAttribValue(1).getAsString());
		}

		double allQty = qty + sumQty;
		if(allQty > openQty + shippedQty){
			//raise error
			throw new UserException("WMEXP_PICK_OPENQTY", parameter);
		}
	}
	
	//To Loc Validation
	/**
	 * To loc validation.
	 *
	 * @param focus the focus
	 * @param loc the loc
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void toLocValidation(DataBean focus, String loc) throws EpiDataException, UserException{
		//02/08/2010 FW:  Added code to populate toloc, CARTONGROUP and CARTONTYPE fields if pickdetail is added manually (Incident3209733_Defect215086) -- Start
		//ToLoc Validation
		/*
		if(!(isEmpty(focus.getValue("TOLOC")) || isEmpty(focus.getValue("LOC")))){
			String toLoc = focus.getValue("TOLOC").toString();
			//Query Loc Table, get Location Type
			//Compare with toLoc, should be PickTo or Staged
			//If they do not match throw exception

			String query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE=PUTAWAYZONE.PUTAWAYZONE WHERE LOC.LOC='"+loc+"'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() == 1){
				String locationType = results.getAttribValue(2).getAsString();
				if(!(locationType.equalsIgnoreCase(toLoc))){
					parameter[0] = toLoc;
					throw new UserException("WMEXP_PICK_TOLOC", parameter);
				}
			}
		}
		*/
		String query = null;
		EXEDataObject results = null;
		if(!(isEmpty(focus.getValue("LOC")))){	
			query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE=PUTAWAYZONE.PUTAWAYZONE WHERE LOC.LOC='"+loc+"'";
			results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() == 1){
				String pickToLoc = results.getAttribValue(2).getAsString();
				if(!(isEmpty(focus.getValue("TOLOC")))){
					//SRG: Lava Issue: No need to validate PUTAWAYZONE.PICKTOLOC with PICKDETAIL.TOLOC
					/*String toLoc = focus.getValue("TOLOC").toString();
					if(!(pickToLoc.equalsIgnoreCase(toLoc))){
						parameter[0] = toLoc;
						throw new UserException("WMEXP_PICK_TOLOC", parameter);
					}*/
					//SRG: End: Lava Issue
				}else {	
					//populate toloc if pickdetail is added manually
					focus.setValue("TOLOC", pickToLoc);
				}
			}
		}
		
		//populate CARTONGROUP and CARTONTYPE fields if pickdetail is added manually
		if(isEmpty(focus.getValue("CARTONGROUP")) || isEmpty(focus.getValue("CARTONTYPE"))){
			//get CARTONGROUP and CARTONTYPE fields
			results = null;
			String sku = focus.getValue("SKU").toString();
			String storerkey = focus.getValue("STORERKEY").toString();
			query = "SELECT MAX(SKU.CARTONGROUP) CARTONGROUP, MAX(CARTONIZATION.CARTONTYPE) CARTONTYPE FROM SKU INNER JOIN CARTONIZATION ON SKU.CARTONGROUP = CARTONIZATION.CARTONIZATIONGROUP WHERE SKU.SKU='"+sku+"' AND SKU.STORERKEY='"+storerkey+"'";
			results = WmsWebuiValidationSelectImpl.select(query);
		
			if(results.getRowCount() == 1){
				if(isEmpty(focus.getValue("CARTONGROUP"))){
					focus.setValue("CARTONGROUP", results.getAttribValue(1).getAsString());
				}
				if(isEmpty(focus.getValue("CARTONTYPE"))){
					focus.setValue("CARTONTYPE", results.getAttribValue(2).getAsString());
				}
			}
		}
		results = null;
		//02/08/2010 FW:  Added code to populate toloc, CARTONGROUP and CARTONTYPE fields if pickdetail is added manually (Incident3209733_Defect215086) -- End
	}
			
	//Find label value base on locale
	/**
	 * Read label.
	 *
	 * @param widgetName the widget name
	 * @return the string
	 */
	private static String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
	

	
	//Read current status, throw error if null
	/**
	 * Read status.
	 *
	 * @param focus the focus
	 * @param form the form
	 * @return the int
	 * @throws UserException the user exception
	 */
	private int readStatus(DataBean focus, RuntimeFormInterface form) throws UserException{
		Object temp = focus.getValue(STATUS);
		if(temp==null){
			String[] parameters = new String[2];
			parameters[0] = (String)temp;
			parameters[1] = colonStrip(readLabel(form.getFormWidgetByName(STATUS)));
			throw new UserException(ERROR_MESSAGE_INVALID_STATUS, parameters);
		}
		return Integer.parseInt(temp.toString());
	}
	
	/**
	 * Validate order detail list values.
	 *
	 * @param headerFocus the header focus
	 * @param detail the detail
	 * @param uow the uow
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws EpiException the epi exception
	 */
	public  void validateOrderDetailListValues(DataBean headerFocus, RuntimeFormInterface detail, UnitOfWorkBean uow) throws EpiDataException, UserException, EpiException{
		RuntimeListFormInterface list = (RuntimeListFormInterface)detail;
		BioCollectionBean bcList = (BioCollectionBean)list.getFocus();
		//EXISTING RECORD: Open Qty must not be negative
		parameter[0] = "";
		String[] allocQtyExistParam = new String[1];
		allocQtyExistParam[0] = "";
		String[] shortShipReasonParam = new String[1];
		shortShipReasonParam[0] = "";
		try {
			for(int index=0; index<bcList.size(); index++){
				BioBean bio = (BioBean)bcList.elementAt(index);
				if(bio.hasBeenUpdated(OPEN_QTY)){
					RuntimeFormWidgetInterface openQTY = list.getFormWidgetByName(OPEN_QTY);
					RuntimeFormWidgetInterface lineNo = list.getFormWidgetByName(LINE_NO);
					String lineNoValue = bio.get(LINE_NO).toString();
					String openQTYValue = bio.get(OPEN_QTY).toString();				
					
					if(openQTYValue.startsWith("-")){
						if(parameter[0].equalsIgnoreCase("")){		
							parameter[0]+="("+colonStrip(readLabel(lineNo))+", "+colonStrip(readLabel(openQTY))+":  "+lineNoValue+", "+openQTYValue;
						}else{
							parameter[0]+="; "+lineNoValue+", "+openQTYValue;
						}
					}else{
						double allocQty = Double.parseDouble(commaStrip(bio.get(ALLOC_QTY).toString()));
						double openQtyNo = Double.parseDouble(commaStrip(openQTYValue));
						if((openQtyNo==0.0) && (allocQty>0.0)){
							allocQtyExistParam[0] = allocQtyExistParam[0].equalsIgnoreCase("") ? allocQtyExistParam[0]+lineNoValue : allocQtyExistParam[0]+", "+lineNoValue;
						}
						
						//Short Ship Changes
						double originalQty = BioAttributeUtil.getDouble(bio, "UOMADJORIGINALQTY");
						if(shortShipValidation(headerFocus, uow, bio, openQtyNo, originalQty) == false)
						{
							shortShipReasonParam[0] = shortShipReasonParam[0].equalsIgnoreCase("") ? shortShipReasonParam[0]+ lineNoValue
								: shortShipReasonParam[0] + ", "+ lineNoValue;
						}
					}
				}
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
		if(!parameter[0].equalsIgnoreCase("")){
			parameter[0]+=")";
			throw new FormException(ERROR_MESSAGE_LIST_NON_NEGATIVE, parameter);
		}else if(!allocQtyExistParam[0].equalsIgnoreCase("")){
			throw new UserException(ERROR_MESSAGE_ALLOC_QTY_EXISTS, allocQtyExistParam);
		}else if(!"".equalsIgnoreCase(shortShipReasonParam[0])){
			uow.clearState();
			throw new UserException(WMEXP_ORDER_SHORT_SHIP_REASON_LIST, shortShipReasonParam);
		}

		//Convert open qty to eaches
		try {
			for(int index=0; index<bcList.size(); index++){
				BioBean bio = bcList.get(""+index);
				if(bio.hasBeenUpdated(OPEN_QTY)){
					Object packVal = bio.get(PACK); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					String uom = bio.get(UOM).toString();
					if(packVal != null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
						String uom3 = UOMMappingUtil.getPACKUOM3Val(packVal.toString(), uow); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
						if(!uom.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
							String pack = packVal.toString();
							String qty = bio.get(OPEN_QTY).toString();
							bio.set(OPEN_QTY, UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
						}
					}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					String [] parameters = new String[1];
					parameters[0] = bio.get(LINE_NO).toString();
					if(openQtyValidation(bio) == false){
						throw new UserException("WMEXP_ALLOCATE_OPEN_QTY", parameters);
					}
					
					flowThruQtyValidation(bio);
				}
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
	}

	/**
	 * Short ship validation.
	 *
	 * @param headerFocus the header focus
	 * @param uow the uow
	 * @param bio the bio
	 * @param openQtyNo the open qty no
	 * @param originalQty the original qty
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean shortShipValidation(DataBean headerFocus,
			UnitOfWorkBean uow, BioBean bio, double openQtyNo,
			double originalQty) throws EpiDataException {

		if (openQtyNo < originalQty
				&& reasonCodeRequired(headerFocus, bio, uow)) {
			// need to check owner and customer for REQREASONSHORTSHIP
			if (StringUtils.isEmpty(BioAttributeUtil.getString(bio,
					"SHORTSHIPREASON"))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Open qty validation.
	 *
	 * @param bio the bio
	 * @return true, if successful
	 * @throws EpiDataInvalidAttrException the epi data invalid attr exception
	 */
	private boolean openQtyValidation(BioBean bio) throws EpiDataInvalidAttrException {
		String status = BioAttributeUtil.getString(bio, "STATUS");
		if("14".equals(status) || "15".equals(status) || "16".equals(status) ||"17".equals(status))	{
			if(bio.hasBeenUpdated("OPENQTY"))
			{
				double openQty = BioAttributeUtil.getDouble(bio, "OPENQTY");
				double allocatedQty = BioAttributeUtil.getDouble(bio, "QTYALLOCATED");
				//QTYALLOCATED]<=[OPENQTY]
				if(allocatedQty > openQty)
				{
					return false;
				}
			}
		}
		
		if("15".equals(status) || "52".equals(status) || "53".equals(status)|| "55".equals(status) || "57".equals(status)){
			if(bio.hasBeenUpdated("OPENQTY"))
			{
				double openQty = BioAttributeUtil.getDouble(bio, "OPENQTY");
				double pickedQty = BioAttributeUtil.getDouble(bio, "QTYPICKED");
				//QTYPICKED]<=[OPENQTY]
				if(pickedQty > openQty)
				{
					return false;
				}
			}
		}
		if("11".equals(status) || "12".equals(status)){
			if(bio.hasBeenUpdated("OPENQTY"))
			{
				double openQty = BioAttributeUtil.getDouble(bio, "OPENQTY");
				double preallocatedQty = BioAttributeUtil.getDouble(bio, "QTYPREALLOCATED");
				if(preallocatedQty > openQty)
				{
					return false;
				}
			}
		}
		return true;
	}

	
	
	/**
	 * Reason code required.
	 *
	 * @param headerFocus the header focus
	 * @param bio the bio
	 * @param uow the uow
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	private boolean reasonCodeRequired(DataBean headerFocus, BioBean bio, UnitOfWorkBean uow) throws EpiDataException {
		boolean reqReason = false;
		//check CUSTOMER 2
		String customer = BioAttributeUtil.getString(headerFocus, "CONSIGNEEKEY");
		if(!StringUtils.isEmpty(customer))
		{
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_storer", "wm_storer.STORERKEY = '" + customer + "' and wm_storer.TYPE = '2' and wm_storer.REQREASONSHORTSHIP = '1'", ""));
			if(rs.size() == 1)
			{
				reqReason = true;
			}
		}
		//check OWNER 1
		String owner = BioAttributeUtil.getString(bio, "STORERKEY");
		if(!StringUtils.isEmpty(owner))
		{
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_storer", "wm_storer.STORERKEY = '" + owner + "' and wm_storer.TYPE = '1' and wm_storer.REQREASONSHORTSHIP = '1'", ""));
			if(rs.size() == 1)
			{
				reqReason = true;
			}
		}
		return reqReason;
	}
	
	//added overload method***************************************************************************************************************
	/**
	 * Validate order detail list values.
	 *
	 * @param headerFocus the header focus
	 * @param detail the detail
	 * @param actionType the action type
	 * @param uow the uow
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws EpiException the epi exception
	 */
	public  void validateOrderDetailListValues(DataBean headerFocus, RuntimeFormInterface detail, String actionType, UnitOfWorkBean uow) throws EpiDataException, UserException, EpiException{
		if(ACTION_TYPE_DELETE_DETAIL_LINE.equalsIgnoreCase(actionType)){
			RuntimeListFormInterface list = (RuntimeListFormInterface)detail;
			ArrayList bcList = list.getAllSelectedItems();
			//EXISTING RECORD: Open Qty must not be negative
			parameter[0] = "";
			String[] allocQtyExistParam = new String[1];
			allocQtyExistParam[0] = "";
			String[] shortShipReasonParam = new String[1];
			shortShipReasonParam[0] = "";
			for (int index = 0; index < bcList.size(); index++) {
				BioBean bio = (BioBean) bcList.get(index);
				if (bio.hasBeenUpdated(OPEN_QTY)) {
					RuntimeFormWidgetInterface openQTY = list.getFormWidgetByName(OPEN_QTY);
					RuntimeFormWidgetInterface lineNo = list.getFormWidgetByName(LINE_NO);
					String lineNoValue = bio.get(LINE_NO).toString();
					String openQTYValue = bio.get(OPEN_QTY).toString();

					if (openQTYValue.startsWith("-")) {
						if (parameter[0].equalsIgnoreCase("")) {
							parameter[0] += "(" + colonStrip(readLabel(lineNo)) + ", " + colonStrip(readLabel(openQTY)) + ":  " + lineNoValue + ", " + openQTYValue;
						}else{
							parameter[0] += "; " + lineNoValue + ", " + openQTYValue;
						}
					} else {
						double allocQty = Double.parseDouble(commaStrip(bio.get(ALLOC_QTY).toString()));
						double openQtyNo = Double.parseDouble(commaStrip(openQTYValue));
						if ((openQtyNo == 0.0) && (allocQty > 0.0)) {
							allocQtyExistParam[0] = allocQtyExistParam[0].equalsIgnoreCase("") ? allocQtyExistParam[0] + lineNoValue : allocQtyExistParam[0] + ", " + lineNoValue;
						}
						
						//Short Ship Changes
						double originalQty = BioAttributeUtil.getDouble(bio, "UOMADJORIGINALQTY");
						if(shortShipValidation(headerFocus, uow, bio, openQtyNo, originalQty) == false)
						{
							shortShipReasonParam[0] = shortShipReasonParam[0].equalsIgnoreCase("") ? shortShipReasonParam[0]+ lineNoValue
								: shortShipReasonParam[0] + ", "+ lineNoValue;
						}
						
					}
				}
			}
			if(!parameter[0].equalsIgnoreCase("")){
				parameter[0]+=")";
				throw new FormException(ERROR_MESSAGE_LIST_NON_NEGATIVE, parameter);
			}else if(!allocQtyExistParam[0].equalsIgnoreCase("")){
				throw new UserException(ERROR_MESSAGE_ALLOC_QTY_EXISTS, allocQtyExistParam);
			}else if(!"".equalsIgnoreCase(shortShipReasonParam[0])){
				throw new UserException(WMEXP_ORDER_SHORT_SHIP_REASON_LIST, shortShipReasonParam);
			}

			//Convert open qty to eaches
			for(int index=0; index<bcList.size(); index++){
				BioBean bio = (BioBean)bcList.get(index);
				if(bio.hasBeenUpdated(OPEN_QTY)){
					String uom = bio.get(UOM).toString();
					String uom3 = UOMMappingUtil.getPACKUOM3Val(bio.get(PACK).toString(), uow); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					if(!uom.equals(uom3)){							
						String pack = bio.get(PACK).toString();
						String qty = bio.get(OPEN_QTY).toString();					
						bio.set(OPEN_QTY, UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom, UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					}
				}
			}
		}
	}
	//end ****************************************************************************************************************************
	
	
	
	/**
	 * Modification History
	 * 
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 
	 * 04/23/2009	AW	SDIS:SCM-00000-06871 Machine:2353530
	 * Changed code to allow qty values for the Currency Locale
	 * something other than dollar.
	 * 05/19/2009   AW  SDIS:SCM-00000-06871 Machine:2353530
	 * UOM conversion is now done in the front end.
	 * Changes were made accordingly.
	 *
	 * @param headerFocus the header focus
	 * @param detail the detail
	 * @param detail2 the detail2
	 * @param uow the uow
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 * @throws EpiException the epi exception
	 */
	public  void validateOrderDetailFormValues(DataBean headerFocus, RuntimeFormInterface detail, RuntimeFormInterface detail2, UnitOfWorkBean uow) throws UserException, EpiDataException, EpiException{
		nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
		RuntimeFormWidgetInterface minShipPercent = detail.getFormWidgetByName(MIN_SHIP_PERCENT);
		
		//Verify MinShipPercent is percent value
		if(minShipPercent.getDisplayValue()!=null){
			int mspValue = Integer.parseInt(minShipPercent.getDisplayValue());
			if((mspValue<0) || (mspValue>100)){
				parameter[0]=colonStrip(readLabel(minShipPercent));
				throw new FormException(ERROR_MESSAGE_PERCENT, parameter);
			}
		}
		
		
		if(!detail2.getName().equals(BLANK)){
			//Verify Shelf Life is not negative
			RuntimeFormWidgetInterface shelfLife = detail2.getFormWidgetByName(SHELF_LIFE);
			if(shelfLife.getDisplayValue()!=null){
				double number = Double.parseDouble(commaStrip(shelfLife.getDisplayValue()));
				if(number<0){
					parameter[0] = colonStrip(readLabel(shelfLife));
					throw new FormException(ERROR_MESSAGE_UNDER_ZERO, parameter);
				}
			}
			
			RuntimeFormWidgetInterface cubicMeter = detail2.getFormWidgetByName(CUBICMETER);
			if(cubicMeter.getDisplayValue()!=null){
				double number = Double.parseDouble(commaStrip(cubicMeter.getDisplayValue()));
				if(number<0){
					parameter[0] = colonStrip(readLabel(cubicMeter));
					throw new FormException(ERROR_MESSAGE_UNDER_ZERO, parameter);
				}
			}
			
			RuntimeFormWidgetInterface hundredWeight = detail2.getFormWidgetByName(HUNDREDWEIGHT);
			if(hundredWeight.getDisplayValue()!=null){
				double number = Double.parseDouble(commaStrip(hundredWeight.getDisplayValue()));
				if(number<0){
					parameter[0] = colonStrip(readLabel(hundredWeight));
					throw new FormException(ERROR_MESSAGE_UNDER_ZERO, parameter);
				}
			}
		}
		
		
		
		RuntimeFormWidgetInterface openQTY = detail.getFormWidgetByName(OPEN_QTY);
		String openQTYValue = openQTY.getDisplayValue();
		DataBean detailFocus = detail.getFocus();
		
		//validate STAGELOC is of type PICKTO
		pickToValidation(BioAttributeUtil.getString(detailFocus, "STAGELOC"), colonStrip(readLabel(detail.getFormWidgetByName("STAGELOC"))), uow);

		if(detailFocus.isTempBio()){
			//NEW RECORD: Open Qty must be positive
			//Convert open qty to eaches
			QBEBioBean qbe = (QBEBioBean)detailFocus;
			String uom = qbe.get(UOM).toString();
			Object packVal = qbe.get(PACK); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			if(packVal != null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String uom3 = UOMMappingUtil.getPACKUOM3Val(qbe.get(PACK).toString(), uow); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String qty = qbe.get(OPEN_QTY).toString();
				if(!uom.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String pack = qbe.get(PACK).toString();
				
				//qbe.set(OPEN_QTY, UOMMappingUtil.convertUOMQty(uom,UOMMappingUtil.UOM_EA, qty, pack));//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				qbe.set(OPEN_QTY, UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			}
				else
				{
					qbe.set(OPEN_QTY, qty);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
			}//Aw...............
			
			//ADDED: 09/21/07 ISSUE #7372.b -HC
			//NEW RECORD: Open Qty must be positive
			openQTYValue = qbe.get(OPEN_QTY).toString();
			if(openQTYValue!=null){
				double temp = Double.parseDouble(commaStrip(openQTYValue));
				if(temp<=0){
					parameter[0]=colonStrip(readLabel(openQTY));
					throw new FormException(ERROR_MESSAGE_UNDER_ZERO,parameter);
				}					
			}
			
			//New Record cannot be less than 1
			try	{
				//09/21/07 ISSUE #7372 -HC
				if(nf.parse(openQTYValue).doubleValue() <= 0) {				
				//09/21/07 ISSUE #7372 -HC the user should be able to input an order quantity of < 1, even for the master unit
					throw new UserException("WM_EXP_NONNEG", new Object[] {colonStrip(readLabel(openQTY))});
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			flowThruQtyValidation(qbe);
			//ADDED: 09/21/07 ISSUE #7372.e -HC			
			detailFocus.setValue("ORDERDETAILID", GUIDFactory.getGUIDStatic());
		}else{				
			//EXISTING RECORD: Open Qty must not be negative
			if(openQTYValue.startsWith("-")){
				parameter[0]=colonStrip(readLabel(openQTY));
				throw new FormException(ERROR_MESSAGE_NON_NEGATIVE, parameter);
			}else{
				BioBean bio = (BioBean)detailFocus;
				double allocQty = Double.parseDouble(commaStrip(bio.get(ALLOC_QTY).toString()));
				double openQtyNo = Double.parseDouble(commaStrip(openQTYValue));
				if((openQtyNo==0.0) && (allocQty>0.0)){
					parameter[0] = bio.get(LINE_NO).toString();
					throw new UserException(ERROR_MESSAGE_ALLOC_QTY_EXISTS, parameter);
				}

				
				double originalQty = BioAttributeUtil.getDouble(bio, "UOMADJORIGINALQTY");
				if(shortShipValidation(headerFocus, uow, bio, openQtyNo, originalQty) == false)
				{
					throw new UserException("WMEXP_ORDER_SHORT_SHIP_REASON", new String[]{""});
				}
			}
			
			
			
			//Convert open qty to eaches
			BioBean bio = (BioBean)detailFocus;
			String uom = bio.get(UOM).toString();
			if(bio.hasBeenUpdated(OPEN_QTY)){		//ADDED 7390
				//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440 start
				String pack = bio.get(PACK).toString();
				String qty = commaStrip(openQTYValue); //AW Machine: 2059980 SDIS: SCM-00000-05176
				if(pack != null){
					String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uow); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					if(!uom.equals(uom3)){
						//bio.set(OPEN_QTY, UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530					
						bio.set(OPEN_QTY, UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),uom,UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					}
					else
					{
						bio.set(OPEN_QTY, qty);
					}
				}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440 end
				
				String [] parameters = new String[1];
				parameters[0] = bio.get(LINE_NO).toString();
				if(openQtyValidation(bio) == false){
					throw new UserException("WMEXP_ALLOCATE_OPEN_QTY", parameters);
				}
				
				flowThruQtyValidation(bio);
			}
		}
		//}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440 end	
	}
	
	/**
	 * Flow thru qty validation.
	 *
	 * @param orderDetail the order detail
	 */
	private void flowThruQtyValidation(DataBean orderDetail) {
		//FT Changes
		if(orderDetail.getValue("PROCESSEDQTY") == null ) {
			orderDetail.setValue("PROCESSEDQTY", new BigDecimal(0));
		}
		
		orderDetail.setValue("QTYTOPROCESS", orderDetail.getValue("OPENQTY"));
	}
	
	/**
	 * Pick to validation.
	 *
	 * @param loc the loc
	 * @param widgetName the widget name
	 * @param uow the uow
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 */
	private void pickToValidation(String loc, String widgetName, UnitOfWorkBean uow) throws EpiDataException, UserException
	{
		if (!StringUtils.isEmpty(loc))
		{
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_location", "wm_location.LOC = '" + loc + "' and wm_location.LOCATIONTYPE = 'PICKTO'", null));
			if (rs.size() == 0)
			{
				throw new UserException("WMEXP_LOC_TYPE_PICKTO", new Object[] { widgetName });
			}
		}

	}
}