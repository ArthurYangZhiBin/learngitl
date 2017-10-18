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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;
//import com.ssaglobal.scm.waveplanning.wavemgmt.bd.WaveBD;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.WaveVO;
import com.ssaglobal.scm.wms.wm_ums.User;
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

public class WaveMaintMassUpdate extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintMassUpdate.class);

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
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintMassUpdate_execute", "Entering Mass Update", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = toolbarForm.getParentForm(state);
		DataBean focus = toolbarForm.getFocus();
		String waveKey = "";
		String massUpdateAction = null;
		waveKey = BioAttributeUtil.getString(focus, "WAVEKEY");
		if (context.getSourceWidget().getName().equals("UPDATE ALL"))
		{
			massUpdateAction = WavePlanningConstants.MASS_UPDATE_ALL;
		}
		else
		{
			massUpdateAction = WavePlanningConstants.MASS_UPDATE_BLANKS;
		}

		String door = parentForm.getFormWidgetByName("DOOR").getDisplayValue();
		String route = parentForm.getFormWidgetByName("ROUTE").getDisplayValue();
		String stage = parentForm.getFormWidgetByName("STAGE").getDisplayValue();
		String stop = parentForm.getFormWidgetByName("STOP").getDisplayValue();
		String carrierCode = parentForm.getFormWidgetByName("CARRIER").getDisplayValue();
		if(route != null && !"null".equalsIgnoreCase(route)
				&& !" ".equalsIgnoreCase(route)){
			route = route.toUpperCase().trim();
		}
		if(door != null && !"null".equalsIgnoreCase(door) 
				&& !" ".equalsIgnoreCase(door)){
			door = door.toUpperCase().trim();
		}
		try
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintMassUpdate_execute", "Going to update Wave " + waveKey
					+ " with values " + door + " " + route + " " + stage + " " + stop + " " + carrierCode
					+ " using method " + massUpdateAction, SuggestedCategory.NONE);
			WMWaveActionInterface actionInterface = WaveActionFactory.getWaveAction(WPConstants.MASS_UPDATE_ALL);
			WaveInputObj wpInput = new WaveInputObj();
			wpInput.setWaveKey(waveKey);
			wpInput.setMassUpdateAction(massUpdateAction);
			wpInput.setDoor(door);
			wpInput.setRoute(route);
			wpInput.setStage(stage);
			wpInput.setStop(stop);
			wpInput.setCarrierCode(carrierCode);
			actionInterface.doWaveAction(wpInput);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[] {});
		}

		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintMassUpdate_execute", "Leaving Mass Update", SuggestedCategory.NONE);

		return RET_CONTINUE;
	}

}
