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




import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.SQLAttributeDomain;
import com.epiphany.shr.metadata.objects.generated.np.SqlAttributeDomainFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class DependantDropdownGetValuesOnPreRender extends FormExtensionBase{

	//private static String POSTPICK = "wm_conditional_validation_POSTPICK";
	private static String TYPE = "TYPE";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DependantDropdownGetValuesOnPreRender.class);
	
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException
    {
		Object tempValue= null;
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Executing DependantDropdownGetValuesOnPreRender",100L);
		try{
			StateInterface state = context.getState();
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);	
			
			    if (shellForm.getName().equals("wms_list_shell"))
			    {			
				SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
				RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
				DataBean headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();
				tempValue = headerFormFocus.getValue(TYPE);
			    }
			    else
			    {
			    	RuntimeFormInterface currForm= shellForm.getParentForm(state);
			    	
					SlotInterface headerSlot = currForm.getSubSlot("list_slot_1");
					RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
					DataBean headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();				    
				    tempValue = headerFormFocus.getValue(TYPE);
			    }
			 
				RuntimeFormWidgetInterface rfDirectedCheck = form.getFormWidgetByName("TMPICK");
				RuntimeFormWidgetInterface rfAssistedCheck = form.getFormWidgetByName("REGRFPICK");
	
			    DataBean detailFocus = form.getFocus();
			    if(!detailFocus.isTempBio())
			    {
			    	detailFocus= (BioBean)detailFocus;
			    	String val = detailFocus.getValue("VALIDATIONROUTINE").toString();
			    	
			    	if(val.equals("POSTPIC01"))
			    	{
			    		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Enabling RFAssisted",100L);
			    		rfAssistedCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			    	}
			    	else
			    		rfAssistedCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			    }
    
	
			
			String TypeValue = null;
			
			if (tempValue != null)
			{
				
				TypeValue = tempValue.toString();
				TypeValue.trim();
				
				form.getFormWidgetByName("GETTYPE").setValue(TypeValue);
				
				
				if(tempValue.equals("PICK"))
				{
					_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Enabling RFDirected",100L);
					rfDirectedCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Disabling RFDirected & RFAssisted",100L);
					rfDirectedCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					rfAssistedCheck.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				}											
		
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","/// " + TYPE + " Dropdown, Value is null",100L);
			}
			
			
		}
		
		catch(Exception e)
		{
			_log.debug("LOG_SYSTEM_OUT","\n\n*** EXCEPTION IS " +e +"\n\n",100L);
			e.printStackTrace();
			return RET_CANCEL;
		}
			
		_log.debug("LOG_DEBUG_EXTENSION_COND_VALIDATIONS","**Exiting DependantDropdownGetValuesOnPreRender",100L);
		return RET_CONTINUE;
    }
}
