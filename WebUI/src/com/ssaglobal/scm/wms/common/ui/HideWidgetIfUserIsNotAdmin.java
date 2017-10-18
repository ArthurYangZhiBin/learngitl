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

package com.ssaglobal.scm.wms.common.ui;


import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.SecurityUtil;


public class HideWidgetIfUserIsNotAdmin extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(HideWidgetIfUserIsNotAdmin.class);


	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
	{									
		if(SecurityUtil.isAdmin(state)){
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
		}
		else{						
			widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
		}
		return RET_CONTINUE;

	}

}
