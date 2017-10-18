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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.exceptions.ScreenException;
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
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InternalTransferSKULookup extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferSKULookup.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing InternalTransferSKULookup " ,100L);
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		String[] parameters = new String[1];
		String table = "wm_lotxlocxid";
		String queryString = "";
		
		String toOrFrom = getParameter("toOrFrom").toString();
		
		final String storerKey = toOrFrom +"STORERKEY";
		final String sku= toOrFrom +"SKU";
		final String lot= toOrFrom +"LOT";
		final String loc= toOrFrom +"LOC";
		final String id= toOrFrom +"ID";
		
		QBEBioBean qbe = null;
		DataBean bio= null;
		
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface detailForm = currentForm.getParentForm(state);
		RuntimeFormInterface shellForm= detailForm.getParentForm(state);
		while(!shellForm.getName().equals("wms_list_shell")){
			shellForm = shellForm.getParentForm(state);
		}

		//new
		DataBean detailFocus= currentForm.getFocus();
		if(detailFocus.isTempBio())
			detailFocus = (QBEBioBean)detailFocus;
		else
			detailFocus= (BioBean)detailFocus;
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		
		RuntimeFormWidgetInterface storerVal= headerForm.getFormWidgetByName(storerKey);
		
		if(storerVal.getValue()== null)
		{
			String[] param = new String[1];
			param[0] = storerKey;
			throw new UserException("WMEXP_BLANK_STORER", param);
		}
		
		RuntimeFormWidgetInterface lotWidget = currentForm.getFormWidgetByName(lot);
		RuntimeFormWidgetInterface locWidget = currentForm.getFormWidgetByName(loc);
		RuntimeFormWidgetInterface idWidget = currentForm.getFormWidgetByName(id);
		RuntimeFormWidgetInterface skuWidget = currentForm.getFormWidgetByName(sku);
		String sourceWidget = context.getSourceWidget().getName();

	
		
		boolean lotIsNull = isNull(lotWidget);
		boolean locationIsNull = isNull(locWidget);
		boolean idIsNull = isNull(idWidget);
		boolean itemIsNull = isNull(skuWidget);
		boolean test = checkNull(detailFocus.getValue(sku));
		
		queryString = table+".STORERKEY='"+storerVal.getDisplayValue()+"'";		
		
		if(!lotIsNull || !locationIsNull || !idIsNull || !itemIsNull){
			if(!itemIsNull && !sourceWidget.equals("ItemLookup"))
				queryString = queryString+" and "+table+".SKU='"+skuWidget.getDisplayValue()+"'";
		}
		
		Query qry = new Query(table, queryString, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(qry); 
		if(newFocus.size()<1){
			String[] param = new String[1];
			param[0] = storerVal.getDisplayValue();
			throw new FormException("WMEXP_NO_VALUES_FOR_OWNER", param);
		}
		
		DataBean focus = currentForm.getFocus();
		if(focus.isTempBio()){
			qbe=(QBEBioBean)focus;		
			//qbe.set(loc, checkNull(newFocus, "LOC"));
			//qbe.set(lot, checkNull(newFocus, "LOT"));
		}
		else
		{
			focus= (BioBean)focus;
		}
		

		result.setFocus(newFocus);
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting InternalTransferSKULookup " ,100L);
		return RET_CONTINUE;

	}

	private Object checkNull(BioCollectionBean focus, String widgetName)throws EpiException {
		// TODO Auto-generated method stub
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}

	private boolean isNull(RuntimeFormWidgetInterface widget) {
		// TODO Auto-generated method stub  
		boolean value;
		if(widget.getValue()== null)			
			value=true;			
		else if(widget.getValue().toString().matches("\\s*"))
			value=true;
		else
			value=false;
		
		return value;
	}
	private boolean checkNull(Object value) {
		// TODO Auto-generated method stub  
		//_log.debug("LOG_SYSTEM_OUT","\n\n*** In CHECKNULL",100L);
		boolean val;
		if(value== null)			
			val=true;
		else if(value.toString().matches("\\s*"))
			val=true;
		else
			val=false;
		
		return val;
	}
	
}
