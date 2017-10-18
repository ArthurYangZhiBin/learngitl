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

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException; //AW 07/21/09 Machine:2073672 SDIS:05256
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;

public class ConditionalValidationPreSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConditionalValidationPreSaveValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException
	{
		//_log.debug("LOG_SYSTEM_OUT","\n\n****** In ConditionalValidationPreSaveValidation*******\n\n",100L); 
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Executing ConditionalValidationPreSaveValidation",100L);	
		ConditionalValidationDataObject obj = new ConditionalValidationDataObject();
		boolean ownerNull = false; //AW 07/21/09 Machine:2073672 SDIS:05256
		
		StateInterface state = context.getState();	
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
		//_log.debug("LOG_SYSTEM_OUT","\n\n****** Populating data objects*******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","** Populating data objects",100L);
		obj.setCondValidKey((String)headerFocus.getValue("VALIDATIONKEY"));
		obj.setCustomer((String)headerFocus.getValue("CONSIGNEEKEY"));
		obj.setItem((String)headerFocus.getValue("SKU"));
		obj.setStorer((String)headerFocus.getValue("STORERKEY"));
		obj.setItem((String)headerFocus.getValue("SKU"));
		Object objType = headerFocus.getValue("TYPE");
		if( objType != null && !objType.toString().equalsIgnoreCase("")){
			obj.setType(objType.toString());
		}
		Object objMinShelfLife = headerFocus.getValue("MINIMUMSHELFLIFE");
		if(objMinShelfLife !=null && !objMinShelfLife.toString().equalsIgnoreCase(""))
		{
			obj.setMinShelfLife(objMinShelfLife.toString());
		}

		//get detail form
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	
	    if (detailForm.getName().equals("wm_conditional_validation_detail_view"))
	    {
	    	//_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name is " +detailForm.getName(),100L);
	    }
	    //existing record
	    else 
	    {
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_conditional_validation_detail_toggle_slot"), "wm_conditional_validation_detail_tab"  );
			//_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" +detailForm.getName(),100L);	
	    }		
		
	   
	    
	    if(!detailForm.getName().equalsIgnoreCase("Blank"))
		{	    
		RuntimeFormWidgetInterface widgetDropdown= detailForm.getFormWidgetByName("VALIDATIONROUTINE");		
		//_log.debug("LOG_SYSTEM_OUT","\n**detailform name " +detailForm.getName(),100L);
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","** DetailForm: " +detailForm.getName(),100L);

		
		Object objRout = widgetDropdown.getValue();
		if (objRout != null)
		{//_log.debug("LOG_SYSTEM_OUT","\n\n**** Dropdown, Value : " + objRout.toString(),100L);
		}
		else{
			throw new UserException("WMEXP_REQ_VAL", new Object[1]);}	
		}
		
//		validate PK
		boolean validate = false;
		if(isNewHeader)
		{
		try{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating PK",100L);
			 _log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","** Validating PK" ,100L);
			validate = ConditionalValidationUtil.isDupPK(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = obj.getCondValidKey();
			//throw new UserException("WMEXP_DUP_PK", param);	
			String errorMsg = getTextMessage("WMEXP_DUP_PK",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**INVALID PK- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);
		}	
		
		
//		validate owner
		if(!isEmpty(obj.getStorer())){ //AW 07/21/2009 SDIS:05256
		try{
//			_log.debug("LOG_SYSTEM_OUT","\n** Validating owner",100L);
			 _log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Validating owner" ,100L);
			validate = ConditionalValidationUtil.isValidOwner(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}		
		if(!validate){
			String [] param = new String[1];
			param[0] = obj.getStorer();
			//throw new UserException("WMEXP_OWNER_VALID", param);		
			String errorMsg = getTextMessage("WMEXP_OWNER_VALID",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**INVALID Owner- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);	
		}	
		}//AW 07/21/2009 SDIS:05256
		else //AW 06/23/09 Machine:2073672 SDIS:05256 start
			{
			obj.setStorer(" ");//AW 07/21/2009 SDIS:05256
			headerFocus.setValue("STORERKEY", " ");
			ownerNull = true;
			} //AW 06/23/09 Machine:2073672 SDIS:05256 end
		
		if(!isEmpty(obj.getCustomer())){ //AW 07/21/2009 SDIS:05256
		//validate customer
		try{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating customer",100L);
			 _log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Validating customer" ,100L);
			validate = ConditionalValidationUtil.isValidCustomer(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[1];
			param[0] = obj.getCustomer();
			//throw new UserException("WMEXP_CUST_VALIDATION", param);	
			String errorMsg = getTextMessage("WMEXP_CUST_VALIDATION",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**INVALID CUSTOMER- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);	
		}	
		}//AW 07/21/2009 SDIS:05256 start
		else
			{obj.setCustomer(" ");//AW 07/21/2009 SDIS:05256
			headerFocus.setValue("CONSIGNEEKEY", " ");
			} //AW 06/23/09 Machine:2073672 SDIS:05256 end
				
		if(!isEmpty(obj.getItem()) ){ //AW 07/21/2009 SDIS:05256
		//validate item-storer comb
		if(!ownerNull){ //AW 06/23/09 Machine:2073672 SDIS:05256 

		try{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating item",100L);
			 _log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Validating item" ,100L);
			validate = ConditionalValidationUtil.isValidItem(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			String [] param = new String[2];
			param[0] = obj.getItem();
			param[1] = obj.getStorer();
			//throw new UserException("WMEXP_SKU_OWNERCOMB", param);		
			String errorMsg = getTextMessage("WMEXP_SKU_OWNERCOMB",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**INVALID ITEM- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);	
		}
		}//AW 07/21/2009 SDIS:05256 start
		}
		else
			{obj.setItem(" ");//AW 07/21/2009 SDIS:05256
			 headerFocus.setValue("SKU", " ");
			} //AW 07/21/2009 Machine:2073672 SDIS:05256 end
		
		
		if(obj.getStorer() != null && obj.getItem() != null && obj.getCustomer() != null){ //AW 06/23/2009 SDIS:05256
		//validate item-customer-storer-type combination
		try{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating item-customer-storer-type combination",100L);
			 _log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Validating item-customer-storer-type combination" ,100L);
			validate = ConditionalValidationUtil.isValidComb(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			//_log.debug("LOG_SYSTEM_OUT","**DUPLICATE*****",100L);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**DUPLICATE" ,100L);
			String [] param = new String[4];
			param[0] = obj.getStorer();
			param[1] = obj.getItem();			
			param[2] = obj.getCustomer();
			param[3] = obj.getType();
			//throw new UserException("WMEXP_COMB", param);	
			String errorMsg = getTextMessage("WMEXP_COMB",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**DUPLICATE- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);		
		}
		}//AW 07/21/2009 SDIS:05256
	}
		
		//validate minShelfLife
		if(objMinShelfLife !=null && !objMinShelfLife.toString().equalsIgnoreCase(""))
		{
		try{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating MinShelfLife",100L);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Validating MinShelfLife" ,100L);
			validate = ConditionalValidationUtil.isGreaterThankZero(obj);
		}catch(DPException dpE){
			dpE.printStackTrace();
			throw new UserException("System_Error",new Object[1] );			
		}
		if(!validate){
			//_log.debug("LOG_SYSTEM_OUT","\n** Negative Value*****",100L);
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Negative Value" ,100L);
			String [] param = new String[1];
			param[0] = "Minimum Shelf Life";
			//throw new UserException("WMEXP_NONNEG", param);		
			String errorMsg = getTextMessage("WMEXP_NONNEG",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Negative Value- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);			 
		}
		}		
		return RET_CONTINUE;
	}
	//AW 07/21/09 Machine:2073672 SDIS:05256 start	
	private boolean isEmpty(Object attributeValue) throws EpiDataException{
		if(attributeValue == null){
			return true;
		}else if(attributeValue.toString().matches("\\s*")){
			return true;
		}else{
			return false;
		}
	} //AW 07/21/09 Machine:2073672 SDIS:05256 end

}
