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
package com.ssaglobal.scm.wms.wm_tariff;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class TariffHeaderDetailSave extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TariffHeaderDetailSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","Executing HeaderDetailSave",100L);	 	
		//Get user entered criteria	
		String shellSlot1 = (String)getParameter("shellSlot1");
		String shellSlot2 = (String)getParameter("shellSlot2");
		String toggleFormSlot = (String)getParameter("toggleSlot");
		String detailBiocollection = (String)getParameter("detailBiocollection");
		String detailFormTab = (String)getParameter("detailFormTab");
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","********shellSlot1="+shellSlot1,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","********shellSlot2="+shellSlot2,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","********toggleFormSlot="+toggleFormSlot,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","********detailBiocollection="+detailBiocollection,100L);
		_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","********detailFormTab="+detailFormTab,100L);
		
		try{
			StateInterface state = context.getState();
			
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			
			//get header data
//			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		//HC
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			DataBean headerFocus = headerForm.getFocus();
			
			//get detail data
//			SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
			SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);		//HC
			RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
			
			
			
			BioBean headerBioBean = null;
			if (headerFocus.isTempBio()) {
				//it is for insert header				
				_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","inserting header ******",100L);
				headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);		   
				DataBean detailFocus = detailForm.getFocus();			
				if(detailFocus != null){
					String tariffDetailKey = new KeyGenBioWrapper().getKey("tariffdetail");
					_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","TARIFFDETAILKEY:"+tariffDetailKey,100L);					
					detailFocus.setValue("TARIFFDETAILKEY",tariffDetailKey);			
					headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean)detailFocus);		//HC
				}
			} else {
				//it is for update header
				_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","updating header ******",100L);				
				headerBioBean = (BioBean)headerFocus;
				SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);						//HC
				RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, detailFormTab);		//HC
				DataBean detailFocus = detailTab.getFocus();
				
				if (detailFocus != null && detailFocus.isTempBio()) {
					_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","*****detaiFocus is tempbio="+detailFocus.isTempBio(),100L);
					_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","inserting detail ******",100L);					
					if(!((QBEBioBean)detailFocus).isEmpty()){
						String tariffDetailKey = new KeyGenBioWrapper().getKey("tariffdetail");
						_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","TARIFFDETAILKEY:"+tariffDetailKey,100L);						
						detailFocus.setValue("TARIFFDETAILKEY",tariffDetailKey);
						headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean)detailFocus);		//HC
					}
				} else {						
					_log.debug("LOG_DEBUG_EXTENSION_TARIFFSAVE","updating detail ******",100L);					
				}
				
			}
			uow.saveUOW(true);
			uow.clearState();
			result.setFocus(headerBioBean);
			
		}catch (UnitOfWorkException e){
			_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		} catch (EpiException e) {			
			e.printStackTrace();
			throwUserException(e, "ERROR_SAVING_BIO", null);
		}		
	
		return RET_CONTINUE;
		
	}
}
