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

package com.ssaglobal.scm.wms.wm_order_status.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
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

public class OrderStatusListPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{


	protected static ILoggerCategory _log = LoggerFactory.getInstance(OrderStatusListPreRender.class);

	/**
	 * Called in response 	`to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + form.getName() + " # of Rows " + listRows.length, SuggestedCategory.NONE);;

			for (int i = 0; i < listRows.length; i++)
			{
				//Get List Widgets
				RuntimeFormWidgetInterface editable = listRows[i].getFormWidgetByName("EDITABLE");
				RuntimeFormWidgetInterface enable = listRows[i].getFormWidgetByName("ENABLED");
				RuntimeFormWidgetInterface order = listRows[i].getFormWidgetByName("ORDERFLAG");
				RuntimeFormWidgetInterface flowThru = listRows[i].getFormWidgetByName("XORDERFLAG");
				RuntimeFormWidgetInterface code = listRows[i].getFormWidgetByName("CODE");

				//Get Values
				String codeValue = code.getDisplayValue();
				String editableValue = editable.getDisplayValue();
				String enableValue = enable.getDisplayValue();
				_log.debug("LOG_DEBUG_EXTENSION_OrderStatusListPreRender", codeValue + " - " + editableValue, SuggestedCategory.NONE);

				//				_log.debug("LOG_DEBUG_EXTENSION", "-------Code " + codeValue + "----------", SuggestedCategory.NONE);;
				//				_log.debug("LOG_DEBUG_EXTENSION", "editable " + editableValue, SuggestedCategory.NONE);;
				//				_log.debug("LOG_DEBUG_EXTENSION", "enable " + enableValue, SuggestedCategory.NONE);;
				//				_log.debug("LOG_DEBUG_EXTENSION", "========================", SuggestedCategory.NONE);;

				/*
				 * Enable			If editable = 0, disable
				 * Shipment Order	If editable = 0 and enable = 0, disable
				 * Flow Thru Order	If editable = 0 and enable = 0, disable
				 */

				if (editableValue.equals("0"))
				{
					_log.debug("LOG_DEBUG_EXTENSION_OrderStatusListPreRender", "Disabling ENABLE " + enableValue + " "
							+ code.getDisplayValue(), SuggestedCategory.NONE);
					enable.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
//					enable.setProperty("widget style","color:#eee;background:#040");
//					code.setProperty("widget style","color:#eee;background:#040");
//					code.setProperty(RuntimeFormWidgetInterface.LABEL_TOOLTIP, "No Edit");
					if ((enableValue.equals("0")))
					{
						_log.debug("LOG_DEBUG_EXTENSION_OrderStatusListPreRender", "Disabling SHIPMENT ORDER "
								+ code.getDisplayValue(), SuggestedCategory.NONE);
						order.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
//						order.setProperty("widget style","color:#eee;background:#040");
						_log.debug("LOG_DEBUG_EXTENSION_OrderStatusListPreRender", "Disabling FLOW THRU "
								+ code.getDisplayValue(), SuggestedCategory.NONE);
						flowThru.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
//						flowThru.setProperty("widget style","color:#eee;background:#040");
					}
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
