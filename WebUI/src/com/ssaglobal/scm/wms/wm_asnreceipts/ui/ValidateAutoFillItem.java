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

//Import 3rd party packages and classes
import java.util.ArrayList;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;

public class ValidateAutoFillItem extends ActionExtensionBase{
	//Form name constants
	private static String LIST_SHELL = "listShellForm";
	private static String LIST_SLOT1 = "shellSlot1";
	private static String SUPPLIER = "wms_supplier_View";
	private static String TAB_GROUP = "tbgrp_slot";
	private static String TAB0 = "tab 0";
	private static String TAB4 = "tab 4";
	
	//Table name constants
	private static String TABLE_ITEM = "sku";
	
	//Widget name constants
	private static String OWNER = "STORERKEY";
	private static String UOM = "UOM";
	private static String ITEM = "SKU";
	private static String PACK = "PACKKEY";
	private static String TARIFF = "TARIFFKEY";
	private static String DESCRIPTION = "DESCR";
	private static String ITEM_DESCR = "SKUDESC";
	private static String ITEM_TYPE = "SKUTYPE";
	private static String ICW = "ICWFLAG";
	private static String ICD = "ICDFLAG";
	private static String LV_KEY = "LOTTABLEVALIDATIONKEY";
	private static String LV_REQ = "LOTTABLESREQ";
	private static String CWD_REQ = "CATCHWDREQ";
	
	//Food Enhancements - 3PL - Inventory Holds - Krishna Kuchipudi  - Aug-16-2010 - Start
	private static String  CONDITIONCODE = "CONDITIONCODE";
	//Food Enhancements - 3PL - Inventory Holds - Krishna Kuchipudi  - Aug-16-2010 - End
	
	//Error message constants
	private static String ERROR_MSG_OWNER = "WMEXP_ENTEROWNER";
	private static String ERROR_MSG_ITEM = "WMEXP_VALIDATESKU";
	
	//Constant strings
	private static String ZERO = "0";
	private static String ONE = "1";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String newItemValue = context.getSourceWidget().getDisplayValue().toUpperCase();
		//Get the storer from the Header
		String shellSlot1 = getParameter(LIST_SLOT1).toString();
		String listShellForm = getParameter(LIST_SHELL).toString();
		String value = null;
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
//		while(!(shellForm.getName().equals(listShellForm))){
//			shellForm = shellForm.getParentForm(state);
//		}
//		SlotInterface slot1 = shellForm.getSubSlot(shellSlot1);
//		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
//		SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TAB_GROUP);
//		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB0);			
		value = state.getCurrentRuntimeForm().getFormWidgetByName(OWNER).getDisplayValue();

		//03/11/2010 FW Commented out owner check for performance problem due to many item records (Incident3243632_Defect216574) -- Start
		/*
		//Executes only if detail form is a new screen
		String qry = TABLE_ITEM+"."+OWNER+"='"+value+"'";
		Query query = new Query(TABLE_ITEM, qry, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(query);
		//Throw exception for unrecognized value
		if(newFocus.size()<1){
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); //AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
			throw new UserException(ERROR_MSG_OWNER, new Object[]{});
		} 
		*/
		String qry = null;
		Query query = null;
		//03/11/2010 FW Commented out owner check for performance problem due to many item records (Incident3243632_Defect216574) -- End

		//Query sku table for data points
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		currentForm.getFormWidgetByName(PACK).setProperty("cursor focus widget", Boolean.TRUE);
		qry = TABLE_ITEM+"."+OWNER+"='"+value+"' AND "+TABLE_ITEM+"."+ITEM+"='"+newItemValue+"'";
		String packVal= null; //AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
		query = new Query(TABLE_ITEM, qry, null);		
		BioCollectionBean itemBio = uow.getBioCollectionBean(query);
		if ((itemBio == null)||(itemBio.size()<1)){
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); //AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
			String[] parameters = new String[2];
			parameters[0]= newItemValue;
			parameters[1]= value;
			currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue("");
			throw new UserException(ERROR_MSG_ITEM, parameters);
		}else{
			try{
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean) focus;
					context.getState().getCurrentRuntimeForm();
					qbe.set(PACK, isNull(itemBio, PACK));
					qbe.set(TARIFF, isNull(itemBio, TARIFF));
					//Food Enhancements - 3PL - Inventory Holds - Krishna Kuchipudi  - Aug-16-2010 - Start
					qbe.set(CONDITIONCODE, isNull(itemBio, "RECEIPTHOLDCODE"));
					//Food Enhancements - 3PL - Inventory Holds - Krishna Kuchipudi  - Aug-16-2010 - End
					packVal = isNull(itemBio, PACK);//AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
				}else{
					BioBean bio = (BioBean) focus;
					bio.set(PACK, isNull(itemBio, PACK));
					bio.set(TARIFF, isNull(itemBio, TARIFF));
					packVal = isNull(itemBio, PACK);//AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
				}
				UOMDefaultValue.fillDropdown(state, UOM, packVal); //AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
				currentForm.getFormWidgetByName(ITEM_DESCR).setDisplayValue(isNull(itemBio, DESCRIPTION));
				RuntimeFormWidgetInterface lottableReq = currentForm.getFormWidgetByName(LV_REQ);
				RuntimeFormWidgetInterface cwdReq = currentForm.getFormWidgetByName(CWD_REQ);
				String lvKey = isNull(itemBio, LV_KEY);
				if (ASNReceiptDetailPreRender.LottableRequiredCheck(lvKey, context)){
					lottableReq.setValue(ONE);
				}else{
					lottableReq.setValue(ZERO);
				}
				String icwflag = isNull(itemBio, ICW);
				String icdflag = isNull(itemBio, ICD);
				if ((icwflag.equalsIgnoreCase(ONE)) || (icdflag.equalsIgnoreCase(ONE))){
					cwdReq.setValue(ONE);
				}else{
					cwdReq.setValue(ZERO);
				}
				ArrayList params = new ArrayList();
				params.add(TAB4);
				RuntimeFormInterface supplierForm =null;
				if (!(isNull(itemBio, ITEM_TYPE).equalsIgnoreCase(ZERO))){
					supplierForm = FormUtil.findForm(toolbar, shellForm.getName(), SUPPLIER, params, context.getState());
					if(supplierForm!=null){
						supplierForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);						
					}
				}else{
					supplierForm = FormUtil.findForm(toolbar, shellForm.getName(), SUPPLIER, params, context.getState());
					if(supplierForm!=null){
						supplierForm.setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
					}
				}				
			}catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		return RET_CONTINUE;
	}
	
	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get(ZERO).get(widgetName)){
			result=focus.get(ZERO).get(widgetName).toString();
		}
		return result;
	}
}