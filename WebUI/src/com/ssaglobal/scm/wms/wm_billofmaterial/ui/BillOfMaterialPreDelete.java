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
package com.ssaglobal.scm.wms.wm_billofmaterial.ui;



import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui.SetFocusForCharges;


public class BillOfMaterialPreDelete extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BillOfMaterialPreDelete.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
	
	 _log.debug("LOG_DEBUG_EXTENSION_BOM","Executing BillOfMaterialPreDelete",100L);
	StateInterface state = context.getState();
	
	RuntimeListFormInterface listForm = null;
	RuntimeFormInterface form = context.getSourceWidget().getForm();

	RuntimeFormInterface shellForm = form.getParentForm(state);
	RuntimeFormInterface headerForm;
	if(form.getName().equals("wm_list_shell_billofmaterial Toolbar")){
		headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
	}else{
		headerForm = state.getRuntimeForm(shellForm.getSubSlot("wm_billofmaterialdetail_toggle_slot"),"List");
	}
	if(headerForm.isListForm()){
		listForm = (RuntimeListFormInterface)headerForm;
	}else{
		throw new FormException("WMEXP_NOT_LIST", null);
	}
	
	
	ArrayList itemsSelected = listForm.getAllSelectedItems();
	DataBean bean = (DataBean)(itemsSelected.get(0));
	String bomID = bean.getValue("BOMHDRDEFNID").toString();
	
	try{
	 	String sql ="select * from WORKORDERDEFN where bomhdrdefnid='"+bomID+"'";
		EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
		if(dataObject.getCount()> 0){
			String [] param = new String[1];
			param[0] = bomID;
			throw new UserException("WMEXP_CANNOT_DELETE", param);
		}
		} catch (DPException e) {
			e.printStackTrace();
		}
	 
	
	
	
	_log.debug("LOG_DEBUG_EXTENSION_BOM","Exiting BillOfMaterialPreDelete",100L);
	return RET_CONTINUE;
	}
}