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
package com.infor.scm.waveplanning.wp_graphical_filters.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_graphical_filters.action.WPGraphicalFilterProceed;



public class WPGraphFilterChartTypePrerender extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	   protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException {

 
		   
  	   		StateInterface state = context.getState();  	   		  	   		
  	   	    RuntimeFormWidgetInterface chartTypeWidget = form.getFormWidgetByName("CHARTTYPE");  	   	   
		    Object dropdownValue = WPUserUtil.getInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_GRAPH_TYPE, state);
		   
		    if(dropdownValue != null){
		    	chartTypeWidget.setValue(dropdownValue.toString());		    	   
		    }
		    			
	  return RET_CONTINUE;
	

	   }
}