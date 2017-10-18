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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

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
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class PreDeleteRefInFTDetails extends ActionExtensionBase{
	//Form reference variables
	private final static String FT_TOOLBAR = "wm_list_shell_facilitytransfer Toolbar";
	private final static String FT_TOGGLE_TOOLBAR = "wm_facilitytransfer_orderdetail_toggle_view Toolbar";
	private final static String SHELL_SLOT = "slot1";
	private final static String TAB_GROUP_SLOT = "tbgrp_slot";
	private final static String TAB_ZERO = "Tab0"; 
	private final static String TOGGLE_SLOT = "wm_facilitytransfer_orderdetail_toggle_slot";
	private final static String LIST_TAB = "wm_facilitytransfer_orderdetail_list_view";
	
	//Bio Names
	private final static String PICK_TABLE = "wm_pickdetail";
	private final static String TASK_TABLE = "wm_taskdetail";
	private final static String OD_TABLE = "wm_orderdetail";
	
	//Attribute names
	private final static String ORDER = "ORDERKEY";
	private final static String ORDERLN = "ORDERLINENUMBER";
	private final static String STATUS = "STATUS";
	private final static String ITEM = "SKU";
	
	//Error Messages
	private final static String ERROR_MESSAGE_REF_IN_DETAILS = "WMEXP_SO_PDRID";
	private final static String ERROR_MESSAGE_NOT_LIST = "WMEXP_Non_list_view_delete";
	private final static String ERROR_MESSAGE_ALLOCATED = "WMEXP_FTD_ALLOCATED";
	private final static String ERROR_MESSAGE_RELEASED = "WMEXP_FTD_RELEASED";
	private final static String ERROR_MESSAGE_HEADER_ALLOCATED = "WMEXP_FT_ALLOCATED";
	private final static String ERROR_MESSAGE_HEADER_RELEASED = "WMEXP_FT_RELEASED";
	
	public PreDeleteRefInFTDetails(){
	}
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeListFormInterface listForm = null;
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		String formName = form.getName();
		String[] parameter = new String[1];		//String array for error message
		BioCollectionBean resultPD = null, resultTD = null;
		String qryPDstatement = null, qryTDstatement = null;
		//Create query statements based on bio
		RuntimeFormInterface shellForm = form.getParentForm(state);
		RuntimeFormInterface headerForm;
		RuntimeFormInterface currentForm;
		if(formName.equals(FT_TOOLBAR)){
			headerForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT), null);
			currentForm = headerForm;
		}else{
			headerForm = state.getRuntimeForm(state.getRuntimeForm(shellForm.getParentForm(state).getSubSlot(SHELL_SLOT), null).getSubSlot(TAB_GROUP_SLOT), TAB_ZERO);
			currentForm = state.getRuntimeForm(shellForm.getSubSlot(TOGGLE_SLOT),LIST_TAB);
		}
		if(currentForm.isListForm()){
			listForm = (RuntimeListFormInterface)currentForm;
		}else{
			throw new FormException(ERROR_MESSAGE_NOT_LIST, null);
		}

		String orderKey;
		ArrayList selected = listForm.getSelectedItems();
		
		for(int index=0; index<selected.size(); index++){
			BioBean bean = (BioBean)selected.get(index);
			if(formName.equals(FT_TOOLBAR)){
				orderKey = bean.get(ORDER).toString();
			}else{
				orderKey = headerForm.getFormWidgetByName(ORDER).getValue().toString();
			}

			parameter[0]= readLabel(headerForm.getFormWidgetByName(ORDER))+" "+orderKey;
			qryPDstatement = PICK_TABLE+"."+ORDER+"='"+orderKey+"'";
			qryTDstatement = TASK_TABLE+"."+ORDER+"='"+orderKey+"'";
			
			if(formName.equals(FT_TOGGLE_TOOLBAR)){
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
		}
		
		if(formName.equals(FT_TOGGLE_TOOLBAR)){
			String[] parameters = new String[2];
			parameters[0] = parameters[1] = "";
			//Verify details are not allocated
			for(int index=0; index<selected.size(); index++){
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
			for(int index=0; index<selected.size(); index++){
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
		return RET_CONTINUE;
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}