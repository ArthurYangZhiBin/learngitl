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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ValidateNoSpecialChar extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateNoSpecialChar.class);
	public ValidateNoSpecialChar() {
	}
	/**
	 * Modification History:
	 * 03/31/2009	AW	Bugaware: 8735
	 * 					Applying patch created for WMS 902. Removing restriction for all special characters 
	 * 					except single and double quotes. 
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException {			
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Executing ValidateKeyFields",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList attrNamesForValidate = (ArrayList) getParameter("widgetsForValidate");		
		ArrayList tabNames = (ArrayList) getParameter("tabs");
		//SM 02/12/08 - ISSUE SCM-00000-04119 Label Printer needs to allow backslashes for network printer names
		boolean isLabelPrinter = getParameterBoolean("isLabelPrinter");
		//Pattern pattern = Pattern.compile("[\\p{L}\\d]*"); //allows letters and numbers
		Pattern pattern;		
		if(isLabelPrinter){
			pattern = Pattern.compile("[^'\"]*"); //disallow ' " , < > & ;	//AW: 03/31/09 Bugaware: 8735			
		}else{
			pattern = Pattern.compile("[^'\"]*"); //disallow ' " , \ < > & ; //AW: 03/31/09 Bugaware: 8735
		}
		//SM 02/12/08 - End Update
		//Pattern pattern = Pattern.compile("[\\p{L}\\d[^'\",\\\\<>&; ]]*");

		Matcher matcher = null;
		
		shellForm = shellForm == null || shellForm.length() == 0?"wms_list_shell":shellForm;
		
		RuntimeFormInterface targetForm = null;
		if(tabNames == null || tabNames.size() == 0)
			targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,context.getState());
		else
			targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,tabNames,context.getState());
		
		if(targetForm != null){
			DataBean focus = null;
			if(targetForm.isListForm()){
				focus = ((RuntimeListFormInterface)targetForm).getQuickAddRowBean();				
			} else {
				focus = targetForm.getFocus();
			}
			_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","targetForm:"+targetForm.getName(),100L);			
			Iterator validateAttrItr = attrNamesForValidate == null?null:attrNamesForValidate.iterator();							
			_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","validateAttrItr:"+validateAttrItr,100L);
			if(validateAttrItr != null){
				List updatedAttributesList = null;
				if(!focus.isTempBio())
					updatedAttributesList = ((BioBean)focus).getUpdatedAttributes();
				if(updatedAttributesList != null && updatedAttributesList.size() == 0)
					updatedAttributesList = null;
				
				if((focus.isTempBio() && !((QBEBioBean)focus).isEmpty()) || updatedAttributesList != null){				
					boolean hasError = false;
					String widgetLabels = "";
					for(;validateAttrItr.hasNext();){					
						String attrKey = (String)validateAttrItr.next();	
						String attr = "";	
						RuntimeFormWidgetInterface widget = null;
						if(targetForm.isListForm())
							widget = ((RuntimeListForm)targetForm).getQuickAddRow().getFormWidgetByName(attrKey);
						else 
							widget = targetForm.getFormWidgetByName(attrKey);
						if(widget != null && widget.getValue() != null){
							_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Widget Attribute:"+widget.getAttribute(),100L);						
							if(updatedAttributesList == null || updatedAttributesList.contains(widget.getAttribute())){
								attr = widget.getValue().toString();
								if(attr.toString().length() > 0){
									matcher = pattern.matcher(attr);
									_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Checking String:"+attr+" For Special Chars...",100L);								
									if(!matcher.matches()){
										_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Special Chars Found...",100L);									
										hasError = true;
										if(widgetLabels.length() == 0)
											widgetLabels = widget.getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");
										else
											widgetLabels += ", "+widget.getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");															
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
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpecialChar","Exiting ValidateKeyFields...",100L);
		return RET_CONTINUE;
	}
}