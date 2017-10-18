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

package com.ssaglobal.scm.wms.wm_task_details.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
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

public class TaskDisableRowBasedOnCondition extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDisableRowBasedOnCondition.class);

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException
	{
		
		try
		{

			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			
			_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + form.getName() + " # of Rows " + listRows.length, SuggestedCategory.NONE);
			for (int i = 0; i < listRows.length; i++)
			{
				//Get List Widgets
				RuntimeFormWidgetInterface item = listRows[i].getFormWidgetByName("SKU");
				RuntimeFormWidgetInterface taskType = listRows[i].getFormWidgetByName("TASKTYPE");

				RuntimeFormWidgetInterface status = listRows[i].getFormWidgetByName("STATUS"); // 9 or OM
				RuntimeFormWidgetInterface priority = listRows[i].getFormWidgetByName("PRIORITY"); // 3,9 or OM
				RuntimeFormWidgetInterface user = listRows[i].getFormWidgetByName("USERKEY"); // 3,9 or OM

				//Get Values
				String statusValue = status.getDisplayValue();
				String taskTypeValue = taskType.getDisplayValue();
				String itemValue = item.getDisplayValue();

				_log.debug("LOG_DEBUG_EXTENSION", "-------ITEM " + itemValue + "-------", SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Status " + statusValue, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Task Type " + taskTypeValue, SuggestedCategory.NONE);
				//Test Values
				//9 = Completed
				//3 = In Process
				//OM = Optimize Move
				
				
				if (statusValue.equalsIgnoreCase("Completed") || taskTypeValue.equalsIgnoreCase("Optimize Move"))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Disabling item on status = Completed or type = Optimize Move", SuggestedCategory.NONE);

					status.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					priority.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					user.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					
//					status.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"No Edit");
//					priority.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"No Edit");
//					user.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"No Edit");

				}
				else if (statusValue.equalsIgnoreCase("In Process"))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Disabling item on status = In Process", SuggestedCategory.NONE);

					priority.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					user.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					
//					priority.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"No Edit");
//					user.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"No Edit");

				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!Not Disabling", SuggestedCategory.NONE);
					status.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					priority.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					user.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					
//					status.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"Edit");
//					priority.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"Edit");
//					user.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP,"Edit");
				}

				

			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}


