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

//Import 3rd party packages and classes
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;//AW 10/15/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;

public class ShipmentOrderPreDelete extends ActionExtensionBase{
	private final static String SHELL_NAME = "wm_list_shell_shipmentorder";
	private final static String SHELL_SLOT_1 = "list_slot_1";
	private static final String SLOT_2 = "list_slot_2";
	private static final String TOGGLE_SLOT = "wm_shipmentorderdetail_toggle_view";
	private static final String DETAIL_LIST_SLOT = "LIST_SLOT";
	private static final String TAB_GROUP_SLOT = "tbgrp_slot";
	private static final String TAB_0 = "tab 0";
	private static final String TAB_1 = "tab 1";
	private static final String LIST_TAB = "wm_shipmentorderdetail_list_view";
	private static final String DETAIL_TAB = "wm_shipmentorderdetail_detail_view";
	private final static String TYPE = "TYPE";
	private final static String ORDER = "ORDERKEY";
	private final static String ERROR_MESSAGE = "WMEXP_DELETE_INV_TYPE";
	
	private static final String ACTION_TYPE_DELETE_DETAIL_LINE = "DELETE_DETAIL_LINE";

	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException, EpiException{
		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm();
		while(!shell.getName().equalsIgnoreCase(SHELL_NAME)){
			shell = shell.getParentForm(state);
		}
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT_1), null);
		if(header.isListForm()){
			RuntimeListFormInterface list = (RuntimeListFormInterface)header;
			ArrayList selected = list.getAllSelectedItems();
			String[] parameter = new String[1];
			parameter[0] = "";
			for(int index=0; index<selected.size(); index++){
				BioBean bio = (BioBean)selected.get(index);
				String type = bio.get(TYPE).toString();
				if(type.equals("20")){				
					if(!parameter[0].equalsIgnoreCase("")){
						parameter[0]+=", ";
					}else{
						parameter[0]+=readLabel(list.getFormWidgetByName(ORDER))+": ";
					}
					parameter[0]+=bio.get(ORDER);
				}
			}
			if(!parameter[0].equalsIgnoreCase("")){
				throw new FormException(ERROR_MESSAGE, parameter);
			}
		}else{
			RuntimeFormInterface detail;
			SlotInterface toggleSlot = state.getRuntimeForm(shell.getSubSlot(SLOT_2), null).getSubSlot(TOGGLE_SLOT);
			int formNumber = state.getSelectedFormNumber(toggleSlot);
			if(formNumber==0){
				detail = state.getRuntimeForm(state.getRuntimeForm(toggleSlot, LIST_TAB).getSubSlot(DETAIL_LIST_SLOT), null);
				String currentRuntimeFormName = state.getCurrentRuntimeForm().getName();
//				_log.debug("LOG_SYSTEM_OUT","***shellname="+state.getCurrentRuntimeForm().getName(),100L);
				//Raghu.sn: Food Enhancements : Delete Demand Allocation : sep-21-2010 : Added validation for demand allocation toolbar
				if("wm_shipmentorder_pickdetail_list_view Toolbar".equalsIgnoreCase(currentRuntimeFormName)
						|| "wm_shipmentorder_preallocate_list_view Toolbar".equalsIgnoreCase(currentRuntimeFormName)
						|| "wm_shipmentorder_demandallocation_view Toolbar".equalsIgnoreCase(currentRuntimeFormName)){//it is pick detail delete
					new ShipmentOrderPreSave(state).validateOrderDetailListValues(header.getFocus(), detail, state.getDefaultUnitOfWork());//AW 10/15/2008 Machine#:2093019 SDIS:SCM-00000-05440
				}else{
					new ShipmentOrderPreSave(state).validateOrderDetailListValues(header.getFocus(), detail, ACTION_TYPE_DELETE_DETAIL_LINE, state.getDefaultUnitOfWork());//AW 10/15/2008 Machine#:2093019 SDIS:SCM-00000-05440);
				}
			}else{
				detail = state.getRuntimeForm(state.getRuntimeForm(toggleSlot, DETAIL_TAB).getSubSlot(TAB_GROUP_SLOT), TAB_0);
				RuntimeFormInterface detail2 = state.getRuntimeForm(detail.getParentForm(state).getSubSlot(TAB_GROUP_SLOT), TAB_1);
				new ShipmentOrderPreSave(state).validateOrderDetailFormValues(header.getFocus(), detail, detail2, state.getDefaultUnitOfWork());
			}
		}
		return RET_CONTINUE;
	}
	
	//Find label value base on locale
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}