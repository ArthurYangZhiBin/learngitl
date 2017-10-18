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
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

// Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_pickdetail.ui.CWCDValidationUtil;

public class SOPickShipAction extends ActionExtensionBase {
	//Static form names
	private static final String SLOT_2 = "list_slot_2";
	private static final String SHELL = "wm_list_shell_shipmentorder";
	private static final String TOGGLE_SLOT = "wm_shipmentorderdetail_toggle_view";
	private static final String LIST_TAB = "wm_shipmentorderdetail_list_view";
	private static final String DETAIL_LIST_SLOT = "LIST_SLOT";
	
	//Static attribute names
	private static final String OPEN_QTY = "OPENQTY";
	private static final String PACK = "PACKKEY";
	private static final String UOM = "UOM";
	private static final String STATUS = "STATUS";
	private static final String ORDER = "ORDERKEY";
		
	//Static Error Message Names
	private static final String ERROR_MSG = "WMEXP_NONE_SELECTED";
	private static final String ERROR_MSG_PICK_SHIP = "WMEXP_PICK_SHIP";
	
	
	@Override
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 05/19/2009   AW  SDIS:SCM-00000-06871 Machine:2353530
	 * 					UOM conversion is now done in the front end.
	 *                  Changes were made accordingly. 
	 * 03/18/2010   FW  Comment out 'attemptSave(uow)' and it is moved to the outside of loop 
	 * 					for possible memory spick issue (Defect217952) 
	 */

	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface tempHeaderForm = state.getCurrentRuntimeForm().getParentForm(state);

		RuntimeListFormInterface listForm = null;
		if (tempHeaderForm.isListForm()){
			listForm = (RuntimeListFormInterface) tempHeaderForm;
		}

		ArrayList items = listForm.getAllSelectedItems();	
		if(items==null){
			throw new FormException(ERROR_MSG, null);
		}
		//03/18/2010   FW  Moved object's definition to be outside of loop (Defect217952)  -- Start
		//iterate items
		BioBean selectedItem = null;
		for (Iterator it = items.iterator(); it.hasNext();){
			selectedItem = (BioBean) it.next();
		//03/18/2010   FW  Moved object's definition to be outside of loop (Defect217952)  -- End
			int status = isNull(selectedItem.getValue(STATUS)) ? -1
					: Integer.parseInt(selectedItem.getValue(STATUS).toString());

			//Retrieve Batch Flag
			String orderNum = selectedItem.getValue(ORDER).toString();
			int batchFlag = 0;
			String queryOrders = "SELECT BATCHFLAG FROM ORDERS WHERE ("+ORDER+"='" + orderNum + "')";

			EXEDataObject results = WmsWebuiValidationSelectImpl.select(queryOrders);
			if (results.getRowCount() == 1){
				batchFlag = Integer.parseInt(results.getAttribute(1).value.getAsString());
			}

			if (batchFlag == 1 && (status < 7)){ //Status 7 = Sorted 
				//raise error
				throw new UserException(ERROR_MSG_PICK_SHIP, new Object[] {});
			}

			if (status != 9){ //Status 9 = Shipped 
				//Handle UOM conversion
				RuntimeFormInterface shell = state.getCurrentRuntimeForm();
				while(!shell.getName().equals(SHELL)){
					shell = shell.getParentForm(state);
				}
				//SM - ISSUE 7542: Adjusted form search to include new normal form container in the Shipment Order Details
				RuntimeListFormInterface list = (RuntimeListFormInterface)state.getRuntimeForm(state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SLOT_2), null).getSubSlot(TOGGLE_SLOT), LIST_TAB).getSubSlot(DETAIL_LIST_SLOT), null);
				//SM - ISSUE 7542: End update
				BioCollectionBean bcList = (BioCollectionBean)list.getFocus();
				boolean toggle = false;
				
				//Convert open qty to eaches
				for(int index=0; index<bcList.size(); index++){
					BioBean bio = bcList.get(""+index);
					if(bio.hasBeenUpdated(OPEN_QTY)){
						Object packVal = bio.get(PACK);//AW 10/15/2008 Machine#:2093019 SDIS:SCM-00000-05440
						String uom = bio.get(UOM).toString();
						if(packVal !=null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
							String uom3 = UOMMappingUtil.getPACKUOM3Val(packVal.toString(), uow);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
							if(!uom.equals(uom3)){ //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
							toggle = true;
							String pack = packVal.toString();//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
							String qty = bio.get(OPEN_QTY).toString();
							bio.set(OPEN_QTY, UOMMappingUtil.convertUOMQty(uom,UOMMappingUtil.UOM_EA, qty, pack, state, UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
						}
						}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					}
				}if(toggle){
		//03/18/2010 FW: Comment out 'attemptSave(uow)' and it is moved to the outside of loop for possible memory spick issue (Defect217952) -- Start
					/*
					//Update UOM conversion first
					if(attemptSave(uow)){
						return RET_CANCEL;
					}
					*/
				}
				
				//set status to 9 and save
				selectedItem.setValue(STATUS, new Integer(9));
				// CWCDValidation
				CWCDValidationUtil cwcd = new CWCDValidationUtil(state, CWCDValidationUtil.Type.SO);
				cwcd.cwcdListValidation(selectedItem, 9);
				/*
				if(attemptSave(uow)){
					return RET_CANCEL;
				}
				
				

				uow.clearState();
				*/
			}
		}
		
		if(attemptSave(uow)){
			return RET_CANCEL;
		}
		
		uow.clearState();
		
		selectedItem = null;
		//03/18/2010 FW: Comment out 'attemptSave(uow)' and it is moved to the outside of loop for possible memory spick issue (Defect217952) -- End
		
		listForm.setSelectedItems(null);
		result.setSelectedItems(null);
	    BioBean headerBioBean = (BioBean)tempHeaderForm.getParentForm(state).getParentForm(state).getFocus();
		result.setFocus(headerBioBean);
		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue) throws EpiDataException{
		if (attributeValue == null)	{
			return true;
		}
		else{
			return false;
		}
	}
	
	
	private boolean attemptSave(UnitOfWorkBean uowb) throws EpiException, UserException{
		try{
			uowb.saveUOW(true);
		} catch (UnitOfWorkException e){
			// Handle Exceptions
			Throwable nested = (e).findDeepestNestedException();
			if (nested instanceof ServiceObjectException){
				Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
				String exceptionMessage = nested.getMessage();
				Matcher matcher = errorPattern.matcher(exceptionMessage);
				if (matcher.find()){
					exceptionMessage = matcher.group(1);
				}
				throw new UserException(exceptionMessage, new Object[] {});
			}
			return true;
		}
		return false;
	}
}