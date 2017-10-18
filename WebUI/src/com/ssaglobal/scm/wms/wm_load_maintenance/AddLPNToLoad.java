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

package com.ssaglobal.scm.wms.wm_load_maintenance;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AddLPNToLoad extends com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			StateInterface state = ctx.getState();
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface loadMaintHeaderForm = FormUtil.findForm(toolbarForm, "wms_list_shell", "wm_load_maintenance_detail_form", state);
			DataBean loadHeaderFocus = loadMaintHeaderForm.getFocus();
			String loadId = BioAttributeUtil.getString(loadHeaderFocus, "LOADID");
			String rawStopValue = StringUtils.isEmpty(ctx.getModalBodyForm(0).getFormWidgetByName("STOP").getDisplayValue()) ? "1" : ctx.getModalBodyForm(0).getFormWidgetByName("STOP")
					.getDisplayValue();
			String lpn = ctx.getModalBodyForm(0).getFormWidgetByName("LPN").getDisplayValue().toUpperCase();

			// Validate Stop exists in LOADSTOP
			int stop = Integer.parseInt(rawStopValue);
			BioCollectionBean loadStops = (BioCollectionBean) loadHeaderFocus.getValue("LOADSTOPS");
			if (loadStops == null) {
				// throw exception
				throw new UserException("WMEXP_LOAD_NO_STOP", new Object[] {});
			} else {
				boolean foundStop = false;
				for (int i = 0; i < loadStops.size(); i++) {
					BioBean loadStop = loadStops.get("" + i);
					int existingStop = BioAttributeUtil.getInt(loadStop, "STOP");
					if (stop == existingStop) {
						foundStop = true;
						break;
					}

				}

				if (foundStop == false) {
					// throw exception
					throw new UserException("WMEXP_LOAD_STOP_EXISTING", new Object[] { loadId, stop });
				}
			}

			// Executes stored proceedure
			// name:NSPADDLPNTOLOAD
			// params:loadid,stop,lpn
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			// Store parameters for stored proceedure call
			params.add(new TextData(loadId));
			params.add(new TextData(stop));
			params.add(new TextData(lpn));
			// Set actionProperties for stored proceedure call
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPADDLPNTOLOAD");
			EXEDataObject response = null;
			try {
				// Run stored proceedure
				response = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
			int returnCode = response.getReturnCode();
			if (returnCode == 0) {
				ctx.setNavigation("NoMsg");
				state.closeModal(true);
			} else {
				ctx.setNavigation("ShowMsg");
				EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
				userContext.put("LOAD_WARNING" + state.getInteractionId(), "WMEXP_LOAD_OVERLOAD");
				userContext.put("LOAD_WARNING" + "ARGS" + state.getInteractionId(), new Object[] { lpn });
			}

		} catch (RuntimeException e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
