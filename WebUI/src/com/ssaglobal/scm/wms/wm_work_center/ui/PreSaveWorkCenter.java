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
package com.ssaglobal.scm.wms.wm_work_center.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class PreSaveWorkCenter extends ActionExtensionBase{
	  protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveWorkCenter.class);
	  
	  public PreSaveWorkCenter() { 
	      _log.info("EXP_1","PreSaveWorkCenter!!!",  SuggestedCategory.NONE);
	  }
	  protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
	  	_log.debug("LOG_SYSTEM_OUT","\n\n*******COnverting to upper case************\n\n",100L);
	  	RuntimeFormInterface form= null;
	  	RuntimeFormWidgetInterface widget= null; 
	  	
	  	ArrayList attr = (ArrayList) getParameter("WidgetName");
	  	ArrayList slots = (ArrayList) getParameter("SlotName");
	  			
	  	Iterator attrItr = attr == null?null:attr.iterator();
		Iterator slotItr = slots == null?null:slots.iterator();	
		
		
	  	StateInterface state = context.getState();
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	  	_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of detail form " +detailForm.getName(),100L);

		for(int i = 0; i < slots.size(); i++){
		form= state.getRuntimeForm(detailForm.getSubSlot(slots.get(i).toString()), null);
		DataBean focus= form.getFocus();
		
		if(focus.isTempBio())
			focus= (QBEBioBean)focus;
		else
			focus= (BioBean)focus;
			
		widget= form.getFormWidgetByName(attr.get(i).toString());
		focus.setValue(attr.get(i).toString(), widget.getValue().toString().toUpperCase());
		}
	  	
	    
	    return RET_CONTINUE;
}
}