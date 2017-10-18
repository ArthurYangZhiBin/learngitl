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
package com.ssaglobal.scm.wms.wm_invoice_processing.action;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_invoice_processing.ui.EnableActionButtonOnRunBillling;

public class PostBilling extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PostBilling.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing PostBilling",100L);
		
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData("PostRun"));
		
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName("NSPBILLINGRUNWRAPPER");
		try {
			EXEDataObject obj = WmsWebuiActionsImpl.doAction(actionProperties);
			
			if(obj._rowsReturned())
			{
				//displayResults(obj);
				//Array val = new Array();		
				//val = obj.getAttributes();
				DataValue dv1 = obj.getAttribValue(4);
							
				context.getServiceManager().getUserContext().put("InvoiceTot", dv1.getAsString());
					
				
			}
			
		} catch (Exception e) {
			e.getMessage();			
			throw new UserException(e.getMessage(), new Object[]{});
		}
	
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting PostBilling",100L);
		return RET_CONTINUE;
	}
	
	private void displayResults(EXEDataObject results)
	{
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + results.getRowCount() + " x " + results.getColumnCount() + "\n",100L);
		 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC", results.getRowCount() + " x " + results.getColumnCount(),100L);
		if (results.getColumnCount() != 0)
		{

			
			_log.debug("LOG_DEBUG_EXTENSION_INV_PROC", "***Results",100L);
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					//_log.debug("LOG_SYSTEM_OUT"," " + i + " @ " + results.getAttribute(i).name + " "+ results.getAttribute(i).value.getAsString(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_INV_PROC", " " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString(),100L);
				} catch (Exception e)
				{
					_log.debug("LOG_SYSTEM_OUT",e.getMessage(),100L);
				}
			}			
			_log.debug("LOG_DEBUG_EXTENSION_INV_PROC", "********",100L);
		}
	}
}
