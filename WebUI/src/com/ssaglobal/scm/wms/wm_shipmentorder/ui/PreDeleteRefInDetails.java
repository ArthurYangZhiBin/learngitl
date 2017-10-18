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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui.LottableValidationPreRenderDetail;

public class PreDeleteRefInDetails extends ActionExtensionBase{
	//Form reference variables
	private final static String SO_TOOLBAR = "wm_list_shell_shipmentorder Toolbar";
	private final static String SO_TOGGLE_TOOLBAR = "wm_shipmentorderdetail_toggle_view Toolbar";
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String TAB_GROUP_SLOT = "tbgrp_slot";
	private final static String TAB_ZERO = "tab 0"; 
	private final static String TOGGLE_SLOT = "wm_shipmentorderdetail_toggle_view";
	private final static String CONTAINER_SLOT = "LIST_SLOT";
	private final static String LIST_TAB = "wm_shipmentorderdetail_list_view";
	
	
	//Attribute names
	private final static String ORDER = "ORDERKEY";
	private final static String ORDERLN = "ORDERLINENUMBER";
	private final static String STATUS = "STATUS";
	private final static String ITEM = "SKU";
	
	//Error Messages
	private final static String ERROR_MESSAGE_ORDER_DELETE = "WMEXP_SO_ORDER_DELETE_VALIDATION";
	private final static String ERROR_MESSAGE_ORDERLINE_DELETE = "WMEXP_SO_ORDERLINE_DELETE_VALIDATION";
	private final static String ERROR_MESSAGE_NOT_LIST = "WMEXP_Non_list_view_delete";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreDeleteRefInDetails.class);
	
	public PreDeleteRefInDetails(){
	}
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeListFormInterface listForm = null;
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		String formName = form.getName();
		//Create query statements based on bio
		RuntimeFormInterface shellForm = form.getParentForm(state);
		RuntimeFormInterface headerForm;
		RuntimeFormInterface currentForm;
		if(formName.equals(SO_TOOLBAR)){
			headerForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT), null);
			currentForm = headerForm;
		}else{
			headerForm = state.getRuntimeForm(state.getRuntimeForm(shellForm.getParentForm(state).getSubSlot(SHELL_SLOT), null).getSubSlot(TAB_GROUP_SLOT), TAB_ZERO);
			currentForm = state.getRuntimeForm(state.getRuntimeForm(shellForm.getSubSlot(TOGGLE_SLOT),LIST_TAB).getSubSlot(CONTAINER_SLOT), null);
		}
		if(currentForm.isListForm()){
			listForm = (RuntimeListFormInterface)currentForm;
		}else{
			throw new FormException(ERROR_MESSAGE_NOT_LIST, null);
		}

		String orderKey;
		String orderStatus = "";
		String[] parameter = new String[2];
		parameter[0] = parameter[1] = "";
		ArrayList selected = listForm.getSelectedItems();
		int numSelected = selected==null ? 0 : selected.size();
		if(formName.equals(SO_TOOLBAR)){		//Machinename381298-SDIS07596
			for(int index=0; index<numSelected; index++){
				
				BioBean bean = (BioBean)selected.get(index);
				orderKey = bean.get(ORDER).toString();
				orderStatus = bean.get(STATUS).toString();
				if(Integer.parseInt(orderStatus) >= 11){//part allocated
					String desc = this.getOrderStatusDesc(uowb, orderStatus);
					parameter[0] = orderKey;
					parameter[1] = desc;				
					throw new FormException(ERROR_MESSAGE_ORDER_DELETE, parameter);
				}
			}
		}
		
		if(formName.equals(SO_TOGGLE_TOOLBAR)){
			String[] parameters = new String[3];
			parameters[0] = parameters[1] = parameters[2] ="";
			//Verify details are not allocated
			for(int index=0; index<numSelected; index++){
				BioBean bio = (BioBean)selected.get(index);
				String orderLineStatus = bio.get(STATUS).toString();
				if(Integer.parseInt(orderLineStatus) > 11){						
						String desc = this.getOrderStatusDesc(uowb, orderLineStatus);
						parameters[0] = bio.get(ORDERLN).toString(); 
						parameters[1] = bio.get(ITEM).toString();
						parameters[2] = desc;
						throw new FormException(ERROR_MESSAGE_ORDERLINE_DELETE, parameters);
				}
			}

		}
/*
		for(int index=0; index<numSelected; index++){
			BioBean bean = (BioBean)selected.get(index);
			if(formName.equals(SO_TOOLBAR)){
				orderKey = bean.get(ORDER).toString();
			}else{
				orderKey = headerForm.getFormWidgetByName(ORDER).getValue().toString();
			}

			parameter[0]= readLabel(headerForm.getFormWidgetByName(ORDER))+" "+orderKey;
			qryPDstatement = PICK_TABLE+"."+ORDER+"='"+orderKey+"'";
			qryTDstatement = TASK_TABLE+"."+ORDER+"='"+orderKey+"'";
			
			if(formName.equals(SO_TOGGLE_TOOLBAR)){
				RuntimeFormInterface detailForm = currentForm;
				String orderLineNumber = bean.get(ORDERLN).toString();
				parameter[0] = parameter[0]+" , "+readLabel(detailForm.getFormWidgetByName(ORDERLN))+" "+orderLineNumber;
				qryPDstatement += " and "+PICK_TABLE+"."+ORDERLN+"='"+orderLineNumber+"'";
				qryTDstatement += " and "+TASK_TABLE+"."+ORDERLN+"='"+orderLineNumber+"'";
			}
			qryPDstatement += " and "+PICK_TABLE+"."+STATUS+">5";
			qryTDstatement += " and "+TASK_TABLE+"."+STATUS+">'3'";
			Query qryPD = new Query(PICK_TABLE, qryPDstatement, null);
			resultPD = uowb.getBioCollectionBean(qryPD);
			
			Query qryTD = new Query(TASK_TABLE, qryTDstatement, null);
			resultTD = uowb.getBioCollectionBean(qryTD);
			
			//End delete if there are related records in pick detail or task detail
			if(resultPD.size()>0 || resultTD.size()>0){
				throw new FormException(ERROR_MESSAGE_REF_IN_DETAILS, parameter);
			}
			
			//Verify details are unallocated
			String[] parameters = new String[2];
			parameters[0] = parameters[1] = "";
			if(!isDetailList){
				String queryString = OD_TABLE+"."+ORDER+"='"+orderKey+"'";
				Query query = new Query(OD_TABLE, queryString, null);
				BioCollectionBean bc = uowb.getBioCollectionBean(query);
				//Verify details are not allocated
				for(int i=0; i<bc.size(); i++){
					BioBean bio = (BioBean)bc.elementAt(i);
					String status = bio.get(STATUS).toString();
					if(status.equals("17")){
						if(parameters[0].equalsIgnoreCase("")){
							parameters[0] = orderKey;
							parameters[1] = bio.get(ORDERLN).toString(); 
						}else{
							parameters[1] += ", " + bio.get(ORDERLN).toString(); 
						}
					}
				}
				//Notify user of allocated details
				if(!parameters[0].equalsIgnoreCase("")){
					throw new FormException(ERROR_MESSAGE_HEADER_ALLOCATED, parameters);
				}
				//Verify details are not released
				for(int i=0; i<bc.size(); i++){
					BioBean bio = (BioBean)bc.elementAt(i);
					String status = bio.get(STATUS).toString();
					if(status.equals("19")){
						if(parameters[0].equalsIgnoreCase("")){
							parameters[0] = orderKey;
							parameters[1] = bio.get(ORDERLN).toString(); 
						}else{
							parameters[1] += ", " + bio.get(ORDERLN).toString(); 
						}
					}
				}
				//Notify user of released details
				if(!parameters[0].equalsIgnoreCase("")){
					throw new FormException(ERROR_MESSAGE_HEADER_RELEASED, parameters);
				}
			}else{
				String status = bean.get(STATUS).toString();
				if(status.equals("17")){
					if(parameters[0].equalsIgnoreCase("")){
						parameters[0] = orderKey;
						parameters[1] = bean.get(ORDERLN).toString(); 
					}else{
						parameters[1] += ", " + bean.get(ORDERLN).toString(); 
					}
				}
				//Notify user of allocated details
				if(!parameters[0].equalsIgnoreCase("")){
					throw new FormException(ERROR_MESSAGE_HEADER_ALLOCATED, parameters);
				}
			}

		}
		
		if(formName.equals(SO_TOGGLE_TOOLBAR)){
			String[] parameters = new String[2];
			parameters[0] = parameters[1] = "";
			//Verify details are not allocated
			for(int index=0; index<numSelected; index++){
				BioBean bio = (BioBean)selected.get(index);
				String status = bio.get(STATUS).toString();
				if(status.equals("17")){
					if(parameters[0].equalsIgnoreCase("")){
						parameters[0] = bio.get(ORDERLN).toString(); 
						parameters[1] = bio.get(ITEM).toString();
					}else{
						parameters[0] += ", " + bio.get(ORDERLN).toString(); 
						parameters[1] += ", " + bio.get(ITEM).toString();
					}
				}
			}
			//Notify user of allocated details
			if(!parameters[0].equalsIgnoreCase("")){
				throw new FormException(ERROR_MESSAGE_ALLOCATED, parameters);
			}
			//Verify details are not released
			for(int index=0; index<numSelected; index++){
				BioBean bio = (BioBean)selected.get(index);
				String status = bio.get(STATUS).toString();
				if(status.equals("19")){
					if(parameters[0].equalsIgnoreCase("")){
						parameters[0] = bio.get(ORDERLN).toString(); 
						parameters[1] = bio.get(ITEM).toString();
					}else{
						parameters[0] += ", " + bio.get(ORDERLN).toString(); 
						parameters[1] += ", " + bio.get(ITEM).toString();
					}
				}
			}
			//Notify user of released details
			if(!parameters[0].equalsIgnoreCase("")){
				throw new FormException(ERROR_MESSAGE_RELEASED, parameters);
			}
		}
*/
		return RET_CONTINUE;
	}
	/*
	 * this function is to get order status desc to be displayed in error message.
	 * input: UnitOfWorkBean and statusID
	 * output: order status description
	 */
	public String getOrderStatusDesc(UnitOfWorkBean uowb, String statusId) throws EpiException{
		String qry = "wm_orderstatussetup.CODE='"+statusId+"'";
		Query query = new Query("wm_orderstatussetup", qry, null);
		BioCollectionBean result = uowb.getBioCollectionBean(query);
		BioBean bioBean = result.get("0");
		String desc = bioBean.get("DESCRIPTION").toString();
		return desc;
	}
	
	//end of //Machinename381298-SDIS07596
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}