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
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
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

public class validateGenerateMask extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(validateGenerateMask.class);
	protected int execute(ActionContext context, ActionResult result)throws EpiException{

		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing validateGenerateMask",100L);
		int returnCode=0;

		StateInterface state = context.getState();
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		DataBean focus= state.getFocus();
		if(focus.isTempBio())
		{focus = (QBEBioBean)focus;}
		else
		{focus = (BioBean)focus;}
		
		RuntimeFormWidgetInterface widget = context.getSourceWidget();
		_log.debug("LOG_SYSTEM_OUT","Form widget = "+ widget.getName(),100L);
		Object lottableMaskObj = context.getSourceWidget().getValue();
		if(!(lottableMaskObj == null) && !(lottableMaskObj.toString().equals("")))
		{
			String lottableMask = lottableMaskObj.toString().trim();
			focus.setValue(widget.getName(), lottableMask);
		
		_log.debug("LOG_SYSTEM_OUT","Form widget Value = "+ lottableMask,100L);
		String[] parameters = new String[1];
		parameters[0] = lottableMask;

		if ((lottableMask.length() > 0) && (lottableMask.length() < 5)){
			throw new FormException("WMEXP_LOTTABLEMASKVALIDATION_1", new Object[]{});
		}
		
		if ((lottableMask.length() >= 5)){
        	WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
        	Array parms = new Array(); 
        	parms.add(new TextData(lottableMask));
        	actionProperties.setProcedureParameters(parms);
        	actionProperties.setProcedureName("NSPGENERATEMASKVALIDATION");
        	try {
        		EXEDataObject ObjMask = WmsWebuiActionsImpl.doAction(actionProperties);
    			 returnCode = ObjMask.getReturnCode();
        	}  catch (WebuiException e) {
    			// TODO Auto-generated catch block
        			e.getMessage();
        			UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
         	   		throw UsrExcp;
        	}
        	/*
        	if(returnCode == 1){
        		context.setNavigation("changeEvent92");
//        		throw new FormException("WMEXP_LOTTABLEMASKVALIDATION_2", parameters);
        	}
//        	else{
//        		throw new FormException("SKU_VALIDATION", parameters);
//        	}
 * 
 */
		}
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting validateGenerateMask",100L);
		return RET_CONTINUE;
	}
}