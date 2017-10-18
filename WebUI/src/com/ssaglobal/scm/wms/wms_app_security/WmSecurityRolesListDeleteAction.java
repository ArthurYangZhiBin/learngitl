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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



public class WmSecurityRolesListDeleteAction extends ActionExtensionBase
{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityRolesListDeleteAction.class);
    public WmSecurityRolesListDeleteAction()
    {
    }

    protected int execute(ActionContext context, ActionResult result)
        throws UserException
    {
    	_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Executing WmSecurityRolesListDeleteAction",100L);    	
        StateInterface state = context.getState();             
        RuntimeListFormInterface rolesForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wms_app_security_roles_list",state);
        
        if(rolesForm != null)
        	_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Found Roles Form:"+rolesForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Found Roles Form:Null",100L);			
        
        UnitOfWorkBean uow = state.getDefaultUnitOfWork();					
        if(rolesForm != null){
        	ArrayList selectedItems = rolesForm.getSelectedItems();
        	if(selectedItems == null || selectedItems.size() == 0){
        		String args[] = new String[0];							
    			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
    			throw new UserException(errorMsg,new Object[0]);
        	}
        	for(int i = 0; i < selectedItems.size(); i++){
        		BioBean role = (BioBean)selectedItems.get(i);
        		String roleName = (String)role.get("user_role_name");
        		_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Deleteing Role "+roleName+" From LDAP...",100L);        		  
//        		boolean didLDAPDeleteSucceed = true;
        		boolean didMetaDataDeleteSucceed = true;
/*				try {
					UserManagementService ums = UMSHelper.getUMS();					
					RoleManager rolemanager=ums.getDefaultRoleManager();
					if(rolemanager.getRole(roleName) != null){
						rolemanager.deleteRole(roleName);
						_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","LDAP Delete Successful...",100L);						
					}
					else
						_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Role Not Found In LDAP...",100L);						
					
					_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Deleteing Role "+roleName+" From Meta Data...",100L);							
*/
					try {
						role.delete();
						//UPDATE: DEFECT 280469 - Delete does not persist to sso_role table
						Query qry = new Query("sso_role","sso_role.sso_role_name = '"+roleName+"'","");
						BioCollectionBean bcBean = uow.getBioCollectionBean(qry);
						BioBean ssoRole = (BioBean)bcBean.elementAt(0);
						ssoRole.delete();
						//END UPDATE
						uow.saveUOW();
					} catch (Exception e) {
						didMetaDataDeleteSucceed = false;
						e.printStackTrace();
					} 
					
/*				} catch (Exception e) {		
					didLDAPDeleteSucceed = false;
					e.printStackTrace();
				}
*/	
/*				if(!didMetaDataDeleteSucceed){
					try {
						_log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Rolling back LDAP delete...",100L);						
						UserManagementService ums = UMSHelper.getUMS();					
						RoleManager rolemanager=ums.getDefaultRoleManager();
						Role ldaprole=new Role(roleName);
						ldaprole.setDescription("");
						rolemanager.addRole(ldaprole);	
					} catch (Exception e) {								
						e.printStackTrace();
					} 
				}
*/
//				if(!didMetaDataDeleteSucceed || !didLDAPDeleteSucceed){
				if(!didMetaDataDeleteSucceed){
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_DELETE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}				
        	}    
        	rolesForm.setSelectedItems(null);
        }		
        _log.debug("LOG_DEBUG_EXTENSION_WMSECROLELISTDEL","Leaving WmSecurityRolesSaveAction",100L);		
        return RET_CONTINUE;
    }
}
