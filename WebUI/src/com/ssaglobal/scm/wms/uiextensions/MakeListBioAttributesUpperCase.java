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

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class MakeListBioAttributesUpperCase extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MakeListBioAttributesUpperCase.class);
	public MakeListBioAttributesUpperCase()
    {
    }

	@Override
	protected int execute(ActionContext context, ActionResult result)
    throws UserException, EpiException
    {			
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","Executing MakeBioAttributesUpperCase",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList widgetNamesForUpdate = (ArrayList) getParameter("widgetsForUpdate");
		
		StateInterface state = context.getState();
        LocaleInterface locale = state.getUser().getLocale();
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,targetFormName,state);
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","targetForm:"+listForm.getName(),100L);	
		
		if(listForm != null)
		{		
			BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();

			for (int i = 0; i < listFocus.size(); i++)
			{
				Iterator updateWidgetItr = widgetNamesForUpdate == null?null:widgetNamesForUpdate.iterator();	
				_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","updateAttrItr:"+updateWidgetItr,100L);			
				if(updateWidgetItr != null)
				{
				
					BioBean listRecord = listFocus.get("" + i);
					for(;updateWidgetItr.hasNext();)
					{
						String widgetName = (String)updateWidgetItr.next();	
						RuntimeFormWidgetInterface formWidget = listForm.getFormWidgetByName(widgetName);	
						if(formWidget == null)
							throw new InvalidParameterException("Form "+listForm.getName()+" has no widget "+widgetName);
						String attributeName = formWidget.getAttribute();	
						Object attributeValue = listRecord.getValue(attributeName);
						if(attributeValue != null)
						{
							listRecord.set(attributeName,attributeValue.toString().toUpperCase());
							listForm.getFormWidgetByName(widgetName).setDisplayValue(attributeValue.toString().toUpperCase());
						}
					}
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","Exiting MakeBioAttributesUpperCase",100L);		
		return RET_CONTINUE;
	}
	
	/* Called when executed from a Modal Window */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE", "Executing Modal MakeBioAttributesUpperCase", 100L);
		String shellForm = (String) getParameter("shellForm");
		String targetFormName = (String) getParameter("targetForm");
		ArrayList widgetNamesForUpdate = (ArrayList) getParameter("widgetsForUpdate");

		StateInterface state = ctx.getState();
		LocaleInterface locale = state.getUser().getLocale();
		RuntimeFormInterface listForm = FormUtil.findForm(	state.getCurrentRuntimeForm(),
															shellForm,
															targetFormName,
															state);
		// if list form is null, try seeing if the modal body form is the target form
		if (listForm == null) {
			listForm = FormUtil.findForm(ctx.getModalBodyForm(0), shellForm, targetFormName, state);
		}
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE", "targetForm:" + listForm.getName(), 100L);

		if (listForm != null) {
			BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();

			for (int i = 0; i < listFocus.size(); i++) {
				Iterator updateWidgetItr = widgetNamesForUpdate == null ? null : widgetNamesForUpdate.iterator();
				_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE", "updateAttrItr:" + updateWidgetItr, 100L);
				if (updateWidgetItr != null) {

					BioBean listRecord = listFocus.get("" + i);
					for (; updateWidgetItr.hasNext();) {
						String widgetName = (String) updateWidgetItr.next();
						RuntimeFormWidgetInterface formWidget = listForm.getFormWidgetByName(widgetName);
						if (formWidget == null)
							throw new InvalidParameterException("Form " + listForm.getName() + " has no widget " + widgetName);
						String attributeName = formWidget.getAttribute();
						Object attributeValue = listRecord.getValue(attributeName);
						if (attributeValue != null) {
							listRecord.set(attributeName, attributeValue.toString().toUpperCase());
							listForm.getFormWidgetByName(widgetName).setDisplayValue(attributeValue.toString().toUpperCase());
						}
					}
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE", "Exiting MakeBioAttributesUpperCase", 100L);
		return RET_CONTINUE;

	}
		
	
}