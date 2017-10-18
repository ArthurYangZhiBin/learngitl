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
package com.ssaglobal.scm.wms.uiextensions;
import java.util.ArrayList;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class WMListDeleteAction extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WMListDeleteAction.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Executing WMListDeleteAction",100L);		
		StateInterface state = context.getState();			
		ArrayList formNames = (ArrayList)getParameter("formNames");
		ArrayList tabs = (ArrayList)getParameter("tabs");
		String shellForm = (String)getParameter("shellForm");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		uow.clearState(); //Clear State So That Dirty Bios Are Not Persisted Unintentionally
		if(formNames != null){
			for(int i = 0; i < formNames.size(); i++){
				RuntimeFormInterface form = null;
				String formName = (String)formNames.get(i);
				if(tabs != null && tabs.size() > 0){
					form = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,formName,tabs,state);
				}
				else{
					form = FormUtil.findForm(state.getCurrentRuntimeForm(),shellForm,formName,state);
				}
				
				if(form != null){
					_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Found Form:"+form.getName(),100L);					
				}
				else
					_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Found Form:Null",100L);					
								
				if(form != null){					
					if(form.isListForm()){
						_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Form "+form.getName()+" is a list form",100L);						
						RuntimeListFormInterface listForm = (RuntimeListFormInterface)form;
						ArrayList selectedItems = listForm.getSelectedItems(); 
						if(selectedItems != null && selectedItems.size() > 0){
							_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Deleting "+selectedItems.size()+" records...",100L);							
							for(int j = 0; j < selectedItems.size(); j++){
								BioBean bio = (BioBean)selectedItems.get(j);
								try {
									bio.delete();
								} catch (EpiDataException e) {
									e.printStackTrace();
									String args[] = new String[0]; 													
									String errorMsg = getTextMessage("ERROR_DELETING_BIO",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							}
						}
						else{
							_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Nothing selected for deletion...",100L);							
						}
					}
					else{
						_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Form "+form.getName()+" is not a list form",100L);						
						//If this is a new record then it cannot be deleted...
						if(form.getFocus().isTempBio()){
							_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Focus is temp BIO cannot delete...",100L);
							_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Leaving WMListDeleteAction",100L);							
							String args[] = new String[0]; 													
							String errorMsg = getTextMessage("WMEXP_CANNOT_DELETE_NEW_REC",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);							
						}
						else{
							try {
								_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Deleteing BIO attaced to Normal Form...",100L);								
								((BioBean)form.getFocus()).delete();
							} catch (EpiDataException e) {								
								e.printStackTrace();
								String args[] = new String[0]; 													
								String errorMsg = getTextMessage("ERROR_DELETING_BIO",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);	
							}
						}
					}
				}
			}
			try {
				uow.saveUOW();
			} catch (EpiException e) {
				e.printStackTrace();
				String args[] = new String[0]; 													
				String errorMsg = getTextMessage("ERROR_DELETING_BIO",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);	
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_WMListDeleteAction","Leaving WMListDeleteAction",100L);
		return RET_CONTINUE;
		
	}	
}