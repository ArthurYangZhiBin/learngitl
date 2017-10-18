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
package com.infor.scm.waveplanning.wp_query_builder.action;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WPQueryBuilderBack extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderBack.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderSaveQuery",100L);		
		StateInterface state = context.getState();
		String interactionId = context.getState().getInteractionId();				
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		Query qry = new Query("querybuildertemp","querybuildertemp.ISHEADER = '1' AND querybuildertemp.INTERACTIONID = '"+interactionId+"'","");		
		BioCollection bc = uow.getBioCollectionBean(qry);
		try {
			if(bc != null && bc.size() > 0){
				result.setFocus((DataBean)bc.elementAt(0));
			}
			else{
				result.setFocus(uow.getQBEBioWithDefaults("querybuildertemp"));
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;	
	}
}
