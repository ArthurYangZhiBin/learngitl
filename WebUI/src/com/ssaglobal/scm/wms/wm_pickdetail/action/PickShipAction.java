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

package com.ssaglobal.scm.wms.wm_pickdetail.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_pickdetail.ui.CWCDValidationUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickShipAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickShipAction.class);

	private StateInterface state;

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

		state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//_log.debug("LOG_DEBUG_EXTENSION", "\n1'''Current form  = " + shellToolbar.getName(), SuggestedCategory.NONE);

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n2'''Current form  = " + shellForm.getName(), SuggestedCategory.NONE);
		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface tempHeaderForm = state.getRuntimeForm(listSlot, null);
		//_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + tempHeaderForm.getName(), SuggestedCategory.NONE);

		RuntimeListFormInterface listForm = null;
		if (tempHeaderForm.isListForm())
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "@#$ A List", SuggestedCategory.NONE);
			listForm = (RuntimeListFormInterface) tempHeaderForm;
		}
		else
		{
			//_log.debug("LOG_DEBUG_EXTENSION", "@#$ Not A List", SuggestedCategory.NONE);
		}
		DataBean listFormFocus = listForm.getFocus();
		//02/02/2010 FW:  Commented out un-used code for memory spike issue (Defect214958) -- Start
		/*  

		BioCollectionBean listFormCollection = null;
		if (listFormFocus.isBioCollection())
		{
			listFormCollection = (BioCollectionBean) listFormFocus;
			//_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + listFormCollection.size(), SuggestedCategory.NONE);
		}
		*/
		//02/02/2010 FW:  Commented out un-used code for memory spike issue (Defect214958) -- End

		ArrayList items = listForm.getAllSelectedItems();
		//try
		//{
			//if (items.isEmpty())
			if ((items==null) || items.isEmpty())
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		//} catch (NullPointerException e)
		//{
			//throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		//}
		//02/02/2010 FW:  Changed code to define objects and do uow.saveUOW(true) outside for loop for memory spike issue (Defect214958) -- Start
		BioBean selectedItem = null;
		EXEDataObject results = null;
		int batchFlag = 0;
		String orderNum = null;
		String old_orderNum = null;
		String queryOrders = null;

		//iterate items
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			selectedItem = (BioBean) it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "! Items Arraylist " + selectedItem.getValue("ORDERKEY") + " - " + selectedItem.getValue("ORDERLINENUMBER") + " - " + selectedItem.getValue("CASEID")
					+ " - " + selectedItem.getValue("LOT") + " - " + selectedItem.getValue("LOC"), SuggestedCategory.NONE);
			//int batchFlag = selectedItem.getValue()
			int status = isNull(selectedItem.getValue("STATUS")) ? -1 : Integer.parseInt(selectedItem.getValue("STATUS").toString());
			_log.debug("LOG_DEBUG_EXTENSION", "~!@ STATUS " + status, SuggestedCategory.NONE);

			//02/02/2010 FW:  Changed code to get BATCHFLAG flag for each order instead of each pickdetail (Defect214958)
			//Retrieve Batch Flag
			orderNum = selectedItem.getValue("ORDERKEY") == null ? null : selectedItem.getValue("ORDERKEY").toString().toUpperCase();
			
			if(!(orderNum.equals(old_orderNum))) {
				old_orderNum = orderNum;  //save old orderkey

				queryOrders = "SELECT BATCHFLAG FROM ORDERS WHERE (ORDERKEY = '" + orderNum + "')";
				_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + queryOrders, SuggestedCategory.NONE);

				results = WmsWebuiValidationSelectImpl.select(queryOrders);
				if (results.getRowCount() == 1)
				{
					batchFlag = Integer.parseInt(results.getAttribute(1).value.getAsString());
				}
			}

			if (batchFlag == 1 && (status < 7)) //Status 7 = Sorted
			{
				//raise error
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Batch Flag is set and Status is < Sorted", SuggestedCategory.NONE);
				throw new UserException("WMEXP_PICK_SHIP", new Object[] {});

			}

			if (status != 9) //Status 9 = Shipped
			{
				//set status to 9 and save
				selectedItem.setValue("STATUS", new Integer(9));

				// CWCD Validation
				CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.PICK);
				cwcd.cwcdListValidation(selectedItem, 9);

				_log.debug("LOG_DEBUG_EXTENSION", "!@# Attempting to save", SuggestedCategory.NONE);
			  }
			
		}

		try
			{
				uow.saveUOW(true);

			} catch (UnitOfWorkException e)
			{
				// Handle Exceptions

				Throwable nested = (e).findDeepestNestedException();
				_log.error("LOG_DEBUG_EXTENSION_PickShipAction", nested.getClass().getName(), SuggestedCategory.NONE);
				_log.error("LOG_DEBUG_EXTENSION_PickShipAction", "Pick Detail " + selectedItem.getValue("ORDERKEY") + " - " + selectedItem.getValue("ORDERLINENUMBER") + " - "
						+ selectedItem.getValue("CASEID") + " - " + selectedItem.getValue("LOT") + " - " + selectedItem.getValue("LOC") + " - " + selectedItem.getValue("STATUS"),
						SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Nested " + nested.getClass().getName(), SuggestedCategory.NONE);
				if (nested instanceof ServiceObjectException)
				{
					Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
					_log.debug("LOG_DEBUG_EXTENSION", "Throwing Exception", SuggestedCategory.NONE);
					String exceptionMessage = nested.getMessage();
					Matcher matcher = errorPattern.matcher(exceptionMessage);
					if (matcher.find())
					{
						exceptionMessage = matcher.group(1);
					}
					_log.error("LOG_DEBUG_EXTENSION_PickShipAction", exceptionMessage, SuggestedCategory.NONE);
					_log.error("LOG_DEBUG_EXTENSION_PickShipAction", "Pick Detail " + selectedItem.getValue("ORDERKEY") + " - " + selectedItem.getValue("ORDERLINENUMBER") + " - "
							+ selectedItem.getValue("CASEID") + " - " + selectedItem.getValue("LOT") + " - " + selectedItem.getValue("LOC") + " - " + selectedItem.getValue("STATUS"),
							SuggestedCategory.NONE);
					throw new UserException(exceptionMessage, new Object[] {});
				}
				return RET_CANCEL;
			}
		uow.clearState();
				
		selectedItem = null;
		results = null; 
		orderNum = null;
		old_orderNum = null;
		queryOrders = null;
		items = null;
		//02/02/2010 FW:  Changed code to define object and do uow.saveUOW(true) outside for loop for memory spike issue (Defect214958) -- End

		listForm.setSelectedItems(null);
		result.setSelectedItems(null);
		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue)
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

	private boolean isEmpty(Object attributeValue)
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
