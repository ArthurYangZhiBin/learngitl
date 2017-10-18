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
package com.ssaglobal.scm.wms.navigation;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.MenuExtensionBase;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;

public class VersionControlledPermissions extends MenuExtensionBase{
	protected int execute(StateInterface state, RuntimeMenuInterface menu){
		if(toHide(state))
			menu.setProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
		else
			menu.setProperty(RuntimeMenuInterface.PROP_HIDDEN, false);
		return RET_CONTINUE;
	}
	
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem){
		if(toHide(state))
			menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
		else
			menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
		return RET_CONTINUE;
	}
	
	boolean toHide(StateInterface state){
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String qryStmt = "wm_system_settings.CONFIGKEY='INFORWMS_ENTERPRISE'";
		Query query = new Query("wm_system_settings", qryStmt , null);
		BioCollectionBean results = uowb.getBioCollectionBean(query);
		try{
			if(results.size()>0){
				if(results.elementAt(0).get("NSQLVALUE").toString().equals("1"))
					return false;
				else
					return true;
			}else
				return true;
		}catch(Exception e){
			return true;
		}
	}
}