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
package com.ssaglobal.scm.wms.wm_server_configuration.ui;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerConfigurationPreSave extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] parameter = new String[1];
		String table="wm_server_configuration", widget="KEYNAME";
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface form = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_2"), null);
		parameter[0] = colonStrip(readLabel(form, widget));
		if(form.getFocus().isTempBio()){
			String keyValue = form.getFormWidgetByName(widget).getDisplayValue() == null ? null : form.getFormWidgetByName(widget).getDisplayValue().toUpperCase() ;
			String queryString = table+"."+widget+"= '"+keyValue+"'";
			Query qry = new Query(table, queryString, null);
			BioCollectionBean stratList = uow.getBioCollectionBean(qry);
			if(stratList!=null){
				if(stratList.size()!=0){
					throw new FormException("WMEXP_CCPS_EXISTS", parameter);
				}	
			}
		}
		String number = form.getFormWidgetByName("KEYCOUNT").getDisplayValue();
		//Amar:  Code Added to fix the SDIS issue issue SCM-00000-04828 Machine 2002874 
		number = commaStrip(number);
		//Amar:  Code End to fix the SDIS issue issue SCM-00000-04828 Machine 2002874 
		if(number.matches(".*\\..*")){
			parameter[0] = number;
			throw new FormException("WMEXP_NON_INTEGER", parameter);
		}else{
			int value = Integer.parseInt(number);
			if(value<0){
				throw new FormException("WMEXP_LESS_THAN_ZERO", parameter);
			}	
		}
		return RET_CONTINUE;
	}
	
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	public String readLabel(RuntimeFormInterface form, String widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label",locale);
	}
	
	//	Amar:  Code Added to fix the SDIS issue issue SCM-00000-04828 Machine 2002874 
	public String commaStrip(String label){
		Pattern pattern = Pattern.compile("\\,");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	//	Amar:  Code End to fix the SDIS issue issue SCM-00000-04828 Machine 2002874 

}