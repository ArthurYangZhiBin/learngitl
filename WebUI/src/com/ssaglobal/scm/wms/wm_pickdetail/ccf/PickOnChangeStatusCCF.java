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

package com.ssaglobal.scm.wms.wm_pickdetail.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class PickOnChangeStatusCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickOnChangeStatusCCF.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException 
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "!@# Start of PickOnChangeStatusCCF", SuggestedCategory.NONE);;
		
		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
		
		int status = 0;
		try
		{
			status = Integer.parseInt(params.get("fieldValue").toString());
		} catch (NullPointerException e)
		{
			return RET_CANCEL;
		}

		int previousStatus = 0;

		previousStatus = Integer.parseInt(form.getFocus().getValue("STATUS") == null ? "0"
				: form.getFocus().getValue("STATUS").toString());

		_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "Current Status " + status + ", Previous Status " + previousStatus, SuggestedCategory.NONE);;

		if (5 <= status && status < 9 && previousStatus < 5)
		{
			String query = "SELECT CONFIGKEY, NSQLVALUE FROM NSQLCONFIG WHERE CONFIGKEY = 'DOMOVEWHENPICKED' AND NSQLVALUE = '0'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if ((results.getRowCount() == 1))
			{
				//do nothing
				return RET_CONTINUE;
			}

			Object tempToLocValue = widgetNamesAndValues.get("TOLOC");
			Object tempLocValue = widgetNamesAndValues.get("LOC");

			if (isEmpty(tempToLocValue))
			{
				_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "!@# TOLOC is empty", SuggestedCategory.NONE);
				if (isEmpty(tempLocValue))
				{
					_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "!## LOC is empty", SuggestedCategory.NONE);
				}
				else
				{
					String locValue = tempLocValue == null ? null : tempLocValue.toString().toUpperCase();
					//check type of location
					//"Select count(*) From LOC Where Loc = '" + ls_loc + "' and LocationType = 'PICKTO'"
					query = "SELECT COUNT(*) "
							+ "FROM LOC "
							+ "WHERE LOC.LOC = '" + locValue + "' and LOC.LOCATIONTYPE = 'PICKTO'";
					results = WmsWebuiValidationSelectImpl.select(query);
					_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "Row Count" + results.getRowCount(), SuggestedCategory.NONE);
					if (results.getRowCount() == 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "!!!!Raising Error, Loc must be of type PICKTO", SuggestedCategory.NONE);;
						setFormErrorMessage(getTextMessage("WMEXP_PICK_LOC", new Object[] {}, state.getLocale()));
						return RET_CANCEL;
						
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "!! Location Not Found", SuggestedCategory.NONE);
					}
				}
			}
			else
			{
				String toLocValue = tempToLocValue.toString();
				_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "Setting TOLOC to LOC", SuggestedCategory.NONE);
				setValue(form.getFormWidgetByName("LOC"), toLocValue);
				setValue(form.getFormWidgetByName("TOLOC"), "");
			}

		}

		//		}

		//		Retrieve Status Value
		//		if 9>status>=5
		//			check for DoMoreWhenPicked in NSQLConfig
		//				if NSQLValue  = 0
		//					if toloc not blank
		//						then assign toloc to location
		//						toloc become "_"
		//					if toloc is blank
		//						if loc is not PICKTO type
		//							raise error

		return RET_CONTINUE;

	}

	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "# " + widgetNames[i] + " " + widgetValues[i], SuggestedCategory.NONE);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}

	private void listParams(HashMap params)
	{
		//		List Params
		_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", "Listing Paramters-----", SuggestedCategory.NONE);;
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			_log.debug("LOG_DEBUG_EXTENSION_PickOnChangeStatusCCF", key.toString() + " " + value.toString(), SuggestedCategory.NONE);
		}
	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
