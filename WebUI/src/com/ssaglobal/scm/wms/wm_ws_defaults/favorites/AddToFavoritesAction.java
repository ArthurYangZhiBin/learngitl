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

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.NavigationDef;
import com.epiphany.shr.metadata.objects.generated.np.Navigation;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;


public class AddToFavoritesAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddToFavoritesAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing AddToFavoritesAction",100L);		
		StateInterface state = context.getState();								
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		
		//Get current interaction's screen code from table and make sure it is avaliable as a favorite
		String screenCode = InteractionNameScreenTable.getScreenCodeForCurrInteraction(context);
		if(screenCode == null){
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Current interaction is not avaliable as a favorite...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);					
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SCREEN_NOT_AVAL_AS_FAV",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		//defect1076.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;

		if (screenCode.equalsIgnoreCase("ITEMENT") && (!isEnterprise)){
			screenCode = "ITEM";
		}
		//defect1076.e
		Query qry = new Query("screens","screens.CANBEFAVORITE = '1' AND screens.SCREENCODE = '"+screenCode.toUpperCase()+"'","");
		BioCollection bioCollection = uow.getBioCollectionBean(qry);
		try {
			if(bioCollection == null || bioCollection.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Current interaction is not avaliable as a favorite...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);			
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SCREEN_NOT_AVAL_AS_FAV",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		String uid = UserUtil.getUserId(context.getState());
		String dbName = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);	
		if(!dbName.equalsIgnoreCase("enterprise")){
			context.setNavigation("menuClickEvent358");
			try {
				LoginNavigationPicker.setIntoUserContextAndSession(context,"ENTERPRISE");
			} catch (Exception e) {
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);			
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		else{
			context.setNavigation("menuClickEvent359");
		}
		qry = new Query("userfavorites","userfavorites.USERID = '"+uid+"' AND userfavorites.SCREEN = '"+screenCode.toUpperCase()+"'","");
		bioCollection = uow.getBioCollectionBean(qry);
		try {
			if(bioCollection != null && bioCollection.size() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);				
				return RET_CANCEL;
			}
			uow.clearState();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Saving Fav...",100L);			
			QBEBioBean newFav = uow.getQBEBioWithDefaults("userfavorites");
			newFav.set("USERID",uid);
			newFav.set("SCREEN",screenCode);
			newFav.save();
			uow.saveUOW(true);
		} catch (EpiException e) {
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		finally{
			try {
				if(!dbName.equalsIgnoreCase("enterprise")){
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","reseting DB2!!",100L);					
					LoginNavigationPicker.setIntoUserContextAndSession(context,dbName);
				}
			} catch (Exception e) {
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);			
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}	
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving AddToFavoritesAction",100L);
		return RET_CONTINUE;
		
	}	
}