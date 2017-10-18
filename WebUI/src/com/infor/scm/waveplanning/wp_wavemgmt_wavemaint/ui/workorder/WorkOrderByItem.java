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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.workorder;

import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
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

public class WorkOrderByItem extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WorkOrderByItem.class);

	
	
    protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
    	StateInterface state = context.getState();
    	String waveKey = "";
        RuntimeFormInterface sourceForm = context.getSourceForm();    //gets the toolbar
		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(sourceForm, "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
		
//    	System.out.println(" ****$$$forname="+state.getCurrentRuntimeForm().getParentForm(state).getName()+"    wavekey="+wmsWaveKey);   	
    	RuntimeListFormInterface orderList =(RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
    	ArrayList selectedOrderList = orderList.getSelectedItems();
    	WmsWebuiActionsProperties	input = null;
    	Array parameters = null;
    	EXEDataObject edo = null;
    	if(selectedOrderList != null){
    		int size = selectedOrderList.size();
    		if(size != 0){
	    		BioBean selectedItem= null;
	    		try{
		    		for(int i=0; i<size;i++){
		    			input = new WmsWebuiActionsProperties();
		    			parameters = new Array();
		    			selectedItem = (BioBean)selectedOrderList.get(i);
		    			parameters.add(new TextData(selectedItem.get("WORKORDERDEFNKEY").toString()));
		    			parameters.add(new TextData(waveKey));
		    			parameters.add(new TextData(new java.util.Date().toString()));
		    			parameters.add(new TextData(new java.util.Date().toString()));
		    			parameters.add(new TextData(selectedItem.get("DEMAND").toString()));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(selectedItem.get("SKU").toString()));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(" "));
		    			parameters.add(new TextData(waveKey));
		    			input.setProcedureName("NSP_MANUALWOFACTORY_WAVE");
		    			input.setProcedureParameters(parameters);
		    			edo = WmsWebuiActionsImpl.doAction(input);
		    		}
		    		context.setNavigation("clickEvent174");
	    		}catch(WebuiException e){
					String [] exceptionMsg = new String[1];
					exceptionMsg[0]=e.getMessage();
					throw new UserException("WPEXP_WORK_OREDER_ELIGIBLE_BY_ITEM_FAILED", exceptionMsg);
	    		}
    		}else{
    			context.setNavigation("clickEvent1764");
    			return RET_CANCEL_EXTENSIONS;
    		}
    	}else{
			context.setNavigation("clickEvent1764");
			return RET_CANCEL_EXTENSIONS;
    	}  	
       return RET_CONTINUE;
    }

}
