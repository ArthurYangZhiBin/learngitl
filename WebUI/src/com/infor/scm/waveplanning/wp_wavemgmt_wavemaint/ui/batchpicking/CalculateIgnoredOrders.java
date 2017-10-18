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
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class CalculateIgnoredOrders extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CalculateIgnoredOrders.class);

	
	
    protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
    	String sortationStationKey = "";
    	String waveKey = "";
    	StateInterface state = context.getState();
        RuntimeFormInterface sourceForm = context.getSourceForm();    //gets the toolbar

//        System.out.println(" %%%% parent form="+sourceForm.getParentForm(state).getName());
		
		ArrayList tabList = new ArrayList();
		tabList.add("tab1");
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(sourceForm, "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		waveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();
//		System.out.println(" %%%%wave header form="+waveHeaderForm.getName());
		RuntimeListFormInterface sortList =(RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
		ArrayList selectedList = sortList.getSelectedItems();
		int size = selectedList.size();
		if(size == 1){
			BioBean sortItem = (BioBean)sortList.getSelectedItems().get(0);
			sortationStationKey = (String)sortItem.get("SORTATIONSTATIONKEY");
		}else if(size >1){
			throw new UserException("WPEXP_SINGLE_SORT_STATION", new Object[] {});
		}
        
		//set up procedure parameters
		WmsWebuiActionsProperties input = new WmsWebuiActionsProperties();
		input.setProcedureName("BATCHCANDIDATES");
		Array parameters = new Array();
		parameters.add(new TextData(waveKey));
		parameters.add(new TextData(sortationStationKey));
		input.setProcedureParameters(parameters);
		//start calculate ignored orders
		state.getRequest().getSession().setAttribute("SORTATION_STATION_KEY", sortationStationKey);
		state.getRequest().getSession().setAttribute("WMS_WAVE_KEY", waveKey);
		EXEDataObject edo = null;
		try{
			edo = WmsWebuiActionsImpl.doAction(input);
		}catch(WebuiException e){
			String [] exceptionMsg = new String[1];
			exceptionMsg[0]=e.getMessage();
			throw new UserException("WPEXP_CALCULATING_IGNORED_OREDERS_FAILED", exceptionMsg);
		}
		DataValue ignoredOrderDataValue = edo.getAttribValue(new TextData("WillBeIgnored"));
//		System.out.println("***datavalue="+ignoredOrderDataValue);
		int numberOfOrdersIgnored = new Integer(ignoredOrderDataValue.getAsString()).intValue();
		if(numberOfOrdersIgnored == 0){
			input = new WmsWebuiActionsProperties();
			input.setProcedureName("BATCHORDER");
			parameters = new Array();
			parameters.add(new TextData(waveKey));
			parameters.add(new TextData(sortationStationKey));
			input.setProcedureParameters(parameters);
			try{
				edo = WmsWebuiActionsImpl.doAction(input);
				
				Query qry = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY='"+waveKey+"'", null);
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
				uowb.clearState();
		 		result.setFocus(resultCollection.get("0"));

				//set navigation to display orders
				context.setNavigation("clickEvent1757");
				state.closeModal(false);
			}catch(WebuiException e){
				String [] exceptionMsg = new String[1];
				exceptionMsg[0]=e.getMessage();
				throw new UserException("WPEXP_BATCHING_ALL_OREDERS_FAILED", exceptionMsg);
			}
		}else{
//System.out.println("***ignoredOrderDDDAAANumber="+numberOfOrdersIgnored);
			state.getRequest().getSession().setAttribute("IGNORED_ORDER_NUMBER", new Integer(numberOfOrdersIgnored));
			context.setNavigation("clickEvent1759");
//			return RET_CANCEL_EXTENSIONS;
		}
		
		
 
        return RET_CONTINUE;
    }

}
