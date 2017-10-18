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


package com.ssaglobal.scm.wms.wm_qc_verify.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class QCVerifyVerifyAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifyVerifyAction.class);
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
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "QCVerifyVerifyAction" + "\n", SuggestedCategory.NONE);;
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		Object qcState = session.getAttribute("QCSTATE");
		if (qcState == null)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Current State: NULL/START" + "\n", SuggestedCategory.NONE);;
			RuntimeListFormInterface pickedForm = (RuntimeListFormInterface) FormUtil.findForm(
																								state.getCurrentRuntimeForm(),
																								"wm_qc_verify_template",
																								"wm_qc_verify_picked_view",
																								state);
			if(!pickedForm.getName().equals("wm_qc_verify_picked_view"))
			{
				//form not found
				throw new UserException("WMEXP_NOTHING_SELECTED", new Object[] {});
			}
			ArrayList selectedItems = pickedForm.getSelectedListBucketValue();

			if (selectedItems.size() == 0)
			{
				throw new UserException("WMEXP_NOTHING_SELECTED", new Object[] {});
			}

			//Navigate to Popup
			context.setNavigation("menuClickEvent391");
			//Set Focus
			String selected = (String) selectedItems.get(0);
			BioBean selectedBean = uowb.getBioBean(BioRef.createBioRefFromString(selected));
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting Focus to " + ((BioBean) selectedBean).getValue("SKU") + "\n", SuggestedCategory.NONE);;
			result.setFocus((DataBean) selectedBean);
			selectedItems.remove(0);
			if (selectedItems.size() == 0)
			{
				//No records left to process
				session.removeAttribute("QCSELECTED");
				session.setAttribute("QCSTATE", "END");
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting State: END" + "\n", SuggestedCategory.NONE);;
			}
			else
			{
				//Add list of selected records to the Session
				session.setAttribute("QCSELECTED", selectedItems);
				//Set state
				session.setAttribute("QCSTATE", "PROCESS");
			}
			return RET_CONTINUE;
		}
		else if (qcState.toString().equals("PROCESS"))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Current State: " + qcState.toString() + "\n", SuggestedCategory.NONE);;
			ArrayList selectedItems = (ArrayList) session.getAttribute("QCSELECTED");

			//Set Focus for Popup
			String selected = (String) selectedItems.get(0);
			BioBean selectedBean = uowb.getBioBean(BioRef.createBioRefFromString(selected));
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting Focus to " + ((BioBean) selectedBean).getValue("SKU") + "\n", SuggestedCategory.NONE);;
			result.setFocus((DataBean) selectedBean);
			selectedItems.remove(0);
			//Navigate to Popup
			context.setNavigation("closeModalDialog63");

			if (selectedItems.size() == 0)
			{
				//No records left to process
				session.removeAttribute("QCSELECTED");
				session.setAttribute("QCSTATE", "END");
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Setting State: END" + "\n", SuggestedCategory.NONE);;
			}
			else
			{
				//Add list of selected records to the Session
				session.setAttribute("QCSELECTED", selectedItems);
				//Set state
				session.setAttribute("QCSTATE", "PROCESS");
			}
			return RET_CONTINUE;
		}
		else if (qcState.toString().equals("END"))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Current State: " + qcState.toString() + "\n", SuggestedCategory.NONE);;
			session.removeAttribute("QCSTATE");
			session.removeAttribute("QCSELECTED");

			//Navigate to Results Slot
			context.setNavigation("closeModalDialog64");
			return RET_CONTINUE;
		}
		else
		{

		}

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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			StateInterface state = ctx.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			DataBean modalFocus = ctx.getModalBodyForm(0).getFocus();
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + modalFocus.getValue("VERIFIEDQTY") + "\n", SuggestedCategory.NONE);;
			uowb.saveUOW(true);
			uowb.clearState();

		} catch (UnitOfWorkException ex)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "IN UnitOfWorkException" + "\n", SuggestedCategory.NONE);;
			
			Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
			_log.debug("LOG_DEBUG_EXTENSION", "\tNested " + nested.getClass().getName(), SuggestedCategory.NONE);;
			_log.debug("LOG_DEBUG_EXTENSION", "\tMessage " + nested.getMessage(), SuggestedCategory.NONE);;
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throw new UserException(reasonCode, new Object [] {});
			}
			return RET_CANCEL;
			
		} 

		return RET_CONTINUE;
	}

}
