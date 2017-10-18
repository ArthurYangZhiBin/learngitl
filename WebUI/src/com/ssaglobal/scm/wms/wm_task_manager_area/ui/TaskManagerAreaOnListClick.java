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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;

import java.io.Serializable;
import java.util.Iterator;



import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;



public class TaskManagerAreaOnListClick extends com.epiphany.shr.ui.action.ActionExtensionBase implements Serializable{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskManagerAreaOnListClick.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {

		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing TaskManagerAreaOnListClick",100L);	

		AreaDataObject obj = new AreaDataObject();		
		
		StateInterface state= context.getState();
		RuntimeFormInterface currentList= state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = currentList.getParentForm(state);


		//wm_list_shell_task_manager_area Toolbar
		
		if(currentList.getName().equals("wm_list_shell_task_manager_area Toolbar"))
		{
			//NEW
			obj.setPerspective("wm_task_manager_area_detail");
			UnitOfWorkBean uowb = state.getTempUnitOfWork();
			context.getServiceManager().getUserContext().put("formChoice", obj);
		}
		else
		{
			//Detail View
			obj.setPerspective("wm_task_manager_area_list_view");
			//obj.setBioRef(state.getBucketValueString("listTagBucket"));
	       	String bioRefString = state.getBucketValueString("listTagBucket");
	        BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
	        UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
	        com.epiphany.shr.ui.model.data.BioBean bioBean = null;
	        try
	        {
	        	
	            bioBean = uowb.getBioBean(bioRef);
	            String key= bioBean.getValue("AREAKEY").toString();
	            obj.setAreaKey(key);
	    		context.getServiceManager().getUserContext().put("formChoice", obj);
	        }
	        catch(BioNotFoundException bioEx)
	        {
	            _logger.error(bioEx);
	            throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
	        }
		}
		
		
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting TaskManagerAreaOnListClick",100L);			
		return RET_CONTINUE;
		
	}

	private void setObj(AreaDataObject obj) {
		// TODO Auto-generated method stub
		obj.setPerspective("wm_task_manager_area_list_view");
	}


}
