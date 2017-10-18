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

package com.ssaglobal.scm.wms.wms_app_security;

import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioCollInsufficientElementsException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

public class WmSecurityRolesSaveAction extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityRolesSaveAction.class);
	public WmSecurityRolesSaveAction()
	{
	}

	protected int execute(ActionContext context, ActionResult result)
	throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing WmSecurityRolesSaveAction",100L);
		StateInterface state = context.getState();
		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("tab 0");
		tabs.add("tab 1");
		tabs.add("tab 2");
		RuntimeFormInterface screensForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wms_app_security_screens_list",state);
		RuntimeFormInterface facilitiesForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wms_app_security_facilities_list",tabs,state);
		RuntimeFormInterface reportsForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wms_app_security_report_list",tabs,state);
		RuntimeFormInterface rolesForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wms_app_security_new_role_form",state);
		_log.debug("LOG_SYSTEM_OUT","\n\nRolesForm:"+rolesForm+"\n\n",100L);
		if(screensForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Screens Form:"+screensForm.getName(),100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Screens Form:Null",100L);

		if(facilitiesForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Facility Form:"+facilitiesForm.getName(),100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Facility Form:Null",100L);

		if(reportsForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Reports Form:"+reportsForm.getName(),100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Reports Form:Null",100L);

		if(rolesForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Roles Form:"+rolesForm.getName(),100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Roles Form:Null",100L);


		boolean isDirty = false;
		String newRoleName = "";
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean screenFocus = null;
		BioCollectionBean facilityFocus = null;
		BioCollectionBean reportFocus = null;
		if(screensForm != null){
			BioCollectionBean focus = (BioCollectionBean)screensForm.getFocus();
			if(focus != null){
//				ArrayList addList = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistProfiles.SESS_KEY_PROF_ADD_LIST);
//				ArrayList delList = (ArrayList)state.getRequest().getSession().getAttribute(WmSecurityPersistProfiles.SESS_KEY_PROF_DEL_LIST);
//				if(addList == null)
//					addList = new ArrayList();
//				if(delList == null)
//					delList = new ArrayList();
				screenFocus = focus;
				ArrayList<String> addList = new ArrayList<String>();
				ArrayList<String> delList = new ArrayList<String>();
				try {
					determineChanges(focus, addList, delList, "user_profile_id");
					for(int i = 0; i < focus.size(); i++){
						Bio bio = focus.elementAt(i);
						String key = bio.getString("user_profile_id");
						boolean isBioDirty = false;

						if(addList.size() > 0){
							if(addList.contains(key)){
								String profileId = (String)bio.get("user_profile_id");
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_profile_id = '"+profileId.toUpperCase()+"' AND wm_meta_profile_role_mapping.user_role_id = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_meta_profile_role_mapping");
									tempBio.set("user_role_id",roleId);
									tempBio.set("user_profile_id",profileId);
									tempBio.set("app_module_id","client_profile_role");
									tempBio.save();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && delList.size() > 0){
							if(delList.contains(key)){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								String profileId = (String)bio.get("user_profile_id");
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_profile_id = '"+profileId.toUpperCase()+"' AND wm_meta_profile_role_mapping.user_role_id = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && bio.get("is_selected") != null){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got Changed Bio "+bio.get("user_profile_name"),100L);
							String profileId = (String)bio.get("user_profile_id");
							String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
							int isSelected = BioUtil.getInt(bio,"is_selected");
							if(isSelected == 0){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								Query qry = new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_profile_id = '"+profileId.toUpperCase()+"' AND wm_meta_profile_role_mapping.user_role_id = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
								}
							}
							else if(isSelected == 1){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 1...",100L);
								Query qry = new Query("wm_meta_profile_role_mapping","wm_meta_profile_role_mapping.user_profile_id = '"+profileId.toUpperCase()+"' AND wm_meta_profile_role_mapping.user_role_id = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_meta_profile_role_mapping");
									tempBio.set("user_role_id",roleId);
									tempBio.set("user_profile_id",profileId);
									tempBio.set("app_module_id","client_profile_role");
									tempBio.save();
									isDirty = true;
								}
							}
						}
					}
				} catch (BioCollInsufficientElementsException e) {
					//ignoring
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
				} catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (DataBeanException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
		if(facilitiesForm != null){
			BioCollectionBean focus = (BioCollectionBean)facilitiesForm.getFocus();
			if(focus != null){
				facilityFocus = focus;
				ArrayList<String> addList = new ArrayList<String>();
				ArrayList<String> delList = new ArrayList<String>();
				try {
					determineChanges(focus, addList, delList, "db_key");
					for(int i = 0; i < focus.size(); i++){
						Bio bio = focus.elementAt(i);
						String key = bio.getString("db_key");
						boolean isBioDirty = false;

						if(addList.size() > 0){
							if(addList.contains(key)){
								String facilityId = bio.get("db_key").toString();
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_facilityrolemapping","wm_facilityrolemapping.FACILITYID = '"+facilityId.toUpperCase()+"' AND wm_facilityrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_facilityrolemapping");
									tempBio.set("ROLEID",roleId);
									tempBio.set("FACILITYID",facilityId);
									tempBio.save();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && delList.size() > 0){
							if(delList.contains(key)){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								String facilityId = bio.get("db_key").toString();
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_facilityrolemapping","wm_facilityrolemapping.FACILITYID = '"+facilityId.toUpperCase()+"' AND wm_facilityrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && bio.get("is_selected") != null){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got Changed Bio "+bio.get("db_name"),100L);
							String facilityId = bio.get("db_key").toString();
							String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
							int isSelected = BioUtil.getInt(bio, "is_selected");
							if(isSelected==0){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								Query qry = new Query("wm_facilityrolemapping","wm_facilityrolemapping.FACILITYID = '"+facilityId.toUpperCase()+"' AND wm_facilityrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
								}
							}
							else if(isSelected==1){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 1...",100L);
								Query qry = new Query("wm_facilityrolemapping","wm_facilityrolemapping.FACILITYID = '"+facilityId.toUpperCase()+"' AND wm_facilityrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_facilityrolemapping");
									tempBio.set("ROLEID",roleId);
									tempBio.set("FACILITYID",facilityId);
									tempBio.save();
									isDirty = true;
								}
							}
						}
					}
				}  catch (BioCollInsufficientElementsException e) {
					//ignoring
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
				}catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (DataBeanException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
		if(reportsForm != null){
			BioCollectionBean focus = (BioCollectionBean)reportsForm.getFocus();
			if(focus != null){
				reportFocus = focus;
				ArrayList<String> addList = new ArrayList<String>();
				ArrayList<String> delList = new ArrayList<String>();
				try {
					determineChanges(focus, addList, delList, "RPT_ID");
					for(int i = 0; i < focus.size(); i++){
						Bio bio = focus.elementAt(i);
						String key = bio.getString("RPT_ID");
						boolean isBioDirty = false;

						if(addList.size() > 0){
							if(addList.contains(key)){
								String reportId = (String)bio.get("RPT_ID");
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_reportsrolemapping","wm_reportsrolemapping.REPORTID = '"+reportId.toUpperCase()+"' AND wm_reportsrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_reportsrolemapping");
									tempBio.set("ROLEID",roleId);
									tempBio.set("REPORTID",reportId);
									tempBio.save();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && delList.size() > 0){
							if(delList.contains(key)){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								String reportId = (String)bio.get("RPT_ID");
								String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
								Query qry = new Query("wm_reportsrolemapping","wm_reportsrolemapping.REPORTID = '"+reportId.toUpperCase()+"' AND wm_reportsrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
									isBioDirty = true;
								}
							}
						}
						if(!isBioDirty && bio.get("is_selected") != null){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Got Changed Bio "+bio.get("RPT_TITLE"),100L);
							String reportId = (String)bio.get("RPT_ID");
							String roleId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_ROLE_SELECTED");
							int isSelected = BioUtil.getInt(bio, "is_selected");
							if(isSelected==0){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);
								Query qry = new Query("wm_reportsrolemapping","wm_reportsrolemapping.REPORTID = '"+reportId.toUpperCase()+"' AND wm_reportsrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection != null && bioCollection.size() > 0){
									bioCollection.elementAt(0).delete();
									isDirty = true;
								}
							}
							else if(isSelected==1){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 1...",100L);
								Query qry = new Query("wm_reportsrolemapping","wm_reportsrolemapping.REPORTID = '"+reportId.toUpperCase()+"' AND wm_reportsrolemapping.ROLEID = '"+roleId.toUpperCase()+"'","");
								BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
								if(bioCollection == null || bioCollection.size() == 0){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","creating temp bio...",100L);
									QBEBioBean tempBio = uow.getQBEBioWithDefaults("wm_reportsrolemapping");
									tempBio.set("ROLEID",roleId);
									tempBio.set("REPORTID",reportId);
									tempBio.save();
									isDirty = true;
								}
							}
						}
					}
				} catch (BioCollInsufficientElementsException e) {
					//ignoring
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
				} catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (DataBeanException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
			if(rolesForm != null){
				QBEBioBean newRecord = (QBEBioBean)rolesForm.getFocus();
				//Save New Role In MetaData...
				try {
					if(newRecord != null && !newRecord.isEmpty()){
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Adding New Role...",100L);
						newRoleName = (String)newRecord.get("user_role_name");
						Query qry = new Query("wm_meta_user_role","wm_meta_user_role.user_role_name = '"+newRoleName.toUpperCase()+"'","");
						BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
						if(bioCollection != null && bioCollection.size() > 0){
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Role already exists...",100L);
							String args[] = {newRoleName};
							String errorMsg = getTextMessage("WMEXP_DUP_ROLE",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						qry = new Query("wm_meta_user_role","","wm_meta_user_role.user_role_listorder");
						bioCollection = uow.getBioCollectionBean(qry);
						Integer userRoleListorder = new Integer(1);
						if(bioCollection != null && bioCollection.size() > 0){
							userRoleListorder = (Integer)bioCollection.elementAt(bioCollection.size() - 1).get("user_role_listorder");
						}

						QBEBioBean newSSORole = uow.getQBEBioWithDefaults("sso_role");

						newRecord.set("user_role_listorder",new Integer(userRoleListorder.intValue() + 1));
						newRecord.set("user_role_id",GUIDFactory.getGUIDStatic());
						newRecord.set("system_profile_flag",Boolean.FALSE);
						newRecord.set("sso_role_name",newRoleName);
						newRecord.set("masked_attributes",new Integer(0));

						newSSORole.set("sso_role_name", newRoleName);

						//Changed to associate client-added roles with no module.
						//This will prevent these roles from being purged if a loadModule is done.
						newRecord.set("app_module_id","87F87950A020462AA143A37FEFFE9C17");
						//newRecord.set("app_module_id","client_roles");
						isDirty = true;
					}
				} catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (DataBeanException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","about to save...",100L);
			if(isDirty){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","saving...",100L);
				try {
					if(screenFocus != null)	{
						clearChanges(screenFocus, uow);
					}
					if(facilityFocus != null) {
						clearChanges(facilityFocus, uow);
					}
					if(reportFocus != null) {
						clearChanges(reportFocus, uow);
					}
					uow.saveUOW(true);
				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			uow.clearState();

//following are adding roles for LDAP, database adding roles are implemented above
		/*mark ma comments starts		if(addingRole){
			boolean didLDAPSaveSucceed = true;
			try {
				//Add Role To LDAP...
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Adding to LDAP...",100L);
				UserManagementService ums = UMSHelper.getUMS();
				RoleManager rolemanager=ums.getDefaultRoleManager();
				Role role=new Role(newRoleName);
				role.setDescription("");
				rolemanager.addRole(role);
			} catch (UMSException e) {
				e.printStackTrace();
				didLDAPSaveSucceed = false;
			} catch (GenException e) {
				e.printStackTrace();
				didLDAPSaveSucceed = false;
			} catch(ResourceAlreadyFoundException e){
				//didLDAPSaveSucceed = false;
			} mark ma comments ends
			if(!didLDAPSaveSucceed){
//				LDAP add failed, roll back Meta-Data save...
				Query qry = new Query("wm_meta_user_role","wm_meta_user_role.user_role_name = '"+newRoleName.toUpperCase()+"'","");
				BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
				try {
					if(bioCollection != null && bioCollection.size() > 0){
						bioCollection.elementAt(0).delete();
						uow.saveUOW(true);
					}
				} catch (EpiDataException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				String args[] = new String[0];
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}*/
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving WmSecurityRolesSaveAction",100L);
		return RET_CONTINUE;
	}

	/**
	 *
	 * Remove changes of the view from the UOW to prevent save errors
	 *
	 * @param focus
	 * @param uow
	 * @throws EpiDataException
	 */
	private void clearChanges(BioCollectionBean focus, UnitOfWorkBean uow) throws EpiDataException {
		try {
			for (int i = 0; i < focus.size(); i++) {
				BioBean profile = focus.get("" + i);
				if (profile.isDirty()) {
					List updatedAttributes = profile.getUpdatedAttributes();
					if(!updatedAttributes.isEmpty())
					{
						//reset previous value to trick OA
						Object previous = profile.getValue("is_selected", true);
						profile.setValue("is_selected", previous);
					}
				}

			}
		} catch (BioCollInsufficientElementsException e) {
			// ignoring
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored"
					+ e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);

		}
	}

	/**
	 * Determine what profiles were added/removed from the role
	 *
	 * @param focus
	 * @param addList
	 * @param delList
	 * @throws EpiDataException
	 */
	private void determineChanges(
			BioCollectionBean focus,
			ArrayList<String> addList,
			ArrayList<String> delList,
			String attribute) throws EpiDataException {

		try {
			for (int i = 0; i < focus.size(); i++) {
				BioBean profile = focus.get("" + i);
				if (profile.hasBeenUpdated("is_selected") == true) {
					int selected = BioAttributeUtil.getInt(profile, "is_selected");
					if (selected == 1) {
						addList.add(BioAttributeUtil.getString(profile, attribute));
					} else {
						delList.add(BioAttributeUtil.getString(profile, attribute));
					}
				}
			}
		} catch (BioCollInsufficientElementsException e) {
			// ignoring
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored"
					+ e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
	}
}