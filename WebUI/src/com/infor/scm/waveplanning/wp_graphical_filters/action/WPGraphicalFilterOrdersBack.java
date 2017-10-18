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
package com.infor.scm.waveplanning.wp_graphical_filters.action;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class WPGraphicalFilterOrdersBack extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterOrdersBack.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Executing WPGraphicalFilterOrdersBack",100L);		
		StateInterface state = context.getState();													
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();				
		
		//Clear out querybuildertemp table for this interaction id	
		BioCollection bc = null;		
		Query qry = new Query("wp_querybuilderdetail","wp_querybuilderdetail.INTERACTIONID = '"+state.getInteractionId()+"'","");
		bc = uow.getBioCollectionBean(qry);				
		try {
			if(bc != null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","bc.size():"+bc.size(),100L);
				for(int i = 0; i < bc.size(); i++){							
					bc.elementAt(i).delete();
				}
				uow.saveUOW(false);
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		qry = new Query("wp_graphicalfilter_temp", "wp_graphicalfilter_temp.INTERACTIONID = '" + state.getInteractionId() + "' AND wp_graphicalfilter_temp.DODISPLAY = '1'", "wp_graphicalfilter_temp.ADDITIONALFILTER ASC");
		result.setFocus(uow.getBioCollectionBean(qry));
		return RET_CONTINUE;	
	}
}
