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

package com.ssaglobal.scm.wms.wm_view_inventory_hold.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Calendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ViewInventoryHoldSearch extends com.epiphany.shr.ui.action.ActionExtensionBase {


protected static ILoggerCategory _log = LoggerFactory.getInstance(ViewInventoryHoldSearch.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		RuntimeFormInterface searchForm = state.getCurrentRuntimeForm().getParentForm(state);

		String storerstart 		= (searchForm.getFormWidgetByName("STORERSTART").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("STORERSTART").getDisplayValue().toUpperCase();
		String storerend 		= (searchForm.getFormWidgetByName("STOREREND").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("STOREREND").getDisplayValue().toUpperCase();
		String skustart			= (searchForm.getFormWidgetByName("SKUSTART").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("SKUSTART").getDisplayValue().toUpperCase();
		String skuend 			= (searchForm.getFormWidgetByName("SKUEND").getDisplayValue() == null) 			? null : searchForm.getFormWidgetByName("SKUEND").getDisplayValue().toUpperCase();
		String lotstart			= (searchForm.getFormWidgetByName("LOTSTART").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("LOTSTART").getDisplayValue().toUpperCase();
		String lotend 			= (searchForm.getFormWidgetByName("LOTEND").getDisplayValue() == null) 			? null : searchForm.getFormWidgetByName("LOTEND").getDisplayValue().toUpperCase();
		String locstart 		= (searchForm.getFormWidgetByName("LOCSTART").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("LOCSTART").getDisplayValue().toUpperCase();
		String locend 			= (searchForm.getFormWidgetByName("LOCEND").getDisplayValue() == null) 			? null : searchForm.getFormWidgetByName("LOCEND").getDisplayValue().toUpperCase();
		String idstart 			= (searchForm.getFormWidgetByName("IDSTART").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("IDSTART").getDisplayValue().toUpperCase();
		String idend 			= (searchForm.getFormWidgetByName("IDEND").getDisplayValue() == null) 			? null : searchForm.getFormWidgetByName("IDEND").getDisplayValue().toUpperCase();
		String qtystart 		= (searchForm.getFormWidgetByName("QTYSTART").getDisplayValue() == null) 		? null : searchForm.getFormWidgetByName("QTYSTART").getDisplayValue().toUpperCase();
		String qtyend 			= (searchForm.getFormWidgetByName("QTYEND").getDisplayValue() == null) 			? null : searchForm.getFormWidgetByName("QTYEND").getDisplayValue().toUpperCase();
		String holdreasonstart 	= (searchForm.getFormWidgetByName("HOLDREASONSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("HOLDREASONSTART").getDisplayValue().toUpperCase();
		String holdreasonend 	= (searchForm.getFormWidgetByName("HOLDREASONEND").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("HOLDREASONEND").getDisplayValue().toUpperCase();
		String lottable01start 	= (searchForm.getFormWidgetByName("LOTTABLE01START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE01START").getDisplayValue().toUpperCase();
		String lottable01end 	= (searchForm.getFormWidgetByName("LOTTABLE01END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE01END").getDisplayValue().toUpperCase();
		String lottable02start 	= (searchForm.getFormWidgetByName("LOTTABLE02START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE02START").getDisplayValue().toUpperCase();
		String lottable02end 	= (searchForm.getFormWidgetByName("LOTTABLE02END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE02END").getDisplayValue().toUpperCase();
		String lottable03start 	= (searchForm.getFormWidgetByName("LOTTABLE03START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTTABLE03START").getDisplayValue().toUpperCase();
		String lottable03end 	= (searchForm.getFormWidgetByName("LOTTABLE03END").getDisplayValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE03END").getDisplayValue().toUpperCase();
		Calendar lottable04start 	= (searchForm.getFormWidgetByName("LOTTABLE04START").getCalendarValue() == null) 	? null : searchForm.getFormWidgetByName("LOTTABLE04START").getCalendarValue();
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
		
		String query = "1=1 ";

		// If Owner locking is on, filter the results by that set of owners...
		if (WSDefaultsUtil.isOwnerLocked(state)) {
			String lockedOwners = WSDefaultsUtil.getLockedOwnersAsDelimetedList(state, "','");
			if (lockedOwners != null && lockedOwners.length() > 0) {
				lockedOwners = "'" + lockedOwners + "'";
			}
			query = "wm_vInventoryHold.STORERKEY IN (" + lockedOwners + ")";
		}
		
		query = addCriteria(storerstart, storerend, query, "wm_vInventoryHold.STORERKEY");
		query = addCriteria(skustart, skuend, query, "wm_vInventoryHold.SKU");
		query = addCriteria(lotstart, lotend, query, "wm_vInventoryHold.LOT");
		query = addCriteria(locstart, locend, query, "wm_vInventoryHold.LOC");
		query = addCriteria(idstart, idend, query, "wm_vInventoryHold.ID");
		query = addCriteria(qtystart, qtyend, query, "wm_vInventoryHold.QTY");
		query = addCriteria(holdreasonstart, holdreasonend, query, "wm_vInventoryHold.STATUS"); // NEED TO CONVERT
		query = addCriteria(lottable01start, lottable01end, query, "wm_vInventoryHold.LOTTABLE01");
		query = addCriteria(lottable02start, lottable02end, query, "wm_vInventoryHold.LOTTABLE02");
		query = addCriteria(lottable03start, lottable03end, query, "wm_vInventoryHold.LOTTABLE03");
		query = addCriteria(lottable04start, lottable04end, query, "wm_vInventoryHold.LOTTABLE04"); // DATE?
		query = addCriteria(lottable05start, lottable05end, query, "wm_vInventoryHold.LOTTABLE05"); // DATE?
		query = addCriteria(lottable06start, lottable06end, query, "wm_vInventoryHold.LOTTABLE06");
		query = addCriteria(lottable07start, lottable07end, query, "wm_vInventoryHold.LOTTABLE07");
		query = addCriteria(lottable08start, lottable08end, query, "wm_vInventoryHold.LOTTABLE08");
		query = addCriteria(lottable09start, lottable09end, query, "wm_vInventoryHold.LOTTABLE09");
		query = addCriteria(lottable10start, lottable10end, query, "wm_vInventoryHold.LOTTABLE10");
		query = addCriteria(lottable11start, lottable11end, query, "wm_vInventoryHold.LOTTABLE11"); // DATE?
		query = addCriteria(lottable12start, lottable12end, query, "wm_vInventoryHold.LOTTABLE12"); // DATE?
		
		
		
		
		
		
		
		// filter biocollection based on the query
		_log.debug("LOG_DEBUG_EXTENSION_ViewInventoryHoldSearch_execute", query, SuggestedCategory.NONE);
		Query loadBiosQry = new Query("wm_vInventoryHold", query, "wm_vInventoryHold.STORERKEY ASC, wm_vInventoryHold.SKU ASC, wm_vInventoryHold.LOT ASC, wm_vInventoryHold.LOC ASC, wm_vInventoryHold.ID ASC, wm_vInventoryHold.HOLDTYPE ASC, wm_vInventoryHold.STATUS ASC");
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		result.setFocus(uow.getBioCollectionBean(loadBiosQry));
		return RET_CONTINUE;
	}

	private String addCriteria(Calendar start, Calendar end, String query, String criteria) {
		String subqry = " ";
		if (!isNull(start))
			subqry = subqry.concat("AND " + criteria + " >= " + "@DATE['" + start.getTimeInMillis() + "']" + " ");
		if (!isNull(end))
			subqry = subqry.concat("AND " + criteria + " <= " + "@DATE['" + end.getTimeInMillis() + "']" + " ");
		if (!subqry.equalsIgnoreCase(" ")) {
			if (isNull(start)) {
				subqry = subqry.replaceFirst("AND ", "");
				query = query + " AND (" + subqry.concat(" OR "+ criteria +" is null)");
			} else {
				subqry = subqry.replaceFirst("AND ", "");
				query = query + " AND (" + subqry.concat(")");
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
		String subqry = " ";
		if (!isNull(start))
			subqry = subqry.concat("AND " + criteria + " >= '" + start + "' ");
		//12/23/2010 FW  Added code to skip building where clause if lottable end string is equal to ‘ZZZZZZZZZZ’ (Incident4213961_Defect295788) -- Start
		//if (!isNull(end))
		if (!isNull(end) && !("ZZZZZZZZZZ".equalsIgnoreCase(end)))
		//12/23/2010 FW  Added code to skip building where clause if lottable end string is equal to ‘ZZZZZZZZZZ’ (Incident4213961_Defect295788) -- End
			subqry = subqry.concat("AND " + criteria + " <= '" + end + "' ");
		if (!subqry.equalsIgnoreCase(" ")) {
			if (isNull(start)) {
				subqry = subqry.replaceFirst("AND ", "");
				query = query + " AND (" + subqry.concat(" OR " + criteria + " is null)");
			} else {
				subqry = subqry.replaceFirst("AND ", "");
				query = query + " AND (" + subqry.concat(")");
			}
		}
		return query;
	}
	
	private boolean isNull(String attributeValue) {
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
		} else {
			return false;
		}
	}

}
