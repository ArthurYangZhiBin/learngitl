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
package com.ssaglobal.scm.wms.wm_ums;

import java.util.ArrayList;
import java.util.HashMap;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_indirect_activity.ui.Name;

public class ADSDirectoryServerUsers extends Users {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ADSDirectoryServerUsers.class);
	@Override
	public ArrayList<String> getAllUserIds(){//for task manager
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
 		UnitOfWork uow = null;
 		try { 
 			
		    serviceFactory = BioServiceFactory.getInstance();
 		    bioService = serviceFactory.create("webui");
 		    uow = bioService.getUnitOfWork();
 		    Query qry = new Query("e_user", null, null);
 		    BioCollection userBios = uow.findByQuery(qry);
 		    int size = userBios.size();
 		    ArrayList <String> userIdList = new ArrayList<String>();
 		    Bio userBio;
 		    String userid;
 		    String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
 		    for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	userid = (String)userBio.get("sso_name");
// 		    	_log.debug("LOG_SYSTEM_OUT","getalluserids "+userid,100L);
 		    	
  		    		//get user id without domain name and add into userIdList
 		    		String userName = UMSUtil.getUserIdForAds(userid, userBaseDn);
 		    		if(!"".equalsIgnoreCase(userName)){
 		    			userIdList.add(userName);
 		    		}
 		    }
 		   if(bioService != null)
 			{
 				bioService.remove();
 			}
 		    return userIdList;
 		} catch (Exception e) { 
 			if(bioService != null){
 				bioService.remove();
 			}
 			e.printStackTrace();			
 		}
		return null;
	}

	@Override
	public HashMap<String, String> getTaskManagerUsers()
	{
		HashMap<String, String> allUsers = new HashMap<String, String>();

		BioServiceFactory serviceFactory;
		BioService bioService = null;
		UnitOfWork uow = null;
		try
		{
			serviceFactory = BioServiceFactory.getInstance();
			bioService = serviceFactory.create("webui");
			uow = bioService.getUnitOfWork();
			//jp.answerlink.148358.begin
 		    //Select just Active users
			//Query qry = new Query("e_user", null, null);
			Query qry = new Query("e_user", 
 		    		"e_user.active_status_lkp = @LABEL_TO_VALUE['e_user', 'active_status_lkp', 'Active']", null);
 		    //jp.answerlink.148358.end
			BioCollection userBios = uow.findByQuery(qry);
			int size = userBios.size();

			Bio userBio;
			String userId;
			String userName;
			for (int i = 0; i < size; i++)
			{
				userBio = userBios.elementAt(i);
				userId = (String) userBio.get("sso_name");
				userName = (String) userBio.getBio("user_data").get("full_name");
				allUsers.put(userId, userName);

			}
			if (bioService != null)
			{
				bioService.remove();
			}
			return allUsers;
		} catch (Exception e)
		{
			if (bioService != null)
			{
				bioService.remove();
			}
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/*
	 * input: String firstName which is what User entered in the filter field
	 *        String lastName which is what User entered in the filter field
	 * return: ArrayList of userIds
	 * Exception: EpiException
	 */
	
	public ArrayList<String> getAllUserIds(String firstName, String lastName) throws EpiException {//for indirectory activity
		BioServiceFactory serviceFactory;
 		BioService bioService;
 		UnitOfWork uow = null;
		boolean search = false;
		String qry = null;
		serviceFactory = BioServiceFactory.getInstance();
 		bioService = serviceFactory.create("webui");
 		uow = bioService.getUnitOfWork();
 		    
	    String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
		if (firstName != null && !"".equalsIgnoreCase(firstName)) 
		{
			//filter on both
			if (containsWildCards(firstName))
			{
				firstName = stripWildCards(firstName);
				qry = "user_data.full_name ~=%'"+firstName+"'%";
			}
			else
			{
				qry = "user_data.full_name ~=%'"+firstName+"'%";
			}
			search = true;
		}
		if(lastName != null && !"".equalsIgnoreCase(lastName)){
			if(search){
				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					qry =qry+  " and user_data.full_name ~=%'"+lastName+"'%";
				}
				else
				{
					qry = qry + " and user_data.full_name ~=%'"+lastName+"'%";
				}
			}else{
				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					qry = "user_data.full_name ~=%'"+lastName+"'%";
				}
				else
				{
					qry = "user_data.full_name ~=%'"+lastName+"'%";
				}
			}
			search = true;
		}
		if(search){
			Query qryObj = new Query("user_data", qry, null);
 		    BioCollection userBios = uow.findByQuery(qryObj);
 		    int size = userBios.size();
 		    ArrayList <String> userIdList = new ArrayList<String>();
 		    Bio userBio, euserBio;
 		    String userid;
 		    for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	euserBio = userBio.getBio("e_user");
	    		String userName = UMSUtil.getUserIdForAds((String)euserBio.get("sso_name"), userBaseDn);
		    		if(!"".equalsIgnoreCase(userName)){
		    			userIdList.add(userName);
		    		}
 		    }
 			if(bioService != null)
 			{
 				bioService.remove();
 			}
 		    return userIdList;
		}
		if(bioService != null)
		{
			bioService.remove();
		}
		return null;
	}
	
	
	
	
	
	
	@Override
	public User getUser(String userId){
		// April 15, 2009 Anindhya 
		// We will use the userid itself to return back.
		// ads.20090415.b
		User user = new User();
		user.setFirstName(userId);
		user.setFullName(userId);
		return user;
//		BioServiceFactory serviceFactory;
// 		BioService bioService = null;
// 		UnitOfWork uow = null;
// 		User user = null;
// 		try {   
// 		    String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
// 		    String userIdAttributeName = SSOConfigSingleton.getSSOConfigSingleton().getUserIdAttributeName();
// 		    serviceFactory = BioServiceFactory.getInstance();
// 		    bioService = serviceFactory.create("webui");
// 		    uow = bioService.getUnitOfWork();
//		    String dnUserId = userIdAttributeName+"="+userId+","+userBaseDn;
// 		    Query qry = new Query("e_user", "e_user.sso_name in ('"+dnUserId+"')", null);
// 		    BioCollection userBios = uow.findByQuery(qry);
// 		    int size = userBios.size();
// 		    if(size != 0){		    	
//	 		    Bio userBio = userBios.elementAt(0);
//	 		    String ssoName = (String)userBio.get("sso_name");
//	 		    String ssoId = UMSUtil.getADSUserId(ssoName);
//	 		    Bio userDataBio = userBio.getBio("user_data");
//	 		    String fullName = (String)userDataBio.get("full_name");
//	 		    String firstName = UMSUtil.getFirstName(fullName);
//	 		    String lastName = UMSUtil.getLastName(fullName);
//	 		   String locale = (String)userBio.get("locale_id");
//	 		    String email = (String)userDataBio.get("email_address");
//	 		    user = new User();
//	 		    user.setFirstName(firstName);
//	 		    user.setLastName(lastName);
//	 		    user.setFullName(fullName);
//	 		    user.setUid(ssoId);
//	 		   user.setLocale(locale);
//	 		    user.setEmail(email);
//	 			if(bioService != null)
//	 			{
//	 				bioService.remove();
//	 			}
//	 		    return user;
// 		    }
// 		   if(bioService != null)
// 			{
// 				bioService.remove();
// 			}
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			if(bioService != null)
// 			{
// 				bioService.remove();
// 			}
// 			
// 		}
//		
// 		if(bioService != null)
//		{
//			bioService.remove();
//		}
//		return null;
		// ads.20090415.e		
	}
	@Override
	public HashMap getUsers(ActionContext context, ActionResult result) throws EpiException{//for unassigned work
		HashMap usersHashmap = new HashMap();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		boolean isNewHashmap = false;
		if (userContext.containsKey(WMUMSConstants.ALL_USERS))
		{
			//load hashmap from context
			usersHashmap = (HashMap) userContext.get(WMUMSConstants.ALL_USERS);
		}
		else
		{
			//create hashmap
			isNewHashmap = true;
		}
 		
		BioServiceFactory serviceFactory;
 		BioService bioService;
 		UnitOfWork uow = null;
 		UserResults user = null;
 		Bio userBio = null;
 		
 		
		    serviceFactory = BioServiceFactory.getInstance();
 		    bioService = serviceFactory.create("webui");
 		    uow = bioService.getUnitOfWork();
 		    Query qry = new Query("e_user", null, null);
 		    BioCollection userBios = uow.findByQuery(qry);
 		    int size = userBios.size();
 		    for(int i=0; i< size; i++){		    	
 		    	userBio = userBios.elementAt(i);
	 		    String ssoName = (String)userBio.get("sso_name");
	 		    if(ssoName.contains("cn=")){
	 		    	String ssoName_tmp = ssoName.replaceFirst("cn=","");
	 		    	if(ssoName_tmp.contains("cn=")){
			 		    String ssoId = UMSUtil.getADSUserId(ssoName);
			 		    Bio userDataBio = userBio.getBio("user_data");
			 		    String fullName = (String)userDataBio.get("full_name");
			 		    String firstName = UMSUtil.getFirstName(fullName);
			 		    String lastName = UMSUtil.getLastName(fullName);
		 		    	user = new UserResults(ssoId, firstName, lastName);
				    	if(isNewHashmap){
		 		    		usersHashmap.put(ssoId, user);
		 		    	}else{
		 		    		if(usersHashmap.get(ssoId) == null){
		 		    			usersHashmap.put(ssoId, user);
		 		    		}//else do nothing
		 		    	}
	 		    	}
	 		    }
		    	
	 		    
 		    }		
 			userContext.put(WMUMSConstants.ALL_USERS, usersHashmap);
 			if(bioService != null)
 			{
 				bioService.remove();
 			}
 	 		return usersHashmap;
	}
	
	
	
	@Override
	public HashMap getUsers(ModalActionContext context, ActionResult result) throws EpiException{//for unassigned work
		HashMap usersHashmap = new HashMap();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		boolean isNewHashmap = false;
		if (userContext.containsKey(WMUMSConstants.ALL_USERS))
		{
			//load hashmap from context
			usersHashmap = (HashMap) userContext.get(WMUMSConstants.ALL_USERS);
		}
		else
		{
			//create hashmap
			isNewHashmap = true;
		}
 		
		BioServiceFactory serviceFactory;
 		BioService bioService;
 		UnitOfWork uow = null;
 		UserResults user = null;
 		Bio userBio = null;
 		
 		
		    serviceFactory = BioServiceFactory.getInstance();
 		    bioService = serviceFactory.create("webui");
 		    uow = bioService.getUnitOfWork();
 		    Query qry = new Query("e_user", null, null);
 		    BioCollection userBios = uow.findByQuery(qry);
 		    int size = userBios.size();
 		    for(int i=0; i< size; i++){		    	
 		    	userBio = userBios.elementAt(i);
	 		    String ssoName = (String)userBio.get("sso_name");
	 		    
	 		    String ssoId = UMSUtil.getUserId(ssoName);
	 		    Bio userDataBio = userBio.getBio("user_data");
	 		    String fullName = (String)userDataBio.get("full_name");
	 		    String firstName = UMSUtil.getFirstName(fullName);
	 		    String lastName = UMSUtil.getLastName(fullName);
 		    	user = new UserResults(ssoId, firstName, lastName);
		    	if(isNewHashmap){
 		    		usersHashmap.put(ssoId, user);
 		    	}else{
 		    		if(usersHashmap.get(ssoId) == null){
 		    			usersHashmap.put(ssoId, user);
 		    		}//else do nothing
 		    	}
		    	
	 		    
 		    }		
 			userContext.put(WMUMSConstants.ALL_USERS, usersHashmap);
 			if(bioService != null)
 			{
 				bioService.remove();
 			}
 	 		return usersHashmap;
	}
	
	
	/*
	 *  input: ActionContext, ActionResult, EXEDataObject
	 *  output: HashMap with Name objects
	 *  Exception:EpiException, program still adds usersHashmap into userContext
	 * @see com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface#getUsers(com.epiphany.shr.ui.action.ActionContext, com.epiphany.shr.ui.action.ActionResult, com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject)
	 */
	@Override
	public HashMap getUsers(ActionContext context, ActionResult result, EXEDataObject collection){//for assign work
		HashMap usersHashmap;
		Object tempObj;
			EpnyUserContext userContext = context.getServiceManager().getUserContext();
			if (userContext.containsKey(WMUMSConstants.ALL_USERS_FOR_ASSIGN))
			{
				//load hash from context
				usersHashmap = (HashMap) userContext.get(WMUMSConstants.ALL_USERS_FOR_ASSIGN);
			}
			else
			{
				//create hash
				usersHashmap = new HashMap();
			}
			try
			{
	 		    String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
	 		    String userIdAttributeName = SSOConfigSingleton.getSSOConfigSingleton().getUserIdAttributeName();
				BioServiceFactory serviceFactory;
		 		BioService bioService = null;
		 		UnitOfWork uow = null;
		 		String fullName;
				String firstName;
				String lastName;
				String ssoUserId;
				StringBuffer qry = new StringBuffer();
				boolean search = false;
//	 		    String domainName = SSOConfigSingleton.getSSOConfigSingleton().getDomainName();
				for (int i = 0; i < collection.getRowCount(); i++)
				{
					tempObj = collection.getAttribValue(new TextData("userid"));
					String uid = tempObj != null ? tempObj.toString() : null;
					if (!usersHashmap.containsKey(uid))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t Adding UID to search " + uid + "\n", SuggestedCategory.NONE);
						search = true;
//						qry.append("'"+domainName+"\\"+uid+"',");
					    String dnUserId = userIdAttributeName+"="+uid+","+userBaseDn;
						qry.append("'"+dnUserId+"',");
						usersHashmap.put(uid, new Name("", ""));
					}
					collection.getNextRow();
				}
				if(search){
					int length = qry.length();
					qry.delete(length-1, length);
					
					String oaQry = null;
					oaQry = "e_user.sso_name in ("+qry.toString()+")";
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t OA query=" + oaQry + "\n", SuggestedCategory.NONE);
		 		    Query qryObj = new Query("e_user", oaQry, null);
					
				    serviceFactory = BioServiceFactory.getInstance();
		 		    bioService = serviceFactory.create("webui");
		 		    uow = bioService.getUnitOfWork();
		 		    BioCollection userBios = uow.findByQuery(qryObj);
		 		    int size = userBios.size();
		 		    for(int i=0; i<size; i++){
			 		    Bio userBio = userBios.elementAt(i);
			 		    String ssoName = (String)userBio.get("sso_name");
			 		    
			 		    String ssoId = UMSUtil.getUserIdForAds(ssoName, userBaseDn);
			 		    if(!"".equalsIgnoreCase(ssoId)){
				 		    Bio userDataBio = userBio.getBio("user_data");
				 		    fullName = (String)userDataBio.get("full_name");
				 		    firstName = UMSUtil.getFirstName(fullName);
				 		    lastName = UMSUtil.getLastName(fullName);
							_log.debug("LOG_DEBUG_EXTENSION", "\n\t adding name:" + fullName  + "into Hashmap\n", SuggestedCategory.NONE);
				 		   usersHashmap.put(ssoId, new Name(firstName, lastName));
			 		    }
		 		    }
		 		   userContext.put(WMUMSConstants.ALL_USERS_FOR_ASSIGN, usersHashmap);
				}
				if(bioService != null)
				{
					bioService.remove();
				}
				return 	usersHashmap;
			} catch (EpiException e)
			{
				
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t adding name nto Hashmap for assign failed:" + e.getMessage() + "\n", SuggestedCategory.NONE);
				userContext.put(WMUMSConstants.ALL_USERS_FOR_ASSIGN, usersHashmap);
			}
		return usersHashmap;
	}
	@Override
	public User getUser(String userId, StateInterface state){
		
		//ADS is tricky, can't rely on building a query 
		User user = new User();
		user.setFirstName(userId);
		user.setLastName(userId);
		user.setUid(userId);
		user.setLocale(state.getUser().getLocale().getLocaleIDString());
		return user;

		// 		UnitOfWorkBean uow = null;
		// 		User user = null;
		// 		try {   
		// 		    
		// 		    String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
		// 		    String userIdAttributeName = SSOConfigSingleton.getSSOConfigSingleton().getUserIdAttributeName();
		//		    uow = state.getDefaultUnitOfWork();
		// 		    String dnUserId = userIdAttributeName+"="+userId+","+userBaseDn;
		// 		    Query qry = new Query("e_user", "e_user.sso_name in ('"+dnUserId+"')", null);
		// 		    BioCollection userBios = uow.getBioCollectionBean(qry);
		// 		    int size = userBios.size();
		// 		    if(size != 0){		    	
		//	 		    Bio userBio = userBios.elementAt(0);
		//	 		    String ssoName = (String)userBio.get("sso_name");
		//	 		    String ssoId = UMSUtil.getUserId(ssoName);
		//	 		    Bio userDataBio = userBio.getBio("user_data");
		//	 		    String fullName = (String)userDataBio.get("full_name");
		//	 		    String firstName = UMSUtil.getFirstName(fullName);
		//	 		    String lastName = UMSUtil.getLastName(fullName);
		//	 		    String locale = (String)userBio.get("locale_id");
		//	 		    String email = (String)userDataBio.get("email_address");
		//	 		    user = new User();
		//	 		    user.setFirstName(firstName);
		//	 		    user.setLastName(lastName);
		//	 		    user.setFullName(fullName);
		//	 		    user.setLocale(locale);
		//	 		    user.setEmail(email);
		//	 		    user.setUid(ssoId);
		//	 		    return user;
		// 		    }
		// 		} catch (Exception e) {
		// 			e.printStackTrace();			
		// 		}
		//		
		//		return null;
	}
@Override
public boolean isUserIdExist(String userId){
		User user = this.getUser(userId);
		if(user == null){
			return false;
		}else{
			return true;			
		}

	}

	public String getCurrentUserId(StateInterface state) {
		String userName = "";
		try {
			Bio bio = state.getServiceManager().getUserContext().getUserDataBio(state.getDefaultUnitOfWork().getUOW());
			String ssoName =(String) bio.get("user_name");
			String userBaseDn = SSOConfigSingleton.getSSOConfigSingleton().getUserBaseDn();
			userName = UMSUtil.getUserIdForAds(ssoName, userBaseDn);
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userName;
	}

}
