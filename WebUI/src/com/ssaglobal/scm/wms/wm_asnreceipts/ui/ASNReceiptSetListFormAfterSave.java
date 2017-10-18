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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;


// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ASNReceiptSetListFormAfterSave extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNReceiptSetListFormAfterSave.class);
	private static String BLANK = "blank";
	private static String SAVE = "SAVE";
	private static String TOGGLE_SLOT = "wm_receiptdetail_toggle";
	private static String TOGGLE_TAB_ID = "wm_receiptdetail_detail_view";
	private static String TAB_GROUP_SLOT = "tbgrp_slot";
	private static String TG_TAB_ID = "tab 6";
	private static String LPN_SLOT = "wm_LPNDETAIL";
	private static String LPN_TAB_ID = "wm_lpndetail_detail_view";
	private static String TG_CW_TAB_ID = "tab 7";
	private static String CW_SLOT = "wm_asndetail_catchweight_toggle_slot";
	private static String CW_TAB_ID = "catchweight_detail_view";
	
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException {
		StateInterface state = context.getState();
		try	{
			String actionName = context.getActionObject().getName();
			_log.debug("LOG_DEBUG_EXTENSION_SetListFormAfterSave", "Action Name " + actionName, SuggestedCategory.NONE);
			
			//Perform only after the SAVE action
			if (actionName.equals(SAVE)){
				//perform the swap
				SlotInterface toggleSlot = form.getSubSlot(TOGGLE_SLOT);
				RuntimeFormInterface tabGroupForm = state.getRuntimeForm(toggleSlot, TOGGLE_TAB_ID);
				_log.debug("LOG_SYSTEM_OUT","Tab Group Form = "+tabGroupForm.getName(),100L);
			
				//Ignore steps when receipt details remains in list view
				if(!tabGroupForm.getName().equalsIgnoreCase(BLANK)){
					SlotInterface tabGroupSlot = tabGroupForm.getSubSlot(TAB_GROUP_SLOT);
					_log.debug("LOG_SYSTEM_OUT","Tab Group Slot = "+tabGroupSlot.getName(),100L);
					int selectedFormNumber = state.getSelectedFormNumber(tabGroupSlot);
				
					//Determine if LPN Details are in focus (FORM NUMBER 6)
					if (!(selectedFormNumber == 6 || selectedFormNumber == 7))
					{
						//Force detail list form refresh 
						_log.debug("LOG_DEBUG_EXTENSION_SetListFormAfterSave", "Setting " + TOGGLE_SLOT + " tab: " + TOGGLE_TAB_ID
								+ " to blank", SuggestedCategory.NONE);
						form.setSpecialFormType(state, toggleSlot, TOGGLE_TAB_ID, BLANK);
					}
					else{
					
						RuntimeFormInterface lpnForm = state.getRuntimeForm(state.getRuntimeForm(tabGroupSlot, TG_TAB_ID).getSubSlot(LPN_SLOT), LPN_TAB_ID);
						RuntimeFormInterface cwcdForm = state.getRuntimeForm(state.getRuntimeForm(tabGroupSlot, TG_CW_TAB_ID).getSubSlot(CW_SLOT), CW_TAB_ID);
						
						//Determine if individual LPN details/CWCD are being edited or created
						//If they are, do nothing. Otherwise force list form refresh
						if(lpnForm != null && lpnForm.getSpecialType().equalsIgnoreCase(BLANK)){
							_log.debug("LOG_DEBUG_EXTENSION_SetListFormAfterSave", "Setting "+TOGGLE_SLOT+" tab: "+TOGGLE_TAB_ID+" to blank", SuggestedCategory.NONE);
							form.setSpecialFormType(state, toggleSlot, TOGGLE_TAB_ID, BLANK);
						}
						else{
							_log.debug("LOG_DEBUG_EXTENSION_SetListFormAfterSave", TAB_GROUP_SLOT+" focus is LPN Details, doing nothing", SuggestedCategory.NONE);
						}
					}
				}
			}
		} catch (Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}
