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
package com.ssaglobal.scm.wms.wm_cc_discrepancy.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class CCDiscrepancyPreSave extends ActionExtensionBase{
	private final static String TABLE_NAME = "wm_cc_discrepancy_handling";
	private final static String SHELL_SLOT_2 = "list_slot_2";
	private final static String CC_KEY = "CCADJRULEKEY";
	private final static String POS1 = "POSADJLOT";
	private final static String POS2 = "POSADJLOT04";
	private final static String POS3 = "POSADJLOT05";
	private final static String NEG1 = "NEGADJLOT";
	private final static String NEG2 = "NEGADJLOT04";
	private final static String NEG3 = "NEGADJLOT05";
	private final static String ERROR_MESSAGE_EXISTS = "WMEXP_CCPS_EXISTS";
	private final static String ERROR_MESSAGE_NULLS = "WMEXP_NULL_PRIMARY_FIELD";
	private final static String ERROR_MESSAGE_POS_REQ = "WMEXP_POS_RECORD_REQ";
	private final static String ERROR_MESSAGE_NEG_REQ = "WMEXP_NEG_RECORD_REQ";
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Check for duplicate key
		String[] parameter = new String[1];
		parameter[0]="";
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormInterface detail = state.getRuntimeForm(shellForm.getSubSlot(SHELL_SLOT_2), null);
		if(detail.getFocus().isTempBio()){
			RuntimeFormWidgetInterface widget = detail.getFormWidgetByName(CC_KEY);
			String value = widget.getDisplayValue();
			String queryString = TABLE_NAME+"."+CC_KEY+"='"+value+"'";
			Query qry = new Query(TABLE_NAME, queryString, null);
			BioCollectionBean ccList = uowb.getBioCollectionBean(qry);
			if(ccList.size()>0){
				//fail save, show message
				parameter[0] = colonStrip(readLabel(widget)); 
				throw new FormException(ERROR_MESSAGE_EXISTS , parameter);
			}
		}
		//Retrieve positive/negative adjustment widgets
		RuntimeFormWidgetInterface pos1 = detail.getFormWidgetByName(POS1);
		RuntimeFormWidgetInterface pos2 = detail.getFormWidgetByName(POS2);
		RuntimeFormWidgetInterface pos3 = detail.getFormWidgetByName(POS3);
		RuntimeFormWidgetInterface neg1 = detail.getFormWidgetByName(NEG1);
		RuntimeFormWidgetInterface neg2 = detail.getFormWidgetByName(NEG2);
		RuntimeFormWidgetInterface neg3 = detail.getFormWidgetByName(NEG3);
		
		//Check positive/negative adjustment lots for nulls
		parameter[0] = isNull(pos1 , parameter[0]);
		parameter[0] = isNull(pos2 , parameter[0]);
		parameter[0] = isNull(pos3 , parameter[0]);
		parameter[0] = isNull(neg1 , parameter[0]);
		parameter[0] = isNull(neg2 , parameter[0]);
		parameter[0] = isNull(neg3 , parameter[0]);
		if(!parameter[0].equalsIgnoreCase("")){
			throw new FormException(ERROR_MESSAGE_NULLS, parameter);
		}
		
		//Check for required positive field sets
		String pos1Value = (String)pos1.getValue();
		String pos2Value = (String)pos2.getValue();
		String pos3Value = (String)pos3.getValue();
		if(notFilled(pos1Value) && notFilled(pos2Value) && notFilled(pos3Value)){
			throw new FormException(ERROR_MESSAGE_POS_REQ, null);
		}
		
		//Check for required negative field sets
		String neg1Value = (String)neg1.getValue();
		String neg2Value = (String)neg2.getValue();
		String neg3Value = (String)neg3.getValue();
		if(notFilled(neg1Value) && notFilled(neg2Value) && notFilled(neg3Value)){
			throw new FormException(ERROR_MESSAGE_NEG_REQ, null);
		}
		return RET_CONTINUE;
	}
	
	private boolean notFilled(String value){
		boolean bool=false;
		if(value.equalsIgnoreCase("0")){
			bool=true;
		}
		return bool;
	}
	private String isNull(RuntimeFormWidgetInterface widget, String exception){
		if(widget.getValue()==null){
			if(!exception.equalsIgnoreCase("")){
				exception+=", ";
			}
			exception+=colonStrip(readLabel(widget));
		}		
		return exception;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}