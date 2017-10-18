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

package com.ssaglobal.scm.wms.wm_container_manifest.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class ContainerLabelsPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ContainerLabelsPreRender.class);
	/**
	 * Called in response to the pre-render event on a form in a modal window. Write code
	 * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
	 * to the end user
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		

		//disable either RFID printers or NONRFID printers
		StateInterface state = context.getState();
		
		RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
		
		RuntimeFormInterface toolbarForm = context.getSourceForm();
		RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);
		
		
		
		try
		{
			ArrayList items = listForm.getAllSelectedItems();
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedItem = (BioBean) it.next();
				_log.debug("LOG_DEBUG_EXTENSION", "ContainerID " + selectedItem.getString("CONTAINERKEY"), SuggestedCategory.NONE);
				//Get RFID value
				//disable appropriate widget
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
