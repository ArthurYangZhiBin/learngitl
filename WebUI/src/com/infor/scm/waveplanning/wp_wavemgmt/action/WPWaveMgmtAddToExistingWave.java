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

package com.infor.scm.waveplanning.wp_wavemgmt.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wave.util.WPWaveUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action.ConfirmWaveAddOrderNavSelect;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
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

public class WPWaveMgmtAddToExistingWave extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWaveMgmtAddToExistingWave.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Executing WPWaveMgmtAddToExistingWave", 100L);
		StateInterface state = context.getState();
		RuntimeListFormInterface ordersListForm = (RuntimeListFormInterface) WPFormUtil.findForm(state
				.getCurrentRuntimeForm(), "", "wm_wp_query_builder_shipment_orders_screen", state);
		if (ordersListForm == null)
		{
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "form is null... exiting.", 100L);
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
		BioCollectionBean ordersCollection = (BioCollectionBean) ordersListForm.getFocus();

		try
		{
			if (ordersCollection == null || ordersCollection.size() == 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "No orders Found...", 100L);
				String args[] = new String[0];
				String errorMsg = getTextMessage("WPEXP_NO_ORDERS_TO_ADD_TO_WAVE", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			}
		} catch (EpiDataException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Get wave from Session
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Getting Wave From Session", 100L);
		Integer waveId = (Integer) WPUserUtil.getInteractionSessionAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, state);

		//If nothing returned throw error
		if (waveId == null )
		{
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_NO_EXISTING_WAVEKEY", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_WPWaveMgmtAddToExistingWave_execute", "Wave Key " + waveId, SuggestedCategory.NONE);

		
		int waveKey = 0;
		
		try
		{
			waveKey = waveId.intValue();
		} catch (NumberFormatException e)
		{
			
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		} catch (NullPointerException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Wave Key is null... exiting.", 100L);
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}

		//Add Orders To Wave
		ArrayList orders = new ArrayList();
		ArrayList whsesList = new ArrayList();
		ArrayList segmentsList = new ArrayList();
		try
		{
			for (int i = 0; i < ordersCollection.size(); i++)
			{
				orders.add(ordersCollection.elementAt(i).get("ORDERKEY"));
				if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000))
				{
					whsesList.add(ordersCollection.elementAt(i).get("WHSEID"));
					segmentsList.add(ordersCollection.elementAt(i).get("SEGMENTID"));
				}
			}
		} catch (EpiDataException e)
		{
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Error occured while adding orders to wave... exiting.",
					100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", e.getStackTraceAsString(), 100L);
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
		try
		{
			if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000))
			{
				WPWaveUtil.addOrdersToWaveFor2000(waveKey, orders, whsesList, segmentsList, WPUtil.getFacility(state
						.getRequest()), "old", state);

			}
			else if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000))
			{
				WPWaveUtil.addOrdersToWave(waveKey, orders, WPUtil.getFacility(state.getRequest()), "old", state);
			}
		} catch (WavePlanningException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Error occured while adding orders to wave... exiting.",
					100L);
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}

		//state.getRequest().getSession().setAttribute(SESSION_KEY_WAVE, waveKey + "");
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
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
