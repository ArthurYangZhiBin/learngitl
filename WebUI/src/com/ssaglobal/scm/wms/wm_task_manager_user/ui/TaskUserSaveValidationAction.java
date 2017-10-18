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

package com.ssaglobal.scm.wms.wm_task_manager_user.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sso.UserAttributes;
import com.epiphany.shr.sso.client.ComponentRegistry;
import com.epiphany.shr.sso.client.ConfigAdapter;
import com.epiphany.shr.sso.exception.InvalidUser;
import com.epiphany.shr.sso.exception.NoServerAvailableException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.ssaglobal.scm.wms.wm_ums.User;
import com.ssaglobal.scm.wms.wm_ums.WMUMSConstants;
import com.ssaglobal.scm.wms.wm_ums.WMUMSDirectoryFactory;
import com.ssaglobal.scm.wms.wm_ums.WmsUmsInterface;

/*
 * * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TaskUserSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	static class MyConfigAdapter implements ConfigAdapter
	{
		private final Properties _clientProps = new Properties();

		MyConfigAdapter(String clientFileName)
		{
			try
			{
				File fileObj = new File(clientFileName);
				LineNumberReader is = new LineNumberReader(new InputStreamReader(new FileInputStream(fileObj), ComponentRegistry.defaultEncoding));
				String line = is.readLine();
				while (line != null)
				{
					if (line.startsWith(ComponentRegistry.CLIENT_KEY_PROPERTY_NAME_NAME))
					{
						String val = line.substring(ComponentRegistry.CLIENT_KEY_PROPERTY_NAME_NAME.length() + 1);
						_clientProps.setProperty(ComponentRegistry.CLIENT_KEY_PROPERTY_NAME_NAME, val);
					}
					else if (line.startsWith(ComponentRegistry.CLIENT_KEY_PROPERTY_KEY_NAME))
					{
						String val = line.substring(ComponentRegistry.CLIENT_KEY_PROPERTY_KEY_NAME.length() + 1);
						_clientProps.setProperty(ComponentRegistry.CLIENT_KEY_PROPERTY_KEY_NAME, val);
					}

					line = is.readLine();
				}
				is.close();
			} catch (Exception ex)
			{
				_log.error(new EpiException("EXP_SSO_UNEXPECTED_EXCEPTION", "SSO Unexpected Exception", SuggestedCategory.TESTING, ex));
			}
		}

		public String getValue(String Name, String DefaultValue)
		{
			if (Name.equals(ConfigAdapter.CLIENT_NAME_NAME))
			{
				String name = (String) _clientProps.get(ComponentRegistry.CLIENT_KEY_PROPERTY_NAME_NAME);
				if (name != null)
					return name;
			}
			else if (Name.equals(ConfigAdapter.CLIENT_KEY_NAME))
			{
				String key = (String) _clientProps.get(ComponentRegistry.CLIENT_KEY_PROPERTY_KEY_NAME);
				if (key != null)
					return key;
			}

			return DefaultValue;
		}
	}

	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskUserSaveValidationAction.class);

	ComponentRegistry factory = null;

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 * @throws  
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskUserSaveValidationAction", SuggestedCategory.NONE);
		StateInterface state = context.getState();

		/* UserAttribute Supporting Code */
		String certFile = System.getProperty("shared.root") + System.getProperty("file.separator") + "etc" + System.getProperty("file.separator") + "EpiphanyDefault.cert";

		factory = new ComponentRegistry();

		try
		{
			factory.initialize(new MyConfigAdapter(certFile));
		} catch (Exception ex)
		{
			_log.error("LOG_INITIALIZING_COMPONENT_RETISTRY", "Unable to initialize ComponentRegistry", SuggestedCategory.TESTING);

		}

		/* End UserAttribute Supporting Code */

		RuntimeFormInterface headerDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_task_manager_user_detail_header_view", state);

		RuntimeListFormInterface detailListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_task_manager_user_detail_list_view", state);

		if (isNull(headerDetailForm))
		{
			return RET_CANCEL;
		}
		DataBean headerDetailFormFocus = headerDetailForm.getFocus();
		boolean isInsert = false;

		if (headerDetailFormFocus instanceof BioBean)
		{
			isInsert = false;
		}
		else if (headerDetailFormFocus instanceof QBEBioBean)
		{
			isInsert = true;
		}

		setUserKey(state, headerDetailFormFocus);

		//check for duplicate userkey
		final String table = "TASKMANAGERUSER";
		final String attribute = "USERKEY";
		if (isInsert)
		{
			String attributeValue = headerDetailFormFocus.getValue(attribute) == null ? null : headerDetailFormFocus.getValue(attribute).toString().toUpperCase();
			String query = "select * from " + table + " where " + attribute + " = '" + attributeValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY " + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() != 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Non unique userkey " + results.getRowCount(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_TASK_USER_DUPLICATE", "WMEXP_TASK_USER_DUPLICATE", new Object[] {});
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Results " + results.getRowCount(), SuggestedCategory.NONE);
			}

			//Populate usr_name, usr_fname and usr_lname from LDAP when user is created in Taskmanageruser
			String firstName = null;
			String lastName = null;
			//			String name = "";

			String uid = attributeValue;
			WmsUmsInterface wmsInterface = WMUMSDirectoryFactory.getUsers(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType());
			User user = wmsInterface.getUser(uid);
			firstName = user.getFirstName();
			lastName = user.getLastName();
			//			name = user.getFullName();

			headerDetailFormFocus.setValue("USR_FNAME", firstName);
			headerDetailFormFocus.setValue("USR_LNAME", lastName);
			//			headerDetailFormFocus.setValue("USR_NAME", name);

		}
		
		if (headerDetailFormFocus.getValue("USERGROUP") == null || StringUtils.isEmpty(headerDetailFormFocus.getValue("USERGROUP").toString()))
		{
			String facility = "FACILITY";
			HttpSession session = state.getRequest().getSession();
			if (session.getAttribute(SetIntoHttpSessionAction.DB_USERID) != null) {
				facility = (String) session.getAttribute(SetIntoHttpSessionAction.DB_USERID);
				facility = facility.toUpperCase();
			}
			headerDetailFormFocus.setValue("USERGROUP", facility);
		}

		if (isNull(detailListForm))
		{
			return RET_CONTINUE;
		}
		if (detailListForm.getFocus() == null ||
			 !detailListForm.getFocus().isBioCollection()){
			return RET_CONTINUE;
		}
		BioCollectionBean detailListFocus = (BioCollectionBean)detailListForm.getFocus();
		if(detailListFocus.isDirty()){
			for (int i = 0; i < detailListFocus.size(); i++){
				Bio bio = detailListFocus.elementAt(i);
				if (bio.get("PERMISSIONTYPE") != null && !(bio.get("PERMISSIONTYPE").toString().equalsIgnoreCase("PK") || bio.get("PERMISSIONTYPE").toString().equalsIgnoreCase("DP"))){
					List updatedAttr = bio.getUpdatedAttributes();
					if(updatedAttr.size() > 0){
						boolean didUpdateReadOnlyValue = false;
						Iterator attrItr = updatedAttr.iterator();
						while(attrItr.hasNext()){
							String attr = (String)attrItr.next();
							if(attr.equals("ALLOWPIECE") ||
									attr.equals("ALLOWCASE") ||
									attr.equals("ALLOWIPS") ||
									attr.equals("ALLOWPALLET")){
								Object attrValueObj = bio.get(attr);
								if (attrValueObj != null && (attrValueObj.toString().equalsIgnoreCase("true") || attrValueObj.toString().equals("1"))){
									didUpdateReadOnlyValue = true;
									break;
								}
							}
						}
						if (didUpdateReadOnlyValue == true){
							throw new UserException("WMEXP_TM_USER_CANNOT_UPDATE_1", "WMEXP_TM_USER_CANNOT_UPDATE_1", new Object[] {});
						}
					}
				}
			}
		}
		return RET_CONTINUE;
	}

	private void setUserKey(StateInterface state, DataBean headerDetailFormFocus) throws EpiDataException, InvalidUser
	{
		//Set UserKey
		
		headerDetailFormFocus.setValue("USERKEY", headerDetailFormFocus.getValue("USR_NAME"));

		//if ADS, perform some magic to get the SAMACCOUNTNAME and set that to the USERKEY
		//needs this block in the sso_config.xml for ADS
		/*
		 ...
		      <sso:user_attributes>
		  	    <sso:user_attribute>
		  		<sso:user_attribute_name>sAMAccountName</sso:user_attribute_name>	
		  		<sso:user_attribute_value>sAMAccountName</sso:user_attribute_value>	
		  	    </sso:user_attribute>	    
		      </sso:user_attributes>
		  ...
		  </sso:directory>
		  */
		if (WMUMSConstants.ADS_DIRECTORY_SERVICE.equalsIgnoreCase(SSOConfigSingleton.getSSOConfigSingleton().getDirectoryServerType()))
		{
			_log.debug("LOG_DEBUG_EXTENSION_TaskUserSaveValidationAction_setUserKey", "In ADS", SuggestedCategory.NONE);
			UserAttributes attw = factory.getUserAttributes();
			Hashtable userAttrHash = null;

			try
			{
				String canonicalName = BioAttributeUtil.getString(headerDetailFormFocus, "USR_NAME");
				userAttrHash = attw.getUserAttributes(canonicalName, new String[] { "sAMAccountName" });
			} catch (NoServerAvailableException e)
			{
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_TaskUserSaveValidationAction_setUserKey", e.getStackTraceAsString(), SuggestedCategory.NONE);
			} catch (RemoteException e)
			{
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_TaskUserSaveValidationAction_setUserKey", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
			}

			_log.debug("LOG_ERROR_EXTENSION_TaskUserSaveValidationAction_setUserKey", "Size of Attributes = " + userAttrHash.size(), SuggestedCategory.NONE);
			//			for (Object key : userAttrHash.keySet())
			//			{
			//				_log.debug("LOG_ERROR_EXTENSION_TaskUserSaveValidationAction_setUserKey", "" + key, SuggestedCategory.NONE);
			//				_log.debug("LOG_ERROR_EXTENSION_TaskUserSaveValidationAction_setUserKey", "" + userAttrHash.get(key), SuggestedCategory.NONE);
			//			}
			Object sAMAccountName = userAttrHash.get("sAMAccountName");
			_log.debug("LOG_DEBUG_EXTENSION_TaskUserSaveValidationAction_setUserKey", "sAMAaccountName is " + sAMAccountName, SuggestedCategory.NONE);
			if (sAMAccountName == null)
			{
				_log.debug("LOG_DEBUG_EXTENSION_TaskUserSaveValidationAction_setUserKey", "sAMAaccount Name is null, going to get full_name from user_data", SuggestedCategory.NONE);
				Query qry = new Query("e_user", "e_user.sso_name = '" + headerDetailFormFocus.getValue("USR_NAME") + "'", null);
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				BioCollectionBean rs = tuow.getBioCollectionBean(qry);
				String userName;
				for (int i = 0; i < rs.size(); i++)
				{
					BioBean userRecord = rs.get("" + i);
					userName = (String) userRecord.getBio("user_data").get("full_name");
					headerDetailFormFocus.setValue("USERKEY", userName.toUpperCase());
				}
			}
			else
			{
				headerDetailFormFocus.setValue("USERKEY", ((String) sAMAccountName).toUpperCase());
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_TaskUserSaveValidationAction_setUserKey", "Setting USERKEY to " + headerDetailFormFocus.getValue("USR_NAME"), SuggestedCategory.NONE);
	}

	private boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
