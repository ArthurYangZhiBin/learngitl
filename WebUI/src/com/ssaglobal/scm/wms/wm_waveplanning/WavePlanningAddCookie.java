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
package com.ssaglobal.scm.wms.wm_waveplanning;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class WavePlanningAddCookie extends com.epiphany.shr.ui.action.ActionExtensionBase {


	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WavePlanningAddCookie.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {


	       try {
	           EpnyControllerState state = (EpnyControllerState) context.getState();
	           HttpSession session = state.getRequest().getSession();
	           

	           // get CurrentFacility
	           String CurrentConnection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
	           String CurrentFacility = session.getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
	    	   String currentFacilityDEtails = CurrentConnection + "~~" + CurrentFacility;
	           // _log.debug("LOG_SYSTEM_OUT","**************Current connection = "+CurrentConnection,100L);
	           // _log.debug("LOG_SYSTEM_OUT","**************Current Facility = "+CurrentFacility,100L);
	           // _log.debug("LOG_SYSTEM_OUT","**************Current Facility Details = "+currentFacilityDEtails,100L);
	           HttpServletResponse res = state.getResponse();
	    	   _log.debug("LOG_SYSTEM_OUT","\n\nadding cookie...\n\n",100L);
	    	   _log.debug("LOG_SYSTEM_OUT","\n\n**************************************************************************\n\n",100L);
	    	   Cookie myCookie = new Cookie ( "WM_FACILITYDETAILS" , currentFacilityDEtails);	    	   
	    	   myCookie.setPath("/");	    	  
	    	   _log.debug("LOG_SYSTEM_OUT","cookie domain:" + SSOManager.getCookieDomain(),100L);
	    	   myCookie.setDomain(SSOManager.getCookieDomain());	    	   
	    	   res.addCookie (myCookie);
	    	   Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
	   			boolean isOwnerLocked = (ownerLockFlag == null || ownerLockFlag.intValue() == 0)?false:true;
	    	   if(isOwnerLocked){
	    		   ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
	    		   if(lockedOwners != null && lockedOwners.size() > 0){
	    			   String lockedOwnersList = "";
	    			   for(int i = 0; i < lockedOwners.size(); i++){
	    				   if(i > 0)
	    					   lockedOwnersList += ",";
	    				   lockedOwnersList += lockedOwners.get(i);
	    			   }
	    			   Cookie defaultOwnerCookie = new Cookie ( "WM_DEFAULTOWNER" , lockedOwnersList);
	    			   defaultOwnerCookie.setPath("/");
	    			   defaultOwnerCookie.setDomain(SSOManager.getCookieDomain());
	    			   res.addCookie (defaultOwnerCookie);
	    		   }
	    	   }
	    	   
	        } catch(Exception e) {
	            
	            // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	       } 
	       
	       return RET_CONTINUE;
	    }
}

