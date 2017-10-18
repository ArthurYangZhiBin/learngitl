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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class WPGraphicalFilterSave extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterSave.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);		
		StateInterface state = context.getState();
		RuntimeListFormInterface graphicalAdditionalFilterForm = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_graphical_filters_charts_add_filter_list",state);						
		if(graphicalAdditionalFilterForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Additional Filter Form:"+graphicalAdditionalFilterForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found Graphical Filter Additional Filter Form:Null",100L);			
		
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		if(graphicalAdditionalFilterForm != null){
			QBEBioBean newRecord = graphicalAdditionalFilterForm.getQuickAddRowBean();
			if(newRecord != null && !newRecord.isEmpty()){
				newRecord.set("USERID", WPUserUtil.getUserId(state));
				newRecord.set("INTERACTIONID", state.getInteractionId());
			}
		}
		
		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
				
		return RET_CONTINUE;	
	}
}
