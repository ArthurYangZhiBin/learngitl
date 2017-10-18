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
package com.infor.scm.waveplanning.wp_waveworkbench.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.infor.scm.waveplanning.common.util.WPUserUtil;




public class WaveworkbenchPrerenderLink extends FormWidgetExtensionBase {
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws UserException, EpiDataException{
				
		Locale localeObj = WPUserUtil.getUserLocale(state);
		ResourceBundle messages = ResourceBundle.getBundle("com.ssaglobal.scm.waveplanning.resources.WavePlanning" , localeObj);
		String output = "";
		if(widget.getName().equals("orderFilter")) {
			output = messages.getString("homepage.orderfilterdesc");
		}else if (widget.getName().equals("waveManagement")) {
			output = messages.getString("homepage.wavemgmtdesc");
		}else if (widget.getName().equals("waveReport")) {
			output = messages.getString("homepage.reportsdesc");
		}else if (widget.getName().equals("monitorAlerts")) {
			output = messages.getString("homepage.mandadesc");
		}else if (widget.getName().equals("operations")) {
			output = messages.getString("homepage.operationsdesc");
		}else if (widget.getName().equals("configuration")) {
			output = messages.getString("homepage.configurationdesc");
		}		
		
		if (output != null) {
			widget.setLabel("label", output);
		}
		
		return RET_CONTINUE;
	}
}
