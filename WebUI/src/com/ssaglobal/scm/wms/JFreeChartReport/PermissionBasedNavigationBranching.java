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
package com.ssaglobal.scm.wms.JFreeChartReport;

//Import 3rd party packages and classes
import java.util.List;

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class PermissionBasedNavigationBranching extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result){
		//Switch navigation based on permission
		StateInterface state = context.getState();
		String permission = getParameterString("PERMISSION");
		String interactionNav = getParameterString("INTERACTION_NAV");
		String noChangeNav = getParameterString("NO_CHANGE_NAV");
		List userProfiles = state.getServiceManager().getUserContext().getUserProfiles();
		
		if(userProfiles.contains(permission) || SecurityUtil.isAdmin(state)){
			//If permission enabled, navigate to chart
			context.setNavigation(interactionNav);
		}else{ 
			//Else, No Change navigation
			context.setNavigation(noChangeNav);
		}
		return RET_CONTINUE;
	}
}