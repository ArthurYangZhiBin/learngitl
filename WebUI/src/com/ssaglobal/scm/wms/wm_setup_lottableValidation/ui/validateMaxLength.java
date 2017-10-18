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

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class validateMaxLength extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(validateMaxLength.class);
	protected int execute(ActionContext context, ActionResult result)throws EpiException{

		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing validateMaxLength",100L);
		Double doublottableMaxLength = null;
		int intlottableMaxLength = 0;

		RuntimeFormWidgetInterface widget = context.getSourceWidget();
		String lottableMaxLength = widget.getDisplayValue().toString();
		String[] parameters = new String[1];
		parameters[0] = lottableMaxLength;

		doublottableMaxLength = new Double (lottableMaxLength);
		intlottableMaxLength = (int)doublottableMaxLength.doubleValue();
		if ((intlottableMaxLength < 0) || (intlottableMaxLength > 50)){
			throw new FormException("WMEXP_GREATERLESSEQUAL_1",parameters);
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting validateMaxLength",100L);
		return RET_CONTINUE;
	}
}