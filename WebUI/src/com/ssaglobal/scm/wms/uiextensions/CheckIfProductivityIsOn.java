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
package com.ssaglobal.scm.wms.uiextensions;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class CheckIfProductivityIsOn extends ActionExtensionBase{
	
	private static String BIO= "wm_system_settings";
	private static String ATTRIBUTECK = "CONFIGKEY";
	private static String ATTRIBUTENSQL = "NSQLVALUE";
	private static String ckVal = "MONITORPRODUCTIVITY";
	private static String nsqlVal= "0";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckIfProductivityIsOn.class);
	
	@Override
	protected int execute(ActionContext context, ActionResult result)throws UserException, EpiException
    {					

		_log.debug("LOG_DEBUG_EXTENSION_CheckIfProductivityIsOn","***Executing CheckIfProductivityIsOn",100L);
		
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();

		String qry = BIO +"." + ATTRIBUTECK + "= '" +ckVal +"' AND " + 
					 BIO + "." +ATTRIBUTENSQL + "='" +nsqlVal +"'";  			
		_log.debug("LOG_DEBUG_EXTENSION_CheckIfProductivityIsOn","***qry statement: " +qry,100L);
		
		Query query = new Query(BIO, qry, null);
		BioCollection results = uow.getBioCollectionBean(query);		
		_log.debug("LOG_DEBUG_EXTENSION_CheckIfProductivityIsOn","***Result size: " +results.size(),100L);

		if(results.size() >= 1){
			_log.debug("LOG_DEBUG_EXTENSION_CheckIfProductivityIsOn","***Productivity is not turned on",100L);
			throw new FormException("WMEXP_PRODUCTIVITY_OFF", new Object[1]);
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_CheckIfProductivityIsOn","***Exiting CheckIfProductivityIsOn",100L);
			return RET_CONTINUE;
		}
    }

	
}
