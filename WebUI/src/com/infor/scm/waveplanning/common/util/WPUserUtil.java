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
package com.infor.scm.waveplanning.common.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sso.exception.InvalidData;
import com.epiphany.shr.sso.exception.NoServerAvailableException;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.User;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;



public class WPUserUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPUserUtil.class);
	public WPUserUtil()
    {
    }
	
	public static String getUserId(){
		String userId = (String)EpnyServiceManagerServer.getInstance().getUserContext().get("logInUserId");
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Got User Id "+userId+" From get(logInUserId)...",100L);				
		if(userId == null || userId.length() == 0){
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","get(logInUserId) returned empty...",100L);			
			String userAttr = EpnyServiceManagerServer.getInstance().getUserContext().getUserName();
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Got "+userAttr+" From getUserName()",100L);			
			String[] userAttrAry = userAttr.split(",");
			if(userAttrAry == null || userAttrAry.length == 0){			
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);				
				return null;
			}
			for(int i = 0; i < userAttrAry.length; i++){
				String[] keyValueAry = userAttrAry[i].split("=");
				if(keyValueAry.length != 2){
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);
					return null;
				}
				if(keyValueAry[0].equalsIgnoreCase("uid")){
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Found User Id:"+keyValueAry[1]+"...",100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);					
					return keyValueAry[1];
				}
			}
			
		}	
		else{
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Found User Id:"+userId+"...",100L);
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);				
			return userId;
		}
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);		
		return null;
	}
	
	public static String getUserId(StateInterface state){
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","In getUserId()",100L);		
		if(state == null){
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","State Is Null...",100L);
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);			
			return null;
		}
		String userId = (String)state.getServiceManager().getUserContext().get("logInUserId");
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Got User Id "+userId+" From get(logInUserId)...",100L);				
		if(userId == null || userId.length() == 0){
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","get(logInUserId) returned empty...",100L);			
			String userAttr = state.getServiceManager().getUserContext().getUserName();
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Got "+userAttr+" From getUserName()",100L);			
			String[] userAttrAry = userAttr.split(",");
			if(userAttrAry == null || userAttrAry.length == 0){			
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);				
				return null;
			}
			for(int i = 0; i < userAttrAry.length; i++){
				String[] keyValueAry = userAttrAry[i].split("=");
				if(keyValueAry.length != 2){
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);
					return null;
				}
				if(keyValueAry[0].equalsIgnoreCase("uid")){
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Found User Id:"+keyValueAry[1]+"...",100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);					
					return keyValueAry[1];
				}
			}
			
		}	
		else{
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Found User Id:"+userId+"...",100L);
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);				
			return userId;
		}
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Could Not Find User ID...",100L);
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Exiting getUserId()",100L);		
		return null;
	}
	
	public static String getCurrentUserEmail(StateInterface state) throws DataBeanException{
		return getUser(state).getEmail();		
	}
	static public User getUser(StateInterface state){ 
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		return umsInterface.getUser(getUserId(state), state);
	}
	static public User getUser(){ 
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		return umsInterface.getUser(getUserId());
	}
	static public User getUser(String userID){ 
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		return umsInterface.getUser(userID);
	}
	static public User getUser(StateInterface state,String userID){ 
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		return umsInterface.getUser(userID, state);
	}
	static public Locale getUserLocale(StateInterface state)
	{
		User user = getUser(state);
		System.out.println("\n\nUser:"+user+"\n\n");
		String locale = user.getLocale();
		Locale localeObject = WPUserUtil.constructLocaleObject(locale);
		return localeObject;
	}
	static public Locale getUserLocale()
	{
		String locale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		System.out.println("\n\nGot Locale:"+locale+"\n\n");
		Locale localeObject = WPUserUtil.constructLocaleObject(locale);
		return localeObject;
	}
	static public Locale constructLocaleObject(String locale)
	{
		String[] localeSplits = locale.split("_");
		Locale localeObject = null;
		if (localeSplits.length == 1)
		{
			localeObject = new Locale(locale);
		}
		else if (localeSplits.length == 2)
		{
			localeObject = new Locale(localeSplits[0], localeSplits[1]);
		}
		else if (localeSplits.length == 3)
		{
			localeObject = new Locale(localeSplits[0], localeSplits[1], localeSplits[2]);
		}
		return localeObject;
	}
	
	static public void setInteractionSessionAttribute(Object key, Object value, StateInterface state){
		HttpSession session = state.getRequest().getSession();
		HashMap interactionSession = (HashMap)session.getAttribute(state.getInteractionId());
		if(interactionSession == null){
			interactionSession = new HashMap();
		}
		interactionSession.put(key, value);
		session.setAttribute(state.getInteractionId(), interactionSession);
	}
	
	static public Object getInteractionSessionAttribute(Object key, StateInterface state){
		HttpSession session = state.getRequest().getSession();
		HashMap interactionSession = (HashMap)session.getAttribute(state.getInteractionId());
		if(interactionSession == null){
			return null;
		}
		return interactionSession.get(key);
	}
	
	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
	
//	public static ArrayList<String> getUsersSubscribedToEvent(StateInterface state, String event){
//		//TODO Rewrite once we figure out how we are going to allow users to subscribe to alerts
//		return new NTLMUsers().getAllUserIds(state);
//	}
	
	public static ArrayList<String> getUsersSubscribedToEvent(String event){
		//TODO Rewrite once we figure out how we are going to allow users to subscribe to alerts
		WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
		try {
			return umsInterface.getAllUserIds();
		} catch (NoServerAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidData e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}