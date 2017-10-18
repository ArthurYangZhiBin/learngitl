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
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;


public class TransshipASNButtonNavPicker extends ActionExtensionBase{
//	private ILoggerCategory logger = ApplicationUtil.getLogger(LoadPlanningSearch.class);
	 
		
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		RuntimeFormInterface shellForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(),"wms_list_shell","wms_list_shell",context.getState());
		String nav1 = (String)getParameter("nav1");
		String nav2 = (String)getParameter("nav2");		
		SlotInterface shellSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface currentForm = context.getState().getRuntimeForm(shellSlot,null);
		if(currentForm.getName().equals("wm_transship_asn_header_search_view") || currentForm.getName().equals("wm_transship_asn_header_new_detail_view")){
			context.setNavigation(nav1);						
		}
		else{
			context.setNavigation(nav2);
		}
		return RET_CONTINUE;
	}
}