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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeSlot;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CCLoadDetailValuesFromHeader extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	private static String SKU_BIO = "wm_sku";

	private static String SKUXLOC_BIO = "wm_skuxloc";

	private static String OWNER = "STORERKEY";

	private static String ITEM = "SKU";

	private static String LOCATION = "LOC";

	private static String QUANTITY = "QTY";
	
	private static String SYSQUANTITY = "SYSQTY";

	private static String DESCR = "DESCR";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCLoadDetailValuesFromHeader.class);

	private static String DESCRIPTION = "DESCRIPTION";
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
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{

		_log.debug("LOG_SYSTEM_OUT","\n\n>>>> Beginning CCLoadDetailValuesFromHeader",100L);
		StateInterface state = context.getState();
		DataBean headerFocus = state.getFocus();
		if( headerFocus instanceof BioBean)
		{
			headerFocus = (BioBean) headerFocus;
		}
		RuntimeFormInterface ccDetailForm;

		RuntimeFormInterface headerDetailForm = state.getCurrentRuntimeForm();
		_log.debug("LOG_SYSTEM_OUT","\n1'''Current form  = " + headerDetailForm.getName(),100L);

		RuntimeFormInterface shellForm = headerDetailForm.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","\n2'''Current form  = " + shellForm.getName(),100L);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_SYSTEM_OUT","\n3'''Current form  = " + detailForm.getName(),100L);
		if (detailForm.getName().equalsIgnoreCase("wm_cyclecount_detail_toggle"))
		{
			_log.debug("LOG_SYSTEM_OUT","Need to go one deeper",100L);
			for (Iterator it = detailForm.getSubSlotsIterator(); it.hasNext();)
			{
				RuntimeSlot slot = (RuntimeSlot) it.next();
				_log.debug("LOG_SYSTEM_OUT"," " + slot.getName(),100L);
			}
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_cyclecount_detail_toggle");

			RuntimeFormInterface ccForm = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab");
			_log.debug("LOG_SYSTEM_OUT","\n4```Current form = " + ccForm.getName(),100L);
			if( ccForm.getName().equalsIgnoreCase("wm_cyclecount_detail_view"))
			{
				_log.debug("LOG_SYSTEM_OUT","Found Detail View, assigning",100L);
				ccDetailForm = ccForm;
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","List view, doing nothing",100L);
				return RET_CONTINUE;
			}
			
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","Found Detail View, assigning",100L);
			ccDetailForm = detailForm;
			
		}
		
		_log.debug("LOG_SYSTEM_OUT","-----Populating Detail-----",100L);
		
//		 Retrieve values for Owner, Item, and Location
		String ownerValue;
		String itemValue;
		String locationValue;
		Object tempOwnerValue = null;
		Object tempItemValue = null;
		Object tempLocationValue = null;
		tempOwnerValue = headerFocus.getValue(OWNER);
		tempItemValue = headerFocus.getValue(ITEM);
		tempLocationValue = headerFocus.getValue(LOCATION);
		
		DataBean ccDetailFocus = ccDetailForm.getFocus();
		if( ccDetailFocus instanceof BioBean)
		{
			ccDetailFocus = (BioBean) ccDetailFocus;
		}
		
		if ((tempOwnerValue == null) || (tempItemValue == null) || (tempLocationValue == null))
		{
			_log.debug("LOG_SYSTEM_OUT","/// Values not set or this is a brand new record, Doing Nothing",100L);
			return RET_CONTINUE;
		}
		else
		{
			ownerValue = tempOwnerValue.toString();
			itemValue = tempItemValue.toString();
			locationValue = tempLocationValue.toString();
			_log.debug("LOG_SYSTEM_OUT","//// Values Owner: " + ownerValue + " Item: " + itemValue + " Location: " + locationValue,100L);
		}

		// Query the SKU and SKUXLOC BIOs
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();

		String skuxlocQueryStatement = SKUXLOC_BIO + "." + OWNER + " = '" + ownerValue + "' AND " + SKUXLOC_BIO + "." + ITEM
				+ " = '" + itemValue + "' AND " + SKUXLOC_BIO + "." + LOCATION + " = '" + locationValue + "'";
		_log.debug("LOG_SYSTEM_OUT","//// Query " + skuxlocQueryStatement,100L);
		Query skuxlocQuery = new Query(SKUXLOC_BIO, skuxlocQueryStatement, null);
		BioCollectionBean skuxlocResults = uow.getBioCollectionBean(skuxlocQuery);

		String skuQueryStatement = SKU_BIO + "." + OWNER + " = '" + ownerValue + "' AND " + SKU_BIO + "." + ITEM + " = '"
				+ itemValue + "'";
		_log.debug("LOG_SYSTEM_OUT","//// Query " + skuQueryStatement,100L);
		Query skuQuery = new Query(SKU_BIO, skuQueryStatement, null);
		
		BioCollection skuResults = uow.getBioCollectionBean(skuQuery);

		String quantity = null;
		String description = null;
		// Display results
		_log.debug("LOG_SYSTEM_OUT","////// Size of skuxloc results " + skuxlocResults.size(),100L);
		_log.debug("LOG_SYSTEM_OUT","////// Size of sku results " + skuResults.size(),100L);
		if ((skuxlocResults.size() == 1) )
		{

			quantity = skuxlocResults.elementAt(0).get(QUANTITY).toString();
			
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","Non unique results returned for skuxloc",100L);
		}
		if( (skuResults.size() == 1 ))
		{
			description = skuResults.elementAt(0).get(DESCR).toString();
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","Non unique results returned for sku",100L);
		}
		
		_log.debug("LOG_SYSTEM_OUT","/// QTY " + quantity + " DESCR " + description,100L);

		// Put Values on the form
		ccDetailFocus.setValue(OWNER, ownerValue);
		ccDetailFocus.setValue(ITEM, itemValue);
		ccDetailFocus.setValue(LOCATION, locationValue);
		ccDetailFocus.setValue(SYSQUANTITY, quantity);
		ccDetailFocus.setValue(DESCRIPTION, description);
		
		
		

		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
