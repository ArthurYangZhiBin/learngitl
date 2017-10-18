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
package com.ssaglobal.scm.wms.wm_ws_defaults.favorites;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultPersistScreensInContext;


public class FavoritesListPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FavoritesListPreRender.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","Executing FavoritesListPreRender",100L);		
		StateInterface state = context.getState();					
		HttpSession session = state.getRequest().getSession();
		List profiles = context.getServiceManager().getUserContext().getUserProfileIDs();
		List profilesStr = context.getServiceManager().getUserContext().getUserProfiles();
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","User Profile IDs:"+profiles,100L);
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","User Profiles:"+profilesStr,100L);
		ArrayList addFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_ADD_FAV); 
		ArrayList delFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_REM_FAV);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		String uid = UserUtil.getUserId(context.getState());
				
		//Get user selected favorites and display these regarless of privaleges.
		Query qry = new Query("userfavorites","userfavorites.USERID = '"+uid+"'","");
		BioCollectionBean bioCollection = uow.getBioCollectionBean(qry);
		ArrayList selectedFavorites = new ArrayList();
		ArrayList viewableFavorites = new ArrayList();
		for(int i = 0; i < bioCollection.size(); i++){
			Bio bio = bioCollection.elementAt(i);
			selectedFavorites.add(bio.getString("SCREEN"));
			viewableFavorites.add(bio.getString("SCREEN"));
		}
		
		if(!SecurityUtil.isAdmin(state)){
			
			//Build FAVORITESPROFILEMAPPING table query and get records for this user's profiles
			String bioQryStr = "";		
			Iterator profileItr = profiles.iterator();
			while(profileItr.hasNext()){
				if(bioQryStr.length() == 0)
					bioQryStr += "wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
				else
					bioQryStr += " OR wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
			}
			_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","FAVORITESPROFILEMAPPING QRY:"+bioQryStr,100L);
			bioCollection = uow.getBioCollectionBean(new Query("wm_favoritesprofilemapping",bioQryStr,""));
			if(bioCollection != null){
				for(int i = 0; i < bioCollection.size(); i++){
					Bio bio = bioCollection.elementAt(i);
					String screenId = bio.get("SCREENID").toString();
					if(!viewableFavorites.contains(screenId)){
						viewableFavorites.add(screenId);
					}
				}
			}									
		}
		
		if(bioCollection != null)
			_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","screens size:"+bioCollection.size(),100L);
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","Got Selected Favs:"+selectedFavorites,100L);		
		if(form.getFocus().isBioCollection()){
			BioCollectionBean focus = (BioCollectionBean)form.getFocus();
			if(focus != null && focus.size() > 0){
				for(int i = 0; i < focus.size(); i++){
					Bio bio = focus.elementAt(i);
					boolean didSet = false;
					if(addFavs != null){
						if(addFavs.contains(bio.getString("SCREENCODE"))){
							bio.set("ISSELECTED","1");
							didSet = true;
						}
					}
					if(!didSet && delFavs != null){
						if(delFavs.contains(bio.getString("SCREENCODE"))){
							bio.set("ISSELECTED","0");
							didSet = true;
						}
					}
					if(!didSet){
						if(selectedFavorites.contains(bio.getString("SCREENCODE")))
							bio.set("ISSELECTED","1");
						else
							bio.set("ISSELECTED","0");
					}
				}
			}		
		}
		else{
			if(SecurityUtil.isAdmin(state)){
				qry = new Query("screens","","");
				bioCollection = uow.getBioCollectionBean(qry);
				if(bioCollection != null && bioCollection.size() > 0){
					for(int i = 0; i < bioCollection.size(); i++){
						Bio bio = bioCollection.elementAt(i);
						if(selectedFavorites.contains(bio.getString("SCREENCODE")))
							bio.set("ISSELECTED","1");
						else
							bio.set("ISSELECTED","0");
					}
				}
				form.setFocus((DataBean)bioCollection);
			}
			else{
//				Reset form focus to subset of favorites that are viewable by the user
				String bioQryStr = "";
				for(int i = 0; i < viewableFavorites.size(); i++){
					if(bioQryStr.length() == 0)
						bioQryStr += "screens.SCREENCODE = '"+viewableFavorites.get(i)+"'";
					else
						bioQryStr += " OR screens.SCREENCODE = '"+viewableFavorites.get(i)+"'";
				}
				bioCollection = uow.getBioCollectionBean(new Query("screens",bioQryStr,""));	
				if(bioCollection != null && bioCollection.size() > 0){
					for(int i = 0; i < bioCollection.size(); i++){
						Bio bio = bioCollection.elementAt(i);
						if(selectedFavorites.contains(bio.getString("SCREENCODE")))
							bio.set("ISSELECTED","1");
						else
							bio.set("ISSELECTED","0");
					}
				}
				form.setFocus(bioCollection);							
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","Exiting FavoritesListPreRender",100L);		
		return RET_CONTINUE;
	}
	
}