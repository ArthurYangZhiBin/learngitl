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
package com.infor.scm.waveplanning.wp_graphical_filters.ui;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_graphical_filters.action.WPGraphicalFilterProceed;
import com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action.ConfirmWaveAddOrderNavSelect;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class WPGraphFilterInitialFormPrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphFilterInitialFormPrerender.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {
		RuntimeFormWidgetInterface baseCriterionWidget = form.getFormWidgetByName("BASECRITERION");
		RuntimeFormWidgetInterface graphTypeWidget = form.getFormWidgetByName("GRAPHTYPE");		
		baseCriterionWidget.setValue("H_STORERKEY");
		graphTypeWidget.setValue("pie");
		
		
		//If navigating from the Wave Mgmt screens, pull the WaveKey from the session and place it somewhere safe based on this screen's interaction id
		final HttpSession session = context.getState().getRequest().getSession();
		Integer waveKey = (Integer) session.getAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD);
		_log.debug("LOG_DEBUG_EXTENSION_QueryBuilderInitialFormPreRender_preRenderForm", "Putting " + waveKey + " Into the Session ",
				SuggestedCategory.NONE);
		session.removeAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD);
		if(waveKey != null)
		{
			WPUserUtil.setInteractionSessionAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, waveKey, context.getState());
		}
		
		return RET_CONTINUE;
	}
}