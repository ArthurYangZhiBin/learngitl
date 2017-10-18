package com.ssaglobal.scm.wms.wm_item.ui;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

//New class for Incident3735455_Defect285007: Removed MakeListBioAttributesUpperCase
public class MakeItemListBioAttributesUpperCase extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MakeItemListBioAttributesUpperCase.class);
	String sessionObjectValue;
	String sessionVariable;
	public MakeItemListBioAttributesUpperCase()
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
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,targetFormName,state);
		_log.debug("LOG_DEBUG_EXTENSION_MAKEBIOATTRIBUTESUPPERCASE","targetForm:"+listForm.getName(),100L);	
		
		if(listForm != null)
		{		
			BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();
			RuntimeListFormInterface itemList = (RuntimeListFormInterface)listForm;		
			String interactionID = state.getInteractionId();		
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			HttpSession session = context.getState().getRequest().getSession();
			//This value is set in the ItemListPreRender extension since this cannot be obtained in this extension.
			sessionObjectValue = (String)session.getAttribute(sessionVariable);
			
			int pageStart = Integer.parseInt(sessionObjectValue);
			int pageSize = itemList.getWindowSize();				
			int totalListSize = listFocus.size();  //Total size of the item list
			int totPrevItems = (totalListSize - pageStart); //Total no of items in the previous pages
			int startValue = 0;
			int endValue = 0;
			if (totPrevItems > pageSize) {
				startValue = pageStart;
				endValue = pageStart + pageSize;
			}
			else { //Last page in the item list
				startValue = pageStart;
				endValue = totalListSize;
			}

			for (int i = startValue; i < endValue; i++)
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
	
}
