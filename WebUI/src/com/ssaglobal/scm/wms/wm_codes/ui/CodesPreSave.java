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
package com.ssaglobal.scm.wms.wm_codes.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class CodesPreSave extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CodesPreSave.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] parameter = new String[1];
		String headerTable="wm_codes", detailTable="wm_codesdetail", headerWidget="LISTNAME", detailWidget="CODE";
		StateInterface state = context.getState();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		DataBean headerFocus = headerForm.getFocus();
		if(headerFocus.isTempBio()){
			
			//Verify listname is unique
			HttpSession session = state.getRequest().getSession();

			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
			String isEnterprise = null;
			try
			{
				isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			} catch (java.lang.NullPointerException e)
			{
				isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
			}
			if("1".equalsIgnoreCase(isEnterprise)){
				headerFocus.setValue("ENTERPRISECODE", "1");
			}else{// it is warehosue user
				headerFocus.setValue("ENTERPRISECODE", "0");
			}
			
			
			_log.debug("LOG_DEBUG_EXTENSION", "Finding null pointer exception: In header if", SuggestedCategory.NONE);;
			String listname = StringUtils.uppercase(BioAttributeUtil.getString(headerFocus, headerWidget));
			String queryString = headerTable+"."+headerWidget+"='"+listname+"'";
			Query qry = new Query(headerTable, queryString, null);
			BioCollectionBean headerList = uow.getBioCollectionBean(qry);
			if(headerList!=null){
				if(headerList.size()>0){
					//fail save	
					parameter[0] = colonStrip(readLabel(headerForm, headerWidget));
					throw new FormException("WMEXP_CCPS_EXISTS", parameter);
				}
			}
		}else{
			//02/19/2008 FW: Added toggleSlot == 1 check before detailForm.getFocus().isTempBio() to prevent NullPointerException error (SDIS#4141_Cos#1850044) -- Start 
			//RuntimeFormInterface detailForm = state.getRuntimeForm(state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"),null).getSubSlot("wm_codesdetail_toggle_slot"), "Detail");
			SlotInterface toggleSlot = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"),null).getSubSlot("wm_codesdetail_toggle_slot");
			if(state.getSelectedFormNumber(toggleSlot)==1){
				RuntimeFormInterface detailForm = state.getRuntimeForm(toggleSlot, "Detail");
			//02/19/2008 FW: Added toggleSlot == 1 check before detailForm.getFocus().isTempBio() to prevent NullPointerException error (SDIS#4141_Cos#1850044) -- End 
			
				DataBean detailFocus = detailForm.getFocus();
				if(detailFocus.isTempBio()){
					//Verify code is unique
					_log.debug("LOG_DEBUG_EXTENSION", "Finding null pointer exception: in detail if", SuggestedCategory.NONE);;
					String code = StringUtils.uppercase(BioAttributeUtil.getString(detailFocus, detailWidget));
					String listname = StringUtils.uppercase(BioAttributeUtil.getString(headerFocus, headerWidget));
					String queryString = detailTable+"."+detailWidget+"='"+code+"' and "+detailTable+"."+headerWidget+"='"+listname+"'";
					_log.debug("LOG_DEBUG_EXTENSION", "Current query string:"+queryString, SuggestedCategory.NONE);;
					Query detailQry = new Query(detailTable, queryString, null);
					BioCollectionBean detailList = uow.getBioCollectionBean(detailQry);
					if(detailList!=null){
						if(detailList.size()>0){
							parameter[0] = colonStrip(readLabel(detailForm, detailWidget));;
							throw new FormException("WMEXP_CCPS_EXISTS", parameter);
						}
					}
					
					
					//Inherit Editable from Parent
					String parentEditableFlag = BioAttributeUtil.getString(headerFocus, "EDITABLE");
					detailFocus.setValue("EDITABLE", parentEditableFlag);
							
				}
			}
		}
		return RET_CONTINUE;
	}
	
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	public String readLabel(RuntimeFormInterface form, String widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label",locale);
	}
}

