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


package com.infor.scm.waveplanning.wp_carrier_labels.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.StringUtils;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing.LabelPrintingConstants;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CarrierLabelsPrint extends com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String CARRIER_LABELS_WAVEKEY = "CARRIER_LABELS_WAVEKEY";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CarrierLabelsPrint.class);

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
		StateInterface state = context.getState();
		String source = getParameterString("SOURCE", "PRINT");
		_log.debug("LOG_DEBUG_EXTENSION_CarrierLabelsPrint_execute", "Source " + source, SuggestedCategory.NONE);

		//Ensure SPS_INSTALLED
		NSQLConfigUtil spsInstalled = new NSQLConfigUtil(state, "SPS_INSTALLED");
		//04/19/10 SRG Defect# 213323 -- Start
		//Check this flag only for carrier complaint labels
		/*if (spsInstalled.isOff() == true) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "SPS_INSTALLED is not on", SuggestedCategory.NONE);
			throw new UserException("WMEXP_SPS_NOT_INSTALLED", new Object[] {});

		}*/
		//04/19/10 SRG Defect# 213323 -- End

		if ("PRINT".equalsIgnoreCase(source)) {
			//03/24/10 SRG Defect# 213323 -- Start
			String printType = SessionUtil.getInteractionSessionAttribute("PRINTINGTYPE",state).toString();
			if (printType.equalsIgnoreCase("wp_wavemgmt_printing_carrier_label_menuitem")){
			//03/24/10 SRG Defect# 213323 -- End	
				RuntimeFormInterface currForm = context.getSourceWidget().getForm();
	
				String waveKeyString = SessionUtil.getInteractionSessionAttribute(CARRIER_LABELS_WAVEKEY, state).toString();
				
				RuntimeFormWidgetInterface printers = currForm.getFormWidgetByName("PRINTERS");
				RuntimeFormWidgetInterface wavekeyWidget = currForm.getFormWidgetByName("WAVEKEY");
				RuntimeFormWidgetInterface numOfCopiesWidget = currForm.getFormWidgetByName("NUMOFCOPIES");
				
				//
				wavekeyWidget.setDisplayValue(waveKeyString);
	
				requiredField(printers, context);
				String printer = (String) printers.getValue();
				requiredField(wavekeyWidget, context);
				String wavekey = wavekeyWidget.getDisplayValue();
				requiredField(numOfCopiesWidget, context);
				String numOfCopies = numOfCopiesWidget.getDisplayValue();
	
				UnitOfWorkBean tuow = state.getTempUnitOfWork();

				_log.debug("LOG_DEBUG_EXTENSION_CarrierLabelsPrint_execute", "WMS Wave  " + wavekey, SuggestedCategory.NONE);
	
	
	
	
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(wavekey));
				params.add(new TextData(printer));
				params.add(new TextData(numOfCopies));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("WaveSPSLabel");
				for (int i = 0; i < params.size(); i++) {
					_log.debug("LOG_DEBUG_EXTENSION_CarrierLabelsPrint_execute", "Parameters " + i + " " + params.get(i), SuggestedCategory.NONE);
				}
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				} catch (WebuiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new UserException(e.getMessage(), new Object[] {});
				}
	
				if (context instanceof ModalActionContext) {
					state.closeModal(false);
				}
			//03/24/10 SRG Defect# 213323 -- Start	
			}
			else if (printType.equalsIgnoreCase("wp_wavemgmt_printing_case_label_menuitem")){
				RuntimeFormInterface currForm = context.getSourceWidget().getForm();				
				RuntimeFormWidgetInterface printers = currForm.getFormWidgetByName("PRINTERS");
				RuntimeFormWidgetInterface wavekeyWidget = currForm.getFormWidgetByName("WAVEKEY");
				RuntimeFormWidgetInterface numOfCopiesWidget = currForm.getFormWidgetByName("NUMOFCOPIES");
				
				//requiredField(printers, context);
				String printer = (String) printers.getValue();
				requiredField(wavekeyWidget, context);
				String wave = wavekeyWidget.getDisplayValue();
				requiredField(numOfCopiesWidget, context);
				String numOfCopies = numOfCopiesWidget.getDisplayValue();				
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				String wavekey = wave;
//				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY = '" + wave + "'", null));
//				for (int i = 0; i < rs.size(); i++) {
//					DataBean bio = rs.get("" + i);
//					wavekey = BioAttributeUtil.getString(bio, "WAVEKEY");
//				}
				_log.debug("LOG_DEBUG_EXTENSION_CaseLabelsPrint_execute", "WMS Wave  " + wavekey, SuggestedCategory.NONE);
							
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(LabelPrintingConstants.CASE_LABEL_PRINTING)); //keytype
				params.add(new TextData(wavekey));
				params.add(new TextData(printer));
				params.add(new TextData(printer));
				params.add(new TextData(numOfCopies));							
				params.add(new TextData(""));
				
				try
				{
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("NSPPrintWaveLabel");
					EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);				
				} catch (WebuiException e)
				{
					e.printStackTrace();
					throw new UserException(e.getMessage(), new Object[] {});
				}			
				for (int i = 0; i < params.size(); i++) {
					_log.debug("LOG_DEBUG_EXTENSION_CaseLabelsPrint_execute", "Parameters " + i + " " + params.get(i), SuggestedCategory.NONE);
				}
				if (context instanceof ModalActionContext) {
					state.closeModal(false);
				}		
			}
			else if (printType.equalsIgnoreCase("wp_wavemgmt_printing_assignment_label_menuitem")){

				RuntimeFormInterface currForm = context.getSourceWidget().getForm();
				ArrayList<String> assignmentNumber = new ArrayList<String>();	
				RuntimeFormWidgetInterface printers = currForm.getFormWidgetByName("PRINTERS");
				RuntimeFormWidgetInterface wavekeyWidget = currForm.getFormWidgetByName("WAVEKEY");
				RuntimeFormWidgetInterface numOfCopiesWidget = currForm.getFormWidgetByName("NUMOFCOPIES");
				
				//requiredField(printers, context);
				String printer = (String) printers.getValue();
				requiredField(wavekeyWidget, context);
				String wave = wavekeyWidget.getDisplayValue();
				requiredField(numOfCopiesWidget, context);
				String numOfCopies = numOfCopiesWidget.getDisplayValue();				
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				String waveKey = wave;
//				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY = '" + wave + "'", null));
//				for (int i = 0; i < rs.size(); i++) {
//					DataBean bio = rs.get("" + i);
//					wmswavekey = BioAttributeUtil.getString(bio, "WMSWAVEID");
//				}
				_log.debug("LOG_DEBUG_EXTENSION_AssignmentLabelsPrint_execute", "WMS Wave  " + waveKey, SuggestedCategory.NONE);
				
				Query qry = new Query("wm_useractivity", "wm_useractivity.WAVEKEY='"+waveKey+"'", null);
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
				int size = resultCollection.size();
				if(resultCollection != null){
					for(int i=0; i<size; i++){
						assignmentNumber.add(resultCollection.elementAt(i).getString("ASSIGNMENTNUMBER"));
					}
				}
				
				if(assignmentNumber.size() == 0){
					String [] msg = new String[1];
					msg [0] = waveKey;
					throw new UserException("WPEXP_WAVE_LABEL_PRINTING_NO_ASSIGNMENT", msg);
				}				
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(LabelPrintingConstants.ASSIGNMENT_LABEL_PRINTING)); //keytype
				params.add(new TextData(waveKey));
				params.add(new TextData(printer));
				params.add(new TextData(printer));
				params.add(new TextData(numOfCopies));
				actionProperties.setProcedureParameters(params);				
				
				for(int i=0; i<size;i++){
					params.add(new TextData(assignmentNumber.get(i)).toString()); //assignment
					
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("NSPPrintWaveLabel");
					try
					{
						EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);
						params.remove(params.size()-1);
					} catch (WebuiException e)
					{
						e.printStackTrace();
						throw new UserException(e.getMessage(), new Object[] {});
					}
				}
				for (int i = 0; i < params.size(); i++) {
					_log.debug("LOG_DEBUG_EXTENSION_AssignmentLabelsPrint_execute", "Parameters " + i + " " + params.get(i), SuggestedCategory.NONE);
				}
				if (context instanceof ModalActionContext) {
					state.closeModal(false);
				}			
			}
			else if (printType.equalsIgnoreCase("wp_wavemgmt_printing_batch_pick_label_menuitem")){

				RuntimeFormInterface currForm = context.getSourceWidget().getForm();				
				RuntimeFormWidgetInterface printers = currForm.getFormWidgetByName("PRINTERS");
				RuntimeFormWidgetInterface wavekeyWidget = currForm.getFormWidgetByName("WAVEKEY");
				RuntimeFormWidgetInterface numOfCopiesWidget = currForm.getFormWidgetByName("NUMOFCOPIES");
				
				requiredField(printers, context);
				String printer = (String) printers.getValue();
				requiredField(wavekeyWidget, context);
				String wave = wavekeyWidget.getDisplayValue();
				requiredField(numOfCopiesWidget, context);
				String numOfCopies = numOfCopiesWidget.getDisplayValue();				
				UnitOfWorkBean tuow = state.getTempUnitOfWork();
				String waveKey = wave;
//				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wp_wave", "wp_wave.WAVEKEY = '" + wave + "'", null));
//				for (int i = 0; i < rs.size(); i++) {
//					DataBean bio = rs.get("" + i);
//					wmswavekey = BioAttributeUtil.getString(bio, "WMSWAVEID");
//				}
				_log.debug("LOG_DEBUG_EXTENSION_CaseLabelsPrint_execute", "WMS Wave  " + waveKey, SuggestedCategory.NONE);
							
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(LabelPrintingConstants.BATCH_PICK_LABEL_PRINTING)); //keytype
				params.add(new TextData(waveKey));
				params.add(new TextData(printer));
				params.add(new TextData(printer));
				params.add(new TextData(numOfCopies));							
				params.add(new TextData(""));
				
				try
				{
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("NSPPrintWaveLabel");
					EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);				
				} catch (WebuiException e)
				{
					e.printStackTrace();
					throw new UserException(e.getMessage(), new Object[] {});
				}			
				for (int i = 0; i < params.size(); i++) {
					_log.debug("LOG_DEBUG_EXTENSION_CaseLabelsPrint_execute", "Parameters " + i + " " + params.get(i), SuggestedCategory.NONE);
				}
				if (context instanceof ModalActionContext) {
					state.closeModal(false);
				}
			}
			//03/24/10 SRG Defect# 213323 -- End
			
		} else if ("MENU".equalsIgnoreCase(source)) {

			ArrayList tabList = new ArrayList();
			tabList.add("tab1");
			RuntimeNormalFormInterface waveHeaderForm = (RuntimeNormalFormInterface) WPFormUtil.findForm
					(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1", tabList, state);
			//			String wmsWaveKey = (String) waveHeaderForm.getFormWidgetByName("WMSWAVEID").getValue();
			String waveKey = BioAttributeUtil.getString(waveHeaderForm.getFocus(), "WAVEKEY");
			_log.debug("LOG_DEBUG_EXTENSION_CarrierLabelsPrint_execute", "Setting " + waveKey, SuggestedCategory.NONE);


			//04/19/10 SRG Defect# 213323 -- Start
			SessionUtil.setInteractionSessionAttribute("PRINTINGTYPE", context.getActionObject().getName(), state);
			SessionUtil.setInteractionSessionAttribute(CARRIER_LABELS_WAVEKEY, waveKey, state);
			if (spsInstalled.isOff() == true) {
				//Throw error only for carrier complaint labels
				if (context.getActionObject().getName().equalsIgnoreCase("wp_wavemgmt_printing_carrier_label_menuitem")){
					_log.error("LOG_ERROR_EXTENSION_CheckPackProceed_execute", "SPS_INSTALLED is not on", SuggestedCategory.NONE);
					throw new UserException("WMEXP_SPS_NOT_INSTALLED", new Object[] {});
				}
			}
			//04/19/10 SRG Defect# 213323 -- End

		} else {
			_log.error("LOG_ERROR_EXTENSION_CarrierLabelsPrint_execute", "Extension is not configured properly, PRINT or MENU needs to be entered as source", SuggestedCategory.NONE);
			return RET_CANCEL;
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private void requiredField(RuntimeFormWidgetInterface widget, ActionContext context) throws UserException {
		String value = widget.getDisplayValue();
		if (StringUtils.isEmpty(value)) {
			throw new UserException("WPEXP_REQ_FIELD", new Object[] { StringUtils.removeTrailingColon(WPFormUtil.getWidgetLabel(context, widget)) });
		}

	}


}
