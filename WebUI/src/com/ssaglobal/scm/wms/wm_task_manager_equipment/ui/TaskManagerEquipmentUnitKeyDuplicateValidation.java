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
package com.ssaglobal.scm.wms.wm_task_manager_equipment.ui;

import java.util.ArrayList;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.exceptions.UserException;

public class TaskManagerEquipmentUnitKeyDuplicateValidation extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
	protected MetaDataAccess mda = MetaDataAccess.getInstance();

	protected LocaleInterface locale = mda.getLocale(userLocale);
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskManagerEquipmentUnitKeyDuplicateValidation.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		ArrayList tabs = new ArrayList();
		tabs.add("wm_task_manager_equipmentunit_detail_tab");
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface equipmentUnitDetailForm = FormUtil.findForm(shellToolbar, "wms_list_shell",
																"wm_task_manager_equipmentunit_detail_view",
															tabs, state);
		RuntimeFormInterface equipmentForm = FormUtil.findForm(shellToolbar, "wms_list_shell",
				"wm_task_manager_equipment_detail_view", state);
		if(equipmentForm != null){
			RuntimeFormWidgetInterface equipmentProfileKey = equipmentForm.getFormWidgetByName("EQUIPMENTPROFILEKEY");
			if(equipmentProfileKey == null
					|| equipmentProfileKey.getValue()== null 
					|| "".equalsIgnoreCase(equipmentProfileKey.getValue().toString())){
					throw new UserException("WMEXP_EQUIPMENT_PROFILE_KEY_NOT_ENTERED", new Object[0]);
			}
			String equipmentProfileKeyStr = equipmentProfileKey.getValue().toString();
			_log.debug("LOG_SYSTEM_OUT","**** detail form name="+equipmentUnitDetailForm.getName(),100L);	
			if (equipmentUnitDetailForm != null)
			{
				DataBean equipmentDataBean = equipmentUnitDetailForm.getFocus();
				if(equipmentDataBean instanceof QBEBioBean){//it is new which is insert
					RuntimeFormWidgetInterface equipmentIdobj = equipmentUnitDetailForm.getFormWidgetByName("EQUIPMENTID");
					if(equipmentIdobj == null 
							|| equipmentIdobj.getValue()== null
							|| "".equalsIgnoreCase(equipmentIdobj.getValue().toString())){
						throw new UserException("WMEXP_UNITKEY_NOT_ENTERED", new Object[0]);
					}else{
							String equipmentId = equipmentIdobj.getValue().toString();
							String query = "SELECT * FROM EQUIPMENTUNIT WHERE (EQUIPMENTPROFILEKEY = '" +  equipmentProfileKeyStr + "' AND EQUIPMENTID='"+equipmentId+"')";
							_log.debug("LOG_SYSTEM_OUT","****sql==="+query,100L);
							_log.debug("LOG_DEBUG_EXTENSION", query, SuggestedCategory.NONE);
							EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
							if (results.getRowCount() >= 1)
							{
								
								String[] parameters = new String[2];
								String equipmentIdLabel = equipmentIdobj.getLabel("label", locale);
								parameters[0] = equipmentIdLabel;
								parameters[1] = equipmentId;
								throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
							}
							
					}
				}
			}
		}

		return RET_CONTINUE;
	}

}
