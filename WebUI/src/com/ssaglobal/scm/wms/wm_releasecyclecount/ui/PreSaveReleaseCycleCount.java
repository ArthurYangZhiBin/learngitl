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
package com.ssaglobal.scm.wms.wm_releasecyclecount.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.HeaderDetailSave;

public class PreSaveReleaseCycleCount extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveReleaseCycleCount.class);

	
	
    public PreSaveReleaseCycleCount() { 
        _log.info("EXP_1","PreSaveDetail Instantiated!!!",  SuggestedCategory.NONE);
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		 _log.debug("LOG_DEBUG_EXTENSION_RELEASE_CYCLE_COUNT","Executing PreSaveReleaseCycleCount",100L);
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
		Object tempMax = headerForm.getFormWidgetByName("MAXCNTS").getValue();
		Object tempBal = headerForm.getFormWidgetByName("BALCHANGE").getValue();
		Object tempQty = headerForm.getFormWidgetByName("QTYLESSTHAN").getValue();
		
		double valMax, valBal, valQty;
		
		
		if (tempMax != null)
		{
			valMax = Double.parseDouble(tempMax.toString());
			if(valMax <0)
			{		String[] param = new String[1];
					param[0] = "Max Counts for CC";
				    throw new UserException("WMEXP_NONNEG", param);}
		}else{//tempMax is null
			String[] param = new String[1];
			param[0] = "Max Counts for CC";
		    throw new UserException("WMEXP_NOT_GRATER_THAN_OR_EQUALS_ZERO", param);			
		}
		

		if (tempBal != null)
		{
			 valBal = Double.parseDouble(tempBal.toString());
			 if(valBal <0)
				{	String[] param = new String[1];
				    param[0] = "Number of days pass to count";
				 throw new UserException("WMEXP_NONNEG", param);}
		}else{//tempBal is null
			String[] param = new String[1];
			param[0] = "Number of Days Past to Count";
		    throw new UserException("WMEXP_NOT_GRATER_THAN_OR_EQUALS_ZERO", param);			
		}

		if (tempQty != null)
		{
			 valQty = Double.parseDouble(tempQty.toString());
			 if(valQty <0)
				{	String[] param = new String[1];
				    param[0] = "Count Qty less than";
				    throw new UserException("WMEXP_NONNEG", param);
				}
		}else{//tempQty is null
			String[] param = new String[1];
			param[0] = "Count Qty less than";
		    throw new UserException("WMEXP_NOT_GRATER_THAN_OR_EQUALS_ZERO", param);			
		}

		 _log.debug("LOG_DEBUG_EXTENSION_RELEASE_CYCLE_COUNT","Exiting PreSaveReleaseCycleCount",100L);
		return RET_CONTINUE;
	}
	
}
