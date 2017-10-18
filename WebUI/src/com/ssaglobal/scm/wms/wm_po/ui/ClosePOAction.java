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
package com.ssaglobal.scm.wms.wm_po.ui;


import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.eai.exception.EAIError;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
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
import com.ssaglobal.scm.wms.wm_pickdetail.ui.SOCatchWeightCatchDataPreRender;

public class ClosePOAction extends ListSelector {
	private final String LISTVIEW = "closeModalDialog48";	
	private final String DETAILVIEW = "closeModalDialog49";	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ClosePOAction.class);


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
	_log.debug("LOG_SYSTEM_OUT","Iam in Modal window OK button",100L);
	StateInterface state = context.getState();
	RuntimeFormInterface toolbar = context.getSourceForm();    //gets the toolbar
	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	_log.debug("LOG_SYSTEM_OUT","headerForm"+ shellForm.getName(),100L);
	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
	RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
	DataBean headerFocus = headerForm.getFocus();
	if (headerFocus instanceof BioCollection){
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Close PO Action \n\n",100L);
		//Executes SP name:NSPPOCLOSE params:POKEY
		RuntimeListFormInterface listviewForm = (RuntimeListFormInterface)headerForm;

		ArrayList items;
		items = listviewForm.getAllSelectedItems();
		if(items != null && items.size() > 0)
		{
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
			Iterator bioBeanIter = items.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
					bio = (BioBean)bioBeanIter.next();
					String POKey = bio.getString("POKEY");
					_log.debug("LOG_SYSTEM_OUT","\n\nPOKEY in list :"+POKey+"\n\n",100L);
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					parms.add(new TextData(POKey));
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("NSPPOCLOSE");
					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e) {
						e.getMessage();
						UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
			 	   		throw UsrExcp;
					}
				}
				uowb.saveUOW();
			}
			catch(EpiException ex)
			{
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}
		context.setNavigation(LISTVIEW);
	}else{
		callActionFromNormalForm(headerFocus);
		result.setFocus(headerFocus);
		context.setNavigation(DETAILVIEW);
	}
	
	
	
	_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
	return RET_CONTINUE;
}	
private void callActionFromNormalForm(DataBean headerFocus)throws EpiException {
	if (headerFocus instanceof BioBean){
			headerFocus = (BioBean)headerFocus;
			String POKey = headerFocus.getValue("POKEY").toString();
			_log.debug("LOG_SYSTEM_OUT","\n POKEY in form:"+POKey+"\n\n",100L);
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(POKey));
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName("NSPPOCLOSE");
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (Exception e) {
				e.getMessage();
				UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
	 	   		throw UsrExcp;
			}
	}
}
}
