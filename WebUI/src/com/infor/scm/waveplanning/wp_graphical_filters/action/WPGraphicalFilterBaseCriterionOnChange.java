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
import java.sql.SQLException;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_graphical_filters.util.WPGraphFilterUtil;


public class WPGraphicalFilterBaseCriterionOnChange extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterBaseCriterionOnChange.class);
	public static String SESSION_KEY_GRAPH_QRY_BASE_CRITERION = "session.key.grph.fltr.base";	
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterBaseCriterionOnChange",100L);		
		StateInterface state = context.getState();
			
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		//get widgets				
		RuntimeFormWidgetInterface baseCriterionWidget = context.getSourceWidget();		
		
		//get widget values
		String baseCriterion = (String)baseCriterionWidget.getValue();		
		ArrayList selectedCriterion = new ArrayList();
		selectedCriterion.add(baseCriterion);		
		
		//Set values in interaction session
		WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_GRAPH_QRY_BASE_CRITERION, selectedCriterion, state);		
				
		//Clean Temp Table				
		try {
			WPGraphFilterUtil.cleanTempTable(state, state.getInteractionId());
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while Cleaning Temp Table...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while Cleaning Temp Table...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//Load temp table
		try{
			WPGraphFilterUtil.loadTempTable(baseCriterion, state);		
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (SQLException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getMessage(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		Query qry = new Query("wp_graphicalfilter_temp", "wp_graphicalfilter_temp.INTERACTIONID = '" + state.getInteractionId() + "' AND wp_graphicalfilter_temp.DODISPLAY = '1'", "wp_graphicalfilter_temp.ADDITIONALFILTER ASC");
		result.setFocus(uow.getBioCollectionBean(qry));
		return RET_CONTINUE;	
	}		
}
