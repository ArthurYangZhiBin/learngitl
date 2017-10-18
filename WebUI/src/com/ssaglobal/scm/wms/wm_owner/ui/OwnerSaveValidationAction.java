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

package com.ssaglobal.scm.wms.wm_owner.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_cxstorer.ui.CXStorerTab;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.ssaglobal.scm.wms.wm_storer.ui.ServiceTab;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OwnerSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerSaveValidationAction.class);

	String ownerValue;

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

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of OwnerSaveValidationAction", SuggestedCategory.NONE);

		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		//General
		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add("tab 0");
		RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_general_view", tabIdentifiers, state);
		if (!isNull(generalForm))
		{
			GeneralTab generalValidation = new GeneralTab(generalForm, state);
			generalValidation.run();
		}
		else
		{
			//if it cannot find the first tab, there is no detail
			//unable to save
			return RET_CANCEL;
		}

		//Address
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 1");
		RuntimeFormInterface addressForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_address_view", tabIdentifiers, state);
		if (!isNull(addressForm))
		{
			AddressTab addressValidation = new AddressTab(addressForm, state);
			addressValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Address is Null", SuggestedCategory.NONE);
		}

		//BillingAddress
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 2");
		RuntimeFormInterface bAddressForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_billing_address", tabIdentifiers, state);
		if (!isNull(addressForm))
		{
			BillingAddressTab bAddressValidation = new BillingAddressTab(bAddressForm, state);
			bAddressValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Billing Address is Null", SuggestedCategory.NONE);
		}

		//Misc
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 3");
		RuntimeFormInterface miscForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_miscellaneous_view", tabIdentifiers, state);
		if (!isNull(miscForm))
		{
			MiscTab miscValidation = new MiscTab(miscForm, state);
			miscValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! MISC IS NULL", SuggestedCategory.NONE);
		}

		//Flow Thru
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 4");
		RuntimeFormInterface flowThruForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_crossdock_view", tabIdentifiers, state);
		if (!isNull(flowThruForm))
		{
			FlowThruTab flowThruValidation = new FlowThruTab(flowThruForm, state);
			//flowThruValidation.run();   //SRG Bugaware# 9357
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! CROSSDOCK IS NULL", SuggestedCategory.NONE);
		}

		//Billing
/*		tabIdentifiers.clear();
		tabIdentifiers.add("tab 5");
		RuntimeFormInterface billingForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_billing_view", tabIdentifiers, state);
		if (!isNull(billingForm))
		{
			BillingTab billingValidation = new BillingTab(billingForm, state);
//			billingValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Billing Form is Null", SuggestedCategory.NONE);
		}
*/
		//Task Manager
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 7");
		RuntimeFormInterface taskManagerForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_taskmanager_view", tabIdentifiers, state);
		if (!isNull(taskManagerForm))
		{
			TaskManagerTab taskManagerValidation = new TaskManagerTab(taskManagerForm, state);
			taskManagerValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Task Manager is Null", SuggestedCategory.NONE);
		}

		//Receiving
/*		tabIdentifiers.clear();
		tabIdentifiers.add("tab 7");
		RuntimeFormInterface receivingForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_receiving_view", tabIdentifiers, state);
		if (!isNull(receivingForm))
		{
			ReceivingTab receivingValidation = new ReceivingTab(receivingForm, state);
			receivingValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Receiving is Null", SuggestedCategory.NONE);
		}
*/
		//Labels
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 8");
		RuntimeFormInterface labelsForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_label_view", tabIdentifiers, state);
		if (!isNull(labelsForm))
		{
			LabelsTab labelsValidation = new LabelsTab(labelsForm, state);
			labelsValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Labels is Null", SuggestedCategory.NONE);
		}

		//Processing	
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 9");
		RuntimeFormInterface processingForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_ccphysicalinv_view", tabIdentifiers, state);
		if (!isNull(processingForm)) {
			ProcessingTab processingValidation = new ProcessingTab(processingForm, state);
			processingValidation.run();
		} else {
			_log.debug("LOG_DEBUG_EXTENSION", "! Processing is Null", SuggestedCategory.NONE);
		}

		//Owner Labels
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 10");
		RuntimeFormInterface ownerLabelsToggleForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_labels_toggle_view", tabIdentifiers, state);
		_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle" + ownerLabelsToggleForm.getName(), SuggestedCategory.NONE);
		SlotInterface ownerLabelsToggleSlot = ownerLabelsToggleForm.getSubSlot("wm_owner_labels_toggle_slot");
		RuntimeFormInterface ownerLabelsDetail = state.getRuntimeForm(ownerLabelsToggleSlot, "wm_owner_labels_detail_view");
		if (!isNull(ownerLabelsDetail) && !(ownerLabelsDetail.getName().equals("Blank"))
				&& !(ownerLabelsDetail.getFocus().isBioCollection()))
		{
			OwnerLabelsTab ownerLabelsValidation = new OwnerLabelsTab(ownerLabelsDetail, state);
			ownerLabelsValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Owner Labels is Null", SuggestedCategory.NONE);
		}
		
		//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Start
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 18");
		RuntimeFormInterface ownerChargeCodesToggleForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_chargecodesforbilling_toggle", tabIdentifiers, state);
		_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle" + ownerChargeCodesToggleForm.getName(), SuggestedCategory.NONE);
		SlotInterface ownerChargeCodesToggleSlot = ownerChargeCodesToggleForm.getSubSlot("wm_owner_chargecodesforbilling_toggle");
		RuntimeFormInterface ownerChargeCodesDetail = state.getRuntimeForm(ownerChargeCodesToggleSlot, "wm_owner_chargecodesforbilling_toggle_DetailView");
		if (!isNull(ownerChargeCodesDetail) && !(ownerChargeCodesDetail.getName().equals("Blank"))
				&& !(ownerChargeCodesDetail.getFocus().isBioCollection()))
		{
			OwnerChargeCodesTab ownerChargeCodesValidation = new OwnerChargeCodesTab(ownerChargeCodesDetail, state);
			ownerChargeCodesValidation.run(context);

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Owner Charge Codes is Null", SuggestedCategory.NONE);
		}
		//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - End

		//UDF Labels
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 12");
		RuntimeFormInterface udfLabelsToggleForm = FormUtil.findForm(shellToolbar, "wms_list_Shell", "wm_owner_udf_toggle_view", tabIdentifiers, state);

		_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle" + udfLabelsToggleForm.getName(), SuggestedCategory.NONE);
		SlotInterface udfLabelsToggleSlot = udfLabelsToggleForm.getSubSlot("wm_owner_labels_toggle_slot");
		RuntimeFormInterface udfLabelsDetail = state.getRuntimeForm(udfLabelsToggleSlot, "wm_owner_udf_detail_view");
		if (!isNull(udfLabelsDetail) && !(udfLabelsDetail.getName().equals("Blank"))
				&& !(udfLabelsDetail.getFocus().isBioCollection()))
		{
			UDFLabelsTab udfLabelsValidation = new UDFLabelsTab(udfLabelsDetail, state);
			udfLabelsValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! UDF Labels is Null", SuggestedCategory.NONE);
		}

		//Owner Reports
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 13");
		RuntimeFormInterface ownerReportsToggleForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_owner_reportformat_toggle_view", tabIdentifiers, state);
		_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle" + ownerReportsToggleForm.getName(), SuggestedCategory.NONE);
		SlotInterface ownerReportsToggleSlot = ownerReportsToggleForm.getSubSlot("wm_owner_reportformat_toggle_slot");
		RuntimeFormInterface ownerReportsDetail = state.getRuntimeForm(ownerReportsToggleSlot, "wm_owner_reportformat_detail_view");
		if (!isNull(ownerReportsDetail) && !(ownerReportsDetail.getName().equals("Blank"))
				&& !(ownerReportsDetail.getFocus().isBioCollection()))
		{
			OwnerReportsTab ownerReportsValidation = new OwnerReportsTab(ownerReportsDetail, state);
			ownerReportsValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Owner Reports is Null", SuggestedCategory.NONE);
		}
		
		//CX Stuff
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 14");
		RuntimeFormInterface cxStorerToggleForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_cxstorer_toggle_view", tabIdentifiers, state);
		_log.debug("LOG_DEBUG_EXTENSION", "@\n\ttoggle" + cxStorerToggleForm.getName(), SuggestedCategory.NONE);
		SlotInterface cxStorerToggleSlot = cxStorerToggleForm.getSubSlot("wm_cxstorer_toggle_view");
		RuntimeFormInterface cxStorerDetail = state.getRuntimeForm(cxStorerToggleSlot, "DETAIL1");
		if (!isNull(cxStorerDetail) && !(cxStorerDetail.getName().equals("Blank"))
				&& !(cxStorerDetail.getFocus().isBioCollection()))
		{
			CXStorerTab cxStorerValidation = new CXStorerTab(cxStorerDetail, generalForm.getFocus(), state);
			cxStorerValidation.run();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! CX Storer is Null", SuggestedCategory.NONE);
		}
		
		
		tabIdentifiers.clear();
		tabIdentifiers.add("tab 16");
		RuntimeFormInterface serviceToggle = FormUtil.findForm(
				shellToolbar, "wms_list_shell", "wm_spsspecialsvcs_toggle",
				tabIdentifiers, state);
		if (!isNull(serviceToggle)) {
			SlotInterface serviceToggleSlot = serviceToggle
					.getSubSlot("toggle");
			RuntimeFormInterface serviceDetail = state.getRuntimeForm(
					serviceToggleSlot, "sps_special_detail");
			if (!isNull(serviceDetail)
					&& !(serviceDetail.getName().equals("Blank"))
					&& !(serviceDetail.getFocus().isBioCollection())) {
				ServiceTab serviceValidation = new ServiceTab(
						serviceDetail, state, generalForm.getFocus());
				serviceValidation.run();
			} else {
				_log.debug("LOG_DEBUG_EXTENSION",
						"!@# SPS Services Is Null", SuggestedCategory.NONE);
			}
		}

		return RET_CONTINUE;
	}

	class TaskManagerTab extends Tab
	{
		public TaskManagerTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskManagerTab" + form.getName(), SuggestedCategory.NONE);
		}

		void run() throws EpiDataException, UserException
		{
			locValidation("DEFAULTQCLOC");
			locValidation("DEFAULTQCLOCOUT");
			locValidation("DEFAULTRETURNSLOC");
			pickToLocValidation("DEFAULTPACKINGLOCATION");

		}

	}

	class ReceivingTab extends Tab
	{
		public ReceivingTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of ReceivingTab " + form.getName(), SuggestedCategory.NONE);
		}

		void run() throws EpiDataException, UserException
		{

		}

	}

	class ProcessingTab extends Tab {
		public ProcessingTab(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of ReceivingTab " + form.getName(), SuggestedCategory.NONE);
		}

		void run() throws EpiDataException, UserException {
			NSQLConfigUtil spsInstalled = new NSQLConfigUtil(state, "SPS_INSTALLED");
			if (spsInstalled.isOn()) {
				String spsuomweight = BioAttributeUtil.getString(focus, "SPSUOMWEIGHT");
				String spsuomdimension = BioAttributeUtil.getString(focus, "SPSUOMDIMENSION");
				if (StringUtils.isEmpty(spsuomweight) || StringUtils.isEmpty(spsuomdimension)) {
					if(focus.isTempBio())
					{
						//set default
						focus.setValue("SPSUOMWEIGHT", "LB");
						focus.setValue("SPSUOMDIMENSION", "IN");
					}
					else {
						throw new UserException("WMEXP_OWNER_SPS_DIMENSION", new Object[] {});
					}
				}
			}

		}

	}

	class BillingAddressTab extends Tab
	{
		public BillingAddressTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Billing Address Tab Validation " + form.getName(), SuggestedCategory.NONE);
		}

		void run() throws UserException
		{
			//if (!isAttributeEmpty("B_STATE"))
			//{
			//	lengthValidation("B_STATE", 25);
			//}
		}

		protected void lengthValidation(String attributeName, int length) throws UserException
		{
			String attributeValue = focus.getValue(attributeName).toString();
			if (attributeValue.length() != length)
			{
				String[] parameters = new String[3];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = String.valueOf(length);
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_STATE", parameters);
			}
		}
	}

	class AddressTab extends Tab
	{

		public AddressTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Address Tab Validation " + form.getName(), SuggestedCategory.NONE);
		}

		void run() throws UserException
		{
			//if (!isAttributeEmpty("STATE"))
			//{
			//	lengthValidation("STATE", 25);
			//}

		}

		protected void lengthValidation(String attributeName, int length) throws UserException
		{
			String attributeValue = focus.getValue(attributeName).toString();
			if (attributeValue.length() != length)
			{
				String[] parameters = new String[3];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = String.valueOf(length);
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_STATE", parameters);
			}
		}

	}

	class LabelsTab extends Tab
	{
		public LabelsTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Labels Tab Validation " + form.getName(), SuggestedCategory.NONE);

		}

		void run() throws UserException
		{
			//Prevent Null 
			checkNull("LPNLENGTH");
			setNextLPNIfBlank();
			//Verify LPNSTARTNUMBER, NEXTLPNNUMBER, LPNROLLBACKNUMBER are Numbers
			//Rollback > Next > Start > 0
			//Rollback.length = Next.length = Start.length = LPNLENGTH
			////verify that LPN Start Not Equal to 0 and < All 9's
			greaterThanZeroValidation("LPNSTARTNUMBER");
			notAllNinesValidation("LPNSTARTNUMBER");
			greaterThanOrEqualValidation("NEXTLPNNUMBER", "LPNSTARTNUMBER");
			greaterThanOrEqualValidation("LPNROLLBACKNUMBER", "NEXTLPNNUMBER");
			lpnLengthValidation("LPNSTARTNUMBER");
			lpnLengthValidation("NEXTLPNNUMBER");
			lpnLengthValidation("LPNROLLBACKNUMBER");
			uccValidation();
			if (!isAttributeEmpty("UCCVENDORNUMBER"))
			{
				greaterThanZeroValidation("UCCVENDORNUMBER");
			}

		}

		private void uccValidation() throws UserException
		{
			Object barcodeFormat = focus.getValue("LPNBARCODEFORMAT");
			if (isEmpty(barcodeFormat))
			{
				return;
			}
			if (((String) barcodeFormat).equals("1"))
			{
				//ensure UCCVENDORNUMBER.length >7
				Object uccVendorNumber = focus.getValue("UCCVENDORNUMBER");
				if (isEmpty(uccVendorNumber))
				{
					String[] parameters = new String[2];
					parameters[0] = retrieveLabel("UCCVENDORNUMBER");
					parameters[1] = retrieveFormTitle();
					throw new UserException("WMEXP_OWNER_UCCVEN", parameters);
				}
				if (((String) uccVendorNumber).trim().length() < 7)
				{
					String[] parameters = new String[2];
					parameters[0] = retrieveLabel("UCCVENDORNUMBER");
					parameters[1] = retrieveFormTitle();
					throw new UserException("WMEXP_OWNER_UCCVEN", parameters);
				}

			}

		}

		private void setNextLPNIfBlank()
		{
			//Set nextlpn to lpnstart if nextlpn is null
			Object nextLPN = focus.getValue("NEXTLPNNUMBER");
			Object lpnStart = focus.getValue("LPNSTARTNUMBER");
			if (isEmpty(nextLPN) && !isEmpty(lpnStart))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "#@! Setting NextLPNNumber", SuggestedCategory.NONE);
				focus.setValue("NEXTLPNNUMBER", lpnStart);
			}
		}

		protected void lpnLengthValidation(String attributeName) throws UserException
		{
			String attributeValue = focus.getValue(attributeName).toString();
			int lpnLength = Integer.parseInt(focus.getValue("LPNLENGTH").toString());
			if (attributeValue.length() != lpnLength)
			{
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = String.valueOf(lpnLength);
				parameters[3] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_LPN_LENGTH", parameters);
			}
		}

		protected void greaterThanOrEqualValidation(String attributeName, String otherAttributeName) throws UserException
		{
			numericValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			String attributeValue = focus.getValue(attributeName).toString();
			double otherValue = NumericValidationCCF.parseNumber(focus.getValue(otherAttributeName).toString());

			double value = NumericValidationCCF.parseNumber(attributeValue);
			_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value + " & " + " Other Value "
					+ otherAttributeName + " - " + otherValue, SuggestedCategory.NONE);
			//System.out.println("Value of " + attributeName + " - " + value + " & " + " Other Value "
			//		+ otherAttributeName + " - " + otherValue);
			if (Double.isNaN(value))
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}
			else if (value < otherValue)
			{
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = removeTrailingColon(retrieveLabel(otherAttributeName).trim());
				parameters[3] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_GREATER_VALIDATION", parameters);
			}

		}

		protected void notAllNinesValidation(String attributeName) throws UserException
		{

			String attributeValue = focus.getValue(attributeName).toString();
			if (isEmpty(attributeValue))
			{
				return;
			}
			String allNines = returnAllNines(attributeValue);
			if (attributeValue.trim().equals(allNines))
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_NOT_ALL_9S", parameters);
			}

		}

		private String returnAllNines(String attributeValue)
		{
			StringBuffer allNines = new StringBuffer();
			for (int i = 0; i < attributeValue.length(); i++)
			{
				allNines.append("9");
			}
			_log.debug("LOG_DEBUG_EXTENSION", ")))All9s " + allNines, SuggestedCategory.NONE);
			return (allNines.toString());

		}

		private void checkNull(String widget) throws UserException
		{
			if (isNull(focus.getValue(widget)))
			{
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(widget).trim());
				parameters[1] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_LABELS_NULL", parameters);
			}
		}

	}

	class UDFLabelsTab extends Tab
	{

		String udfLabel;

		String ownerValue;

		public UDFLabelsTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of UDF Labels Tab Validation " + form.getName(), SuggestedCategory.NONE);

		}

		public void run() throws DPException, UserException
		{

			udfLabel = focus.getValue("UDFLABEL").toString();
			ownerValue = focus.getValue("STORERTEMP") == null ? null : focus.getValue("STORERTEMP").toString();
			if (isInsert)
			{
				udfLabelDuplication();
			}

		}

		private void udfLabelDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM STORER_UDF WHERE (UDFLABEL = '" + udfLabel + "') AND (STORERKEY = '"
					+ ownerValue + "')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("UDFLABEL");
				parameters[1] = udfLabel;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_UDF_LABEL_DUPLICATE", parameters);
			}
		}

	}

	class OwnerLabelsTab extends Tab
	{

		String customerValue;

		String ownerValue;

		String labelName;

		String labelType;

		public OwnerLabelsTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Owner Labels Tab Validation " + form.getName(), SuggestedCategory.NONE);

		}

		public void run() throws DPException, UserException, EpiDataInvalidAttrException
		{

			Object tempValue = focus.getValue("CONSIGNEEKEY");
			labelType = focus.getValue("LABELTYPE").toString();
			labelName = focus.getValue("LABELNAME").toString();
			//Backup, STORER_INDIRECT
			try
			{
				ownerValue = focus.getValue("STORERTEMP").toString();
			} catch (NullPointerException e)
			{
				ownerValue = focus.getValue("STORER_INDIRECT").toString();
			}
			//			_log.debug("LOG_DEBUG_EXTENSION", "BIO " + focus.getValue("STORERTEMP") + " " + focus.getValue("STORER_INDIRECT"), SuggestedCategory.NONE);
			//			_log.debug("LOG_DEBUG_EXTENSION", "Form " + form.getFormWidgetByName("STORERTEMP").getDisplayValue() + " " + form.getFormWidgetByName("STORERTEMP").getValue(), SuggestedCategory.NONE);
			//			_log.debug("LOG_DEBUG_EXTENSION", "Form " + form.getFormWidgetByName("STORER_INDIRECT").getDisplayValue() + " " + form.getFormWidgetByName("STORER_INDIRECT").getValue(), SuggestedCategory.NONE);
			//test for valid customer
			if (!isNull(tempValue))
			{
				customerValue = tempValue.toString().toUpperCase();
				if (!customerValue.equalsIgnoreCase("DEFAULT"))
				{
					customerValidation(2);
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!Setting Customer to Default", SuggestedCategory.NONE);
				customerValue = "DEFAULT";
				focus.setValue("CONSIGNEEKEY", "DEFAULT");

			}

			//test for duplicates
			if (isInsert)
			{
				ownerLabelDuplication();
			}

		}

		private void ownerLabelDuplication() throws DPException, UserException
		{

			//Check TYPE & Customer
			String queryTypeCustomer = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelType + "') "
					+ "AND (CONSIGNEEKEY = '" + customerValue + "')" + "AND (STORERKEY = '" + ownerValue + "')";
			EXEDataObject resultsTypeCustomer = WmsWebuiValidationSelectImpl.select(queryTypeCustomer);
			if (resultsTypeCustomer.getRowCount() >= 1)
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("CONSIGNEEKEY");
				parameters[1] = customerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_LABEL_DUPLICATE", parameters);
			}
			//Check TYPE & LabelName
			String queryTypeName = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelType + "') "
					+ "AND (LABELNAME = '" + labelName + "') " + "AND (STORERKEY = '" + ownerValue + "') AND CONSIGNEEKEY='"+ customerValue + "'";
			EXEDataObject resultsTypeName = WmsWebuiValidationSelectImpl.select(queryTypeName);
			if (resultsTypeName.getRowCount() >= 1)
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("LABELNAME");
				parameters[1] = labelName;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_LABEL_DUPLICATE", parameters);
			}
		}

		void customerValidation(int i) throws DPException, UserException
		{
			if (verifyStorer(customerValue, i) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("CONSIGNEEKEY");
				parameters[1] = customerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
		}

	}

	class OwnerReportsTab extends Tab
	{

		String customerValue;
		
		String ownerValue;

		String rpt_id;

		String customreporttype;

		public OwnerReportsTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Owner Reports Tab Validation " + form.getName(), SuggestedCategory.NONE);

		}

		public void run() throws DPException, UserException, EpiDataInvalidAttrException
		{

			Object tempValue = focus.getValue("consigneekey");
			rpt_id = focus.getValue("rpt_id").toString();
			customreporttype = focus.getValue("customreporttype").toString();
			ownerValue = focus.getValue("storerkey").toString();
_log.debug("LOG_SYSTEM_OUT","&&& owner="+ownerValue,100L);			
			//test for valid customer
			if (!isNull(tempValue))
			{
				customerValue = tempValue.toString().toUpperCase();
				_log.debug("LOG_SYSTEM_OUT","&&& owner="+ownerValue+"     customer="+tempValue.toString().toUpperCase(),100L);			
				if (!customerValue.equalsIgnoreCase("DEFAULT"))
				{
					customerValidation(2);
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!Setting Customer to Default", SuggestedCategory.NONE);
				_log.debug("LOG_SYSTEM_OUT","&&& customer is set to default",100L);	
				customerValue = "DEFAULT";
				focus.setValue("consigneekey", "DEFAULT");

			}

			//test for duplicates
			if (isInsert)
			{
				ownerReportDuplication();
			}

		}

		private void ownerReportDuplication() throws DPException, UserException
		{
			//Check type/customer/owner
			String queryTypeCustomer = "SELECT 1 " + "FROM storer_reports " + 
						"WHERE customreporttype = '" + customreporttype + "' " +
						"AND consigneekey = '" + customerValue + "' " + 
						"AND storerkey = '" + ownerValue + "' ";
			EXEDataObject resultsTypeCustomer = WmsWebuiValidationSelectImpl.select(queryTypeCustomer);
			if (resultsTypeCustomer.getRowCount() >= 1)
			{
				_log.debug("LOG_SYSTEM_OUT","&&& owner report customer duplicate",100L);
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("customreporttype");
				parameters[1] = customreporttype;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_REPORT_DUPLICATE", parameters);
			}
			//Check Report Name
			String queryTypeName = "SELECT 1 " + "FROM storer_reports " + 
						"WHERE rpt_id = '" + rpt_id + "' " +
						"AND storerkey = '" + ownerValue + "' AND CONSIGNEEKEY='" + customerValue + "' ";
			EXEDataObject resultsTypeName = WmsWebuiValidationSelectImpl.select(queryTypeName);
			if (resultsTypeName.getRowCount() >= 1)
			{
				_log.debug("LOG_SYSTEM_OUT","&&& owner storer report duplicate",100L);
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("rpt_id");
				parameters[1] = rpt_id;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_REPORT_DUPLICATE", parameters);
			}
			
		}

		void customerValidation(int i) throws DPException, UserException
		{
			if (verifyStorer(customerValue, i) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("CONSIGNEEKEY");
				parameters[1] = customerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
		}

	}

	class MiscTab extends Tab
	{
		RuntimeFormInterface othersForm;

		DataBean othersFocus;

		public MiscTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Misc Tab Validation " + form.getName(), SuggestedCategory.NONE);
			SlotInterface othersSlot = form.getSubSlot("OTHERS");
			othersForm = state.getRuntimeForm(othersSlot, null);
		}

		void run() throws UserException
		{
			if (!isNull(othersForm))
			{
				numericValidation("CREDITLIMIT", othersForm);
			}
		}
	}

	class BillingTab extends Tab
	{

		public BillingTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Billing Tab Validation " + form.getName(), SuggestedCategory.NONE);
		}

		public void run() throws UserException
		{
			//Check Charges >0
			greaterThanZeroCurrencyValidation("HIMINIMUMRECEIPTCHARGE");
			greaterThanZeroCurrencyValidation("HOMINIMUMSHIPMENTCHARGE");
			greaterThanZeroCurrencyValidation("ISMINIMUMRECEIPTCHARGE");
			greaterThanZeroCurrencyValidation("HIMINIMUMINVOICECHARGE");
			greaterThanZeroCurrencyValidation("ISMINIMUMINVOICECHARGE");
			greaterThanZeroCurrencyValidation("RSMINIMUMINVOICECHARGE");
			greaterThanZeroCurrencyValidation("ALMINIMUMCHARGE");

		}

		protected void currencyValidation(String attributeName) throws UserException
		{
			Object attributeValue = focus.getValue(attributeName);
			if (!isEmpty(attributeValue) && (NumericValidationCCF.isCurrency(attributeValue.toString()) == false))
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}

		}

		protected void greaterThanZeroCurrencyValidation(String attributeName) throws UserException
		{
			currencyValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			String attributeValue = focus.getValue(attributeName).toString();
			double value = NumericValidationCCF.parseCurrency(attributeValue);
			_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
			if (Double.isNaN(value))
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}
			else if (value < 0)
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
			}

		}

	}

	class FlowThruTab extends Tab
	{
		RuntimeFormInterface productivityForm;
		private final RuntimeFormInterface opportunisticForm;
		


		public FlowThruTab(RuntimeFormInterface f, StateInterface st) throws EpiDataException
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Flow Thru Tab Validation " + form.getName(), SuggestedCategory.NONE);
			//
			
			//try to get subforms
			SlotInterface productivitySlot = form.getSubSlot("wm_owner_crossdock_productivitygain");
			productivityForm = state.getRuntimeForm(productivitySlot, null);
			
			SlotInterface oppurtunisticSlot = form.getSubSlot("wm_owner_crossdock_opportunistic");
			opportunisticForm = state.getRuntimeForm(oppurtunisticSlot, null);

		}

		void run() throws UserException, EpiDataException
		{
			
			if (!isNull(opportunisticForm))
			{
				//if Enable Opportunistic Crossdock is checked, 
				//make sure associated items do not have end to end checked

				String enableOppXDock = BioAttributeUtil.getString(focus, "ENABLEOPPXDOCK");
				if ("1".equals(enableOppXDock))
				{
					UnitOfWorkBean tuow = state.getTempUnitOfWork();
					BioCollectionBean rs = tuow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + focus.getValue("STORERKEY") + "'", null));
					for (int i = 0; i < rs.size(); i++)
					{
						BioBean item = rs.get("" + i);
						int endToEnd = BioAttributeUtil.getInt(item, "SNUM_ENDTOEND");
						if (endToEnd == 1)
						{
							//throw error
							throw new UserException("WMEXP_OPPXDOCK_ENDTOEND", new Object[] {});
						}
					}
				}

			}
			
			/*
			 * Maximum # of Orders – STORER. Maximumorders (validate numeric value)
			 Minimum Allowable % - STORER. Minimumpercent (validate numeric value)
			 Order Date – From Today – STORER.orderdatestartdays (validate numeric value)
			 Order Date – To Today – STORER.orderdateenddays (validate numeric value)

			 */
			if (!isNull(productivityForm))
			{
				greaterThanOrEqualToZeroValidation("MAXIMUMORDERS", productivityForm);
				numericValidation("MINIMUMPERCENT", productivityForm);
				numericValidation("ORDERDATESTARTDAYS", productivityForm);
				numericValidation("ORDERDATEENDDAYS", productivityForm);
			}

		}

	}

	class GeneralTab extends Tab
	{
		String ownerValue;

		public GeneralTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of General Tab Validation " + form.getName(), SuggestedCategory.NONE);
			ownerValue = focus.getValue("STORERKEY") == null ? null : focus.getValue("STORERKEY").toString();
		}

		void run() throws DPException, UserException
		{
			if (isInsert)
			{
				ownerDuplication();
			}
			alphaNumericValidation("SCAC_CODE");
		}

		private void alphaNumericValidation(String attributeName) throws UserException
		{
			Object attributeValue = focus.getValue(attributeName);
			if (isNull(attributeValue))
			{
				return;
			}
			if (!(attributeValue.toString().matches("^[\\d\\w]*$")))
			{
				String[] parameters = new String[2];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName));
				parameters[1] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_ALPHANUM", parameters);
			}

		}

		void ownerDuplication() throws DPException, UserException
		{
			if (isEmpty(ownerValue))
			{
				String[] params = new String[1];
				params[0] = removeTrailingColon(retrieveLabel(("STORERKEY")));
				throw new UserException("WMEXP_REQFIELD", params);
			}
			String type = BioAttributeUtil.getString(focus, "TYPE");
			String query = "SELECT * FROM STORER WHERE (STORERKEY = '" + ownerValue + "' AND TYPE = '" + type +"')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_OWNER_DUPLICATE", parameters);
			}
		}

	}
	
	//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Starts
	class OwnerChargeCodesTab extends Tab
	{
		String customerValue;
		String labelName;
		String labelType;
		String ownerValue;
		String ChargeCodeValue;
		String TransactionType;
		String RFFlagValue;
		String DefaultFlag = "0";

		public OwnerChargeCodesTab(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of Owner Labels Tab Validation " + form.getName(), SuggestedCategory.NONE);

		}
		//methods to be implimented

		public void isTheRecordSetExists() throws DPException, UserException{

			String queryTypeChargeCodesByOwner = "SELECT * " +  " FROM BILL_OWNERCHARGECODES " + "WHERE (TRANTYPE = '" + TransactionType + "') "
			+ "AND (CHARGE_CODE = '" + ChargeCodeValue + "')" + "AND (STORERKEY = '" + ownerValue + "')";
			EXEDataObject resultsTypeChargeCodesByOwner= WmsWebuiValidationSelectImpl.select(queryTypeChargeCodesByOwner);
			if (resultsTypeChargeCodesByOwner.getRowCount() > 0)
			{
				String[] parameters = new String[3];
				parameters[0] = ownerValue;
				parameters[1] = TransactionType;
				parameters[2] = ChargeCodeValue;
				throw new UserException("WMEXP_OWNER_CHARGECODE_DUPLICATE", parameters);
			}
		}

		public void removeDefaultFlag(ActionContext context ) throws EpiException
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean rs = uowb.getBioCollectionBean(new Query("bill_ownerchargecodes", "bill_ownerchargecodes.STORERKEY = '" + ownerValue+ "'", null));

			try
			{
				for (int i = 0; i < rs.size(); i++) {
					BioBean bill_ownerchargecodes = rs.get("" + i);
					bill_ownerchargecodes.setValue("DEFAULT_FLAG",0);
					bill_ownerchargecodes.save();	
				}
				uowb.saveUOW();
			}
			catch(EpiException ex)
			{
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}

		public void run(ActionContext context) throws DPException, UserException, EpiDataInvalidAttrException
		{     
			boolean isNewBio = false;

			if (focus instanceof QBEBioBean) 
			{
				isNewBio = true;
			}
			if(focus instanceof BioBean)
			{
				isNewBio =  false;
			}

			//Backup, STORER_INDIRECT
			try
			{
				ownerValue = focus.getValue("STORERKEY").toString();
				TransactionType = focus.getValue("TRANTYPE").toString();
				ChargeCodeValue = focus.getValue("CHARGE_CODE").toString();
				DefaultFlag = focus.getValue("DEFAULT_FLAG").toString();

			} catch (NullPointerException e)
			{
				ownerValue = focus.getValue("STORERKEY").toString();
			}

			if(isNewBio){
				isTheRecordSetExists();
			}else{
				//Check if the record type or charge code is modified.
				UnitOfWorkBean uow = state.getTempUnitOfWork();		

			}

			if(DefaultFlag.equalsIgnoreCase("1")){
				try{
					removeDefaultFlag(context);
				}catch(Exception e){

				}
			}
			
			//SRG.b
			String billToCust = focus.getValue("BILL_TO_CUST").toString();
			if (verifyStorer(billToCust, 14) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("BILL_TO_CUST");
				parameters[1] = billToCust;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
			//SRG.e
		}		
	}	
	//Krishna Kuchipudi: March-24-2010 : 3PL Enhancements:  Owner charge codes for billing - Ends

	boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	boolean isEmpty(Object attributeValue)
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

}
