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
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class PrintPutAwayLabel extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrintPutAwayLabel.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Confirm Putaway \n\n",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ shellForm.getName(),100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		if (headerFocus instanceof BioCollection){
			
			//Executes SP name:PrintPPLables params:RECEIPTKEY
			java.util.HashMap selectedItemsMap = null;	  	  
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
						String ReceiptKey = bio.getString("RECEIPTKEY");
						_log.debug("LOG_SYSTEM_OUT","\n\n RECEIPTKEY:"+ReceiptKey+"\n\n",100L);
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(ReceiptKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("PrintPPLables");
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
			/*Machine2249586_SDIS06391.delete.b
			 *			else{
			 callActionFromNormalForm(headerFocus);
			 }
			 Machine2249586_SDIS06391.delete.e*/
		}
//		Machine2249586_SDIS06391.b		
		else{
			callActionFromNormalForm(headerFocus);
		}
//		Machine2249586_SDIS06391.e

		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
		return RET_CONTINUE;
		
	}
	private void callActionFromNormalForm(DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
				headerFocus = (BioBean)headerFocus;
				String ReceiptKey = headerFocus.getValue("RECEIPTKEY").toString();
				_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:"+ReceiptKey+"\n\n",100L);
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				parms.add(new TextData(ReceiptKey));
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("PrintPPLables");
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
