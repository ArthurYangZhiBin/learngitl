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

package com.ssaglobal.scm.wms.wm_pack_charge.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PackChargeEnableDisableWidgets extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

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
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		StateInterface state = context.getState();
		DataBean formFocus = state.getFocus();
		
		ArrayList prefix = new ArrayList();
		prefix.add(0, "CASE");
		prefix.add(1, "INNERPACK");
		prefix.add(2, "EACH");
		prefix.add(3, "PALLET");
		prefix.add(4, "CUBE");
		prefix.add(5, "GROSSWGT");
		prefix.add(6, "NETWGT");
		prefix.add(7, "OTHERUNIT1");
		prefix.add(8, "OTHERUNIT2");
		
		
		try
		{
			//Run through each row
			for(int i = 0 ; i < 9; i++)
			{
				String uomPrefix = (String) prefix.get(i);
				//if "PACKUOM" attr has a value - enable widgets
				//else disable
				if(!isEmpty(formFocus.getValue(uomPrefix + "PACKUOM")))
				{
					form.getFormWidgetByName(uomPrefix + "QUANTITY").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					for(int j = 1; j <= 3; j++)
					{
						form.getFormWidgetByName(uomPrefix + "FROM" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
						if(j==3)
						{
							form.getFormWidgetByName(uomPrefix + "TO" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
						}
						else
						{
							form.getFormWidgetByName(uomPrefix + "TO" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
						}
						form.getFormWidgetByName(uomPrefix + "CHARGE" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
					form.getFormWidgetByName(uomPrefix + "COST").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}
				else
				{
					form.getFormWidgetByName(uomPrefix + "QUANTITY").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					for(int j = 1; j <= 3; j++)
					{
						form.getFormWidgetByName(uomPrefix + "FROM" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
						form.getFormWidgetByName(uomPrefix + "TO" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
						form.getFormWidgetByName(uomPrefix + "CHARGE" + j).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					}
					form.getFormWidgetByName(uomPrefix + "COST").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
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
