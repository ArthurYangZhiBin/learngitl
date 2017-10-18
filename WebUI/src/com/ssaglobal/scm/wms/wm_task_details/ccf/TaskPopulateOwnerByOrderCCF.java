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

package com.ssaglobal.scm.wms.wm_task_details.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class TaskPopulateOwnerByOrderCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskPopulateOwnerByOrderCCF.class);

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
/*		Object tempOrder = params.get("fieldValue");
		String owner;
		if (isEmpty(tempOrder))
		{
			return RET_CONTINUE;
		}
		tempOrder = tempOrder == null ? null : tempOrder.toString().toUpperCase();

		//retrieve Seller of Order
		String query = "SELECT STORERKEY FROM ORDERS WHERE ORDERKEY = '" + tempOrder + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "QUERY\n" + query, SuggestedCategory.NONE);

		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1)
		{
			//value exists
			owner = results.getAttribValue(1).getAsString();
		}
		else
		{
			return RET_CONTINUE;
		}

		HashMap allWidgets = getWidgetsValues(params);
		RuntimeFormWidgetInterface ownerWidget;
		for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
			Object value = entry.getValue();
			try
			{
				_log.debug("LOG_DEBUG_EXTENSION", "*& " + key.getName(), SuggestedCategory.NONE);
				//get the widget that has the right name but is on a different form
				if ((key.getName().equals("STORERKEY")))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "***Selecting this widget " + key.getName(), SuggestedCategory.NONE);
					ownerWidget = key;
					setValue(ownerWidget, owner);
				}
			} catch (NullPointerException e)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Exception Caught" + e.getMessage(), SuggestedCategory.NONE);
			}
		}
*/
		return RET_CONTINUE;

	}

	private boolean isEmpty(Object attributeValue)
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
