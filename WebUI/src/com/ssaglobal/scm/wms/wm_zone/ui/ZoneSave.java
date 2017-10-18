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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
//import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.data.bio.BioAttributeTypes;
//import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class ZoneSave extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ZoneSave.class);
	
	//Static form reference variables
	private static final String SHELL_SLOT = "list_slot_2";
	private static final String BLANK = "Blank";
	private static final String TAB_GROUP_SLOT = "tbgrp_slot";
	private static final String TAB_ZERO = "tab 0";
//	private static final String TAB_TWO = "tab 2";
//	private static final String TAB_THREE = "tab 3";
//	private static final String TAB_FOUR = "tab 4";
	private static final String TAB_FIVE = "tab 5";
	private static final String TAB_SIX = "tab 6";
	private static final String TAB_DETAIL = "Detail";
	private static final String EFZ_SLOT = "wm_zone_equipment_exclude_toggle_slot";
	private static final String ZONESTD_SLOT = "wm_zone_zonestds_toggle_view";
	private static final String ZONESTD_SLOT_TAB_DETAIL = "wm_zone_zonestds_detail_tab";
	
	//Static table names
	private static final String ZONE_TABLE = "wm_zone";
//	private static final String PA_ZONE_TABLE = "wm_pazoneequipmentexcludedetail";
//	private static final String LOCATION_TABLE = "wm_location";
	
	//Static attribute names
	private static final String ZONE = "PUTAWAYZONE";
//	private static final String EQUIP_PROF = "EQUIPMENTPROFILEKEY";
//	private static final String REPLEN_METH = "REPLENISHMENTMETHOD";
//	private static final String AISLE_START = "AISLESTART";
//	private static final String AISLE_END = "AISLEEND";
//	private static final String SLOT_START = "SLOTSTART";
//	private static final String SLOT_END = "SLOTEND";
//	private static final String MAX_PICK_LINES = "MAXPICKLINES";
//	private static final String MAX_PICK_CONTAIN = "MAXPICKINGCONTAINERS";
//	private static final String LABOR_MAX_CC = "LABORMAXCASECNT";
//	private static final String LABOR_MAX_CUBE = "LABORMAXCUBE";
//	private static final String LABOR_MAX_WEIGHT = "LABORMAXWEIGHT";
//	private static final String MPPI = "MaxPalletsPerSku";
//	private static final String QTYCC = "QTYCC";
//	private static final String MAX_CUBE = "MAXCUBE";
//	private static final String MAX_WEIGHT = "MAXWEIGHT";
//	private static final String MAX_CC = "MAXCASECNT";
	private static final String IN_LOCATION = "INLOC";
	private static final String OUT_LOCATION = "OUTLOC";
//	private static final String PT_LOCATION = "PICKTOLOC";
//	private static final String LOCATION = "LOC";
//	private static final String LOCATION_TYPE = "LOCATIONTYPE";
	private static final String EFZ_LINK = "EQUIPMENT_EXCLUDE_DETAIL";
	private static final String ZONESTDS_LINK = "ZONESTDS";
	
	
	//Static error message names
	private static final String ERROR_MESSAGE_NO_SAVE = "WMEXP_NO_SAVE_AVAILABLE";
//	private static final String ERROR_MESSAGE_DUPE_KEY = "WMEXP_CCPS_EXISTS";
//	private static final String ERROR_MESSAGE_PICK_TO_REQ = "WMEXP_ZONE_PICKTOREQ";
//	private static final String ERROR_MESSAGE_INLOC_REQ = "WMEXP_ZONE_INLOCREQ";
//	private static final String ERROR_MESSAGE_PICK_TO_STAGED = "WMEXP_ZONE_LOCTYPEPTS";
//	private static final String ERROR_MESSAGE_PICK_AND_DROP = "WMEXP_ZONE_LOCTYPEPND";
	private static final String ERROR_MESSAGE_NONINT = "WMEXP_NON_INTEGER";
//	private static final String ERROR_MESSAGE_LTZ = "WMEXP_LESS_THAN_ZERO";
//	private static final String ERROR_MESSAGE_NGTZ = "WMEXP_NOT_GREATER_THAN_ZERO";
	private static final String ERROR_MESSAGE_LTE = "WMEXP_ZONE_LESSTHANEXPECTED";
	private static final String ERROR_MESSAGE_INVALID_EFZ = "WMEXP_ZONE_INVALID_EFZ";
//	private static final String ERROR_MESSAGE_EFZ_DUPE_KEY = "WMEXP_ZONE_EFZ_DUPLICATE";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		StateInterface state = context.getState();
//		String queryString = null;
//		Query qry = null;
//		BioCollectionBean testList = null;
//		String[] parameter = new String[1];
//		String[] parameters = new String[2];
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface tabForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT), null);
		if(tabForm.getName().equals(BLANK)){
			throw new FormException(ERROR_MESSAGE_NO_SAVE, null);
		}
		SlotInterface tabSlot = tabForm.getSubSlot(TAB_GROUP_SLOT);
		RuntimeFormInterface detailTabForm = state.getRuntimeForm(tabSlot,TAB_ZERO);
		DataBean parentFocus = detailTabForm.getFocus();
		RuntimeFormInterface toggleForm = state.getRuntimeForm(tabSlot,TAB_FIVE);
		RuntimeFormInterface zonestdsToggleForm = state.getRuntimeForm(tabSlot,TAB_SIX);
		RuntimeFormInterface childForm = state.getRuntimeForm(toggleForm.getSubSlot(EFZ_SLOT), TAB_DETAIL);
		RuntimeFormInterface zonestdsChildForm = state.getRuntimeForm(zonestdsToggleForm.getSubSlot(ZONESTD_SLOT), ZONESTD_SLOT_TAB_DETAIL);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();


		//Verify locations
		RuntimeFormWidgetInterface inLocation = detailTabForm.getFormWidgetByName(IN_LOCATION);
		RuntimeFormWidgetInterface outLocation = detailTabForm.getFormWidgetByName(OUT_LOCATION);
//_log.debug("LOG_SYSTEM_OUT","***toggleformname="+zonestdsToggleForm.getName()+"    childformname="+zonestdsChildForm.getName(),100L);
		
		//Perform save		
		DataBean childFocus = null;
		try{
			childFocus = childForm.getFocus();
		}catch(NullPointerException e){
			childFocus = null;
		}

		
		//for zonestds
		DataBean zonestdsChildFocus = null;
		try{
			zonestdsChildFocus = zonestdsChildForm.getFocus();
		}catch(NullPointerException e){
			zonestdsChildFocus = null;
		}

		
		
		try
		{
			BioBean savedBean = null;
			if(parentFocus.isTempBio()){
				savedBean = uowb.getNewBio((QBEBioBean)parentFocus);
				if(inLocation.getDisplayValue()==null){
//					SM 08/29/07 ISSUE 7082: Unable to save Zone in Oracle (Added space in quotes)
					savedBean.set(IN_LOCATION, " ");
//					SM 08/29/07 End Edit
				}
				if(outLocation.getDisplayValue()==null){
//					SM 08/29/07 ISSUE 7082: Unable to save Zone in Oracle (Added space in quotes)
					savedBean.set(OUT_LOCATION, " ");
//					SM 08/29/07 End Edit
				}
				if(childFocus!=null){
					if(!(detailTabForm.getFormWidgetByName(ZONE).getDisplayValue().equals(childForm.getFormWidgetByName(ZONE).getDisplayValue()))){
						isValidZone(childForm, uowb);
					}
					savedBean.addBioCollectionLink(EFZ_LINK, (QBEBioBean)childFocus);
				}
				
				//for zonestds
				if(zonestdsChildFocus!=null){
					savedBean.addBioCollectionLink(ZONESTDS_LINK, (QBEBioBean)zonestdsChildFocus);
				}

			
			}else{
				savedBean = (BioBean)parentFocus;
				if(childFocus!=null){
					isValidZone(childForm, uowb);
					if(childFocus.isTempBio()){
						savedBean.addBioCollectionLink(EFZ_LINK, (QBEBioBean)childFocus);
					}
				}
				if(zonestdsChildFocus!=null){
					if(zonestdsChildFocus.isTempBio()){
						savedBean.addBioCollectionLink(ZONESTDS_LINK, (QBEBioBean)zonestdsChildFocus);
					}
				}
				
				//RM 07/09/2008 MACHINE: 2060187 SDIS: SCM-00000-05178 
				if(inLocation.getDisplayValue()==null){ 
					savedBean.set(IN_LOCATION, " ");
				}
				if(outLocation.getDisplayValue()==null){
					savedBean.set(OUT_LOCATION, " ");
				}
				//end RM edit 07/09/2008
			}
			uowb.saveUOW(true);
			uowb.clearState();
			result.setFocus(savedBean);
		}
		catch (UnitOfWorkException e)
		{

			_log.error("LOG_ERROR_EXTENSION_ZoneSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.toString(), SuggestedCategory.NONE);
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.error("LOG_ERROR_EXTENSION_ZoneSave", nested.getClass().getName(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_ZoneSave", nested.getMessage(), SuggestedCategory.NONE);
			
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throw new UserException(reasonCode, new Object[] {});
			}
			else
			{
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}

		}
		catch(EpiException e)
		{
			e.printStackTrace();
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_ZoneSave", e.toString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		
		return RET_CONTINUE;
	}
	
	public void isValidZone(RuntimeFormInterface form, UnitOfWorkBean uowb)throws EpiException{
		String[] parameter = new String[1];
		RuntimeFormWidgetInterface widget = form.getFormWidgetByName(ZONE);
		String widgetDisplayValue = widget.getDisplayValue() == null ? null : widget.getDisplayValue().toUpperCase();
		String queryString = ZONE_TABLE+"."+ZONE+"='"+widgetDisplayValue+"'";
		Query qry = new Query(ZONE_TABLE, queryString, null);
		_log.debug("LOG_DEBUG_EXTENSION_ZonePreSave", qry.getQueryExpression(), SuggestedCategory.NONE);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if(list.size()!=1){
			parameter[0] = colonStrip(readLabel(widget));
			throw new FormException(ERROR_MESSAGE_INVALID_EFZ, parameter);
		}
	}
	
	public String nonNegative(RuntimeFormWidgetInterface widget, String text)throws FormException{
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		if(number!=null){
			number = commaStrip(number);
		}
		if(widget.getAttributeType()==BioAttributeTypes.INT_TYPE){
			if(number!=null){
				if(number.matches(".*\\..*")){
					parameter[0] = widgetLabel;
					throw new FormException(ERROR_MESSAGE_NONINT, parameter);
				}
				int value = Integer.parseInt(number);
				if(value<0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}else{
			if(number!=null){
				float value = Float.parseFloat(number);
				if(value<0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}
		return text;
	}
	
	public String greaterThanValue(RuntimeFormWidgetInterface widget, RuntimeFormWidgetInterface widget2, String text) throws FormException{
		String[] parameter = new String[2];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		String number2 = widget2.getDisplayValue();
		if(number!=null){
			if(number.matches(".*\\..*")){
				parameter[0] = widgetLabel;
				throw new FormException(ERROR_MESSAGE_NONINT, parameter);
			}
			int value = Integer.parseInt(number);
			int compareTo = Integer.parseInt(number2);
			if(value<compareTo){
				parameter[0] = widgetLabel;
				parameter[1] = colonStrip(readLabel(widget2));
				throw new FormException(ERROR_MESSAGE_LTE, parameter);
			}
			if(value<0){
				if(text.equals("")){
					text += widgetLabel;
				}else{
					text += ", "+widgetLabel;
				}
			}
		}
		return text;
	}
	
	public String greaterThanZero(RuntimeFormWidgetInterface widget, String text)throws FormException{
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		//_log.debug("LOG_SYSTEM_OUT","Current widget data type: "+widget.getAttributeType(),100L);
		if(widget.getAttributeType()==BioAttributeTypes.INT_TYPE){
			if(number!=null){
				if(number.matches(".*\\..*")){
					parameter[0] = widgetLabel;
					throw new FormException(ERROR_MESSAGE_NONINT, parameter);
				}
				int value = Integer.parseInt(number);
				if(value<=0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}else{
			if(number!=null){
				float value = Float.parseFloat(number);
				if(value<=0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}
		return text;
	}
	
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	public String commaStrip(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try
		{
			numberS = nf.parse(number).toString();
		} catch (ParseException e)
		{
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
	public String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}