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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
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

public class ItemSGTINEnableDisablePreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemSGTINEnableDisablePreRender.class);

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
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException
	{
		

		try
		{
			//retrieve value of Item Reference
			StateInterface state = context.getState();

			//			DataBean currentFormFocus = form.getFocus();
			//			if (currentFormFocus instanceof BioBean)
			//			{
			//				currentFormFocus = (BioBean) currentFormFocus;
			//			}

			//String itemAttribute = getParameterString("ITEMATTR");

			

//			RuntimeFormInterface detailToggleForm = form.getParentForm(state);
			

//			RuntimeFormInterface tabGroupForm = detailToggleForm.getParentForm(state);
			RuntimeFormInterface outboundForm = form.getParentForm(state);
			String f1Name = outboundForm.getName();
			
			RuntimeFormInterface tabGroupForm = outboundForm.getParentForm(state);
			
			SlotInterface tabGroupSlot = tabGroupForm.getSubSlot("tbgrp_slot");
  
			RuntimeFormInterface parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
			String fName = parentForm.getName();

			DataBean parentFocus = parentForm.getFocus();

			if (parentFocus instanceof BioBean)
			{
				parentFocus = (BioBean) parentFocus;
			}

			//If null, disable each blank
			Object tempValue = parentFocus.getValue("ITEMREFERENCE");
			if (tempValue == null)
			{
				disableFormWidgets(form);

			}
			else if (tempValue.toString().matches("\\s*"))
			{
				disableFormWidgets(form);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Item Reference " + tempValue, SuggestedCategory.NONE);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void disableFormWidgets(RuntimeNormalFormInterface form)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Disabling Form Widgets", SuggestedCategory.NONE);
		for (Iterator it = form.getFormWidgets(); it.hasNext();)
		{
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();
			if(widget != null){
				if("SERIALNUMBERSTART".equalsIgnoreCase(widget.getName()) || 
						"SERIALNUMBERNEXT".equalsIgnoreCase(widget.getName()) ||
						"SERIALNUMBEREND".equalsIgnoreCase(widget.getName())){
							widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}
			}
		}
	}
}
