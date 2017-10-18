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
package com.ssaglobal.scm.wms.wm_job_management.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class JMUncombineAction extends ListSelector {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JMUncombineAction.class);

	protected int execute( ModalActionContext context, ActionResult result ) throws EpiException {
		ActionContext actContext = (ActionContext)context;
		StateInterface state = actContext.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = context.getSourceForm(); 
		RuntimeFormInterface detailForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ detailForm.getName(),100L);

		
		SlotInterface toggleSlot = detailForm.getSubSlot("wm_job_management_detail_toggle_slot");		
		_log.debug("LOG_SYSTEM_OUT","toggleSlot = "+ toggleSlot.getName(),100L);
		RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "Detail");
		RuntimeFormInterface ListTab = state.getRuntimeForm(toggleSlot, "List");
		_log.debug("LOG_SYSTEM_OUT","detailTab = "+ detailTab.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","ListTab = "+ ListTab.getName(),100L);
		BioBean WOGroupBioBean = null;
		_log.debug("LOG_SYSTEM_OUT","Toggle Form focus = "+detailForm.getFocus(),100L);
		_log.debug("LOG_SYSTEM_OUT","Form number = "+ state.getSelectedFormNumber(toggleSlot),100L);
		if (state.getSelectedFormNumber(toggleSlot) == 0){
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Uncombine Job\n\n",100L);
			RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)ListTab;
			ArrayList selectedItems = headerListForm.getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
				Iterator bioBeanIter = selectedItems.iterator();
				KeyGenBioWrapper newKey = new KeyGenBioWrapper();
				try
				{
					BioBean bio;
					for(; bioBeanIter.hasNext();){
						_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
						bio = (BioBean)bioBeanIter.next();
						//Create new work group
						String newJobid = newKey.getKey("WOGROUP");
						WOGroupBioBean = uowb.getNewBio("wm_jm_wogroup");
						WOGroupBioBean.set("GROUPID",newJobid);
						WOGroupBioBean.set("STATUS", "0");
						WOGroupBioBean.set("ISRELEASED", "0");
						WOGroupBioBean.set("GROUPKEY", newJobid);
						//assign this workorder to this work group
						
						bio.set("ISCOMBINED", "0");
						bio.set("GROUPID", newJobid);
						
					}

					result.setSelectedItems(null);
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
				result.setFocus(ListTab.getFocus());
			}

		}else{
			_log.debug("LOG_SYSTEM_OUT","I am on a detail form",100L);
			callActionFromNormalForm(detailTab.getFocus(), uowb);
			result.setFocus(detailTab.getFocus());

		}
		uowb.saveUOW();
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
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
	protected int execute(ActionContext ctx, ActionResult args) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Wrong Action1 \n\n",100L);
		try {
			// Add your code here to process the event
			
		} catch(Exception e) {
			
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		
		return RET_CONTINUE;
	}	
	private void callActionFromNormalForm(DataBean headerFocus, UnitOfWorkBean uowb)throws EpiException {
		if (headerFocus instanceof BioBean){
			BioBean WOGroupBioBean = null;
			KeyGenBioWrapper newKey = null;

				BioBean bio = (BioBean)headerFocus;

				String newJobid = newKey.getKey("WOGROUP");
				WOGroupBioBean = uowb.getNewBio("wm_jm_wogroup");
				WOGroupBioBean.set("GROUPID",newJobid);
				WOGroupBioBean.set("STATUS", "0");
				WOGroupBioBean.set("ISRELEASED", "0");
				WOGroupBioBean.set("GROUPKEY", newJobid);
				//assign this workorder to this work group
				
				bio.set("ISCOMBINED", "0");
				bio.set("GROUPID", newJobid);

		}
	}
}
