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

package com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollectionRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ConfirmWaveRemoveOrder extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmWaveRemoveOrder.class);

	private String CONTEXT_WAVEMGMT_WAVEKEY = "WAVEMGMT_WAVEKEY";

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
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder", "Entering ConfirmWaveRemoveOrder",
				SuggestedCategory.NONE);

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);

//		String waveKey = retrieveWaveKey(context);
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String waveKey = userContext.get("WAVEMGMT_WAVEKEY").toString();
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_execute", "WaveKey " + waveKey, SuggestedCategory.NONE);
		if ("".equalsIgnoreCase(waveKey))
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveRemoveOrder_execute", "Unable to retrieve WaveKey, exiting",
					SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		ArrayList serialKeysList = new ArrayList();

		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave_execute", "No Waves Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_NO_ORDER_SELECTED", new Object[] {});
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder", "Called from List Form "
						+ parentForm.getName(), SuggestedCategory.NONE);

				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{
					DataBean focus = (DataBean) it.next();
					serialKeysList.add(focus.getValue("SERIALKEY").toString());
				}
				removeOrdersFromWave(state, uow, waveKey, serialKeysList);
				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder", "Called from Normal Form "
					+ currentRuntimeForm.getName(), SuggestedCategory.NONE);
			DataBean focus = currentRuntimeForm.getFocus();

			serialKeysList.add(focus.getValue("SERIALKEY").toString());

			removeOrdersFromWave(state, uow, waveKey, serialKeysList);

		}
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder", "Leaving ConfirmWaveRemoveOrder",
				SuggestedCategory.NONE);
		
		
		//decide nevigation
		RuntimeFormInterface slot = parentForm.getParentForm(state);
		if("wm_wp_shell_form.slot2".equalsIgnoreCase(slot.getName())){
			context.setNavigation("clickEvent1876");
		}else{
			context.setNavigation("clickEvent1854");
		}
	   	Query qryReload = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY='"+waveKey+"'", null);			 		
		BioCollectionBean resultCollection = uow.getBioCollectionBean(qryReload);	
		BioCollectionRef bcRef = null;
		bcRef = resultCollection.get("0").getBioCollection("ORDERBIOCOLLECTION").getBioCollectionRef();
		result.setFocus(uow.getBioCollection(bcRef));
		uow.clearState();

		
		return RET_CONTINUE;
	}

	private void removeOrdersFromWave(StateInterface state, UnitOfWorkBean uow, String waveKey, ArrayList serialKeysList) throws UserException
	{
		try
		{
			//removeOrdersFromWave
			String wmsName = WavePlanningUtils.wmsName;

			//getOrderDetailsForRemove
			String orderDetailsToRemove = "";
			String serialKeys = "";

			serialKeys = flattenList(serialKeysList, serialKeys);
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave",
					"SerialKeys To Be Removed From The Wave " + serialKeys, SuggestedCategory.NONE);
			orderDetailsToRemove += "wm_wp_orders.SERIALKEY IN ( " + serialKeys + " )";
			Query orderDetailsQuery = new Query("wm_wp_orders", orderDetailsToRemove, null);
			BioCollectionBean orderDetailsCollection = uow.getBioCollectionBean(orderDetailsQuery);
			ArrayList whsesList = new ArrayList(orderDetailsCollection.size());
			ArrayList ordersList = new ArrayList(orderDetailsCollection.size());
			ArrayList segmentsList = new ArrayList(orderDetailsCollection.size());
			for (int i = 0; i < orderDetailsCollection.size(); i++)
			{
				BioBean order = orderDetailsCollection.get("" + i);
				whsesList.add(order.get("WHSEID"));
				ordersList.add(order.get("ORDERKEY"));
//				segmentsList.add(order.get("SEGMENTID"));
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", whsesList.toString(),
						SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", ordersList.toString(),
						SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", segmentsList.toString(),
						SuggestedCategory.NONE);
			}

				String orders = "";
				orders = flattenList(ordersList, orders);
				final String target = "wm_wp_wavedetail";
				Query deleteWaveDetailsQuery = new Query(target, target + ".WAVEKEY ='" + waveKey + "' and " + target
						+ ".ORDERKEY IN (" + orders + " )", null);
				BioCollectionBean deletedWaves = uow.getBioCollectionBean(deleteWaveDetailsQuery);
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", "Deleting "
						+ deletedWaves.size() + " wm_wp_wavedetail", SuggestedCategory.NONE);
				for (int i = 0; i < deletedWaves.size(); i++)
				{
					deletedWaves.get("" + i).delete();
				}



			//Update Status
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", "Updating Status ",
					SuggestedCategory.NONE);
			Query waveQuery = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY = '" + waveKey + "'", null);
			BioCollectionBean parentWave = uow.getBioCollectionBean(waveQuery);
			if (parentWave.size() == 1)
			{
				parentWave.get("" + 0).set("STATUS", new Integer(WavePlanningConstants.WAVE_STATUSES_MODIFIED));
			}

			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_removeOrdersFromWave", "Saving UOW ",
					SuggestedCategory.NONE);
			uow.saveUOW();

		} catch (UnitOfWorkException e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveUpdateWave",
					"UnitOfWorkException trying to Remove Orders From Wave " + waveKey + e.getErrorMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);

		} catch (Exception e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveUpdateWave",
					"Generic Exception trying to Remove Order From Wave " + e.getMessage(), SuggestedCategory.NONE);
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
	}

	private String retrieveWaveKey(ActionContext context)
	{
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String contextKey = CONTEXT_WAVEMGMT_WAVEKEY + context.getState().getInteractionId();
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_retrieveWaveKey", "ContextKey " + contextKey,
				SuggestedCategory.NONE);
		String waveKey = (String) userContext.get(contextKey);
		if (waveKey != null && !"".equalsIgnoreCase(waveKey))
		{
			return waveKey;
		}
		//no WaveKey in context, try retrieving from Form
		StateInterface state = context.getState();
		RuntimeFormInterface confirmWaveHeader = WPFormUtil.findForm(state.getCurrentRuntimeForm(), "",
				getParameterString("WAVEFORM"), state);
		if (confirmWaveHeader != null)
		{
			DataBean focus = confirmWaveHeader.getFocus();
			Object waveKeyObj = focus.getValue("WAVEKEY");
			if(waveKeyObj != null){
				return waveKeyObj.toString();
			}
		}
		return "";

	}

	private String flattenList(ArrayList list, String flattenList)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (i >= 1)
			{
				flattenList += ",";
			}
			flattenList += "'" + list.get(i) + "'";
		}
		return flattenList;
	}

}
