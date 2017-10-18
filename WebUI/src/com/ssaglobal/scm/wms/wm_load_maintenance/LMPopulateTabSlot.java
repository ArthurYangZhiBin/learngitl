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

package com.ssaglobal.scm.wms.wm_load_maintenance;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.customization.SlotExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;



public class LMPopulateTabSlot extends SlotExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LMPopulateTabSlot.class);
    public LMPopulateTabSlot()
    {
    }

    protected int prePopulateSlot(UIRenderContext context, SlotInterface slot, RuntimeFormExtendedInterface form)
        throws EpiException
    {
    	_log.debug("LOG_DEBUG_EXTENSION_LMPOPULATETAB","Executing LMPopulateTabSlot",100L);    	
    	StateInterface state = context.getState();
    	HttpSession session = state.getRequest().getSession();
    	String loadStopId = (String)session.getAttribute("LOADSTOPID");
    	if(loadStopId != null){
    		Query loadBiosQry = new Query("wm_loadstop_lm", "wm_loadstop_lm.LOADSTOPID = '"+loadStopId.toUpperCase()+"'", "");
    		_log.debug("LOG_DEBUG_EXTENSION_LMPOPULATETAB","Executing Query:"+loadBiosQry.getQueryExpression(),100L);			
			UnitOfWorkBean uow = context.getState().getTempUnitOfWork();									
			BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
			if(bioCollection != null && bioCollection.size() > 0){
				BioBean stop = (BioBean)bioCollection.elementAt(0);				
				form.setFocus(state, slot, null, stop, "wm_load_maintenance_tab_container_form");
			}
    	}
    	_log.debug("LOG_DEBUG_EXTENSION_LMPOPULATETAB","Leaving LMPopulateTabSlot",100L);        
        return 0;
    }
   
}
