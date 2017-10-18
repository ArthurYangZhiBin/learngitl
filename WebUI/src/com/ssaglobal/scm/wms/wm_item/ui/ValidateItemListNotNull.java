package com.ssaglobal.scm.wms.wm_item.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
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

//New class for Incident3735455_Defect285007: Removed ValidateListNotNull
public class ValidateItemListNotNull extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateItemListNotNull.class);
	String sessionObjectValue;
	String sessionVariable;
	public ValidateItemListNotNull()
	{
	}
	
	protected int execute(ActionContext context, ActionResult result)
	throws UserException, EpiException
	{			
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Executing ValidateListNotNull",100L);		
		String shellForm = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetFormName");
		ArrayList attrNamesForValidate = (ArrayList) getParameter("widgetsForValidate");
		
		boolean hasError = false;
		String widgetLabels = "";
		StateInterface state = context.getState();
        LocaleInterface locale = state.getUser().getLocale();
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,targetFormName,state);
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","targetForm:"+listForm.getName(),100L);	
		
		if (listForm != null)
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
				BioBean listRecord = listFocus.get("" + i);
						
				Iterator validateAttrItr = attrNamesForValidate == null?null:attrNamesForValidate.iterator();							
				_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","validateAttrItr:"+validateAttrItr,100L);
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
								_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Widget Attribute:"+widget.getAttribute(),100L);						
								if(updatedAttributesList.contains(widget.getAttribute()))
								{
									if(listRecord.getValue(widget.getAttribute()) == null)
									{									
										_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Nulls Found...",100L);									
										hasError = true;
										String labelString = widget.getLabel("label",locale).replaceAll(":","");
										if (!widgetLabels.contains(labelString))
										{
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
				String errorMsg = getTextMessage("WMEXP_SPECIAL_NO_NULLS",args,locale);
				_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Exiting ValidateListNotNull...",100L);					
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALNOTNULL","Exiting ValidateListNotNull...",100L);
		return RET_CONTINUE;
	}
	
	
}
