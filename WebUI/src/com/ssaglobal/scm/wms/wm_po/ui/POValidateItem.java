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
package com.ssaglobal.scm.wms.wm_po.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class POValidateItem extends ActionExtensionBase{
	//Form name constants
	private static String SHELL = "wms_list_shell";
	private static String SLOT1 = "list_slot_1";
	private static String TAB_GROUP = "tbgrp_slot";
	private static String TAB1 = "tab 1";
	
	//Table name constants
	private static String TABLE_ITEM = "sku";
	
	//Variable name constants
	private static String OWNER = "STORERKEY";
	private static String ITEM = "SKU";
	private static String UOM = "UOM";
	private static String PACK = "PACKKEY";
	private static String DESCRIPTION = "DESCR";
	private static String DESCR_WIDGET = "SKUDESCRIPTION";
	
	//Error message constants
	private static String ERROR_MSG_OWNER = "WMEXP_ENTEROWNER";
	private static String ERROR_MSG_ITEM = "WMEXP_SO_ILQ_FOREIGN_INVALID";
	
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String newItemValue = context.getSourceWidget().getDisplayValue();
		newItemValue = newItemValue == null ? null : newItemValue.toUpperCase();
		//Get the storer from the Header
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
		while(!(shellForm.getName().equals(SHELL))){
			shellForm = shellForm.getParentForm(state);
		}
		SlotInterface slot1 = shellForm.getSubSlot(SLOT1);
		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
		SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TAB_GROUP);
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB1);
		RuntimeFormWidgetInterface owner = normalHeaderForm.getFormWidgetByName(OWNER);
		String value = owner.getDisplayValue();
		
		//Executes only if detail form is a new screen
		String qry = TABLE_ITEM+"."+OWNER+"='"+value+"'";
		Query loadBiosQry = new Query(TABLE_ITEM, qry, null);
		BioCollectionBean newFocus = uowb.getBioCollectionBean(loadBiosQry);
		//Throw exception for unrecognized value
		if(newFocus.size()<1){
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			throw new UserException(ERROR_MSG_OWNER, new Object[]{});
		} 

		//Query sku table for data points
		String itemQry = TABLE_ITEM+"."+OWNER+"='"+value+"' AND "+TABLE_ITEM+"."+ITEM+"='"+newItemValue+"'";
		Query itemQuery = new Query(TABLE_ITEM, itemQry, null);		
		BioCollectionBean itemBio = uowb.getBioCollectionBean(itemQuery);
		if ((itemBio == null)||(itemBio.size()<1)){
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD);//AW Machine#:2093019 SDIS:SCM-00000-05440
			String[] parameters = new String[2];
			parameters[0]= colonStrip(readLabel(context.getSourceWidget()));
			parameters[1]= colonStrip(readLabel(owner));
			throw new UserException(ERROR_MSG_ITEM, parameters);
		}else{
			try{
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean) focus;
					qbe.set(ITEM, isNull(itemBio, ITEM));
					qbe.set(DESCR_WIDGET, isNull(itemBio, DESCRIPTION));
					qbe.set(PACK, isNull(itemBio, PACK));
				}else{
					BioBean bio = (BioBean) focus;
					bio.set(ITEM, isNull(itemBio, ITEM));
					bio.set(DESCR_WIDGET, isNull(itemBio, DESCRIPTION));
					bio.set(PACK, isNull(itemBio, PACK));
				}
				UOMDefaultValue.fillDropdown(state, UOM, isNull(itemBio, PACK)); //AW Machine#:2093019 SDIS:SCM-00000-05440
			}catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		return RET_CONTINUE;
	}
	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}
	
	public String colonStrip(String label){
		return 	label.substring(0, label.length()-1);
	}
	
	public String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}