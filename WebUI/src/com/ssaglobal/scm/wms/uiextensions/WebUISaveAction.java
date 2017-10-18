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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyState;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WebUISaveAction extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WebUISaveAction.class);
	public WebUISaveAction()
	{
	}

	@Override
	protected int execute(ActionContext context, ActionResult result)throws UserException , EpiException
	{
	       boolean saveAllTempBios;
	        UnitOfWorkBean uowb;
	        DataBean currentFocus;
	        DataBean newFocus;
	        saveAllTempBios = getParameterBoolean("save all temp bios", false);
	        EpnyState state = (EpnyState)context.getState();
	        uowb = state.getDefaultUnitOfWork();
	        currentFocus = result.getFocus();
	        newFocus = currentFocus;
	        
	        try
	        {
	            uowb.saveUOW(saveAllTempBios);
	        }
	            catch (UnitOfWorkException e){
	    			_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
	    			
	    			Throwable nested = (e).findDeepestNestedException();
	    			_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
	    			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
	    			
	    			if(nested instanceof ServiceObjectException)
	    			{
	    				String reasonCode = nested.getMessage();
	    				//replace terms like Storer and Commodity
	    				
	    				throwUserException(e, reasonCode, null);
	    			}
	    			else
	    			{
	    				throwUserException(e, "ERROR_SAVING_BIO", null);
	    			}

	    		}
	    		uowb.clearState();
	    	    result.setFocus(newFocus);

//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
	    		
	    		
	    		
	    		
	    	return RET_CONTINUE;
	}
	  public static final String SAVE_ALL_TEMP_BIOS = "save all temp bios";
}