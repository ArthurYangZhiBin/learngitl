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

package com.ssaglobal.scm.wms.wm_demand_allocation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * DemandAllocationRestoreFilterNTINUE, RET_CANCEL
 */

public class DemandAllocationRestoreFilter extends FormExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(DemandAllocationRestoreFilter.class);
	
	private static String USER_CONTEXT_KEY_OWNER_START = "user.ctx.key.owner.start";
	private static String USER_CONTEXT_KEY_OWNER_END = "user.ctx.key.owner.end";
	private static String USER_CONTEXT_KEY_ITEM_START = "user.ctx.key.item.start";
	private static String USER_CONTEXT_KEY_ITEM_END = "user.ctx.key.item.end";
	private static String USER_CONTEXT_KEY_ORDERNUM_START = "user.ctx.key.ordernum.start";
	private static String USER_CONTEXT_KEY_ORDERNUM_END = "user.ctx.key.ordernum.end";
	private static String USER_CONTEXT_KEY_ORDERLINE_START = "user.ctx.key.orderline.start";
	private static String USER_CONTEXT_KEY_ORDERLINE_END = "user.ctx.key.orderline.end";
	private static String USER_CONTEXT_KEY_STAGELOC_START = "user.ctx.key.stageloc.start";
	private static String USER_CONTEXT_KEY_STAGELOC_END = "user.ctx.key.stageloc.end";
	private static String SESSION_KEY_DO_FILTER = "session.key.do.filter";
	
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {		
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if(context.getState().getRequest().getSession().getAttribute(SESSION_KEY_DO_FILTER) != null){
			form.getFormWidgetByName("STORERKEYSTART").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_OWNER_START));
			form.getFormWidgetByName("STORERKEYEND").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_OWNER_END));
			form.getFormWidgetByName("SKUSTART").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ITEM_START));
			form.getFormWidgetByName("SKUEND").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ITEM_END));
			form.getFormWidgetByName("ORDERKEYSTART").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ORDERNUM_START));
			form.getFormWidgetByName("ORDERKEYEND").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ORDERNUM_END));
			form.getFormWidgetByName("ORDERLINENUMBERSTART").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ORDERLINE_START));
			form.getFormWidgetByName("ORDERLINENUMBEREND").setDisplayValue((String)userContext.get(USER_CONTEXT_KEY_ORDERLINE_END));
			form.getFormWidgetByName("STAGELOCSTART").setDisplayValue((String) userContext.get(USER_CONTEXT_KEY_STAGELOC_START));
			form.getFormWidgetByName("STAGELOCEND").setDisplayValue((String) userContext.get(USER_CONTEXT_KEY_STAGELOC_END));
		}
		else{
			context.getState().getRequest().getSession().setAttribute(SESSION_KEY_DO_FILTER,"true");
		}
		return RET_CONTINUE;
	}	
}
