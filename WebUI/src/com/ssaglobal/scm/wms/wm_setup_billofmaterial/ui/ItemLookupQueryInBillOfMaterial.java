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
package com.ssaglobal.scm.wms.wm_setup_billofmaterial.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_load_schedule.ui.LoadSchedulePreSaveValidation;

public class ItemLookupQueryInBillOfMaterial extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemLookupQueryInBillOfMaterial.class);
	//Specific query for an item lookup that only returns the selected owner 
	protected int execute(ActionContext context, ActionResult result)throws EpiException{
		
		_log.debug("LOG_DEBUG_EXTENSION_ITEM_LOOKUP_QUERY","Executing ItemLookupQueryInBillOfMaterial",100L);	
		String qry = "sku.STORERKEY = '";
		String[] param= new String[1]; 
		StateInterface state = context.getState();
		RuntimeFormInterface currForm= state.getCurrentRuntimeForm();
		
		DataBean focus= currForm.getFocus();
		if(focus instanceof QBEBioBean)
		{focus= (QBEBioBean)focus;}
		else
		{focus= (BioBean)focus;}
		
		
		if(isNull(focus.getValue("STORERKEY")))
		{
			param[0]=colonStrip(readLabel(currForm.getFormWidgetByName("STORERKEY")));
			qry = null; //AW 07/21/09 Machine:2073672 SDIS:05256
		}
		else
		{
			qry = qry + focus.getValue("STORERKEY").toString() + "'"; //AW 07/21/09 Machine:2073672 SDIS:05256
		}
		
		
		
		//_log.debug("LOG_SYSTEM_OUT","Query: " +qry,100L);
		Query loadBiosQry = new Query("sku", qry, null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		if(newFocus.size()<1){
			param[0]= focus.getValue("STORERKEY").toString();
			throw new FormException("WMEXP_NO_MATCHING_RECORDS", param);
		}
		result.setFocus(newFocus);
		_log.debug("LOG_DEBUG_EXTENSION_ITEM_LOOKUP_QUERY","Exiting ItemLookupQueryInBillOfMaterial",100L);
		return RET_CONTINUE;
	}

	private boolean isNull(Object value) {
		// TODO Auto-generated method stub
		if(value == null)
			{return true;}
		else if(value.toString().matches("\\s*"))
			{return true;}
		else
			{return false;}
	
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
