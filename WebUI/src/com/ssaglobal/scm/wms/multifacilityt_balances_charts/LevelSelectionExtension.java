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
package com.ssaglobal.scm.wms.multifacilityt_balances_charts;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import javax.servlet.http.HttpSession;


public class LevelSelectionExtension extends com.epiphany.shr.ui.action.ActionExtensionBase{
	   protected int execute(ActionContext context, ActionResult result)throws EpiException {

	 try {		 			   
  	   	StateInterface state = context.getState();  	   		
  	   	Object level = context.getSourceWidget().getValue();
  	   	if(level == null){
  	   		return RET_CANCEL;
  	   	}  	     	   		
		HttpSession session = state.getRequest().getSession();						
		session.setAttribute("LEVEL", level);
		
	  } catch(Exception e) {
	     e.printStackTrace();
	     return RET_CANCEL;          
	  } 
	  
	  return RET_CONTINUE;
	}
}
