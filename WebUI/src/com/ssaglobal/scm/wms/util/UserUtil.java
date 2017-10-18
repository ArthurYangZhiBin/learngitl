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
package com.ssaglobal.scm.wms.util;

import java.util.Locale;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class UserUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserUtil.class);
	public UserUtil()
    {
    }
	
	/**
	 * Traverses the form tree using the topLevelFormName as the root looking for the targetForm. Returns the targetForm if it can find it
	 * or null if it cannot. If the tree contains forms within a form slot that have more than one tab identifier, then the function will
	 * only traverse the form associated with the default tab identifier. 
	 * @param startingForm - the current runtime form
	 * @param topLevelFormName - the name of the form to be used as the root of the form tree
	 * @param targetFormName - the name of the form you are searching for
	 * @param state
	 * @return - the target form if it exists and is locatable without going though non-default tab identifiers, null otherwise.
	 */
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
	
public static String getUserId(){
		
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","In getUserId()",100L);		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String userId = (String)userContext.get("logInUserId");		
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","Got User Id "+userId+" From get(logInUserId)...",100L);				
		if(userId == null || userId.length() == 0){
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL","get(logInUserId) returned empty...",100L);			
			String userAttr = userContext.getUserName();
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

	public static Locale getLocale(StateInterface state)
	{
		return state.getUser().getLocale().getJavaLocale();
	}
		
}