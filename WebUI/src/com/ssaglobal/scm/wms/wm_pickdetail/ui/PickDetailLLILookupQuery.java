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
package com.ssaglobal.scm.wms.wm_pickdetail.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class PickDetailLLILookupQuery extends ActionExtensionBase{
	private static String TABLE = "wm_lotxlocxid";
	private static String OWNER = "STORERKEY";
	private static String ITEM = "SKU";
	private static String ORDER = "ORDERKEY";
	private static String OLN = "ORDERLINENUMBER";
	private static String LOCATION = "LOC";
	private static String LOT = "LOT";
	private static String LPN = "ID";
	private static String ERROR_MISSING_VALUES = "WMEXP_PD_NOORDER";
	private static String ERROR_EMPTYSET = "WMEXP_ADJ_NO_RECORDS";
		
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String source = context.getSourceWidget().getName();
		String[] params = new String[1];
		
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		RuntimeFormWidgetInterface owner = form.getFormWidgetByName(OWNER);
		RuntimeFormWidgetInterface item = form.getFormWidgetByName(ITEM);
		if(isNull(owner.getValue()) || isNull(item.getValue())){
			params[0] = colonStrip(readLabel(form.getFormWidgetByName(ORDER)))+", "+colonStrip(readLabel(form.getFormWidgetByName(OLN)));
			throw new FieldException(form, source, ERROR_MISSING_VALUES, params);
		}
		String ownerValue = owner.getValue().toString(), itemValue = item.getValue().toString();
		RuntimeFormWidgetInterface location = form.getFormWidgetByName(LOCATION);
		RuntimeFormWidgetInterface lot = form.getFormWidgetByName(LOT);
		RuntimeFormWidgetInterface lpn = form.getFormWidgetByName(LPN);
		
		//Build Query String
		String queryString = TABLE+"."+OWNER+"='"+ownerValue+"' and "+TABLE+"."+ITEM+"='"+itemValue+"'";
		params[0] = readLabel(owner)+" "+ownerValue+", "+readLabel(item)+" "+itemValue;
		if(!isNull(location.getValue()) && !source.equals(LOCATION)){
			queryString = queryString+" and "+TABLE+"."+LOCATION+"='"+location.getValue().toString()+"'";
			params[0] = params[0]+", "+readLabel(location)+" "+location.getValue().toString();
		}
		if(!isNull(lot.getValue()) && !source.equals(LOT)){
			queryString = queryString+" and "+TABLE+"."+LOT+"='"+lot.getValue().toString()+"'";
			params[0] = params[0]+", "+readLabel(lot)+" "+lot.getValue().toString();
		}
		if(!isNull(lpn.getValue()) && !source.equals(LPN)){
			queryString = queryString+" and "+TABLE+"."+LPN+"='"+lpn.getValue().toString()+"'";
			params[0] = params[0]+", "+readLabel(lpn)+" "+lpn.getValue().toString();
		}
		Query query = new Query(TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(query);
		//Throw exception for empty query results
		if(list.size()<1){
			throw new FieldException(form, source, ERROR_EMPTYSET, params);
		}
		result.setFocus(list);
		return RET_CONTINUE;
	}
	
	private boolean isNull(Object attributeValue) throws EpiDataException {
		if (attributeValue == null){
			return true;
		}else if (attributeValue.toString().matches("\\s*")){
			return true;
		}else{
			return false;
		}
	}
	
	private String colonStrip(String label){
		return 	label.substring(0, label.length()-1);
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}