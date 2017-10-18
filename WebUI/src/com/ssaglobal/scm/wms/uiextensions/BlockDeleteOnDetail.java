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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import java.util.*;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class BlockDeleteOnDetail extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BlockDeleteOnDetail.class);
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
		
		String shellForm = getParameter("shellForm") == null ? "wms_list_shell" : getParameterString("shellForm");
		ArrayList tabs = (ArrayList) getParameter("tabs");
		String slotFormName = getParameterString("slotForm");
		String slotName = getParameter("slot") == null ? slotFormName : getParameterString("slot");
		_log.debug("LOG_DEBUG_EXTENSION_BlockDeleteOnDetail", "Parameters : slotFormName - " + slotFormName + ", slotName - " + slotName, SuggestedCategory.NONE);
		
		RuntimeFormInterface form;
		if(tabs == null)
		{
			form = FormUtil.findForm(state.getCurrentRuntimeForm(), shellForm, slotFormName, state);
		}
		else
		{
			form = FormUtil.findForm(state.getCurrentRuntimeForm(), shellForm, slotFormName,tabs, state);
		}
		
		int formNumber = -1;
		SlotInterface slot = form.getSubSlot(slotName);
		if(slot == null)
		{
			_log.debug("LOG_DEBUG_EXTENSION_BlockDeleteOnDetail", "Slot is null, continuing" , SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		else
		{
			formNumber = state.getSelectedFormNumber(slot);
			_log.debug("LOG_DEBUG_EXTENSION_BlockDeleteOnDetail", "Found Form Number - " + formNumber + " in Slot " + slot.getName(), SuggestedCategory.NONE);
		}
		RuntimeFormInterface slotForm = state.getRuntimeForm(slot, formNumber);
		if(slotForm.isListForm())
		{
			_log.debug("LOG_DEBUG_EXTENSION_BlockDeleteOnDetail", "Allowing delete on listform " + slotForm.getName() , SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_BlockDeleteOnDetail", "Blocking delete on form " + slotForm.getName() , SuggestedCategory.NONE);
			throw new UserException("WMEXP_Non_list_view_delete", new Object[] {});
		}


	}

}
