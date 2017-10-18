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

import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.EpiException;

public class SetNavPrintOption extends ActionExtensionBase {

    /**
     * Executes when a button in the modal form is clicked. Sets for the form focus back to toolbar to avoid any
     * incorrect focus values set in previous extensions.
     * @param context
     * @param result
     * @return RET_CONTINUE
     * @throws EpiException
	 */
     protected int execute(ActionContext context, ActionResult result) throws EpiException {
    	EpnyUserContext userCtx = context.getServiceManager().getUserContext();
    	if ((userCtx.get("printOption").toString().equalsIgnoreCase("PRN"))){
    		return RET_CANCEL;
    	}
        return RET_CONTINUE;
    }

}
