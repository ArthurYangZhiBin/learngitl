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

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class BatchPickingValidateOrderKey extends ActionExtensionBase{
	private static final String ERROR_MESSAGE_EXIST = "WMEXP_SO_ILQ_Invalid";
	private static final String ERROR_MESSAGE_INV_STATUS = "WMEXP_INV_STATUS";
	private static final String ERROR_MESSAGE_INV_TYPE = "WMEXP_INV_TYPE";
	private static final String ERROR_MESSAGE_BATCHED = "WMEXP_ORDER_BATCHED";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		QBEBioBean qbe = (QBEBioBean)context.getState().getFocus();
		String orderKey = context.getSourceWidget().getDisplayValue();
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		if(orderKey!=null){
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
			orderKey = orderKey.toUpperCase();
			String[] parameter = new String[1];
			//Verify Order Exists
			String queryString = "wm_orders.ORDERKEY = '"+orderKey+"'";
			Query query = new Query("wm_orders", queryString, null);
			BioCollectionBean bc = uowb.getBioCollectionBean(query);  
			if(bc.size()<1){
				clearBio(qbe, form);
				parameter[0] = colonStrip(readLabel(context.getSourceWidget()));
				throw new FormException(ERROR_MESSAGE_EXIST, parameter);
			}
			//Verify Order Status
			queryString = "wm_orders.STATUS!='95' and wm_orders.STATUS!='13' and wm_orders.ORDERKEY = '"+orderKey+"'";
			query = new Query("wm_orders", queryString, null);
			bc = uowb.getBioCollectionBean(query);  
			if(bc.size()<1){
				clearBio(qbe, form);
				parameter[0] = colonStrip(readLabel(context.getSourceWidget()));
				throw new FormException(ERROR_MESSAGE_INV_STATUS, parameter);
			}
			//Verify Order Type
			queryString = "wm_orders.TYPE!='20' and wm_orders.ORDERKEY = '"+orderKey+"'";
			query = new Query("wm_orders", queryString, null);
			bc = uowb.getBioCollectionBean(query);  
			if(bc.size()<1){
				clearBio(qbe, form);
				parameter[0] = colonStrip(readLabel(context.getSourceWidget()));
				throw new FormException(ERROR_MESSAGE_INV_TYPE, parameter);
			}			
			//Verify Order Batch Flag
			queryString = "wm_orders.BATCHFLAG!='1' and wm_orders.ORDERKEY = '"+orderKey+"'";
			query = new Query("wm_orders", queryString, null);
			bc = uowb.getBioCollectionBean(query);  
			if(bc.size()<1){
				clearBio(qbe, form);
				parameter[0] = colonStrip(readLabel(context.getSourceWidget()));
				throw new FormException(ERROR_MESSAGE_BATCHED, parameter);
			}
			form.getFormWidgetByName("STATUS").setDisplayValue(isNull(bc.elementAt(0).get("STATUS")));
			form.getFormWidgetByName("C_COMPANY").setDisplayValue(isNull(bc.elementAt(0).get("C_COMPANY")));
			form.getFormWidgetByName("EXTERNORDERKEY").setDisplayValue(isNull(bc.elementAt(0).get("EXTERNORDERKEY")));
			form.getFormWidgetByName("PRIORITY").setDisplayValue(isNull(bc.elementAt(0).get("PRIORITY")));
			form.getFormWidgetByName("STORERKEY").setDisplayValue(isNull(bc.elementAt(0).get("STORERKEY")));
			form.getFormWidgetByName("TEMPTYPE").setValue(isNull(bc.elementAt(0).get("TYPE")));
			form.getFormWidgetByName("TEMPORDERDATE").setCalendarValue((Calendar)bc.elementAt(0).get("ORDERDATE"));
			form.getFormWidgetByName("TEMPREQUESTEDSHIPDATE").setCalendarValue((Calendar)bc.elementAt(0).get("REQUESTEDSHIPDATE"));
			qbe.set("ORDERKEY", orderKey);
		}else{
			clearBio(qbe, form);
		}
		result.setFocus(qbe);
		return RET_CONTINUE;
	}
	
	private QBEBioBean clearBio(QBEBioBean qbe, RuntimeFormInterface form){
		form.getFormWidgetByName("STATUS").setDisplayValue("");
		form.getFormWidgetByName("C_COMPANY").setDisplayValue("");
		form.getFormWidgetByName("EXTERNORDERKEY").setDisplayValue("");
		form.getFormWidgetByName("PRIORITY").setDisplayValue("");
		form.getFormWidgetByName("STORERKEY").setDisplayValue("");
		form.getFormWidgetByName("TEMPTYPE").setValue("");
		form.getFormWidgetByName("TEMPORDERDATE").setValue(null);
		form.getFormWidgetByName("TEMPREQUESTEDSHIPDATE").setValue(null);
		qbe.set("ORDERKEY", "");
		return qbe;
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
	
	private String isNull(Object value){
		if(value!=null){
			return value.toString();
		}else{
			return "";
		}
	}
}