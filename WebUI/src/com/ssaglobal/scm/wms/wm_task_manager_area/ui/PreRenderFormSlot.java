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

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.DataBean;

public class PreRenderFormSlot extends FormExtensionBase implements Serializable{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderFormSlot.class);
 	protected int preRenderForm(UIRenderContext context,RuntimeNormalFormInterface form) throws EpiException,UserException {
   		
 		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing PreRenderFormSlot",100L);	
			
			String q= null;
			StateInterface state = context.getState();
			UnitOfWorkBean uow = null;
		
			BioRef bioRef= null;
			
			if(context.getServiceManager().getUserContext().containsKey("formChoice"))
			{
				try{ 
				AreaDataObject obj = (AreaDataObject)context.getServiceManager().getUserContext().get("formChoice");
				String perspective= obj.getPerspective();
					if(perspective.equals("wm_task_manager_area_detail"))
					{
						QBEBioBean qbe= null;
						uow = state.getTempUnitOfWork();
						qbe = uow.getQBEBioWithDefaults("wm_areadetail");						
						form.setFocus(state, "wm_task_manager_area_form_slot", 0, qbe, perspective);
					}
					else
					{
					uow = state.getDefaultUnitOfWork();
					String key = obj.getAreaKey();
									
					q= "wm_areadetail.AREAKEY='" +key +"'";
					_log.debug("LOG_SYSTEM_OUT","\n\nq: " +q,100L);
					Query query= new Query("wm_areadetail", q, null);				
					BioCollectionBean newFocus = uow.getBioCollectionBean(query);
						if(newFocus.size()<1){
							throw new FormException("WMEXP_NO_DETAIL_RECORDS", new Object[1]);
							}
						form.setFocus(state, "wm_task_manager_area_form_slot", 0, newFocus, perspective);
					}	
				 }
				 catch (Exception x) 
				 	{
	   	   			x.printStackTrace();
	   	   			context.getServiceManager().getUserContext().remove("formChoice");
	   	   			throw new UserException("System_Error",new Object[1] );		
	   				}
				
				
		
			}//end if
			context.getServiceManager().getUserContext().remove("formChoice");
			_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting PreRenderFormSlot",100L);	
			return RET_CONTINUE;
 	}			

}
