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
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.UserUtil;


public class GetUnselectedFavorite extends ActionExtensionBase{
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
				
		StateInterface state = context.getState();
		String uid = UserUtil.getUserId(context.getState());
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		Query loadBiosQry = new Query("userfavorites","userfavorites.USERID = '"+uid+"'","");
		BioCollectionBean bioCollection = uow.getBioCollectionBean(loadBiosQry);
		String qryStr = "screens.CANBEFAVORITE = '1' ";
		try {
			if(bioCollection != null && bioCollection.size() > 0){
				for(int i = 0; i < bioCollection.size(); i++){
					Bio bio = bioCollection.elementAt(i);					
					qryStr += " AND screens.SCREENCODE != '"+bio.getString("SCREEN").toUpperCase()+"'";				
				}
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		loadBiosQry = new Query("screens",qryStr,"");
		bioCollection = uow.getBioCollectionBean(loadBiosQry);
		result.setFocus(bioCollection);			
		return RET_CONTINUE;
		
	}	
}