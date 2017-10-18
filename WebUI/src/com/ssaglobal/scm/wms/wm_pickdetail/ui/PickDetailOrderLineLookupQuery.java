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
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class PickDetailOrderLineLookupQuery extends ActionExtensionBase {
	private static String ORDER = "ORDERKEY";
	private static String OD_TABLE = "wm_orderdetail"; 
	private static String ERROR_MSG = "WMEXP_PD_NOORDER";
	private static String ERROR_MSG_EMPTYSET = "WMEXP_PD_EMPTYSET";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] params = new String[1];
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormWidgetInterface orderKey = state.getCurrentRuntimeForm().getFormWidgetByName(ORDER);
		Object orderKeyValue = orderKey.getValue();
		if(isNull(orderKeyValue)){
			params[0] = colonStrip(readLabel(orderKey));
			throw new FieldException(state.getCurrentRuntimeForm(), context.getSourceWidget().getName(), ERROR_MSG, params);
		}
		String queryString = OD_TABLE+"."+ORDER+"='"+orderKeyValue.toString()+"' and "+OD_TABLE+".STATUS!='95'";
		Query query = new Query(OD_TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(query);
		if(list.size()<1){
			params[0] = orderKey.getName()+" "+orderKeyValue.toString();
			throw new FormException(ERROR_MSG_EMPTYSET, params);
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