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


package com.ssaglobal.scm.wms.wm_homepage;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.state.EpnyInProcStateImpl;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;



public class HomepagePrerenderAction extends
		com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(HomepagePrerenderAction.class);			
	public static final String HOME_PAGE_COOKIE_VAL_DELIMITER = "&H__P__C__V__D&";
	
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) {
		
		try {
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing HomepagePrerenderAction",100L);			
			HttpSession session = state.getRequest().getSession();
//			ServletContext servletCtx = session.getServletContext().getContext("/DashBoard");			
//			HashMap params = new HashMap();
			
			//8743.b
			//In websphere the cookie is not getting set, therefore homepage does not show up.
			//To fix this we are populating the servlet context as well as setting the information in the cookie.
			//While in the dashboard servelet we will check if the cookie is set if it is then the cookie will be 
			//used otherwise servlet context will be used.

			StringTokenizer strtok = new StringTokenizer(state.getRequest().getContextPath(),"/");
			String homepageCtx = "/" + (strtok.nextToken()).concat("/DashBoard");
			ServletContext servletCtx = session.getServletContext().getContext(homepageCtx);	
			HashMap params = new HashMap();
			//8743.e
			
			
			
			String ctxKey = GUIDFactory.getGUIDStatic();
			String url = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("homePageURL");	
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","got homepage from config file:"+url,100L);			
			String uid = UserUtil.getUserId(state);
			// BugAware 8646: Homepage support for multiple languages
			// String userLocale = state.getUser().getLocale().getLocaleIDString();
			String userLocale = state.getUser().getLocale().getJavaLocale().getLanguage();
			if(userLocale == null)
				userLocale = "";
			if(userLocale.indexOf("pt") != -1){
				userLocale = "pt";
			}
			String dbName = (String)session.getAttribute("dbDatabase");
			dbName = dbName == null?"":dbName;
			String schemaName = (String)session.getAttribute("dbUserName");
			schemaName = schemaName == null?"":schemaName;
			String connName = (String)session.getAttribute("dbConnectionName");
			connName = connName == null?"":connName;
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","User Id:"+uid,100L);			
			String args[] = new String[0]; 
			String home = getTextMessage("HomeInteractionName",args,state.getLocale());
			state.setInteractionName(home);			
			String[] rolesTemp = state.getServiceManager().getAllRolesForCurrentUser();
			ArrayList roles = new ArrayList(rolesTemp.length);
			for(int i = 0; i < rolesTemp.length; i++){
				roles.add(rolesTemp[i]);
			}
			//8743.b
			params.put("u",uid);
			params.put("dbName",dbName);
			params.put("dbSchema",schemaName);
			params.put("dbConnection",connName);
						
			if(SecurityUtil.isAdmin(state) || roles.contains("Home Page Admin")){				
				params.put("isadmin","true");
			}
			else{				
				params.put("isadmin","false");
			}
			params.put("locale",userLocale);
			url += "?a="+ctxKey;
			servletCtx.setAttribute(ctxKey,params);	
			//8743.e
						
			//Create Cookie Value
			String cookieValue = "";
			cookieValue += uid;
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			cookieValue += dbName;
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			cookieValue += schemaName;
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			cookieValue += connName;
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			if(SecurityUtil.isAdmin(state) || roles.contains("Home Page Admin")){				
				cookieValue += "true";				
			}
			else{
				cookieValue += "false";					
			}
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			cookieValue += userLocale;
			cookieValue += HOME_PAGE_COOKIE_VAL_DELIMITER;
			cookieValue += ctxKey;
			
			//Create Cookie
			HttpServletResponse res = state.getResponse();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Creating Cookie...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Cookie Value:"+cookieValue,100L);
			 Cookie myCookie = new Cookie ( "WM_HOMEPAGE" , cookieValue);	 
			 
			 //Place Cookie
			 myCookie.setPath("/");	    	  
	    	 _log.debug("LOG_SYSTEM_OUT","cookie domain:" + SSOManager.getCookieDomain(),100L);
	    	 myCookie.setDomain(SSOManager.getCookieDomain());	    	   
	    	 res.addCookie (myCookie);
	    	 
	    	 //Timezone Cookie
	    	 TimeZone timeZone = ReportUtil.getTimeZone(state);
	    	 Cookie tzCookie = new Cookie("WM_TMZ", timeZone.getID());
	    	 tzCookie.setPath("/");
	    	 _log.debug("LOG_SYSTEM_OUT","cookie domain:" + SSOManager.getCookieDomain(),100L);
	    	 tzCookie.setDomain(SSOManager.getCookieDomain());
	    	 res.addCookie(tzCookie);
	    	 
	    	 
			// Session ID Cookie
			String id = state.getRequest().getSession().getId();
			if (state instanceof EpnyInProcStateImpl) {
				id = ((EpnyInProcStateImpl) state).getSessionId();
			}
			Cookie idCookie = new Cookie("WM_ID", id);
			idCookie.setPath("/");
			idCookie.setDomain(SSOManager.getCookieDomain());
			res.addCookie(idCookie);
	    	 
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","URL Final:"+url,100L);			
			widget.setProperty(RuntimeFormWidgetInterface.PROP_SRC,url);

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;

		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","HomepagePrerenderAction Exiting",100L);		
		return RET_CONTINUE;

	}	
}
