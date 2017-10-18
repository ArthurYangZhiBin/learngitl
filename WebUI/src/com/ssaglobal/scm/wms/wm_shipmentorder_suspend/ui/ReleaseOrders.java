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

package com.ssaglobal.scm.wms.wm_shipmentorder_suspend.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.internal.ServiceObjectException;
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
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ReleaseOrders extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SuspendOrders.class);
	
	private static final String SUSPENDED_INDICATOR = "SuspendedIndicator";

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
		RuntimeFormInterface suspendOrdersForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_shipmentorder_suspend_list_view", state);

		if (suspendOrdersForm.isListForm())
		{
			RuntimeListFormInterface suspendOrdersListForm = (RuntimeListFormInterface) suspendOrdersForm;
			ArrayList<BioBean> allSelectedOrders = suspendOrdersListForm.getAllSelectedItems();
			for (BioBean selectedOrder : allSelectedOrders)
			{
				selectedOrder.setValue(SUSPENDED_INDICATOR, "0");
				selectedOrder.save();
			}
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			try
			{
				uow.saveUOW();
			} catch (EpiException e)
			{
				e.printStackTrace();
				Throwable deepestNestedException = e.findDeepestNestedException();
				if (deepestNestedException instanceof ServiceObjectException)
				{
					String reasonCode = deepestNestedException.getMessage();
					_log.error("LOG_ERROR_EXTENSION_SuspendOrders_execute", reasonCode, SuggestedCategory.NONE);
					throw new UserException(reasonCode, new Object[] {});
				}
				else
				{
					_log.error("LOG_ERROR_EXTENSION_SuspendOrders_execute", e.getMessage(), SuggestedCategory.NONE);
					throw e;
				}
				
			}
			suspendOrdersListForm.setSelectedItems(null);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	
}
