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
package com.ssaglobal.scm.wms.wm_serial_inventory.ui;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.wm_save.ui.BlockSave;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class SerialInventorySearch extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerialInventorySearch.class);

	private boolean includesNull(String attributeValue) {
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
		
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","[SerialInventorySearch] Entered...",100L);

		// Get user entered criteria
		StateInterface state = context.getState();
		RuntimeFormInterface searchForm = state.getCurrentRuntimeForm().getParentForm(state);

		String OWNEREND			= (searchForm.getFormWidgetByName("OWNEREND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("OWNEREND").getDisplayValue().toUpperCase();
		String OWNERSTART		= (searchForm.getFormWidgetByName("OWNERSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("OWNERSTART").getDisplayValue().toUpperCase();
		String ITEMEND			= (searchForm.getFormWidgetByName("ITEMEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("ITEMEND").getDisplayValue().toUpperCase();
		String ITEMSTART		= (searchForm.getFormWidgetByName("ITEMSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("ITEMSTART").getDisplayValue().toUpperCase();
		String LOTEND			= (searchForm.getFormWidgetByName("LOTEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTEND").getDisplayValue().toUpperCase();
		String LOTSTART			= (searchForm.getFormWidgetByName("LOTSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOTSTART").getDisplayValue().toUpperCase();
		String LPNEND			= (searchForm.getFormWidgetByName("LPNEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LPNEND").getDisplayValue().toUpperCase();
		String LPNSTART			= (searchForm.getFormWidgetByName("LPNSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LPNSTART").getDisplayValue().toUpperCase();
		String LOCATIONEND		= (searchForm.getFormWidgetByName("LOCATIONEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOCATIONEND").getDisplayValue().toUpperCase();
		String LOCATIONSTART	= (searchForm.getFormWidgetByName("LOCATIONSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LOCATIONSTART").getDisplayValue().toUpperCase();
		String SERIALEND		= (searchForm.getFormWidgetByName("SERIALEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("SERIALEND").getDisplayValue().toUpperCase();
		String SERIALSTART		= (searchForm.getFormWidgetByName("SERIALSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("SERIALSTART").getDisplayValue().toUpperCase();
		String DATA2END			= (searchForm.getFormWidgetByName("DATA2END").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA2END").getDisplayValue().toUpperCase();
		String DATA2START		= (searchForm.getFormWidgetByName("DATA2START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA2START").getDisplayValue().toUpperCase();
		String DATA3END			= (searchForm.getFormWidgetByName("DATA3END").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA3END").getDisplayValue().toUpperCase();
		String DATA3START		= (searchForm.getFormWidgetByName("DATA3START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA3START").getDisplayValue().toUpperCase();
		String DATA4END			= (searchForm.getFormWidgetByName("DATA4END").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA4END").getDisplayValue().toUpperCase();
		String DATA4START		= (searchForm.getFormWidgetByName("DATA4START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA4START").getDisplayValue().toUpperCase();
		String DATA5END			= (searchForm.getFormWidgetByName("DATA5END").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA5END").getDisplayValue().toUpperCase();
		String DATA5START		= (searchForm.getFormWidgetByName("DATA5START").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("DATA5START").getDisplayValue().toUpperCase();
		String LONGSERIALEND	= (searchForm.getFormWidgetByName("LONGSERIALEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LONGSERIALEND").getDisplayValue().toUpperCase();
		String LONGSERIALSTART	= (searchForm.getFormWidgetByName("LONGSERIALSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("LONGSERIALSTART").getDisplayValue().toUpperCase();
		String WEIGHTEND		= (searchForm.getFormWidgetByName("WEIGHTEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("WEIGHTEND").getDisplayValue();
		String WEIGHTSTART		= (searchForm.getFormWidgetByName("WEIGHTSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("WEIGHTSTART").getDisplayValue();
		String QTYEND			= (searchForm.getFormWidgetByName("QTYEND").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("QTYEND").getDisplayValue();
		String QTYSTART			= (searchForm.getFormWidgetByName("QTYSTART").getDisplayValue() == null) ? null : searchForm.getFormWidgetByName("QTYSTART").getDisplayValue();

		// Build query string; start by not filtering the results at all...
		String qry = "1=1";
		
		// If Owner locking is on, filter the results by that set of owners... 
		if(WSDefaultsUtil.isOwnerLocked(state))
		{
			String lockedOwners = WSDefaultsUtil.getLockedOwnersAsDelimetedList(state, "','");
			if(lockedOwners != null && lockedOwners.length() > 0)
				lockedOwners = "'"+lockedOwners+"'";
			qry = "wm_serialinventory.STORERKEY IN ("+lockedOwners+")";
		}

		// Continue to narrow down the results with additional user-supplied criteria, if provided... 
		String subqry = " ";
		if (!includesNull(OWNERSTART))
			subqry = subqry.concat("AND wm_serialinventory.STORERKEY >= '" + OWNERSTART + "' ");
		if (!includesNull(OWNEREND))
			subqry = subqry.concat("AND wm_serialinventory.STORERKEY <= '" + OWNEREND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(OWNERSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.STORERKEY is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}			

		subqry = " ";
		if (!includesNull(ITEMSTART))
			subqry = subqry.concat("AND wm_serialinventory.SKU >= '" + ITEMSTART + "' ");
		if (!includesNull(ITEMEND))
			subqry = subqry.concat("AND wm_serialinventory.SKU <= '" + ITEMEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(ITEMSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.SKU is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(LOTSTART))
			subqry = subqry.concat("AND wm_serialinventory.LOT >= '" + LOTSTART + "' ");
		if (!includesNull(LOTEND))
			subqry = subqry.concat("AND wm_serialinventory.LOT <= '" + LOTEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(LOTSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.LOT is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(LPNSTART))
			subqry = subqry.concat("AND wm_serialinventory.ID >= '" + LPNSTART + "' ");
		if (!includesNull(LPNEND))
			subqry = subqry.concat("AND wm_serialinventory.ID <= '" + LPNEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(LPNSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.ID is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(LOCATIONSTART))
			subqry = subqry.concat("AND wm_serialinventory.LOC >= '" + LOCATIONSTART + "' ");
		if (!includesNull(LOCATIONEND))
			subqry = subqry.concat("AND wm_serialinventory.LOC <= '" + LOCATIONEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(LOCATIONSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.LOC is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(SERIALSTART))
			subqry = subqry.concat("AND wm_serialinventory.SERIALNUMBER >= '" + SERIALSTART + "' ");
		if (!includesNull(SERIALEND))
			subqry = subqry.concat("AND wm_serialinventory.SERIALNUMBER <= '" + SERIALEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(SERIALSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.SERIALNUMBER is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(DATA2START))
			subqry = subqry.concat("AND wm_serialinventory.DATA2 >= '" + DATA2START + "' ");
		if (!includesNull(DATA2END))
			subqry = subqry.concat("AND wm_serialinventory.DATA2 <= '" + DATA2END + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(DATA2START))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.DATA2 is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(DATA3START))
			subqry = subqry.concat("AND wm_serialinventory.DATA3 >= '" + DATA3START + "' ");
		if (!includesNull(DATA3END))
			subqry = subqry.concat("AND wm_serialinventory.DATA3 <= '" + DATA3END + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(DATA3START))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.DATA3 is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(DATA4START))
			subqry = subqry.concat("AND wm_serialinventory.DATA4 >= '" + DATA4START + "' ");
		if (!includesNull(DATA4END))
			subqry = subqry.concat("AND wm_serialinventory.DATA4 <= '" + DATA4END + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(DATA4START))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.DATA4 is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(DATA5START))
			subqry = subqry.concat("AND wm_serialinventory.DATA5 >= '" + DATA5START + "' ");
		if (!includesNull(DATA5END))
			subqry = subqry.concat("AND wm_serialinventory.DATA5 <= '" + DATA5END + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(DATA5START))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.DATA5 is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(LONGSERIALSTART))
			subqry = subqry.concat("AND wm_serialinventory.SERIALNUMBERLONG >= '" + LONGSERIALSTART + "' ");
		if (!includesNull(LONGSERIALEND))
			subqry = subqry.concat("AND wm_serialinventory.SERIALNUMBERLONG <= '" + LONGSERIALEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(LONGSERIALSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.SERIALNUMBERLONG is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(WEIGHTSTART))
			subqry = subqry.concat("AND wm_serialinventory.GROSSWEIGHT >= '" + WEIGHTSTART + "' ");
		if (!includesNull(WEIGHTEND))
			subqry = subqry.concat("AND wm_serialinventory.GROSSWEIGHT <= '" + WEIGHTEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(WEIGHTSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.GROSSWEIGHT is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}

		subqry = " ";
		if (!includesNull(QTYSTART))
			subqry = subqry.concat("AND wm_serialinventory.QTY >= '" + QTYSTART + "' ");
		if (!includesNull(QTYEND))
			subqry = subqry.concat("AND wm_serialinventory.QTY <= '" + QTYEND + "' ");
		if (!subqry.equalsIgnoreCase(" "))
		{
			if (includesNull(QTYSTART))
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(" OR wm_serialinventory.QTY is null)");
			}
			else
			{
				subqry = subqry.replaceFirst("AND ", "");
				qry = qry + " AND (" + subqry.concat(")");
			}
		}
		
		_log.debug("LOG_SYSTEM_OUT","[SerialInventorySearch qry:] "+qry,100L);
		
		// filter biocollection based on the query
		Query loadBiosQry = new Query("wm_serialinventory", qry, null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		result.setFocus((DataBean) uow.getBioCollectionBean(loadBiosQry));
		return RET_CONTINUE;
	}
}
