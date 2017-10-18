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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
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

public class ConfirmPutawayAction extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmPutawayAction.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Confirm Putaway from Action Context \n\n",100L);
		//Executes SP name:ConfirmPaperPutaway params:TASKDETAILKEY
		java.util.HashMap selectedItemsMap = null;	  	  
		StateInterface state = context.getState();
		TabIdentifier tab = (TabIdentifier)getParameter("tab identifier");
		String targetSlotType = (String)getParameter("target slot type");
		FormSlot fs = (FormSlot)getParameter("form slot");
		ScreenSlot ss = (ScreenSlot)getParameter("screen slot");
		boolean cascade = false;
		ArrayList listForms = new ArrayList();
		selectedItemsMap = getSelectedItemsMap(state, targetSlotType, fs, tab, cascade, listForms);
		if(selectedItemsMap != null)
			result.setSelectedItems(selectedItemsMap);
		ArrayList selectedItems = result.getSelectedItems();		
		RuntimeFormInterface searchForm = context.getSourceWidget().getForm();
		searchForm = searchForm.getParentForm(state);
		
		if(selectedItems != null && selectedItems.size() > 0)
		{
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
					bio = (BioBean)bioBeanIter.next();
					String TaskDetailtKey = bio.getString("TASKDETAILKEY");
					_log.debug("LOG_SYSTEM_OUT","\n\nTASKDETAILKEY:"+TaskDetailtKey+"\n\n",100L);
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					parms.add(new TextData(TaskDetailtKey));
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("ConfirmPaperPutaway");
					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e) {
						e.getMessage();
						UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
			 	   		throw UsrExcp;
					}
		                  
				}
				uowb.saveUOW();
				if(listForms.size() <= 0)
					listForms = (ArrayList)getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			}
			catch(EpiException ex)
			{
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}
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
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Confirm Putaway from Modal \n\n",100L);
		//Executes SP name:ConfirmPaperPutaway params:TASKDETAILKEY
		java.util.HashMap selectedItemsMap = null;	  	  
		StateInterface state = context.getState();
		UnitOfWorkBean uob = context.getState().getDefaultUnitOfWork();
/*		TabIdentifier tab = (TabIdentifier)getParameter("tab identifier");
		String targetSlotType = (String)getParameter("target slot type");
		FormSlot fs = (FormSlot)getParameter("form slot");
		ScreenSlot ss = (ScreenSlot)getParameter("screen slot");
		boolean cascade = false;
		ArrayList listForms = new ArrayList();
		selectedItemsMap = getSelectedItemsMap(state, targetSlotType, ss, tab, cascade, listForms); 
		if(selectedItemsMap != null)
			result.setSelectedItems(selectedItemsMap);*/
		RuntimeFormInterface currentRuntimeForm = context.getState().getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(context.getState());
		RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)parentForm;
		ArrayList selectedItems = headerListForm.getSelectedItems();

        if(selectedItems != null && selectedItems.size() > 0)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				try
				{
					BioBean bio;
					for(; bioBeanIter.hasNext();){
						_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
						bio = (BioBean)bioBeanIter.next();
						String TaskDetailtKey = bio.getString("TASKDETAILKEY");
						_log.debug("LOG_SYSTEM_OUT","\n\nTASKDETAILKEY:"+TaskDetailtKey+"\n\n",100L);
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(TaskDetailtKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("ConfirmPaperPutaway");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
				 	   		throw UsrExcp;
						}
		                  
					}
					uowb.saveUOW();
					result.setSelectedItems(null);
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
			return RET_CONTINUE;
		
	}	

}
