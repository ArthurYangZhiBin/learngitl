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

package com.ssaglobal.scm.wms.wm_generate_replenishments.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class GenerateDefaultValuesPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
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

		//populate default values on the Search form because it's not linked to  a bio
		try
		{
			StateInterface state = context.getState();
			RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell",
																"wm_generate_replenishments_search_view_form", state);

			String tenZs = "ZZZZZZZZZZ";
			searchForm.getFormWidgetByName("OWNERSTART").setDisplayValue("0");
			searchForm.getFormWidgetByName("OWNEREND").setDisplayValue(tenZs);
			searchForm.getFormWidgetByName("ZONESTART").setDisplayValue("0");
			searchForm.getFormWidgetByName("ZONEEND").setDisplayValue(tenZs);
			searchForm.getFormWidgetByName("ITEMSTART").setDisplayValue("0");
			searchForm.getFormWidgetByName("ITEMEND").setDisplayValue(tenZs);
			searchForm.getFormWidgetByName("PTRSTART").setDisplayValue("0");
			searchForm.getFormWidgetByName("PTREND").setDisplayValue(tenZs);
			searchForm.getFormWidgetByName("PRIORITYSTART").setDisplayValue("1");
			searchForm.getFormWidgetByName("PRIORITYEND").setDisplayValue("6");

			searchForm.getFormWidgetByName("RETRIEVE").setDisplayValue("0");
			
			
			searchForm.getFormWidgetByName("OWNERSTART").setValue("");
			searchForm.getFormWidgetByName("OWNEREND").setValue(tenZs);
			searchForm.getFormWidgetByName("ZONESTART").setValue("0");
			searchForm.getFormWidgetByName("ZONEEND").setValue(tenZs);
			searchForm.getFormWidgetByName("ITEMSTART").setValue("0");
			searchForm.getFormWidgetByName("ITEMEND").setValue(tenZs);
			searchForm.getFormWidgetByName("PTRSTART").setValue("0");
			searchForm.getFormWidgetByName("PTREND").setValue(tenZs);
			searchForm.getFormWidgetByName("PRIORITYSTART").setValue("1");
			searchForm.getFormWidgetByName("PRIORITYEND").setValue("6");

			searchForm.getFormWidgetByName("RETRIEVE").setValue("0");
			searchForm.getFormWidgetByName("RADIOBUTTON").setValue("1");

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
