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

package com.ssaglobal.scm.wms.wm_allocation_management.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AllocMgtShipAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AllocMgtShipAction.class);
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

		StateInterface state = context.getState();	
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		ArrayList tabList = new ArrayList();		
		tabList.add("tab 1");					
		RuntimeFormInterface pickDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_pick_detail_list",tabList,state);
				
		if(pickDetailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Pick Detail Form:"+pickDetailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Pick Detail Form:Null",100L);			
		
		if(pickDetailForm == null || 
				((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems() == null || 
				((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems().size() == 0){
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		ArrayList items = ((RuntimeListFormInterface)pickDetailForm).getAllSelectedItems();
		//iterate items
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selectedItem = (BioBean) it.next();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","! Items Arraylist " + selectedItem.getValue("ORDERKEY") + " - "
					+ selectedItem.getValue("ORDERLINENUMBER") + " - " + selectedItem.getValue("CASEID"),100L);			
			//int batchFlag = selectedItem.getValue()
			int status = isNull(selectedItem.getValue("STATUS")) ? -1
					: Integer.parseInt(selectedItem.getValue("STATUS").toString());
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","~!@ STATUS " + status,100L);			

			//Retrieve Batch Flag
			String orderNum = selectedItem.getValue("ORDERKEY").toString();
			int batchFlag = 0;
			String queryOrders = "SELECT BATCHFLAG FROM ORDERS WHERE (ORDERKEY = '" + orderNum + "')";
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Query" + queryOrders,100L);			

			EXEDataObject results = WmsWebuiValidationSelectImpl.select(queryOrders);
			if (results.getRowCount() == 1)
			{
				batchFlag = Integer.parseInt(results.getAttribute(1).value.getAsString());
			}

			if (batchFlag == 1 && (status < 7)) //Status 7 = Sorted
			{
				//raise error
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","!@# Batch Flag is set and Status is < Sorted",100L);
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_PICK_SHIP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}

			if (status != 9) //Status 9 = Shipped
			{
				uow.clearState();
				//set status to 9 and save
				selectedItem.setValue("STATUS", new Integer(9));				
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","!@# Attempting to save",100L);
				try
				{
					uow.saveUOW(true);

				} catch (UnitOfWorkException e)
				{
					// Handle Exceptions

					Throwable nested = (e).findDeepestNestedException();
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Nested " + nested.getClass().getName(),100L);					
					if (nested instanceof ServiceObjectException)
					{
						Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Throwing Exception",100L);						
						String exceptionMessage = nested.getMessage();
						Matcher matcher = errorPattern.matcher(exceptionMessage);
						if (matcher.find())
						{
							exceptionMessage = matcher.group(1);
						}
						throw new UserException(exceptionMessage, new Object[] {});

					}
					return RET_CANCEL;

				}
				uow.clearState();
			}
		}

		((RuntimeListFormInterface)pickDetailForm).setSelectedItems(null);
		result.setSelectedItems(null);
		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue) throws EpiDataException
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

}
