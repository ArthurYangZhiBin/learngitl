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

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class OnClickNew  extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnClickNew.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing OnClickNew",100L);
		String formName= null;
		
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
		RuntimeFormInterface shellForm = toggleForm.getParentForm(state);

		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) headerForm;
	
		RuntimeFormInterface detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_assign_accessorial_charges_detail_toggle_slot"), "Detail"  );	
		
		DataBean listFocus = listForm.getFocus();
		if (listFocus instanceof BioCollection){
			formName= getFormTitle(listForm);
			
			ArrayList itemsSelected = listForm.getAllSelectedItems();
			if(itemsSelected != null && itemsSelected.size() ==1)
			{
				 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Items Selected",100L);
				Iterator bioBeanIter = itemsSelected.iterator();
				try{
					BioBean bean= null;
						for(; bioBeanIter.hasNext(); )
						{
							bean = (BioBean)bioBeanIter.next();				
						}
				}
				catch(RuntimeException e)
				{
					e.printStackTrace();}	
			}
			else
			{
			}
			
			
		}
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting OnClickNew",100L);		
	return RET_CONTINUE;	
	}

	private String getFormTitle(RuntimeListFormInterface listForm) {
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In getFormTitle",100L);	
		// TODO Auto-generated method stub
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
