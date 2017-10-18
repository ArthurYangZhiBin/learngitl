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

package com.ssaglobal.scm.wms.wm_order_status.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
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

public class OrderStatusSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeListFormInterface orderForm = (RuntimeListFormInterface) FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_order_status_list_view", state);
		BioCollectionBean orderListFocus = (BioCollectionBean) orderForm.getFocus();

		int size = orderListFocus.size();
		for (int i = 0; i < size; i++)
		{
			BioBean orderStatus = orderListFocus.get(String.valueOf(i));

			String status = orderStatus.getValue("CODE").toString();
			String description = orderStatus.getValue("DESCRIPTION").toString();

			String editableValue = orderStatus.getString("EDITABLE");
			String enableValue = orderStatus.getString("ENABLED");

			String parameters[] = new String[2];
			parameters[0] = status;
			parameters[1] = description;

			
			//Added b/c OA bug - unable to set checkboxes to read only
			/*
			 * Enable			If editable = 0, disable
			 * Shipment Order	If editable = 0 and enable = 0, disable
			 * Flow Thru Order	If editable = 0 and enable = 0, disable
			 */
			if(orderStatus.hasBeenUpdated("ENABLED"))
			{
				if (editableValue.equals("0"))
				{
					state.getDefaultUnitOfWork().clearState();
					throw new UserException("WMEXP_OS_REJECT", parameters);
				}
			}
			if( orderStatus.hasBeenUpdated("ORDERFLAG"))
			{
				if (editableValue.equals("0") && enableValue.equals("0"))
				{
					state.getDefaultUnitOfWork().clearState();
					throw new UserException("WMEXP_OS_REJECT", parameters);
				}
			}
			if(orderStatus.hasBeenUpdated("XORDERFLAG"))
			{
				if (editableValue.equals("0") && enableValue.equals("0"))
				{
					state.getDefaultUnitOfWork().clearState();
					throw new UserException("WMEXP_OS_REJECT", parameters);
				}
			}
			
			
			if (orderStatus.hasBeenUpdated("ENABLED") && orderStatus.getValue("ENABLED").toString().equals("0"))
			{
				
				
				if (isStatusUsed("ORDERS", status) || isStatusUsed("ORDERDETAIL", status))
				{

					throw new UserException("WMEXP_ORDER_STATUS_IN_USE", parameters);
				}
				if (isStatusUsed("XORDERS", status) || isStatusUsed("XORDERDETAIL", status))
				{

					throw new UserException("WMEXP_ORDER_STATUS_IN_USE", parameters);
				}
				if (isStatusUsed("TRANSSHIP", status) || isStatusUsed("TRANSDETAIL", status))
				{

					throw new UserException("WMEXP_ORDER_STATUS_IN_USE", parameters);
				}
			}
			
			
			if (orderStatus.hasBeenUpdated("ORDERFLAG") && orderStatus.getValue("ORDERFLAG").toString().equals("0"))
			{
				if (isStatusUsed("ORDERS", status) || isStatusUsed("ORDERDETAIL", status))
				{

					throw new UserException("WMEXP_ORDER_STATUS_IN_USE", parameters);
				}
			}
			
			
			if (orderStatus.hasBeenUpdated("XORDERFLAG") && orderStatus.getValue("XORDERFLAG").toString().equals("0"))
			{
				if (isStatusUsed("XORDERS", status) || isStatusUsed("XORDERDETAIL", status))
				{

					throw new UserException("WMEXP_ORDER_STATUS_IN_USE", parameters);
				}
			}

		}

		//Iterate through BioBean

		return RET_CONTINUE;
	}

	// Checks table to see if the status is used in the table
	boolean isStatusUsed(String tableName, String status) throws DPException
	{
		String query = "SELECT STATUS FROM " + tableName + " WHERE STATUS = '" + status + "'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() >= 1)
		{
			//status is being used
			return true;
		}
		else
		{
			return false;
		}

	}
}
