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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.ReturnPartyQueryAction;

public class AutoFillAddress extends ActionExtensionBase{
	public AutoFillAddress(){
	}
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		return updateStorerInformation(state, focus, sourceWidget);
	}

	public int updateStorerInformation(StateInterface state, DataBean focus,
			RuntimeFormWidgetInterface sourceWidget) throws FormException,
			EpiDataException {
		QBEBioBean qbe = null;
		BioBean bio = null;
		
		String activeWidget = sourceWidget.getName();
		boolean case1 = activeWidget.equals("CONSIGNEEKEY");
		boolean case2 = activeWidget.equals("BILLTOKEY");
		boolean case3 = activeWidget.equals("CarrierCode");
		boolean unsaved = focus.isTempBio();
		String sourceValue = sourceWidget.getDisplayValue();
		sourceValue = sourceValue == null ? null : sourceValue.toUpperCase().trim();
		if(unsaved){
			qbe = (QBEBioBean)focus;
		}else{
			bio = (BioBean)focus;
		}
		//Validate input		
		String[] parameters = new String[1];
		parameters[0] = colonStrip(readLabel(sourceWidget.getForm(), activeWidget));
		String qry = "wm_storer.TYPE='";			
		if(case1){
			qry = qry + ReturnPartyQueryAction.getStorerType(focus);
		}else if(case2){
			qry = qry + "4";
		}else if(case3){
			qry = qry + "3";
		}
		qry = qry + "' AND wm_storer.STORERKEY='"+sourceValue;
		//Throw exception when no value is entered
		if(qry.endsWith("null") && !case3){
			throw new FormException("WMEXP_SO_ILQ_Null", parameters);						
		}
		qry += "'";
		Query loadBiosQry = new Query("wm_storer", qry, null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		//Throw exception for unrecognized value
		if(!(newFocus.size()==1)){
			throw new FormException("WMEXP_SO_ILQ_Invalid", parameters);
		}
		//Set strings for proper storage of values
		String prefix = null, postfix = null;
		if(case1){
			prefix="C_";
			postfix="1";
		}else if(case2){
			prefix="B_";
			postfix="2";
		}
		try{
		//AutoFill values from storer table
		if(case1 || case2){
			if(unsaved){
				qbe.set(prefix+"COMPANY", isNull(newFocus, "COMPANY"));
				qbe.set(prefix+"ADDRESS1", isNull(newFocus, "ADDRESS1"));
				qbe.set(prefix+"ADDRESS2", isNull(newFocus, "ADDRESS2"));
				qbe.set(prefix+"ADDRESS3", isNull(newFocus, "ADDRESS3"));
				qbe.set(prefix+"ADDRESS4", isNull(newFocus, "ADDRESS4"));
				qbe.set(prefix+"ADDRESS5", isNull(newFocus, "ADDRESS5"));
				qbe.set(prefix+"ADDRESS6", isNull(newFocus, "ADDRESS6"));
				qbe.set(prefix+"CITY", isNull(newFocus, "CITY"));
				qbe.set(prefix+"STATE", isNull(newFocus, "STATE"));
				qbe.set(prefix+"ZIP", isNull(newFocus, "ZIP"));			
				qbe.set(prefix+"ISOCNTRYCODE", isNull(newFocus, "ISOCNTRYCODE"));
				qbe.set(prefix+"VAT", isNull(newFocus, "VAT"));
				qbe.set("C_EMAIL2", isNull(newFocus, ("EMAIL2")));
				qbe.set(prefix+"CONTACT1", isNull(newFocus, ("CONTACT"+postfix)));
				if(case1){
					qbe.set("C_EMAIL1", isNull(newFocus, ("EMAIL1")));
					qbe.set("CONSIGNEEKEY", sourceValue);
				}else{
					qbe.set("BILLTOKEY", sourceValue);
				}
			}else{
				bio.set(prefix+"COMPANY", isNull(newFocus, "COMPANY"));
				bio.set(prefix+"ADDRESS1", isNull(newFocus, "ADDRESS1"));
				bio.set(prefix+"ADDRESS2", isNull(newFocus, "ADDRESS2"));
				bio.set(prefix+"ADDRESS3", isNull(newFocus, "ADDRESS3"));
				bio.set(prefix+"ADDRESS4", isNull(newFocus, "ADDRESS4"));
				bio.set(prefix+"ADDRESS5", isNull(newFocus, "ADDRESS5"));
				bio.set(prefix+"ADDRESS6", isNull(newFocus, "ADDRESS6"));
				bio.set(prefix+"CITY", isNull(newFocus, "CITY"));
				bio.set(prefix+"STATE", isNull(newFocus, "STATE"));
				bio.set(prefix+"ZIP", isNull(newFocus, "ZIP"));			
				bio.set(prefix+"ISOCNTRYCODE", isNull(newFocus, "ISOCNTRYCODE"));
				bio.set(prefix+"VAT", isNull(newFocus, "VAT"));
				bio.set("C_EMAIL2", isNull(newFocus, ("EMAIL2")));
				bio.set(prefix+"CONTACT1", isNull(newFocus, ("CONTACT"+postfix)));
				if(case1){
					bio.set("C_EMAIL1", isNull(newFocus, "EMAIL1"));
					bio.set("CONSIGNEEKEY", sourceValue);
				}else{
					bio.set("BILLTOKEY", sourceValue);
				}
			}
		}else if(case3){
			if(unsaved){
				qbe.set("CarrierAddress1", isNull(newFocus, "ADDRESS1"));
				qbe.set("CarrierAddress2", isNull(newFocus, "ADDRESS2"));
				qbe.set("CarrierCity", isNull(newFocus, "CITY"));
				qbe.set("CarrierState", isNull(newFocus, "STATE"));
				qbe.set("CarrierZip", isNull(newFocus, "ZIP"));
				qbe.set("CarrierCountry", isNull(newFocus, "COUNTRY"));
				qbe.set("CarrierPhone", isNull(newFocus, "PHONE1"));
				qbe.set("CarrierName", isNull(newFocus, "COMPANY"));
				qbe.set("CarrierCode", sourceValue);
			}else{
				bio.set("CarrierAddress1", isNull(newFocus, "ADDRESS1"));
				bio.set("CarrierAddress2", isNull(newFocus, "ADDRESS2"));
				bio.set("CarrierCity", isNull(newFocus, "CITY"));
				bio.set("CarrierState", isNull(newFocus, "STATE"));
				bio.set("CarrierZip", isNull(newFocus, "ZIP"));
				bio.set("CarrierCountry", isNull(newFocus, "COUNTRY"));
				bio.set("CarrierPhone", isNull(newFocus, "PHONE1"));
				bio.set("CarrierName", isNull(newFocus, "COMPANY"));
				bio.set("CarrierCode", sourceValue);
			}
		}}catch(Exception e){
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
	
	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
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
}