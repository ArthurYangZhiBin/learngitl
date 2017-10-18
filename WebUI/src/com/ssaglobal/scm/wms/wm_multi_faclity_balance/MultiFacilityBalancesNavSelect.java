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
package com.ssaglobal.scm.wms.wm_multi_faclity_balance;

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class MultiFacilityBalancesNavSelect extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MultiFacilityBalancesNavSelect.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Executing MultiFacilityBalancesNavSelect",100L);		
		StateInterface state = context.getState();		
		HttpSession session = state.getRequest().getSession();	
		String storerFilter = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_STORER_KEY);
		String selectedLevel = (String)session.getAttribute(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_KEY);
		if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIVISION)){
			if(storerFilter == null || storerFilter.length() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 1...",100L);				
				context.setNavigation("menuClickEvent409");
			} else {
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 2..",100L);				
				context.setNavigation("menuClickEvent412");
			}
		} else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_DIST_CENTER)){
			if(storerFilter == null || storerFilter.length() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 3..",100L);
				context.setNavigation("menuClickEvent410");
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 4..",100L);				
				context.setNavigation("menuClickEvent413");
			}			
		} else if(selectedLevel.equals(MultiFacilityBalancesSearch.MULTI_FAC_BAL_SELECTED_LEVEL_ENT)){
			if(storerFilter == null || storerFilter.length() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 5..",100L);
				context.setNavigation("menuClickEvent566");
			} else {
				_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Setting Nav 6..",100L);				
				context.setNavigation("menuClickEvent567");
			}			
		} else {
			if(storerFilter == null || storerFilter.length() == 0){
				context.setNavigation("menuClickEvent328");
			} else {
				context.setNavigation("menuClickEvent411");
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_MULTIFACBALNAVSEL","Exiting MultiFacilityBalancesNavSelect",100L);		
		return RET_CONTINUE;
	}			
}