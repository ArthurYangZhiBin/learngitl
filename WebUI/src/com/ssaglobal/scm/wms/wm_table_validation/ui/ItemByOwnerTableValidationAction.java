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

package com.ssaglobal.scm.wms.wm_table_validation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class ItemByOwnerTableValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		StateInterface state = context.getState();

		boolean setPack = getParameter("SET_PACK") == null ? false : getParameterBoolean("SET_PACK");
		String errorMessage = getParameter("ERRORMESSAGE") == null ? "WMEXP_INVALID_ITEM_BY_OWNER"
				: getParameterString("ERRORMESSAGE");
		String ownerWidgetName = getParameter("OWNERWIDGET") == null ? "STORERKEY" : getParameterString("OWNERWIDGET");
		String packWidgetName = getParameter("PACKWIDGET") == null ? "PACKKEY" : getParameterString("PACKWIDGET");

		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();

		String itemValue = context.getSourceWidget().getDisplayValue() == null ? null
				: context.getSourceWidget().getDisplayValue().toUpperCase();
		if(isEmpty(itemValue)) {
			return RET_CANCEL;
		}
		
		String ownerValue = focus.getValue(ownerWidgetName) == null ? ""
				: focus.getValue(ownerWidgetName).toString().toUpperCase();
				
		BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("sku","sku.STORERKEY='"+ownerValue+"' and sku.SKU='"+itemValue+"'", null));
		if(results.size() == 0) {
			//throw exception
			//{0} {1} does not belong to {2} {3}
			String[] params = new String[4];
			params[0] = context.getSourceWidget().getLabel("label", state.getLocale());
			params[1] = itemValue;
			params[2] = form.getFormWidgetByName(ownerWidgetName).getLabel("label", state.getLocale());
			params[3] = ownerValue;
			throw new UserException(errorMessage, params);
		}
		
		if(setPack){
			String pack = results.elementAt(0).get("PACKKEY").toString();
			form.getFormWidgetByName(packWidgetName).setDisplayValue(pack);
			focus.setValue(packWidgetName, pack);  //SRG 08/03/09 Bugaware# 9310
		}
		return RET_CONTINUE;
	}

	boolean isEmpty(Object attributeValue) {
		if (attributeValue == null) {
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else if (attributeValue.toString().matches("null")) {
			return true;
		} else {
			return false;
		}
	}
}