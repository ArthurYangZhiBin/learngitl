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
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.extensions.OAGlobalAudit;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class WebUIListDeleteAction extends ListSelector {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WebUIListDeleteAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		// _log.debug("LOG_SYSTEM_OUT","\n\t" + "CALLING WEBUILISTDELETEACTION" + "\n",100L);
		// ListDeleteAction test;
		RuntimeFormInterface currentRuntimeForm = context.getState().getCurrentRuntimeForm();
		if (currentRuntimeForm != null) {
			RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(context.getState());
			if (parentForm != null) {
				RuntimeFormInterface parentParentForm = parentForm.getParentForm(context.getState());
				if (parentParentForm != null) {
					String parentParentFormName = parentParentForm.getName();
					if (parentParentFormName != null && parentParentFormName.equals("cti_call_customer_search"))
						return 1;
				}
			}
		}
		String targetSlotType = (String) getParameter("target slot type");
		FormSlot fs = (FormSlot) getParameter("form slot");
		ScreenSlot ss = (ScreenSlot) getParameter("screen slot");
		boolean cascade = getParameterBoolean("cascade", false);
		TabIdentifier tab = (TabIdentifier) getParameter("tab identifier");
		int formNumber = 0;
		StateInterface state = context.getState();
		java.util.HashMap selectedItemsMap = null;
		ArrayList listForms = new ArrayList();
		if (targetSlotType.equals("SELF") || targetSlotType.equals("PARENT"))
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, null, tab, cascade, listForms);
		else if (fs != null)
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, fs, tab, cascade, listForms);
		else if (ss != null)
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, ss, tab, cascade, listForms);
		if (selectedItemsMap != null)
			result.setSelectedItems(selectedItemsMap);
		ArrayList selectedItems = result.getSelectedItems();
		if (selectedItems != null && selectedItems.size() > 0) {
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try {
				BioBean bio;
				for (; bioBeanIter.hasNext(); bio.delete())
					bio = (BioBean) bioBeanIter.next();
				// _log.debug("LOG_SYSTEM_OUT","\n\t" + "BEFORE SAVE" + "\n",100L);
				uowb.saveUOW();
				if (listForms.size() <= 0)
					listForms = (ArrayList) getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			} catch (UnitOfWorkException ex) {
				ex.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_WebUIListDeleteAction_execute", "IN UnitOfWorkException", SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_WebUIListDeleteAction_execute", ex.getStackTraceAsString(), SuggestedCategory.NONE);

				Throwable nested = ((UnitOfWorkException) ex).findDeepestNestedException();
				// System.out.println("\tNested " +
				// nested.getClass().getName());
				// _log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);

				if (nested instanceof ServiceObjectException) {
					String reasonCode = nested.getMessage();
					throwUserException(ex, reasonCode, null);
				} else {
					// throw ex;
					throwUserException(ex, "ERROR_DELETING_BIO", null);
				}

			} catch (EpiException ex) {
				_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN EPIEXCEPTION" + "\n",100L);
				_log.error("LOG_ERROR_EXTENSION_WebUIListDeleteAction_execute", "IN EPIEXCEPTION", SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_WebUIListDeleteAction_execute", ex.getStackTraceAsString(), SuggestedCategory.NONE);
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			} finally{
				//jp.answerlink.274358.begin
				com.epiphany.shr.data.bio.extensions.OAGlobalAudit   audit = new OAGlobalAudit();
				
				for (Iterator itr = selectedItems.iterator(); itr.hasNext();){
					BioBean bio = (BioBean) itr.next();
					audit.bioAfterDeleteWrapper(bio);
				}
				//jp.answerlink.274358.end
				
			}

		}
		return 0;
	}
}
