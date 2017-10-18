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
package com.ssaglobal.scm.wms.shared_app;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuElementInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemFactory;
import com.epiphany.shr.ui.view.customization.MenuExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.UserUtil;

public class FavoritesMenuPreRender extends MenuExtensionBase{

	public FavoritesMenuPreRender(){
	}
	protected int execute(StateInterface state, RuntimeMenuInterface menu) throws EpiException {
		String uid = UserUtil.getUserId(state);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();														
		HttpSession session = state.getRequest().getSession();
		String dbName = (String)session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);	
		if(dbName == null)
			return RET_CONTINUE;
		if(dbName.equalsIgnoreCase("enterprise")){
			Query loadBiosQryA = new Query("userfavorites", "userfavorites.USERID = '"+uid+"' AND userfavorites.SCREENISENT = '1'", "userfavorites.SCREENDESC");																			
			menu.setFocus(uow.getBioCollectionBean(loadBiosQryA));
		}
		else{
			Query loadBiosQryA = new Query("userfavorites", "userfavorites.USERID = '"+uid+"' AND userfavorites.SCREENISWHSE = '1'", "userfavorites.SCREENDESC");																			
			menu.setFocus(uow.getBioCollectionBean(loadBiosQryA));
		}
		return RET_CONTINUE;
	}
		
	
	
}