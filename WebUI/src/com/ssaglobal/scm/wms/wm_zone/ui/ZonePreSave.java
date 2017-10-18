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
package com.ssaglobal.scm.wms.wm_zone.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class ZonePreSave extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ZonePreSave.class);
	//Static form reference variables
	private static final String SHELL_SLOT = "list_slot_2";
	private static final String BLANK = "Blank";
	private static final String TAB_GROUP_SLOT = "tbgrp_slot";
	private static final String TAB_ZERO = "tab 0";
	private static final String TAB_TWO = "tab 2";

	//	private static final String TAB_THREE = "tab 3";
	private static final String TAB_FOUR = "tab 4";
	private static final String TAB_FIVE = "tab 5";
	private static final String TAB_DETAIL = "Detail";
	private static final String EFZ_SLOT = "wm_zone_equipment_exclude_toggle_slot";

	//Static table names
	private static final String ZONE_TABLE = "wm_zone";
	private static final String PA_ZONE_TABLE = "wm_pazoneequipmentexcludedetail";
	private static final String LOCATION_TABLE = "wm_location";

	//Static attribute names
	private static final String ZONE = "PUTAWAYZONE";
	private static final String EQUIP_PROF = "EQUIPMENTPROFILEKEY";
	private static final String REPLEN_METH = "REPLENISHMENTMETHOD";

	//	private static final String AISLE_START = "AISLESTART";
	//	private static final String AISLE_END = "AISLEEND";
	//	private static final String SLOT_START = "SLOTSTART";
	//	private static final String SLOT_END = "SLOTEND";
	private static final String MAX_PICK_LINES = "MAXPICKLINES";
	private static final String MAX_PICK_CONTAIN = "MAXPICKINGCONTAINERS";
	private static final String LABOR_MAX_CC = "LABORMAXCASECNT";
	private static final String LABOR_MAX_CUBE = "LABORMAXCUBE";
	private static final String LABOR_MAX_WEIGHT = "LABORMAXWEIGHT";
	private static final String MPPI = "MaxPalletsPerSku";
	private static final String QTYCC = "QTYCC";
	private static final String MAX_CUBE = "MAXCUBE";
	private static final String MAX_WEIGHT = "MAXWEIGHT";
	private static final String MAX_CC = "MAXCASECNT";
	private static final String IN_LOCATION = "INLOC";
	private static final String OUT_LOCATION = "OUTLOC";
	private static final String PT_LOCATION = "PICKTOLOC";
	private static final String LOCATION = "LOC";
	private static final String LOCATION_TYPE = "LOCATIONTYPE";
//	private static final String EFZ_LINK = "EQUIPMENT_EXCLUDE_DETAIL";

	//Static error message names
	private static final String ERROR_MESSAGE_NO_SAVE = "WMEXP_NO_SAVE_AVAILABLE";
	private static final String ERROR_MESSAGE_DUPE_KEY = "WMEXP_CCPS_EXISTS";
	private static final String ERROR_MESSAGE_PICK_TO_REQ = "WMEXP_ZONE_PICKTOREQ";
	private static final String ERROR_MESSAGE_INLOC_REQ = "WMEXP_ZONE_INLOCREQ";
	private static final String ERROR_MESSAGE_PICK_TO_STAGED = "WMEXP_ZONE_LOCTYPEPTS";
	private static final String ERROR_MESSAGE_PICK_AND_DROP = "WMEXP_ZONE_LOCTYPEPND";
	private static final String ERROR_MESSAGE_NONINT = "WMEXP_NON_INTEGER";
	private static final String ERROR_MESSAGE_LTZ = "WMEXP_LESS_THAN_ZERO";
	private static final String ERROR_MESSAGE_NGTZ = "WMEXP_NOT_GREATER_THAN_ZERO";
	private static final String ERROR_MESSAGE_LTE = "WMEXP_ZONE_LESSTHANEXPECTED";
	private static final String ERROR_MESSAGE_INVALID_EFZ = "WMEXP_ZONE_INVALID_EFZ";
	private static final String ERROR_MESSAGE_EFZ_DUPE_KEY = "WMEXP_ZONE_EFZ_DUPLICATE";
	private static final String TAB_SIX = "tab 6";
	private static final String ZONESTD_SLOT = "wm_zone_zonestds_toggle_view";
	private static final String ZONESTD_SLOT_TAB_DETAIL = "wm_zone_zonestds_detail_tab";
	
	
//	private static final String UNCHECKED = "0";

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		
		
		
		StateInterface state = context.getState();
		String queryString = null;
		Query qry = null;
		BioCollectionBean testList = null;
		String[] parameter = new String[1];
		String[] parameters = new String[2];
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface tabForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT), null);
		if (tabForm.getName().equals(BLANK)){
			throw new FormException(ERROR_MESSAGE_NO_SAVE, null);
		}
		SlotInterface tabSlot = tabForm.getSubSlot(TAB_GROUP_SLOT);
		RuntimeFormInterface detailTabForm = state.getRuntimeForm(tabSlot, TAB_ZERO);
		DataBean parentFocus = detailTabForm.getFocus();
		RuntimeFormInterface toggleForm = state.getRuntimeForm(tabSlot, TAB_FIVE);
		RuntimeFormInterface childForm = state.getRuntimeForm(toggleForm.getSubSlot(EFZ_SLOT), TAB_DETAIL);
		
		
		RuntimeFormInterface zonestdsToggleForm = state.getRuntimeForm(tabSlot,TAB_SIX);
		RuntimeFormInterface zonestdsChildForm = state.getRuntimeForm(zonestdsToggleForm.getSubSlot(ZONESTD_SLOT), ZONESTD_SLOT_TAB_DETAIL);

		
		
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		//Validate data
		//Verify unique primary keys
		if (parentFocus.isTempBio()){
			String keyField = detailTabForm.getFormWidgetByName(ZONE).getDisplayValue();
			keyField = keyField == null ? null : keyField.toUpperCase();
			queryString = ZONE_TABLE + "." + ZONE + "='" + keyField + "'";
			qry = new Query(ZONE_TABLE, queryString, null);
			_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
			testList = uowb.getBioCollectionBean(qry);
			if (testList.size() != 0){
				parameter[0] = colonStrip(readLabel(detailTabForm.getFormWidgetByName(ZONE)));
				throw new FormException(ERROR_MESSAGE_DUPE_KEY, parameter);
			}
		}

		if (childForm != null){
			if (!childForm.getName().equals(BLANK) && childForm.getFocus() != null){
				if (childForm.getFocus().isTempBio()){
					String zone = childForm.getFormWidgetByName(ZONE).getDisplayValue();
					zone = zone == null ? null : zone.toString().toUpperCase();
					String equip = ((QBEBioBean) childForm.getFocus()).getValue(EQUIP_PROF).toString();
					queryString = PA_ZONE_TABLE + "." + ZONE + "='" + zone + "' and " + PA_ZONE_TABLE + "."
							+ EQUIP_PROF + "='" + equip + "'";
					qry = new Query(PA_ZONE_TABLE, queryString, null);
					_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
					testList = uowb.getBioCollectionBean(qry);
					if (testList.size() != 0){
						parameter[0] = colonStrip(readLabel(childForm.getFormWidgetByName(ZONE))) + ", "
								+ colonStrip(readLabel(childForm.getFormWidgetByName(EQUIP_PROF)));
						throw new FormException(ERROR_MESSAGE_EFZ_DUPE_KEY, parameter);
					}
				}
			}
		}
		
		
		//for zonestds
		DataBean zonestdsChildFocus = null;
		try{
			zonestdsChildFocus = zonestdsChildForm.getFocus();
		}catch(NullPointerException e){
			zonestdsChildFocus = null;
		}
		if(zonestdsChildForm == null)
			_log.debug("LOG_SYSTEM_OUT","zonestdsChildForm**** is null",100L);
		else
			_log.debug("LOG_SYSTEM_OUT","zonestdsChildForm**** is not null",100L);
		
		
/*		if(zonestdsChildFocus != null){
			String isCS = zonestdsChildFocus.getValue("CALCSTDS").toString();
			String isDS = zonestdsChildFocus.getValue("DISPLAYSTDS").toString();
			String isDA = zonestdsChildFocus.getValue("DISPLAYACTUAL").toString();
			String isDP = zonestdsChildFocus.getValue("DISPLAYPERF").toString();
			if(UNCHECKED.equalsIgnoreCase(isCS) && 
					UNCHECKED.equalsIgnoreCase(isDS) &&
					UNCHECKED.equalsIgnoreCase(isDA) &&
					UNCHECKED.equalsIgnoreCase(isDP)){
					UserException UsrExcp = new UserException("WMEXP_ZONESTD_TASK_DUPLICATE", new Object[]{});
	 	   			throw UsrExcp;
			}
		}
*/
		//check tasktype duplication*****************************
		boolean isTaskZoneDuplicate = false;
		if(zonestdsChildFocus != null && zonestdsChildFocus.isTempBio()){
			if(!parentFocus.isTempBio()){
				String zone = "";
				String taskType = "";
				try{
					BioBean savedBean = (BioBean)parentFocus;
					zone = savedBean.getString("PUTAWAYZONE").toString();
					taskType = zonestdsChildFocus.getValue("TASKTYPE").toString();
					isTaskZoneDuplicate = this.isStdDuplicate(context, zone, taskType);
				}catch(EpiException e){
					e.printStackTrace();
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});					
				}
				if(isTaskZoneDuplicate){
						UserException UsrExcp = new UserException("WMEXP_ZONESTD_TASK_DUPLICATE", new Object[]{});
			 	   		throw UsrExcp;
				}
			}
		}
		
		
		//end of checking duplication *****************************
		
		
		

		//Verify numerical values
		String errorText = "", errorTextNN = "";
		RuntimeFormInterface rmTabForm = state.getRuntimeForm(tabSlot, TAB_TWO);
		//		RuntimeFormInterface lsTabForm = state.getRuntimeForm(tabSlot, TAB_THREE);
		RuntimeFormInterface arTabForm = state.getRuntimeForm(tabSlot, TAB_FOUR);
		//		RuntimeFormWidgetInterface aisleStart = lsTabForm.getFormWidgetByName(AISLE_START);
		//		RuntimeFormWidgetInterface aisleEnd = lsTabForm.getFormWidgetByName(AISLE_END);
		//		RuntimeFormWidgetInterface slotStart = lsTabForm.getFormWidgetByName(SLOT_START);
		//		RuntimeFormWidgetInterface slotEnd = lsTabForm.getFormWidgetByName(SLOT_END);
		RuntimeFormWidgetInterface mpl = arTabForm.getFormWidgetByName(MAX_PICK_LINES);
		RuntimeFormWidgetInterface mpc = arTabForm.getFormWidgetByName(MAX_PICK_CONTAIN);
		RuntimeFormWidgetInterface lmcc = arTabForm.getFormWidgetByName(LABOR_MAX_CC);
		RuntimeFormWidgetInterface lmc = arTabForm.getFormWidgetByName(LABOR_MAX_CUBE);
		RuntimeFormWidgetInterface lmw = arTabForm.getFormWidgetByName(LABOR_MAX_WEIGHT);

		errorTextNN = nonNegative(detailTabForm.getFormWidgetByName(MPPI), errorTextNN);
		errorTextNN = nonNegative(detailTabForm.getFormWidgetByName(QTYCC), errorTextNN);
		errorTextNN = nonNegative(rmTabForm.getFormWidgetByName(MAX_CUBE), errorTextNN);
		errorTextNN = nonNegative(rmTabForm.getFormWidgetByName(MAX_WEIGHT), errorTextNN);
		errorTextNN = nonNegative(rmTabForm.getFormWidgetByName(MAX_CC), errorTextNN);
		//		errorText = greaterThanZero(aisleStart, errorText);
		//		errorText = greaterThanValue(aisleEnd, aisleStart, errorText);
		//		errorText = greaterThanZero(slotStart, errorText);
		//		errorText = greaterThanValue(slotEnd, slotStart, errorText);
		if (mpl != null){
			if (!(mpl.equals(""))){
				errorTextNN = nonNegative(mpl, errorTextNN);
			}
		}
		if (mpc != null){
			if (!(mpc.equals(""))){
				errorTextNN = nonNegative(mpc, errorTextNN);
			}
		}
		if (lmcc != null){
			if (!(lmcc.equals(""))){
				errorTextNN = nonNegative(lmcc, errorTextNN);
			}
		}
		if (lmc != null){
			if (!(lmc.equals(""))){
				errorTextNN = nonNegative(lmc, errorTextNN);
			}
		}
		if (lmw != null){
			if (!(lmw.equals(""))){
				errorTextNN = nonNegative(lmw, errorTextNN);
			}
		}
		if (!(errorTextNN.equals(""))){
			parameter[0] = errorTextNN;
			throw new FormException(ERROR_MESSAGE_LTZ, parameter);
		}
		if (!(errorText.equals(""))){
			parameter[0] = errorText;
			throw new FormException(ERROR_MESSAGE_NGTZ, parameter);
		}

		//Verify locations
		RuntimeFormWidgetInterface inLocation = detailTabForm.getFormWidgetByName(IN_LOCATION);
		RuntimeFormWidgetInterface outLocation = detailTabForm.getFormWidgetByName(OUT_LOCATION);
		RuntimeFormWidgetInterface defaultPickTo = detailTabForm.getFormWidgetByName(PT_LOCATION);
		//_log.debug("LOG_SYSTEM_OUT","Current replenishment method: "+rmTabForm.getFormWidgetByName(REPLEN_METH).getValue(),100L);
		if (rmTabForm.getFormWidgetByName(REPLEN_METH).getValue() != null){
			if (rmTabForm.getFormWidgetByName(REPLEN_METH).getValue().equals("1")
					&& (inLocation.getDisplayValue() == null || inLocation.getDisplayValue().equals(""))){
				throw new FormException(ERROR_MESSAGE_INLOC_REQ, null);
			}
		}
		if (inLocation.getDisplayValue() == null && outLocation.getDisplayValue() == null
				&& defaultPickTo.getDisplayValue() == null)	{
			throw new FormException(ERROR_MESSAGE_PICK_TO_REQ, null);
		}
		if (defaultPickTo.getDisplayValue() != null){
			queryString = LOCATION_TABLE + "." + LOCATION + "='" + defaultPickTo.getDisplayValue().toUpperCase() + "' and ("
					+ LOCATION_TABLE + "." + LOCATION_TYPE + "='PICKTO' or " + LOCATION_TABLE + "." + LOCATION_TYPE
					+ "='STAGED')";
			qry = new Query(LOCATION_TABLE, queryString, null);
			_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
			testList = uowb.getBioCollectionBean(qry);
			if (testList != null){
				if (testList.size() == 0){
					parameter[0] = colonStrip(readLabel(defaultPickTo));
					throw new FormException(ERROR_MESSAGE_PICK_TO_STAGED, parameter);
				}
			}
		}
		
		if (inLocation.getDisplayValue() != null){
			//SM 9/19/07 ISSUE 7258: Empty inLocation values now shown as single space according to ISSUE 7082, updated logic accordingly
			if (StringUtils.isEmpty(inLocation.getDisplayValue())) {
				inLocation.setDisplayValue(" ");
			}			
			if(!(inLocation.getDisplayValue().equals(" "))){
				//SM 9/19/07 End update
				queryString = LOCATION_TABLE + "." + LOCATION + "='" + inLocation.getDisplayValue().toUpperCase() + "' and "
				+ LOCATION_TABLE + "." + LOCATION_TYPE + "='PND'";
				qry = new Query(LOCATION_TABLE, queryString, null);
				_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
				testList = uowb.getBioCollectionBean(qry);
				if (testList != null){
					if (testList.size() == 0){
						parameters[0] = colonStrip(readLabel(inLocation));
						parameters[1] = detailTabForm.getFormWidgetByName(ZONE).getDisplayValue();
						throw new FormException(ERROR_MESSAGE_PICK_AND_DROP, parameters);
					}
				}	
			}
		}
		//SM 9/19/07 ISSUE 7258: Empty outLocation values now shown as single space according to ISSUE 7082, updated logic accordingly
		if (outLocation.getDisplayValue() != null){
			if (StringUtils.isEmpty(outLocation.getDisplayValue())) {
				outLocation.setDisplayValue(" ");
			}	
			if(!(outLocation.getDisplayValue().equals(" "))){
			//SM 9/19/07 End update
				queryString = LOCATION_TABLE + "." + LOCATION + "='" + outLocation.getDisplayValue().toUpperCase() + "' and "
						+ LOCATION_TABLE + "." + LOCATION_TYPE + "='PND'";
				qry = new Query(LOCATION_TABLE, queryString, null);
				_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
				testList = uowb.getBioCollectionBean(qry);
				if (testList != null){
					if (testList.size() == 0){
						parameters[0] = colonStrip(readLabel(outLocation));
						parameters[1] = detailTabForm.getFormWidgetByName(ZONE).getDisplayValue();
						throw new FormException(ERROR_MESSAGE_PICK_AND_DROP, parameters);
					}
				}
			}
		}
			
		return RET_CONTINUE;
	}

	public void isValidZone(RuntimeFormInterface form, UnitOfWorkBean uowb) throws EpiException {
		String[] parameter = new String[1];
		RuntimeFormWidgetInterface widget = form.getFormWidgetByName(ZONE);
		String widgetDisplayValue = widget.getDisplayValue() == null ? null : widget.getDisplayValue().toUpperCase();
		String queryString = ZONE_TABLE + "." + ZONE + "='" + widgetDisplayValue + "'";
		Query qry = new Query(ZONE_TABLE, queryString, null);
		_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if (list.size() != 1){
			parameter[0] = colonStrip(readLabel(widget));
			throw new FormException(ERROR_MESSAGE_INVALID_EFZ, parameter);
		}
	}

	public String nonNegative(RuntimeFormWidgetInterface widget, String text) throws FormException {
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		if (number != null){
			number = commaStrip(number);
		}
		if (widget.getAttributeType() == BioAttributeTypes.INT_TYPE){
			if (number != null){
				if (number.matches(".*\\..*")){
					parameter[0] = widgetLabel;
					throw new FormException(ERROR_MESSAGE_NONINT, parameter);
				}
				int value = Integer.parseInt(number);
				if (value < 0){
					text = addLabel(text, widgetLabel);
				}
			}
		}else{
			if (number != null){
				float value = Float.parseFloat(number);
				if (value < 0){
					text = addLabel(text, widgetLabel);
				}
			}
		}
		return text;
	}

	public String greaterThanValue(RuntimeFormWidgetInterface widget, RuntimeFormWidgetInterface widget2, String text) throws FormException {
		String[] parameter = new String[2];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		String number2 = widget2.getDisplayValue();
		if (number != null){
			if (number.matches(".*\\..*")){
				parameter[0] = widgetLabel;
				throw new FormException(ERROR_MESSAGE_NONINT, parameter);
			}
			int value = Integer.parseInt(number);
			int compareTo = Integer.parseInt(number2);
			if (value < compareTo){
				parameter[0] = widgetLabel;
				parameter[1] = colonStrip(readLabel(widget2));
				throw new FormException(ERROR_MESSAGE_LTE, parameter);
			}
			if (value < 0){
				text = addLabel(text, widgetLabel);
			}
		}
		return text;
	}

	//Validate value is greater than zero
	public String greaterThanZero(RuntimeFormWidgetInterface widget, String text) throws FormException {
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();

		if (widget.getAttributeType() == BioAttributeTypes.INT_TYPE) {
			//Widget value is of type INT
			if (number != null){
				if (number.matches(".*\\..*")){
					//Input value has decimals
					parameter[0] = widgetLabel;
					throw new FormException(ERROR_MESSAGE_NONINT, parameter);
				}
				int value = Integer.parseInt(number);
				if (value <= 0){
					text = addLabel(text, widgetLabel);
				}
			}
		}else{
			//Widget value allows decimals
			if (number != null){
				float value = Float.parseFloat(number);
				if (value <= 0){
					text = addLabel(text, widgetLabel);
				}
			}
		}
		return text;
	}

	//Remove colon from widget labels for error message
	public String colonStrip(String label) {
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return matcher.replaceAll("");
	}

	//Remove commas from Strings containing numerical values
	public String commaStrip(String number) {
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try	{
			numberS = nf.parse(number).toString();
		} catch (ParseException e){
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}

	//Read Widget Label for error message
	public String readLabel(RuntimeFormWidgetInterface widget) {
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label", locale);
	}
	
	//Append widget label for error message
	public String addLabel(String text, String widgetLabel){
		if (text.equals("")){
			text += widgetLabel;
		}else{
			text += ", " + widgetLabel;
		}
		return text;
	}
	
	boolean isStdDuplicate(ActionContext context, String zone, String taskType) throws EpiDataException{
		StateInterface state = context.getState();
 		Query qry = new Query("wm_zonestds", "wm_zonestds.PUTAWAYZONE='"+zone+"' AND wm_zonestds.TASKTYPE='"+taskType+"'", null);
 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
		int size = resultCollection.size();
		if(size == 0){//insert
			return false;
		}else{//update
			return true;
		}
	}
}