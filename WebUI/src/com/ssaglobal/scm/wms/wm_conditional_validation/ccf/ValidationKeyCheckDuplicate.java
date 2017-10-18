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
package com.ssaglobal.scm.wms.wm_conditional_validation.ccf;

import java.util.HashMap;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ValidationKeyCheckDuplicate extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidationKeyCheckDuplicate.class);
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
	throws EpiException
	{
		String errorMessage = null;
		RuntimeFormWidgetInterface widgetValidKey= form.getFormWidgetByName("VALIDATIONKEY");
		String value = (String)widgetValidKey.getValue();
		_log.debug("LOG_SYSTEM_OUT","\n\n*** Validation Key is " +value,100L);
		
		
  		String query = "SELECT * " + "FROM CONDITIONALVALIDATION" 
		+ "WHERE (VALIDATIONKEY = '" + value + "') " ;
        _log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
        EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

        	//Duplicate record
        	if (results.getRowCount() >= 1)
        	{	
        		_log.debug("LOG_SYSTEM_OUT","\n\n*** Record Exists",100L);
      			errorMessage = "Duplicate Validation Key exists.";
      			widgetValidKey.setError(errorMessage);
    			return RET_CANCEL;
        	}
        	else
        	{
        		_log.debug("LOG_SYSTEM_OUT","\n\n *** Record Does Not exist- Allowed to save ",100L);
        		widgetValidKey.setError("");
    			return RET_CONTINUE;
        	}

	}
	
}
