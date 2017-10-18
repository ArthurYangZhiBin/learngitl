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
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;

public class PreSaveValidateConversion extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateConversion.class);
	   
	DataBean detailFormFocus = null;
	String error= "";
	String errorMsg= "";
	String err = "";
	   
    public PreSaveValidateConversion() 
    { 
        _log.info("EXP_1","PreSaveValidateConversion Instantiated!!!",  SuggestedCategory.NONE);
    }
    
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException 
	{	
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing PreSaveValidateConversion",100L);
	
		RuntimeFormInterface detailForm = null;		
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		SlotInterface shellSlot = shellForm.getSubSlot("list_slot_2");	
		detailForm= state.getRuntimeForm(shellSlot, null);
		detailFormFocus = detailForm.getFocus();
		
		//New Record
		if (!detailForm.getName().equals("wm_setup_lottable_validation_detail_view"))
		{			    
			RuntimeFormInterface toggleForm = state.getRuntimeForm(shellSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_setup_lottable_validation_toggle"), "wm_setup_lottable_validation_detail_tab");
			detailFormFocus = detailForm.getFocus();	
		}
			    
		if (detailFormFocus instanceof BioBean)
		{
			detailFormFocus = (BioBean)detailFormFocus;
		}
		else if(detailFormFocus instanceof QBEBioBean)
		{
			detailFormFocus= (QBEBioBean)detailFormFocus;
		}
			
		try
		{ 
			//get widget values from header
			String validTemp = null;
			String convTemp = null; 
			String num = null;
			Object[][] detailWidgetValues = new Object[12][2];
			int j=0, k=0;
				    
			for (int i=1; i<=12; i++)
			{
				if(i >= 10)
				{
					validTemp = "LOTTABLE" + Integer.toString(i) + "RECEIPTVALIDATION";
					convTemp = "LOTTABLE" + Integer.toString(i) + "RECEIPTCONVERSION";
					num = "LOTTABLE" + Integer.toString(i);
				}
				else
				{
					 validTemp = "LOTTABLE0" + Integer.toString(i) + "RECEIPTVALIDATION";
					 convTemp = "LOTTABLE0" + Integer.toString(i) + "RECEIPTCONVERSION";
					 num = "LOTTABLE0" + Integer.toString(i);
				}			    	
				    	
		    	//get dropdown widgets from detail form
		    	Object validValue = detailFormFocus.getValue(validTemp);	    	
		     	Object reqValue = detailFormFocus.getValue(convTemp);
		     	
		     	k=0;				        
		     	detailWidgetValues[j][k]= (validValue != null) ? validValue : "0";  	
		     	detailWidgetValues[j][k+1]= (validValue != null) ? reqValue : "0";
		     	j++;				     	
			}
				    
			errorMsg = checkConversion(detailWidgetValues);			     	
			err = checkValidation(detailWidgetValues);
			errorMsg = errorMsg + err;			     				     	
		}
		catch(Exception e)
		{
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		
		if (errorMsg.length() > 0)
		{
			throw new FormException(errorMsg, null);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting PreSaveValidateConversion",100L);
			return RET_CONTINUE;
		}	
	}
	
	
	
	private String checkValidation(Object[][] detailWidgetValuesForValid) 
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkValidation",100L);
		String valToCheck= "";
		String tempVal= "";
		err="";
		
		for( int idx = 0; idx < 12; idx++ )
		{
			valToCheck = detailWidgetValuesForValid[idx][0].toString();
			_log.debug("LOG_SYSTEM_OUT","valToCheck : "+valToCheck,100L);
			if ( valToCheck == "1" ) //the checkbox is checked
			{
				for(int index=0; index<12; index++)
				{
					tempVal= detailWidgetValuesForValid[index][0].toString();
					if((tempVal.equals("NONE") || (tempVal.equals("0"))))
					{
						err= "";
					}										
					else if(index == idx)						
					{
						err= "";
					}												
					else
					{	
						if (detailWidgetValuesForValid[index][0].toString().substring(0,5).equalsIgnoreCase("NSPLV"))
						{
							err = err + "Cannot select Shelf Life Validation for more than 1 lottable";	
							return err;
						}
					}	
				}
			}
				
		}
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkValidation",100L);
		return err;
	}
	
	private String checkConversion(Object[][] detailValues) 
	{
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing checkConversion",100L);

     	for(int a = 0; a < 12; a++)
     	{      			
     		if(detailValues[a][1] != null)
     		{     		        	
	        	//case to ignore
				if(detailValues[a][1].equals("NONE"))
	        	{
	        		//String strToCheck = detailValues[a][1].toString();
	        	}
	        	else
	        	{
	        		if(detailValues[a][0].toString().equalsIgnoreCase("NSPLV002"))
	        		{
	        			int numLot = a+1;
	        			error= error + "Conversion is not allowed for Validation: " +"NSPLV002" +" in Lottable" +numLot +"\n";
	        		}	
	        	}
     		}    			
     	}
     	_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting checkConversion",100L);
		return error;			  	  	
	}
}
