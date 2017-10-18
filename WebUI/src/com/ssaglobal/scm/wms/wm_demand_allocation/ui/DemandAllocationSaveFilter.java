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
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DemandAllocationSaveFilter extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(DemandAllocationSaveFilter.class);
	
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
	
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		
		//Extension called from Search Form - save the current filter into the usercontext
		userContext.put(USER_CONTEXT_KEY_OWNER_START,form.getFormWidgetByName("STORERKEYSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_OWNER_END,form.getFormWidgetByName("STORERKEYEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ITEM_START,form.getFormWidgetByName("SKUSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ITEM_END,form.getFormWidgetByName("SKUEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERNUM_START,form.getFormWidgetByName("ORDERKEYSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERNUM_END,form.getFormWidgetByName("ORDERKEYEND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERLINE_START,form.getFormWidgetByName("ORDERLINENUMBERSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERLINE_END,form.getFormWidgetByName("ORDERLINENUMBEREND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERLINE_START, form.getFormWidgetByName("ORDERLINENUMBERSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_ORDERLINE_END, form.getFormWidgetByName("ORDERLINENUMBEREND").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_STAGELOC_START, form.getFormWidgetByName("STAGELOCSTART").getDisplayValue());
		userContext.put(USER_CONTEXT_KEY_STAGELOC_END, form.getFormWidgetByName("STAGELOCEND").getDisplayValue());
		return RET_CONTINUE;
		
	}
}
