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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SaveAssignAccessorialCharges extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveAssignAccessorialCharges.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	
		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing SaveAssignAccessorialCharges",100L);

		 
		StateInterface state = context.getState();		 
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
		RuntimeFormInterface shellForm = toggleForm.getParentForm(state);		
		RuntimeFormInterface detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_assign_accessorial_charges_detail_toggle_slot"), "Detail"  );	
		
		if(!detailForm.getName().equals("Blank"))
		{	
			DataBean focus = detailForm.getFocus();
			if(focus.isTempBio())
			{focus = (QBEBioBean)focus;}
			else
			{focus = (BioBean)focus;}
			
			
			Object descObj= focus.getValue("DESCRIP");
			if(descObj!= null && !descObj.toString().equalsIgnoreCase(""))
			{
				if (descObj.toString().indexOf("'") != -1)
				{
					String args[] = new String[1]; 						
					args[0] = "Description";
					//String errorMsg = getTextMessage("WMEXP_NO_QUOTES_ALLOWED",args,state.getLocale());
					throw new UserException("WMEXP_NO_QUOTES_ALLOWED",args);
					
				}
			}
			
			if(focus.isTempBio())
			{
		  	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		  	RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) headerForm;
			String formName = getFormTitle(listForm);
			
			//_log.debug("LOG_SYSTEM_OUT","\n\n\n**********FORM NAME: " +headerForm.getName(),100L);
			if(headerForm.getName().equals("wm_assign_accessorial_charges_receipts_list_view") || headerForm.getName().equals("wm_assign_accessorial_charges_receipts_detail_list_view"))
			{
				try
				{
				String key = focus.getValue("ACCUMULATEDCHARGESKEY").toString();
				
				
				String adKey = focus.getValue("SOURCEKEY").toString();
				String query = "wm_accessorial_charges.SOURCETYPE='AccessorialRECEIPT' and wm_accessorial_charges.SOURCEKEY='"+adKey +"'";
				
				_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);
				Query qry = new Query("wm_accessorial_charges", query, null);
				BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
				_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Num of records: " +newFocus.size(),100L);
				if(newFocus.size()<1){
					focus.setValue("LINETYPE", "NA");
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","New rec NA Linetype",100L);
				}
				else
				{					
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","New rec TA Linetype",100L);
					focus.setValue("LINETYPE", "TA");
				}
				}
				catch(NullPointerException e)
				{
					String[] param = new String[1];
					param[0] = formName;
					throw new UserException("WMEXP_NOT_SELECTED", param);
				}
				
			}
			else
			{
				focus.setValue("LINETYPE", "N");
			}
			}
		  	
			focus.setValue("ACCUMULATEDCHARGESKEY", focus.getValue("ACCUMULATEDCHARGESKEY"));
			focus.setValue("CHARGETYPE", "AC");
			
			
		}
		
		try{
			uow.saveUOW(true);
		}catch (UnitOfWorkException e){			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			//_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
			//_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_DELETING_BIO", null);
			}

		}
		uow.clearState();
		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting SaveAssignAccessorialCharges",100L);
		return RET_CONTINUE;
	}
	

	
	private String getFormTitle(RuntimeListFormInterface listForm) {
		// TODO Auto-generated method stub
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In getFormTitle",100L);
		final String ADJUSTMENT = "adjustment_list_view";
		final String ADJUSTMENTDETAIL = "adjustment_detail_list_view";
		final String ORDER = "shipmentorders_list_view";
		final String ORDERDETAIL = "shipmentorders_detail_list_view";
		final String RECEIPT = "receipts_list_view";
		final String RECEIPTDETAIL = "receipts_detail_list_view";
		final String TRANSFER = "transfer_list_view";
		final String TRANSFERDETAIL = "transfer_detail_list_view";
		String name= null;
		
		String listName= listForm.getName(); 
		
		if(listName.endsWith(ADJUSTMENT))
		{name= "Adjustment";}
		else if(listName.endsWith(ADJUSTMENTDETAIL))
		{name= "Adjustment Detail";}
		else if(listName.endsWith(ORDER))
		{name= "Shipment Order";}
		else if(listName.endsWith(ORDERDETAIL))
		{name= "Shipment Order Detail";}
		else if(listName.endsWith(RECEIPT))
		{name= "Receipt";}
		else if(listName.endsWith(RECEIPTDETAIL))
		{name= "Receipt Detail";}
		else if(listName.endsWith(TRANSFER))
		{name= "Transfer";}
		else if(listName.endsWith(TRANSFERDETAIL))
		{name= "Transfer Detail";}
		
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving getFormTitle",100L);
		return name;
	}
	
}
