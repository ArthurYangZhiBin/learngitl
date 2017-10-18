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
import com.infor.scm.waveplanning.wp_wm_wave.wave.ActionInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveValidateInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.*;

/**
 * TODO Document WaveMaintProcessActionRunBackground class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveMaintProcessActionRunBackground extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintProcessActionRunBackground.class);

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

		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		String waveKey = "";
		String action = getParameterString("ACTION");
		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintProcessAction_execute", "No Waves Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_NO_WAVE_SELECTED", new Object[] {});
			}
			else if (((RuntimeListFormInterface) parentForm).getAllSelectedItems().size() > 1)
			{
				throw new UserException("WPEXP_MULTI_WAVE_SELECTED", new Object[] {});
			}
			else
			{//only one wave is selected
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction", "Called from List Form "
						+ parentForm.getName(), SuggestedCategory.NONE);
				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{
					final DataBean focus = (DataBean) it.next();
					if (focus.getValue("WAVEKEY") != null)
					{
						waveKey=focus.getValue("WAVEKEY").toString();
					}					
				}
				
			}
			
		}
		else
		{
			DataBean focus = parentForm.getFocus();
			waveKey = focus.getValue("WAVEKEY")==null?"":focus.getValue("WAVEKEY").toString();
		}
		
		
		
		WMWaveActionInterface actionInterface = WaveActionFactory.getWaveAction(action);
		ActionInputObj actionInput = new ActionInputObj();
		actionInput.setWaveKey(waveKey);
		actionInput.setAction(action);
		
		//do validation
		WaveValidateInputObj waveValObj = new WaveValidateInputObj();
		waveValObj.setWaveKey(waveKey);
		waveValObj.setContext(context);
		waveValObj.setAction(action);
		actionInterface.validate(waveValObj); //if not passed, it will throw UserException

		WaveInputObj wpInput = new WaveInputObj();
		wpInput.setWaveKey(waveKey);
		RunningInBackgroundWrapper.execute(wpInput, action);
		return RET_CONTINUE;
	}
}
	
	
	
