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

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/*
 * Modification history
 * 
 * 09/11/2008	AW	Initial version. Added in for SDIS:SCM-00000-05605 Machine:2111399 
 * 					
 * 
 */
public class ItemAssignLocationsSetReplUOMOnListForm extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocationsSetReplUOMOnListForm.class);
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException{
		//_log.debug("LOG_SYSTEM_OUT","\n\n Executing ConditionalValidationSetValidRoutingOnListRender ",100L);
		_log.debug("LOG_DEBUG_EXTENSION","**Executing ItemAssignLocationsSetReplUOMOnListForm",100L);	
		RuntimeListRowInterface[] rows = form.getRuntimeListRows(); 
		_log.debug("LOG_DEBUG_EXTENSION","**No. of rows: " +rows.length,100L);	
		String sql= "";
		
		if(rows.length !=0)
		{	
		try
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork(); 
			
			RuntimeFormInterface parentForm = FormUtil.findForm(form, "wms_list_shell", "wm_item_general_detail_view", state);
			DataBean parentFocus = parentForm.getFocus();
			Object parentPackkey = null;
			if (parentFocus instanceof BioBean)
			{
				parentFocus = (BioBean) parentFocus;
				parentPackkey = parentFocus.getValue("PACKKEY");
			}
			else
			{
				parentFocus = (QBEBioBean) parentFocus;
				parentPackkey = parentForm.getFormWidgetByName("PACKKEY").getDisplayValue();
			}

			// Set Value
			_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Packkey " + parentPackkey.toString(), SuggestedCategory.NONE);
			
			for(int j=0; j<rows.length; j++){
				_log.debug("LOG_DEBUG_EXTENSION","**[" +j +"]: " +"packkey" +"-->" +parentPackkey ,100L);					
				String currentUOMValue = rows[j].getFormWidgetByName("REPLENISHMENTUOM").getDisplayValue();				
				String uomAttr = UOMMappingUtil.getUOMAttribute(currentUOMValue);
				String currentUOM = UOMMappingUtil.getUOM(uomAttr, parentPackkey.toString(), uow);	
				_log.debug("LOG_DEBUG_EXTENSION","**[" +j +"]: " +"currentUOM" +"-->" +currentUOM ,100L);	

				rows[j].getFormWidgetByName("REPLENISHMENTUOM").setDisplayValue(currentUOM);
					
			}			
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		}
		_log.debug("LOG_DEBUG_EXTENSION","**Exiting ItemAssignLocationsSetReplUOMOnListForm",100L);
		return RET_CONTINUE;
		}
	
	
}
