package com.ssaglobal.scm.wms.wms_app_security.ButtonSwitch;

import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;




public class ValidateSSOSetNav extends ActionExtensionBase
{
    private static ILoggerCategory _log = LoggerFactory.getInstance(ValidateSSOSetNav.class);
  
    protected int execute(ActionContext context, ActionResult result) throws EpiException
    {
    	UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
    	String menuItem = context.getActionObject().getName().toString();
    	if (menuItem.equalsIgnoreCase("wm_enterprise_user_management")){
        	if(isAdsDirectoryServer()){
        		Query loadBiosQry = new Query("user_data","","");
        		setBC(uowb.getBioCollectionBean(loadBiosQry), result);
        		context.setNavigation("menuClickEvent828");
        	}else{
        		Query loadBiosQry = new Query("sso_user","","");
        		setBC(uowb.getBioCollectionBean(loadBiosQry), result);
        		context.setNavigation("menuClickEvent643");
        	}
    	}else if(menuItem.equalsIgnoreCase("wm_enterprise_support_setup_permissions_menuitem")) {
//    		if(isAdsDirectoryServer()){
//        		Query loadBiosQry = new Query("user_role","user_role.user_role_name != 'administrator' AND user_role.user_role_name != 'superadministrator'","");
//        		setBC(uowb.getBioCollectionBean(loadBiosQry), result);
//        		context.setNavigation("menuClickEvent829");
//        	}else{
//        		Query loadBiosQry = new Query("wm_meta_user_role","wm_meta_user_role.user_role_name != 'administrator' AND wm_meta_user_role.user_role_name != 'superadministrator'","");
//        		setBC(uowb.getBioCollectionBean(loadBiosQry), result);
//        		context.setNavigation("menuClickEvent410");
//       		}
    		Query loadBiosQry = new Query("wm_meta_user_role","wm_meta_user_role.user_role_name != 'administrator' AND wm_meta_user_role.user_role_name != 'superadministrator'","");
    		setBC(uowb.getBioCollectionBean(loadBiosQry), result);
    		context.setNavigation("menuClickEvent410");
    	}

        return RET_CONTINUE;
    }
	private boolean isAdsDirectoryServer(){
		SSOConfigSingleton ssoConfig = SSOConfigSingleton.getSSOConfigSingleton();
		if("ADS_Directory_Specification".equalsIgnoreCase(ssoConfig.getDirectoryServerType())){
			return true;
		}else{
			return false;
		}
	}
	private void setBC(BioCollectionBean bioCollection, ActionResult result){
		bioCollection.setEmptyList(false);
		result.setFocus(bioCollection);
	}
}
