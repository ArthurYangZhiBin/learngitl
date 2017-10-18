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
package com.ssaglobal.scm.wms.wm_receiptreversal.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.WMSDateUtil;

public class ReceiptReversalPreSave extends ActionExtensionBase{
	//Static form reference names
	private static final String SHELL_SLOT = "list_slot_1";
	private static final String SHELL_SLOT_2 = "list_slot_2";
	private static final String TOGGLE_SLOT = "wm_receiptreversaldetail_new_toggle_slot";
	private static final String LIST_TAB = "List";
	
	//Static Table names
	private static final String ASN_TABLE = "receipt";
	
	//Static attribute names
	private static final String ASN = "RECEIPTKEY";
	private static final String STATUS = "STATUS";
	private static final String EFF_DATE = "EFFECTIVEDATE";
	
	//Static values
	private static final String STATUS1 = "5";
	private static final String STATUS2 = "9";
	// 2012-08-14
	// Modified by Will Pu
	// 新增需求，已上传SAP(status='11')状态的也可以回转收货
	private static final String STATUS3 = "11";
	
	//Static error messages
	private static final String ERROR_MESSAGE_INVALID = "WMEXP_SO_ILQ_Invalid";
	private static final String ERROR_MESSAGE_INV_STATUS = "WMEXP_RR_INVALIDSTATUS";
	private static final String ERROR_MESSAGE_NO_SELECT = "WMEXP_NONE_SELECTED";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Identify local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shell = context.getSourceWidget().getForm().getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		String asnValue = header.getFormWidgetByName(ASN).getDisplayValue();
		
		//Compile validation query for entered ASN Receipt Key
		String queryString = ASN_TABLE+"."+ASN+"='"+asnValue.toUpperCase()+"'";
		Query qry = new Query(ASN_TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		if(list.size()!=1){
			//If query returns anything but one record, entry is invalid: throw exception
			String[] parameters = new String[1];
			parameters[0]= colonStrip(readLabel(header.getFormWidgetByName(ASN)));
			throw new FormException(ERROR_MESSAGE_INVALID, parameters);
		}
		String status = list.get("0").get(STATUS).toString();
		// 2012-08-14
		// Modified by Will Pu
		// 新增需求，已上传SAP(status='11')状态的也可以回转收货
		if(!status.equals(STATUS1)&&!status.equals(STATUS2)&&!status.equals(STATUS3)){
			//Invalid status to perform save: throw exception
			String[] parameters = new String[1];
			parameters[0]= colonStrip(readLabel(header.getFormWidgetByName(ASN)));
			throw new FormException(ERROR_MESSAGE_INV_STATUS, parameters);
		}
		RuntimeFormWidgetInterface effectiveDate = header.getFormWidgetByName(EFF_DATE);
		QBEBioBean qbe = (QBEBioBean)header.getFocus();
		
		//Append current time to Effective Date
		qbe.set(EFF_DATE, WMSDateUtil.setCurrentTime(effectiveDate.getValue()));
		
		RuntimeListFormInterface detailList = (RuntimeListFormInterface) state.getRuntimeForm(state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT_2), null).getSubSlot(TOGGLE_SLOT), LIST_TAB);
		if(detailList.getSelectedItems()==null){
			//No receipt details selected to reverse: throw exception
			throw new FormException(ERROR_MESSAGE_NO_SELECT, null);
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
}