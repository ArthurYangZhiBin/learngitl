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
package com.ssaglobal.scm.wms.wm_reports.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class ReportsQuery extends com.epiphany.shr.ui.action.ActionExtensionBase {


	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
//		   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		   result.setFocus(SecurityUtil.getUserAccessableReports(context.getState(),getParameterString("orderBy"),getParameterString("query"),result.getFocus()));
//			Query loadBiosQryA = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.SERIALKEY >= 1", "");																
//		   result.setFocus(uow.getBioCollectionBean(loadBiosQryA));
		   
	       return RET_CONTINUE;
	    }
}

