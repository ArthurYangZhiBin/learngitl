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
package com.ssaglobal.scm.wms.wm_inventory_transaction.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_inventory_request_to_count.ui.InventoryReqToCountUtil;

import java.io.*;

public class PreRenderFormForModalWindow extends FormExtensionBase implements Serializable{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderFormForModalWindow.class);

	protected int preRenderForm(UIRenderContext context,RuntimeNormalFormInterface form) throws EpiException,UserException {
   		

   			_log.debug("LOG_SYSTEM_OUT","\n\n ****In PreRender**** \n\n",100L);
   			StateInterface state = context.getState();
   			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
   			
   			if(context.getServiceManager().getUserContext().containsKey("viewSrc"))
   			{
   			try{  															  
     			ViewSourceDocDataObject obj = (ViewSourceDocDataObject)context.getServiceManager().getUserContext().get("viewSrc");
     			String key= obj.getKey();
     			String lineNum = obj.getLineNumber();
      			String bio = obj.getbio();
      			String perspective = obj.getPerspective();
     			  
      			//_log.debug("LOG_SYSTEM_OUT","\n\nkey: " +key,100L);
     			//_log.debug("LOG_SYSTEM_OUT","\n\nkey: " +key,100L);
     			//_log.debug("LOG_SYSTEM_OUT","\n\nBio: " +bio,100L);
     			//_log.debug("LOG_SYSTEM_OUT","\n\nPers: " +perspective,100L);

     			String queryStr = bio +"." +obj.getkeyAtt() +"='" +key +"' and " +bio +"." +obj.getLNAtt() +"='" +lineNum +"'";
   				
     			_log.debug("LOG_SYSTEM_OUT","\nQuery: "+queryStr,100L);
     			Query q = new Query(bio, queryStr, null);
     			BioCollectionBean newFocus = uow.getBioCollectionBean(q); 
     			if(newFocus.size()<1){
     				String[] param = new String[4];
     				param[0] = obj.getkeyAtt();
     				param[1] = key;
     				param[2] = obj.getLNAtt();
     				param[3] = lineNum;
     				throw new FormException("WMEXP_NO_SOURCE_DOCUMENT", param);
     			}
     			else
     			{	
   			    DataBean focus = (DataBean)state.getDefaultUnitOfWork().getBioCollectionBean(q).get("0");
   				form.setFocus(state,"wm_inventory_transaction_popup_form_slot", 0, focus, perspective);
     			}
   				//_log.debug("LOG_SYSTEM_OUT","\n\n Name of form: " +form.getName(),100L);

   		   			  
   				}  catch (Exception x) {
   	   			x.printStackTrace();
   	   			context.getServiceManager().getUserContext().remove("viewSrc");
   	   			throw new UserException("System_Error",new Object[1] );		
   				}
   			}
   		context.getServiceManager().getUserContext().remove("viewSrc");	
   		_log.debug("LOG_SYSTEM_OUT","\n\n***Before exiting\n\n",100L);
   		return RET_CONTINUE;
   	}
}
