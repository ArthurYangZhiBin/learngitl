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
package com.ssaglobal.scm.wms.wm_owner;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_order_bio.ui.UnallocatePickLinesAction;

public class ListDeleteOwner extends com.epiphany.shr.ui.action.ActionExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ListDeleteOwner.class);

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		String processStr = getParameterString("PROCESSNAME");
		String[] process = new String[1];
		process[0] = processStr;
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		_log.debug("LOG_SYSTEM_OUT","\n!@# Toolbar " + toolbar.getName(),100L);
		RuntimeFormInterface listForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","\n!@# List Form - " + listForm.getName(),100L);
		if (!listForm.isListForm())
		{

			throw new UserException("Delete_From_Detail_View", process);
		}
		ArrayList selectedItems = ((RuntimeListForm) listForm).getAllSelectedItems();
		if (selectedItems.size() > 1)
		{
			throw new UserException("Single_Select_Validation", process);
		}

		return RET_CONTINUE;
	}
}
