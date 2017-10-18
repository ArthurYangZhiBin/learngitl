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
package com.ssaglobal.scm.wms.wms_app_security.ButtonSwitch;

import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.ui.view.customization.MenuExtensionBase;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_ums.SSOConfigSingleton;

public class MenuPrerender extends MenuExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MenuPrerender.class);
	public MenuPrerender()
	{
	}
	
	protected int execute(StateInterface state, RuntimeMenuInterface menu)  {
		if(isAdsDirectoryServer()){		
				menu.setProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
		}
		return RET_CONTINUE;
	}

	
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem){
		if(isAdsDirectoryServer()){
			menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
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
}
