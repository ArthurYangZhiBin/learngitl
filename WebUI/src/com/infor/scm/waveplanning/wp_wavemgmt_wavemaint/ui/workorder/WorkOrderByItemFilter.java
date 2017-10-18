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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;

public class WorkOrderByItemFilter extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WorkOrderByItemFilter.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_Wave_Work_order_by_Item_execute", "Entering WorkOrderByItemFilter",
				SuggestedCategory.NONE); 
		StateInterface state = context.getState();

		ArrayList tabList = new ArrayList();
		tabList.add("tab1");		
		RuntimeNormalFormInterface  waveHeaderForm  = (RuntimeNormalFormInterface)WPFormUtil.findForm
							(state.getCurrentRuntimeForm(), "wp_wavemgmt_wavemaint_tab_shell", "wp_wavemgmt_wavemaint_wave_header_detail_view_1",tabList, state);
		String wmsWaveKey = (String)waveHeaderForm.getFormWidgetByName("WAVEKEY").getValue();

		System.out.println("&&&&& wave key="+wmsWaveKey);	       		
		Query qry = new Query("wp_vWorkOrderByItem", "wp_vWorkOrderByItem.WAVEKEY='"+wmsWaveKey+"'", null);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
		uowb.clearState(); 		
 		result.setFocus(resultCollection);
		return RET_CONTINUE;
	}
}
