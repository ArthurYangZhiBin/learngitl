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
package com.ssaglobal.scm.wms.wm_conditional_validation.ui;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_load_schedule.ui.LoadSchedulePreSaveValidation;

public class ConditionalValidationSetValidRoutingOnListRender  extends FormExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConditionalValidationSetValidRoutingOnListRender.class);
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n Executing ConditionalValidationSetValidRoutingOnListRender ",100L);
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Executing ConditionalValidationSetValidRoutingOnListRender",100L);	
		String sql= "";
		RuntimeListRowInterface[] rows = form.getRuntimeListRows(); 
		_log.debug("LOG_SYSTEM_OUT","**No. of rows: " +rows.length,100L);
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**No. of rows: " +rows.length,100L);	
		
		for(int j=0; j<rows.length; j++){
			String validRoutine = rows[j].getFormWidgetByName("VALIDATIONROUTINE").getDisplayValue();
			//_log.debug("LOG_SYSTEM_OUT","*[" +j +"]= " +validRoutine,100L);
			sql = "select description from codelkup where code='" +validRoutine +"'";
			_log.debug("LOG_SYSTEM_OUT","\n*** sql: " +sql +"\n",100L);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","** SQL: " +sql,100L);	
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			String desc = dataObject.getAttribValue(new TextData("description")).toString();
			//_log.debug("LOG_SYSTEM_OUT","\n\n*[" +j +"]: " +validRoutine +"-->" +desc  ,100L);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**[" +j +"]: " +validRoutine +"-->" +desc ,100L);	
			rows[j].getFormWidgetByName("VALIDATIONROUTINE").setDisplayValue(desc);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Exiting ConditionalValidationSetValidRoutingOnListRender",100L);	
		}
		
		return RET_CONTINUE;
		}
		
	}