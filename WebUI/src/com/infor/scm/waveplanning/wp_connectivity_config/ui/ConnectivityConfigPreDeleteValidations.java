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
package com.infor.scm.waveplanning.wp_connectivity_config.ui;

import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class ConnectivityConfigPreDeleteValidations extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConnectivityConfigPreDeleteValidations.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	

	_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**Executing ConnectivityConfigPreDeleteValidations",100L);	
			String value ="";
		   boolean isExist = false;
		   
		StateInterface state = context.getState();   
		RuntimeFormInterface shellForm = context.getSourceWidget().getForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot("slot1"), null);
		
		ArrayList itemsSelected = listForm.getAllSelectedItems();
		if(itemsSelected != null)
		{
			for(int i = 0; i < itemsSelected.size(); i++)
			{
				DataBean bean = (DataBean)(itemsSelected.get(i));
				value = bean.getValue("WHSEID").toString();
				_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**Facility: " +value,100L);
		
	    
		//HttpSession session = context.getState().getRequest().getSession();
		//WebContext ctxt = (WebContext)session.getAttribute(Constants.SSA_WEB_CONTEXT);
		
				//Commenting this since we do not currently have a way of associating a user with a facility
//		UserManagementService ums;
//		try {
//			int size=0;
//			ums = UMSHelper.getUMS();
//			UserStore userstore = ums.getDefaultUserStore();
//			SearchCriteria criteria = new SearchCriteria();			
//			//correct version
//			criteria.addCondition("facility",value,SearchCondition.EXACT);
//			
//
//			Enumeration userEnum = userstore.search(criteria, ums.getDefaultUserStore().getRootHierarchy().getDN(),true, -1,-1);
//			//ArrayList fac=new ArrayList();
//			while(userEnum.hasMoreElements())
//			{  
//				User user = (User)userEnum.nextElement();				
//				_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**user: " +user.getId(),100L);
//				isExist =true;
//
//			size++;
//			}
//			_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**size: " +String.valueOf(size),100L);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**Val: " +value +" " +isExist,100L);
//		
//			if(isExist == true)
//			{
//			String [] param = new String[1];
//			param[0] = value;
//			String errorMsg = getTextMessage("WPEXP_FACILITY_UNABLE_TO_DELETE",param,state.getLocale());
//			_log.error("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**Cannot delete warehouse " +value,100L);						
//			throw new UserException(errorMsg,new Object[0]);
//			}
		}	
		
	}
	
		_log.debug("LOG_DEBUG_EXTENSION_CONNECTIVITY_CONFIG_PREDELETE","**Exiting ConnectivityConfigPreDeleteValidations",100L);	
		return RET_CONTINUE;
	}	
}
