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
package com.ssaglobal.scm.wms.wm_conditional_validation.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;


public class ItemLookupForStorer extends ActionExtensionBase {
	//Specific query for an item lookup that only returns the selected owner 
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemLookupForStorer.class);
	protected int execute(ActionContext context, ActionResult result)throws EpiException, UserException{
		
		_log.debug("LOG_SYSTEM_OUT","\n\n*******In ItemLookupForStorer************\n\n",100L);

		//ConditionalValidationDataObject obj = new ConditionalValidationDataObject();
		
		String[] parameters = new String[1];
		parameters[0] = "Owner";
		//String qry = "sku.STORERKEY = '";
		String value = null;        
		String a = "";
		
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	
		RuntimeFormInterface headerForm = state.getCurrentRuntimeForm();
		_log.debug("LOG_SYSTEM_OUT","headerform: "+headerForm.getName(),100L);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		DataBean headerFocus = headerForm.getFocus();
		QBEBioBean headerBioBean = null;

	
		_log.debug("LOG_SYSTEM_OUT","\n\n Before QBEBioBean##### " +headerFocus.getBeanType(),100L);
		
		
		
		
	
		
		
		if (headerFocus instanceof QBEBioBean) {//it is new
			_log.debug("LOG_SYSTEM_OUT","\n\n In QBEBioBean#####",100L);
			headerBioBean = (QBEBioBean)headerFocus;
			headerFocus = uow.getNewBio((QBEBioBean)headerFocus);
			Object widgetStorer= headerBioBean.get("STORERKEY");
	
			//Object widgetStorer = headerFocus.getValue("STORERKEY");
			if(widgetStorer != null)
			{
			_log.debug("LOG_SYSTEM_OUT","\n\n In QBEBioBean value: " +widgetStorer.toString(),100L);
			}
		}
		
		RuntimeFormWidgetInterface widget = headerForm.getFormWidgetByName("STORERKEY");
		_log.debug("LOG_SYSTEM_OUT","\n\n ###Widget NAme: " +widget.getName(),100L);
		_log.debug("LOG_SYSTEM_OUT","\n\n ###Widget NAme: " +widget.getDisplayValue(),100L);
	//	Object widgetStorer = headerFocus.getValue("STORERKEY");
	//	_log.debug("LOG_SYSTEM_OUT","\n\n DataBean value: " +widgetStorer.toString(),100L);

		
		if(widget != null)
		{
		
			_log.debug("LOG_SYSTEM_OUT","\n\n***### val " +widget.getValue().toString(),100L);	

		}

/*		
		//detail Form
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	
	    if (detailForm.getName().equals("wm_conditional_validation_detail_view"))
	    {
	    	_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name is " +detailForm.getName(),100L);
	    }
	    //existing record
	    else 
	    {
			RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
			detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_conditional_validation_detail_toggle_slot"), "wm_conditional_validation_detail_tab"  );
			_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" +detailForm.getName(),100L);	
	    }		
		
	    _log.debug("LOG_SYSTEM_OUT","\n**detailform name " +detailForm.getName(),100L);
		RuntimeFormWidgetInterface widgetTemp= detailForm.getFormWidgetByName("STORERVAL");
		//widgetTemp.setValue(widget.getDisplayValue());
		String dispVal = widgetTemp.getDisplayValue();
		_log.debug("LOG_SYSTEM_OUT","\n**value is " +dispVal,100L);
	*/
		
	  	String queryStr = "sku.STORERKEY = '" + widget.getDisplayValue() + "'";				
 			_log.debug("LOG_SYSTEM_OUT","\nQuery: "+queryStr,100L);
 			Query q = new Query("sku", queryStr, null);
 			BioCollectionBean newFocus = uow.getBioCollectionBean(q); 			 
			
			result.setFocus(newFocus);

	
		
			
			//state,"wm_conditional_validation_modal_form_slot", 0, focus, "wm_conditional_validation_sku");
					
	/*******************************************************************	
		String qry = "sku.STORERKEY = '" + widget.getDisplayValue() + "'";
		_log.debug("LOG_SYSTEM_OUT","qry: "+qry,100L);
		Query loadBiosQry = new Query("sku", qry, null);
		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		if(newFocus.size()<1){
			String [] param = new String[1];
			param[0] = value;
			throw new UserException("WMEXP_OWNER_VALID", param);
		}
		result.setFocus(newFocus);
	*********************************************************************/
/*			//detail Form
		    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
			RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		
		    if (detailForm.getName().equals("wm_conditional_validation_detail_view"))
		    {
		    	_log.debug("LOG_SYSTEM_OUT","\n\n*** Detail Form Name is " +detailForm.getName(),100L);
		    }
		    //existing record
		    else 
		    {
				RuntimeFormInterface toggleForm = state.getRuntimeForm(detailSlot, null);
				detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_conditional_validation_detail_toggle_slot"), "wm_conditional_validation_detail_tab"  );
				_log.debug("LOG_SYSTEM_OUT","\n\n***Detail Form Name" +detailForm.getName(),100L);	
		    }		
			
		    _log.debug("LOG_SYSTEM_OUT","\n**detailform name " +detailForm.getName(),100L);
			RuntimeFormWidgetInterface widgetTemp= detailForm.getFormWidgetByName("STORERVAL");
			//widgetTemp.setValue(widget.getDisplayValue());
			_log.debug("LOG_SYSTEM_OUT","\n**value is " +widgetTemp.getValue().toString(),100L);
	*/		
		return RET_CONTINUE;
	}
}
