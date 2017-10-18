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
package com.ssaglobal.scm.wms.wm_facility;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.UserUtil;


public class LoginNavigationPicker extends ActionExtensionBase{
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	public static String DB_TYPE = "dbType";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoginNavigationPicker.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Executing LoginNavigationPicker",100L);		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();
		String uid = UserUtil.getUserId(state);
		try {
			setIntoUserContextAndSession(context,"ENTERPRISE");
		} catch (Exception e1) {		
			e1.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Leaving LoginNavigationPicker",100L);			
			return RET_CONTINUE;
		}
		BioCollection defaultCollection = null;
		try {
			Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' ", null);	
			_log.debug("LOG_SYSTEM_OUT","wsdefaults.USERID ="+ uid,100L);
			defaultCollection = uow.getBioCollectionBean(loadBiosQry);
			_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Got Default Collection:"+defaultCollection,100L);			
		} catch (RuntimeException e1) {			
			e1.printStackTrace();
		}
		try {
/*			if(defaultCollection == null || defaultCollection.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Default Record Not Found, Setting Navigation To Empty Menu...",100L);				
//				context.setNavigation("wmsapp");
				session.removeAttribute(DB_CONNECTION);
				session.removeAttribute(DB_USERID);
				session.removeAttribute(DB_PASSWORD);
				session.removeAttribute(DB_DATABASE);
				session.removeAttribute(DB_ISENTERPRISE);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
			}*/
			if(context.getAppUserBio("e_user") == null){
				_log.debug("LOG_SYSTEM_OUT","Inside the Propmt Navigation",100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","User is not fully registered, Setting Navigation To Registration...",100L);				
				context.setNavigation("prompt");
				session.removeAttribute(DB_CONNECTION);
				session.removeAttribute(DB_USERID);
				session.removeAttribute(DB_PASSWORD);
				session.removeAttribute(DB_DATABASE);
				session.removeAttribute(DB_ISENTERPRISE);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
			}

			if(defaultCollection == null || defaultCollection.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Default Record Not Found, Setting Navigation To Empty Menu...",100L);				
				context.setNavigation("wmsapp");
				session.removeAttribute(DB_CONNECTION);
				session.removeAttribute(DB_USERID);
				session.removeAttribute(DB_PASSWORD);
				session.removeAttribute(DB_DATABASE);
				session.removeAttribute(DB_ISENTERPRISE);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
			}
			
			Bio bio = defaultCollection.elementAt(0);
			if(bio.get("FACILITY") == null || bio.get("FACILITY").toString().trim().length() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Default Record Found But FACILITY is not present, Setting Navigation To Empty Menu...",100L);				
				context.setNavigation("wmsapp");
				session.removeAttribute(DB_CONNECTION);
				session.removeAttribute(DB_USERID);
				session.removeAttribute(DB_PASSWORD);
				session.removeAttribute(DB_DATABASE);
				session.removeAttribute(DB_ISENTERPRISE);				
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
				
			}
			String facility = bio.get("FACILITY").toString();
			_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Facility is:"+facility,100L);			
			if(facility.equalsIgnoreCase("null")){
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Default Record Found But FACILITY is not present, Setting Navigation To Empty Menu...",100L);				
				context.setNavigation("wmsapp");
				session.removeAttribute(DB_CONNECTION);
				session.removeAttribute(DB_USERID);
				session.removeAttribute(DB_PASSWORD);
				session.removeAttribute(DB_DATABASE);
				session.removeAttribute(DB_ISENTERPRISE);				
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
			}
			else if(facility.equalsIgnoreCase("enterprise")){				
				context.setNavigation("loginSuccessfulEvent7");		
			}
			else{
				context.setNavigation("loginSuccessfulEvent8");
				try {
					setIntoUserContextAndSession(context,facility);				
			           // fix bug #8505***********************
			           String CurrentConnection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			           String CurrentFacility = session.getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
			    	   String currentFacilityDEtails = CurrentConnection + "~~" + CurrentFacility;
			           // _log.debug("LOG_SYSTEM_OUT","**************Current connection = "+CurrentConnection,100L);
			           // _log.debug("LOG_SYSTEM_OUT","**************Current Facility = "+CurrentFacility,100L);
			           // _log.debug("LOG_SYSTEM_OUT","**************Current Facility Details = "+currentFacilityDEtails,100L);
			           HttpServletResponse res = state.getResponse();
			    	   _log.debug("LOG_SYSTEM_OUT","\n\n navigation picker cookie setting *****\n\n",100L);
			    	   _log.debug("LOG_SYSTEM_OUT","&&&& current facility details="+currentFacilityDEtails,100L);
			    	   Cookie myCookie = new Cookie ( "WM_FACILITYDETAILS" , currentFacilityDEtails);	    	   
			    	   myCookie.setPath("/");	    	  
			    	   _log.debug("LOG_SYSTEM_OUT","cookie domain:" + SSOManager.getCookieDomain(),100L);
			    	   myCookie.setDomain(SSOManager.getCookieDomain());			    	 
			    	   res.addCookie (myCookie);
			    	   //end of fix #fix bug #8505
				} catch (Exception e) {					
					e.printStackTrace();
					return RET_CONTINUE;
				}
			}	

		} catch (EpiDataException e) {			
			e.printStackTrace();
		}		
		_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","Leaving LoginNavigationPicker",100L);
		return RET_CONTINUE;
		
	}	
	
	static public void setIntoUserContextAndSession(ActionContext context, String facility) throws Exception{
		HttpSession session = context.getState().getRequest().getSession();
		String sQueryString = "(wm_pl_db.db_name ~= '"+facility+"')";
		_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","sQueryString = "+ sQueryString,100L);		
		Query DatasourceQuery = new Query("wm_pl_db",sQueryString,null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean DataSource = uow.getBioCollectionBean(DatasourceQuery);
		
			int size = DataSource.size();
			_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","size = "+ size,100L);			
			session.setAttribute(DB_CONNECTION, facility);
			for(int i=0; i < size; i++){
				String UserId = DataSource.elementAt(i).get("db_logid").toString();
				String Password = DataSource.elementAt(i).get("db_logpass").toString();
				String DatabaseName = DataSource.elementAt(i).get("db_database").toString();
				String IsEnterprise = DataSource.elementAt(i).get("db_enterprise").toString();
				String serverType = DataSource.elementAt(i).get("db_dbms").toString();
				session.setAttribute(DB_USERID,UserId );
				session.setAttribute(DB_PASSWORD,Password );
				session.setAttribute(DB_DATABASE,DatabaseName);
				session.setAttribute(DB_ISENTERPRISE,IsEnterprise);
				session.setAttribute(DB_TYPE, serverType);
				
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[DataSource is :" + facility,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[DatabaseName is :" + DatabaseName,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[UserID is :" + UserId,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[Password is :" + Password,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[IsEnterprise is :" + IsEnterprise,100L);
				_log.debug("LOG_DEBUG_EXTENSION_LOGINNAVPKER","[Type is :" + serverType,100L);
			}			
				
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		userContext.put(SetIntoHttpSessionAction.DB_CONNECTION, session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION));
		userContext.put(SetIntoHttpSessionAction.DB_USERID, session.getAttribute(SetIntoHttpSessionAction.DB_USERID));
		userContext.put(SetIntoHttpSessionAction.DB_PASSWORD, session.getAttribute(SetIntoHttpSessionAction.DB_PASSWORD));
		userContext.put(SetIntoHttpSessionAction.DB_DATABASE, session.getAttribute(SetIntoHttpSessionAction.DB_DATABASE));
		userContext.put(SetIntoHttpSessionAction.DB_ISENTERPRISE, session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE));
		userContext.put(SetIntoHttpSessionAction.DB_TYPE, session.getAttribute(SetIntoHttpSessionAction.DB_TYPE));
	}
	
}