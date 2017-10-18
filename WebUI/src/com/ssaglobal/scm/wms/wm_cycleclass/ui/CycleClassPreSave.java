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
package com.ssaglobal.scm.wms.wm_cycleclass.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.ssaglobal.scm.wms.util.*;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

public class CycleClassPreSave extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] parameter = new String[1];
		String pattern = "[A-Z]", widgetName = "CLASSID", tableName = "wm_ccsetup";
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface detail = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);
		if(detail.getName().equals("Blank")){
			throw new FormException("WMEXP_NO_SAVE_AVAILABLE", null);
		}
		RuntimeFormWidgetInterface widget = detail.getFormWidgetByName(widgetName);
		String value = widget.getDisplayValue();
		value = value.toUpperCase();
		if(detail.getFocus().isTempBio()){
			if(value.matches(pattern)){
				//Verify uniqueness against existing recordset
				String queryString = tableName+"."+widgetName+"='"+value+"'";
				Query qry = new Query(tableName, queryString, null);
				BioCollectionBean ccList = uow.getBioCollectionBean(qry);
				if(ccList.size()>0){
					//fail save, show message
					parameter[0] = colonStrip(readLabel(widget)); 
					throw new FormException("WMEXP_CCPS_EXISTS" , parameter);
				}
				DataBean focus = detail.getFocus();
				if(focus.isBio()){
					BioBean bio = (BioBean) focus;
					bio.set(widgetName, value);
				}else if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean) focus;
					qbe.set(widgetName, value);
				}
			}else{
				//fail save, show message
				throw new FormException("WMEXP_CCPS_NON_CHARACTER", null);
			}
		}
		
		//Validate non negative values
		RuntimeFormWidgetInterface days = detail.getFormWidgetByName("CYCLEDAYS");
		RuntimeFormWidgetInterface negativeCountChange = detail.getFormWidgetByName("NEGCNTCHG");
		RuntimeFormWidgetInterface negativeDollarChange = detail.getFormWidgetByName("NEGDOLCHG");
		RuntimeFormWidgetInterface positiveCountChange = detail.getFormWidgetByName("POSCNTCHG");
		RuntimeFormWidgetInterface positiveDollarChange = detail.getFormWidgetByName("POSDOLCHG");
		String errorText = "";
		errorText = isNonNegative(days, errorText);
		if(negativeCountChange.getDisplayValue()!=null){
			errorText = isNonNegative(negativeCountChange, errorText);
		}
		if(negativeDollarChange.getDisplayValue()!=null){
			errorText = isNonNegative(negativeDollarChange, errorText);
		}
		if(positiveCountChange.getDisplayValue()!=null){
			errorText = isNonNegative(positiveCountChange, errorText);
		}
		if(positiveDollarChange.getDisplayValue()!=null){
			errorText = isNonNegative(positiveDollarChange, errorText);
		}

		if(!(errorText.equals(""))){
			parameter[0]=errorText;
			throw new FormException("WMEXP_LESS_THAN_ZERO", parameter);
		}
		return RET_CONTINUE;
	}
	
	private String isNonNegative(RuntimeFormWidgetInterface widget, String text)throws FormException{
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = LocaleUtil.resetQtyToDecimalForBackend(widget.getDisplayValue());//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		if(widget.getAttributeType()==BioAttributeTypes.INT_TYPE){
			if(number!=null){
				if(number.matches(".*\\..*")){
					parameter[0] = widgetLabel;
					throw new FormException("WMEXP_NON_INTEGER", parameter);
				}
				int value = Integer.parseInt(number);
				if(value<0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}else{
			float value = Float.parseFloat(number);
			if(value<0){
				if(text.equals("")){
					text += widgetLabel;
				}else{
					text += ", "+widgetLabel;
				}
			}
		}
		return text;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}