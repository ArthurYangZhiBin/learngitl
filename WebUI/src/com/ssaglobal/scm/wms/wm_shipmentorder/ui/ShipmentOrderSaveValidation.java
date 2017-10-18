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

package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
//import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
//import com.ssaglobal.scm.wms.wm_item.ui.Tab;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ReturnPartyQueryAction;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ShipmentOrderSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderSaveValidation.class);

	String sellerHeader;

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();

		//Header Validation
		ArrayList tabs = new ArrayList();
		tabs.add("tab 0");
		RuntimeFormInterface soHeader = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_header_view", tabs, state);
		if (!isNull(soHeader)) {
			SOHeader soHeaderValidation = new SOHeader(soHeader, state);
			soHeaderValidation.run();
		}

		tabs.clear();
		tabs.add("tab 1");
		RuntimeFormInterface soShipTo = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_shipto_view", tabs, state);
		if (!isNull(soShipTo)) {
			SOShipTo soShipToValidation = new SOShipTo(soShipTo, state);
			soShipToValidation.run();
		}

		tabs.clear();
		tabs.add("tab 2");
		RuntimeFormInterface soBillTo = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_billto_view", tabs, state);
		if (!isNull(soBillTo)) {
			SOBillTo soBillToValidation = new SOBillTo(soBillTo, state);
			soBillToValidation.run();
		}

		tabs.clear();
		tabs.add("tab 3");
		RuntimeFormInterface soMisc = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_misc_view", tabs, state);
		if (!isNull(soMisc)) {
			SOMisc soMiscValidation = new SOMisc(soMisc, state);
			soMiscValidation.run();
		}
		
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End
		tabs.clear();
		tabs.add("tab 4");
		RuntimeFormInterface carrierValidation = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_carrier_view", tabs, state);
		String carrier = carrierValidation.getFormWidgetByName("CarrierCode").getDisplayValue() == null || carrierValidation.getFormWidgetByName("CarrierCode").getDisplayValue().toString().matches("\\s*") ? "" : carrierValidation.getFormWidgetByName("CarrierCode").getDisplayValue().toString();
		String trailer = carrierValidation.getFormWidgetByName("TrailerNumber").getDisplayValue() == null || carrierValidation.getFormWidgetByName("TrailerNumber").getDisplayValue().toString().matches("\\s*") ? "" : carrierValidation.getFormWidgetByName("TrailerNumber").getDisplayValue().toString();
		if(!trailer.equalsIgnoreCase(""))
		{
			if(carrier.equalsIgnoreCase(""))
			{
				throw new UserException("Carrier cannot be empty when Trailer is associated with Shipment order ", new Object[1]);
			}
		}
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End

		tabs.clear();
		tabs.add("tab 4");
		RuntimeFormInterface soCarrier = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_carrier_view", tabs, state);
		if (!isNull(soCarrier)) {
			SOCarrier soCarrierValidation = new SOCarrier(soCarrier, state);
			soCarrierValidation.run();
		}

		tabs.clear();
		tabs.add("tab 6");
		RuntimeFormInterface labelRfid = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_label_rfid_view", tabs, state);
		if (!isNull(labelRfid)) {
			SOLabelRfid soLabelRfidValidation = new SOLabelRfid(labelRfid, state);
			soLabelRfidValidation.run();
		}
//SRG.b
		tabs.clear();
		tabs.add("tab 12");
		RuntimeFormInterface billCharge = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_order_bill_chargeinstruct_list_view", tabs, state);
		if (!isNull(billCharge)) {
			SOBillCharge soBillChargeValidation = new SOBillCharge(billCharge, state);
			soBillChargeValidation.run();
		}
//SRG.e
	
		//Detail
		tabs.clear();
		tabs.add("list_slot_2");
		tabs.add("tbgrp_slot");
		tabs.add("wm_shipmentorderdetail_toggle_view");
		tabs.add("wm_shipmentorderdetail_detail_view");
		
		tabs.add("tab 0");
		RuntimeFormInterface soDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_detail1_view",tabs, state);
		if(!isNull(soDetail)) {
			SODetail1 soDetail1Validation = new SODetail1(soDetail, state);
			soDetail1Validation.run();
		}
		
		tabs.clear();
		tabs.add("list_slot_2");
		tabs.add("tbgrp_slot");
		tabs.add("wm_shipmentorderdetail_toggle_view");
		tabs.add("wm_shipmentorderdetail_detail_view");
		
		tabs.add("tab 3");
		RuntimeFormInterface labelRfidDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_shipmentorder_Labelrfid_view", tabs, state);
		if (!isNull(labelRfidDetail)) {
			SOLabelRfid soLabelRfidValidation = new SOLabelRfid(labelRfidDetail, state);
			soLabelRfidValidation.run();
		}
//SRG.b
		tabs.clear();
		tabs.add("list_slot_2");
		tabs.add("tbgrp_slot");
		tabs.add("wm_shipmentorderdetail_toggle_view");
		tabs.add("wm_shipmentorderdetail_detail_view");
		tabs.add("tab 8");
		RuntimeFormInterface billChargeDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_list_shell_shipmentorder", "wm_orderdetail_bill_chargeinstruct_list_view", tabs, state);
		if (!isNull(billChargeDetail)) {
			SOBillCharge soBillChargeValidation = new SOBillCharge(billChargeDetail, state);
			soBillChargeValidation.run();
		}
//SRG.e		
		return RET_CONTINUE;
	}

	public class SOHeader extends FormValidation {
		public SOHeader(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws UserException, EpiDataException {
			ownerValidation("STORERKEY");
			sellerHeader = focus.getValue("STORERKEY") == null ? null : focus.getValue("STORERKEY").toString().toUpperCase();
			poValidation();
		}

		private void poValidation() throws EpiDataException, UserException {
			String poKey = BioAttributeUtil.getString(focus, "POKEY");
			if(!StringUtils.isEmpty(poKey)){
				String sQueryString = "(wm_po.POKEY = '"+poKey+"') and wm_po.POTYPE = '1' and NOT(wm_po.STATUS IN ('9','11','15','20')) ";
				Query bioQuery = new Query("wm_po",sQueryString,null);
				UnitOfWorkBean uowb = state.getTempUnitOfWork();
				BioCollectionBean rs = uowb.getBioCollectionBean(bioQuery);
				if(rs.size()== 0){
					throw new UserException("WMEXP_INVALID_PO", new String[]{poKey});
				}
			}
			
		}
	}

	public class SOShipTo extends FormValidation {
		public SOShipTo(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws UserException, EpiDataException {
			if ("12".equals(ReturnPartyQueryAction.getStorerType(focus))) {
				shipFromValidation("CONSIGNEEKEY");
			} else {
				customerValidation("CONSIGNEEKEY");
			}
		}
	}

	public class SOBillTo extends FormValidation {
		public SOBillTo(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws DPException, UserException {
			billToValidation("BILLTOKEY");
		}
	}

	public class SOMisc extends FormValidation {
		public SOMisc(RuntimeFormInterface soMisc, StateInterface state) {
			super(soMisc, state);
		}

		public void run() throws EpiDataException, UserException {
			pickToLocValidation("PACKINGLOCATION");
		}
	}

	public class SOCarrier extends FormValidation {
		public SOCarrier(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws EpiDataException, UserException {
			carrierValidation("CarrierCode");
		}
	}
	
//SRG.b
	public class SOBillCharge extends FormValidation {
		public DataBean chargeFocus = null;
		public SOBillCharge(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
			if (f.getFocus()instanceof BioCollectionBean){
				chargeFocus = (BioCollectionBean)f.getFocus();
			}
		}

		public void run() throws EpiDataException, UserException {
			if (chargeFocus != null){
				BioCollectionBean chargeBioCol = (BioCollectionBean) chargeFocus;
				for (int i = 0; i < chargeBioCol.size(); i++){
					BioBean chargeRecord = chargeBioCol.get("" + i);
					if (chargeRecord.hasBeenUpdated("BILL_TO_CUST")){
						verifyBilltoCust((String)chargeRecord.get("BILL_TO_CUST",false),4);
						chargeRecord.set("BILL_TO_CUST", chargeRecord.get("BILL_TO_CUST",false).toString().toUpperCase());
					}
				}
			}
		}
		public void verifyBilltoCust(String attributeValue, int type) throws DPException, UserException {
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			String query = "SELECT * FROM STORER WHERE (STORERKEY = '"+attributeValue+"') AND (TYPE = '"+type+"')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() != 1) {
				String[] parameters = new String[1];
				parameters[0] = attributeValue;
				throw new UserException("WMEXP_INVALID_BILLTOCUST", parameters);
			}
		}
		//SRG.e
	}
//SRG.e
	public class SOLabelRfid extends FormValidation {
		public SOLabelRfid(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws UserException, EpiDataException {
			labelNameValidation("LABELNAME");
			labelNameValidation("RFIDGTINLABELNAME");//RFIDGTINLABELNAME
			labelNameValidation("RFIDSSCCLABELNAME");//RFIDSSCCLABELNAME
			labelNameValidation("STDGTINLABELNAME");//STDGTINLABELNAME
			labelNameValidation("STDSSCCLABELNAME");//STDSSCCLABELNAME
		}

		void labelNameValidation(String attributeName) throws UserException, EpiDataException {
			if (verifyBioAttribute(attributeName, "wm_label_configuration", "LABELNAME") == false) {
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = focus.getValue(attributeName).toString();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}

		private boolean verifyBioAttribute(String attributeName, String bio, String bioAttribute) throws EpiDataException {
			Object attributeValue = focus.getValue(attributeName);
			if (isEmpty(attributeValue)) {
				return true; //Do Nothing
			}
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			String query = bio + "." + bioAttribute + " = '" + attributeValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
			BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query(bio, query, null));

			if (results.size() == 1) {
				//value exists, verified
				return true;
			} else {
				//value does not exist
				return false;
			}
		}
	}

	public class SODetail1 extends FormValidation {
		public SODetail1(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws EpiDataException, UserException {
			itemValidation("SKU");
			packValidation();
			validation("TARIFFKEY", "TARIFF", "TARIFFKEY");
			
		}

		protected void itemValidation(String itemAttribute) throws EpiDataException, UserException {
			if (verifyItemByOwner(itemAttribute) == false) {
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(itemAttribute));
				parameters[1] = focus.getValue(itemAttribute).toString();
				throw new UserException("WMEXP_NOTAVALID", parameters);
			}
		}

		protected boolean verifyItemByOwner(String itemAttributeName) throws EpiDataException {
			Object itemAttributeValue = focus.getValue(itemAttributeName);
			Object ownerAttributeValue = focus.getValue("STORERKEY");
			if (isEmpty(itemAttributeValue) || (isEmpty(ownerAttributeValue))) {
				return true; //Do Nothing
			}

			itemAttributeValue = itemAttributeValue == null ? null : itemAttributeValue.toString().toUpperCase();
			ownerAttributeValue = ownerAttributeValue == null ? null : ownerAttributeValue.toString().toUpperCase();
			String query = "SELECT * " + " FROM " + "SKU" + " WHERE " + "SKU" + " = '" + itemAttributeValue + "' "
					+ " AND STORERKEY = '" + ownerAttributeValue + "'";
			_log.debug("LOG_SYSTEM_OUT","Query\n" + query,100L);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1) {
				//value exists, verified
				return true;
			} else {
				//value does not exist
				return false;
			}
		}
	}

	boolean isNull(Object attributeValue) {
		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}
	}
}