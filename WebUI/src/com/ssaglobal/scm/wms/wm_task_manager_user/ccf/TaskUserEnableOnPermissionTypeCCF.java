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

package com.ssaglobal.scm.wms.wm_task_manager_user.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
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
public class TaskUserEnableOnPermissionTypeCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskUserEnableOnPermissionTypeCCF.class);
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
	@Override
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskUserEnableOnPermissionTypeCCF", SuggestedCategory.NONE);
		Object taskValue = params.get("fieldValue");
		if (isEmpty(taskValue))
		{
			return RET_CANCEL;
		}
		if (taskValue.toString().equalsIgnoreCase("PK") || taskValue.toString().equalsIgnoreCase("DP"))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Enabling Widgets", SuggestedCategory.NONE);
			setProperty(form.getFormWidgetByName("ALLOWPIECE"), RuntimeFormWidgetInterface.PROP_READONLY, "false");
			setProperty(form.getFormWidgetByName("ALLOWCASE"), RuntimeFormWidgetInterface.PROP_READONLY, "false");
			setProperty(form.getFormWidgetByName("ALLOWIPS"), RuntimeFormWidgetInterface.PROP_READONLY, "false");
			setProperty(form.getFormWidgetByName("ALLOWPALLET"), RuntimeFormWidgetInterface.PROP_READONLY, "false");
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Disabling Widgets", SuggestedCategory.NONE);
			setProperty(form.getFormWidgetByName("ALLOWPIECE"), RuntimeFormWidgetInterface.PROP_READONLY, "true");
			setProperty(form.getFormWidgetByName("ALLOWCASE"), RuntimeFormWidgetInterface.PROP_READONLY, "true");
			setProperty(form.getFormWidgetByName("ALLOWIPS"), RuntimeFormWidgetInterface.PROP_READONLY, "true");
			setProperty(form.getFormWidgetByName("ALLOWPALLET"), RuntimeFormWidgetInterface.PROP_READONLY, "true");
		}

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
