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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class ReopenASNAction extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReopenASNAction.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ shellForm.getName(),100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		if (headerFocus instanceof BioCollection){
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting ReOpen ASN Action \n\n",100L);
			//Executes SP name:NSPRECEIPTREOPEN params:RECEIPTKEY
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
			boolean process = false;
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
						BioCollectionBean receiptdetail = (BioCollectionBean)bio.get("RECEIPTDETAILS");

						
						int j = 0;
		                for (j=0; j<receiptdetail.size(); j++){
		                	BioBean ReceiptDetailLine = (BioBean)receiptdetail.elementAt(j);
		                		if (ReceiptDetailLine.getValue("POKEY") != null){
		                			if (! (ReceiptDetailLine.getValue("POKEY").toString().trim().equalsIgnoreCase(""))){
			                			if (checkPOVerifyClosed(state,ReceiptDetailLine.getValue("POKEY").toString())){
			                				process = false;
			                				break;
			                			} else {
			                				process = true;
			                			}
		                			}else{
			                			process= true;
		                			}
		                		}else {
		                			process= true;
		                		}
		                }
						if (bio.get("STATUS").toString().equalsIgnoreCase("15")){
							process = false;
							String[] ErrorParem = new String[1];
							ErrorParem[0]= bio.getString("RECEIPTKEY");
							UserException UsrExcp = new UserException("WMEXP_VERIFYCLOSE1", ErrorParem);
				 	   		throw UsrExcp;
						}
						if (!(bio.get("STATUS").toString().equalsIgnoreCase("11"))){
							String[] ErrorParem = new String[1];
							ErrorParem[0]= bio.getString("RECEIPTKEY");
							UserException UsrExcp = new UserException("WMEXP_STATUSCLOSE_01", ErrorParem);
				 	   		throw UsrExcp;
						}
		                if(process){
		                	String ReceiptKey = bio.getString("RECEIPTKEY");
		                	_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:"+ReceiptKey+"\n\n",100L);
		                	WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		                	Array parms = new Array(); 
		                	parms.add(new TextData(ReceiptKey));
		                	actionProperties.setProcedureParameters(parms);
		                	actionProperties.setProcedureName("NSPRECEIPTREOPEN");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
				 	   		throw UsrExcp;
						}
		                }	
					}
					uowb.saveUOW();
					uowb.clearState();
					if(listForms.size() <= 0)
						listForms = (ArrayList)getTempSpaceHash().get("SELECTED_LIST_FORMS");
					clearBuckets(listForms);
					result.setSelectedItems(null);
				}
				catch(EpiException ex)
				{
					ex.getMessage();
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}else{
			callActionFromNormalForm(headerFocus, state);
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
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
	public boolean checkPOVerifyClosed (StateInterface state, String pokey)throws EpiException{
		String sQueryString = "( wm_po.POKEY  = '%" + pokey + "%')";
		Query mlQuery = new Query("wm_po", sQueryString, null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean bioColl = uow.getBioCollectionBean(mlQuery);
   	   	if (bioColl.size()!= 0){
   			if(bioColl.get("0").get("STATUS").equals("15")){
   				return true;
   			}
   			else {
   				return false;
   			}
   	   	}else{
   			return false;
   	   	}
	}
	private void callActionFromNormalForm(DataBean headerFocus, StateInterface state)throws EpiException {
		boolean process = false;
		if (headerFocus instanceof BioBean){
			BioBean bio = (BioBean)headerFocus;
			BioCollectionBean receiptdetail = (BioCollectionBean)bio.get("RECEIPTDETAILS");
			int j = 0;
            for (j=0; j<receiptdetail.size(); j++){
            	BioBean ReceiptDetailLine = (BioBean)receiptdetail.elementAt(j);
        		if (ReceiptDetailLine.getValue("POKEY") != null){
        			if (! (ReceiptDetailLine.getValue("POKEY").toString().trim().equalsIgnoreCase(""))){
            			if (checkPOVerifyClosed(state,ReceiptDetailLine.getValue("POKEY").toString())){
            				process = false;
            				break;
            			} else {
            				process = true;
            			}
        			}else{
            			process= true;
        			}
        		}else {
        			process= true;
        		}
            }        
            if(process){
            	String ReceiptKey = bio.getString("RECEIPTKEY");
            	_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:"+ReceiptKey+"\n\n",100L);
            	WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();	
            	Array parms = new Array(); 
            	parms.add(new TextData(ReceiptKey));
            	actionProperties.setProcedureParameters(parms);
            	actionProperties.setProcedureName("NSPRECEIPTREOPEN");
		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			e.getMessage();
			UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
 	   		throw UsrExcp;
		}
        }		}
	}
}
