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
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class PickDetailPostOrderSelectionFill extends ActionExtensionBase{
	private static String ORDER = "ORDERKEY";
	private static String OLN = "ORDERLINENUMBER";
	private static String STATUS = "STATUS";
	private static String OWNER = "STORERKEY";
	private static String ITEM = "SKU";
	private static String PACK = "PACKKEY";
	private static String CARTON = "CARTONGROUP";
	private static String TABLE = "wm_orders";
	private static String OD_TABLE = "wm_orderdetail";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] params = new String[2];
		StateInterface state = context.getState();
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormWidgetInterface orderKey = state.getCurrentRuntimeForm().getFormWidgetByName(ORDER); 
		RuntimeFormWidgetInterface orderLineNumber = state.getCurrentRuntimeForm().getFormWidgetByName(OLN);
		String orderKeyValue;
		//Validate orderkey against orders table and validate for valid status (NOT Shipped Complete)
		if(source.getName().equals(ORDER)){
			if(!isNull(orderKey.getValue())){
				orderKeyValue = orderKey.getValue().toString().toUpperCase();
				String queryString = TABLE+"."+ORDER+"='"+orderKeyValue+"'";
				Query query = new Query(TABLE, queryString, null);
				BioCollectionBean list = uowb.getBioCollectionBean(query);
				if(list.size()!=1){
					params[0] = colonStrip(readLabel(orderKey));
					throw new FieldException(state.getCurrentRuntimeForm(), ORDER, "WMEXP_SO_ILQ_Invalid", params);
				}
				if(list.get("0").getValue(STATUS).toString().equals("95")){
					params[0] = orderKeyValue;
					params[1] = colonStrip(readLabel(orderKey));
					throw new FieldException(state.getCurrentRuntimeForm(), ORDER, "WMEXP_PD_SHIP_COMPLETED", params);
				}
			}
		}
		if(!isNull(orderLineNumber.getValue()) && !isNull(orderKey.getValue())){
			orderKeyValue = orderKey.getValue().toString().toUpperCase();
			String orderLineNumberValue = orderLineNumber.getValue().toString().toUpperCase();
			String queryString = OD_TABLE+"."+ORDER+"='"+orderKeyValue+"' AND "+OD_TABLE+"."+OLN+"='"+orderLineNumberValue+"'";
			Query query = new Query(OD_TABLE, queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(query);
			if(list.size()==1){
				if(list.get("0").getValue(STATUS).toString().equals("95")){
					params[0] = orderLineNumberValue;
					params[1] = colonStrip(readLabel(orderLineNumber));
					throw new FieldException(state.getCurrentRuntimeForm(), OLN, "WMEXP_PD_SHIP_COMPLETED", params);
				}
				QBEBioBean qbe = (QBEBioBean)state.getFocus();
				qbe.set(ORDER, orderKeyValue);
				qbe.set(OLN, orderLineNumberValue);
				qbe.set(OWNER, list.get("0").getValue(OWNER));
				qbe.set(ITEM, list.get("0").getValue(ITEM));
				qbe.set(PACK, list.get("0").getValue(PACK));
				qbe.set(CARTON, list.get("0").getValue(CARTON));
			}else{		
				params[0] = colonStrip(readLabel(orderLineNumber));
				params[1] = colonStrip(readLabel(orderKey));
				throw new FieldException(state.getCurrentRuntimeForm(), OLN, "WMEXP_SO_ILQ_FOREIGN_INVALID", params);
			}
		}
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