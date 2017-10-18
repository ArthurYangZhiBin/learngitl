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
package com.ssaglobal.scm.wms.wm_item.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class InboundCatchDataValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InboundCatchDataValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		
		
			// Get Handle on Form
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();
			String widgetName = context.getSourceWidget().getName();
			RuntimeFormInterface currentFormInterface = state.getCurrentRuntimeForm();
			if (currentFormFocus instanceof BioBean)
			{
				currentFormFocus = (BioBean) currentFormFocus;
			}
			else
			{
				currentFormFocus = (QBEBioBean) currentFormFocus;
			}
			Object ICDFLAGValueObj = null;
			Object SNUM_ENDTOENDValueObj = null;
			Object OCDFLAGValueObj = null;
			String ICDFLAGValue = "0";
			String SNUM_ENDTOENDValue = "0";
			String OCDFLAGValue = "0";

			ICDFLAGValueObj = currentFormFocus.getValue("ICDFLAG");
			SNUM_ENDTOENDValueObj = currentFormFocus.getValue("SNUM_ENDTOEND");
			OCDFLAGValueObj = currentFormFocus.getValue("OCDFLAG");
			
			if (ICDFLAGValueObj != null) {
				ICDFLAGValue = ICDFLAGValueObj.toString();
			}
			if (SNUM_ENDTOENDValueObj != null) {
				SNUM_ENDTOENDValue = SNUM_ENDTOENDValueObj.toString();
			}
			if (OCDFLAGValueObj != null) {
				OCDFLAGValue = OCDFLAGValueObj.toString();
			}
			
			if("1".equalsIgnoreCase(SNUM_ENDTOENDValue)){
				if("0".equalsIgnoreCase(ICDFLAGValue) || 
						"0".equalsIgnoreCase(OCDFLAGValue)){
					throw new FieldException(currentFormInterface, widgetName, "WMEXP_ICD_OCD_CHECKED", new Object[]{});
				}
			}
			



		return RET_CONTINUE;
	}



}
