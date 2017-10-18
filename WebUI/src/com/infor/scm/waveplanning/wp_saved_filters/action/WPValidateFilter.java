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
package com.infor.scm.waveplanning.wp_saved_filters.action;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.text.NumberFormat;
import java.text.ParseException;

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


public class WPValidateFilter extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPValidateFilter.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Executing WPValidateQueryBuilder",100L);			
		StateInterface state = context.getState();			
		//Get filter header form
		RuntimeFormInterface filterHeaderForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_saved_filters_filter_header",state);				
		if(filterHeaderForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Found Filter Header Form:"+filterHeaderForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Found Filter Header Form Form:Null",100L);			
					
		//Validate initial form if present		
		if(filterHeaderForm != null){
			String ordersMax = filterHeaderForm.getFormWidgetByName("MAXORDERS").getDisplayValue();
			String cubeMax = filterHeaderForm.getFormWidgetByName("MAXCUBE").getDisplayValue();
			String orderLinesMax = filterHeaderForm.getFormWidgetByName("MAXORDERLINES").getDisplayValue();
			String weightMax = filterHeaderForm.getFormWidgetByName("MAXWEIGHT").getDisplayValue();
			String cases = filterHeaderForm.getFormWidgetByName("MAXCASES").getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget MAXORDERS:"+ordersMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget MAXORDERLINES:"+orderLinesMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget MAXWEIGHT:"+weightMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget MAXCUBE:"+cubeMax,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Got following value for widget MAXCASES:"+cases,100L);
			
			String failedWidgetLabels = "";		

			if(!isPositiveInteger(ordersMax,state)){			
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget ORDERSMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(filterHeaderForm.getFormWidgetByName("MAXORDERS"), failedWidgetLabels, state);				
			}
			if(!isPositiveInteger(cubeMax,state)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget CUBEMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(filterHeaderForm.getFormWidgetByName("MAXCUBE"), failedWidgetLabels, state);
			}
			if(!isPositiveInteger(orderLinesMax,state)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget ORDERLINESMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(filterHeaderForm.getFormWidgetByName("MAXORDERLINES"), failedWidgetLabels, state);
			}
			if(!isPositiveInteger(weightMax,state)){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget WEIGHTMAX failed validation",100L);
				failedWidgetLabels = appendWidgetLabelToErrorMessage(filterHeaderForm.getFormWidgetByName("MAXWEIGHT"), failedWidgetLabels, state);
			}
			if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000)){
				if(!isPositiveInteger(cases,state)){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Widget CASES failed validation",100L);
					failedWidgetLabels = appendWidgetLabelToErrorMessage(filterHeaderForm.getFormWidgetByName("MAXCASES"), failedWidgetLabels, state);
				}
			}

			failedWidgetLabels = formatErrorMessage(failedWidgetLabels);			
			if(failedWidgetLabels.length() > 0){
				String args[] = new String[1]; 				
				args[0] = failedWidgetLabels;								
				String errorMsg = getTextMessage("WPEXP_FIELDS_NON_NEG_INT",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			
			if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000)){
				if(filterHeaderForm.getFormWidgetByName("RFID_STND").getValue() == null){					
					String args[] = new String[0]; 																	
					String errorMsg = getTextMessage("WPEXP_INCLUDE_RF_REQ",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);					
				}
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Exiting WPValidateQueryBuilder",100L);
		return RET_CONTINUE;
		
		
	}	
	private boolean isPositiveInteger(String value, StateInterface state){
		NumberFormat nf = NumberFormat.getInstance(state.getLocale().getJavaLocale());
		nf.setGroupingUsed(true);
		Number valueNumber = null;
		if(value != null){
			try {
				valueNumber = nf.parse(value);
			} catch (ParseException e1) {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Value is not an integer... returning false",100L);
				return false;
			}
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEQRYBLDR","Entering isPositiveInteger()...",100L);			
			try {
				if(Integer.parseInt(valueNumber.toString()) < 0){
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