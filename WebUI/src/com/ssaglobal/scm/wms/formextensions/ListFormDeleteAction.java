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
package com.ssaglobal.scm.wms.formextensions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;


public class ListFormDeleteAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ListFormDeleteAction.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ListFormDeleteAction",100L);		
		StateInterface state = context.getState();	
		String targetFormName = (String)getParameter("targetFormName");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		
		//Get Target Form		
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell",targetFormName,tabs,state);		
		if(form != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Target Form:"+form.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Target Form:Null",100L);						
		
		//If form is avaliable and items are selected then delete		
		if(		form != null && 
				form.isListForm() && 
				((RuntimeListFormInterface)form).getSelectedItems() != null &&
				((RuntimeListFormInterface)form).getSelectedItems().size() > 0){			
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","deleteing items...",100L);
			Iterator selectedItemsItr = ((RuntimeListFormInterface)form).getSelectedItems().iterator();			
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioBean bio = null;
			try {
				//jp.answerlink.291161.begin
				uow.clearState();
				uow.saveUOW();
				//jp.answerlink.291161.end

				while(selectedItemsItr.hasNext()){
					bio = (BioBean)selectedItemsItr.next();
					bio.delete();
				}
				uow.saveUOW();
				((RuntimeListFormInterface)form).setSelectedItems(null);
			} catch (EpiDataException e) {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ListFormDeleteAction",100L);				
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("ERROR_DELETING_BIO",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} catch (EpiException e) {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ListFormDeleteAction",100L);				
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("ERROR_DELETING_BIO",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		else{
			if(form == null){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Could Not Find Form "+targetFormName+"...",100L);				
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Nothing selected...",100L);				
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ListFormDeleteAction",100L);		
		return RET_CONTINUE;
		
	}	
}