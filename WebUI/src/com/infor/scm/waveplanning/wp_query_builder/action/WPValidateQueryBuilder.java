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
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;


public class WPValidateQueryBuilder extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPValidateQueryBuilder.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Executing WPValidateQueryBuilder",100L);			
		StateInterface state = context.getState();	
		
		//Get initial form
		RuntimeFormInterface initialForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_initial_screen",state);				
		if(initialForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Found Initial Form:"+initialForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Found Initial Form Form:Null",100L);			
					
		//Validate initial form if present		
		if(initialForm != null){
			String ordersMax = initialForm.getFormWidgetByName("ORDERSMAX").getDisplayValue();
			String cubeMax = initialForm.getFormWidgetByName("CUBEMAX").getDisplayValue();
			String orderLinesMax = initialForm.getFormWidgetByName("ORDERLINESMAX").getDisplayValue();
			String weightMax = initialForm.getFormWidgetByName("WEIGHTMAX").getDisplayValue();
			String cases = initialForm.getFormWidgetByName("CASES").getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget ORDERSMAX:"+ordersMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget ORDERLINESMAX:"+orderLinesMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget WEIGHTMAX:"+weightMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget CUBEMAX:"+cubeMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget CASES:"+cases,100L);
			
			String failedWidgetLabels = "";					
			if(!isPositiveInteger(ordersMax)){			
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget ORDERSMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(initialForm.getFormWidgetByName("ORDERSMAX"), failedWidgetLabels, state);				
			}
			if(!isPositiveInteger(cubeMax)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget CUBEMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(initialForm.getFormWidgetByName("CUBEMAX"), failedWidgetLabels, state);
			}
			if(!isPositiveInteger(orderLinesMax)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget ORDERLINESMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(initialForm.getFormWidgetByName("ORDERLINESMAX"), failedWidgetLabels, state);
			}
			if(!isPositiveInteger(weightMax)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget WEIGHTMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(initialForm.getFormWidgetByName("WEIGHTMAX"), failedWidgetLabels, state);
			}
			if(!isPositiveInteger(cases)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget CASES failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(initialForm.getFormWidgetByName("CASES"), failedWidgetLabels, state);
			}

			failedWidgetLabels = formatErrorMessage(failedWidgetLabels);			
			if(failedWidgetLabels.length() > 0){
				String args[] = new String[1]; 				
				args[0] = failedWidgetLabels;								
				String errorMsg = getTextMessage("WPEXP_FIELDS_NON_NEG_INT",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			
			if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000)){
				if(initialForm.getFormWidgetByName("INCLUDERF").getValue() == null){					
					String args[] = new String[0]; 																	
					String errorMsg = getTextMessage("WPEXP_INCLUDE_RF_REQ",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);					
				}
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Exiting WPValidateQueryBuilder",100L);
		return RET_CONTINUE;
		
		
	}	
	private boolean isPositiveInteger(String value){
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Entering isPositiveInteger()...",100L);
		if(value != null){
			try {
				if(Integer.parseInt(value) < 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Value is negative... returning false",100L);
					return false;
				}
			} catch (NumberFormatException e) {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Value is not an integer... returning false",100L);
				return false;
			}
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Value is null... returning true",100L);
		}		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Exiting isPositiveInteger()...",100L);
		return true;
	}
	
	private String appendWidgetLabelToErrorMessage(RuntimeFormWidgetInterface widget, String message, StateInterface state){		
		message += widget.getLabel("label", state.getUser().getLocale()) + "     ";		
		return message;
	}
	private String formatErrorMessage(String message){
		message = message.trim();
		message = message.replaceAll("     ", ",");
		return message;
	}
}