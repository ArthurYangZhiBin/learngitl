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

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
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
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class PopulatePackFromItemOnChange extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulatePackFromItemOnChange.class);
	//Form name constants
	private static String SHELL = "wms_list_shell";
	private static String SLOT1 = "list_slot_1";
	
	//Table name constants
	private static String TABLE = "sku";
	
	//Widget name constants
	private static String ITEM = "SKU";
	private static String OWNER = "STORERKEY";
	private static String PACK = "PACKKEY";
	private static String UOM = "UOM";
	
	//Parameter name constants
	private static String TO_FROM = "toOrFrom";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PopulatePackFromItemOnChange",100L);
		String toOrFrom = getParameter(TO_FROM).toString(); 
		
		String storerKey = toOrFrom+OWNER;
		String packKey= toOrFrom+PACK;
		String tfUOM = toOrFrom + UOM;
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface subForm = state.getCurrentRuntimeForm();		
		RuntimeFormInterface detailForm = subForm.getParentForm(state);
		RuntimeFormInterface shellForm = null;
		shellForm = detailForm.getParentForm(state);
		
		if(!shellForm.getName().equals(SHELL)) {
			shellForm = detailForm.getParentForm(state).getParentForm(state);
		}
			
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot(SLOT1);
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,null);		
		DataBean headerFocus = headerForm.getFocus();
				
		String owner = (String)headerFocus.getValue(storerKey);
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Storer: " +owner,100L);
		
		RuntimeFormWidgetInterface item = context.getSourceWidget();
		String itemValue = item.getDisplayValue()!=null ? item.getDisplayValue().toString().toUpperCase() : null;
		
		//validate item
		String itemQry = TABLE+"."+OWNER+"='"+owner+"'AND "+TABLE+"."+ITEM+"='"+itemValue+"'";
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Query: "+itemQry,100L);
		Query itemQuery = new Query(TABLE, itemQry, null);		
		BioCollectionBean itemBio = uow.getBioCollectionBean(itemQuery);
		if ((itemBio == null)||(itemBio.size()<1)){
			//Query returned empty, throw invalid item error message
			UOMDefaultValue.fillDropdown(state, tfUOM, UOMMappingUtil.PACK_STD);//AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
			String[] param= new String[2];
			param[0]= item.getDisplayValue();
			param[1]= owner;
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Throwing Exception- empty biocollection",100L);
			throw new UserException("WMEXP_VALIDATESKU", param);
		}else{
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Query returned records",100L);
			DataBean formFocus = state.getFocus();
			if(formFocus.isTempBio()){
				QBEBioBean qbe = (QBEBioBean) formFocus;
				qbe.set(packKey, isNull(itemBio, PACK));
				qbe.set(storerKey, isNull(itemBio, OWNER));
				qbe.set(item.getName(), itemValue);
			} else {
				BioBean bio = (BioBean) formFocus;
				bio.set(packKey, isNull(itemBio, PACK));
				bio.set(storerKey, isNull(itemBio, OWNER));
				bio.set(item.getName(), itemValue);
			}
			UOMDefaultValue.fillDropdown(state, tfUOM, isNull(itemBio, PACK));//AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PopulatePackFromItemOnChange",100L);
		return RET_CONTINUE;
	}

	private String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}
}