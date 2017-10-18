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

package com.ssaglobal.scm.wms.wm_task_manager_user.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
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

public class TaskUserEnableOnPermissionType extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskUserEnableOnPermissionType.class);
	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskUserEnableOnPermissionType preRenderForm", SuggestedCategory.NONE);
		try
		{
			DataBean currentFormFocus = form.getFocus();
			Object tempPermission = currentFormFocus.getValue("PERMISSIONTYPE");
			String permissionType;
			if (isNull(tempPermission))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Permission Type is null, doing nothing", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}
			else
			{
				permissionType = tempPermission.toString();
			}

			if (permissionType.equalsIgnoreCase("PK") || permissionType.equalsIgnoreCase("DP"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Enabling Widgets", SuggestedCategory.NONE);
				form.getFormWidgetByName("ALLOWPIECE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				form.getFormWidgetByName("ALLOWCASE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				form.getFormWidgetByName("ALLOWIPS").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				form.getFormWidgetByName("ALLOWPALLET").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Disabling Widgets", SuggestedCategory.NONE);
				form.getFormWidgetByName("ALLOWPIECE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				form.getFormWidgetByName("ALLOWCASE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				form.getFormWidgetByName("ALLOWIPS").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				form.getFormWidgetByName("ALLOWPALLET").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskUserEnableOnPermissionType modifyListValues", SuggestedCategory.NONE);
		try
		{
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();

			_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + form.getName() + " # of Rows " + listRows.length, SuggestedCategory.NONE);
			for (int i = 0; i < listRows.length; i++)
			{
				//Get List Widgets

				RuntimeFormWidgetInterface permissionType = listRows[i].getFormWidgetByName("PermissionType");
				RuntimeFormWidgetInterface line = listRows[i].getFormWidgetByName("USERLINENUMBER");

				RuntimeFormWidgetInterface allowPiece = listRows[i].getFormWidgetByName("ALLOWPIECE");
				RuntimeFormWidgetInterface allowCase = listRows[i].getFormWidgetByName("ALLOWCASE");
				RuntimeFormWidgetInterface allowIPS = listRows[i].getFormWidgetByName("ALLOWIPS");
				RuntimeFormWidgetInterface allowPallet = listRows[i].getFormWidgetByName("ALLOWPALLET");

//				_log.debug("LOG_SYSTEM_OUT","\n\n\n\n\t" + "Before - Piece",100L);
//				for(Iterator it = allowPiece.iterateAllProperties(); it.hasNext(); )
//				{
//					Object next = it.next();
//					_log.debug("LOG_SYSTEM_OUT","\n\t" + next + " = " + allowPiece.getProperty(next.toString()),100L);
//				}
//				
//				_log.debug("LOG_SYSTEM_OUT","\n\n\n\n\t" + "Before - Case",100L);
//				for(Iterator it = allowCase.iterateAllProperties(); it.hasNext(); )
//				{
//					Object next = it.next();
//					_log.debug("LOG_SYSTEM_OUT","\n\t" + next + " = " + allowCase.getProperty(next.toString()),100L);
//				}
				
				//Get Values
				String permissionTypeValue = permissionType.getDisplayValue();
				String lineValue = line.getDisplayValue();

				_log.debug("LOG_DEBUG_EXTENSION", "-------Line " + lineValue + "-------", SuggestedCategory.NONE);

				//Test Values
				//9 = Completed
				//3 = In Process
				//OM = Optimize Move

				if (permissionTypeValue.equalsIgnoreCase("Picks"))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Enabling Widgets", SuggestedCategory.NONE);
					allowPiece.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					allowCase.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					allowIPS.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					allowPallet.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					
//					allowPiece.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "Edit");
//					allowCase.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "Edit");
//					allowIPS.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "Edit");
//					allowPallet.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "Edit");

				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Disabling Widgets", SuggestedCategory.NONE);
					allowPiece.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					allowCase.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					allowIPS.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					allowPallet.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

//					allowPiece.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "No Edit");
//					allowCase.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "No Edit");
//					allowIPS.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "No Edit");
//					allowPallet.setLabel(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "No Edit");
				}
//				_log.debug("LOG_SYSTEM_OUT","\n\n\n\n\t" + "After - Piece",100L);
//				for(Iterator it = allowPiece.iterateAllProperties(); it.hasNext(); )
//				{
//					Object next = it.next();
//					_log.debug("LOG_SYSTEM_OUT","\n\t" + next + " = " + allowPiece.getProperty(next.toString()),100L);
//				}
//				
//				_log.debug("LOG_SYSTEM_OUT","\n\n\n\n\t" + "After - Case",100L);
//				for(Iterator it = allowCase.iterateAllProperties(); it.hasNext(); )
//				{
//					Object next = it.next();
//					_log.debug("LOG_SYSTEM_OUT","\n\t" + next + " = " + allowCase.getProperty(next.toString()),100L);
//				}
				
				_log.debug("LOG_DEBUG_EXTENSION", "------------------------", SuggestedCategory.NONE);
			}
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue)
			throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
