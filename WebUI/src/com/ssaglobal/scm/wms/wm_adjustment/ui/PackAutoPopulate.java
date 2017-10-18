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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class PackAutoPopulate extends ActionExtensionBase{
	public PackAutoPopulate(){
	}
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Find current item and owner values
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface headerForm = FormUtil.findForm(currentForm, "wms_list_shell", "wm_adjustment_detail_view", state);
		RuntimeFormWidgetInterface owner = headerForm.getFormWidgetByName("STORERKEY");
		String newItemValue = context.getSourceWidget().getDisplayValue();
		if(newItemValue!=null){
			newItemValue = newItemValue.toUpperCase();
		}

		//Verify Item/Owner combination
		BioCollectionBean list = findItemOwnerCombo(state, newItemValue, owner.getDisplayValue(), "SKU");
		if(list.size()<1){
			UOMDefaultValue.fillDropdown(state, "UOM", UOMMappingUtil.PACK_STD);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			String foreign[] = new String[2];
			foreign[0]=colonStrip(readLabel(context.getSourceWidget()));
			foreign[1]=colonStrip(readLabel(owner));
			throw new FormException("WMEXP_SO_ILQ_FOREIGN_INVALID", foreign);
		}
		
		//Fill Pack and UOM based on correct Item/Owner combination 
		populatePack(state, list);
		
		
		//Check for manual reset of values lot, location, and LPN
		String loc = convertNull(currentForm.getFormWidgetByName("LOC").getDisplayValue());
		String lot = convertNull(currentForm.getFormWidgetByName("LOT").getDisplayValue());
		String id = convertNull(currentForm.getFormWidgetByName("ID").getDisplayValue());
		boolean erase = true;
		String query = "wm_lotxlocxid.LOT='"+lot+"' and wm_lotxlocxid.LOC='"+loc+"' and wm_lotxlocxid.ID='"+id+"'";
		Query eraseQry = new Query("wm_lotxlocxid", query, null);
		BioCollectionBean eraseList = uowb.getBioCollectionBean(eraseQry);
		if(eraseList.size()==1){
			if(eraseList.get("0").get("SKU").toString().equals(context.getSourceWidget().getDisplayValue())){
				erase=false;	
			}
		}
		if(erase){
			QBEBioBean qbe = (QBEBioBean)state.getFocus();
			qbe.set("LOT", null);
			qbe.set("LOC", null);
			qbe.set("ID", null);
		}

		return RET_CONTINUE;
	}

	public String convertNull(String value){
		if(value==null){
			value="";
		}
		return value;
	}
	
	public static String isNull(BioCollectionBean list, String widgetName) throws EpiException{
		String result=null;
		if(result!=list.get("0").get(widgetName)){
			result=list.get("0").get(widgetName).toString();
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
	
	public static BioCollectionBean findItemOwnerCombo(StateInterface state, String newItemValue, String owner, String itemName){
		DataBean focus = state.getFocus();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)state.getFocus();
			qbe.set(itemName, newItemValue);
		}
		
		//Build query
		String query = "sku.SKU='"+newItemValue+"' and sku.STORERKEY='"+owner+"'";
		Query qry = new Query("sku", query, null);
		return uowb.getBioCollectionBean(qry);
	}
	
	public static void populatePack(StateInterface state, BioCollectionBean list) throws EpiException{
		populatePack(state, list, "PACKKEY", "STORERKEY", "UOM");
	}
	
	public static void populatePack(StateInterface state, BioCollectionBean list, String packWidget, String ownerWidget, String uomWidget) throws EpiException{
		DataBean focus = state.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean) focus;
			qbe.set(packWidget, isNull(list, "PACKKEY"));
			qbe.set(ownerWidget, isNull(list, "STORERKEY"));
		} else {
			BioBean bio = (BioBean) focus;
			bio.set(packWidget, isNull(list, "PACKKEY"));
			bio.set(ownerWidget, isNull(list, "STORERKEY"));
		}
		state.getCurrentRuntimeForm().getFormWidgetByName(packWidget).setDisplayValue(isNull(list, "PACKKEY"));
		UOMDefaultValue.fillDropdown(state, uomWidget, isNull(list, "PACKKEY"));//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
	}
}

