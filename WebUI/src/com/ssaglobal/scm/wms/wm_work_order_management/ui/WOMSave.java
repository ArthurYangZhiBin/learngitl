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
package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.base.Config;
import com.ssaglobal.base.Enterprise;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.wm_waveplanning.WavePlanningAddCookie;
import com.ssaglobal.SsaAccessBase;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import org.apache.commons.configuration.Configuration;


public class WOMSave extends ActionExtensionBase{
    private final static String SHELL_SLOT_1 = "list_slot_1";
    private final static String SHELL_SLOT_2 = "list_slot_2";
    private final static String TAB_0 = "tab 0";
    private final static String TAB_GROUP_SLOT = "tbgrp_slot";
	private final static String TOGGLE_SLOT = "wm_workordermanagementdetail_toggle_slot";
	private final static String WO_TABLE = "wm_workorder";
    private final static String BIO_LINK = "ROUTEOPS";
    private final static String WOKEY = "WORKORDERKEY";
    private final static String EXWOKEY = "EXTERNWOKEY";
    private final static String EXORDERDATE = "EXTERNORDERDATE";
    private final static String DUEDATE = "DUEDATE";
    private final static String QTY = "QUANTITY";
    private final static String ACTUAL = "ACTUALTIME";
    private final static String STD = "STANDARDTIME";
    private final static String QTY_COMPLETE = "QTYCOMPLETE";
    private final static String BLANK = " ";
    private final static String NEW_NAV = "clickEvent2";
    private final static String SAVED_NAV = "clickEvent1";
    private final static String PROC_NAME = "NSP_MANUALWOFACTORY";
    private final static String ERROR_MESSAGE = "WMEXP_NULL_PRIMARY_FIELD";
    private final static String ERROR_MESSAGE_NEGATIVES = "WMEXP_LESS_THAN_ZERO";
    private final static String ERROR_MESSAGE_BELOW_ZERO = "WMEXP_GREATER_THAN_ZERO";
    private final static String ERROR_MESSAGE_LIST_SAVE = "WMEXP_SAVE_ERROR";
	private final static String ERROR_MESSAGE_STORED_PROC_FAILED = "WMEXP_STORED_PROC_FAILED";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WOMSave.class);
    
	public WOMSave() { 
    }
    
	protected int execute(ActionContext context, ActionResult result) throws EpiException {		
		StateInterface state = context.getState();	
		String[] parameter = new String[1];
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork(); 
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		 
		//get header data		
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT_1), null);
		if(headerForm.isListForm()){
	    	throw new FormException(ERROR_MESSAGE_LIST_SAVE, null);			
		}
		DataBean headerFocus = headerForm.getFocus();
		BioBean headerBioBean = null;
		
		if (headerFocus.isTempBio()) {//it is for insert header
			//Check for work order key
			double qty = Double.parseDouble(headerForm.getFormWidgetByName(QTY).getDisplayValue());
			if(qty<=0){
				parameter[0] = colonStrip(readLabel(headerForm.getFormWidgetByName(QTY)));
				throw new FormException(ERROR_MESSAGE_BELOW_ZERO, parameter);
			}
			String woKey = (String)headerForm.getFormWidgetByName(WOKEY).getValue();
			if(woKey==null){
				parameter[0] = colonStrip(readLabel(headerForm.getFormWidgetByName(WOKEY)));
				throw new FormException(ERROR_MESSAGE, parameter);
			} 
			//Format dates for ejb

			QBEBioBean qbe = (QBEBioBean)headerFocus;
			Calendar oaDueCal = (Calendar)qbe.get(DUEDATE);
			Calendar oaExOrderCal = (Calendar)qbe.get(EXORDERDATE);
			
			Date oaDueDate = null, oaExOrderDate = null;
			try{
				oaDueDate = buildDate(oaDueCal) ;
				oaExOrderDate = buildDate(oaExOrderCal);
			}catch(ParseException e){
				e.printStackTrace();
			}
	
			Config conf  = SsaAccessBase.getConfig("cognosClient","defaults");
			Configuration wmDateConfig = conf.getConfiguration();
			_log.debug("LOG_SYSTEM_OUT","*****::::" + wmDateConfig.getString("parm"),100L);
			
			String ejbDateFormat = wmDateConfig.getString("parm");
			_log.debug("LOG_SYSTEM_OUT","ejbDateFormat = "+ ejbDateFormat,100L);

			SimpleDateFormat ejbFormat = new SimpleDateFormat(ejbDateFormat.toString());
			
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(woKey));
			parms.add(new TextData(headerForm.getFormWidgetByName(EXWOKEY).getDisplayValue()));
			parms.add(new TextData(ejbFormat.format(oaDueCal.getTime())));
			parms.add(new TextData(ejbFormat.format(oaExOrderCal.getTime())));
			parms.add(new TextData(headerForm.getFormWidgetByName(QTY).getDisplayValue()));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			parms.add(new TextData(BLANK));
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName(PROC_NAME);
			try{
				WmsWebuiActionsImpl.doAction(actionProperties);
			}catch(Exception e){
				throw new FormException(ERROR_MESSAGE_STORED_PROC_FAILED, null);
			}
			Query query = new Query(WO_TABLE, null, null);
			result.setFocus(uowb.getBioCollectionBean(query));
			context.setNavigation(NEW_NAV);
		} else {
			//get detail data		
			RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT_2), null);
			_log.debug("LOG_SYSTEM_OUT","DETAIL FORM = "+ detailForm.getName(),100L);
		    headerBioBean = (BioBean)headerFocus;

			double qty = Double.parseDouble(headerForm.getFormWidgetByName(QTY).getDisplayValue());
			if(qty<=0){
				parameter[0] = colonStrip(readLabel(headerForm.getFormWidgetByName(QTY)));
				throw new FormException(ERROR_MESSAGE_BELOW_ZERO, parameter);
			}
			SlotInterface toggleSlot = detailForm.getSubSlot(TOGGLE_SLOT);		
			int currentFormNum = state.getSelectedFormNumber(toggleSlot);
			RuntimeFormInterface routeOpsDetail = state.getRuntimeForm(toggleSlot,currentFormNum);
			_log.debug("LOG_SYSTEM_OUT","ROUTOPS FOCUS = "+ routeOpsDetail.getName(),100L);
			if (routeOpsDetail.getFocus() instanceof BioBean){
				SlotInterface tabGroupSlot = routeOpsDetail.getSubSlot(TAB_GROUP_SLOT);
				BioBean routeOpsBean = (BioBean)routeOpsDetail.getFocus();
				_log.debug("LOG_SYSTEM_OUT","Inside routops bio focus",100L);
				RuntimeFormInterface routOpsForm = state.getRuntimeForm(tabGroupSlot, TAB_0);
				RuntimeFormWidgetInterface actual = routOpsForm.getFormWidgetByName(ACTUAL);
				RuntimeFormWidgetInterface std = routOpsForm.getFormWidgetByName(STD);
				RuntimeFormWidgetInterface qtyComplete = routOpsForm.getFormWidgetByName(QTY_COMPLETE);
				Object objActula = routeOpsBean.get(ACTUAL);
				Object objStd = routeOpsBean.get(STD);
				Object objQtyComplete = routeOpsBean.get(QTY_COMPLETE);
				parameter[0] = "";
				parameter[0] = checkNegative(actual,objActula,parameter[0]);
				parameter[0] = checkNegative(std,objStd,parameter[0]);
				parameter[0] = checkNegative(qtyComplete,objQtyComplete,parameter[0]);
				if(!parameter[0].equals("")){
					throw new FormException(ERROR_MESSAGE_NEGATIVES, parameter);
				}
				
			}
			DataBean detailFocus = routeOpsDetail.getFocus();

			if (detailFocus != null && detailFocus.isTempBio()) {
			    headerBioBean.addBioCollectionLink(BIO_LINK, (QBEBioBean)detailFocus);		
			}
			try{
				uowb.saveUOW(true);
			}catch (UnitOfWorkException e){			
				Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
				
				if(nested instanceof ServiceObjectException){
					String reasonCode = nested.getMessage();				
					throwUserException(e, reasonCode, null);
				}
				else{
					throwUserException(e, "ERROR_DELETING_BIO", null);
				}
			}
			uowb.clearState();
		    result.setFocus(headerBioBean);
			context.setNavigation(SAVED_NAV);
		}
	    return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
	
	private String checkNegative(RuntimeFormWidgetInterface widget, Object objValue, String errorList){
		_log.debug("LOG_SYSTEM_OUT","Inside check negative",100L);
		if (objValue != null){
			_log.debug("LOG_SYSTEM_OUT","Inside check negative widget not null",100L);
			if(objValue.toString().startsWith("-")){
				if(!errorList.equals("")){
					errorList+=", ";
				}
				_log.debug("LOG_SYSTEM_OUT","Widget = "+widget.getName(),100L);
				errorList = errorList + colonStrip(readLabel(widget));
			}			
		}
		return errorList;
	}
	
	private Date buildDate(Calendar cal) throws ParseException{
		String date = cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return sdf.parse(date);
	}
}
