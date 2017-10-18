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
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class WPGraphicalFilterChartTypeOnChange extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterChartTypeOnChange.class);
	public static String SESSION_KEY_GRAPH_QRY_BASE_CRITERION = "session.key.grph.fltr.base";
	public static String SESSION_KEY_GRAPH_QRY_GRAPH_TYPE = "session.key.grph.fltr.type";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);		
		StateInterface state = context.getState();							
		
		//get widgets						
		RuntimeFormWidgetInterface graphTypeWidget = context.getSourceWidget();
		
		//get widget values		
		String graphType = (String)graphTypeWidget.getValue();		
		
		//Set values in interaction session		
		WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_GRAPH_QRY_GRAPH_TYPE, graphType, state);
				
				
		return RET_CONTINUE;	
	}
		 
}
