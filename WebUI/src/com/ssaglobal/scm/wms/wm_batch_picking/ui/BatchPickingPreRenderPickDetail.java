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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;


import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



public class BatchPickingPreRenderPickDetail extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BatchPickingPreRenderPickDetail.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();		
		//if(session.getAttribute("SESS_KEY_BP_PICK_DETAIL") == null){
			session.setAttribute("SESS_KEY_BP_PICK_DETAIL","true");						
			RuntimeFormInterface waveForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_batch_picking_detail_view",state);		
			if(waveForm != null && waveForm.getFocus() != null && waveForm.getFocus() instanceof BioBean){
				BioBean waveFocus = (BioBean)waveForm.getFocus();
				BioCollection waveDetails = (BioCollection)waveFocus.getBioCollection("WAVEDETAIL");			
				if(waveDetails != null){
					
					String qry = "";
					if(waveDetails.size() > 0){
						qry += "wm_pickdetail.ORDERKEY = '"+waveDetails.elementAt(0).get("ORDERKEY").toString().toUpperCase()+"'";
						
						for(int i = 1; i < waveDetails.size(); i++){
							qry += " OR wm_pickdetail.ORDERKEY = '"+waveDetails.elementAt(i).get("ORDERKEY").toString().toUpperCase()+"'";
						}								
						Query loadBiosQry = new Query("wm_pickdetail", qry, "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	
						BioCollectionBean bc = uow.getBioCollectionBean(loadBiosQry);		
						_log.debug("LOG_SYSTEM_OUT","bc size:"+bc.size(),100L);
						form.setFocus(bc);
					}				
				}
			}		
		//}
				
		return RET_CONTINUE;
	}
	
}