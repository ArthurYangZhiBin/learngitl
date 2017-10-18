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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.printing;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;

public class LabelPrintingUIExtension extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LabelPrintingUIExtension.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_Wave_Label_Printing_execute", "Entering LabelPrintingUIExtension",
				SuggestedCategory.NONE); 
		String printingType = (String)getParameter("PrintingType");
		
		StateInterface state = context.getState();

		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		String waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
		PrintLabelInputObj input = this.getInputValue(printingType, waveKey, state);
		PrintLabelInterface printLabel = LabelPrintingFactory.getPrintingFacility(printingType); 
		printLabel.doPrintin(input);
		return RET_CONTINUE;
	}

	public PrintLabelInputObj getInputValue(String printingType, String waveKey, StateInterface state) throws EpiException{
		PrintLabelInputObj input = new PrintLabelInputObj();
		ArrayList<String> assignmentNumber = new ArrayList<String>();		
		input.setWavekey(waveKey);
		if(printingType.equalsIgnoreCase(LabelPrintingConstants.CASE_LABEL_PRINTING)){
			input.setPrintingType(LabelPrintingConstants.CASE_LABEL_PRINTING);			
		}else if(printingType.equalsIgnoreCase(LabelPrintingConstants.BATCH_PICK_LABEL_PRINTING)){
			input.setPrintingType(LabelPrintingConstants.BATCH_PICK_LABEL_PRINTING);			
		}else if(printingType.equalsIgnoreCase(LabelPrintingConstants.ASSIGNMENT_LABEL_PRINTING)){
			Query qry = new Query("wm_useractivity", "wm_useractivity.WAVEKEY='"+waveKey+"'", null);
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
			if(resultCollection != null){
				int size = resultCollection.size();
				for(int i=0; i<size; i++){
					assignmentNumber.add(resultCollection.elementAt(i).getString("ASSIGNMENTNUMBER"));
				}
			}
			input.setPrintingType(LabelPrintingConstants.ASSIGNMENT_LABEL_PRINTING);
			uowb.clearState();
		}else{
			throw new UserException("WPEXP_WAVE_LABEL_PRINTING_WRONG_TYPE", new Object[] {});
		}
		input.setAssignmentNumber(assignmentNumber);
		//set up standard and rfid printer names here
		input.setStandardPrinterName(" ");
		input.setRfidPrinterName(" ");
		return input;
	}
}
