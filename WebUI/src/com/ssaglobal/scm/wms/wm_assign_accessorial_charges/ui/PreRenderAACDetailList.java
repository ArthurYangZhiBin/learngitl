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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PreRenderAACDetailList extends FormExtensionBase{
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderAACDetailList.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException
    {
	  	 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing PreRenderAACDetailList",100L);
	  	
	  	//StateInterface state = context.getState();
		DataBean focus= context.getState().getFocus();
		if(focus instanceof BioBean)
		{
			focus = (BioBean)focus;
		}
		Object lineNum = focus.getValue("ORDERLINENUMBER");
		if (lineNum !=null)
		{
			String line = lineNum.toString();
			_log.debug("LOG_SYSTEM_OUT","***Line Num: ***" +line,100L);
		}
		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting PreRenderAACDetailList",100L);
	  	return RET_CONTINUE;
    }
}
