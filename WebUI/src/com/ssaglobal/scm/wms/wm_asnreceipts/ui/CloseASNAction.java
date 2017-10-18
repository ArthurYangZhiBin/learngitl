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
import com.agileitp.forte.framework.DataValue;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class CloseASNAction extends ListSelector {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CloseASNAction.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","ShellForm"+ shellForm.getName(),100L);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();	
		
		if (headerFocus instanceof BioCollection){
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Close ASN Action \n\n",100L);
			//Executes SP name:NSPRECEIPTCLOSE params:RECEIPTKEY
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
						_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:"+ReceiptKey+"\n\n",100L);
						if (bio.get("STATUS").toString().equalsIgnoreCase("11")){
							String[] ErrorParem = new String[1];
							ErrorParem[0]= bio.getString("RECEIPTKEY");
							UserException UsrExcp = new UserException("WMEXP_STATUSCLOSE_02", ErrorParem);
				 	   		throw UsrExcp;
						}
						
						// 20/Nov/2009 Seshu - 3PL Enhancements Container Exchange changes starts						
						String storerKey = bio.getString("STORERKEY");
						String carrierKey = bio.getString("CARRIERKEY");
						String supplierKey = bio.getString("SUPPLIERCODE");
						
						validateContainerExchangeData(ReceiptKey, storerKey, carrierKey, supplierKey);
						// 3PL Enhancements Ends
						
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(ReceiptKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("NSPRECEIPTCLOSE");
						try {
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							e.getMessage();
							UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
				 	   		throw UsrExcp;
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
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}else{
			_log.debug("LOG_SYSTEM_OUT","I am on a detail form",100L);
			
			// 20/Nov/2009 Seshu - 3PL Enhancements starts 
			String storerKey = headerFocus.getValue("STORERKEY").toString();
			String receiptKey = headerFocus.getValue("RECEIPTKEY").toString();
			Object carrierKey = headerFocus.getValue("CARRIERKEY");
			Object supplierKey = headerFocus.getValue("SUPPLIERCODE");
						
			validateContainerExchangeData(receiptKey, storerKey, carrierKey, supplierKey); 			
			//	3PL Enhancements Ends	
			
			callActionFromNormalForm(headerFocus);
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
	private void callActionFromNormalForm(DataBean headerFocus)throws EpiException {
		if (headerFocus instanceof BioBean){
				headerFocus = (BioBean)headerFocus;
				String ReceiptKey = headerFocus.getValue("RECEIPTKEY").toString();
				_log.debug("LOG_SYSTEM_OUT","\n\nRECEIPTKEY:"+ReceiptKey+"\n\n",100L);
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				parms.add(new TextData(ReceiptKey));
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPRECEIPTCLOSE");
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (Exception e) {
					e.getMessage();
					UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
		 	   		throw UsrExcp;
				}
		}
	}
	
	public boolean validateContainerExchangeData(String receiptKey, String storerKey, Object carrierKey, 
			Object supplierKey) throws EpiException
	{
		/******************************************************************
         * Programmer		:       Seshabrahmam Yaddanapudi
         * Created			:       20/Nov/2009
         * Purpose	        :       Throw exception if the flag CONTAINEREXCHANGEFLAG is ON for the STORER and 
         * 							Receipt does not have any records on Container Exchange.
         * Release			:		3PL Enhancements - Container Exchange
         *******************************************************************
         * Modification History
         * 
         *******************************************************************/
		   EXEDataObject results = null;
		   DataValue flag = null;
		   String query = null;
		
		   boolean ownerFlag = false;
		   boolean carrierFlag = false;
		   boolean supplierFlag = false;
		   
		   // Check Owner flag
		   query = "SELECT CONTAINEREXCHANGEFLAG FROM STORER WHERE STORERKEY = '" + storerKey + "'";   
		   try{
			   results = WmsWebuiValidationSelectImpl.select(query);		   
		   }catch(Exception e){
			   e.printStackTrace();
		   }		   
		   flag = results.getAttribValue(new TextData("CONTAINEREXCHANGEFLAG"));
		   if(flag.toString().equalsIgnoreCase("1"))
			   ownerFlag = true;
		   
		   // Check Carrier flag
		   if(carrierKey != null && carrierKey.toString().trim().length() != 0)
		   {
			   query = "SELECT CONTAINEREXCHANGEFLAG FROM STORER WHERE STORERKEY = '" + carrierKey.toString() + "'";   
			   try{
				   results = WmsWebuiValidationSelectImpl.select(query);		   
			   }catch(Exception e){
				   e.printStackTrace();
			   }		   
			   flag = results.getAttribValue(new TextData("CONTAINEREXCHANGEFLAG"));
			   if(flag.toString().equalsIgnoreCase("1"))
				   carrierFlag = true;
		   }
		   
		   // Check Supplier flag
		   if(supplierKey != null && supplierKey.toString().trim().length() != 0)
		   {
			   query = "SELECT CONTAINEREXCHANGEFLAG FROM STORER WHERE STORERKEY = '" + supplierKey.toString() + "'";   
			   try{
				   results = WmsWebuiValidationSelectImpl.select(query);		   
			   }catch(Exception e){
				   e.printStackTrace();
			   }		   
			   flag = results.getAttribValue(new TextData("CONTAINEREXCHANGEFLAG"));
			   if(flag.toString().equalsIgnoreCase("1"))
				   supplierFlag = true;
		   }
		   
		   if(ownerFlag == true || carrierFlag == true || supplierFlag == true)
		   {		   
			   query = "SELECT COUNT(*) AS COUNT FROM RECEIPTCONTAINEREXCHANGE WHERE RECEIPTKEY = '" + receiptKey + "'";   
			   try{
				   results = WmsWebuiValidationSelectImpl.select(query);		   
			   }catch(Exception e){
				   e.printStackTrace();
			   }
			   DataValue count = results.getAttribValue(new TextData("COUNT"));			   
			   if(count.toString().equalsIgnoreCase("0"))
			   {
				   String[] ErrorParem = new String[1];
				   ErrorParem[0]= receiptKey;				   
				   throw new UserException("WMEXP_ASN_CNTRX_NO_DATA", ErrorParem);
			   }
		   }
		return true;
	}
}
