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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

public class LMDefaultHeaderDetailSave extends SaveAction{
	private static final String LOADID = "LOADID";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LMDefaultHeaderDetailSave.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Executing LMDefaultHeaderDetailSave",100L);		
		StateInterface state = context.getState();	
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();

		// Load Enhancements
		// Get List Form in case Save is being called form the list form
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_list_form", state);
		if (listForm != null) {
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATELM", "Found List Form:" + listForm.getName(), 100L);
			BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();

			try {
				for (int i = 0; i < listFocus.size(); i++) {
					BioBean loadBioBean = listFocus.get("" + i);
					if (loadBioBean.getUpdatedAttributes().size() > 0) {
						updateOrdersOnLoad(uow, loadBioBean);
					}
				}
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_detail_form",state);
		ArrayList tabs = new ArrayList();
		tabs.add("data_entry_tab");
		RuntimeFormInterface stopForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_stop_detail_form",tabs,state);
		RuntimeFormInterface sealForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_load_maintenance_seals_detail_form",tabs,state);
		
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Header Form:Null",100L);			
		
		if(stopForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Stop Form:"+stopForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Stop Form:Null",100L);			
		
		if(sealForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Seal Form:"+sealForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","Found Seal Form:Null",100L);			
		
		


		BioBean headerBioBean = null;
		try {
			if(headerForm != null){
				



				if (headerForm.getFocus().isTempBio()) {
					//it is for insert header
					_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","inserting header ******",100L);					
					headerBioBean = uow.getNewBio((QBEBioBean) headerForm.getFocus());
					updateOrdersOnLoad(uow, headerBioBean);
					if(stopForm != null && stopForm.getFocus() != null && stopForm.getFocus().isTempBio() && !((QBEBioBean)stopForm.getFocus()).isEmpty()){
						DataBean stopFocus = stopForm.getFocus();
						_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","inserting stop ******",100L);						
						stopFocus.setValue("LOADSTOPID",new KeyGenBioWrapper().getKey("loadstopid"));
						headerBioBean.addBioCollectionLink("LOADSTOPS",(QBEBioBean)stopFocus);
					}
				} else {
					// it is for update header
					updateOrdersOnLoad(uow, (BioBean) headerForm.getFocus());
					_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","updating header ******",100L);
					if(stopForm != null && stopForm.getFocus() != null && stopForm.getFocus().isTempBio() && !((QBEBioBean)stopForm.getFocus()).isEmpty()){						
						_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","inserting stop ******",100L);
						stopForm.getFocus().setValue("LOADSTOPID",new KeyGenBioWrapper().getKey("loadstopid"));
						((BioBean)headerForm.getFocus()).addBioCollectionLink("LOADSTOPS",(QBEBioBean)stopForm.getFocus());
					}					
					else if(stopForm != null && stopForm.getFocus() != null){							
						_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","updating stop ******",100L);						
					}
					
					if(sealForm != null && sealForm.getFocus() != null && sealForm.getFocus().isTempBio() && !((QBEBioBean)sealForm.getFocus()).isEmpty()){
						_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","inserting seal ******",100L);						
						Object stopIdObj = session.getAttribute("LOADSTOPID");
						String stopId = stopIdObj == null ?"":stopIdObj.toString();
						try {
							if(stopId.length() > 0){
								BioBean headerFocus = (BioBean)headerForm.getFocus();
								BioCollectionBean stops = (BioCollectionBean)headerFocus.get("LOADSTOPS");
								if(stops != null && stops.size() > 0){
									stops.filterInPlace(new Query("wm_loadstop_lm","wm_loadstop_lm.LOADSTOPID = '"+stopId.toUpperCase()+"'",null));
									if(stops != null && stops.size() > 0){
										BioBean stop = (BioBean)stops.elementAt(0);
										String sealId = new KeyGenBioWrapper().getKey("loadstopsealid");
										((QBEBioBean)sealForm.getFocus()).set("LOADSTOPSEALID",sealId);
										stop.addBioCollectionLink("SEALS",(QBEBioBean)sealForm.getFocus());
									}
									else{
										String args[] = new String[0]; 
										String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
										throw new UserException(errorMsg,new Object[0]);
									}
								}
								else{
									String args[] = new String[0]; 
									String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							}
							else{
								String args[] = new String[0]; 
								String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
						} catch (EpiDataException e) {							
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						} 
					}					
					else if(stopForm != null && stopForm.getFocus() != null){																					
						_log.debug("LOG_DEBUG_EXTENSION_LMHEADRDETLSAVE","updating seal ******",100L);
					}
				}
				
			}
			
			
				uow.saveUOW(true);
			} catch (EpiException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}				
	
		return RET_CONTINUE;
		
	}

	private void updateOrdersOnLoad(UnitOfWorkBean uow, BioBean loadHeaderFocus) throws EpiDataException {
		// Load Enhancements
		String loadID = BioAttributeUtil.getString(loadHeaderFocus, LOADID);
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_orders", "wm_orders.LOADID = '" + loadID + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean order = rs.get("" + i);
			if (loadHeaderFocus.hasBeenUpdated("CARRIERID")) {
				order.setValue("CarrierCode", loadHeaderFocus.getValue("CARRIERID"));
			}
			if (loadHeaderFocus.hasBeenUpdated("DOOR")) {
				order.setValue("DOOR", loadHeaderFocus.getValue("DOOR"));
			}
			if (loadHeaderFocus.hasBeenUpdated("ROUTE")) {
				order.setValue("ROUTE", loadHeaderFocus.getValue("ROUTE"));
			}
			if (loadHeaderFocus.hasBeenUpdated("DEPARTURETIME")) {
				order.setValue("SCHEDULEDSHIPDATE", loadHeaderFocus.getValue("DEPARTURETIME"));
			}
			if (loadHeaderFocus.hasBeenUpdated("APPOINTMENTTIME")) {
				order.setValue("PLANNEDSHIPDATE", loadHeaderFocus.getValue("APPOINTMENTTIME"));
			}
			if (loadHeaderFocus.hasBeenUpdated("TRAILERID")) {
				order.setValue("TrailerNumber", loadHeaderFocus.getValue("TRAILERID"));
			}
			if (loadHeaderFocus.hasBeenUpdated("EXTERNALID")) {
				order.setValue("EXTERNALLOADID", loadHeaderFocus.getValue("EXTERNALID"));
			}
			//
			if (loadHeaderFocus.hasBeenUpdated("PRONUMBER")) {
				order.setValue("PRONUMBER", loadHeaderFocus.getValue("PRONUMBER"));
			}
			if (loadHeaderFocus.hasBeenUpdated("DRIVERNAME")) {
				order.setValue("DriverName", loadHeaderFocus.getValue("DRIVERNAME"));
			}
			order.save();
		}
	}
}
