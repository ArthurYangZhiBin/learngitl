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


package com.ssaglobal.scm.wms.wm_facilityviewer;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.epiphany.shr.ui.state.EpnyInProcStateImpl;
import com.epiphany.shr.ui.state.StateInterface;


public class FacilityViewerPreRender extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {

	/** The Constant PARAM_KEY_SESSION. */
	private static final String PARAM_KEY_SESSION = "&_id=";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FacilityViewerPreRender.class);			
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) {
		HttpSession session = state.getRequest().getSession();
		String connName = (String)session.getAttribute("dbConnectionName");
		connName = connName == null?"":connName;
		String userLocale = state.getUser().getLocale().getJavaLocale().getLanguage();
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing FacilityViewerPreRender",100L);			
		String url = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("facilityviewerURL");
		url = url+ "?locale="+ userLocale + "&facility="+ connName;
		//Adding session id
		String id = state.getRequest().getSession().getId();
		if (state instanceof EpnyInProcStateImpl) {
			id = ((EpnyInProcStateImpl) state).getSessionId();
		}
		url = url + PARAM_KEY_SESSION + id;
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","got Facility Viewer URL from config file:"+url,100L);			
		widget.setProperty(RuntimeFormWidgetInterface.PROP_SRC,url);
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","HomepagePrerenderAction Exiting",100L);		
		return RET_CONTINUE;

	}	
}
