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


package com.ssaglobal.scm.wms.multifacilityt_balances_charts;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.ssaglobal.scm.wms.wm_multi_faclity_balance.MultiFacilityBalancesNavSelect;
import com.ssaglobal.scm.wms.wm_multi_faclity_balance.MultiFacilityBalancesSearch;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class MultiFacilityBalancesSlotSelect extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesSlotSelect.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Executing MultiFacilityBalancesNavSelect",100L);		
		StateInterface state = context.getState();		
		HttpSession session = state.getRequest().getSession();	
//		String storerFilter = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY);
		String selectedLevel = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY);
		if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION)){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 1...",100L);				
				context.setNavigation("clickEvent1112");
		}
		else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER)){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 3..",100L);
				context.setNavigation("clickEvent1113");
		}
		else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_ENT)){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 3..",100L);
				context.setNavigation("clickEvent1018");
		}
		else{
				context.setNavigation("clickEvent1114");
		}
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Exiting MultiFacilityBalancesNavSelect",100L);		
		return RET_CONTINUE;
		
		
	}			
}