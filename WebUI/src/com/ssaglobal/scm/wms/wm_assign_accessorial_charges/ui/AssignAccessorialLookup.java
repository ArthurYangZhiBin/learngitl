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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class AssignAccessorialLookup extends ActionExtensionBase {
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignAccessorialLookup.class);
	//Specific query for an item lookup that only returns the selected owner 
	protected int execute(ActionContext context, ActionResult result) throws UserException,EpiException{

		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing AssignAccessorialLookup",100L);	
		String newQuery = null;
		
        StateInterface state = context.getState();
		RuntimeFormInterface currentList= state.getCurrentRuntimeForm();
	
        String bioRefString = state.getBucketValueString("listTagBucket");
        BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
        UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
        com.epiphany.shr.ui.model.data.BioBean bioBean = null;
        try
        {
            bioBean = uowb.getBioBean(bioRef);
            newQuery = getQuery(currentList, bioBean);
            _log.debug("LOG_SYSTEM_OUT","\n\nQuery is: " +newQuery,100L);
   		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +newQuery,100L);
            context.getServiceManager().getUserContext().put("QUERY", newQuery);
    		Query bioQry = new Query("wm_accessorial_charges", newQuery, null);
    		BioCollectionBean newFocus = uowb.getBioCollectionBean(bioQry);
    		try {
				if(newFocus.size()<1){
					//throw new FormException("SKU_VALIDATION", new Object[1]);
				}
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //catch (FormException e) {
				// TODO Auto-generated catch block
		//	e.printStackTrace();
		//	}                   
            result.setFocus(newFocus);
        }
        catch(BioNotFoundException bioEx)
        {
            _logger.error(bioEx);
            throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
        }
        
   	 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting AssignAccessorialLookup",100L);	
        return 0;
    }

	private String getQuery(RuntimeFormInterface currentList, BioBean bioBean) {
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing getQuery()",100L);	
		// TODO Auto-generated method stub
		
		final String ADJUSTMENT = "adjustment_list_view";
		final String ORDER = "shipmentorders_list_view";
		final String RECEIPT = "receipts_list_view";
		final String TRANSFER = "transfer_list_view";
		
		String formName= currentList.getName();
		String query = null;
		String key = null;
		String keyName = null;
		String typeVal= "Accessorial";
		
		if(formName.endsWith(ADJUSTMENT))
		{
			typeVal= typeVal+"ADJUSTMENT";
			keyName = "ADJUSTMENTKEY";				       
		}
		else if(formName.endsWith(ORDER))
		{
			keyName = "ORDERKEY";
			typeVal= typeVal+"ORDER";
		}
		else if(formName.endsWith(RECEIPT))
		{
			keyName = "RECEIPTKEY";
			typeVal= typeVal+"RECEIPT";
		}
		else if(formName.endsWith(TRANSFER))
		{
			keyName = "TRANSFERKEY";
			typeVal = typeVal+"TRANSFER";
		}

		
		key = bioBean.getValue(keyName).toString();
        query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY ~=" +" '" +key +"%'";
        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);
    	
        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving getQuery()",100L);
		return query;
	}
}
