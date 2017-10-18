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
package com.ssaglobal.scm.wms.wm_barcode_config.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.data.dp.exception.DPException;
import com.ssaglobal.scm.wms.wm_audit.ui.WMAuditDeleteDetailPreRender;
import com.ssaglobal.scm.wms.wm_load_schedule.ui.LoadSchedulePreSaveValidationUtil;
import com.ssaglobal.scm.wms.wm_load_schedule.ui.LoadScheduleValidationDataObject;

public class BarcodeConfigPreSave extends com.epiphany.shr.ui.action.ActionExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(BarcodeConfigPreSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		BarcodeConfigDataObject data = new BarcodeConfigDataObject();

		StateInterface state = context.getState();
		 
		_log.debug("LOG_SYSTEM_OUT","\n\n****** In BarcodeConfigPreSave*******\n\n",100L); 
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewHeader = false;
		boolean isNewDetail = false;
		if(headerFocus.isTempBio()){
			isNewHeader = true;
		}
		//populate data object
		_log.debug("LOG_SYSTEM_OUT","\n\n****** Populating data objects*******\n\n",100L); 
		data.setbarcodeConfigKey((String)headerFocus.getValue("BARCODECONFIGKEY"));

		//get detail data
		DataBean detailFocus = null;
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name: " +detailForm.getName() +"**\n\n",100L);
		

		
		//New Record
	    if (detailForm.getName().equals("wm_barcode_config_detail_view"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name is " +detailForm.getName(),100L);
	    	detailFocus = detailForm.getFocus();
	    }
	    //existing record
	    else 
	    {
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_barcode_config_detail_toggle"), "wm_barcode_config_detail_toggle_tab"  );
			_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" +detailForm.getName(),100L);	
			detailFocus = detailForm.getFocus();
	    }
				
		data.setAI((String)detailFocus.getValue("AI"));	
		
		if (detailFocus.isTempBio()) {//it is for insert header
			isNewDetail = true;
		}	

		
		boolean validate = false;
		if(isNewHeader){
		try{
			_log.debug("LOG_SYSTEM_OUT","\n** Validating Barcodeconfigkey",100L);
			validate = BarCodeConfigUtil.isValidKey(data);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(validate){
			String [] param = new String[1];
			param[0] = data.getbarcodeConfigKey();
			throw new UserException("WMEXP_BARCODEKEY", param);			
		}	
		}
		
		validate=false;
		if(isNewDetail){
		try{
			_log.debug("LOG_SYSTEM_OUT","\n** Validating AI",100L);
			validate = BarCodeConfigUtil.isValidAI(data);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(validate){
			String [] param = new String[1];
			param[0] = data.getappIdentifier();
			throw new UserException("WMEXP_AI", param);			
		}
		else
		{
			detailFocus.setValue("AI", data.getappIdentifier().toUpperCase());
		}
		}
		
		
 return RET_CONTINUE;
	}
}