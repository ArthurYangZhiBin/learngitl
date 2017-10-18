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
package com.ssaglobal.scm.wms.wm_thru_order;
import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;

public class HeaderDetailSaveThruOrder extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(HeaderDetailSaveThruOrder.class);
	
	public HeaderDetailSaveThruOrder() { 
		_log.info("EXP_1","HeaderDetailSave Instantiated!!!",  SuggestedCategory.NONE);
	}
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","Executing HeaderDetailSave",100L);
		//Get user entered criteria	
		String shellSlot1 = (String)getParameter("shellSlot1");
		String shellSlot2 = (String)getParameter("shellSlot2");
		String toggleFormSlot = (String)getParameter("toggleSlot");
		String detailBiocollection = (String)getParameter("detailBiocollection");
		String detailFormTab = (String)getParameter("detailFormTab");
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","********shellSlot1="+shellSlot1,100L);
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","********shellSlot2="+shellSlot2,100L);
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","********toggleFormSlot="+toggleFormSlot,100L);
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","********detailBiocollection="+detailBiocollection,100L);
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","********detailFormTab="+detailFormTab,100L);
		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		//get header data
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if(headerForm.isListForm()){
			String [] desc = new String[1];
			desc[0] = "";
			throw new UserException("List_Save_Error",desc);			
		}
		DataBean headerFocus = headerForm.getFocus();
		
		
		//get detail data
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);		//HC
		for(int i = 0; i < shellForm.getSubSlots().size(); i++){
			_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","Slot:"+((SlotInterface)shellForm.getSubSlots().get(i)).getName(),100L);			
		}
		_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","Detail Slot:"+detailSlot.getName(),100L);		
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		
		BioBean headerBioBean = null;
		if (headerFocus.isTempBio()) {//it is for insert header
			_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","inserting header ******",100L);			
			try {
				headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
			} catch (EpiException e) {				
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			
			DataBean detailFocus = detailForm.getFocus();			
			if(detailFocus != null){
				QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
				detailBioBean.set("STORERKEY",headerBioBean.get("STORERKEY"));
				if(session.getAttribute("THRUORDERORIGQTY") != null){
					String origQty = (String)session.getAttribute("THRUORDERORIGQTY");
					detailBioBean.set("OPENQTY",new BigDecimal(origQty));
					detailBioBean.set("QTYTOPROCESS",new BigDecimal(origQty));
					session.removeAttribute("THRUORDERORIGQTY");
				}
				headerBioBean.addBioCollectionLink(detailBiocollection, detailBioBean);
			}
		} else {//it is for update header			
			_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","updating header ******",100L);
			headerBioBean = (BioBean)headerFocus;		
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);
			for(int i = 0; i < detailForm.getSubSlots().size(); i++){
				_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","Slot:"+((SlotInterface)detailForm.getSubSlots().get(i)).getName(),100L);				
			}
			_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","Toggle Slot:"+toggleSlot.getName(),100L);			
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, detailFormTab);	
			DataBean detailFocus = detailTab.getFocus();
			
			if (detailFocus != null && detailFocus.isTempBio()) {
				_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","*****detaiFocus is tempbio="+detailFocus.isTempBio(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","inserting detail ******",100L);				
				QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
				detailBioBean.set("STORERKEY",headerBioBean.get("STORERKEY"));
				if(session.getAttribute("THRUORDERORIGQTY") != null){
					String origQty = (String)session.getAttribute("THRUORDERORIGQTY");
					detailBioBean.set("OPENQTY",new BigDecimal(origQty));
					detailBioBean.set("QTYTOPROCESS",new BigDecimal(origQty));
					session.removeAttribute("THRUORDERORIGQTY");
				}
				headerBioBean.addBioCollectionLink(detailBiocollection, detailBioBean);		//HC
			} else {	
				BioBean detailBioBean = (BioBean)detailFocus;
				if(session.getAttribute("THRUORDERORIGQTY") != null){
					String origQty = (String)session.getAttribute("THRUORDERORIGQTY");
					detailBioBean.set("OPENQTY",new BigDecimal(origQty));
					detailBioBean.set("QTYTOPROCESS",new BigDecimal(origQty));
					session.removeAttribute("THRUORDERORIGQTY");
				}
				_log.debug("LOG_DEBUG_EXTENSION_SAVETHRUORDER","updating detail ******",100L);				
			}
			
		}
			
		try {
			uow.saveUOW(true);
		} catch (EpiException e) {		
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		uow.clearState();
		result.setFocus(headerBioBean);
			
		return RET_CONTINUE;
		
	}
}
