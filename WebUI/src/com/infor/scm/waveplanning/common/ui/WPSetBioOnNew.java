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
package com.infor.scm.waveplanning.common.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class WPSetBioOnNew extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPSetBioOnNew.class);
    
    protected int execute(ActionContext context, ActionResult result)
        throws UserException
    {
    	_log.debug("LOG_DEBUG_EXTENSION_SetBioOnNew", "***Executing SetBioOnNew", SuggestedCategory.NONE);
        StateInterface state = context.getState();
        UnitOfWorkBean uow = state.getDefaultUnitOfWork();
        String bio = getParameter("bioName").toString();
        _log.debug("LOG_DEBUG_EXTENSION_SetBioOnNew", "***Bio Name: " +bio, SuggestedCategory.NONE);
    	try {
    		Query qry = new Query(bio, null, null);  
    		if(uow.getBioCollectionBean(qry).size() >0)
    			{
    			BioBean newFocus = uow.getBioCollectionBean(qry).get("0");    		 		
    			BioBean newBioBean = uow.getBioBean(newFocus.getBioRef());
    			result.setFocus(newBioBean);
    			}
    		else
    			{
    			  QBEBioBean tempQBE = uow.getQBEBioWithDefaults(bio);
    			  result.setFocus(tempQBE);
    			}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
             
        
    		_log.debug("LOG_DEBUG_EXTENSION_SetBioOnNew", "***Exiting SetBioOnNew", SuggestedCategory.NONE);
    		return RET_CONTINUE;
    }
}