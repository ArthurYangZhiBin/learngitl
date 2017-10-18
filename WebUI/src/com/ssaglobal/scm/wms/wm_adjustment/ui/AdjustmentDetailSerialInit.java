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


package com.ssaglobal.scm.wms.wm_adjustment.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class AdjustmentDetailSerialInit extends com.epiphany.shr.ui.action.ActionExtensionBase {

	//private static final String LISTSLOT1 = "list_slot_1";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailSerialInit.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		StateInterface state = context.getState();
		RuntimeFormInterface parent=state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		
		//RuntimeFormInterface shell = parent.getParentForm(state);
		RuntimeFormInterface shell = FormUtil.findForm(parent, "wms_list_shell", "wms_list_shell", state);
		SlotInterface headerSlot = shell.getSubSlot(AdjustmentHelper.SLOT_LISTSLOT1);
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialInit]parent:"+parent.getName(),100L);
		
		_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialInit]grandparent:\n\n"+parent.getParentForm(state).getName(),100L);
		
		
		AdjustmentDetailSerialTmpDTO adjDetailSerial = new AdjustmentDetailSerialTmpDTO();
		RuntimeFormWidgetInterface widget= null;
		
		widget = headerForm.getFormWidgetByName("ADJUSTMENTKEY");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setAdjustmentkey(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Adjustmentkey",100L);
			return RET_CANCEL;
		}
		
		widget = parent.getFormWidgetByName("ADJUSTMENTLINENUMBER");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setAdjustmentlinenumber(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate AdjustmentLineNumber",100L);
			return RET_CANCEL;
		}

		widget = parent.getFormWidgetByName("STORERKEY");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setStorerkey(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Storerkey",100L);
			return RET_CANCEL;
		}

		widget = parent.getFormWidgetByName("SKU");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setSku(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Sku",100L);
			return RET_CANCEL;
		}

		widget = parent.getFormWidgetByName("LOT");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setLot(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Lot",100L);
			return RET_CANCEL;
		}

		widget = parent.getFormWidgetByName("LOC");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setLoc(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Loc",100L);
			return RET_CANCEL;
		}

		widget = parent.getFormWidgetByName("ID");
		if(widget!=null && widget.getValue()!=null )
			adjDetailSerial.setId(widget.getValue().toString());
		else{
			_log.debug("LOG_SYSTEM_OUT","Failed to populate Id",100L);
			return RET_CANCEL;
		}

		state.getRequest().getSession().setAttribute("ADJUSTMENTDETAILSERIALTMP",adjDetailSerial);

		_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialInit]DTO:"+adjDetailSerial.toString(),100L);
		return RET_CONTINUE;
	}
   
}
