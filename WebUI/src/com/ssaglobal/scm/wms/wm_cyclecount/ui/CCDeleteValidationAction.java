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
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CCDeleteValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDeleteValidationAction.class);

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of CCDeleteValidationAction", SuggestedCategory.NONE);
		StateInterface state = context.getState();

		RuntimeFormInterface shellForm = retrieveShellForm(state);
		RuntimeFormInterface headerDetailForm = retrieveHeaderDetailForm(state, shellForm);
		//RuntimeFormInterface ccDetailForm = retrieveDetailForm(state, shellForm);

		RuntimeListFormInterface headerListForm = null;
		if (headerDetailForm.isListForm())
		{
			_log.debug("LOG_DEBUG_EXTENSION", "List Form", SuggestedCategory.NONE);
			headerListForm = (RuntimeListFormInterface) headerDetailForm;
		}

		//get selected item
		ArrayList items = headerListForm.getAllSelectedItems();
		//iterate items
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selectedItem = (BioBean) it.next();
			_log.debug("LOG_DEBUG_EXTENSION", "! Going to delete " + selectedItem.getValue("CCKEY"), SuggestedCategory.NONE);

			//int batchFlag = selectedItem.getValue()
			int status = isNull(selectedItem.getValue("STATUS")) ? -1
					: Integer.parseInt(selectedItem.getValue("STATUS").toString());
			_log.debug("LOG_DEBUG_EXTENSION", "~!@ STATUS " + status, SuggestedCategory.NONE);

			if (status == 9)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Preventing Delete, Status Posted", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CC_DELETE_POSTED", "WMEXP_CC_DELETE_POSTED", new Object[] {});
			}
		}

		//retrieve status

		//if posted throw error

		// Header Validation - Starting from Save Button
		//		DataBean headerFocus = headerDetailForm.getFocus();
		//		if (headerFocus instanceof BioBean)
		//		{
		//			headerFocus = (BioBean) headerFocus;
		//		}
		//
		//		// Detail Validation
		//		if (isNull(ccDetailForm))
		//		{
		//			_log.debug("LOG_DEBUG_EXTENSION", "No detail, returning", SuggestedCategory.NONE);
		//			return RET_CONTINUE;
		//		}
		//		DataBean ccDetailFocus = ccDetailForm.getFocus();
		//		if (ccDetailFocus instanceof BioBean)
		//		{
		//			ccDetailFocus = (BioBean) ccDetailFocus;
		//		}

		return RET_CONTINUE;
	}

	private RuntimeFormInterface retrieveShellForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
	
		return shellForm;
	}

	private RuntimeFormInterface retrieveDetailForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		//Detail
		RuntimeFormInterface ccDetailForm = null;
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		
		if (detailForm.getName().equalsIgnoreCase("wm_cyclecount_detail_toggle"))
		{
		
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_cyclecount_detail_toggle");

			RuntimeFormInterface ccForm = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab");
		
			if (ccForm.getName().equalsIgnoreCase("wm_cyclecount_detail_view"))
			{
		
				ccDetailForm = ccForm;
			}
			else
			{
		

			}

		}
		else
		{
		
			ccDetailForm = detailForm;

		}
		return ccDetailForm;
	}

	private RuntimeFormInterface retrieveHeaderDetailForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		SlotInterface headerDetailSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface headerDetailForm = state.getRuntimeForm(headerDetailSlot, null);
		
		return headerDetailForm;
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
