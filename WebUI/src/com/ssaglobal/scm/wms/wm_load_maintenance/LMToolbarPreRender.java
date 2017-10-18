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


package com.ssaglobal.scm.wms.wm_load_maintenance;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LMToolbarPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the
	 * properties of a form that is
	 * being displayed to a user for the first time belong here. This is not
	 * executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException {

		try {
			StateInterface state = context.getState();
			RuntimeFormInterface loadMaintHeaderForm = FormUtil.findForm(form, "wms_list_shell", "wm_load_maintenance_detail_form", state);
			DataBean focus = loadMaintHeaderForm.getFocus();
			String status = BioAttributeUtil.getStringNoNull(focus, "STATUS");
			//if status equals shipped, disable new stop and new seal
			if ("9".equals(status)) {
				disableWidgets(form);
			} else {
				enableWidgets(form);
			}

		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void enableWidgets(RuntimeNormalFormInterface form) {
		form.getFormWidgetByName("NEW SEAL").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
		form.getFormWidgetByName("NEW STOP").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	}

	private void disableWidgets(RuntimeNormalFormInterface form) {
		form.getFormWidgetByName("NEW SEAL").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		form.getFormWidgetByName("NEW STOP").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

	}

}
