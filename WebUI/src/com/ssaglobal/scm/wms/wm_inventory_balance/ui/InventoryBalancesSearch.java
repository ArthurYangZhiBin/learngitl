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
package com.ssaglobal.scm.wms.wm_inventory_balance.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.beans.ejb.BioCollectionEpistub;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class InventoryBalancesSearch extends ActionExtensionBase{
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryBalancesSearch.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException,EpiException{
		String searchType = getParameter("searchType").toString();
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing Inventory Balances Search",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface searchForm = context.getSourceWidget().getForm();
		searchForm = searchForm.getParentForm(state);
		String qtyMax = searchForm.getFormWidgetByName("QTYMAX").getDisplayValue();
		String qtyMin = searchForm.getFormWidgetByName("QTYMIN").getDisplayValue();
		String skuEnd = searchForm.getFormWidgetByName("SKUEND").getDisplayValue();
		String skuStart = searchForm.getFormWidgetByName("SKUSTART").getDisplayValue();
		String storerKeyEnd = searchForm.getFormWidgetByName("STORERKEYEND").getDisplayValue();
		String storerKeyStart = searchForm.getFormWidgetByName("STORERKEYSTART").getDisplayValue();
		String locEnd = searchForm.getFormWidgetByName("LOCEND").getDisplayValue();
		String locStart = searchForm.getFormWidgetByName("LOCSTART").getDisplayValue();
		String lotEnd = searchForm.getFormWidgetByName("LOTEND").getDisplayValue();
		String lotStart = searchForm.getFormWidgetByName("LOTSTART").getDisplayValue();
		String lotXIdIdEnd = searchForm.getFormWidgetByName("LPNEND").getDisplayValue();
		String lotXIdIdStart = searchForm.getFormWidgetByName("LPNSTART").getDisplayValue();
		String externalLotEnd = searchForm.getFormWidgetByName("EXTERNALLOTEND").getDisplayValue();
		String externalLotStart = searchForm.getFormWidgetByName("EXTERNALLOTSTART").getDisplayValue();

		String lottable01start 	= (searchForm.getFormWidgetByName("LOTTABLE01START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE01START").getDisplayValue().toUpperCase();
		String lottable01end 	= (searchForm.getFormWidgetByName("LOTTABLE01END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE01END").getDisplayValue().toUpperCase();
		String lottable02start 	= (searchForm.getFormWidgetByName("LOTTABLE02START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE02START").getDisplayValue().toUpperCase();
		String lottable02end 	= (searchForm.getFormWidgetByName("LOTTABLE02END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE02END").getDisplayValue().toUpperCase();
		String lottable03start 	= (searchForm.getFormWidgetByName("LOTTABLE03START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE03START").getDisplayValue().toUpperCase();
		String lottable03end 	= (searchForm.getFormWidgetByName("LOTTABLE03END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE03END").getDisplayValue().toUpperCase();
		Calendar lottable04start 	= (searchForm.getFormWidgetByName("LOTTABLE04START").getCalendarValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE04START").getCalendarValue();
//		_log.debug("LOG_SYSTEM_OUT","******start4="+searchForm.getFormWidgetByName("LOTTABLE04START").getCalendarValue()+"mmm",100L);
		Calendar lottable04end 		= (searchForm.getFormWidgetByName("LOTTABLE04END").getCalendarValue() == null) 		? null : searchForm.getFormWidgetByName("LOTTABLE04END").getCalendarValue();

		Calendar lottable05start 	= (searchForm.getFormWidgetByName("LOTTABLE05START").getCalendarValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE05START").getCalendarValue();
		Calendar lottable05end 		= (searchForm.getFormWidgetByName("LOTTABLE05END").getCalendarValue() == null) 		? null : searchForm.getFormWidgetByName("LOTTABLE05END").getCalendarValue();
		String lottable06start 	= (searchForm.getFormWidgetByName("LOTTABLE06START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE06START").getDisplayValue().toUpperCase();
		String lottable06end 	= (searchForm.getFormWidgetByName("LOTTABLE06END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE06END").getDisplayValue().toUpperCase();
		String lottable07start 	= (searchForm.getFormWidgetByName("LOTTABLE07START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE07START").getDisplayValue().toUpperCase();
		String lottable07end 	= (searchForm.getFormWidgetByName("LOTTABLE07END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE07END").getDisplayValue().toUpperCase();
		String lottable08start 	= (searchForm.getFormWidgetByName("LOTTABLE08START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE08START").getDisplayValue().toUpperCase();
		String lottable08end 	= (searchForm.getFormWidgetByName("LOTTABLE08END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE08END").getDisplayValue().toUpperCase();
		String lottable09start 	= (searchForm.getFormWidgetByName("LOTTABLE09START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE09START").getDisplayValue().toUpperCase();
		String lottable09end 	= (searchForm.getFormWidgetByName("LOTTABLE09END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE09END").getDisplayValue().toUpperCase();
		String lottable10start 	= (searchForm.getFormWidgetByName("LOTTABLE10START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE10START").getDisplayValue().toUpperCase();
		String lottable10end 	= (searchForm.getFormWidgetByName("LOTTABLE10END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE10END").getDisplayValue().toUpperCase();
		Calendar lottable11start 	= (searchForm.getFormWidgetByName("LOTTABLE11START").getCalendarValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE11START").getCalendarValue();
		Calendar lottable11end 		= (searchForm.getFormWidgetByName("LOTTABLE11END").getCalendarValue() == null) 		? null : searchForm.getFormWidgetByName("LOTTABLE11END").getCalendarValue();
		Calendar lottable12start 	= (searchForm.getFormWidgetByName("LOTTABLE12START").getCalendarValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE12START").getCalendarValue();
		Calendar lottable12end 		= (searchForm.getFormWidgetByName("LOTTABLE12END").getCalendarValue() == null) 		? null : searchForm.getFormWidgetByName("LOTTABLE12END").getCalendarValue();

		/*enforce Kirk cases inputs for all dates:
		 * (1)Date inputs can only be left all blank
		 * (2)If user enters either startdate or enddate, user has to enter another one. It means if one of date input is not blank
		 *    both two (startdate, enddate) should not be blank.
		 */
		if(this.isMissingDate(lottable04start, lottable04end)){
			throw new FormException("WMEXP_MISSING_DATE", new Object[]{});
		}
		if(this.isMissingDate(lottable05start, lottable05end)){
			throw new FormException("WMEXP_MISSING_DATE", new Object[]{});
		}
		if(this.isMissingDate(lottable11start, lottable11end)){
			throw new FormException("WMEXP_MISSING_DATE", new Object[]{});
		}
		if(this.isMissingDate(lottable12start, lottable12end)){
			throw new FormException("WMEXP_MISSING_DATE", new Object[]{});
		}

//		Incident3811464_Defect273246: Receive null pointer exception in Oracle environments when Owner or Item is blank
//		IF any of the widgets return null that means they have been specifically blanked
//		out from the UI by the User in that case set them as follows
//		IF any of the widgets return empty spaces i.e. '', that means the user wants to take the defaults from the UI
//		in that case set them to defaults
		if(qtyMax == null){
			qtyMax = "0";
		}else if(qtyMax.equals("")){
			qtyMax = "999999999";
		}

		if(qtyMin == null){
			qtyMin = "1";
		}else if(qtyMin.equals("")){
			qtyMin = "1";
		}

		if(skuEnd == null){
			skuEnd = " ";
		}else if(skuEnd.equals("")){
			skuEnd = "ZZZZZZZZZZ";
		}

		if(skuStart == null || "0".equalsIgnoreCase(skuStart)){
			skuStart = " ";
		}else if(skuStart.equals("")){
			skuStart = "0";
		}

		if(storerKeyEnd == null){
			storerKeyEnd = " ";
		}else if(storerKeyEnd.equals("")){
			storerKeyEnd = "ZZZZZZZZZZ";
		}

		if(storerKeyStart == null){
			storerKeyStart = " ";
		}else if(storerKeyStart.equals("")){
			storerKeyStart = "0";
		}

		if(locEnd == null){
			locEnd = " ";
		}else if(locEnd != null && locEnd.equals("")){
			locEnd = "ZZZZZZZZZZ";
		}

		if(locStart == null){
			locStart = " ";
		}else if(locStart != null && locStart.equals("")){
			locStart = "0";
		}


		if(lotXIdIdEnd == null){
			lotXIdIdEnd = " ";
		}else if(lotXIdIdEnd != null && lotXIdIdEnd.equals("")){
			lotXIdIdEnd = "ZZZZZZZZZZZZZZZZZZ";
		}

		if(lotXIdIdStart == null){
			lotXIdIdStart = " ";
		}else if(lotXIdIdStart != null && lotXIdIdStart.equals("")){
			lotXIdIdStart = " ";
		}

		if(externalLotEnd == null){
			externalLotEnd = " ";
		}else if(externalLotEnd != null && externalLotEnd.equals("")){
			externalLotEnd = "ZZZZZZZZZZ";
		}

		if(externalLotStart == null){
			externalLotStart = " ";
		}else if( externalLotStart != null && externalLotStart.equals("")){
			externalLotStart = "0";
		}
//		END Incident3811464_Defect273246

		String qry = null;
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal_ib");
		UnitOfWorkBean uowb = state.getTempUnitOfWork();
		HelperBio helper = uowb.getUOW().createHelperBio("wm_multifacbal_ib");
		String lockedOwners = WSDefaultsUtil.getLockedOwnersAsDelimetedList(state, "','");
		if(lockedOwners != null && lockedOwners.length() > 0)
			lockedOwners = "'"+lockedOwners+"'";

		if(searchType.equalsIgnoreCase("ITEM")){
/*			if(!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax))&& qtyMax != null && qtyMin != null){
				qry="wm_daqtyallocatedxskuqtyunionskuqty.qty >= "+qtyMin+" AND wm_daqtyallocatedxskuqtyunionskuqty.qty <= "+qtyMax+" ";
			}
			if(!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd))&& skuEnd != null && skuStart != null){
				if(qry != null){
					qry=qry+" AND (wm_daqtyallocatedxskuqtyunionskuqty.sku >= '"+skuStart.toUpperCase()+"' AND wm_daqtyallocatedxskuqtyunionskuqty.sku <= '"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_daqtyallocatedxskuqtyunionskuqty.sku >= '"+skuStart.toUpperCase()+"' AND wm_daqtyallocatedxskuqtyunionskuqty.sku <= '"+skuEnd.toUpperCase()+"')";
				}
			}

			if(!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd))&& storerKeyEnd != null && storerKeyStart != null){
				if(qry != null){
					qry=qry+" AND (wm_daqtyallocatedxskuqtyunionskuqty.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_daqtyallocatedxskuqtyunionskuqty.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_daqtyallocatedxskuqtyunionskuqty.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_daqtyallocatedxskuqtyunionskuqty.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}
			}

			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_daqtyallocatedxskuqtyunionskuqty.storerkey IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_daqtyallocatedxskuqtyunionskuqty.storerkey IN ("+lockedOwners+"))";
				}
			}*/
//			_log.debug("LOG_SYSTEM_OUT","*******it is in search********",100L);
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_skuinventory.qty >= "+qtyMin+" AND wm_skuinventory.qty <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuEnd != null && skuStart != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_skuinventory.sku >= '"+skuStart.toUpperCase()+"' AND wm_skuinventory.sku <= '"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_skuinventory.sku >= '"+skuStart.toUpperCase()+"' AND wm_skuinventory.sku <= '"+skuEnd.toUpperCase()+"')";
				}
			}

			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyEnd != null && storerKeyStart != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_skuinventory.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_skuinventory.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_skuinventory.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_skuinventory.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}
			}

			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_skuinventory.storerkey IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_skuinventory.storerkey IN ("+lockedOwners+"))";
				}
			}
		}//end *************************
		else if(searchType.equalsIgnoreCase("ITEMXLOC")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_skuxloc_ib.QTY >= "+qtyMin+" AND wm_skuxloc_ib.QTY <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuEnd != null && skuStart != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_skuxloc_ib.SKU >= '"+skuStart.toUpperCase()+"' AND wm_skuxloc_ib.SKU <='"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_skuxloc_ib.SKU >= '"+skuStart.toUpperCase()+"' AND wm_skuxloc_ib.SKU <='"+skuEnd.toUpperCase()+"')";
				}
			}
			if (!("".equalsIgnoreCase(locStart) && "".equalsIgnoreCase(locEnd)) && locEnd != null && locStart != null && (isTextStartValueChanged(locStart) || isTextEndValueChanged(locEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_skuxloc_ib.LOC >= '"+locStart.toUpperCase()+"' AND wm_skuxloc_ib.LOC <='"+locEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_skuxloc_ib.LOC >= '"+locStart.toUpperCase()+"' AND wm_skuxloc_ib.LOC <='"+locEnd.toUpperCase()+"')";
				}
			}
			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyEnd != null && storerKeyStart != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_skuxloc_ib.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_skuxloc_ib.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_skuxloc_ib.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_skuxloc_ib.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_skuxloc_ib.STORERKEY IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_skuxloc_ib.STORERKEY IN ("+lockedOwners+"))";
				}
			}
		}
		else if(searchType.equalsIgnoreCase("LOTXLOCXLPN")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_inventorybalanceslotxlocxlpn.qty >= "+qtyMin+" AND wm_inventorybalanceslotxlocxlpn.qty <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuStart != null && skuEnd != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.sku >= '"+skuStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.sku <='"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_inventorybalanceslotxlocxlpn.sku >= '"+skuStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.sku <='"+skuEnd.toUpperCase()+"')";
				}
			}
			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyStart != null && storerKeyEnd != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_inventorybalanceslotxlocxlpn.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(lotStart) && "".equalsIgnoreCase(lotEnd)) && lotStart != null && lotEnd != null && (isTextStartValueChanged(lotStart) || isTextEndValueChanged(lotEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.lot >= '"+lotStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.lot <='"+lotEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_inventorybalanceslotxlocxlpn.lot >= '"+lotStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.lot <='"+lotEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(lotXIdIdStart) && "".equalsIgnoreCase(lotXIdIdEnd)) && lotXIdIdStart != null && lotXIdIdEnd != null
				&& (isTextValueBlankChanged(lotXIdIdStart) || isTextEndValueChanged(lotXIdIdEnd))) {

					if(qry != null){
						qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.id >= '"+lotXIdIdStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.id <='"+lotXIdIdEnd.toUpperCase()+"') ";
					}else{
						qry=" (wm_inventorybalanceslotxlocxlpn.id >= '"+lotXIdIdStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.id <='"+lotXIdIdEnd.toUpperCase()+"') ";
					}
			}
			if (!("".equalsIgnoreCase(locStart) && "".equalsIgnoreCase(locEnd)) && locEnd != null && locStart != null && (isTextStartValueChanged(locStart) || isTextEndValueChanged(locEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.loc >= '"+locStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.loc <='"+locEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_inventorybalanceslotxlocxlpn.loc >= '"+locStart.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.loc <='"+locEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(externalLotStart) && "".equalsIgnoreCase(externalLotEnd)) && externalLotStart != null && externalLotEnd != null
				&& (isTextStartValueChanged(externalLotStart) || isTextEndValueChanged(externalLotEnd))) {
				if(qry != null){
					qry=qry+" AND ((wm_inventorybalanceslotxlocxlpn.externallot <='"+externalLotEnd.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.externallot >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.externallot is NULL )) ";
				}else{
					qry=" ((wm_inventorybalanceslotxlocxlpn.externallot <='"+externalLotEnd.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.externallot >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_inventorybalanceslotxlocxlpn.externallot is NULL )) ";
				}
			}

			//added on 1/14/2009
			qry = this.addCriteria(lottable01start, lottable01end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE01");
			qry = this.addCriteria(lottable02start, lottable02end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE02");
			qry = this.addCriteria(lottable03start, lottable03end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE03");
			qry = this.addCriteria(lottable04start, lottable04end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE04");
			qry = this.addCriteria(lottable05start, lottable05end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE05");
			qry = this.addCriteria(lottable06start, lottable06end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE06");
			qry = this.addCriteria(lottable07start, lottable07end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE07");
			qry = this.addCriteria(lottable08start, lottable08end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE08");
			qry = this.addCriteria(lottable09start, lottable09end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE09");
			qry = this.addCriteria(lottable10start, lottable10end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE10");
			qry = this.addCriteria(lottable11start, lottable11end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE11");
			qry = this.addCriteria(lottable12start, lottable12end, qry, "wm_inventorybalanceslotxlocxlpn.LOTATTRIBUTE.LOTTABLE12");

//_log.debug("LOG_SYSTEM_OUT","&&&&&qry="+qry,100L);
			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_inventorybalanceslotxlocxlpn.storerkey IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_inventorybalanceslotxlocxlpn.storerkey IN ("+lockedOwners+"))";
				}
			}
		}
		else if(searchType.equalsIgnoreCase("LOTXLOC")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_lotxloc.Qty >= "+qtyMin+" AND wm_lotxloc.Qty <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuEnd != null && skuStart != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxloc.Sku >= '"+skuStart.toUpperCase()+"' AND wm_lotxloc.Sku <='"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_lotxloc.Sku >= '"+skuStart.toUpperCase()+"' AND wm_lotxloc.Sku <='"+skuEnd.toUpperCase()+"')";
				}
			}
			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyEnd != null && storerKeyStart != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxloc.Storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_lotxloc.Storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxloc.Storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_lotxloc.Storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(lotStart) && "".equalsIgnoreCase(lotEnd)) && lotEnd != null && lotStart != null && (isTextStartValueChanged(lotStart) || isTextEndValueChanged(lotEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxloc.Lot >= '"+lotStart.toUpperCase()+"' AND wm_lotxloc.Lot <='"+lotEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxloc.Lot >= '"+lotStart.toUpperCase()+"' AND wm_lotxloc.Lot <='"+lotEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(locStart) && "".equalsIgnoreCase(locEnd)) && locEnd != null && locStart != null && (isTextStartValueChanged(locStart) || isTextEndValueChanged(locEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxloc.Loc >= '"+locStart.toUpperCase()+"' AND wm_lotxloc.Loc <='"+locEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxloc.Loc >= '"+locStart.toUpperCase()+"' AND wm_lotxloc.Loc <='"+locEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(externalLotStart) && "".equalsIgnoreCase(externalLotEnd)) && externalLotEnd != null && externalLotStart != null
				&& (isTextStartValueChanged(externalLotStart) || isTextEndValueChanged(externalLotEnd))) {
				if(qry != null){
					qry=qry+" AND ((wm_lotxloc.LOTATTRIBUTEEXTERNALLOT <='"+externalLotEnd.toUpperCase()+"' AND wm_lotxloc.LOTATTRIBUTEEXTERNALLOT >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_lotxloc.LOTATTRIBUTEEXTERNALLOT is NULL )) ";
				}else{
					qry=" ((wm_lotxloc.LOTATTRIBUTEEXTERNALLOT <='"+externalLotEnd+"' AND wm_lotxloc.LOTATTRIBUTEEXTERNALLOT >='"+externalLotStart+"') "
					+" OR ('0' = '"+externalLotStart+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd+"' AND wm_lotxloc.LOTATTRIBUTEEXTERNALLOT is NULL )) ";
				}
			}

			//added on 1/14/2009
			qry = this.addCriteria(lottable01start, lottable01end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE01");
			qry = this.addCriteria(lottable02start, lottable02end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE02");
			qry = this.addCriteria(lottable03start, lottable03end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE03");
			qry = this.addCriteria(lottable04start, lottable04end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE04");
			qry = this.addCriteria(lottable05start, lottable05end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE05");
			qry = this.addCriteria(lottable06start, lottable06end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE06");
			qry = this.addCriteria(lottable07start, lottable07end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE07");
			qry = this.addCriteria(lottable08start, lottable08end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE08");
			qry = this.addCriteria(lottable09start, lottable09end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE09");
			qry = this.addCriteria(lottable10start, lottable10end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE10");
			qry = this.addCriteria(lottable11start, lottable11end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE11");
			qry = this.addCriteria(lottable12start, lottable12end, qry, "wm_lotxloc.LOTATTRIBUTE.LOTTABLE12");
			//end ********************
//_log.debug("LOG_SYSTEM_OUT","&&&&&LotXlocqry="+qry,100L);

			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_lotxloc.Storerkey IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_lotxloc.Storerkey IN ("+lockedOwners+"))";
				}
			}
		}
		else if(searchType.equalsIgnoreCase("LOTXLPN")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_lotxid.Qty >= "+qtyMin+" AND wm_lotxid.Qty <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuEnd != null && skuStart != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxid.SKU >= '"+skuStart.toUpperCase()+"' AND wm_lotxid.SKU <='"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_lotxid.SKU >= '"+skuStart.toUpperCase()+"' AND wm_lotxid.SKU <='"+skuEnd.toUpperCase()+"')";
				}
			}
			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyEnd != null && storerKeyStart != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxid.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_lotxid.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxid.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_lotxid.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(lotStart) && "".equalsIgnoreCase(lotEnd)) && lotEnd != null && lotStart != null && (isTextStartValueChanged(lotStart) || isTextEndValueChanged(lotEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxid.Lot >= '"+lotStart.toUpperCase()+"' AND wm_lotxid.Lot <='"+lotEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxid.Lot >= '"+lotStart.toUpperCase()+"' AND wm_lotxid.Lot <='"+lotEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(lotXIdIdStart) && "".equalsIgnoreCase(lotXIdIdEnd)) && lotXIdIdEnd != null && lotXIdIdStart != null
				&& (isTextValueBlankChanged(lotXIdIdStart) || isTextEndValueChanged(lotXIdIdEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lotxid.Id >= '"+lotXIdIdStart.toUpperCase()+"' AND wm_lotxid.Id <='"+lotXIdIdEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lotxid.Id >= '"+lotXIdIdStart.toUpperCase()+"' AND wm_lotxid.Id <='"+lotXIdIdEnd.toUpperCase()+"') ";
				}
			}
			if (!("".equalsIgnoreCase(externalLotStart) && "".equalsIgnoreCase(externalLotEnd)) && externalLotEnd != null && externalLotStart != null
				&& (isTextStartValueChanged(externalLotStart) || isTextEndValueChanged(externalLotEnd))) {
				if(qry != null){
					qry=qry+" AND ((wm_lotxid.LOTATTRIBUTEEXTERNALLOT <='"+externalLotEnd.toUpperCase()+"' AND wm_lotxid.LOTATTRIBUTEEXTERNALLOT >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_lotxid.LOTATTRIBUTEEXTERNALLOT is NULL )) ";
				}else{
					qry=" ((wm_lotxid.LOTATTRIBUTEEXTERNALLOT <='"+externalLotEnd.toUpperCase()+"' AND wm_lotxid.LOTATTRIBUTEEXTERNALLOT >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_lotxid.LOTATTRIBUTEEXTERNALLOT is NULL )) ";
				}
			}

			//added on 1/14/2009
			qry = this.addCriteria(lottable01start, lottable01end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE01");
			qry = this.addCriteria(lottable02start, lottable02end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE02");
			qry = this.addCriteria(lottable03start, lottable03end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE03");
			qry = this.addCriteria(lottable04start, lottable04end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE04");
			qry = this.addCriteria(lottable05start, lottable05end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE05");
			qry = this.addCriteria(lottable06start, lottable06end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE06");
			qry = this.addCriteria(lottable07start, lottable07end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE07");
			qry = this.addCriteria(lottable08start, lottable08end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE08");
			qry = this.addCriteria(lottable09start, lottable09end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE09");
			qry = this.addCriteria(lottable10start, lottable10end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE10");
			qry = this.addCriteria(lottable11start, lottable11end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE11");
			qry = this.addCriteria(lottable12start, lottable12end, qry, "wm_lotxid.LOTATTRIBUTE.LOTTABLE12");
			//end ********************
//_log.debug("LOG_SYSTEM_OUT","&&&&&LotXIDqry="+qry,100L);

			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_lotxid.STORERKEY IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_lotxid.STORERKEY IN ("+lockedOwners+"))";
				}
			}
		}
		else if(searchType.equalsIgnoreCase("LOT")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_lot_ib.QTY >= "+qtyMin+" AND wm_lot_ib.QTY <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuEnd != null && skuStart != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_lot_ib.SKU >= '"+skuStart.toUpperCase()+"' AND wm_lot_ib.SKU <='"+skuEnd.toUpperCase()+"')";
				}else{
					qry=" (wm_lot_ib.SKU >= '"+skuStart.toUpperCase()+"' AND wm_lot_ib.SKU <='"+skuEnd.toUpperCase()+"')";
				}
			}
			if(!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd))&& storerKeyEnd != null && storerKeyStart != null && (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))){
				if(qry != null){
					qry=qry+" AND (wm_lot_ib.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_lot_ib.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lot_ib.STORERKEY >= '"+storerKeyStart.toUpperCase()+"' AND wm_lot_ib.STORERKEY <='"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if(!("".equalsIgnoreCase(lotStart) && "".equalsIgnoreCase(lotEnd))&& lotStart != null && lotEnd != null && (isTextStartValueChanged(lotStart) || isTextEndValueChanged(lotEnd))){
				if(qry != null){
					qry=qry+" AND (wm_lot_ib.LOT >= '"+lotStart.toUpperCase()+"' AND wm_lot_ib.LOT <='"+lotEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_lot_ib.LOT >= '"+lotStart.toUpperCase()+"' AND wm_lot_ib.LOT <='"+lotEnd.toUpperCase()+"') ";
				}
			}
			if(!("".equalsIgnoreCase(externalLotStart) && "".equalsIgnoreCase(externalLotEnd))&& externalLotStart != null && externalLotEnd != null && (isTextStartValueChanged(externalLotStart) || isTextEndValueChanged(externalLotEnd))){
				if(qry != null){
					qry=qry+" AND ((wm_lot_ib.EXTERNALLOT <='"+externalLotEnd.toUpperCase()+"' AND wm_lot_ib.EXTERNALLOT >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_lot_ib.EXTERNALLOT is NULL )) ";
				}else{
					qry=" ((wm_lot_ib.EXTERNALLOT <='"+externalLotEnd.toUpperCase()+"' AND wm_lot_ib.EXTERNALLOT >='"+externalLotStart.toUpperCase()+"') "
					+" OR ('0' = '"+externalLotStart.toUpperCase()+"' AND 'ZZZZZZZZZZ' = '"+externalLotEnd.toUpperCase()+"' AND wm_lot_ib.EXTERNALLOT is NULL )) ";
				}
			}

			//added on 1/14/2009
			qry = this.addCriteria(lottable01start, lottable01end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE01");
			qry = this.addCriteria(lottable02start, lottable02end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE02");
			qry = this.addCriteria(lottable03start, lottable03end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE03");
			qry = this.addCriteria(lottable04start, lottable04end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE04");
			qry = this.addCriteria(lottable05start, lottable05end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE05");
			qry = this.addCriteria(lottable06start, lottable06end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE06");
			qry = this.addCriteria(lottable07start, lottable07end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE07");
			qry = this.addCriteria(lottable08start, lottable08end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE08");
			qry = this.addCriteria(lottable09start, lottable09end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE09");
			qry = this.addCriteria(lottable10start, lottable10end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE10");
			qry = this.addCriteria(lottable11start, lottable11end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE11");
			qry = this.addCriteria(lottable12start, lottable12end, qry, "wm_lot_ib.LOTATTRIBUTE.LOTTABLE12");
			//end ********************
//_log.debug("LOG_SYSTEM_OUT","&&&&&LotXIBqry="+qry,100L);

			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_lot_ib.STORERKEY IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_lot_ib.STORERKEY IN ("+lockedOwners+"))";
				}
			}
		}
		else if(searchType.equalsIgnoreCase("OWNER")){
			if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
				qry="wm_inventorybalancesowner.qty >= "+qtyMin+" AND wm_inventorybalancesowner.qty <= "+qtyMax+" ";
			}
			if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyStart != null && storerKeyEnd != null
				&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
				if(qry != null){
					qry=qry+" AND (wm_inventorybalancesowner.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_inventorybalancesowner.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}else{
					qry=" (wm_inventorybalancesowner.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND wm_inventorybalancesowner.storerkey <= '"+storerKeyEnd.toUpperCase()+"') ";
				}
			}
			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(qry != null){
					qry=qry+" AND (wm_inventorybalancesowner.storerkey IN ("+lockedOwners+")) ";
				}else{
					qry="(wm_inventorybalancesowner.storerkey IN ("+lockedOwners+"))";
				}
			}
		}

		//For ITEM there are no group bys only a union, so the temp-biocollection is unnessary, a view will work.
		if(searchType.equalsIgnoreCase("ITEM")){
//			Query loadBiosQry = new Query("wm_daqtyallocatedxskuqtyunionskuqty", qry, "wm_daqtyallocatedxskuqtyunionskuqty.storerkey ASC,wm_daqtyallocatedxskuqtyunionskuqty.sku ASC");
			Query loadBiosQry = new Query("wm_skuinventory", qry, "wm_skuinventory.storerkey ASC,wm_skuinventory.sku ASC");
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
//			_log.debug("LOG_SYSTEM_OUT","IB_ITEM size@@@@ = "+ bioCollection.size(),100L);
			return RET_CONTINUE;
		}
//		For ITEM BY LOC the query is simple. No temp-biocollection is necessary.
		else if(searchType.equalsIgnoreCase("ITEMXLOC")){
			Query loadBiosQry = new Query("wm_skuxloc_ib", qry, "wm_skuxloc_ib.STORERKEY ASC,wm_skuxloc_ib.SKU ASC,wm_skuxloc_ib.LOC ASC");
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
		//LOC has a complex query... temp-biocollection is necessary
		else if(searchType.equalsIgnoreCase("LOC")){
			qry = buildLocationQuery(qtyMin,qtyMax,skuStart,skuEnd,storerKeyStart,storerKeyEnd,locStart,locEnd,state,lockedOwners);
			state.getRequest().getSession().setAttribute("INV_BAL_LOC_QRY",new String (qry));
			try {
				EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Building Temp Bio Collection of size 2:"+collection.getRowCount(),100L);
				Bio bio = null;

				for(int i = 0; i < collection.getRowCount(); i++){
					bio = uowb.getUOW().createBio(helper);
					Object attrValue = collection.getAttribValue(new TextData("Loc"));
					if(attrValue != null)
						bio.set("LOCATION",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("Status"));
					if(attrValue != null)
						bio.set("STATUS",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("CubeOnHand"));
					if(attrValue != null)
						bio.set("CUBEONHAND",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("LocCubeCapacity"));
					if(attrValue != null)
						bio.set("CUBECAPACITY",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("Qty"));
					if(attrValue != null)
						bio.set("QTY",attrValue.toString());
					bio.set("SERIALKEY",new Integer(i));
					tempBioCollRefArray.add(bio.getBioRef());
					collection.getNextRow();
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				//throw new UserException("ERROR EXECUTING SEARCH", "");
			}
			DataBean focus;
			try {
				BioCollection tempBioColl = uowb.getUOW().fetchBioCollection(tempBioCollRefArray);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Bio Coll:"+tempBioColl,100L);
				BioCollectionBean tempBioCollection = uowb.getBioCollection(tempBioColl.getBioCollectionRef());
				focus = tempBioCollection;
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			result.setFocus(focus);
			return RET_CONTINUE;
		}
//		For LOTXLOCXLPN the query is simple. No temp-biocollection is necessary.
		else if(searchType.equalsIgnoreCase("LOTXLOCXLPN")){
			Query loadBiosQry = new Query("wm_inventorybalanceslotxlocxlpn", qry, null);
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
//		For LOTXLOC the query is simple. No temp-biocollection is necessary.
		else if(searchType.equalsIgnoreCase("LOTXLOC")){
			Query loadBiosQry = new Query("wm_lotxloc", qry, "wm_lotxloc.Storerkey ASC,wm_lotxloc.Sku ASC,wm_lotxloc.Lot ASC,wm_lotxloc.Loc ASC");
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
//		For LOTXLPN the query is simple. No temp-biocollection is necessary.
		else if(searchType.equalsIgnoreCase("LOTXLPN")){
			Query loadBiosQry = new Query("wm_lotxid", qry, null);
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
//		For LOT the query is simple. No temp-biocollection is necessary.
		else if(searchType.equalsIgnoreCase("LOT")){
			Query loadBiosQry = new Query("wm_lot_ib", qry, null);
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
//		LPN has a complex query... temp-biocollection is necessary
		else if(searchType.equalsIgnoreCase("LPN")){
			qry = buildLpnQuery(qtyMin,qtyMax,skuStart,skuEnd,storerKeyStart,storerKeyEnd,locStart,locEnd,lotXIdIdStart,lotXIdIdEnd,lotStart,lotEnd,state,lockedOwners);
			state.getRequest().getSession().setAttribute("INV_BAL_LPN_QRY",new String (qry));
			try {
				EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Building Temp Bio Collection of size 2:"+collection.getRowCount(),100L);
				Bio bio = null;

				for(int i = 0; i < collection.getRowCount(); i++){
					bio = uowb.getUOW().createBio(helper);
					Object attrValue = collection.getAttribValue(new TextData("Id"));
					if(attrValue != null)
						bio.set("LPN",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("Status"));
					if(attrValue != null)
						bio.set("STATUS",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("Packkey"));
					if(attrValue != null)
						bio.set("PACK",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("PutawayTI"));
					if(attrValue != null)
						bio.set("TI",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("PutawayHI"));
					if(attrValue != null)
						bio.set("HI",attrValue.toString());
					attrValue = collection.getAttribValue(new TextData("qty"));
					if(attrValue != null)
						bio.set("ONHAND",attrValue.toString());
					bio.set("SERIALKEY",new Integer(i));
					tempBioCollRefArray.add(bio.getBioRef());
					collection.getNextRow();
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				//throw new UserException("ERROR EXECUTING SEARCH", "");
			}
			DataBean focus;
			try {
				BioCollectionEpistub tempBioColl = (BioCollectionEpistub)uowb.getUOW().fetchBioCollection(tempBioCollRefArray);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Bio Coll:"+tempBioColl,100L);
				BioCollectionBean tempBioCollection = uowb.getBioCollection(tempBioColl.getBioCollectionRef());
				focus = tempBioCollection;
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			result.setFocus(focus);
			return RET_CONTINUE;
		}
		else if(searchType.equalsIgnoreCase("OWNER")){
			Query loadBiosQry = new Query("wm_inventorybalancesowner", qry, null);
			BioCollection bioCollection = uowb.getBioCollectionBean(loadBiosQry);
			result.setFocus((DataBean)bioCollection);
			return RET_CONTINUE;
		}
		return RET_CONTINUE;
	}

	/**
	 * Has the text value, which is by default blank, changed?
	 *
	 * @param txt
	 * @return
	 */
	private boolean isTextValueBlankChanged(String txt) {
		if (StringUtils.isEmpty(txt)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Has the Start Value changed?
	 *
	 * @param start
	 * @return
	 */
	private boolean isTextStartValueChanged(String start) {
		if (Pattern.matches("0*", start)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Has the End Value changed?
	 *
	 * @param end
	 * @return
	 */
	private boolean isTextEndValueChanged(String end) {
		if (Pattern.matches("Z*", end)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Has the End Value changed?
	 *
	 * @param qty
	 * @return true if it has changed
	 *         false if it has not changed
	 */
	private boolean isNumericalEndValueChanged(String qty) {
		if (Pattern.matches("9*", qty)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Has the Start Value changed?
	 *
	 * @param qty
	 * @return true if it has changed
	 *         false if it has not changed
	 */
	private boolean isNumbericalStartValueChanged(String qty) {
		if (Pattern.matches("0*", qty)) {
			return false;
		} else {
			return true;
		}
	}

	String buildQuery(HashMap nonQuotedParams, HashMap quotedParams){
		String query = "INSERT INTO MULTIFACBAL (";
		String valuesClause = " VALUES (";
		Iterator nonQuotedParamKeyItr = nonQuotedParams.keySet().iterator();
		Iterator quotedParamKeyItr = quotedParams.keySet().iterator();
		boolean clauseStarted = false;

		if(nonQuotedParamKeyItr.hasNext()){
			clauseStarted = true;
			Object key = nonQuotedParamKeyItr.next();
			query += key.toString();
			valuesClause += nonQuotedParams.get(key).toString();
			while(nonQuotedParamKeyItr.hasNext()){
				key = nonQuotedParamKeyItr.next();
				query += ","+key;
				valuesClause += ","+nonQuotedParams.get(key).toString();
			}
		}

		if(quotedParamKeyItr.hasNext()){
			Object key = null;
			if(!clauseStarted){
				key = quotedParamKeyItr.next();
				query += key.toString();
				valuesClause += "'"+quotedParams.get(key).toString()+"'";
			}
			while(quotedParamKeyItr.hasNext()){
				key = quotedParamKeyItr.next();
				query += ","+key;
				valuesClause += ",'"+quotedParams.get(key).toString()+"'";
			}
			valuesClause += ")";
		}

		query += ")"+valuesClause;

		return query;
	}

	HashMap<String, String> addToParams(HashMap<String, String> params, Object value, String key){
		if(value != null)
			params.put(key,value.toString());
		return params;
	}

	String buildLocationQuery(String qtyMin,String qtyMax,String skuStart,String skuEnd,String storerKeyStart,String storerKeyEnd,String locStart,String locEnd,StateInterface state, String lockedOwners){
		String qry = "SELECT LOTxLOC.Loc, Sum(LotxLoc.Qty) Qty, Sum(LotxLoc.Qty * Sku.StdCube) CubeOnHand, Loc.CubicCapacity LocCubeCapacity, Loc.Status Status FROM LOTxLOC, LOC, SKU";
		qry += " WHERE ( LOC.Loc = LOTxLOC.Loc )";
		qry += " and ( SKU.StorerKey = LOTxLOC.StorerKey )";
		qry += " and ( SKU.Sku = LOTxLOC.Sku )";

		if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
			qry=qry+" AND (LOTxLOC.qty >= "+qtyMin+" AND LOTxLOC.qty <= "+qtyMax+")";
		}
		if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuStart != null && skuEnd != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
			qry=qry+" AND (LOTxLOC.sku >= '"+skuStart.toUpperCase()+"' AND LOTxLOC.sku <='"+skuEnd.toUpperCase()+"')";
		}
		if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyStart != null && storerKeyEnd != null
			&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
			qry=qry+" AND (LOTxLOC.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND LOTxLOC.storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
		}
		if (!("".equalsIgnoreCase(locStart) && "".equalsIgnoreCase(locEnd)) && locStart != null && locEnd != null && (isTextStartValueChanged(locStart) || isTextEndValueChanged(locEnd))) {
			qry=qry+" AND (LOTxLOC.loc >= '"+locStart.toUpperCase()+"' AND LOTxLOC.loc <='"+locEnd.toUpperCase()+"') ";
		}
		if(WSDefaultsUtil.isOwnerLocked(state)){
			qry=qry+" AND (LOTxLOC.storerkey IN ("+lockedOwners+")) ";
		}
		qry += " GROUP BY LOTxLOC.Loc, LOC.CubicCapacity, LOC.Status ";
		return qry;
	}

	String buildLpnQuery(String qtyMin,String qtyMax,String skuStart,String skuEnd,String storerKeyStart,String storerKeyEnd,String locStart,String locEnd, String idStart, String idEnd, String lotStart, String lotEnd, StateInterface state, String lockedOwners){
		String qry = "SELECT ID.Id, SUM(ID.Qty) qty, ID.Status,  ID.Packkey,  Pack.PalletTI PutawayTI,Pack.PalletHI PutawayHI FROM ID, pack";
		qry += " WHERE ( ID.packkey = pack.packkey  )";
		qry += " and ID.id IN ( SELECT ID FROM LOTxLOCxID WHERE LOTxLOCxID.ID = ID.ID";

		if (!("".equalsIgnoreCase(qtyMin) && "".equalsIgnoreCase(qtyMax)) && qtyMax != null && qtyMin != null && (isNumbericalStartValueChanged(qtyMin) || isNumericalEndValueChanged(qtyMax))) {
			qry=qry+" AND (LOTxLOCxID.qty >= "+qtyMin+" AND LOTxLOCxID.qty <= "+qtyMax+")";
		}
		if (!("".equalsIgnoreCase(skuStart) && "".equalsIgnoreCase(skuEnd)) && skuStart != null && skuEnd != null && (isTextValueBlankChanged(skuStart) || isTextEndValueChanged(skuEnd))) {
			qry=qry+" AND (LOTxLOCxID.sku >= '"+skuStart.toUpperCase()+"' AND LOTxLOCxID.sku <='"+skuEnd.toUpperCase()+"')";
		}
		if (!("".equalsIgnoreCase(storerKeyStart) && "".equalsIgnoreCase(storerKeyEnd)) && storerKeyStart != null && storerKeyEnd != null
			&& (isTextStartValueChanged(storerKeyStart) || isTextEndValueChanged(storerKeyEnd))) {
			qry=qry+" AND (LOTxLOCxID.storerkey >= '"+storerKeyStart.toUpperCase()+"' AND LOTxLOCxID.storerkey <='"+storerKeyEnd.toUpperCase()+"') ";
		}
		if (!("".equalsIgnoreCase(locStart) && "".equalsIgnoreCase(locEnd)) && locStart != null && locEnd != null && (isTextStartValueChanged(locStart) || isTextEndValueChanged(locEnd))) {
			qry=qry+" AND (LOTxLOCxID.loc >= '"+locStart.toUpperCase()+"' AND LOTxLOCxID.loc <='"+locEnd.toUpperCase()+"') ";
		}
		if (!("".equalsIgnoreCase(lotStart) && "".equalsIgnoreCase(lotEnd)) && lotStart != null && lotEnd != null && (isTextStartValueChanged(lotStart) || isTextEndValueChanged(lotEnd))) {
			qry=qry+" AND (LOTxLOCxID.lot >= '"+lotStart.toUpperCase()+"' AND LOTxLOCxID.lot <='"+lotEnd.toUpperCase()+"') ";
		}
		if (!("".equalsIgnoreCase(idStart) && "".equalsIgnoreCase(idEnd)) && idStart != null && idEnd != null && (isTextValueBlankChanged(idStart) || isTextEndValueChanged(idEnd))) {
			qry=qry+" AND (LOTxLOCxID.id >= '"+idStart.toUpperCase()+"' AND LOTxLOCxID.id <='"+idEnd.toUpperCase()+"') ";
		}

		if(WSDefaultsUtil.isOwnerLocked(state)){
			qry=qry+" AND (LOTxLOCxID.storerkey IN ("+lockedOwners+")) ";
		}

		qry += ") GROUP BY	ID.Id,   ID.Status,   ID.Packkey,	Pack.PalletTI,	Pack.PalletHI ";
		return qry;
	}

	//added on 1/15/2009
	private String addCriteria(Calendar start, Calendar end, String query, String criteria) {
		if (!isNull(start)){
			if(query != null){
				query = query.concat("AND " + criteria + " >= " + "@DATE['" + start.getTimeInMillis() + "']" + " ");
			}else{
				query = criteria + " >= " + "@DATE['" + start.getTimeInMillis() + "'] ";
			}
		}
		if (!isNull(end)){
			if(query != null){
				query = query.concat("AND " + criteria + " <= " + "@DATE['" + end.getTimeInMillis() + "']" );
			}else{
				query = criteria + " <= " + "@DATE['" + end.getTimeInMillis() + "']";
			}
		}
		return query;
	}

	private boolean isNull(Calendar attributeValue) {
		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}
	}

	private String addCriteria(String start, String end, String query, String criteria) {
		if (!isNull(start, false)) {
			if(query != null){
				query = query.concat("AND " + criteria + " >= '" + start + "' ");
			}else{
				query =  criteria + " >= '" + start + "' ";
			}

		}
		if (!isNull(end, true)) {
			if(query != null){
				query = query.concat("AND " + criteria + " <= '" + end + "' ");
			}else{
				query = criteria + " <= '" + end + "' ";
			}
		}
		return query;
	}

	private boolean isNull(String attributeValue, boolean isEndValue) {
		if (attributeValue == null) {
			return true;
		} else if (attributeValue.trim().length() == 0) {
			return true;
		} else if (attributeValue.matches("\\s*")) {
			return true;
		} else if (attributeValue.equals("*")) {
			return true;
		} else if (attributeValue.equals("%")) {
			return true;
		}else if(isEndValue){
			if("ZZZZZZZZZZZZZZZ".equalsIgnoreCase(attributeValue)){
				return true;
			}else{
				return false;
			}
		}else {
			return false;
		}
	}

	private boolean isMissingDate(Calendar start, Calendar end){
		if(!((start == null && end == null) || (start != null && end != null))){
			return true;
		}
		return false;
	}
}