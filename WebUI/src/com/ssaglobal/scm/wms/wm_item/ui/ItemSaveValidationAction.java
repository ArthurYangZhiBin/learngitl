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
package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

//SM 08/15/07 ISSUE #6834 ADDED VALIDATION TO RECEIVING TAB
public class ItemSaveValidationAction extends ActionExtensionBase{
	//Karen's changes include moving all shipping validation to the receiving tab and adding shelf validation to the general tab
	//Compare with version 1.16.2.6 to see all changes
	public boolean catchWeightChecked = false;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemSaveValidationAction.class);
	
	private static String GENERAL_TAB_ID = "tab 0";
	private static String RECEIVING_INFO_TAB_ID = "tab 1"; 
//	private static String LOTTABLE_BARCODE_TAB_ID = "tab 2"; 
	private static String CATCH_WEIGHT_DATA_TAB_ID = "tab 3"; 
	private static String SERIAL_NUMBER_TAB_ID = "tab 5"; 
	
	private static String INBOUND_TAB_ID = "tab 7"; 
	private static String OUTBOUND_TAB_ID = "tab 8"; 
	private static String CYCLE_COST_TAB_ID = "tab 9"; 
//	private static String NOTES_TAB_ID = "tab 8"; 
	private static String ASSIGN_LOCATIONS_TAB_ID = "tab 12"; 
	private static String ALTERNATE_ITEMS_TAB_ID = "tab 14"; 
	private static String SUBSTITUTE_ITEMS_TAB_ID = "tab 16"; 
//	private static String HISTORY_TAB_ID = "tab 12"; 
	
	//SRG Begin: Incident3735455_Defect285007
	String sessionObjectValue;
	String sessionVariable;
	//SRG End: Incident3735455_Defect285007

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface listForm = state.getRuntimeForm(listSlot, null);

		// Iterate over our listform's collection of BIOs...
		BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();
		//SRG Begin: Incident3735455_Defect285007
		RuntimeListFormInterface itemList = (RuntimeListFormInterface)listForm;		
		String interactionID = state.getInteractionId();		
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		//This value is set in the ItemListPreRender extension since this cannot be obtained in this extension.
		sessionObjectValue = (String)session.getAttribute(sessionVariable);
		
		int pageStart = Integer.parseInt(sessionObjectValue);
		int pageSize = itemList.getWindowSize();				
		int totalListSize = listFocus.size();  //Total size of the item list
		int totPrevItems = (totalListSize - pageStart); //Total no of items in the previous pages
		int startValue = 0;
		int endValue = 0;
		if (totPrevItems > pageSize) {
			startValue = pageStart;
			endValue = pageStart + pageSize;
		}
		else { //Last page in the item list
			startValue = pageStart;
			endValue = totalListSize;
		}		
		//for (int i = 0; i < listFocus.size(); i++)
		for (int i = startValue; i < endValue; i++)
        //SRG End: Incident3735455_Defect285007
		{
			BioBean itemRecord = listFocus.get("" + i);
			if (itemRecord.hasBeenUpdated("PACKKEY"))
			{
				Object attributeValue = itemRecord.getValue("PACKKEY");
				if (!isEmpty(attributeValue)) 
				{
					attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
					String query = "SELECT * FROM PACK WHERE PACKKEY = '"+attributeValue.toString()+"'";
					_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
					EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
					if (results.getRowCount() < 1) 
					{
						String[] parameters = new String[3];
						parameters[0] = retrieveLabel(listForm,"PACKKEYENT");
						parameters[1] = attributeValue.toString();
						throw new UserException("WMEXP_ITEM_DOES_NOT_EXIST", parameters);
					}
				}
				else
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { retrieveLabel(listForm,"PACKKEYENT") });
				}
			}
		}

		//General Detail Form Validation
		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add(GENERAL_TAB_ID);
		RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_general_detail_view", tabIdentifiers, state);
		if(!isNull(generalForm)){
			GeneralTab generalValidation = new GeneralTab(generalForm, state);
			generalValidation.run();
		}
		
		//Receiving 
		tabIdentifiers.clear();
		tabIdentifiers.add(RECEIVING_INFO_TAB_ID);
		RuntimeFormInterface receivingForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_receivinginfo_detail_view", tabIdentifiers, state);
		if(!isNull(receivingForm)){
			ShippingTab shippingValidation = new ShippingTab(receivingForm, state);
			shippingValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# ReceivingTab is null", SuggestedCategory.NONE);
		}

		//Shipping Info Tab Validation
		//See Shipping Tab class for removal comments
//		tabIdentifiers.clear();
//		tabIdentifiers.add("tab 2");
//		RuntimeFormInterface shippingForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_shippinginfo_detail_view", tabIdentifiers, state);
//		if(!isNull(shippingForm)){
//			ShippingTab shippingValidation = new ShippingTab(shippingForm, state);
//			shippingValidation.run();
//		}else{
//			_log.debug("LOG_DEBUG_EXTENSION", "SHIPPING ORDER IS NULL", SuggestedCategory.NONE);
//		}

		//Weight/Data Tab
		tabIdentifiers.clear();
		tabIdentifiers.add(CATCH_WEIGHT_DATA_TAB_ID);
		RuntimeFormInterface wdForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_weightdata_detail_view", tabIdentifiers, state);
		if(!isNull(wdForm)){
			WeightDataTab wdValidation = new WeightDataTab(wdForm, state);
			wdValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Weight Data is Null", SuggestedCategory.NONE);
		}
		
		//Serial Number
		tabIdentifiers.clear();
		tabIdentifiers.add(SERIAL_NUMBER_TAB_ID);
		RuntimeFormInterface snForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_serial_number_view", tabIdentifiers, state);
		if(!isNull(snForm)){
			SerialTab snValidation = new SerialTab(snForm, state);
			snValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Serial Number is Null", SuggestedCategory.NONE);
		}
		
		
		//Inbound validation
		tabIdentifiers.clear();
		tabIdentifiers.add(INBOUND_TAB_ID);
		RuntimeFormInterface inboundForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_inbound", tabIdentifiers, state);
		if(!isNull(inboundForm)){
			InboundTab inboundValidation = new InboundTab(inboundForm, state);
			inboundValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Control1 is Null", SuggestedCategory.NONE);
		}
		
		//Outbound validation
		tabIdentifiers.clear();
		tabIdentifiers.add(OUTBOUND_TAB_ID);
		RuntimeFormInterface outboundForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_control1_how", tabIdentifiers, state);
		if(!isNull(outboundForm)){
			OutboundTab outboundValidation = new OutboundTab(outboundForm, state);
			outboundValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Control1 is Null", SuggestedCategory.NONE);
		}

		//Control 2
//		tabIdentifiers.clear();
//		tabIdentifiers.add("tab 6");
//		RuntimeFormInterface control2Form = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_control2_detail_view", tabIdentifiers, state);
//		if(!isNull(control2Form)){
//			Control2Tab c2Validation = new Control2Tab(control2Form, state);
//			c2Validation.run();
//		}else{
//			_log.debug("LOG_DEBUG_EXTENSION", "Control2 is Null", SuggestedCategory.NONE);
//		}

		//Cycle Costs
		tabIdentifiers.clear();
		tabIdentifiers.add(CYCLE_COST_TAB_ID);
		RuntimeFormInterface ccForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_cyclecosts_detail_view", tabIdentifiers, state);
		if(!isNull(ccForm)){
			CycleCostsTab ccValidation = new CycleCostsTab(ccForm, state);
			ccValidation.run();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Cycle Costs is Null", SuggestedCategory.NONE);
		}

		//Assign Locations
		tabIdentifiers.clear();
		tabIdentifiers.add(ASSIGN_LOCATIONS_TAB_ID);
		RuntimeFormInterface assignToggle = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_assignlocations_formslot", tabIdentifiers, state);
		if(!isNull(assignToggle)){
			SlotInterface assignToggleSlot = assignToggle.getSubSlot("wm_assignlocations_formslot");
			RuntimeFormInterface assignDetail = state.getRuntimeForm(assignToggleSlot, "wm item assignlocation toggle detail tab");
			if(!isNull(assignDetail) && !(assignDetail.getName().equals("Blank")) && !(assignDetail.getFocus().isBioCollection())){
				AssignLocationsTab assignValidation = new AssignLocationsTab(assignDetail, generalForm, state);
				assignValidation.run();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Assign Locations Is Null", SuggestedCategory.NONE);
			}
			RuntimeListFormInterface assignList = (RuntimeListFormInterface) state.getRuntimeForm(assignToggleSlot, "wm item assignlocation toggle list tab");
			if(!isNull(assignList) && !(assignList.getName().equals("Blank")) && (assignList.getFocus().isBioCollection()))
			{
				AssignLocationsListTab assignListValidation = new AssignLocationsListTab(assignList, state);
				assignListValidation.run();
			}
		}

		//Alternate Items
		tabIdentifiers.clear();
		tabIdentifiers.add(ALTERNATE_ITEMS_TAB_ID);
		RuntimeFormInterface alternateToggle = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_alternate_toggle_form", tabIdentifiers, state);
		if(!isNull(alternateToggle)){
			SlotInterface alternateToggleSlot = alternateToggle.getSubSlot("wm_item_alternate_toggle");
			RuntimeFormInterface alternateDetail = state.getRuntimeForm(alternateToggleSlot, "wm_item_alternate_toggle_detail_tab");
			if(!isNull(alternateDetail) && !(alternateDetail.getName().equals("Blank")) && !(alternateDetail.getFocus().isBioCollection())){
				AlternateItemsTab alternateValidation = new AlternateItemsTab(alternateDetail, generalForm, state);
				alternateValidation.run();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Alternate Items Is Null", SuggestedCategory.NONE);
			}
		}

		//Substitute Items
		tabIdentifiers.clear();
		tabIdentifiers.add(SUBSTITUTE_ITEMS_TAB_ID);
		RuntimeFormInterface subToggle = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_substitutesku_toggle_form", tabIdentifiers, state);
		if(!isNull(subToggle)){
			SlotInterface subToggleSlot = subToggle.getSubSlot("wm_item_substitutesku_toggle");
			RuntimeFormInterface subDetail = state.getRuntimeForm(subToggleSlot, "wm_item_substitutesku_toggle_detail");
			if(!isNull(subDetail) && !(subDetail.getName().equals("Blank")) && !(subDetail.getFocus().isBioCollection())){
				SubItemsTab subValidation = new SubItemsTab(subDetail, generalForm, state);
				subValidation.run();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Substitute Items Is Null", SuggestedCategory.NONE);
			}
		}

		//Verify Defaults
		//if Outbound QC Location is null, set to Default Outbound QC Location of Owner
		return RET_CONTINUE;
	}

	String retrieveLabel(RuntimeFormInterface form, String widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	boolean isNull(Object attributeValue){
		if(attributeValue == null){
			return true;
		}else{
			return false;
		}
	}

	private class AssignLocationsListTab extends Tab {

		public AssignLocationsListTab(RuntimeFormInterface f, StateInterface st) {
			super(f, st);
		}

		public void run() throws EpiDataInvalidAttrException, EpiDataException, UserException {
			BioCollectionBean assignListBioCollection = (BioCollectionBean) form.getFocus();
			for(int i = 0; i < assignListBioCollection.size(); i++)
			{
				BioBean assignListBio = assignListBioCollection.get("" + i);
				if(assignListBio.hasBeenUpdated("LOCATIONTYPE"))
				{
					//change values
					String locationTypeValue = assignListBio.getValue("LOCATIONTYPE") == null ? null : assignListBio.getValue("LOCATIONTYPE").toString();
					if(locationTypeValue == null)
					{
						assignListBio.setValue("ALLOWREPLENISHFROMBULK", "0");
						assignListBio.setValue("ALLOWREPLENISHFROMCASEPICK", "0");
						assignListBio.setValue("ALLOWREPLENISHFROMPIECEPICK", "0");
					}
					else if("PICK".equals(locationTypeValue))
					{
						assignListBio.setValue("ALLOWREPLENISHFROMBULK", "1");
						assignListBio.setValue("ALLOWREPLENISHFROMCASEPICK", "1");
						assignListBio.setValue("ALLOWREPLENISHFROMPIECEPICK", "0");
					}
					else if("CASE".equals(locationTypeValue))
					{
						assignListBio.setValue("ALLOWREPLENISHFROMBULK", "1");
						assignListBio.setValue("ALLOWREPLENISHFROMCASEPICK", "0");
						assignListBio.setValue("ALLOWREPLENISHFROMPIECEPICK", "0");

					}
					else
					{
						assignListBio.setValue("ALLOWREPLENISHFROMBULK", "0");
						assignListBio.setValue("ALLOWREPLENISHFROMCASEPICK", "0");
						assignListBio.setValue("ALLOWREPLENISHFROMPIECEPICK", "0");
					}
				}
				if(assignListBio.hasBeenUpdated("QTYLOCATIONLIMIT"))
				{
					greaterThanOrEqualToZeroValidation("QTYLOCATIONLIMIT", assignListBio);
				}
				if(assignListBio.hasBeenUpdated("QTYLOCATIONMINIMUM"))
				{
					greaterThanOrEqualToZeroValidation("QTYLOCATIONMINIMUM", assignListBio);
				}
				if(assignListBio.hasBeenUpdated("REPLENISHMENTUOM"))
				{

				}

			}

		}

		private void greaterThanOrEqualToZeroValidation(String attributeName, BioBean assignListBio) throws UserException {
			numericValidation(attributeName, assignListBio);
			//if it passes numericValidation, you can parse the number
			Object tempValue = assignListBio.getValue(attributeName);
			if (isNull(tempValue)) {
				return;
			}
			String attributeValue = tempValue.toString();
			double value = NumericValidationCCF.parseNumber(attributeValue);
			_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
			if (Double.isNaN(value)) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			} else if (value < 0) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
			}

		}

		private void numericValidation(String attributeName, BioBean assignListBio) throws UserException {
			Object attributeValue = assignListBio.getValue(attributeName);
			if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false)) {
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}
		}

	}
	
	private class GeneralTab extends Tab{
		private String itemValue;
		private String ownerValue;
		RuntimeFormInterface shelfForm;

		public GeneralTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of General Tab Validation - "+form.getName(), SuggestedCategory.NONE);

			SlotInterface shelfToggle = form.getSubSlot("wm_item_receivinginfo_shelflife_detail_view");
			shelfForm = state.getRuntimeForm(shelfToggle, "wm_item_receivinginfo_shelflife_detail_view");
		}

		void itemDuplication() throws DPException, UserException{
			String query = "SELECT * FROM SKU WHERE (SKU = '"+itemValue+"') AND (STORERKEY = '"+ownerValue+"')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1){
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveLabel("SKU");
				parameters[3] = itemValue;
				parameters[4] = retrieveFormTitle();
				throw new UserException("WMEXP_ITEM_DUPLICATE", parameters);
			}
		}

		void run() throws DPException, UserException, EpiDataException{
			_log.debug("LOG_DEBUG_EXTENSION", focus.getValue("PACKKEY")+" "+focus.getValue("STORERKEY")+" "+focus.getValue("SKU"), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", form.getFormWidgetByName("STORERKEY").getDisplayValue(), SuggestedCategory.NONE);
			ownerValue = (String) focus.getValue("STORERKEY") == null ? null : focus.getValue("STORERKEY").toString().toUpperCase();
			itemValue = (String) focus.getValue("SKU") == null ? null : focus.getValue("SKU").toString().toUpperCase();

			storerValidation(1);
			packValidation();
			if(isInsert){
				itemDuplication();
				//load default values
				loadDefaultValues();
			}
			numericValidation("ITEMREFERENCE");
			greaterThanOrEqualToZeroValidation("STDCUBE");
			greaterThanOrEqualToZeroValidation("STDGROSSWGT");
			greaterThanOrEqualToZeroValidation("STDNETWGT");
			greaterThanOrEqualToZeroValidation("TARE");
			determineCatchWeightTabValidation();
			
			//check Item general gross weight = net weight + tare weight
			double gross=0;
			double net=0;
			double tare=0;
			double sum=0;
			String attributeValue =null; 
			

			
			//retrieve std gross wgt
			if(focus.getValue("STDGROSSWGT")!=null)
			{
				attributeValue =focus.getValue("STDGROSSWGT").toString();						
				gross= NumericValidationCCF.parseNumber(attributeValue);
			}	
			//retrieve std net weight
			if(focus.getValue("STDNETWGT")!=null)
			{	
				attributeValue =focus.getValue("STDNETWGT").toString();						
				net= NumericValidationCCF.parseNumber(attributeValue);
			}					
			//retrieve tare weight
			if(focus.getValue("TARE")!=null)
			{
				attributeValue =focus.getValue("TARE").toString();						
				tare= NumericValidationCCF.parseNumber(attributeValue);
			}	
			sum=net+tare;
			

			if(Math.abs(gross - sum)<0.000001) 
			{
				//do nothing
			}
			else
			{	
				//if gross weight sum is not equal to net and tare weight  throw an exception
				String[] parameters = new String[2];
				parameters[0] = "";
				throw new UserException("WMEXP_ITEM_WEIGHTSEQUALLCHECK", new Object[]{null});					
			}
				
			//end of checking
			
			
			
			
			
			
			

			greaterThanOrEqualToZeroValidation("TOEXPIREDAYS");
			greaterThanOrEqualToZeroValidation("TODELIVERBYDAYS");
			greaterThanOrEqualToZeroValidation("TOBESTBYDAYS");
						
			//Shelf life was moved to the general tab during the usability updates with Karen Casemier. Thus the shelf life validation was moved here.
			//-Luis Ochoa 11/14/07
			if(!isNull(shelfForm)) {
				greaterThanOrEqualToZeroValidation("SHELFLIFEONRECEIVING", shelfForm);
				greaterThanOrEqualToZeroValidation("SHELFLIFE", shelfForm);
			}
		}

		private void loadDefaultValues() throws EpiDataException{
			//Query STORER Bio to get Rotate By, Rotation, Strategy, Putaway Strategy, Receipt Validation
			String BIO = "wm_storer";
			String ATTRIBUTE = "STORERKEY";
			String queryStatement = BIO+"."+ATTRIBUTE+" = '"+ownerValue+"'";
			_log.debug("LOG_DEBUG_EXTENSION", "////query statement "+queryStatement, SuggestedCategory.NONE);
			Query query = new Query(BIO, queryStatement, null);
			BioCollectionBean results = focus.getUnitOfWorkBean().getBioCollectionBean(query);

			//Set values
			BioBean ownerBean = results.get("0");

			//These attributes are not able to be set when creating a new Item
			//So I can assume the values will be default by OA, and I can safely override from owner defaults
			if(!isEmpty(ownerBean.get("DEFAULTPUTAWAYSTRATEGY"))){
				focus.setValue("PUTAWAYSTRATEGYKEY", ownerBean.get("DEFAULTPUTAWAYSTRATEGY"));
			}
			if(!isEmpty(ownerBean.get("RECEIPTVALIDATIONTEMPLATE"))){
				focus.setValue("RECEIPTVALIDATIONTEMPLATE", ownerBean.get("RECEIPTVALIDATIONTEMPLATE"));
			}
			if(!isEmpty(ownerBean.get("DEFAULTSKUROTATION"))){
				focus.setValue("ROTATEBY", ownerBean.get("DEFAULTSKUROTATION"));
			}
			if(!isEmpty(ownerBean.get("DEFAULTROTATION"))){
				focus.setValue("DEFAULTROTATION", ownerBean.get("DEFAULTROTATION"));
			}
			if(!isEmpty(ownerBean.get("DEFAULTSTRATEGY"))){
				focus.setValue("STRATEGYKEY", ownerBean.get("DEFAULTSTRATEGY"));
			}
		}

		private void determineCatchWeightTabValidation(){
			//Determine if we need to check Catch Weight Tab
//			Object cwFlag = focus.getValue("CWFLAG");
//			if((isUnchecked(cwFlag))){
//				catchWeightChecked = false;
//			}else{
//				catchWeightChecked = true;
//			}
			catchWeightChecked = true;
		}

		void storerValidation(int i) throws DPException, UserException{
			if(verifyStorer(ownerValue, i) == false){
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
		}
	}

//	Removed Shipping Tab during karen's usability changes. Fields were moved to Receiving tab thus the validations were move there as well.
//	11/14/07 Luis Ochoa
//	private class ShippingTab extends Tab{
//		public ShippingTab(RuntimeFormInterface f, StateInterface st){
//			super(f, st);
//			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Shipping Tab Validation - "+form.getName(), SuggestedCategory.NONE);
//		}
//
//		void run() throws UserException, EpiDataException{
//			hazmatCodeValidation();
//		}
//
//		void hazmatCodeValidation() throws EpiDataException, UserException{
//			String attributeName = "HAZMATCODESKEY";
//			String table = "HAZMATCODES";
//			if(verifySingleAttribute(attributeName, table) == false){
//				//throw exception
//				String[] parameters = new String[3];
//				parameters[0] = retrieveLabel(attributeName);
//				parameters[1] = focus.getValue(attributeName).toString();
//				parameters[2] = retrieveFormTitle();
//				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
//			}
//		}
//	}

	private class AssignLocationsTab extends Tab{
		private final String itemValue;
		private final String ownerValue;
		private final String locValue;
		DataBean parentFocus;

		AssignLocationsTab(RuntimeFormInterface f, RuntimeFormInterface parent, StateInterface st){
			super(f, st);
			parentFocus = parent.getFocus();
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Assign Locations Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			itemValue = parentFocus.getValue("SKU") == null ? null : parentFocus.getValue("SKU").toString().toUpperCase();
			ownerValue = parentFocus.getValue("STORERKEY") == null ? null : parentFocus.getValue("STORERKEY").toString().toUpperCase();
			locValue = focus.getValue("LOC") == null ? null : focus.getValue("LOC").toString().toUpperCase();
		}

		void run() throws UserException, EpiDataException{
			if(isInsert){
				_log.debug("LOG_DEBUG_EXTENSION", "! Insert", SuggestedCategory.NONE);
				assignLocationsDuplication();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "! Update", SuggestedCategory.NONE);
			}
			locValidation("LOC");
			greaterThanOrEqualToZeroValidation("QTYLOCATIONLIMIT");
			greaterThanOrEqualToZeroValidation("QTYLOCATIONMINIMUM");
			greaterThanOrEqualToZeroValidation("QTYREPLENISHMENTOVERRIDE");
		}

		void assignLocationsDuplication() throws DPException, UserException{
			String query = "SELECT * FROM SKUXLOC WHERE (SKU = '"+itemValue+"') AND (STORERKEY = '"+ownerValue+"') AND (LOC = '"+locValue+"')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1){
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("LOC");
				parameters[1] = locValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_SKUXLOC_DUPLICATE", parameters);
			}
		}
	}

	private class AlternateItemsTab extends Tab{
		private final String ownerValue;
		private final String altItemValue;
		DataBean parentFocus;

		public AlternateItemsTab(RuntimeFormInterface f, RuntimeFormInterface parent, StateInterface st){
			super(f, st);
			parentFocus = parent.getFocus();
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Alternate Items Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			ownerValue = parentFocus.getValue("STORERKEY") == null ? null : parentFocus.getValue("STORERKEY").toString().toUpperCase();
			altItemValue = focus.getValue("ALTSKU") == null ? null : focus.getValue("ALTSKU").toString().toUpperCase();
		}

		public void run() throws UserException, EpiDataException{
			if(isInsert){
				_log.debug("LOG_DEBUG_EXTENSION", "! Insert", SuggestedCategory.NONE);
				alternateItemsDuplication();
				masterItemDuplication();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "! Update", SuggestedCategory.NONE);
				if(((BioBean) focus).hasBeenUpdated("ALTSKU")){
					masterItemDuplication();
				}
			}
			packValidation();
		}

		void alternateItemsDuplication() throws DPException, UserException{
			String query = "SELECT * FROM ALTSKU WHERE (ALTSKU = '"+altItemValue+"') AND (STORERKEY = '"+ownerValue+"')";
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY: "+query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1){
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("ALTSKU");
				parameters[1] = altItemValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_ALTSKU_DUPLICATE", parameters);
			}
		}

		void masterItemDuplication() throws DPException, UserException{
			String query = "SELECT * FROM SKU WHERE (SKU = '"+altItemValue+"') AND (STORERKEY = '"+ownerValue+"')";
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY: "+query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1){
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("ALTSKU");
				parameters[1] = altItemValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_ALTSKU_MASTER", parameters);
			}
		}
	}

	private class SubItemsTab extends Tab{
		private final String itemValue;
		private final String ownerValue;
		private final String subItemValue;
		DataBean parentFocus;

		SubItemsTab(RuntimeFormInterface f, RuntimeFormInterface parent, StateInterface st){
			super(f, st);
			parentFocus = parent.getFocus();
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Substitute Items Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			itemValue = parentFocus.getValue("SKU") == null ? null : parentFocus.getValue("SKU").toString().toUpperCase();
			ownerValue = parentFocus.getValue("STORERKEY") == null ? null : parentFocus.getValue("STORERKEY").toString().toUpperCase();
			subItemValue = focus.getValue("SUBSTITUTESKU") == null ? null : focus.getValue("SUBSTITUTESKU").toString().toUpperCase();
		}

		void run() throws UserException, EpiDataException{
			if(isInsert){
				_log.debug("LOG_DEBUG_EXTENSION", "! Insert", SuggestedCategory.NONE);
				subItemsDuplication();
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "! Update", SuggestedCategory.NONE);
			}
			numericValidation("SEQUENCE");
			itemValidation("SUBSTITUTESKU", "STORERKEY");

			greaterThanZeroValidationForm("ITEMUNITS");
			greaterThanZeroValidationForm("SUBUNITS");

			setConvertedUOMValueIntoFocus("PACKKEY", "UOM", "ITEMUNITS", "QTY");
			setConvertedUOMValueIntoFocus("SUBPACKKEY", "SUBUOM", "SUBUNITS", "SUBQTY");
		}

		void setConvertedUOMValueIntoFocus(String packKeyName, String uomName, String qtyName, String attributeQtyName) throws UserException{
			//Convert from UOM Qty
			String packKey = (String) focus.getValue(packKeyName);
			String fromUom = (String) focus.getValue(uomName);
			Object tempQty = form.getFormWidgetByName(qtyName).getValue();
			String unitQty = null;
			if(isEmpty(tempQty)){
				unitQty = "1"; //default value in db
			}else{
				NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
				NumberFormat nfo = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
				nfo.setGroupingUsed(false);
				Number parsedQty;
				try{
					parsedQty = nf.parse(tempQty.toString());
					_log.debug("LOG_DEBUG_EXTENSION_SubItemsTab", "Parsed Qty is "+parsedQty, SuggestedCategory.NONE);
				}catch(ParseException e){
					e.printStackTrace();
					parsedQty = new Integer(0);
				}
				unitQty = nfo.format(parsedQty);
			}

			String convertedQty = UOMMappingUtil.convertUOMQty(fromUom, UOMMappingUtil.UOM_EA,unitQty, packKey,state,UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			_log.debug("LOG_DEBUG_EXTENSION_SubItemsTab", "Setting "+convertedQty+" into "+attributeQtyName, SuggestedCategory.NONE);
			focus.setValue(attributeQtyName, convertedQty);
		}

		

		void subItemsDuplication() throws DPException, UserException{
			String query = "SELECT * FROM SUBSTITUTESKU WHERE (SKU = '"+itemValue+"') AND (STORERKEY = '"+ownerValue+"') AND (SUBSTITUTESKU = '"+subItemValue+"')";
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY\n"+query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1){
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("SUBSTITUTESKU");
				parameters[1] = subItemValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_SUBSKU_DUPLICATE", parameters);
			}
		}

		void storerValidation(int i) throws DPException, UserException{
			if(verifyStorer(ownerValue, i) == false){
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel("STORERKEY");
				parameters[1] = ownerValue;
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
		}

		void itemValidation(String itemAttribute, String ownerAttribute) throws EpiDataException, UserException{
			if(verifyItemByOwner(itemAttribute, ownerAttribute) == false){
				//throw exception
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel(itemAttribute);
				parameters[1] = focus.getValue(itemAttribute).toString();
				parameters[2] = retrieveLabel(ownerAttribute);
				parameters[3] = focus.getValue(ownerAttribute).toString();
				parameters[4] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_INVALID_ITEM_BY_OWNER", parameters);
			}
		}

		protected void numericValidationForm(String attributeName) throws UserException{
			Object attributeValue = form.getFormWidgetByName(attributeName).getValue();
			if(isEmpty(attributeValue)){
				form.getFormWidgetByName(attributeName).setValue("1");
			}else{
				if((NumericValidationCCF.isNumber(attributeValue.toString()) == false)){
					//throw exception
					String[] parameters = new String[3];
					parameters[0] = retrieveLabel(attributeName);
					parameters[1] = attributeValue.toString();
					parameters[2] = retrieveFormTitle();
					throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);				
				}
			}
		}

		protected void greaterThanZeroValidationForm(String attributeName) throws UserException{
			numericValidationForm(attributeName);
			//if it passes numericValidation, you can parse the number
			Object tempValue = form.getFormWidgetByName(attributeName).getValue();
			if(isNull(tempValue)){
				return;
			}
			String attributeValue = tempValue.toString();
			double value = NumericValidationCCF.parseNumber(attributeValue);
			_log.debug("LOG_DEBUG_EXTENSION", "Value of "+attributeName+" - "+value, SuggestedCategory.NONE);
			if(Double.isNaN(value)){
				String[] parameters = new String[3];
				parameters[0] = colonStrip(retrieveLabel(attributeName));
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}else if(value <= 0){
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_GREATER_THAN_ZERO", parameters);
			}
		}
		
		private String colonStrip(String value){
			Pattern pattern = Pattern.compile("\\:");
			Matcher matcher = pattern.matcher(value);
			return 	matcher.replaceAll("");
		}
	}

	private class WeightDataTab extends Tab{
		Object icwFlagValue;
		Object icdFlagValue;
		Object ocwFlagValue;
		Object ocdFlagValue;
		RuntimeFormInterface inbound;
		RuntimeFormInterface outbound;

		public WeightDataTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Weight Data Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			icwFlagValue = focus.getValue("ICWFLAG");
			icdFlagValue = focus.getValue("ICDFLAG");
			ocwFlagValue = focus.getValue("OCWFLAG");
			ocdFlagValue = focus.getValue("OCDFLAG");

			SlotInterface inboundToggle = form.getSubSlot("inbound receipt");
			inbound = state.getRuntimeForm(inboundToggle, "wm_item_weightdata_inboundreceipts_detail_view");
			SlotInterface outboundToggle = form.getSubSlot("outbound shipments");
			outbound = state.getRuntimeForm(outboundToggle, "wm_item_weightdata_outboundshipments_detail_view");
		}

		void run() throws UserException{
			if(isNull(inbound) || isNull(outbound)){
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Inbound and Outbound are null", SuggestedCategory.NONE);
				return;
			}else{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Inbound "+inbound.getName()+" - Outbound "+outbound.getName(), SuggestedCategory.NONE);
			}
			if(catchWeightChecked){
//				ensureWeightDataTabIsNotNull();
				
				//ICWFLAG
				if(!isUnchecked(icwFlagValue)){
					greaterThanOrEqualToZeroValidation("AVGCASEWEIGHT", inbound);
					greaterThanOrEqualToZeroValidation("TOLERANCEPCT", inbound);
				}
				//ICDFLAG
				if(!isUnchecked(icdFlagValue)){
					String[] attributes = { "ICDLABEL1", "ICDLABEL2", "ICDLABEL3" };
					allAttributesNotEmptyValidation(attributes, inbound);
				}
				//OCWFLAG
				if(!isUnchecked(ocwFlagValue)){
					greaterThanOrEqualToZeroValidation("OAVGCASEWEIGHT", outbound);
					greaterThanOrEqualToZeroValidation("OTOLERANCEPCT", outbound);
				}
				//OCDFLAG
				if(!isUnchecked(ocdFlagValue)){
					String[] attributes = { "OCDLABEL1", "OCDLABEL2", "OCDLABEL3" };
					allAttributesNotEmptyValidation(attributes, outbound);
				}
				
				checkEndToEndFlags();
			}
			
			if(StringUtils.isNull(focus.getValue("FILLQTYUOM"))) {
				focus.setValue("FILLQTYUOM", " ");
			}
		}

		private void checkEndToEndFlags() throws UserException
		{
			String val = form.getFormWidgetByName("SNUM_ENDTOEND").getDisplayValue();
			if(!isEmpty(val)){
				if(val.trim().equalsIgnoreCase("1")){
					
					if(isInsert){
						QBEBioBean qbe = (QBEBioBean)focus;
						qbe.set("ICDFLAG", "1");
						qbe.set("OCDFLAG", "1");
					}else{
						BioBean bio = (BioBean)focus;
						bio.set("ICDFLAG", "1");
						bio.set("OCDFLAG", "1");
					}

					Object icwBY = focus.getValue("ICWBY");
					String icwBYValue = icwBY.toString();
					Object ocwBY = focus.getValue("OCWBY");
					String ocwBYValue = ocwBY.toString();					
					if (isEmpty(icwBY) || isEmpty(ocwBY) || icwBYValue.equalsIgnoreCase(ocwBYValue) == false)
					{
						throw new UserException("WMEXP_ITEM_SERIAL_ENDTOEND_INVALID_CWBY", new Object[]{null});
					}
				}			
			}
		}
		
		private void allAttributesNotEmptyValidation(String[] attributes, RuntimeFormInterface subForm) throws UserException{
			int size = attributes.length;
			int nullCount = 0;
			for(int i = 0; i < size; i++){
				if(isEmpty(focus.getValue(attributes[i]))){
					_log.debug("LOG_DEBUG_EXTENSION", "!@# "+attributes[i]+" is null", SuggestedCategory.NONE);
					nullCount++;
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION", "!@ Size "+size+" # "+nullCount, SuggestedCategory.NONE);
			if(nullCount == size){
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = "";
				for(int i = 0; i < size; i++){
					parameters[0] += removeTrailingColon(retrieveLabel(attributes[i], subForm))+" ";
				}

				parameters[1] = retrieveFormTitle(subForm);
				throw new UserException("WMEXP_ITEM_CW_LABEL", parameters);
			}
		}

		private void ensureWeightDataTabIsNotNull() throws UserException{
			ArrayList catchWeightWidgets = new ArrayList();
			catchWeightWidgets.add(icwFlagValue);
			catchWeightWidgets.add(icdFlagValue);
			catchWeightWidgets.add(ocwFlagValue);
			catchWeightWidgets.add(ocdFlagValue);

			int numberOfNulls = 0;

			for(Iterator it = catchWeightWidgets.iterator(); it.hasNext();){
				Object flag = it.next();
				if(isUnchecked(flag)){
					numberOfNulls++;
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION", "!@ Size "+catchWeightWidgets.size()+" # "+numberOfNulls, SuggestedCategory.NONE);
			if(catchWeightWidgets.size() == numberOfNulls){
				_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@# All Catch Weight Widgets are unchecked, informing user ", SuggestedCategory.NONE);
				String[] parameters = new String[1];
				parameters[0] = retrieveFormTitle();
				throw new UserException("WMEXP_ITEM_CWCHECK", parameters);
			}
		}
	}

	private class CycleCostsTab extends Tab{
		RuntimeFormInterface costs;

		public CycleCostsTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Weight Data Tab Validation - "+form.getName(), SuggestedCategory.NONE);

			SlotInterface costsToggle = form.getSubSlot("wm_item_cyclecosts_costs form slot");
			costs = state.getRuntimeForm(costsToggle, "wm_item_cyclecosts_costs_detail_view");
		}

		public void run() throws UserException{
			if(isNull(costs)){
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Costs is null", SuggestedCategory.NONE);
				return;
			}
			greaterThanOrEqualToZeroValidation("REORDERQTY", costs);
			greaterThanOrEqualToZeroValidation("STDORDERCOST", costs);
			greaterThanOrEqualToZeroValidation("REORDERPOINT", costs);
			greaterThanOrEqualToZeroValidation("PRICE", costs);
			greaterThanOrEqualToZeroValidation("COST", costs);
			greaterThanOrEqualToZeroValidation("CARRYCOST", costs);
		}
	}

//	private class Control2Tab extends Tab{
//		RuntimeFormInterface sgtin;
//
//		public Control2Tab(RuntimeFormInterface f, StateInterface st){
//			super(f, st);
//			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Control 2 Tab Validation - "+form.getName(), SuggestedCategory.NONE);
//
//			SlotInterface costsToggle = form.getSubSlot("wm_item_control2_sgtin form slot");
//			sgtin = state.getRuntimeForm(costsToggle, "wm_item_control2_sgtin");
//		}
//
//		public void run() throws UserException{
//			if(!isNull(sgtin)){
//				//SERIALNUMBERSTART > 0
//				//SERIALNUMBERNEXT > SERIALNUMBERSTART
//				//SERIALNUMBEREND > SERIALNUMBERNEXT
//
//				greaterThanZeroValidation("SERIALNUMBERSTART", sgtin);
//				greaterThanValidation("SERIALNUMBERNEXT", "SERIALNUMBERSTART", sgtin);
//				greaterThanValidation("SERIALNUMBEREND", "SERIALNUMBERNEXT", sgtin);
//				lessThanMaxSerialValidation("SERIALNUMBERSTART", sgtin);
//				lessThanMaxSerialValidation("SERIALNUMBERNEXT", sgtin);
//				lessThanMaxSerialValidation("SERIALNUMBEREND", sgtin);
//			}
//		}
//
//		protected void greaterThanValidation(String attributeName, String otherAttributeName, RuntimeFormInterface subForm) throws UserException{
//			numericValidation(attributeName);
//			//if it passes numericValidation, you can parse the number
//			Object tempValue = focus.getValue(attributeName);
//			if(isNull(tempValue)){
//				return;
//			}
//			String attributeValue = tempValue.toString();
//
//			double otherValue = NumericValidationCCF.parseNumber(focus.getValue(otherAttributeName).toString());
//
//			double value = NumericValidationCCF.parseNumber(attributeValue);
//			if(Double.isNaN(value)){
//				String[] parameters = new String[3];
//				parameters[0] = retrieveLabel(attributeName, subForm);
//				parameters[1] = attributeValue.toString();
//				parameters[2] = retrieveFormTitle(subForm);
//				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
//			}else if(value <= otherValue){
//				String[] parameters = new String[4];
//				parameters[0] = retrieveLabel(attributeName, subForm);
//				parameters[1] = attributeValue.toString();
//				parameters[2] = removeTrailingColon(retrieveLabel(otherAttributeName, subForm).trim());
//				parameters[3] = retrieveFormTitle(subForm);
//				throw new UserException("WMEXP_TAB_GREATER_VALIDATION", parameters);
//			}
//		}
//
//		protected void lessThanMaxSerialValidation(String attributeName, RuntimeFormInterface subForm) throws UserException{
//			numericValidation(attributeName);
//			//if it passes numericValidation, you can parse the number
//			Object tempValue = focus.getValue(attributeName);
//			if(isNull(tempValue)){
//				return;
//			}
//			String attributeValue = tempValue.toString();
//			long otherValue = 274877906943L;
//
//			double value = NumericValidationCCF.parseNumber(attributeValue);
//			if(Double.isNaN(value)){
//				String[] parameters = new String[3];
//				parameters[0] = retrieveLabel(attributeName, subForm);
//				parameters[1] = attributeValue.toString();
//				parameters[2] = retrieveFormTitle();
//				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
//			}else if(value >= otherValue){
//				String[] parameters = new String[4];
//				parameters[0] = retrieveLabel(attributeName, subForm);
//				parameters[1] = attributeValue.toString();
//				parameters[2] = "274877906943";
//				parameters[3] = retrieveFormTitle();
//				throw new UserException("WMEXP_TAB_LESSER_VALIDATION", parameters);
//			}
//		}
//	}

	private class InboundTab extends Tab{
		RuntimeFormInterface what;
		RuntimeFormInterface where;
		RuntimeFormInterface inbound;
		public InboundTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			String fName = form.getName();
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Control 1 Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			
			SlotInterface whatToggle = form.getSubSlot("INBOUNDAUTOMATIC");
			what = state.getRuntimeForm(whatToggle, "wm_item_control1_what");
			
			SlotInterface whereToggle = form.getSubSlot("INBOUNDPUTAWAY");
			where = state.getRuntimeForm(whereToggle, "wm_item_control1_where");
			
			inbound = f;
			
		}

		public void run() throws UserException, EpiDataException{
			if(what != null)
				greaterThanOrEqualToZeroValidation("MaxPalletsPerZone", what);
			putawayLocationValidation(where);
			locValidation("QCLOC", inbound);
			locValidation("RETURNSLOC", inbound);
			
			
			
			//SM 08/15/07 ISSUE #6834 ADDED VALIDATION TO RECEIVING TAB
			if(!isNull(inbound)){
				final String attribute = "RFDEFAULTPACK";
				
				DataBean focus = inbound.getFocus();
				if(!focus.isTempBio()){
					BioBean bio = (BioBean)focus;
					Object value = bio.get(attribute);
					if(value==null){
						String label = retrieveLabel(attribute, inbound);
						String[] parameters = new String[2];
						parameters[0] = "[null]";
						parameters[1] = label.substring(0, label.length()-1);
						throw new UserException("WMEXP_RFDEFAULTPACK_VALIDATION", parameters);
					}
				}
			}
			
			if (!isNull(where)) {
				if (isEmpty(focus.getValue("PUTAWAYCLASS"))) {
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED",
							new Object[] { removeTrailingColon(retrieveLabel(
									"PUTAWAYCLASS", where)) });
				}
			}


		}

		
		protected void greaterThanValidation(String attributeName, String otherAttributeName, RuntimeFormInterface subForm) throws UserException {
			numericValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			Object tempValue = focus.getValue(attributeName);
			if(isNull(tempValue)){
				return;
			}
			String attributeValue = tempValue.toString();
			double otherValue = NumericValidationCCF.parseNumber(focus.getValue(otherAttributeName).toString());
			double value = NumericValidationCCF.parseNumber(attributeValue);
			if(Double.isNaN(value)){
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle(subForm);
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}else if(value <= otherValue){
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = removeTrailingColon(retrieveLabel(otherAttributeName, subForm).trim());
				parameters[3] = retrieveFormTitle(subForm);
				throw new UserException("WMEXP_TAB_GREATER_VALIDATION", parameters);
			}
		}

		protected void lessThanMaxSerialValidation(String attributeName, RuntimeFormInterface subForm) throws UserException {
			numericValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			Object tempValue = focus.getValue(attributeName);
			if (isNull(tempValue)) {
				return;
			}
			String attributeValue = tempValue.toString();
			long otherValue = 274877906943L;

			double value = NumericValidationCCF.parseNumber(attributeValue);
			if(Double.isNaN(value)) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			} else if(value >= otherValue) {
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = "274877906943";
				parameters[3] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_LESSER_VALIDATION", parameters);
			}
		}
		
		private void putawayLocationValidation(RuntimeFormInterface f) throws DPException, UserException{
			String attributeName = "PUTAWAYLOC";
			if(verifyPutawayLocation(attributeName) == false){
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName, f));
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NOT_A_VALID", parameters);
			}
		}

		protected boolean verifyPutawayLocation(String attributeName) throws DPException{
			Object attributeValue = focus.getValue(attributeName);
			if(isEmpty(attributeValue)){
				return true; //Do Nothing
			}
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			String query = "SELECT * FROM LOC WHERE LOC = '"+attributeValue+"' AND (LOCATIONTYPE NOT IN ('PICKTO','STAGED'))";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n"+query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() != 0){
				//value exists, verified
				return true;
			}else{
				//value does not exist
				return false;
			}
		}
	}
	private class OutboundTab extends Tab{
		RuntimeFormInterface outbound;
		public OutboundTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Outbound Tab Validation - "+form.getName(), SuggestedCategory.NONE);
			
			
			outbound = f;
			
		}

		public void run() throws UserException, EpiDataException{
			if(outbound != null){
				greaterThanOrEqualToZeroValidation("DATECODEDAYS", outbound);
				greaterThanZeroValidation("SERIALNUMBERSTART", outbound);
				greaterThanValidation("SERIALNUMBERNEXT", "SERIALNUMBERSTART", outbound);
				greaterThanValidation("SERIALNUMBEREND", "SERIALNUMBERNEXT", outbound);
				lessThanMaxSerialValidation("SERIALNUMBERSTART", outbound);
				lessThanMaxSerialValidation("SERIALNUMBERNEXT", outbound);
				lessThanMaxSerialValidation("SERIALNUMBEREND", outbound);
			}
			locValidation("QCLOCOUT", outbound);
			
//			Amar:11/19/2008:  Start Code change for fixing the issue Machine 2230449 SCM-00000-06095
			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
			String db_type = (userContext.get(SetIntoHttpSessionAction.DB_TYPE)).toString();
			if(focus.getValue("RECEIPTHOLDCODE")==null)	{
				if(db_type.equalsIgnoreCase("MSS"))	{
					focus.setValue("RECEIPTHOLDCODE","");
				} else	{
					focus.setValue("RECEIPTHOLDCODE"," ");
				}
			}
			//Amar:11/19/2008:  End Code change for fixing the issue Machine 2230449 SCM-00000-06095
			endToEnd_OpportunisticValidation();
		}

		private void endToEnd_OpportunisticValidation() throws UserException
		{
			ItemSaveValidationAction.this.endToEnd_OpportunisticValidation(focus);
		}
		
		protected void greaterThanValidation(String attributeName, String otherAttributeName, RuntimeFormInterface subForm) throws UserException {
			numericValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			Object tempValue = focus.getValue(attributeName);
			if(isNull(tempValue)){
				return;
			}
			String attributeValue = tempValue.toString();
			double otherValue = NumericValidationCCF.parseNumber(focus.getValue(otherAttributeName).toString());
			double value = NumericValidationCCF.parseNumber(attributeValue);
			if(Double.isNaN(value)){
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle(subForm);
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			}else if(value <= otherValue){
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = removeTrailingColon(retrieveLabel(otherAttributeName, subForm).trim());
				parameters[3] = retrieveFormTitle(subForm);
				throw new UserException("WMEXP_TAB_GREATER_VALIDATION", parameters);
			}
		}

		protected void lessThanMaxSerialValidation(String attributeName, RuntimeFormInterface subForm) throws UserException {
			numericValidation(attributeName);
			//if it passes numericValidation, you can parse the number
			Object tempValue = focus.getValue(attributeName);
			if (isNull(tempValue)) {
				return;
			}
			String attributeValue = tempValue.toString();
			long otherValue = 274877906943L;

			double value = NumericValidationCCF.parseNumber(attributeValue);
			if(Double.isNaN(value)) {
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NON_NUMERIC", parameters);
			} else if(value >= otherValue) {
				String[] parameters = new String[4];
				parameters[0] = retrieveLabel(attributeName, subForm);
				parameters[1] = attributeValue.toString();
				parameters[2] = "274877906943";
				parameters[3] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_LESSER_VALIDATION", parameters);
			}
		}
		
		private void putawayLocationValidation(RuntimeFormInterface f) throws DPException, UserException{
			String attributeName = "PUTAWAYLOC";
			if(verifyPutawayLocation(attributeName) == false){
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = removeTrailingColon(retrieveLabel(attributeName, f));
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_TAB_NOT_A_VALID", parameters);
			}
		}

		protected boolean verifyPutawayLocation(String attributeName) throws DPException{
			Object attributeValue = focus.getValue(attributeName);
			if(isEmpty(attributeValue)){
				return true; //Do Nothing
			}
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			String query = "SELECT * FROM LOC WHERE LOC = '"+attributeValue+"' AND (LOCATIONTYPE NOT IN ('PICKTO','STAGED'))";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n"+query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() != 0){
				//value exists, verified
				return true;
			}else{
				//value does not exist
				return false;
			}
		}
	}
	private class SerialTab extends Tab{
		public SerialTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
		}
		
		public void run() throws UserException{
			checkDelimiter();
			maskValidation();
			serialIncrementValidation();
			serialLongValidation();
			endToEnd_OpportunisticValidation();
		}

		private void endToEnd_OpportunisticValidation() throws UserException
		{
			ItemSaveValidationAction.this.endToEnd_OpportunisticValidation(focus);
		}

		private void checkDelimiter(){
			String val = form.getFormWidgetByName("SNUM_DELIMITER").getDisplayValue();
			if(!isEmpty(val)){
				if(isInsert){
					QBEBioBean qbe = (QBEBioBean)focus;
					qbe.set("SNUM_POSITION", "0");
				}else{
					BioBean bio = (BioBean)focus;
					bio.set("SNUM_POSITION", "0");
				}
			}
		}
		
		private void maskValidation() throws UserException{
			String val = form.getFormWidgetByName("SNUM_MASK").getDisplayValue();
			if(!isEmpty(val)){
				val = val.toUpperCase();
				Pattern pattern = Pattern.compile("[A-DN]*");
				Matcher matcher = pattern.matcher(val);
				if(matcher.matches()){
					//Input verified, force upper case prior to save		
					if(isInsert){
						QBEBioBean qbe = (QBEBioBean)focus;
						qbe.set("SNUM_MASK", val);
					}else{
						BioBean bio = (BioBean)focus;
						bio.set("SNUM_MASK", val);
					}
				}else{
					//Input invalid, notify user
					throw new UserException("WMEXP_ITEM_SERIAL_MASK_INVALID", new Object[]{null});
				}
			}
		}
		
		private void serialIncrementValidation() throws UserException{
			String autoIncrement = form.getFormWidgetByName("SNUM_AUTOINCREMENT").getDisplayValue();
			String sQty = form.getFormWidgetByName("SNUM_QUANTITY").getDisplayValue();
			String sIncrLength = form.getFormWidgetByName("SNUM_INCR_LENGTH").getDisplayValue();
			String sIncrPosition = form.getFormWidgetByName("SNUM_INCR_POS").getDisplayValue();

			if (isEmpty(autoIncrement))
			{
				autoIncrement = "0"; // Fix the case where autoIncrement == "false" instead of "0".
			}
			else
			{
				if (autoIncrement == "true")
					autoIncrement = "1"; // Fix the case where autoIncrement == "true" instead of "1".
				if (autoIncrement == "false")
					autoIncrement = "0"; // Fix the case where autoIncrement == "false" instead of "0".
			}
			if (isEmpty(sIncrLength))
				sIncrLength = "0";
			if (isEmpty(sIncrPosition))
				sIncrPosition = "0";
			if (isEmpty(sQty))
				sQty = "0";
			
			if (sQty.equalsIgnoreCase("0"))
			{
				if(isInsert){
					QBEBioBean qbe = (QBEBioBean)focus;
					qbe.set("SNUM_QUANTITY", sQty);
				}else{
					BioBean bio = (BioBean)focus;
					bio.set("SNUM_QUANTITY", sQty);
				}
			}
			int qty = Integer.parseInt(sQty);
			if(autoIncrement.equalsIgnoreCase("1"))
			{
				if(!(qty > 1))
					throw new UserException("WMEXP_ITEM_SERIAL_AUTOINCR_ENABLED_QTY", new Object[]{null});
			}
			else
			{
				if(isInsert){
					QBEBioBean qbe = (QBEBioBean)focus;
					qbe.set("SNUM_QUANTITY", "1");
				}else{
					BioBean bio = (BioBean)focus;
					bio.set("SNUM_QUANTITY", "1");
				}
			}
			
			int incrLength = Integer.parseInt(sIncrLength);
			int incrPosition = Integer.parseInt(sIncrPosition);
			if (incrLength > 0 && incrPosition < 1)
				throw new UserException("WMEXP_ITEM_SERIAL_INCRLEN_INVALID_POS", new Object[]{null});			
		}
		
		private void serialLongValidation() throws UserException{
			String temp = form.getFormWidgetByName("SNUMLONG_FIXED").getDisplayValue()==null ? "0" : form.getFormWidgetByName("SNUMLONG_FIXED").getDisplayValue();
			int valueLong = Integer.parseInt(temp);
			if(valueLong>0){
				temp = form.getFormWidgetByName("SNUM_LENGTH").getDisplayValue()==null ? "0" : form.getFormWidgetByName("SNUM_LENGTH").getDisplayValue();
				int valueLength = Integer.parseInt(temp);
				if(valueLength>valueLong){
					String[] params = new String[2];
					params[0] = removeTrailingColon(retrieveLabel("SNUMLONG_FIXED"));
					params[1] = removeTrailingColon(retrieveLabel("SNUM_LENGTH"));
					throw new UserException("Greater_Than_Validation", params);
				}
			}
		}
	}
	
	private class ShippingTab extends Tab{
		//SM 08/15/07 ISSUE #6834 ADDED VALIDATION TO RECEIVING TAB
		RuntimeFormInterface receivingForm;
		//SM 08/15/07 END UPDATE
		RuntimeFormInterface shippingForm;

		public ShippingTab(RuntimeFormInterface f, StateInterface st){
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%%\n\t Start of Receiving Tab Validation - "+form.getName(), SuggestedCategory.NONE);
		
			//SM 08/15/07 ISSUE #6834 ADDED VALIDATION TO RECEIVING TAB	
//			SlotInterface receivingSlot = form.getSubSlot("wm_item_receivinginfo_detail_view");
//			receivingForm = state.getRuntimeForm(receivingSlot, "wm_item_receivinginfo_general_detail_view");
			//SM 08/15/07 END UPDATE
			SlotInterface shippingSlot = form.getSubSlot("wm_item_shippinginfo_detail_view");
			shippingForm = state.getRuntimeForm(shippingSlot, "wm_item_shippinginfo_detail_view");
		}

		public void run() throws UserException, EpiDataException{
			//SM 08/15/07 ISSUE #6834 ADDED VALIDATION TO RECEIVING TAB
			/*			if(!isNull(receivingForm)){
				final String attribute = "RFDEFAULTPACK";
				
				DataBean focus = receivingForm.getFocus();
				if(!focus.isTempBio()){
					BioBean bio = (BioBean)focus;
					Object value = bio.get(attribute);
					if(value==null){
						String label = retrieveLabel(attribute, receivingForm);
						String[] parameters = new String[2];
						parameters[0] = "[null]";
						parameters[1] = label.substring(0, label.length()-1);
						throw new UserException("WMEXP_RFDEFAULTPACK_VALIDATION", parameters);
					}
				}

				
			}*/
			hazmatCodeValidation(shippingForm);
			//SM 08/15/07 END UPDATE
		}
		
		void hazmatCodeValidation(RuntimeFormInterface f) throws EpiDataException, UserException {
			String attributeName = "HAZMATCODESKEY";
			String table = "HAZMATCODES";
			if(verifySingleAttribute(attributeName, table) == false) {
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName,f);
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
			}
		}
	}
	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void endToEnd_OpportunisticValidation(DataBean focus) throws UserException
	{
		String flowThruItem = BioAttributeUtil.getString(focus, "FLOWTHRUITEM");
		int snumEndToEnd = BioAttributeUtil.getInt(focus, "SNUM_ENDTOEND");
		if ("Y".equalsIgnoreCase(flowThruItem) && 1 == snumEndToEnd)
		{
			//throw error
			_log.error("LOG_ERROR_EXTENSION_endToEnd_OpportunisticValidation", "Not possible to have flowThruItem and snumEndToEnd on", SuggestedCategory.NONE);
			throw new UserException("WMEXP_ITEM_ENDTOEND_OPP", new Object[] {});
		}
	}
}