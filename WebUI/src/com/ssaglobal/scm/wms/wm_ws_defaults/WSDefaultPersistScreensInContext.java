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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;

public class WSDefaultPersistScreensInContext extends SaveAction{
	public static final String SESSION_KEY_ADD_FAV = "session.key.add.fav";
	public static final String SESSION_KEY_REM_FAV = "session.key.rem.fav";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSDefaultPersistScreensInContext.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFPERFAV","Executing WSDefaultHeaderDetailSave",100L);		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
		ArrayList tabList = new ArrayList();				
		tabList.add("tab 3");
		RuntimeListFormInterface favoritesListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_favorites_list_form",tabList,state);
		
		if(favoritesListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFPERFAV","Found Favorites List Form:"+favoritesListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFPERFAV","Found Favorites List Form:Null",100L);			
			
		//If user has changed favorites then save changes here		
		try {
			if(favoritesListForm != null && favoritesListForm.getFocus().isBioCollection()){														
				BioCollectionBean favorites = (BioCollectionBean)favoritesListForm.getFocus();
				if(favorites != null && favorites.size() > 0){
					ArrayList addFavs = new ArrayList();
					ArrayList delFavs = new ArrayList();
					if(session.getAttribute(SESSION_KEY_ADD_FAV) != null)
						addFavs = (ArrayList)session.getAttribute(SESSION_KEY_ADD_FAV);
					if(session.getAttribute(SESSION_KEY_REM_FAV) != null)
						delFavs = (ArrayList)session.getAttribute(SESSION_KEY_REM_FAV);
					for(int i = 0; i < favorites.size(); i++){
						com.epiphany.shr.data.bio.Bio favorite = favorites.elementAt(i);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFPERFAV","SCREENCODE:"+favorite.getString("SCREENCODE"),100L);						
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFPERFAV","ISSELECTED:"+favorite.getString("ISSELECTED"),100L);						
						if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("1")){
							if(!addFavs.contains(favorite.getString("SCREENCODE")))
								addFavs.add(favorite.getString("SCREENCODE"));							
						}
						if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("0")){
							if(!delFavs.contains(favorite.getString("SCREENCODE")))
								delFavs.add(favorite.getString("SCREENCODE"));							
						}
					}
					for(int i = 0; i < delFavs.size(); i++){
						if(addFavs.contains(delFavs.get(i))){
							addFavs.remove(delFavs.get(i));
						}
					}
					session.setAttribute(SESSION_KEY_ADD_FAV,addFavs);
					session.setAttribute(SESSION_KEY_REM_FAV,delFavs);
				}
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 
										
		return RET_CONTINUE;	
	}
}
