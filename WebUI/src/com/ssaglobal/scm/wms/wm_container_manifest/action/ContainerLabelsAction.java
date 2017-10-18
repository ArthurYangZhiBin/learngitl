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

package com.ssaglobal.scm.wms.wm_container_manifest.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
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

public class ContainerLabelsAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ContainerLabelsAction.class);
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
		StateInterface state = ctx.getState();

		RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
		_log.debug("LOG_DEBUG_EXTENSION", "Modal Form " + modalForm.getName(), SuggestedCategory.NONE);
		RuntimeFormInterface toolbarForm = ctx.getSourceForm();
		RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "Header Form " + listForm.getName(), SuggestedCategory.NONE);

		try
		{
			//process container id
			ArrayList items = listForm.getAllSelectedItems();
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedItem = (BioBean) it.next();
				_log.debug("LOG_DEBUG_EXTENSION", "ContainerID " + selectedItem.getString("CONTAINERKEY"), SuggestedCategory.NONE);
				/*
				 * UnitOfWorkBean uowb = state.getDefaultUnitOfWork();				
				 try
				 {						
				 for(; containerIDIter.hasNext();){            	 
				 String containerId = (String)containerIDIter.next();
				 Array parms = new Array(); 
				 parms.add(new TextData(containerId));
				 parms.add(new TextData(nonRfidPrinterName));
				 parms.add(new TextData(rfidPrinterName));
				 WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				 actionProperties.setProcedureParameters(parms);
				 actionProperties.setProcedureName("NSPPrintContainerLabel");												
				 try {
				 _log.debug("LOG_DEBUG_EXTENSION", "Printing Container:"+containerId+"   On RFID Printer:"+rfidPrinterName+"   and Standard Printer:"+nonRfidPrinterName+"...", SuggestedCategory.NONE);
				 WmsWebuiActionsImpl.doAction(actionProperties);
				 } catch (Exception e) {
				 _log.debug("LOG_DEBUG_EXTENSION", "\n\n----------Exiting PrintLabelsAction...--------------", SuggestedCategory.NONE);
				 throw new UserException(e.getLocalizedMessage(),new Object[0]);
				 }										
				 }
				 uowb.saveUOW();  					
				 result.setSelectedItems(null);
				 listForm.setSelectedItems(null);					
				 }
				 catch(EpiException ex)
				 {
				 String args[] = new String[0];
				 String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				 throw new UserException(errorMsg,new Object[0]);
				 }
				 */
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
