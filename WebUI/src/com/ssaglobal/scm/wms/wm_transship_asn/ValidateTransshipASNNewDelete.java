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
package com.ssaglobal.scm.wms.wm_transship_asn;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateTransshipASNNewDelete extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateTransshipASNNewDelete.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Executing ValidateTransshipASNNew",100L);		
		StateInterface state = context.getState();	
		
		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_detail_view",state);
		if(headerForm == null)
			headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_new_detail_view",state);		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Found Header Form:Null",100L);			
		
		//validate header form fields		
		if(headerForm != null){
			Object statusObj = headerForm.getFocus().getValue("STATUS");
			Object verifyFlgObj = headerForm.getFocus().getValue("VERIFYFLG");		
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","statusObj:"+statusObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","verifyFlgObj:"+verifyFlgObj,100L);
			String status = statusObj == null?"":statusObj.toString();
			String verifyFlg = verifyFlgObj == null?"":verifyFlgObj.toString();
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","status:"+status,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","verifyFlg:"+verifyFlg,100L);			
			
			//If Flag = 2 or Status = 9 then details cannot be added or deleted
			if(status.equals("9") || verifyFlg.equals("2")){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Cannot Add Or Delete...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Leaving ValidateTransshipASNNew",100L);					
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_TRANSSHIP_ASN_DETAIL_UNCHANGEABLE",args,context.getState().getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}				
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANASNNEWDEL","Leaving ValidateTransshipASNNew",100L);
		return RET_CONTINUE;
		
	}	
}