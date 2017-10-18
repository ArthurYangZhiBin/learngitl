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

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.NavigationDef;
import com.epiphany.shr.metadata.objects.generated.np.Navigation;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
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


public class AddToFavoritesFromModalAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddToFavoritesFromModalAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Executing AddToFavoritesFromModalAction",100L);		
		StateInterface state = context.getState();					
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		String uid = UserUtil.getUserId(context.getState());
		String dbName = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);	
		try {
			LoginNavigationPicker.setIntoUserContextAndSession(context,"ENTERPRISE");
		} catch (Exception e) {
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);					
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		RuntimeListFormInterface form = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),context.getSourceWidget().getForm().getName(),"wm_ws_defaults_popup_list_form",state);
		try {
			ArrayList selectedItems = form.getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0){
				for(int i = 0; i < selectedItems.size(); i++){
					BioBean bio = (BioBean)selectedItems.get(i);
					Query qry = new Query("userfavorites","userfavorites.USERID = '"+uid+"' AND userfavorites.SCREEN = '"+bio.getString("SCREENCODE")+"'","");
					BioCollection bioCollection = uow.getBioCollectionBean(qry);					
					if(bioCollection != null && bioCollection.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);			
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_FAV_NON_UNIQUE",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
					uow.clearState();					
					_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Saving Fav...",100L);
					QBEBioBean newFav = uow.getQBEBioWithDefaults("userfavorites");
					newFav.set("USERID",uid);
					newFav.set("SCREEN",bio.getString("SCREENCODE"));
					newFav.save();
					uow.saveUOW(true);
					
				}
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			e.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);			
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}  
		finally{
			try {
				LoginNavigationPicker.setIntoUserContextAndSession(context,dbName);
			} catch (Exception e) {
				e.printStackTrace();
				_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);			
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}	
		}
		_log.debug("LOG_DEBUG_EXTENSION_ADDTOFAVFROMMOD","Leaving AddToFavoritesFromModalAction",100L);
		return RET_CONTINUE;
		
	}	
}