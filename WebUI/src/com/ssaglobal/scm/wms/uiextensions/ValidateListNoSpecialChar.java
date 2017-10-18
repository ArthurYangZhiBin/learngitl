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
package com.ssaglobal.scm.wms.uiextensions;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ValidateListNoSpecialChar extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateListNoSpecialChar.class);
	public ValidateListNoSpecialChar() {
	}
	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {			
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Executing ValidateKeyFields",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList attrNamesForValidate = (ArrayList) getParameter("widgetsForValidate");	
		boolean isLabelPrinter = getParameterBoolean("isLabelPrinter");
		
		//Pattern pattern = Pattern.compile("[\\p{L}\\d]*"); //allows letters and numbers
		Pattern pattern;		
		if(isLabelPrinter){
			pattern = Pattern.compile("[^'\",<>&; ]*"); //disallow ' " , < > & ;			
		}else{
			pattern = Pattern.compile("[^'\",\\\\<>&; ]*"); //disallow ' " , \ < > & ;
		}

		Matcher matcher = null;
		
		shellForm = shellForm == null || shellForm.length() == 0?"wms_list_shell":shellForm;

		boolean hasError = false;
		String widgetLabels = "";
		StateInterface state = context.getState();
        LocaleInterface locale = state.getUser().getLocale();
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,targetFormName,state);
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","targetForm:"+listForm.getName(),100L);	
		

		if (listForm != null)
		{
			BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();

			for (int i = 0; i < listFocus.size(); i++)
			{
				BioBean listRecord = listFocus.get("" + i);
						
				Iterator validateAttrItr = attrNamesForValidate == null?null:attrNamesForValidate.iterator();							
				_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","validateAttrItr:"+validateAttrItr,100L);
				if(validateAttrItr != null)
				{
					List updatedAttributesList = listRecord.getUpdatedAttributes();
					if(updatedAttributesList != null && updatedAttributesList.size() == 0)
						updatedAttributesList = null;

					if(updatedAttributesList != null)
					{
						for(;validateAttrItr.hasNext();)
						{				
							String widgetName = (String)validateAttrItr.next();	
							RuntimeFormWidgetInterface widget = listForm.getFormWidgetByName(widgetName);								
							if(widget != null)
							{
								_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Widget Attribute:"+widget.getAttribute(),100L);						
								if(updatedAttributesList.contains(widget.getAttribute()))
								{
									String attr = listRecord.getValue(widget.getAttribute()).toString();
									if(attr.length() > 0)
									{		
										matcher = pattern.matcher(attr);
										_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Checking String:"+attr+" For Special Chars...",100L);
										String labelString = widget.getLabel("label",locale).replaceAll(":","");								
										if(!matcher.matches() && !widgetLabels.contains(labelString)){
											_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Special Chars Found...",100L);									
											hasError = true;
											if(widgetLabels.length() == 0)
												widgetLabels = labelString;
											else
												widgetLabels += ", "+labelString;															
										}
									}
								}
							}
						}							
					}	
				}
				
			}
			if(hasError){
				String args[] = {widgetLabels}; 
				String errorMsg = getTextMessage("WMEXP_SPECIAL_CHARS_IN_KEY",args,context.getState().getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Exiting ValidateKeyFields...",100L);					
				throw new UserException(errorMsg,new Object[0]);
			}
		}
					
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Exiting ValidateKeyFields...",100L);
		return RET_CONTINUE;
	}
}