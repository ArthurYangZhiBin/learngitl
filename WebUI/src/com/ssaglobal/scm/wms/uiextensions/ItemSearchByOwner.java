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
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.security.InvalidParameterException;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ItemSearchByOwner extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemSearchByOwner.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_ITEMSEARCHBYOWNER","Executing ItemSearchByOwner",100L);		
		String formName = getParameter("formName").toString();
		String widgetName = getParameter("widgetName").toString();
		String bioName = getParameter("bioName").toString();
		StateInterface state = context.getState();						
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell",formName,state);
		if(detailForm == null){
			_log.debug("LOG_DEBUG_EXTENSION_ITEMSEARCHBYOWNER","Could not find form:"+formName,100L);
			throw new InvalidParameterException("Could not find form:"+formName);			
		}
		String owner = (String)detailForm.getFocus().getValue(widgetName);
		_log.debug("LOG_DEBUG_EXTENSION_ITEMSEARCHBYOWNER","owner:"+owner,100L);						
		String qry = null;				
		
		if(owner != null && !("".equalsIgnoreCase(owner))){
			qry= bioName+".STORERKEY = '"+owner+"'";
		}				
		_log.debug("LOG_DEBUG_EXTENSION_ITEMSEARCHBYOWNER","qry:"+qry,100L);	
		Query loadBiosQry = new Query(bioName, qry, ""); 				
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
		BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);					
		result.setFocus((DataBean)bioCollection);			
		_log.debug("LOG_DEBUG_EXTENSION_ITEMSEARCHBYOWNER","Leaving ItemSearchByOwner",100L);		
		return RET_CONTINUE;
		
	}
	
	
}