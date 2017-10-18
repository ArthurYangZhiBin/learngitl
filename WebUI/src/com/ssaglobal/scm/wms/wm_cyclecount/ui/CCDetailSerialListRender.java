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


package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentHelper;


public class CCDetailSerialListRender extends com.epiphany.shr.ui.action.ActionExtensionBase {


	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDetailSerialListRender.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialListRender]:START",100L);
		
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormExtendedInterface form = (RuntimeFormExtendedInterface)state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		SlotInterface slotSerialInventory = (SlotInterface)  form.getSubSlot(CCHelper.SLOT_SERIALINVENTORYLIST);
		
		String ccdetailkey = (String)form.getFormWidgetByName("CCDETAILKEY").getValue();
		
		RuntimeFormInterface parent = FormUtil.findForm((RuntimeFormInterface)form, CCHelper.WMSLISTSHELL, CCHelper.WMSLISTSHELL, state);
		SlotInterface headerSlot = parent.getSubSlot(CCHelper.SLOT_LISTSLOT1);
		//RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,"wm_adjustment_detail_view");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,null);
		String cckey = headerForm.getFormWidgetByName("CCKEY").getValue().toString();
		
		CCHelper helper = new CCHelper();
		
		helper.refreshCCDetailSerialList(uowb, form, state, slotSerialInventory, cckey, ccdetailkey);
		
		helper.refreshQtySelected((UIRenderContext)context);
		result.setFocus(helper.getCCDetailSerialList(uowb, form, state, slotSerialInventory, cckey, ccdetailkey));
		_log.debug("LOG_SYSTEM_OUT","[CCDetailSerialListRender]:END",100L);
		return RET_CONTINUE;   
	}
   
	
   
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
