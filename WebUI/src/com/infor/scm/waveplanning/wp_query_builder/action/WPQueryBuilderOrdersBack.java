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

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WPQueryBuilderOrdersBack extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderOrdersBack.class);

	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRORDERBACK","Executing WPQueryBuilderOrdersBack",100L);
		StateInterface state = context.getState();
//		String interactionId = state.getInteractionId();
		String interactionId = "";
		Object uniqueIdObj = state.getRequest().getSession().getAttribute(WPQueryBuilderNewTempRecord.HAS_DEFAULT_FILTER);
		if(uniqueIdObj != null){//user has default filter
			interactionId = uniqueIdObj.toString();
		}else{
			interactionId = context.getState().getInteractionId();
		}

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		Query qry = new Query("querybuildertemp","querybuildertemp.INTERACTIONID = '"+interactionId+"'","");
		BioBean newFocus = null;
		try{
			newFocus = uow.getBioCollectionBean(qry).get("0");
		}catch(EpiDataException e){
			throw new UserException("WMEXP_QUERY_FAILED", new Object[] {});
		}
		result.setFocus(newFocus);
		return RET_CONTINUE;
	}
}
