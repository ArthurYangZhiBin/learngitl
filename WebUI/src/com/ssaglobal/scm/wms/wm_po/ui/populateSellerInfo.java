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
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class populateSellerInfo extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(populateSellerInfo.class);

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		   final String type = "5";
			final String name = "Seller";
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
	    				populateSeller(FormName ,context, listCollection, result);
	    			}
	    		} catch(EpiException e) {
	    			// Handle Exceptions 
	    			e.printStackTrace();
	    			return RET_CANCEL;
	    		} 
	    		if (size == 0) {
	    			context.getSourceWidget().setDisplayValue("");
	    			clearBadSellerInfo (FormName,context,result);
	    			String WidgetName = context.getSourceWidget().getName();
	    			String[] ErrorParem = new String[2];
	    			ErrorParem[0]= displayValue;
	    			ErrorParem[1] = name;
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
	    public void populateSeller(RuntimeFormInterface form, ActionContext context, BioCollectionBean listCollection, ActionResult result)throws EpiException {
	    	StateInterface state = context.getState();

	    	if (form.getFocus().isTempBio()){
	    		QBEBioBean poQBEBioBean = (QBEBioBean)form.getFocus();
	    		poQBEBioBean.set("SELLERADDRESS1",  listCollection.get("0").getValue("ADDRESS1"));
	    		poQBEBioBean.set("SELLERADDRESS2",  listCollection.get("0").getValue("ADDRESS2"));
	    		poQBEBioBean.set("SELLERADDRESS3",  listCollection.get("0").getValue("ADDRESS3"));
	    		poQBEBioBean.set("SELLERADDRESS4",  listCollection.get("0").getValue("ADDRESS4"));
	    		poQBEBioBean.set("SELLERCITY",  listCollection.get("0").getValue("CITY"));
	    		poQBEBioBean.set("SELLERSTATE",  listCollection.get("0").getValue("STATE"));
	    		poQBEBioBean.set("SELLERZIP",  listCollection.get("0").getValue("ZIP"));
	    		poQBEBioBean.set("SELLERVAT",  listCollection.get("0").getValue("VAT"));
	    		result.setFocus(poQBEBioBean);
	    	}else{
	    		BioBean poBioBean = (BioBean)form.getFocus();
	    		poBioBean.set("SELLERADDRESS1",  listCollection.get("0").getValue("ADDRESS1"));
	    		poBioBean.set("SELLERADDRESS2",  listCollection.get("0").getValue("ADDRESS2"));
	    		poBioBean.set("SELLERADDRESS3",  listCollection.get("0").getValue("ADDRESS3"));
	    		poBioBean.set("SELLERADDRESS4",  listCollection.get("0").getValue("ADDRESS4"));
	    		poBioBean.set("SELLERCITY",  listCollection.get("0").getValue("CITY"));
	    		poBioBean.set("SELLERSTATE",  listCollection.get("0").getValue("STATE"));
	    		poBioBean.set("SELLERZIP",  listCollection.get("0").getValue("ZIP"));
	    		poBioBean.set("SELLERVAT",  listCollection.get("0").getValue("VAT"));
	    		result.setFocus(poBioBean);
	    	}
	    }
	    public void clearBadSellerInfo (RuntimeFormInterface form, ActionContext context, ActionResult result)throws EpiException {
	    	if (form.getFocus().isTempBio()){
	    		QBEBioBean poQBEBioBean = (QBEBioBean)form.getFocus();
	    		poQBEBioBean.set("SELLERNAME","");
	    		poQBEBioBean.set("SELLERADDRESS1","");
	    		poQBEBioBean.set("SELLERADDRESS2","");
	    		poQBEBioBean.set("SELLERADDRESS3","");
	    		poQBEBioBean.set("SELLERADDRESS4","");
	    		poQBEBioBean.set("SELLERCITY","");
	    		poQBEBioBean.set("SELLERSTATE","");
	    		poQBEBioBean.set("SELLERZIP","");
	    		poQBEBioBean.set("SELLERVAT","");
	    		result.setFocus(poQBEBioBean);
	    	}else{
	    		BioBean poBioBean = (BioBean)form.getFocus();
	    		poBioBean.set("SELLERNAME","");
	    		poBioBean.set("SELLERADDRESS1","");
	    		poBioBean.set("SELLERADDRESS2","");
	    		poBioBean.set("SELLERADDRESS3","");
	    		poBioBean.set("SELLERADDRESS4","");
	    		poBioBean.set("SELLERCITY","");
	    		poBioBean.set("SELLERSTATE","");
	    		poBioBean.set("SELLERZIP","");
	    		poBioBean.set("SELLERVAT","");
	    		result.setFocus(poBioBean);
	    	}
	    }
	    
}

