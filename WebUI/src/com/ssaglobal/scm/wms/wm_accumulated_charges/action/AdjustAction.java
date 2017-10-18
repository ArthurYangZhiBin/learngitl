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
package com.ssaglobal.scm.wms.wm_accumulated_charges.action;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_accumulated_charges.ui.PreRenderAccumulatedCharges;

public class AdjustAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustAction.class);
protected int execute(ActionContext context, ActionResult result)throws EpiException
{
	  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing AdjustAction",100L);
	StateInterface state = context.getState();
	RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																						state.getCurrentRuntimeForm(),
																						"wms_list_shell",
																						"wm_accumulated_charges_list_view",
																						state);
	ArrayList items = listForm.getAllSelectedItems();
	if (isZero(items))
	{
		throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
	}
	
	try
	{
		
		ArrayList itemsSelected = listForm.getAllSelectedItems();		
		if(itemsSelected != null && itemsSelected.size() == 1)
		{
        UnitOfWorkBean uowb = state.getDefaultUnitOfWork();     
		Iterator bioBeanIter = itemsSelected.iterator();
		
		try{
			BioBean bean= null;
				for(; bioBeanIter.hasNext(); )
				{
					bean = (BioBean)bioBeanIter.next();
					
					RuntimeFormWidgetInterface debWidget = listForm.getFormWidgetByName("DEBIT");
					//debWidget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					String keyVal = bean.getValue("ACCUMULATEDCHARGESKEY").toString();					
					result.setFocus(bean);					
				}
			
		
			}catch(RuntimeException e)
			{
				e.printStackTrace();}				
		}//end if
		
	
}
	catch (RuntimeException e1)
	{
	e1.printStackTrace();
	}
	  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting AdjustAction",100L);
	return RET_CONTINUE;
}
	private boolean isZero(ArrayList items) {
		// TODO Auto-generated method stub
		if (items == null)
			return true;
		else if (((ArrayList) items).size() == 0)
			return false;
		else
			return false;
		
	}


}			



