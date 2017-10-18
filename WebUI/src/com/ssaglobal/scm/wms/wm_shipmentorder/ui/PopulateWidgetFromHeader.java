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

import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
public class PopulateWidgetFromHeader extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateWidgetFromHeader.class);
	
	public PopulateWidgetFromHeader(){
	}
	
	//Gets value from header and places in detail form
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException{
		
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		String source = context.getSourceWidget().getName();
		
		QBEBioBean qbe = null;
		
		//Executes only if detail form is a new screen
		if(focus.isTempBio()){
			qbe = (QBEBioBean)focus;
			String value = null;
			RuntimeFormInterface shellForm = null;
			RuntimeFormInterface normalHeaderForm = null;
			String name = getParameterString("widgetName");
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(name);
			String shellSlot = getParameterString("shellSlot1");
			String listShellName = getParameterString("listShellName");
			//one level to list shell
			shellForm = widget.getForm().getParentForm(state);
			while(!(shellForm.getName().equals(listShellName))){
				//iteratively finds list shell form
				shellForm = shellForm.getParentForm(state);
			}
			SlotInterface headerSlot = shellForm.getSubSlot(shellSlot); 
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,null);
			boolean headerInTabGroup = getParameterBoolean("headerInTabGroup");
			if(headerInTabGroup){
				String tabSlot = getParameterString("tabSlot");
				String tab = getParameterString("tab");
				SlotInterface slot2 = headerForm.getSubSlot(tabSlot);
				normalHeaderForm = state.getRuntimeForm(slot2, tab);
			}else{
				normalHeaderForm = headerForm;
			}
			value = normalHeaderForm.getFormWidgetByName(name).getDisplayValue();
			DataBean headerFocus = normalHeaderForm.getFocus();
			boolean validate = getParameterBoolean("validate");
			
			//Validates value from header if header is new
			if(headerFocus.isTempBio() && validate){
				String[] parameters = new String[1];
				String headerFieldName = getParameterString("widgetLabel");
				String bioName = getParameterString("bioName");
				String qry = bioName+"."+name+" = '";			
				qry = qry + value;
				//Throw exception when no value is entered
				if(qry.endsWith("null")){
					//Do not throw exception for first render
					if(source.equals("NEW")){
						return RET_CONTINUE;
					}else{
						parameters[0] = colonStrip(readLabel(normalHeaderForm, headerFieldName));
						throw new FormException("WMEXP_SO_ILQ_Null", parameters);						
					}
				}
				qry += "'";
				_log.debug("LOG_DEBUG_EXTENSION", "This is the query: "+qry, SuggestedCategory.NONE);;
				Query loadBiosQry = new Query(bioName, qry, null);
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
				//Throw exception for unrecognized value
				if(newFocus.size()<1){
					parameters[0] = colonStrip(readLabel(normalHeaderForm, headerFieldName));
					throw new FormException("WMEXP_SO_ILQ_Invalid", parameters);
				}
			} 
			qbe.set(name, value);
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
}