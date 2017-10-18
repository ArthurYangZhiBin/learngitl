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
package com.ssaglobal.scm.wms.wm_allocation_management.ui;
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateAllocationManagement extends ActionExtensionBase{
	StateInterface state;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateAllocationManagement.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Executing ValidateAllocationManagement",100L);		
		state = context.getState();	
		ArrayList tabList = new ArrayList();
		tabList.add("wm_allocation_management_detail_toggle_detail");
		tabList.add("tab 1");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_detail_view",tabList,state);				
		RuntimeFormInterface detailListForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_list_view",state);
		RuntimeFormInterface pickDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_pick_detail_list",tabList,state);
		
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Detail Form:Null",100L);			
		if(pickDetailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Pick Detail Form:"+pickDetailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Pick Detail Form:Null",100L);			
		if(detailListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Detail List Form:"+detailListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Detail List Form:Null",100L);			
		
		//validate detail form fields		
		if(detailForm != null){		
			Object qtyToProcObj = detailForm.getFocus().getValue("QTYTOPROCESS");
			
			String qtyToProc = qtyToProcObj == null ?"":qtyToProcObj.toString();
			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","qtyToProcObj:"+qtyToProcObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","qtyToProc"+qtyToProc,100L);			
			//QtyToProcess must be > 0 if present
			if(qtyToProc.length() > 0){
				try {
					double qtyToProcNum = Double.parseDouble(qtyToProc);
					if(!(qtyToProcNum > 0)){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Qty to Process not greater than 0...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);						
						String args[] = new String[0]; 											
						String errorMsg = getTextMessage("WMEXP_QTY_TO_PROC_0",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}					
				} catch (NumberFormatException e) {						
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Qty to Process not a number...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
					String args[] = new String[0]; 											
					String errorMsg = getTextMessage("WMEXP_QTY_TO_PROC_NAN",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);			
				}
			}					
		}
		
//		validate detail list form fields		
		try {
			if(detailListForm != null){		
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Validating Detail List Form...",100L);				
				BioCollectionBean detailListFormFocus = (BioCollectionBean)detailListForm.getFocus();
				if(detailListFormFocus != null){
					for(int i = 0; i < detailListFormFocus.size(); i++){
						BioBean bio = (BioBean)detailListFormFocus.elementAt(i);
						if(bio.hasBeenUpdated("QTYTOPROCESS")){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Found Record With Updated QTYTOPROCESS...",100L);							
							Object qtyToProcObj = bio.get("QTYTOPROCESS");								
							String qtyToProc = qtyToProcObj == null?"":qtyToProcObj.toString();
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","qtyToProcObj:"+qtyToProcObj,100L);							
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","qtyToProc"+qtyToProc,100L);							
							
							//QtyToProcess must be > 0 if present
							if(qtyToProc.length() > 0){
								try {
									double qtyToProcNum = Double.parseDouble(qtyToProc);									
									if(!(qtyToProcNum > 0)){
										_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Qty to Process not greater than 0...",100L);										
										_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
										String args[] = new String[0]; 											
										String errorMsg = getTextMessage("WMEXP_QTY_TO_PROC_0",args,state.getLocale());
										throw new UserException(errorMsg,new Object[0]);
									}					
								} catch (NumberFormatException e) {	
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Qty to Process not a number...",100L);
									_log.debug("LOG_SYSTEM_OUT","Qty to Process not a number...",100L);
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
									String args[] = new String[0]; 											
									String errorMsg = getTextMessage("WMEXP_QTY_TO_PROC_NAN",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);			
								}
							}			
						}
					}
				}
			}
		} catch (EpiDataInvalidAttrException e1) {
			e1.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
			String args[] = new String[0]; 											
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
			String args[] = new String[0]; 											
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 
		
		try {
			if (pickDetailForm != null)
			{
				BioCollectionBean listFormCollection = (BioCollectionBean) pickDetailForm.getFocus();
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","! Header Validation",100L);				
				for (int i = 0; i < listFormCollection.size(); i++)
				{
					BioBean bean = (BioBean) listFormCollection.elementAt(i);
					if (bean.hasBeenUpdated("STATUS") || bean.hasBeenUpdated("QTY") || bean.hasBeenUpdated("DROPID"))
					{
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Performing STATUS validation on " + bean.get("CASEID").toString(),100L);						
						int status = Integer.parseInt(bean.get("STATUS").toString());
						if (status < 5)
						{
							String lot = bean.get("LOT") == null ? "" : bean.get("LOT").toString();
							String loc = bean.get("LOC") == null ? "" : bean.get("LOC").toString();
							String id = bean.get("ID") == null ? "" : bean.get("ID").toString();
							statusCombinationValidation(bean, lot, loc, id);
						}
						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Performing QTY validation on " + bean.get("CASEID").toString(),100L);
						String lot = bean.get("LOT") == null ? "" : bean.get("LOT").toString();
						String loc = bean.get("LOC") == null ? "" : bean.get("LOC").toString();
						String id = bean.get("ID") == null ? "" : bean.get("ID").toString();
						// Quantity Validation
						quantityValidation(bean, false, lot, loc, id);

						// Open Qty Validation
						openQtyValidation(bean, false);

					}
				}
			}
		} catch (EpiDataInvalidAttrException e) {			
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
			String args[] = new String[0]; 											
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DPException e) {			
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
			String args[] = new String[0]; 											
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiDataException e) {			
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
			String args[] = new String[0]; 											
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Leaving ValidateAllocationManagement",100L);
		return RET_CONTINUE;		
	}
	
	private void statusCombinationValidation(DataBean pickDetailFormFocus, String lot, String loc, String id) throws DPException, UserException
	{
		String query = "SELECT * " + "FROM LOTXLOCXID " + "WHERE " + "LOT = '" + lot.toUpperCase() + "' AND " + "LOC = '" + loc.toUpperCase()
				+ "' AND " + "ID  = '" + id.toUpperCase() + "' ";		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","///QUERY " + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 0)
		{
			//display error
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","! Throwing Combination error",100L);			
			String[] parameters = new String[1];
			parameters[0] = pickDetailFormFocus.getValue("CASEID").toString();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_PICK_COMBINATION",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);			
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","! Status Combination Validation passed",100L);			
		}
	}
	
	private void quantityValidation(DataBean pickDetailFormFocus, boolean isInsert, String lot, String loc, String id) throws DPException, EpiDataException, UserException
	{
		//Start of Quantity Validation

		//Retrieve Qty and QtyAllocated
		double availableQty = 0;
		String query = "SELECT QTY, QTYALLOCATED FROM LOTXLOCXID " + "WHERE " + "LOT = '" + lot.toUpperCase() + "' AND " + "LOC = '"
				+ loc.toUpperCase() + "' AND " + "ID  = '" + id.toUpperCase() + "' ";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			double qty = Double.parseDouble(results.getAttribValue(1).getAsString());
			double qtyAllocated = Double.parseDouble(results.getAttribValue(2).getAsString());
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","Quantity - " + qty + " QuantityAllocated - " + qtyAllocated,100L);			
			availableQty = qty - qtyAllocated;

		}
		else
		{
			//error
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Available Quantity " + availableQty,100L);		

		if (isInsert)
		{
			//Insert Quantity Validation
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Start of Insert Quantity Validation",100L);			
			double quantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!!! Quantity " + quantity,100L);				
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Quantity is blank",100L);				
			}
			if (quantity > availableQty)
			{
				//Construct error message
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
		}
		else
		{
			//Update Quantity Validation			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Start of Update Quantity Validation",100L);
			double quantity = 0;
			double originalQuantity = 0;
			Object tempQuantity = pickDetailFormFocus.getValue("QTY");
			if (!(isEmpty(tempQuantity)))
			{
				quantity = Double.parseDouble(tempQuantity.toString());				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!!! Quantity " + quantity,100L);
				//Retrieve original value
				Object tempOldQuantity = pickDetailFormFocus.getValue("QTY", true);
				if(tempOldQuantity == null)
					tempOldQuantity = "0.0";
				originalQuantity = Double.parseDouble(tempOldQuantity.toString());			
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!!! Old Quantity " + originalQuantity,100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!!! Available Quantity " + availableQty,100L);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Quantity is blank",100L);				
			}
			if (quantity > availableQty + originalQuantity)
			{
				String base = getTextMessage("WMEXP_PICK_QTY", new Object[] {}, state.getLocale());
				if (!isEmpty(lot))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOT", new Object[] { lot }, state.getLocale());
				}
				if (!isEmpty(loc))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LOC", new Object[] { loc }, state.getLocale());
				}
				if (!isEmpty(id))
				{
					base += getTextMessage("WMEXP_PICK_QTY_LPN", new Object[] { id }, state.getLocale());
				}
				throw new UserException(base, new Object[] {});
			}
		}
	}

	private void openQtyValidation(DataBean pickDetailFormFocus, boolean isInsert) throws DPException, UserException
	{		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@# Start of Open Qty Validation",100L);
		String orderNumber = pickDetailFormFocus.getValue("ORDERKEY").toString();
		String orderLineNumber = pickDetailFormFocus.getValue("ORDERLINENUMBER").toString();
		//Get Open Qty
		String query = "SELECT ORDERKEY,ORDERLINENUMBER, OPENQTY FROM ORDERDETAIL " + "WHERE ORDERKEY = '"
				+ orderNumber.toUpperCase() + "' " + "AND " + "ORDERLINENUMBER = '" + orderLineNumber.toUpperCase() + "'";		
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		double openQty = 0;
		if (results.getRowCount() == 1)
		{
			openQty = Double.parseDouble(results.getAttribValue(3).getAsString());
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!%% OpenQty " + openQty,100L);			
		}
		else
		{
			//error
		}
		//Get Qty Entered
		double qty = Double.parseDouble(pickDetailFormFocus.getValue("QTY").toString());
		//Get Sum All PickDetail Qty From DB
		query = "SELECT ORDERKEY, ORDERLINENUMBER, SUM(QTY) " + "FROM PICKDETAIL "
				+ "GROUP BY ORDERKEY, ORDERLINENUMBER " + "HAVING " + "(ORDERKEY = '" + orderNumber.toUpperCase() + "') " + "AND "
				+ "(ORDERLINENUMBER = '" + orderLineNumber.toUpperCase() + "')";
		results = WmsWebuiValidationSelectImpl.select(query);
		double sumQty = 0;
		if (results.getRowCount() == 1)
		{
			sumQty = Double.parseDouble(results.getAttribValue(3).getAsString());
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!%% Sum of PickDetailQty " + sumQty,100L);			
		}
		else
		{
			//display error
			//			String[] parameters = new String[2];
			//			parameters[0] = orderNumber;
			//			parameters[1] = orderLineNumber;
			//			throw new UserException("WMEXP_PICK_ORDER_KEY", parameters);
		}
		double allQty;
		if (isInsert)
		{
			allQty = qty + sumQty;

		}
		else
		{
			double originalQty = Double.parseDouble(pickDetailFormFocus.getValue("QTY", true).toString());			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","! Old Value " + originalQty,100L);			
			allQty = sumQty - originalQty + qty;

		}
		if (allQty > openQty)
		{
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!Raise Error Sum of PICKDETAIL.QTY > OPENQTY " + allQty + " > " + openQty,100L);						
			//raise error			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_PICK_OPENQTY",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);						
		}
		else
		{			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEALLOCMGT","!@(* Sum of PICKDETAIL.QTY < OPENQTY " + allQty + " < " + openQty,100L);			
		}
		//if new, qty + sumQty
		//if update, sumQty - (original qty) + qty 
	}
	
	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}