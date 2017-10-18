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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
 * TODO Document WaveMaintRefresh class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveMaintRefresh extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintProcessAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{

		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
/*		String waveKey = "";
			DataBean focus = parentForm.getFocus();
			if (focus.getValue("WAVEKEY") != null)
			{
				waveKey=focus.getValue("WAVEKEY").toString();
			}
*/				
			String slotName = parentForm.getParentForm(state).getName();
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("clickEvent1863");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("clickEvent1614");
				}
				
/*				Query qry = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY='"+waveKey+"'", null);
		 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);			
				uowb.clearState();
				try{
					result.setFocus(resultCollection.get("0")); 
				}catch(EpiDataException e){
					e.printStackTrace();
				}
				*/				
		return RET_CONTINUE;
	}
}
