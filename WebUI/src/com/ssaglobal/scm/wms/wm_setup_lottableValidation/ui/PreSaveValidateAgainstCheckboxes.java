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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;

public class PreSaveValidateAgainstCheckboxes extends ActionExtensionBase
{
	   protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateAgainstCheckboxes.class);
	   
	   RuntimeFormInterface headerForm= null;
	   DataBean detailFormFocus = null;
	   DataBean headerFormFocus = null;
	   RuntimeFormInterface detailForm= null;	
	   String errorMessage = "";
	   String finalError = "";
	   String error= ""; 
	   String errMsg= "";
	    
    public PreSaveValidateAgainstCheckboxes() 
    { 
        _log.info("EXP_1","PreSaveValidateAgainstCheckboxes Instantiated!!!",  SuggestedCategory.NONE);
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException 
	{	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing PreSaveValidateAgainstCheckboxes",100L);
	
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		headerForm = state.getRuntimeForm(headerSlot, null);
		headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();
        
		SlotInterface shellSlot = shellForm.getSubSlot("list_slot_2");	
		detailForm= state.getRuntimeForm(shellSlot, null);
		detailFormFocus = detailForm.getFocus();
			
		if (!detailForm.getName().equals("wm_setup_lottable_validation_detail_view"))
		{
			RuntimeFormInterface toggleForm = state.getRuntimeForm(shellSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_setup_lottable_validation_toggle"), "wm_setup_lottable_validation_detail_tab"  );
			detailFormFocus = detailForm.getFocus();	
		}
			    
		if (detailFormFocus instanceof BioBean)
		{
			detailFormFocus = (BioBean)detailFormFocus;
		}
				
		if (headerFormFocus instanceof BioBean)
		{
			headerFormFocus = (BioBean) headerFormFocus;
		}				
	
		try
		{ 
		    //get widget values from header
		    String visibleTemp = null;
		    String reqTemp = null;
		    String validTemp = null;
		    String convTemp = null; 
		    String num= null;
			   
		    for (int i=1; i<=10; i++)
		    {
		    	if ( i == 10 )
			    {
			    	visibleTemp= "SHOWLOTTABLE" + Integer.toString(i) + "ONRFRECEIPT";
				   	reqTemp = "LOTTABLE" + Integer.toString(i) + "ONRFRECEIPTMANDATORY";
				   	validTemp= "LOTTABLE" + Integer.toString(i) + "RECEIPTVALIDATION";
				   	convTemp = "LOTTABLE" + Integer.toString(i) + "RECEIPTCONVERSION";
				   	num = "LOTTABLE" + Integer.toString(i);
			    }
			    else
			    {
			    	visibleTemp= "SHOWLOTTABLE0" + Integer.toString(i) + "ONRFRECEIPT";
			    	reqTemp = "LOTTABLE0" + Integer.toString(i) + "ONRFRECEIPTMANDATORY";
				   	validTemp= "LOTTABLE0" + Integer.toString(i) + "RECEIPTVALIDATION";
				   	convTemp = "LOTTABLE0" + Integer.toString(i) + "RECEIPTCONVERSION";
				   	num = "LOTTABLE0" + Integer.toString(i);
			    }
			    
			   	//get checkbox widgets from header form
			   	RuntimeFormWidgetInterface visibleCheckboxTemp = headerForm.getFormWidgetByName(visibleTemp);
			   	RuntimeFormWidgetInterface reqCheckboxTemp = headerForm.getFormWidgetByName(reqTemp);
 			    	
			    //set values in checkboxes if null or ""
			    RuntimeFormWidgetInterface visibleCheckbox= checkCheckboxForNull(visibleCheckboxTemp, headerFormFocus);
			    RuntimeFormWidgetInterface reqCheckbox= checkCheckboxForNull(reqCheckboxTemp, headerFormFocus);
			   	
			    //get dropdown widgets from detail form
			    Object validValue = detailFormFocus.getValue(validTemp);	    	
			    Object reqValue = detailFormFocus.getValue(convTemp);
			     			    			    	
			    errMsg = checkIfChecked(validValue,reqValue, visibleCheckbox, reqCheckbox, num);				     
			 }
		}
		catch(Exception e)
		{
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
			 
		if (errMsg.length() > 0)
		{
			String[] param= new String[1];
			param[0]= (errMsg.trim()).substring(0, errMsg.length()-2);
			throw new UserException("WMEXP_VALIDATECHKBX", param);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting PreSaveValidateAgainstCheckboxes",100L);
			return RET_CONTINUE;
		}	    
	}	

	private String checkCheckboxes(Object objVal, RuntimeFormWidgetInterface req, RuntimeFormWidgetInterface visible, String label) 
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkCheckBoxes",100L);
		String rVal= req.getValue().toString();
		String vVal= visible.getValue().toString();

		rVal= rVal.trim();
		vVal = vVal.trim();
		
		if(rVal.equals("0") || vVal.equals("0"))
		{
			errorMessage = errorMessage + label + ", ";
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting checkCheckBoxes",100L);
		return errorMessage;
	}	
	
	private String checkIfChecked(Object valueVal, Object valueConv,  RuntimeFormWidgetInterface visibleCheckbox, RuntimeFormWidgetInterface reqCheckbox, String str) 
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkIsChecked",100L);
		String strToCheck = null;
		String valStr = "";
		String convStr = "";
		
        if ( valueVal != null )
        {
        	if(valueVal.equals("NONE") || valueVal.equals("0"))
        	{
        		strToCheck = valueVal.toString();
        		error = error + "";
        	}
        	else
        	{
        		strToCheck = valueVal.toString();
        		error = checkCheckboxes(valueVal, reqCheckbox, visibleCheckbox, str);       		
        		return error;
        	}
        }      
        
        if ( valueConv != null )
        {
        	if ( valueConv.equals("NONE") || valueVal.equals("0"))
        	{
        		strToCheck = valueConv.toString();
        		error = error + "";
        	}
        	else
        	{
        		strToCheck = valueConv.toString();
        		error= checkCheckboxes(valueConv, reqCheckbox, visibleCheckbox, str);
        		return error;
        	}
        }
        
        _log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting checkIsChecked",100L);
        return error;
	}
	
	private RuntimeFormWidgetInterface checkCheckboxForNull(RuntimeFormWidgetInterface checkbox, DataBean focus) 
	{	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkCheckboxForNull",100L);		
		
	    if ( checkbox.getValue() != null ) 
	    {	    	 	    	
	        if (checkbox.getValue().equals(""))	 
	        {
	        	focus.setValue(checkbox.getName(), "0");
	        }
	    }
	    else 
	    {
	        focus.setValue(checkbox.getName(), "0"); //else it's 1 or 0	 
	    }
	    _log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting checkCheckboxForNull",100L);
		return checkbox; 		
	}
}
