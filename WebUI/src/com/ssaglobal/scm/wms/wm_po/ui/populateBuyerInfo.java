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
package com.ssaglobal.scm.wms.wm_po.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class populateBuyerInfo extends com.epiphany.shr.ui.action.ActionExtensionBase {
	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		DataBean focus = context.getState().getFocus();
		final String type = "1";
		final String bio = "wm_storer";
		final String attribute = "STORERKEY";
		String displayValue = context.getSourceWidget().getDisplayValue();
		displayValue = displayValue == null ? null : displayValue.toUpperCase();
		String sQueryString = bio + "." + attribute + " = '" + displayValue + "'" + " AND " + bio + ".TYPE = " + type;
//		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query BioQuery = new Query(bio,sQueryString,null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean list = uow.getBioCollectionBean(BioQuery);
		if  (list.size()==1){
			result.setFocus(populateBuyer(focus, list));
		}else{
			String[] params = new String[1];
			params[0]= colonStrip(readLabel(context.getSourceWidget()));
			throw new FormException("WMEXP_SO_ILQ_Invalid", params);
		}
		return RET_CONTINUE;
	}
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		try {
//			Add your code here to process the event
		} catch(Exception e) {
//			Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		return RET_CONTINUE;
	}
	
	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get("0").get(widgetName)){
			result=focus.get("0").get(widgetName).toString();
		}
		return result;
	}
	
	public String colonStrip(String label){
		return 	label.substring(0, label.length()-1);
	}
	
	public String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
	
	public DataBean populateBuyer(DataBean focus, BioCollectionBean list)throws EpiException {
		if (focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set("STORERKEY", isNull(list, "STORERKEY"));
			qbe.set("BUYERNAME",  isNull(list, "COMPANY"));
			qbe.set("BUYERADDRESS1",  isNull(list, "ADDRESS1"));
			qbe.set("BUYERADDRESS2",  isNull(list, "ADDRESS2"));
			qbe.set("BUYERADDRESS3",  isNull(list, "ADDRESS3"));
			qbe.set("BUYERADDRESS4",  isNull(list, "ADDRESS4"));
			qbe.set("BUYERCITY",  isNull(list, "CITY"));
			qbe.set("BUYERSTATE",  isNull(list, "STATE"));
			qbe.set("BUYERZIP",  isNull(list, "ZIP"));
			qbe.set("BUYERVAT",  isNull(list, "VAT"));
			qbe.set("APPORTIONRULE",  isNull(list, "APPORTIONRULE")); 
			return qbe;
		}else{
			BioBean bio = (BioBean)focus;
			bio.set("STORERKEY", isNull(list, "STORERKEY"));
			bio.set("BUYERNAME",  isNull(list, "COMPANY"));
			bio.set("BUYERADDRESS1",  isNull(list, "ADDRESS1"));
			bio.set("BUYERADDRESS2",  isNull(list, "ADDRESS2"));
			bio.set("BUYERADDRESS3",  isNull(list, "ADDRESS3"));
			bio.set("BUYERADDRESS4",  isNull(list, "ADDRESS4"));
			bio.set("BUYERCITY",  isNull(list, "CITY"));
			bio.set("BUYERSTATE",  isNull(list, "STATE"));
			bio.set("SELLERZIP",  isNull(list, "ZIP"));
			bio.set("BUYERVAT",  isNull(list, "VAT"));
			bio.set("APPORTIONRULE",  isNull(list, "APPORTIONRULE"));
			return bio;
		}
	}  
}

