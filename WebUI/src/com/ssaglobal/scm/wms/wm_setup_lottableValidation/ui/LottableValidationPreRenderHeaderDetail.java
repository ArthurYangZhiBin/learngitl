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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_internal_transfer.ui.EnforcePrecision;

public class LottableValidationPreRenderHeaderDetail extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LottableValidationPreRenderHeaderDetail.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException
    {
		
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing LottableValidationPreRenderHeaderDetail",100L);
		StateInterface state = context.getState();	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
	
		if(headerFocus.isTempBio())
		{headerFocus = (QBEBioBean)headerFocus;
		headerFocus.setValue("GENERATEMASK01" , " ");
		headerFocus.setValue("GENERATEMASK02" , " ");
		headerFocus.setValue("GENERATEMASK03" , " ");
		headerFocus.setValue("GENERATEMASK06" , " ");
		headerFocus.setValue("GENERATEMASK07" , " ");
		headerFocus.setValue("GENERATEMASK08" , " ");
		headerFocus.setValue("GENERATEMASK09" , " ");
		headerFocus.setValue("GENERATEMASK10" , " ");
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting ConditionalValidHeaderDetailPreRender",100L);
		return RET_CONTINUE;
    }
}
