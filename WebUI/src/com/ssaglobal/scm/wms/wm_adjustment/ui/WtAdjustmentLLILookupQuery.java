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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;

public class WtAdjustmentLLILookupQuery extends ActionExtensionBase{
	// Added for WM 9 3PL Enhancements - Catch weights - Weight Adjustment - Krishna Kuchipudi
	//Perform query for Lookup records on the Adjustment Detail screen
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String[] parameters = new String[1];
		String table = "wm_cust_wt_lotxlocxid";
		String queryString = null;
		
		//Find forms and values for building query string
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = currentForm.getParentForm(state);
		while(!shellForm.getName().equals("wms_list_shell")){
			shellForm = shellForm.getParentForm(state);
		}
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		RuntimeFormWidgetInterface owner = headerForm.getFormWidgetByName("STORERKEY");
		if(owner.getDisplayValue()==null){
			parameters[0]=colonStrip(readLabel(owner));
			throw new FormException("WMEXP_SO_ILQ_Null", parameters);
		}
		RuntimeFormWidgetInterface lot = currentForm.getFormWidgetByName("LOT");
		RuntimeFormWidgetInterface location = currentForm.getFormWidgetByName("LOC");
		RuntimeFormWidgetInterface id = currentForm.getFormWidgetByName("ID");
		RuntimeFormWidgetInterface item = currentForm.getFormWidgetByName("SKU");
		String source = context.getSourceWidget().getName();
		
		//Initialize string building flags
		boolean lotIsNull = isNull(lot);
		boolean locationIsNull = isNull(location);
		boolean idIsNull = isNull(id);
		boolean itemIsNull = isNull(item);
		
		//Build query string
		queryString = table+".STORERKEY='"+owner.getDisplayValue()+"'"+" and "+table+".QTY>0";
		if(!lotIsNull || !locationIsNull || !idIsNull || !itemIsNull){
			if(!lotIsNull && !source.equals("LOT"))
				queryString = queryString+" and "+table+".LOT='"+lot.getDisplayValue()+"'";
			if(!locationIsNull && !source.equals("LOC"))
				queryString = queryString+" and "+table+".LOC='"+location.getDisplayValue()+"'";
			if(!idIsNull && !source.equals("ID"))
				queryString = queryString+" and "+table+".ID='"+id.getDisplayValue()+"'";
			if(!itemIsNull && !source.equals("SKU"))
				queryString = queryString+" and "+table+".SKU='"+item.getDisplayValue()+"'";
		}
		
		//Perform query
		Query qry = new Query(table, queryString, null);
		BioCollectionBean list = uow.getBioCollectionBean(qry); 
		
		//Throw exception for empty query results
		if(list.size()<1){
			String temp = null;
			temp = readLabel(owner)+" "+owner.getDisplayValue();
			if(!lotIsNull || !locationIsNull || !idIsNull || !itemIsNull){
				if(!lotIsNull)
					temp = temp+", "+readLabel(lot)+" "+lot.getDisplayValue();
				if(!locationIsNull)
					temp = temp+", "+readLabel(location)+" "+location.getDisplayValue();
				if(!idIsNull)
					temp = temp+", "+readLabel(id)+" "+id.getDisplayValue();
				if(!itemIsNull)
					temp = temp+", "+readLabel(item)+" "+item.getDisplayValue();
			}
			parameters[0]=temp;
			throw new FormException("WMEXP_ADJ_NO_RECORDS", parameters);
		}
		
		//Set results as focus
		result.setFocus(list);
		return RET_CONTINUE;
	}
	
	boolean isNull(RuntimeFormWidgetInterface widget){
		boolean value;
		if(widget.getDisplayValue()==null)
			value=true;
		else
			value=false;
		return value;
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