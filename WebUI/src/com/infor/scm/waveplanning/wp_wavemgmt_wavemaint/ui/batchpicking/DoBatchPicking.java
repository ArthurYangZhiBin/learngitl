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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.batchpicking;


import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class DoBatchPicking extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DoBatchPicking.class);

	
	
    protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
    	StateInterface state = context.getState();
    	String sortationStationKey = state.getRequest().getSession().getAttribute("SORTATION_STATION_KEY").toString();
    	String wmsWaveKey = state.getRequest().getSession().getAttribute("WMS_WAVE_KEY").toString();
    	WmsWebuiActionsProperties	input = new WmsWebuiActionsProperties();
		input.setProcedureName("BATCHORDER");
		Array parameters = new Array();
		parameters.add(new TextData(wmsWaveKey));
		parameters.add(new TextData(sortationStationKey));
		input.setProcedureParameters(parameters);
		try{
			EXEDataObject edo = WmsWebuiActionsImpl.doAction(input);
			state.closeModal(true);
		}catch(WebuiException e){
				String [] exceptionMsg = new String[1];
				exceptionMsg[0]=e.getMessage();
				throw new UserException("WPEXP_BATCHING_ALL_OREDERS_FAILED", exceptionMsg);
		}		
 
        return RET_CONTINUE;
    }
}
