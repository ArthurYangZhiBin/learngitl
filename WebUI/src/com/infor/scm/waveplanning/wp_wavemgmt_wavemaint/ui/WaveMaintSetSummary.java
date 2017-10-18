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
import java.util.GregorianCalendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action.WaveMaintViewSummary;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintSetSummary extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintSetSummary.class);

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

		try
		{
			//Set the focus of the Summary List
			StateInterface state = context.getState();
			SlotInterface subSlot = form.getSubSlot("LIST SLOT");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String interactionId = state.getInteractionId();
			DataBean focus = form.getFocus();
			DataBean rs = (DataBean) focus.getValue("SOSUMMARIESCOLLECTION");

			form.setFocus(state, subSlot, "", rs, "wp_wavemgmt_wavemaint_summary_list_view");

			//Set the focus of the Total Form
			form.setFocus(state, "TOTAL SLOT", "", focus, "wp_wavemgmt_wavemaint_summary_totals_view");
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

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


		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the setFocusInForm event on a form. Write code
	 * to change the focus of this form. This code is executed everytime irrespective of whether the form
	 * is being redisplayed or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form) throws EpiException
	{

		try
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_setFocusInForm", "Entering WaveMaintSetSummary",
					SuggestedCategory.NONE);
			StateInterface state = context.getState();
			RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
			String  waveKey = "";
			final String facility = WPUtil.getFacility(state.getRequest());
			final String interactionId = state.getInteractionId();
			final String userId = WPUserUtil.getUserId(state);
			final GregorianCalendar currentDate = new GregorianCalendar();

			//Clean Temp Records
			WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatusheader");
			WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_shipmentordersummaryvotemp");

			final DataBean focus = parentForm.getFocus();
			Object value = focus.getValue("WAVEKEY");
			if(value != null){
				waveKey = value.toString();
			}
//			WaveMaintViewSummary.processSummaryRecords(state, waveKey, facility, interactionId, userId, currentDate, focus);
			WaveMaintViewSummary.processSummaryRecords(state, waveKey, focus);

			// Replace the following line with your code,
			// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			// as appropriate

			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			final Query waveStatusHeaderQuery = new Query("wp_wavestatusheader", "wp_wavestatusheader.WAVEKEY = '"
					+ waveKey + "' and wp_wavestatusheader.INTERACTIONID = '" + interactionId + "'", null);
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_setFocusInForm", "Querying for wp_wavestatusheader record "
					+ waveStatusHeaderQuery, SuggestedCategory.NONE);
			BioCollectionBean rs = uow.getBioCollectionBean(waveStatusHeaderQuery);
			for (int i = 0; i < rs.size(); i++)
			{
				form.setFocus(rs.get("" + i));
			}
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_setFocusInForm", "Leaving WaveMaintSetSummary",
					SuggestedCategory.NONE);

			

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the setFocusInForm event on a form in a modal window. Write code
	 * to change the focus of this form. This code is executed everytime irrespective of whether the form
	 * is being redisplayed or not.
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form) throws EpiException
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
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
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
	 * Called in response to the pre-render event on a list form in a modal dialog. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
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
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
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
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
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
}
