/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * TODO Document ShipmentOrderListSave class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ShipmentOrderListSave extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderListSave.class);
	private final String SHELL_LIST_SLOT_1 ="list_slot_1";
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot(SHELL_LIST_SLOT_1); 
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if (headerForm.isListForm()) {
				try {
					uow.saveUOW(true);
				} catch (UnitOfWorkException e) {
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
					_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
					Throwable nested = (e).findDeepestNestedException();
					if (nested != null) {
						_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
									"Nested " + nested.getClass().getName(),
									SuggestedCategory.NONE);
						_log.error(	"LOG_ERROR_EXTENSION_HeaderDetailSave",
									"Message " + nested.getMessage(),
									SuggestedCategory.NONE);
					}
					if (nested instanceof ServiceObjectException) {
						String reasonCode = nested.getMessage();
						throwUserException(e, reasonCode, null);
					} else {
						throwUserException(e, "ERROR_SAVING_BIO", null);
					}
	
				} catch (EpiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
				uow.clearState();
				result.setFocus(headerForm.getFocus());
				context.setNavigation("listClickEvent1748");
				return RET_CANCEL_EXTENSIONS;
			}else{
				return RET_CONTINUE;
			}
			
	}


}
