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
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class UnbatchOrders extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnbatchOrders.class);

	
	
    protected int execute(ActionContext context, ActionResult result) throws EpiException {
    	WmsWebuiActionsProperties	input = new WmsWebuiActionsProperties();
    	String waveKey = "";
    	StateInterface state = context.getState();
 
 		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
//		System.out.println(" %%%% wave headerform="+waveHeaderForm.getName());
		waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
//		System.out.println(" %%%% wavekey="+wmsWaveKey);
		input.setProcedureName("UNBATCHORDER");
		Array parameters = new Array();
		parameters.add(new TextData(waveKey));
		input.setProcedureParameters(parameters);
		try{
			EXEDataObject edo = WmsWebuiActionsImpl.doAction(input);
			Query qry = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY='"+waveKey+"'", null);
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
			uowb.clearState();
	 		result.setFocus(resultCollection.get("0"));

		}catch(WebuiException e){
				String [] exceptionMsg = new String[1];
				exceptionMsg[0]=e.getMessage();
				throw new UserException("WPEXP_UNBATCH_OREDERS_FAILED", exceptionMsg);
		}		
		
	
 
        return RET_CONTINUE;
    }
}
