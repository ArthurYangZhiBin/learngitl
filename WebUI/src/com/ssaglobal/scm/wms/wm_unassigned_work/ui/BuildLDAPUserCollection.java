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

package com.ssaglobal.scm.wms.wm_unassigned_work.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.wm_ums.*;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class BuildLDAPUserCollection extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BuildLDAPUserCollection.class);
//	private static final String LDAPRESULTLIST = "LDAPRESULTLIST";

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Entering BuildLDAPUserCollection" + "\n", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		UnitOfWork uow = uowb.getUOW();

		HelperBio helper = uow.createHelperBio("wm_lp_usr");
		ArrayListBioRefSupplier newBioList = new ArrayListBioRefSupplier("wm_lp_usr");
		HashMap users = new HashMap();
		
		try{
			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			users = umsInterface.getUsers(context, result);
		}catch (Exception e){
			e.printStackTrace();
			return RET_CANCEL;
		}
		
		
		/*mark ma commented out
		HashMap ldapResults = new HashMap();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if (userContext.containsKey(LDAPRESULTLIST))
		{
			//load arraylist from context
			ldapResults = (HashMap) userContext.get(LDAPRESULTLIST);
		}
		else
		{
			//create arraylist
			ldapResults = new HashMap();
		}
		try
		{
			UserManagementService ums = UMSHelper.getUMS();
			UserStore us = ums.getDefaultUserStore();
			SearchCriteria sc = new SearchCriteria();
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Searching LDAP" + "\n", SuggestedCategory.NONE);
			Enumeration searchResults = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1,
													-1);
			while (searchResults.hasMoreElements())
			{
				com.ssaglobal.cs.ums.api.User queryResult = (com.ssaglobal.cs.ums.api.User) searchResults.nextElement();
				ldapResults.put(
								queryResult.getId(),
								new LDAPResults(queryResult.getId(), queryResult.getFirstName(), queryResult.getLastName()));

			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		} */
//		userContext.put(LDAPRESULTLIST, ldapResults);

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Going to build collection" + "\n", SuggestedCategory.NONE);
		//Build BioCollection

		for (Iterator it = users.entrySet().iterator(); it.hasNext();)
		{

			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			UserResults userInfo = (UserResults) entry.getValue();
			String first = userInfo.getFirstName();
			String last = userInfo.getLastName();
//			String uid = userInfo.getUid();
			Bio temp = uow.createBio(helper);
			temp.set("usr_login", key);
			temp.set("usr_fname", first);
			temp.set("usr_lname", last);
			newBioList.add(temp.getBioRef());

		}
		BioCollection bc = uow.fetchBioCollection(newBioList);
		BioCollectionBean returnList = uowb.getBioCollection(bc.getBioCollectionRef());
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + state.getCurrentRuntimeForm().getName() + "\n", SuggestedCategory.NONE);
		//state.getCurrentRuntimeForm().setFocus(returnList);
		result.setFocus(returnList);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Exiting BuildLDAPUserCollection" + "\n", SuggestedCategory.NONE);		
		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Entering BuildLDAPUserCollection" + "\n", SuggestedCategory.NONE);
		StateInterface state = ctx.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		UnitOfWork uow = uowb.getUOW();

		HelperBio helper = uow.createHelperBio("wm_lp_usr");
		ArrayListBioRefSupplier newBioList = new ArrayListBioRefSupplier("wm_lp_usr");
		HashMap users = new HashMap();
		
		try{
			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			users = umsInterface.getUsers(ctx, args);
		}catch (Exception e){
			e.printStackTrace();
			return RET_CANCEL;
		}

/*		HashMap ldapResults = new HashMap();
		EpnyUserContext userContext = ctx.getServiceManager().getUserContext();
		if (userContext.containsKey(LDAPRESULTLIST))
		{
			//load arraylist from context
			ldapResults = (HashMap) userContext.get(LDAPRESULTLIST);
		}
		else
		{
			//create arraylist
			ldapResults = new HashMap();
		}
/* mark ma commented out 		try
		{
			UserManagementService ums = UMSHelper.getUMS();
			UserStore us = ums.getDefaultUserStore();
			SearchCriteria sc = new SearchCriteria();
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Searching LDAP" + "\n", SuggestedCategory.NONE);
			Enumeration searchResults = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1,
													-1);
			while (searchResults.hasMoreElements())
			{
				com.ssaglobal.cs.ums.api.User queryResult = (com.ssaglobal.cs.ums.api.User) searchResults.nextElement();
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + queryResult.getId() + "\n", SuggestedCategory.NONE);
				ldapResults.put(
								queryResult.getId(),
								new LDAPResults(queryResult.getId(), queryResult.getFirstName(), queryResult.getLastName()));

			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}mark ma comments ends*/
//		userContext.put(LDAPRESULTLIST, ldapResults);

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Going to build collection" + "\n", SuggestedCategory.NONE);
		//Build BioCollection

		for (Iterator it = users.entrySet().iterator(); it.hasNext();)
		{

			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			UserResults userInfo = (UserResults) entry.getValue();
			String first = userInfo.getFirstName();
			String last = userInfo.getLastName();
			String uid = userInfo.getUid();
			Bio temp = uow.createBio(helper);
			temp.set("usr_login", uid);
			temp.set("usr_fname", first);
			temp.set("usr_lname", last);
			newBioList.add(temp.getBioRef());

		}
		BioCollection bc = uow.fetchBioCollection(newBioList);
		BioCollectionBean returnList = uowb.getBioCollection(bc.getBioCollectionRef());

		state.getCurrentRuntimeForm().setFocus(returnList);
		args.setFocus(returnList);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Exiting BuildLDAPUserCollection" + "\n", SuggestedCategory.NONE);
		return RET_CONTINUE;
	}
}
