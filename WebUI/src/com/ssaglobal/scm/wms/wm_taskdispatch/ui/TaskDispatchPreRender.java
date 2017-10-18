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

package com.ssaglobal.scm.wms.wm_taskdispatch.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TaskDispatchPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDispatchPreRender.class);

	/**
	 * Called in response to the pre-render event on a form in a modal window. Write code to customize the properties of
	 * a form. This code is re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		StateInterface state = context.getState();
		try {
			// Add your code here to process the event
			DataBean focus = form.getFocus();
			Integer stepNumber = BioAttributeUtil.getInteger(focus, "STEPNUMBER");
			if (stepNumber == null) {
				focus.setValue("PROXIMITYINTERLEAVING", new Integer(0));
				setDisabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
				setAsNormal(form.getFormWidgetByName("STEPNUMBER"));
				return RET_CONTINUE;
			}
			if (focus.isTempBio()) {
				// new
				// get ttmstrategykey
				String ttmkey = getTTMKey(state);
				// there is a possibility that the header could have a blank ttmstrategykey
				// usually on a brand new header
				if (StringUtils.isEmpty(ttmkey)) {
					focus.setValue("PROXIMITYINTERLEAVING", new Integer(0));
					setDisabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
				} else {
					UnitOfWorkBean uow = state.getTempUnitOfWork();
					// get records based on ttmstrategykey & stepnumber
					BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_taskdispatchdetail", "wm_taskdispatchdetail.TTMSTRATEGYKEY = '" + ttmkey + "' and wm_taskdispatchdetail.STEPNUMBER = '" + stepNumber + "'", "wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER"));
					int size = rs.size();
					_log.info(	"LOG_INFO_EXTENSION_TaskDispatchPreRender_preRenderForm",
								"Size " + size + " based on " + ttmkey + " and " + stepNumber,
								SuggestedCategory.NONE);
					size += 1; // this is a new record;
					if (size >= 2) {
						// focus.setValue("PROXIMITYINTERLEAVING", new Integer(1));
						setEnabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
					} else {
						// set PROXIMITYINTERLEAVING to 0 and disable
						focus.setValue("PROXIMITYINTERLEAVING", new Integer(0));
						setDisabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
					}
				}

			} else {
				// existing record
				// get ttmstrategykey
				String ttmkey = BioAttributeUtil.getString(focus, "TTMSTRATEGYKEY");
				UnitOfWorkBean uow = state.getTempUnitOfWork();
				// get records based on ttmstrategykey & stepnumber, ignoring the current one (because the database
				// stepnumber value may not be the same)
				String linenum = BioAttributeUtil.getString(focus, "TTMSTRATEGYLINENUMBER");
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_taskdispatchdetail", "wm_taskdispatchdetail.TTMSTRATEGYKEY = '" + ttmkey + "' and wm_taskdispatchdetail.STEPNUMBER = '" + stepNumber + "' and wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER != '" + linenum + "'", "wm_taskdispatchdetail.TTMSTRATEGYLINENUMBER"));
				int size = rs.size();
				_log.info(	"LOG_INFO_EXTENSION_TaskDispatchPreRender_preRenderForm",
							"Size " + size + " based on " + ttmkey + " and " + stepNumber,
							SuggestedCategory.NONE);
				size += 1; // account for the current record
				if (size >= 2) {
					// focus.setValue("PROXIMITYINTERLEAVING", new Integer(1));
					setEnabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
				} else {
					// set PROXIMITYINTERLEAVING to 0 and disable
					int proximityInterleaving = BioAttributeUtil.getInt(focus, "PROXIMITYINTERLEAVING");
					if (proximityInterleaving != 0) {
						focus.setValue("PROXIMITYINTERLEAVING", new Integer(0));
					}
					setDisabled(form.getFormWidgetByName("PROXIMITYINTERLEAVING"));
				}
			}

			int proximityInterleaving = BioAttributeUtil.getInt(focus, "PROXIMITYINTERLEAVING");
			if (proximityInterleaving == 1) {
				// make ProximityInterleaving Required
				setAsRequired(form.getFormWidgetByName("STEPNUMBER"));
			} else {
				setAsNormal(form.getFormWidgetByName("STEPNUMBER"));
				// setEmpty(form.getFormWidgetByName("PROXIMITYGROUP"));
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private String getTTMKey(StateInterface state) {
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
															"wms_list_shell",
															"wm_taskdispatch_detail_view",
															state);
		DataBean headerFocus = headerForm.getFocus();
		String ttmkey = BioAttributeUtil.getString(headerFocus, "TTMSTRATEGYKEY");
		return ttmkey;
	}

	// Method to set widget properties as required
	public void setAsRequired(RuntimeFormWidgetInterface widget) {
		widget.setProperty("input type", "required");
		setEnabled(widget);
		// widget.setProperty("label class", "epnyRequired");
		// widget.setProperty("label clickable class", "epnyLabelClickableRequired");
		// widget.setProperty("label clickable mouseover class", "epnyLabelClickableRequiredMouseover");
	}

	private void setEnabled(RuntimeFormWidgetInterface widget) {
		widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
	}

	// Method to set widget properties as required
	public void setAsNormal(RuntimeFormWidgetInterface widget) {
		widget.setProperty("input type", "normal");
		// setDisabled(widget);

		// widget.setProperty("label class", "epnyLabel");
		// widget.setProperty("label clickable class", "epnyLabelClickable");
		// widget.setProperty("label clickable mouseover class", "epnyLabelClickableMouseover");
	}

	private void setDisabled(RuntimeFormWidgetInterface widget) {
		widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
	}

	private void setEmpty(RuntimeFormWidgetInterface widget) {
		if (!StringUtils.isEmpty(widget.getDisplayValue())) {
			widget.setDisplayValue("");
		}
	}
}
