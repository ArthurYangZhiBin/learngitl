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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.state.StateInterface;

public class PopulateCarrierInfo extends com.epiphany.shr.ui.action.ActionExtensionBase {

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateCarrierInfo.class);
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		   final String type = "3";
			final String name = "Carrier";
			final String bio = "wm_storer";
			final String attribute = "STORERKEY";
			int size = 0;
		   String displayValue;
		   Object objDisplayValue = context.getSourceWidget().getDisplayValue();
	    	if (objDisplayValue == null){
	    		displayValue = "";
	    	}else
	    	{
	    		displayValue = objDisplayValue.toString();
	    		RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
	    		try {
	    			String sQueryString = bio + "." + attribute + " = '" + displayValue + "'" + " AND " + bio + ".TYPE = " + type;
	    			_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
	    			Query BioQuery = new Query(bio,sQueryString,null);
	    			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	    			BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
	    			size = listCollection.size();
	    			if  (size != 0){
	    				populateCarrierInfo(FormName , listCollection, result);
	    			}
	    		} catch(EpiException e) {
	    			// Handle Exceptions 
	    			e.printStackTrace();
	    			return RET_CANCEL;
	    		} 
	    		if (size == 0) {
	    			context.getSourceWidget().setDisplayValue("");
	    			clearBadCarrierInfo (FormName,result);
	    			String WidgetName = context.getSourceWidget().getName();
	    			String[] ErrorParem = new String[2];
	    			ErrorParem[0]= displayValue;
	    			ErrorParem[1] = name;
	    			_log.debug("LOG_SYSTEM_OUT","Location is not present in the loc table = " + name,100L);
	    			FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
	    			throw UsrExcp;
	    		}
	    	}
	    return RET_CONTINUE;
	}
	    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		       try {
		           // Add your code here to process the event
		           
		        } catch(Exception e) {
		            
		            // Handle Exceptions 
		          e.printStackTrace();
		          return RET_CANCEL;          
		       } 
		       
		       return RET_CONTINUE;
		    }
	    public void populateCarrierInfo(RuntimeFormInterface form, BioCollectionBean listCollection, ActionResult result)throws EpiException {
    	
	    	if (form.getFocus().isTempBio()){
	    		QBEBioBean receiptQBEBioBean = (QBEBioBean)form.getFocus();
	    		receiptQBEBioBean.set("CARRIERADDRESS1",  listCollection.get("0").getValue("ADDRESS1"));
	    		receiptQBEBioBean.set("CARRIERADDRESS2",  listCollection.get("0").getValue("ADDRESS2"));
	    		receiptQBEBioBean.set("CARRIERCITY",  listCollection.get("0").getValue("CITY"));
	    		receiptQBEBioBean.set("CarrierCountry",  listCollection.get("0").getValue("COUNTRY"));
	    		receiptQBEBioBean.set("CARRIERSTATE",  listCollection.get("0").getValue("STATE"));
	    		receiptQBEBioBean.set("CARRIERZIP",  listCollection.get("0").getValue("ZIP"));
	    		receiptQBEBioBean.set("CarrierPhone",  listCollection.get("0").getValue("PHONE1"));
	    		receiptQBEBioBean.set("CARRIERNAME",  listCollection.get("0").getValue("COMPANY"));
	    		result.setFocus(receiptQBEBioBean);
	    	}else{
	    		BioBean receiptBioBean = (BioBean)form.getFocus();
	    		receiptBioBean.set("CARRIERADDRESS1",  listCollection.get("0").getValue("ADDRESS1"));
	    		receiptBioBean.set("CARRIERADDRESS2",  listCollection.get("0").getValue("ADDRESS2"));
	    		receiptBioBean.set("CARRIERCITY",  listCollection.get("0").getValue("CITY"));
	    		receiptBioBean.set("CarrierCountry",  listCollection.get("0").getValue("COUNTRY"));
	    		receiptBioBean.set("CARRIERSTATE",  listCollection.get("0").getValue("STATE"));
	    		receiptBioBean.set("CARRIERZIP",  listCollection.get("0").getValue("ZIP"));
	    		receiptBioBean.set("CarrierPhone",  listCollection.get("0").getValue("PHONE1"));
	    		receiptBioBean.set("CARRIERNAME",  listCollection.get("0").getValue("COMPANY"));
	    		result.setFocus(receiptBioBean);
	    	}
	    }
	    public void clearBadCarrierInfo (RuntimeFormInterface form, ActionResult result)throws EpiException {
	    	if (form.getFocus().isTempBio()){
	    		QBEBioBean receiptQBEBioBean = (QBEBioBean)form.getFocus();
	    		receiptQBEBioBean.set("CARRIERKEY","");
	    		receiptQBEBioBean.set("CARRIERADDRESS1","");
	    		receiptQBEBioBean.set("CARRIERADDRESS2","");
	    		receiptQBEBioBean.set("CARRIERCITY","");
	    		receiptQBEBioBean.set("CarrierCountry","");
	    		receiptQBEBioBean.set("CARRIERSTATE","");
	    		receiptQBEBioBean.set("CARRIERZIP","");
	    		receiptQBEBioBean.set("CarrierPhone","");
	    		receiptQBEBioBean.set("CARRIERNAME","");
	    		result.setFocus(receiptQBEBioBean);
	    	}else{
	    		BioBean receiptBioBean = (BioBean)form.getFocus();
	    		receiptBioBean.set("CARRIERKEY","");
	    		receiptBioBean.set("CARRIERADDRESS1",  "");
	    		receiptBioBean.set("CARRIERADDRESS2",  "");
	    		receiptBioBean.set("CARRIERCITY",  "");
	    		receiptBioBean.set("CarrierCountry", "");
	    		receiptBioBean.set("CARRIERSTATE",  "");
	    		receiptBioBean.set("CARRIERZIP",  "");
	    		receiptBioBean.set("CarrierPhone", "");
	    		receiptBioBean.set("CARRIERNAME",  "");
	    		result.setFocus(receiptBioBean);
	    	}
	    }
	    
}

