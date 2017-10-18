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

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.HeaderDetailSave;

public class PreSaveDetail extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(HeaderDetailSave.class);

    public PreSaveDetail() { 
        _log.info("EXP_1","PreSaveDetail Instantiated!!!",  SuggestedCategory.NONE);
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException , EpiException{
		_log.debug("LOG_SYSTEM_OUT","\n\n*******Validating Records************\n\n",100L);
		
		RuntimeFormInterface detailForm= null;		
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		_log.debug("LOG_SYSTEM_OUT","\n\n*** TOOLBAR NAME: " +toolbar.getName() +"\n\n",100L);
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","\n\n^&* shellForm:" +shellForm.getName(),100L);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
        String validKey= (String)headerForm.getFormWidgetByName("VALIDATIONKEY").getValue();
		_log.debug("LOG_SYSTEM_OUT","\n\n*** Validation Key is " +validKey,100L);
        
			SlotInterface shellSlot = shellForm.getSubSlot("list_slot_2");	
			detailForm= state.getRuntimeForm(shellSlot, null);
				//New Record
			    if (detailForm.getName().equals("wm_conditional_validation_detail_view"))
			    {
			    	_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name is " +detailForm.getName(),100L);
			    }
			    //existing record
			    else 
			    {
					RuntimeFormInterface toggleForm = state.getRuntimeForm(shellSlot, null);
					detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_conditional_validation_detail_toggle_slot"), "wm_conditional_validation_detail_tab"  );
					_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" +detailForm.getName(),100L);	
			    }
		
			    
			    
			    if(!detailForm.getName().equalsIgnoreCase("Blank"))    
			    {
			    
		RuntimeFormWidgetInterface widgetDropdown= detailForm.getFormWidgetByName("VALIDATIONROUTINE");
		RuntimeFormWidgetInterface widgetAssisted = detailForm.getFormWidgetByName("REGRFPICK");
		RuntimeFormWidgetInterface widgetDirected = detailForm.getFormWidgetByName("TMPICK");
		RuntimeFormWidgetInterface widgetActive =  detailForm.getFormWidgetByName("ACTIVE");
		
		_log.debug("LOG_SYSTEM_OUT","\n\n**Slot2 form name is " +detailForm.getName() +"*****\n\n",100L);

		//Validations
		
		
	//	try{
			Object tempValue= widgetDropdown.getValue();
			String validRoutineValue = null;
			if (tempValue != null)
			{
			validRoutineValue = tempValue.toString();
			_log.debug("LOG_SYSTEM_OUT","\n\n**** Dropdown, Value : " + validRoutineValue,100L);
			}
			else
			{
			_log.debug("LOG_SYSTEM_OUT","\n\n**** Dropdown, Value is null",100L);
			}

		_log.debug("LOG_SYSTEM_OUT","\n//// Performing Actions",100L);

		//testing
        Object activeVal= widgetActive.getValue();
        Object rfDirectedVal= widgetDirected.getValue();
        Object rfAssistedVal= widgetAssisted.getValue();
        
        String checkActive= null;
        String checkDirected= null;
        String checkAssisted= null;
        String errorMessage= null;
      	        
        if (activeVal !=null) 
        {	
        	checkActive = activeVal.toString();    	 
        	if (checkActive.equals(""))
        			{checkActive="0";}
        }
        else {checkActive="0";}
        
        
        if (rfDirectedVal !=null) 
        {
        	 checkDirected = rfDirectedVal.toString();
        	 if(checkDirected.equals(""))
 			 {checkDirected="0";}
        }
        else {checkDirected="0";}
        
        	
        if(rfAssistedVal !=null)
        {
        	checkAssisted = rfAssistedVal.toString();
        	if(checkAssisted.equals(""))
			{checkAssisted="0";}
        }
        else {checkAssisted="0";}
        
   
       if(checkActive.equals("1") && validRoutineValue.equals("POSTPIC01"))
           	{
        		_log.debug("LOG_SYSTEM_OUT","\n\n\n\nCHECKING other conditions",100L);
        		if (checkAssisted.equals("0") && checkDirected.equals("0"))
        		{
        			_log.debug("LOG_SYSTEM_OUT","\n\n In check \n\n",100L);
    				throw new UserException("WMEXP_ENABLE_RF", new Object[1]);
        		}
        		else
        		{
        			_log.debug("LOG_SYSTEM_OUT","\n\n*** CHECKBOXES ENABLED",100L);
        			//detailForm.setError("");
        			//return RET_CONTINUE;
        		}
        		
           	}	
       else if(checkActive.equals("1") && validRoutineValue.equals("POSTPIC06"))
       {   	   
   		String query = "SELECT * " + "FROM CONDITIONALVALIDATIONDETAIL " 
		+ "WHERE (VALIDATIONKEY = '" + validKey + "') "
		+ "AND (VALIDATIONROUTINE = 'POSTPIC06')"
		+ "AND (ACTIVE = '1')";
        _log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
        
    
        EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

      	//Duplicate record
    		if (results.getRowCount() >= 1)
    		{	
    		_log.debug("LOG_SYSTEM_OUT","\n\n*** Record Exists",100L);
			throw new UserException("WMEXP_POSTPIC6_VALIDATION", new Object[1]);			
    		}
    		else
    		{
    		_log.debug("LOG_SYSTEM_OUT","\n\n *** Record Does Not exist- Allowed to save ",100L);
  			//detailForm.setError("");
			//return RET_CONTINUE;
    		} 
  
        }
              
               
/*	} 
		
		catch (Exception e)
	{

		// Handle Exceptions
		e.printStackTrace();
		throw new UserException("System_Error",new Object[1]);
	}
*/		
	}
		return RET_CONTINUE;
	}
}
