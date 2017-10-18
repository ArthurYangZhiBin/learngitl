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
package com.infor.scm.waveplanning.wp_saved_filters.ui;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;


public class SavedFiltersFilterHeaderFormPreRender extends FormExtensionBase{	
	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		
		if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000)) {
			form.getFormWidgetByName("MAXCASES").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
			form.getFormWidgetByName("RFID_STND").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
		}
		else if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000)){
			form.getFormWidgetByName("MAXCASES").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			form.getFormWidgetByName("RFID_STND").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
		}
		
		return RET_CONTINUE;
	}
	
	
}