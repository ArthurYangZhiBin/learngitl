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
import com.ssaglobal.scm.wms.wm_assign_accessorial_charges.action.PotentialChargesAction;

public class AssignAccessorialDetailSearch extends ActionExtensionBase {
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignAccessorialDetailSearch.class);
	//Specific query for an item lookup that only returns the selected owner 
	protected int execute(ActionContext context, ActionResult result) throws UserException,EpiException{

		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing PotentialChargesAction",100L);
		  
		String key = null;
		String query = null;
		String lineNum = null;
		String keyVal = null;
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
            //keyVal = bioBean.getValue("ADJUSTMENTKEY").toString();
            //_log.debug("LOG_SYSTEM_OUT","\n\n*** Key Val: " +keyVal,100L);
            //lineNum = bioBean.getValue("ADJUSTMENTLINENUMBER").toString();
            //_log.debug("LOG_SYSTEM_OUT","\n\n*** Line Number: " +lineNum,100L);
            //key= keyVal + lineNum;
            
            //query= "wm_accessorial_charges.SOURCETYPE='AccessorialADJUSTMENT' and " +"wm_accessorial_charges.SOURCEKEY =" +" '" +key +"'";
            _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +newQuery,100L);
            
            _log.debug("LOG_SYSTEM_OUT","\n\nQUERY: " +newQuery,100L);
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
        return 0;
    }

	private String getQuery(RuntimeFormInterface currentList, BioBean bioBean) {
		// TODO Auto-generated method stub
		_log.debug("LOG_SYSTEM_OUT","\n\n&&&&Query is being set\n\n",100L);
		
		final String ADJUSTMENTDETAIL = "adjustment_detail_list_view";
		final String ORDERDETAIL = "shipmentorders_detail_list_view";
		final String RECEIPTDETAIL = "receipts_detail_list_view";
		final String TRANSFERDETAIL = "transfer_detail_list_view";
		
		String formName= currentList.getName();
		String query = null;
		String key = null;
		String keyVal =null;
		String keyName = null;
		String line = null;
		String lineVal = null;
		String typeVal= "Accessorial";
		
		if(formName.endsWith(ADJUSTMENTDETAIL))
		{
			typeVal= typeVal+"ADJUSTMENT";
			keyName = "ADJUSTMENTKEY";
			line = "ADJUSTMENTLINENUMBER";
		}
		else if(formName.endsWith(ORDERDETAIL))
		{
			keyName = "ORDERKEY";
			typeVal= typeVal+"ORDER";
			line = "ORDERLINENUMBER";
		}
		else if(formName.endsWith(RECEIPTDETAIL))
		{
			keyName = "RECEIPTKEY";
			typeVal= typeVal+"RECEIPT";
			line = "RECEIPTLINENUMBER";
		}
		else if(formName.endsWith(TRANSFERDETAIL))
		{
			keyName = "TRANSFERKEY";
			typeVal = typeVal+"TRANSFER";
			line = "TRANSFERLINENUMBER";
		}

		
		keyVal = bioBean.getValue(keyName).toString();
		lineVal = bioBean.getValue(line).toString();
		key = keyVal+lineVal;
		//_log.debug("LOG_SYSTEM_OUT","\n\n***####New Key Val: " +key,100L);
		
        query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY =" +" '" +key +"'";
		
        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);

        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting PotentialChargesAction",100L);
		return query;
	}
}
