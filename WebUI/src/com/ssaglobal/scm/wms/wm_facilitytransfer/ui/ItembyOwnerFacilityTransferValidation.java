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

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil; //AW 10/13/2008 Machine#:2093019 SDIS:SCM-00000-05440

public class ItembyOwnerFacilityTransferValidation extends ActionExtensionBase{
	//Form static strings
	private static String SHELL_NAME = "wm_list_shell_facilitytransfer";
	private static String SHELL_SLOT_1 = "slot1";
	private static String SHELL_SLOT_2 = "slot2";
	private static String TOGGLE_SLOT = "wm_facilitytransfer_orderdetail_toggle_slot";
	private static String DETAIL_TAB = "wm_facilitytransfer_orderdetail_detail_view";
	private static String TAB_GROUP_SLOT = "tbgrp_slot";
	private static String TAB_0 = "Tab0";
	private static String TAB_1 = "Tab1";
	
	//Static table names
	private static String ITEM_TABLE = "sku";
	private static String STRATEGY_TABLE = "wm_strategy";
	private static String ALLOC_STRAT_TABLE = "wm_allocatestrategy";
	
	//Static attribute names
	private static String ITEM = "SKU";
	private static String STRATEGY = "STRATEGYKEY";
	private static String OWNER = "STORERKEY";
	private static String UOM = "UOM";
	private static String ITEM_DESCRIPTION = "SKU_DESCRIPTION";
	private static String ITEM_DESCRIPTION_SOURCE = "DESCR";
	private static String PACK = "PACKKEY";
	private static String TARIFF = "TARIFFKEY";
	private static String ITEM_ROTATION = "SKUROTATION";
	private static String ITEM_ROTATION_SOURCE = "ROTATEBY";
	private static String ROTATION = "ROTATION";
	private static String ROTATION_SOURCE = "DEFAULTROTATION";
	private static String SHELFLIFE = "SHELFLIFE";
	private static String ALLOC_STRAT = "ALLOCATESTRATEGYKEY";
	private static String PREALLOC_STRAT = "PREALLOCATESTRATEGYKEY";
	private static String ALLOC_STRAT_TYPE = "ALLOCATESTRATEGYTYPE";
	
	//Static error message names
	private static String ERROR_NULL_VALUE = "WMEXP_SO_ILQ_Null";
	private static String ERROR_INVALID_FOREIGN = "WMEXP_SO_ILQ_FOREIGN_INVALID";
	private static String ERROR_NO_RECORDS = "WMEXP_SO_ICAF_NOT_FOUND";
	
	//Default constructor
	public ItembyOwnerFacilityTransferValidation(){
	}
	
	//Overridden method
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Local variables
		StateInterface state = context.getState();
		QBEBioBean qbe = null;
		BioCollectionBean stratBio = null;
		BioCollectionBean allocStratBio = null;
		String[] parameter = new String[2];
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		
		//Find header form
		RuntimeFormInterface shellForm = null;
		//one level to list shell
		shellForm = context.getSourceWidget().getForm().getParentForm(state);
		while(!(shellForm.getName().equals(SHELL_NAME))){
			//iteratively finds list shell form
			shellForm = shellForm.getParentForm(state);
		}
		RuntimeFormInterface headerTabGroupShell = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT_1),null);
		RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTabGroupShell.getSubSlot(TAB_GROUP_SLOT), TAB_0);
		RuntimeFormInterface detailToggleForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT_2), null);
		RuntimeFormInterface detailTabGroupShell;
		if(normalHeaderForm.getFocus().isTempBio()){
			detailTabGroupShell = detailToggleForm;
		}else{
			detailTabGroupShell = state.getRuntimeForm(detailToggleForm.getSubSlot(TOGGLE_SLOT), DETAIL_TAB);
		}

		RuntimeFormInterface firstDetailForm = state.getRuntimeForm(detailTabGroupShell.getSubSlot(TAB_GROUP_SLOT), TAB_0);
				
		DataBean focus = firstDetailForm.getFocus();
		
		//Dynamically find the owner(STORERKEY)
		RuntimeFormWidgetInterface storerKey = normalHeaderForm.getFormWidgetByName(OWNER);
		String currentStorerValue = storerKey.getDisplayValue();
		//Dynamically find the item(SKU)
		RuntimeFormWidgetInterface itemWidget = firstDetailForm.getFormWidgetByName(ITEM);
		String newItemValue = itemWidget.getDisplayValue();
		
		//Query sku table for data points
		if(currentStorerValue==null){
			parameter[0] = colonStrip(readLabel(storerKey));
			normalHeaderForm.setError(ERROR_NULL_VALUE, parameter);
		}else{
			if(newItemValue!=null){
				newItemValue = newItemValue.toUpperCase();
				String itemQry = ITEM_TABLE+"."+ITEM+"='"+newItemValue+"' AND "+ITEM_TABLE+"."+OWNER+"='"+currentStorerValue+"'";
				Query itemQuery = new Query(ITEM_TABLE, itemQry, null);
				BioCollectionBean itemBio = uowb.getBioCollectionBean(itemQuery);
				if(itemBio.size()<1){
					//No records returned for primary key
					UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD);//AW 10/13/2008 Machine#:2093019 SDIS:SCM-00000-05440
					parameter[0]=colonStrip(readLabel(itemWidget));
					parameter[1]=colonStrip(readLabel(storerKey));
					firstDetailForm.setError(ERROR_INVALID_FOREIGN, parameter);
				}else{
					//Should return one record (query on primary key)
					//New record
					qbe=(QBEBioBean)focus;
					qbe.set(ITEM, newItemValue);
					qbe.set(OWNER, currentStorerValue);
					qbe.set(ITEM_DESCRIPTION, isNull(itemBio, ITEM_DESCRIPTION_SOURCE));
					qbe.set(PACK, isNull(itemBio, PACK));
					qbe.set(TARIFF, isNull(itemBio, TARIFF));
					qbe.set(ITEM_ROTATION, isNull(itemBio, ITEM_ROTATION_SOURCE));
					qbe.set(ROTATION, isNull(itemBio, ROTATION_SOURCE));
					qbe.set(SHELFLIFE, isNull(itemBio, SHELFLIFE));
					UOMDefaultValue.fillDropdown(state, UOM, isNull(itemBio, PACK));//AW 10/13/2008 Machine#:2093019 SDIS:SCM-00000-05440
					
					//Query strategy table for data points
					String stratQry = STRATEGY_TABLE+"."+STRATEGY+"='"+itemBio.get("0").get(STRATEGY).toString()+"'";
					Query strategyQuery = new Query(STRATEGY_TABLE, stratQry, null);
					stratBio = uowb.getBioCollectionBean(strategyQuery);

					RuntimeFormInterface secondDetailForm = state.getRuntimeForm(detailTabGroupShell.getSubSlot(TAB_GROUP_SLOT), TAB_1);
					if(stratBio.size()<1){
						parameter[0]=colonStrip(readLabel(secondDetailForm.getFormWidgetByName(ALLOC_STRAT)))+", "+colonStrip(readLabel(secondDetailForm.getFormWidgetByName(PREALLOC_STRAT)))+", "+colonStrip(readLabel(secondDetailForm.getFormWidgetByName(ALLOC_STRAT_TYPE)));
						firstDetailForm.setError(ERROR_NO_RECORDS, parameter);
					}else{
						qbe.set(ALLOC_STRAT, isNull(stratBio, ALLOC_STRAT));
						qbe.set(PREALLOC_STRAT, isNull(stratBio, PREALLOC_STRAT));
						//Query allocate strategy table for data points
						String allocStratQry = ALLOC_STRAT_TABLE+"."+ALLOC_STRAT+"='"+stratBio.get("0").get(ALLOC_STRAT).toString()+"'";
						Query allocateStrategyQuery = new Query(ALLOC_STRAT_TABLE, allocStratQry, null);
						allocStratBio = uowb.getBioCollectionBean(allocateStrategyQuery);
						if(allocStratBio.size()<1){
							parameter[0]=colonStrip(readLabel(secondDetailForm.getFormWidgetByName(ALLOC_STRAT_TYPE)));
							firstDetailForm.setError(ERROR_NO_RECORDS, parameter);
						}else{
							qbe.set(ALLOC_STRAT_TYPE, isNull(allocStratBio, ALLOC_STRAT_TYPE));
						}
					}
				}
			}else{
				qbe=(QBEBioBean)focus;
				qbe.set(ITEM, null);
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
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	public String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}