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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class MakeBioAttributesUpperCase extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MakeBioAttributesUpperCase.class);
	public MakeBioAttributesUpperCase()
    {
    }

	protected int execute(ActionContext context, ActionResult result)
    throws UserException
    {			
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","Executing MakeBioAttributesUpperCase",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList attrNamesForNew = (ArrayList) getParameter("bioAttributesForNew");
		ArrayList attrNamesForUpdate = (ArrayList) getParameter("bioAttributesForUpdate");
		ArrayList tabNames = (ArrayList) getParameter("tabs");
		
		RuntimeFormInterface targetForm = null;
		if(tabNames == null || tabNames.size() == 0)
			targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,context.getState());
		else
			targetForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),shellForm,targetFormName,tabNames,context.getState());
		
		if(targetForm != null){
			if(targetForm.isListForm()){
				_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","Exiting MakeBioAttributesUpperCase",100L);				
				throw new InvalidParameterException("target form cannot be a list form");
			}
			_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","targetForm:"+targetForm.getName(),100L);			
			Iterator newAttrItr = attrNamesForNew == null?null:attrNamesForNew.iterator();
			Iterator updateAttrItr = attrNamesForUpdate == null?null:attrNamesForUpdate.iterator();	
			_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","newAttrItr:"+newAttrItr,100L);
			_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","updateAttrItr:"+updateAttrItr,100L);			
			if(newAttrItr != null && targetForm.getFocus().isTempBio()){
				QBEBioBean bio = (QBEBioBean)targetForm.getFocus();	
				for(;newAttrItr.hasNext();){					
					String attrKey = (String)newAttrItr.next();	
					String attr = "";
					RuntimeFormWidgetInterface formWidget = targetForm.getFormWidgetByName(attrKey);
					if(formWidget == null)
						throw new InvalidParameterException("Form "+targetForm.getName()+" has no widget "+attrKey);
					attr = targetForm.getFormWidgetByName(attrKey).getDisplayValue();
					if(attr == null){
						//Commenting because CRB will take care of this.
						//bio.set(attrKey,null);
					}
					else{
						bio.set(attrKey,attr.toUpperCase());
					}
				}
			}else if(updateAttrItr != null){
				BioBean bio = (BioBean)targetForm.getFocus();
				for(;updateAttrItr.hasNext();){
					String attrKey = (String)updateAttrItr.next();	
					String attr = "";
					RuntimeFormWidgetInterface formWidget = targetForm.getFormWidgetByName(attrKey);					
					if(formWidget == null)
						throw new InvalidParameterException("Form "+targetForm.getName()+" has no widget "+attrKey);
					attr = targetForm.getFormWidgetByName(attrKey).getDisplayValue();
					if(attr == null){
						//bio.set(attrKey,null);
					}
					else{
						bio.set(attrKey,attr.toUpperCase());
					}
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","Exiting MakeBioAttributesUpperCase",100L);		
		return RET_CONTINUE;
	}
		
	
}