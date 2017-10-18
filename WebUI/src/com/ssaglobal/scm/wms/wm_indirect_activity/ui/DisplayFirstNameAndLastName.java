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

package com.ssaglobal.scm.wms.wm_indirect_activity.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DisplayFirstNameAndLastName extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisplayFirstNameAndLastName.class);

	private static final String LDAPNAMEHASH = "LDAPNAMEHASH";

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{
		try{
			WmsUmsInterface umsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			java.util.HashMap users = umsInterface.getUsers(context, form);
		}catch (Exception e){
			e.printStackTrace();
			return RET_CANCEL;
		}
/*		HashMap name;
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if (userContext.containsKey(LDAPNAMEHASH))
		{
			//load hash from context
			name = (HashMap) userContext.get(LDAPNAMEHASH);
		}
		else
		{
			//create hash
			name = new HashMap();
		}

		RuntimeListRowInterface[] rows = form.getRuntimeListRows();
		try
		{
			//Query LDAP Once
			{
				UserManagementService ums = UMSHelper.getUMS();
				UserStore us = ums.getDefaultUserStore();
				SearchCriteria sc = new SearchCriteria();
				String firstName;
				String lastName;
				boolean search = false;
				for (int i = 0; i < rows.length; i++)
				{
					RuntimeFormWidgetInterface uidWidget = rows[i].getFormWidgetByName("USERID");
					String uid = uidWidget.getDisplayValue();
					if (!name.containsKey(uid))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "\n\t Adding UID to search " + uid + "\n", SuggestedCategory.NONE);
						search = true;
						sc.addCondition(AttributeConstants.ID, uid, com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
						name.put(uid, new Name("", ""));
					}
				}
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + ums.getDefaultUserStore().getRootHierarchy().getDN() + "\n", SuggestedCategory.NONE);

				if (search == true)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Querying LDAP..." + "\n", SuggestedCategory.NONE);
					Enumeration results = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1, -1);
					while (results.hasMoreElements())
					{
						com.ssaglobal.cs.ums.api.User result = (com.ssaglobal.cs.ums.api.User) results.nextElement();
						firstName = result.getFirstName();
						lastName = result.getLastName();
						_log.debug("LOG_DEBUG_EXTENSION", "Results " + result + " " + firstName + " " + lastName, SuggestedCategory.NONE);
						name.put(result.getId(), new Name(firstName, lastName));
					}
				}

			}

			for (int i = 0; i < rows.length; i++)
			{
				RuntimeFormWidgetInterface uidWidget = rows[i].getFormWidgetByName("USERID");
				RuntimeFormWidgetInterface fnWidget = rows[i].getFormWidgetByName("FIRSTNAME");
				RuntimeFormWidgetInterface lnWidget = rows[i].getFormWidgetByName("LASTNAME");

				String userid = uidWidget.getDisplayValue();

				String firstName = null;
				String lastName = null;
				if (name.containsKey(userid))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Querying hash " + uidWidget.getDisplayValue(), SuggestedCategory.NONE);
					firstName = ((Name) name.get(userid)).first;
					lastName = ((Name) name.get(userid)).last;

				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "Querying LDAP: " + uidWidget.getDisplayValue(), SuggestedCategory.NONE);
					//LDAP
					UserManagementService ums = UMSHelper.getUMS();
					UserStore us = ums.getDefaultUserStore();
					SearchCriteria sc = new SearchCriteria();

					sc.addCondition(AttributeConstants.ID, uidWidget.getDisplayValue(), com.ssaglobal.cs.ums.api.SearchCondition.EXACT);
					Enumeration results = us.search(sc, ums.getDefaultUserStore().getRootHierarchy().getDN(), true, -1, -1);
					//Enumeration enum = us.search(sc, "ou=ssausers,dc=ssainternal,dc=net", true, -1, -1);
					while (results.hasMoreElements())
					{
						com.ssaglobal.cs.ums.api.User result = (com.ssaglobal.cs.ums.api.User) results.nextElement();
						firstName = result.getFirstName();
						lastName = result.getLastName();
						_log.debug("LOG_DEBUG_EXTENSION", "Results" + result + " " + firstName + " " + lastName, SuggestedCategory.NONE);

					}
					name.put(userid, new Name(firstName, lastName));
				}

				fnWidget.setDisplayValue(firstName);
				lnWidget.setDisplayValue(lastName);

			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		userContext.put(LDAPNAMEHASH, name);*/
		return RET_CONTINUE;
	}

}
