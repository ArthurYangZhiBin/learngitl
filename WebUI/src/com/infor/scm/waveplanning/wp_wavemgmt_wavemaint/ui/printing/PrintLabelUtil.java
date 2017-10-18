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

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

import java.util.ArrayList;
public class PrintLabelUtil {
	public static void printLabel(PrintLabelInputObj input)throws UserException, EpiDataException{
		ArrayList<String> assignmentNumber = input.getAssignmentNumber();
		int size = assignmentNumber.size();
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData(input.getPrintingType())); //keytype
		params.add(new TextData(input.getWavekey())); //key
		params.add(new TextData(input.getStandardPrinterName())); //std printer name
		params.add(new TextData(input.getRfidPrinterName())); //rfid printer name
		params.add(new TextData(input.getCount())); //copies

		String printingType = input.getPrintingType();
		if(LabelPrintingConstants.CASE_LABEL_PRINTING.equalsIgnoreCase(printingType)
				|| LabelPrintingConstants.BATCH_PICK_LABEL_PRINTING.equalsIgnoreCase(printingType)){
			params.add(new TextData(""));
			try
			{
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPPrintWaveLabel");
				EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e)
			{
				throw new UserException(e.getMessage(), new Object[] {});
			}
		}
		if(LabelPrintingConstants.ASSIGNMENT_LABEL_PRINTING.equalsIgnoreCase(printingType)){
			if(input.getAssignmentNumber().size() == 0){
				String [] msg = new String[1];
				msg [0] = input.getWavekey();
				throw new UserException("WPEXP_WAVE_LABEL_PRINTING_NO_ASSIGNMENT", msg);
			}
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
					throw new UserException(e.getMessage(), new Object[] {});
				}
			}
		}
		
	}
}
