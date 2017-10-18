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
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class RemoveRequiredIfUnEditable extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(RemoveRequiredIfUnEditable.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		EpnyControllerState state = (EpnyControllerState) context.getState();
		

		_log.debug("LOG_DEBUG_EXTENSION_RemoveRequiredIfUnEditable", "Form ReadOnly "
				+ form.getProperty(RuntimeFormInterface.PROP_READONLY), SuggestedCategory.NONE);
		if (((Integer) form.getProperty(RuntimeFormInterface.PROP_READONLY)).intValue() == 1)
		{
			//take off required if widgets are uneditable
			for (Iterator it = form.getFormWidgets(); it.hasNext();)
			{
				RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) it.next();

				if ("required".equals((String) widget.getProperty("input type")))
				{
					_log.debug("LOG_DEBUG_EXTENSION_RemoveRequiredIfUnEditable", "Setting " + widget.getName()
							+ " to normal", SuggestedCategory.NONE);
					widget.setProperty("input type", "normal");

				}

			}

		}

		return RET_CONTINUE;
	}

}
