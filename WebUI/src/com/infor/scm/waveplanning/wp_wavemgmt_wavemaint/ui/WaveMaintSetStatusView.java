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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action.WPWaveMaintSetGraphType;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintSetStatusView extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintSetStatusView.class);

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
			StateInterface state = context.getState();
			Object chartType = WPUserUtil.getInteractionSessionAttribute(WPWaveMaintSetGraphType.SESSION_KEY_WAVEMGMT_GRAPHTYPE_VIEW, state);
			if(chartType != null)
			{
				//set widget
				RuntimeFormWidgetInterface viewDropDown = form.getFormWidgetByName("VIEWOPTION");
				viewDropDown.setValue(((Integer)chartType).toString());
				
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
	 * Called in response to the pre-render event on a form in a modal window. Write code
	 * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
	 * to the end user
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifySubSlot event on a form. Write code
	 * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetStatusView_modifySubSlots", "Entering WaveMaintSetStatusView",
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		try
		{
			//Set the Graph Form
			RuntimeFormWidgetInterface viewDropDown = form.getFormWidgetByName("VIEWOPTION");
			SlotInterface viewSlot = form.getSubSlot("GRAPHVIEW");
			String value = (String) viewDropDown.getValue();
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetStatusView_modifySubSlots", "Dropdown = " + value,
					SuggestedCategory.NONE);
			if (value == null || value.equals("0"))
			{
				//set to graph view
				form.setSpecialFormType(state, viewSlot, null, "wp_wavemgmt_wavemaint_graph_view");

			}
			else
			{
				//set to graph view
				form.setSpecialFormType(state, viewSlot, null, "wp_wavemgmt_wavemaint_graph_view");

			}

			//Set the focus of the Status List
			SlotInterface subSlot = form.getSubSlot("STATUSVIEW");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String interactionId = state.getInteractionId();
			DataBean focus = form.getFocus();
			DataBean rs = (DataBean) focus.getValue("WAVESTATUSCOLLECTION");
//			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatus", "wp_wavestatus.WAVEKEY = '"
//					+ BioAttributeUtil.getInt(focus, "WAVEKEY") + "' and wp_wavestatus.INTERACTIONID = '"
//					+ interactionId + "' and wp_wavestatus.WHSEID = '" + BioAttributeUtil.getString(focus, "WHSEID")
//					+ "'", null));
//			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatus", "wp_wavestatus.INTERACTIONID = '"
//					+ interactionId + "'", null));
//			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetStatusView_modifySubSlots", "Status List Size : " + rs.size(),
//					SuggestedCategory.NONE);
//			System.out.println("Status List Size : " + rs.size());
			form.setFocus(state, subSlot, "", rs, "wp_wavemgmt_wavemaint_status_list_view");

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSetStatusView_modifySubSlots", "Leaving WaveMaintSetStatusView",
				SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifySubSlot event on a form in a modal window. Write code
	 * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
	 * whether the form is re-displayed to the user or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}


	protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_execute", "Entering WaveMaintViewStatus " + form.getName(),
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		
		String waveKey = "";
		final String facility = WPUtil.getFacility(state.getRequest());

		//Clean Temp Records
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatus");
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatusheader");
		WPUserUtil.setInteractionSessionAttribute(WaveMaintViewStatus.SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW, null, state);

		final DataBean focus = parentForm.getFocus();
		Object waveKeyObj = focus.getValue("WAVEKEY");
		if(waveKeyObj!=null){
			waveKey = waveKeyObj.toString();
		}
		WaveMaintViewStatus.populateWaveStatus(state, facility, focus);
		WPUserUtil.setInteractionSessionAttribute(WaveMaintViewStatus.SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW, waveKey, state);

		//navigate to status form
		final String interactionId = state.getInteractionId();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		final Query waveStatusHeaderQuery = new Query("wp_wavestatusheader", "wp_wavestatusheader.WAVEKEY = '"
				+ waveKey + "' and wp_wavestatusheader.INTERACTIONID = '" + interactionId + "'", null);
		BioCollectionBean rs = uow.getBioCollectionBean(waveStatusHeaderQuery);
		for (int i = 0; i < rs.size(); i++)
		{
			form.setFocus(rs.get("" + i));
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_execute", "Leaving WaveMaintViewStatus",
				SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

}
