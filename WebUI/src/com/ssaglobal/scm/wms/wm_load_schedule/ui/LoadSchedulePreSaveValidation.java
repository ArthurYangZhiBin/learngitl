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
package com.ssaglobal.scm.wms.wm_load_schedule.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.ssaglobal.scm.wms.wm_ws_defaults.favorites.FavoritesGoAction;

public class LoadSchedulePreSaveValidation extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadSchedulePreSaveValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		LoadScheduleValidationDataObject data = new LoadScheduleValidationDataObject();

		StateInterface state = context.getState();

		//_log.debug("LOG_SYSTEM_OUT","\n\n****** In LoadSchedulePreSaveValidation*******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","Executing LoadSchedulePreSaveValidation",100L);	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);

		//get header data
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
		boolean isNewHeader = false;
		boolean isNewDetail = false;
		if (headerFocus.isTempBio())
		{
			isNewHeader = true;
		}
		//populate data object
		//_log.debug("LOG_SYSTEM_OUT","\n\n****** Populating data objects*******\n\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","** Populating data objects",100L);
		data.setLoadScheduleKey((String) headerFocus.getValue("LOADSCHEDULEKEY"));
		data.setOwner((String) headerFocus.getValue("STORER"));
		data.setScheduleType((String) headerFocus.getValue("SCHEDULETYPE"));
		data.setDayOfWeek((String) headerFocus.getValue("DAYOFWEEK"));
		data.setRoute((String) headerFocus.getValue("ROUTE"));
		Object objOutbund = headerFocus.getValue("OUTBOUNDLANE");
		if (objOutbund != null && !objOutbund.toString().equalsIgnoreCase(""))
		{
			data.setOutbound(headerFocus.getValue("OUTBOUNDLANE").toString());
		}
		//get detail data
		DataBean detailFocus = null;
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		//_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name: " + detailForm.getName() + "**\n\n",100L);

		//New Record
		if (!detailForm.getName().equals("wm_load_schedule_detail_view"))
		{
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_load_schedule_detail_toggle"),
												"wm_load_shcedule_detail_toggle_tab");
			_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" + detailForm.getName(),100L);
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Detail Form: " +detailForm.getName(),100L);
		}

		if(detailForm.getFocus().isBio())
		{	
			detailFocus = detailForm.getFocus();		
			//it is for insert header
			if (detailFocus.isTempBio())
			{
			isNewDetail = true;
			}
			
			Object objOhtype = detailFocus.getValue("OHTYPE");
			if (objOhtype != null && !objOhtype.toString().equalsIgnoreCase(""))
			{
			data.setOrderHandlingType(objOhtype.toString());
			}
			
			data.setStop(detailFocus.getValue("STOP").toString());
			data.setCustomer(detailFocus.getValue("CONSIGNEEKEY").toString());
			detailFocus.setValue("STOP", data.getStop().toUpperCase());
		}
		
		//validate header owner
		boolean validate = false;
		try
		{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating owner",100L);
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Validating header owner: ",100L);
			validate = LoadSchedulePreSaveValidationUtil.isValidOwner(data);
		} catch (DPException dpE)
		{
			dpE.printStackTrace();
			throw new UserException("System_Error", new Object[1]);
		}
		if (!validate)
		{
			//String[] param = new String[1];
			//param[0] = data.getOwner();
			//throw new UserException("WMEXP_OWNER_VALIDATION", param);
			
			 	String args[] = new String[1];
			 	args[0]= data.getOwner();
				String errorMsg = getTextMessage("WMEXP_OWNER_VALIDATION",args,state.getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","Invalid Owner- Exiting",100L);						
				throw new UserException(errorMsg,new Object[0]);			 
		}

		//validate outbound***********************
		validate = false;
		if (objOutbund != null && !objOutbund.toString().equalsIgnoreCase(""))
		{
			try
			{
				//_log.debug("LOG_SYSTEM_OUT","\n** Validating outbound",100L);
				validate = LoadSchedulePreSaveValidationUtil.isValidLocation(data);
			} catch (DPException dpE)
			{
				dpE.printStackTrace();
				throw new UserException("System_Error", new Object[1]);
			}
			if (!validate)
			{
				//String[] param = new String[1];
				//param[0] = data.getOutbound();
				//throw new UserException("WMEXP_LANEKEY_VALIDATION", param);
			 	String args[] = new String[1];
			 	args[0]= data.getOutbound();
				String errorMsg = getTextMessage("WMEXP_LANEKEY_VALIDATION",args,state.getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","Invalid LANEKEY- Exiting",100L);						
				throw new UserException(errorMsg,new Object[0]);
			}
		}

		//validate storer, schedule type, day of week, route combination*********************
		validate = false;
		if (isNewHeader)
		{
			try
			{
				validate = LoadSchedulePreSaveValidationUtil.isHeaderCombUnique(data);
			} catch (DPException dpE)
			{
				dpE.printStackTrace();
				throw new UserException("System_Error", new Object[1]);
			}
			if (!validate)
			{
				String[] param = new String[4];
				param[0] = data.getOwner();
				param[1] = data.getScheduleType();
				param[2] = data.getDayOfWeek();
				param[3] = data.getRoute();
			 	
				String errorMsg = getTextMessage("WMEXP_HEADER_COMBINATION",param,state.getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Invalid Header Owner Item COmbination- Exiting",100L);						
				throw new UserException(errorMsg,new Object[0]);
				
				
			}
		}

		//Detail level Validations
		if(detailForm.getFocus().isBio())
		{
		//validate customer
		validate = false;
		try
		{
			//_log.debug("LOG_SYSTEM_OUT","\n** Validating customer",100L);
			validate = LoadSchedulePreSaveValidationUtil.isValidCustomer(data);
		} catch (DPException dpE)
		{
			dpE.printStackTrace();
			throw new UserException("System_Error", new Object[1]);
		}
		if (!validate)
		{
			String[] param = new String[1];
			param[0] = data.getCustomer();
			//throw new UserException("WMEXP_CONSIGNEE_VALIDATION", param);
			String errorMsg = getTextMessage("WMEXP_CONSIGNEE_VALIDATION",param,state.getLocale());
			_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Invalid Customer- Exiting",100L);						
			throw new UserException(errorMsg,new Object[0]);
		}

		//validate consignee key, ohtype combination unique for this header record*********************
		try
		{
			if (isNewDetail || ((BioBean)detailFocus).hasBeenUpdated("OHTYPE") || ((BioBean)detailFocus).hasBeenUpdated("CONSIGNEEKEY")) //or changes customer or ohtype
			{
				try
				{
					validate = LoadSchedulePreSaveValidationUtil.isOhtypeUnique(data);
				} catch (DPException dpE)
				{
					dpE.printStackTrace();
					throw new UserException("System_Error", new Object[1]);
				}
				if (!validate)
				{
					String[] param = new String[3];
					param[0] = data.getCustomer();
					param[1] = data.getOrderHandlingType();
					param[2] = data.getLoadScheduleKey();
					//throw new UserException("WMEXP_DETAIL_COMBINATION", param);
					String errorMsg = getTextMessage("WMEXP_DETAIL_COMBINATION",param,state.getLocale());
					_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Invalid detail Combination- Exiting",100L);						
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		} catch (EpiDataInvalidAttrException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException("System_Error", new Object[1]);
		} 

		//validate week day *******************************************
		validate = false;
		if (isNewHeader)
		{
			try
			{
				//_log.debug("LOG_SYSTEM_OUT","\n** Day of Week",100L);
				validate = LoadSchedulePreSaveValidationUtil.isDayOfWeekScheduled(data);
			} catch (DPException dpE)
			{
				dpE.printStackTrace();
				throw new UserException("System_Error", new Object[1]);
			}
			if (!validate)
			{
				String[] param = new String[4];
				param[0] = data.getOwner();
				param[1] = data.getScheduleType();
				param[2] = data.getOrderHandlingType();
				param[3] = data.getCustomer();
				//throw new UserException("WMEXP_SCHEDULE", param);
				String errorMsg = getTextMessage("WMEXP_SCHEDULE",param,state.getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_LOADSCH","**Invalid schedule- Exiting",100L);						
				throw new UserException(errorMsg,new Object[0]);				
			}
		}
		}
		return RET_CONTINUE;
	}

}
