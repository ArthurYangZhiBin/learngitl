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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Disables the New and Delete button of the Cycle Count Detail form if the
 * corresponding Cycle Count has a status of 'Posted'
 * 
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CCDisableNewDeleteOnPosted extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDisableNewDeleteOnPosted.class);

	public CCDisableNewDeleteOnPosted()
	{
		_log.info("EXP_1", "CCDetailNewPreRender has been instantiated...", SuggestedCategory.NONE);

	}

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		
		try
		{

			// Get Parent Form (Shell)

			

			// returns Detail List Form
//			RuntimeListFormInterface parentListForm = (RuntimeListFormInterface) form.getParentForm(context.getState());
//			_log.debug("LOG_DEBUG_EXTENSION", "\n\n////-- 2 " + parentListForm.getName(), SuggestedCategory.NONE);

			// returns toggle
			RuntimeNormalFormInterface toggleForm = (RuntimeNormalFormInterface) form.getParentForm(context.getState());
			

			// returns shell
			RuntimeNormalFormInterface shellForm = (RuntimeNormalFormInterface) toggleForm.getParentForm(context.getState());
			
//
			// Retrieve the slot of the Header Form
			SlotInterface headerListSlot = shellForm.getSubSlot("list_slot_1");

			// Retrieve the Header Detail Form
			RuntimeFormInterface headerListForm = context.getState().getRuntimeForm(headerListSlot, "");
			//_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Name of Header Detail Form " + headerListForm.getName(), SuggestedCategory.NONE);

			// Get Focus of the Header Detail Form
			DataBean ccForm = headerListForm.getFocus();
			if (ccForm instanceof BioBean)
			{
				ccForm = (BioBean) ccForm;
			}

			// Retrieve the value of Status
			String statusValue = ccForm.getValue("STATUS").toString();
			_log.debug("LOG_DEBUG_EXTENSION", "//// Status: " + statusValue, SuggestedCategory.NONE);

//			// List Widgets
//			_log.debug("LOG_DEBUG_EXTENSION", "\n\n//// Listing Widgets of " + form.getName(), SuggestedCategory.NONE);
//			for (Iterator it = form.getFormWidgets(); it.hasNext();)
//			{
//				RuntimeWidget w = (RuntimeWidget) it.next();
//				_log.debug("LOG_DEBUG_EXTENSION", "//- Widget: " + w.getName() + " Class " + w.getClass().getName(), SuggestedCategory.NONE);
//			}

			// Disable New & Delete buttons of the Detail List Form
			if (statusValue.equals("9")) // 9 = Posted
			{
				RuntimeFormWidgetInterface newWidget = form.getFormWidgetByName("NEW");
				newWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				RuntimeFormWidgetInterface deleteWidget = form.getFormWidgetByName("DELETE");
				deleteWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
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
