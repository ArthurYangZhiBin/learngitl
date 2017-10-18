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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

//Import 3rd party packages and classes
import java.util.Iterator;
import java.util.List;

//Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class PreRenderStatusAction extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderStatusAction.class);
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PreRenderStatusAction",100L);
		StateInterface state = context.getState();
		if(!form.getFocus().isTempBio()){
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Existing Record",100L);
			RuntimeFormWidgetInterface statusWidget = form.getFormWidgetByName("STATUS");
			Iterator widgets = form.getFormWidgets();
			while(widgets.hasNext()){
				Object obj = widgets.next();
				RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)obj;
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				//SRG: Transaction Date Override enhancement: Make effective date read only if status = 9 (finalized)
				if(!(form.getName().equals("wm_internal_transfer_detail_detail_view"))) {
					if(!(statusWidget.getValue().equals("9"))) {
						form.getFormWidgetByName("EFFECTIVEDATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					}
				}
				//SRG: End
			}
			//For detail form and detail form slots
			if(statusWidget.getValue().equals("9")){
				if(form.getName().equals("wm_internal_transfer_detail_detail_view")){

					List subslots = form.getSubSlots();
					Iterator sub = subslots.iterator();
					while(sub.hasNext()){
						Object obj = sub.next();
						SlotInterface slot = (SlotInterface) obj;
						RuntimeFormInterface subForm = state.getRuntimeForm(slot, null);

						Iterator subWidgets = subForm.getFormWidgets();
						while(subWidgets.hasNext()){
							Object subObj = subWidgets.next();
							RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)subObj;
							widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						}
					}
				}
			}
		}else{
			//Apply header key to detail
			//SERIAL NUMBER NAVIGATION REQUIREMENT
			RuntimeFormInterface shell = form.getParentForm(state);
			while(!shell.getName().equals("wms_list_shell")){
				shell = shell.getParentForm(state);
			}
			RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot("list_slot_1"), null);
			QBEBioBean qbe = (QBEBioBean)form.getFocus();
			qbe.set(TRANSFER_KEY, header.getFormWidgetByName(TRANSFER_KEY).getDisplayValue());
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PreRenderStatusAction",100L);
		return RET_CONTINUE;
	}
}