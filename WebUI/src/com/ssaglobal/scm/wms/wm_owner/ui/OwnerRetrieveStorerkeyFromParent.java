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

package com.ssaglobal.scm.wms.wm_owner.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class OwnerRetrieveStorerkeyFromParent extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerRetrieveStorerkeyFromParent.class);

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

		try
		{
			// Add your code here to process the event
			StateInterface state = context.getState();

			DataBean currentFormFocus = form.getFocus();
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}

			String ownerAttribute = getParameterString("OWNERATTR");
			
			// If we already have a value (ie we clicked the detail button not creating a new record)
			// then we do not want to go get the owner...
			if ( currentFormFocus.getValue(ownerAttribute) != null )
			{
				return RET_CONTINUE;
			}

			RuntimeFormInterface detailToggleForm = form.getParentForm(state);

			RuntimeFormInterface tabGroupForm = detailToggleForm.getParentForm(state);

			SlotInterface tabGroupSlot = tabGroupForm.getSubSlot("tbgrp_slot");
			if (tabGroupSlot == null)
			{
				tabGroupSlot = tabGroupForm.getSubSlot("wm_tbgrp_billto");
			}

			RuntimeFormInterface parentForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
			

			DataBean parentFocus = parentForm.getFocus();

			if (parentFocus instanceof BioBean)
			{
				parentFocus = (BioBean) parentFocus;
			}
			Object parentStorerKey = parentFocus.getValue("STORERKEY") == null ? null
					: parentFocus.getValue("STORERKEY").toString().toUpperCase();

			_log.debug("LOG_DEBUG_EXTENSION", "!!@#!@ " + parentStorerKey, SuggestedCategory.NONE);
			currentFormFocus.setValue(ownerAttribute, parentStorerKey);
			form.getFormWidgetByName(ownerAttribute).setDisplayValue(parentStorerKey.toString());

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
