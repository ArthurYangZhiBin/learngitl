/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

/**
 * TODO Document WaveMaintShipWaveAction class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_ums.User;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;

import com.agileitp.forte.framework.TextData;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.epiphany.shr.ui.action.ModalActionContext;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintShipWaveAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintShipWaveAction.class);

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
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		System.out.println("*****good sign**********");
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		String waveKey = "";
//		String action = getParameterString("ACTION");
		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintShipWaveAction_execute", "No Waves Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_NO_WAVE_SELECTED", new Object[] {});
			}
			else if (((RuntimeListFormInterface) parentForm).getAllSelectedItems().size() > 1)
			{
				throw new UserException("WPEXP_MULTI_WAVE_SELECTED", new Object[] {});
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintShipWaveAction", "Called from List Form "
						+ parentForm.getName(), SuggestedCategory.NONE);
				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{
					final DataBean focus = (DataBean) it.next();
					if (focus.getValue("WAVEKEY") != null)
					{
						waveKey=focus.getValue("WAVEKEY").toString();
						ActionInputObj actionInput = new ActionInputObj();
						actionInput.setWaveKey(waveKey);
//						actionInput.setAction(action);
						int status = processWave(actionInput, context);
						if(status == RET_CANCEL){
							return RET_CANCEL;
						}
					}					
				}
				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintShipWaveAction", "Called from Normal Form "
					+ parentForm.getName(), SuggestedCategory.NONE);
			DataBean focus = parentForm.getFocus();
			
			if (focus.getValue("WAVEKEY") != null)
			{
				waveKey=focus.getValue("WAVEKEY").toString();
				ActionInputObj actionInput = new ActionInputObj();
				actionInput.setWaveKey(waveKey);
//				actionInput.setAction(action);
				int status =  processWave(actionInput, context);
				if(status == RET_CANCEL){
					return RET_CANCEL;
				}
			}
		}
		context.setNavigation("menuClickEvent601");
		return RET_CONTINUE;
	}
	
	
	
	public int processWave(ActionInputObj actionInput, ActionContext context) throws UserException{
		String waveKey = actionInput.getWaveKey();
		String action = actionInput.getAction();
		
		WMWaveActionInterface actionInterface = WaveActionFactory.getWaveAction(action);
		WaveInputObj wpInput = new WaveInputObj();
		wpInput.setWaveKey(actionInput.getWaveKey());
		wpInput.getProcedureParametes().add(new TextData(waveKey));
		String actionName = context.getActionObject().getName();
		if("CANCELUNSORTED".equalsIgnoreCase(actionName) || "CANCELTASKS".equalsIgnoreCase(actionName)){
			return RET_CANCEL;
		}
		wpInput.getProcedureParametes().add(new TextData("YES"));
		wpInput.getProcedureParametes().add(new TextData("YES"));			
		try{
			EXEDataObject edo = actionInterface.doWaveAction(wpInput);
			return RET_CONTINUE;
		}catch(WebuiException e){
			e.printStackTrace();
			String [] ex = new String[1];
			ex[0]=e.getMessage();
			throw new UserException("WPEXE_APP_ERROR", ex);			
			
		}
	}
	
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			StateInterface state = ctx.getState();

			RuntimeFormInterface listForm = ctx.getModalBodyForm(0);
			String facility = WPUtil.getFacility(state.getRequest());
			User user = null;


		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
