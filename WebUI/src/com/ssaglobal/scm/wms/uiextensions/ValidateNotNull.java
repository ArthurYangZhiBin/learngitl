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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ValidateNotNull extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateNotNull.class);
	public ValidateNotNull()
	{
	}
	
	protected int execute(ActionContext context, ActionResult result)
	throws UserException
	{			
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Executing ValidateNotNull",100L);		
		String shellForm = (String)getParameter("shellForm");
		ArrayList targetFormNames = (ArrayList)getParameter("targetFormNames");
		ArrayList attrNamesForValidate = (ArrayList) getParameter("widgetsForValidate");		
		ArrayList tabNames = (ArrayList) getParameter("tabs");		
		Iterator formItr = targetFormNames==null?null:targetFormNames.iterator();
		shellForm = shellForm == null || shellForm.length() == 0?"wms_list_shell":shellForm;		
		if(formItr != null){
			boolean hasError = false;
			String widgetLabels = "";
			while(formItr.hasNext()){
				String targetFormName = (String)formItr.next();
				RuntimeFormInterface targetForm = null;
				if(tabNames == null || tabNames.size() == 0)
					targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,context.getState());
				else
					targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,tabNames,context.getState());
				
				
				if(targetForm != null){
					DataBean focus = null;
					if(targetForm.isListForm()){
						focus = ((RuntimeListFormInterface)targetForm).getQuickAddRowBean();				
					}
					else{
						focus = targetForm.getFocus();
					}
					_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","targetForm:"+targetForm.getName(),100L);			
					Iterator validateAttrItr = attrNamesForValidate == null?null:attrNamesForValidate.iterator();							
					_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","validateAttrItr:"+validateAttrItr,100L);
					if(validateAttrItr != null){
						List updatedAttributesList = null;
						if(!focus.isTempBio())
							updatedAttributesList = ((BioBean)focus).getUpdatedAttributes();
						if(updatedAttributesList != null && updatedAttributesList.size() == 0)
							updatedAttributesList = null;
						
						if((focus.isTempBio() && !((QBEBioBean)focus).isEmpty()) || updatedAttributesList != null){
						
							for(;validateAttrItr.hasNext();){					
								String attrKey = (String)validateAttrItr.next();									
								RuntimeFormWidgetInterface widget = null;
								if(targetForm.isListForm())
									widget = ((RuntimeListForm)targetForm).getQuickAddRow().getFormWidgetByName(attrKey);
								else 
									widget = targetForm.getFormWidgetByName(attrKey);								
								if(widget != null){
									_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Widget Attribute:"+widget.getAttribute(),100L);						
									if(updatedAttributesList == null || updatedAttributesList.contains(widget.getAttribute())){								
										if(widget.getValue() == null){									
											_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Nulls Found...",100L);									
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
					}
				}
			}
			if(hasError){
				String args[] = {widgetLabels}; 
				String errorMsg = getTextMessage("WMEXP_SPECIAL_NO_NULLS",args,context.getState().getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Exiting ValidateNotNull...",100L);					
				throw new UserException(errorMsg,new Object[0]);
				
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Exiting ValidateNotNull...",100L);
		return RET_CONTINUE;
	}
	
	
}