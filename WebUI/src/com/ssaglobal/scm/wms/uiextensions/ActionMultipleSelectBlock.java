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
package com.ssaglobal.scm.wms.uiextensions;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import java.util.ArrayList;
import com.epiphany.shr.util.exceptions.UserException;

public class ActionMultipleSelectBlock extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ActionMultipleSelectBlock.class);

	public ActionMultipleSelectBlock()
	{
		_log.info("EXP_1", "MultipleSelectBlock Instantiated!!!", SuggestedCategory.NONE);
	}

	protected int execute(ActionContext context, ActionResult result) throws UserException
	{

		String processStr = (String) getParameter("processName");
		String[] process = new String[1];
		process[0] = processStr;

		String headerOrDetail = (String) getParameter("headerOrDetail");
		String toggleFormSlot = (String) getParameter("toggleFormSlot");
		String headerErrorMessage = getParameter("headerErrorMessage") == null ? "WMEXP_ACTION_FROM_HEADER_VIEW"
				: getParameterString("headerErrorMessage");
		String detailErrorMessage = getParameter("detailErrorMessage") == null ? "WMEXP_ACTION_FROM_DETAIL_VIEW"
				: getParameterString("detailErrorMessage");

		RuntimeListFormInterface listForm = null;
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		if ("Header".equalsIgnoreCase(headerOrDetail))
		{
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if (headerForm.isListForm())
			{
				listForm = (RuntimeListFormInterface) headerForm;
			}
			else
			{
				throw new UserException(headerErrorMessage, process);
			}
		}
		else if ("Detail".equalsIgnoreCase(headerOrDetail))
		{
			RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
			SlotInterface toggleSlot = toggleForm.getSubSlot(toggleFormSlot);
			RuntimeFormInterface detailTabList = state.getRuntimeForm(toggleSlot, null);
			if (detailTabList.isListForm())
			{
				listForm = (RuntimeListFormInterface) detailTabList;
			}
			else
			{
				throw new UserException(detailErrorMessage, process);
			}
		}
		ArrayList selectedItems = listForm.getAllSelectedItems();
		if (selectedItems == null || selectedItems.size() == 0)
		{
			throw new UserException("WMEXP_NOTHING_SELECTED_WARNING", process);
		}
		if (selectedItems.size() > 1)
		{
			throw new UserException("WMEXP_SINGLE_SELECT_VALIDATION", process);
		}
		return RET_CONTINUE;

	}

}
