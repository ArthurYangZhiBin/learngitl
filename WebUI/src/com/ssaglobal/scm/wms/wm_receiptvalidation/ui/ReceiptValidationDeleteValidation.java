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

package com.ssaglobal.scm.wms.wm_receiptvalidation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ReceiptValidationDeleteValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
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

		RuntimeFormInterface itemForm = FormUtil.findForm(shellToolbar, "wms_list_shell",
															"wm_receiptvalidation_list_view", state);

		RuntimeListFormInterface itemList = null;
		if (itemForm.isListForm())
		{
			itemList = (RuntimeListFormInterface) itemForm;
		}
		else
		{
			return RET_CANCEL;
		}

//		DataBean listFormFocus = itemList.getFocus();
//		BioCollectionBean itemCollection = null;
//		if (listFormFocus.isBioCollection())
//		{
//			itemCollection = (BioCollectionBean) listFormFocus;
//			_log.debug("LOG_SYSTEM_OUT","!@# It is a BioCollectionBean, size: " + itemCollection.size(),100L);
//		}

		
		ArrayList items;
		try
		{
			items = itemList.getAllSelectedItems();
			if(items.size() == 0)
			{
				throw new UserException("WMEXP_NONE_SELECTED",new Object[] {});
			}
		}
		catch(RuntimeException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED",new Object[] {});
		}
		//iterate items
		for (Iterator item = items.iterator(); item.hasNext();)
		{
			BioBean selectedRV = (BioBean) item.next();

			Object tempValue = selectedRV.getValue("DEFAULTFLAG");
			String defaultFlagValue;

			if (isEmpty(tempValue))
			{
				//treat as unchecked
				defaultFlagValue = "0";
			}
			else
			{
				defaultFlagValue = tempValue.toString();
			}

			//Block delete of record with Default Flag set
			if (defaultFlagValue.equals("1"))
			{
				//throw error prevent delete
				throw new UserException("WMEXP_RV_DEFAULT_DELETE", new Object[] {});
			}
		}
		return RET_CONTINUE;
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
