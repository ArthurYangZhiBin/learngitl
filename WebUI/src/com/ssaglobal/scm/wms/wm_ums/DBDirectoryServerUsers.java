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
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_indirect_activity.ui.Name;
public class DBDirectoryServerUsers extends Users{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DBDirectoryServerUsers.class);
	@Override
	public ArrayList<String> getAllUserIds(){//for task manager
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
 		UnitOfWork uow = null;
 		try {   
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
 		    ArrayList <String> userIdList = new ArrayList<String>();
 		    Bio userBio;
 		    String userid;
 		    for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	userid = (String) userBio.get("sso_name");
 		    	userIdList.add(userid);
 		    }
 		    bioService.remove();
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
		ArrayList<String> allUserIds = getAllUserIds();

		int size = 0;
		if (allUserIds != null)
		{
			size = allUserIds.size();
		}
		for (int i = 0; i < size; i++)
		{
			allUsers.put(allUserIds.get(i), allUserIds.get(i));
		}
		return allUsers;
	}
	
	
	
	/*
	 * input: String firstName which is what User entered in the filter field
	 *        String lastName which is what User entered in the filter field
	 * return: ArrayList of userIds
	 * Exception: EpiException
	 */
	
	public ArrayList<String> getAllUserIds(String firstName, String lastName) throws EpiException {//for indirectory activity
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
 		UnitOfWork uow = null;
		boolean search = false;
		String qry = null;
		serviceFactory = BioServiceFactory.getInstance();
 		bioService = serviceFactory.create("webui");
 		uow = bioService.getUnitOfWork();
 		    
		if (firstName != null && !"".equalsIgnoreCase(firstName)) 
		{
			//filter on both
			if (containsWildCards(firstName))
			{
				firstName = stripWildCards(firstName);
				qry = "wm_e_sso_user.fully_qualified_id ~=%'"+firstName+"'%";
			}
			else
			{
				qry = "wm_e_sso_user.fully_qualified_id ~=%'"+firstName+"'%";
			}
			search = true;
		}
		if(lastName != null && !"".equalsIgnoreCase(lastName)){
			if(search){
				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					qry =qry+  " and wm_e_sso_user.fully_qualified_id ~=%'"+lastName+"'%";
				}
				else
				{
					qry = qry + " and wm_e_sso_user.fully_qualified_id ~=%'"+lastName+"'%";
				}
			}else{
				if (containsWildCards(lastName))
				{
					lastName = stripWildCards(lastName);
					qry = "wm_e_sso_user.fully_qualified_id ~=%'"+lastName+"'%";
				}
				else
				{
					qry = "wm_e_sso_user.fully_qualified_id ~=%'"+lastName+"'%";
				}
			}
			search = true;
		}
		if(search){
			Query qryObj = new Query("wm_e_sso_user", qry, null);
 		    BioCollection userBios = uow.findByQuery(qryObj);
 		    int size = userBios.size();
 		    ArrayList <String> userIdList = new ArrayList<String>();
 		    Bio userBio;
 		    String userid;
 		    for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	userid = (String)userBio.get("sso_user_name");
 		    	userIdList.add(userid);
 		    }
 		    bioService.remove();
 		    return userIdList;
		}
		bioService.remove();
		return null;
	}
	
	
	
	
	
	
	
	@Override
	public User getUser(String userId){
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
 		UnitOfWork uow = null;
 		User user = null;
 		try {   
 		    serviceFactory = BioServiceFactory.getInstance();
 		    bioService = serviceFactory.create("webui");
 		    uow = bioService.getUnitOfWork();
 		    Query qry = new Query("wm_e_sso_user", "wm_e_sso_user.sso_user_name in ('"+userId+"')", null);
 		    BioCollection userBios = uow.findByQuery(qry);
 		    int size = userBios.size();
 		    if(size != 0){
	 		    Bio userBio = userBios.elementAt(0);
	 		    String fullName = (String)userBio.get("fully_qualified_id");
	 		    String firstName = UMSUtil.getFirstName(fullName);
	 		    String lastName = UMSUtil.getLastName(fullName);
	 		    String ssoUserId = (String)userBio.get("sso_user_name");
	 		    
	 		   Query usrQry = new Query("e_user", "e_user.sso_name in ('"+ssoUserId+"')", null);
	 		    BioCollection eUserBios = uow.findByQuery(usrQry);
	 		    String locale = null;
	 		    String email = null;	 	
	 		    if(eUserBios.size() != 0){	
	 		    	Bio euserBio = eUserBios.elementAt(0);
	 		    	locale = (String)euserBio.get("locale_id");
	 		    	Bio userDataBio = euserBio.getBio("user_data");
	 		    	email = (String)userDataBio.get("email_address");	 	
	 		    }
	 		    
	 		    
	 		    user = new User();
	 		    user.setFirstName(firstName);
	 		    user.setLastName(lastName);
	 		    user.setFullName(fullName);
	 		    user.setUid(ssoUserId);
	 		   user.setLocale(locale);
	 		    user.setEmail(email);
	 		    bioService.remove();
	 		    return user;
 		    }
 		    bioService.remove();
 		} catch (Exception e) {
 			if(bioService != null){
 				bioService.remove();
 			}
			e.printStackTrace();			
 		}
		
		return null;
	}
	
	
	@Override
	public HashMap getUsers(ActionContext context, ActionResult result) throws EpiException{// this is for unassigned work
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
 		UnitOfWork uow_webui = null;
  		serviceFactory = BioServiceFactory.getInstance();
 		bioService = serviceFactory.create("webui");
 		uow_webui = bioService.getUnitOfWork();
 		Query qry = new Query("wm_e_sso_user", null, null);
 		BioCollection userBios = uow_webui.findByQuery(qry);
 		int size = userBios.size();
 		Bio userBio;
 		String userid;
 		String fullName;
 		String firstName;
 		String lastName;
 		UserResults user;
 		for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	userid = (String)userBio.get("sso_user_name");
 	 		    fullName = (String)userBio.get("fully_qualified_id");
 	 		    firstName = UMSUtil.getFirstName(fullName);
 	 		    lastName = UMSUtil.getLastName(fullName);
 		    	user = new UserResults(userid, firstName, lastName);
 		    	if(isNewHashmap){
 		    		usersHashmap.put(userid, user);
 		    	}else{
 		    		if(usersHashmap.get(userid) == null){
 		    			usersHashmap.put(userid, user);
 		    		}//else do nothing
 		    	}
  		}
 		userContext.put(WMUMSConstants.ALL_USERS, usersHashmap);
 		bioService.remove();
 		return usersHashmap;
	}
	@Override
	public HashMap getUsers(ModalActionContext context, ActionResult result) throws EpiException{// this is for unassigned work
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
 		UnitOfWork uow_webui = null;
  		serviceFactory = BioServiceFactory.getInstance();
 		bioService = serviceFactory.create("webui");
 		uow_webui = bioService.getUnitOfWork();
 		Query qry = new Query("wm_e_sso_user", null, null);
 		BioCollection userBios = uow_webui.findByQuery(qry);
 		int size = userBios.size();
 		Bio userBio;
 		String userid;
 		String fullName;
 		String firstName;
 		String lastName;
 		UserResults user;
 		for(int i=0; i<size; i++){
 		    	userBio = userBios.elementAt(i);
 		    	userid = (String)userBio.get("sso_user_name");
 	 		    fullName = (String)userBio.get("fully_qualified_id");
 	 		    firstName = UMSUtil.getFirstName(fullName);
 	 		    lastName = UMSUtil.getLastName(fullName);
 		    	user = new UserResults(userid, firstName, lastName);
 		    	if(isNewHashmap){
 		    		usersHashmap.put(userid, user);
 		    	}else{
 		    		if(usersHashmap.get(userid) == null){
 		    			usersHashmap.put(userid, user);
 		    		}//else do nothing
 		    	}
  		}
 		userContext.put(WMUMSConstants.ALL_USERS, usersHashmap);
 		bioService.remove();
 		return usersHashmap;
	}
	
	/*
	 *  input: ActionContext, ActionResult, EXEDataObject
	 *  output: HashMap with Name objects
	 *  Exception:EpiException, program still adds usersHashmap into userContext
	 * @see com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface#getUsers(com.epiphany.shr.ui.action.ActionContext, com.epiphany.shr.ui.action.ActionResult, com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject)
	 */
	@Override
	public HashMap getUsers(ActionContext context, ActionResult result, EXEDataObject collection){//for assigned work
		HashMap usersHashmap;
		Object tempObj;
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
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
		 		UnitOfWork uow = null;
		 		String fullName;
				String firstName;
				String lastName;
				String ssoUserId;
				StringBuffer qry = new StringBuffer();
				boolean search = false;
				for (int i = 0; i < collection.getRowCount(); i++)
				{
					tempObj = collection.getAttribValue(new TextData("userid"));
					String uid = tempObj != null ? tempObj.toString() : null;
					if (!usersHashmap.containsKey(uid))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t Adding UID to search " + uid + "\n", SuggestedCategory.NONE);
						search = true;
						qry.append("'"+uid+"',");
						usersHashmap.put(uid, new Name("", ""));
					}
					collection.getNextRow();
				}
				if(search){
					int length = qry.length();
					qry.delete(length-1, length);
					
					String oaQry = null;
					oaQry = "wm_e_sso_user.sso_user_name in ("+qry.toString()+")";
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t OA query=" + oaQry + "\n", SuggestedCategory.NONE);
		 		    Query qryObj = new Query("wm_e_sso_user", oaQry, null);
					
				    serviceFactory = BioServiceFactory.getInstance();
		 		    bioService = serviceFactory.create("webui");
		 		    uow = bioService.getUnitOfWork();
		 		    BioCollection userBios = uow.findByQuery(qryObj);
		 		    int size = userBios.size();
		 		    for(int i=0; i<size; i++){
			 		    Bio userBio = userBios.elementAt(i);
			 		    fullName = (String)userBio.get("fully_qualified_id");
			 		    firstName = UMSUtil.getFirstName(fullName);
			 		    lastName = UMSUtil.getLastName(fullName);
			 		    ssoUserId = (String)userBio.get("sso_user_name");
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t adding name:" + fullName  + "into Hashmap\n", SuggestedCategory.NONE);
			 		   usersHashmap.put(ssoUserId, new Name(firstName, lastName));
		 		    }
		 		   userContext.put(WMUMSConstants.ALL_USERS_FOR_ASSIGN, usersHashmap);
		 		   bioService.remove();
				}
				return 	usersHashmap;
			} catch (EpiException e)
			{
				if(bioService != null){
					bioService.remove();
				}
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t adding name nto Hashmap for assign failed:" + e.getMessage() + "\n", SuggestedCategory.NONE);
				userContext.put(WMUMSConstants.ALL_USERS_FOR_ASSIGN, usersHashmap);
			}
		return usersHashmap;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface#setList(com.epiphany.shr.ui.action.UIRenderContext, com.epiphany.shr.ui.view.RuntimeListFormInterface)
	 */
	@Override
	public HashMap getUsers(UIRenderContext context, RuntimeListFormInterface form) throws EpiException{//for indirect activity
				HashMap usersHashmap;
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if (userContext.containsKey(WMUMSConstants.ALL_USERS_FOR_INDIRECT_ACTIVITY))
		{
			//load hash from context
			usersHashmap = (HashMap) userContext.get(WMUMSConstants.ALL_USERS_FOR_INDIRECT_ACTIVITY);
		}
		else
		{
			//create hash
			usersHashmap = new HashMap();
		}

		RuntimeListRowInterface[] rows = form.getRuntimeListRows();
		BioServiceFactory serviceFactory;
 		BioService bioService=null;
 		UnitOfWork uow = null;
 		String fullName;
		String firstName;
		String lastName;
		String ssoUserId;
				StringBuffer qry = new StringBuffer();
				boolean search = false;
				for (int i = 0; i < rows.length; i++)
				{
					RuntimeFormWidgetInterface uidWidget = rows[i].getFormWidgetByName("USERID");
					String uid = uidWidget.getDisplayValue();
					if (!usersHashmap.containsKey(uid))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t Adding UID to search " + uid + "\n", SuggestedCategory.NONE);
						search = true;
						qry.append("'"+uid+"',");
						usersHashmap.put(uid, new Name("", ""));
					}
				}

				if (search == true)
				{
					int length = qry.length();
					qry.delete(length-1, length);
					
					String oaQry = null;
					oaQry = "wm_e_sso_user.sso_user_name in ("+qry.toString()+")";
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t OA query=" + oaQry + "\n", SuggestedCategory.NONE);
		 		    Query qryObj = new Query("wm_e_sso_user", oaQry, null);					
				    serviceFactory = BioServiceFactory.getInstance();
		 		    bioService = serviceFactory.create("webui");
		 		    uow = bioService.getUnitOfWork();
		 		    BioCollection userBios = uow.findByQuery(qryObj);
		 		    int size = userBios.size();
		 		    for(int i=0; i<size; i++){
			 		    Bio userBio = userBios.elementAt(i);
			 		    fullName = (String)userBio.get("fully_qualified_id");
			 		    firstName = UMSUtil.getFirstName(fullName);
			 		    lastName = UMSUtil.getLastName(fullName);
			 		    ssoUserId = (String)userBio.get("sso_user_name");
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t adding name:" + fullName  + "into Hashmap\n", SuggestedCategory.NONE);
			 		   usersHashmap.put(ssoUserId, new Name(firstName, lastName));
		 		    }
		 		   userContext.put(WMUMSConstants.ALL_USERS_FOR_INDIRECT_ACTIVITY, usersHashmap);	
		 		   bioService.remove();
				}


			for (int i = 0; i < rows.length; i++)
			{
				RuntimeFormWidgetInterface uidWidget = rows[i].getFormWidgetByName("USERID");
				RuntimeFormWidgetInterface fnWidget = rows[i].getFormWidgetByName("FIRSTNAME");
				RuntimeFormWidgetInterface lnWidget = rows[i].getFormWidgetByName("LASTNAME");

				String userId = uidWidget.getDisplayValue();

				if (usersHashmap.containsKey(userId))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Querying hash " + uidWidget.getDisplayValue(), SuggestedCategory.NONE);
					firstName = ((Name) usersHashmap.get(userId)).first;
					lastName = ((Name) usersHashmap.get(userId)).last;

				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Querying DB: " + uidWidget.getDisplayValue(), SuggestedCategory.NONE);
					User user = this.getUser(userId);
					firstName = user.getFirstName();
					lastName = user.getLastName();
					usersHashmap.put(userId, new Name(firstName, lastName));
				}

				fnWidget.setDisplayValue(firstName);
				lnWidget.setDisplayValue(lastName);

			}
			userContext.put(WMUMSConstants.ALL_USERS_FOR_ASSIGN,usersHashmap); 
			return usersHashmap;
	}
	@Override
	public User getUser(String userId, StateInterface state){
		
 		UnitOfWorkBean uow = null;
 		User user = null;
 		try {   
 		    
		    uow = state.getDefaultUnitOfWork();
 		    Query qry = new Query("wm_e_sso_user", "wm_e_sso_user.sso_user_name in ('"+userId+"')", null);
 		    BioCollection userBios = uow.getBioCollectionBean(qry);
		    
 		    
 		    int size = userBios.size();
 		    if(size != 0){
	 		    Bio userBio = userBios.elementAt(0);
	 		    String fullName = (String)userBio.get("fully_qualified_id");
	 		    String firstName = UMSUtil.getFirstName(fullName);
	 		    String lastName = UMSUtil.getLastName(fullName);
	 		    String ssoUserId = (String)userBio.get("sso_user_name");
	 		   	 		   
	 		    Query usrQry = new Query("e_user", "e_user.sso_name in ('"+ssoUserId+"')", null);
	 		    BioCollection eUserBios = uow.getBioCollectionBean(usrQry);
	 		    String locale = null;
	 		    String email = null;	 	
	 		    if(eUserBios.size() != 0){	
	 		    	Bio euserBio = eUserBios.elementAt(0);
	 		    	locale = (String)euserBio.get("locale_id");
	 		    	Bio userDataBio = euserBio.getBio("user_data");
	 		    	email = (String)userDataBio.get("email_address");	 	
	 		    }
	 		    
	 		    user = new User();
	 		    user.setFirstName(firstName);
	 		    user.setLastName(lastName);
	 		    user.setFullName(fullName);
	 		    user.setUid(ssoUserId);
	 		    user.setLocale(locale);
	 		    user.setEmail(email);
	 		    return user;
 		    }
 		} catch (Exception e) {
 			e.printStackTrace();			
 		}
		
		return null;
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
			userName =(String) bio.get("user_name");
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userName;
	}

}
