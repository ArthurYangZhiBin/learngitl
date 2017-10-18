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

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
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

public class ConvertFlowThruOrderCloseModal extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertFlowThruOrderCloseModal.class);
	
	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Executing ConvertFlowThruOrderCloseModal",100L);		
		StateInterface state = ctx.getState();	
		String initialConf = (String)getParameter("initialConf");
		String routeConf = (String)getParameter("routeConf");
		String routeCancel = (String)getParameter("routeCancel");				
		_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Got Params initialConf:"+initialConf+" And routeConf:"+routeConf+" And routeCancel:"+routeCancel,100L);
		state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
		state.getRequest().getSession().removeAttribute("didCloseRetainRouteConfModal");
		state.getRequest().getSession().removeAttribute("didCancelRetainRouteModal");
	
		if(initialConf != null && initialConf.equals("true")){			
			_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Setting Session Attr didCloseInitialConfModal to true",100L);
			state.getRequest().getSession().setAttribute("didCloseInitialConfModal","true");
		}
		else if(routeConf != null && routeConf.equals("true")){			
			_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Setting Session Attr didCloseRetainRouteConfModal to true",100L);
			state.getRequest().getSession().setAttribute("didCloseRetainRouteConfModal","true");
		}
		else if(routeCancel != null && routeCancel.equals("true")){			
			_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Setting Session Attr didCancelRetainRouteModal to true",100L);
			state.getRequest().getSession().setAttribute("didCancelRetainRouteModal","true");
		}
		else{
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERLINENUMBER");
			state.getRequest().getSession().removeAttribute("CONVERTFTOORDERKEY");
			state.getRequest().getSession().removeAttribute("CONVERTFTOLOADID");
		}
		_log.debug("LOG_DEBUG_EXTENSION_CONVFTOCLOSEMOD","Exiting ConvertFlowThruOrderCloseModal",100L);		
		return RET_CONTINUE;
}
}
