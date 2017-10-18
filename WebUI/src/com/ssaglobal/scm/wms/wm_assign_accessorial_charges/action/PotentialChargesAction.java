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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.action;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PotentialChargesAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PotentialChargesAction.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing PotentialChargesAction",100L);
		
		String storerVal = null;
		String skuVal = null;
		String query = null;
		String detailQuery = null;
		String qryCharge= null;
		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)headerForm;
		String formName= getFormTitle(listForm);
		
		ArrayList itemsSelected = listForm.getAllSelectedItems();

		if(itemsSelected != null && itemsSelected.size() ==1)
		{
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In Selected items",100L);
		Iterator bioBeanIter = itemsSelected.iterator();
			for(; bioBeanIter.hasNext(); )
			{
			BioBean bean= null;
			bean = (BioBean)bioBeanIter.next();
				if(formName.endsWith("Detail"))
				{
					qryCharge = createDetailQuery(formName, bean);
					 context.getServiceManager().getUserContext().put("QUERY", qryCharge);
					 
				//get Storer & Item
					if(!formName.equals("Transfer Detail"))
					{
						storerVal= bean.get("STORERKEY").toString();
						skuVal = bean.get("SKU").toString();
					}
					else
					{
						storerVal = bean.get("FROMSTORERKEY").toString();
						skuVal = bean.get("FROMSKU").toString();
					}
					//_log.debug("LOG_SYSTEM_OUT","\n\n**** Storer: " +storerVal +"\t***sku " +skuVal,100L);
					//NEW CHANGES- Potential charges should not filter against SKU. It should only filter based on STORERKEY
				      //query= "wm_accessorial.STORERKEY='" +storerVal+"' and " +"wm_accessorial.SKU=" +" '" +skuVal +"'";
					query= "wm_accessorial.STORERKEY='" +storerVal+"'";
				      _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query is: " +query,100L);
					
				}
				else   //Header List View
				{
					
					qryCharge= createHeaderQuery(formName, bean);
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Header query: " +qryCharge,100L);
					 context.getServiceManager().getUserContext().put("QUERY", qryCharge);
					 
					 
					if(!formName.equals("Transfer"))
					{
						storerVal= bean.get("STORERKEY").toString();	
					}
					else
					{
						storerVal = bean.get("FROMSTORERKEY").toString();
					}
				      query= "wm_accessorial.STORERKEY='" +storerVal+"'";
				      _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Storer query: " +query,100L);
					
				}
				
				//query accessorial for accessorial key
				Query qry = new Query("wm_accessorial", query, null);
				BioCollectionBean resultBean = uow.getBioCollectionBean(qry);
				BioCollection results = uow.getBioCollectionBean(qry);
				
				if( resultBean.size() > 0 )
				{
					
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Records with this storer: " +resultBean.size(),100L);
					int size = resultBean.size();
					//_log.debug("LOG_SYSTEM_OUT","\n Attributes size" +val.size() ,100L);
					String sqlString= "";
					String keyVal = "";
					for(int i =0; i< size ; i++)
					{
						keyVal= resultBean.elementAt(i).get("ACCESSORIALKEY").toString();
						_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Key val: " +keyVal,100L);
						//dataObject.deleteCurrentRow();											
						
						if(i == 0)
						{
							sqlString = sqlString + " wm_accessorialdetail.ACCESSORIALKEY ='" +keyVal +"'";
						}
						else
						{
							sqlString = sqlString + " OR wm_accessorialdetail.ACCESSORIALKEY ='" +keyVal +"'";
						}
					}
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Final String: " +sqlString,100L);
					
					
					
					
					
					Bio resultBio= resultBean.elementAt(0);
					String accKey=  resultBio.get("ACCESSORIALKEY").toString();
					
					detailQuery = "wm_accessorialdetail.ACCESSORIALKEY='" +accKey+"'" ;
				    
				    Query qryDetail = new Query("wm_accessorialdetail", sqlString, null);
				    BioCollectionBean detailResults = uow.getBioCollectionBean(qryDetail);
				    
				    	if(detailResults.size() > 0)
				    	{				    		
				    		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Records returned: " +detailResults.size(),100L);
				    		result.setFocus(detailResults);
				    	}
				    	else
				    	{
				    		//error message- records do not exist in accessorialdetail			    	
				    		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","0 records returned" ,100L);
				    		throw new UserException("WMEXP_NO_POTENTIAL_CHARGES", new Object[1]);
				    	}
				}
				else
				{
					//record does not exist in accessorial
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","NO POTENTIAL CHARGES EXIST",100L);
					throw new UserException("WMEXP_NO_POTENTIAL_CHARGES", new Object[1]);					  
				}
				
				
				//query accessorialdetail for records with that accessorialkey
				
				
				
			}
		}
		else if(itemsSelected ==null)
		{
			throw new FormException("WMEXP_MUST_SELECT",new Object[1]);
		}
		else if(itemsSelected.size() > 1)
		{
			throw new FormException("WMEXP_MAX_ONE",  new Object[1]);
		}

		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting PotentialChargesAction",100L);
		return RET_CONTINUE;
	}

	private String createDetailQuery(String formName, BioBean bean) {
		// TODO Auto-generated method stub
	//_log.debug("LOG_SYSTEM_OUT","\n\n&&&&Query is being set\n\n",100L);
		
		final String ADJUSTMENTDETAIL = "Adjustment Detail";
		final String ORDERDETAIL = "Shipment Order Detail";
		final String RECEIPTDETAIL = "Receipt Detail";
		final String TRANSFERDETAIL = "Transfer Detail";
		
		
		String query = null;
		String key = null;
		String keyVal =null;
		String keyName = null;
		String line = null;
		String lineVal = null;
		String typeVal= "Accessorial";
		
		
		if(formName.equals(ADJUSTMENTDETAIL))
		{
			typeVal= typeVal+"ADJUSTMENT";
			keyName = "ADJUSTMENTKEY";
			line = "ADJUSTMENTLINENUMBER";
		}
		else if(formName.equals(ORDERDETAIL))
		{
			keyName = "ORDERKEY";
			typeVal= typeVal+"ORDER";
			line = "ORDERLINENUMBER";
		}
		else if(formName.equals(RECEIPTDETAIL))
		{
			keyName = "RECEIPTKEY";
			typeVal= typeVal+"RECEIPT";
			line = "RECEIPTLINENUMBER";
		}
		else if(formName.equals(TRANSFERDETAIL))
		{
			keyName = "TRANSFERKEY";
			typeVal = typeVal+"TRANSFER";
			line = "TRANSFERLINENUMBER";
		}

		
		keyVal = bean.getValue(keyName).toString();
		

		lineVal = bean.getValue(line).toString();
		key = keyVal+lineVal;
        query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY =" +" '" +key +"'";
		
        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);

		return query;
	}

	private String createHeaderQuery(String formName, BioBean bean) {
		// TODO Auto-generated method stub
		
		
		final String ADJUSTMENT = "Adjustment";
		final String ORDER = "Shipment Order";
		final String RECEIPT = "Receipt";
		final String TRANSFER = "Transfer";
		
		
		String query = null;
		String key = null;
		String keyName = null;
		String typeVal= "Accessorial";
		
		if(formName.equals(ADJUSTMENT))
		{
			typeVal= typeVal+"ADJUSTMENT";
			keyName = "ADJUSTMENTKEY";				       
		}
		else if(formName.equals(ORDER))
		{
			keyName = "ORDERKEY";
			typeVal= typeVal+"ORDER";
		}
		else if(formName.equals(RECEIPT))
		{
			keyName = "RECEIPTKEY";
			typeVal= typeVal+"RECEIPT";
		}
		else if(formName.equals(TRANSFER))
		{
			keyName = "TRANSFERKEY";
			typeVal = typeVal+"TRANSFER";
		}

		
		key = bean.getValue(keyName).toString();
        query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY ~=" +" '" +key +"%'";
        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);
    	
		return query; 
		
	}

	private String getFormTitle(RuntimeListFormInterface listForm) {
		// TODO Auto-generated method stub
		final String ADJUSTMENT = "adjustment_list_view";
		final String ADJUSTMENTDETAIL = "adjustment_detail_list_view";
		final String ORDER = "shipmentorders_list_view";
		final String ORDERDETAIL = "shipmentorders_detail_list_view";
		final String RECEIPT = "receipts_list_view";
		final String RECEIPTDETAIL = "receipts_detail_list_view";
		final String TRANSFER = "transfer_list_view";
		final String TRANSFERDETAIL = "transfer_detail_list_view";
		String name= null;
		
		String listName= listForm.getName(); 
		
		
		if(listName.endsWith(ADJUSTMENT))
		{name= "Adjustment";}
		else if(listName.endsWith(ADJUSTMENTDETAIL))
		{name= "Adjustment Detail";}
		else if(listName.endsWith(ORDER))
		{name= "Shipment Order";}
		else if(listName.endsWith(ORDERDETAIL))
		{name= "Shipment Order Detail";}
		else if(listName.endsWith(RECEIPT))
		{name= "Receipt";}
		else if(listName.endsWith(RECEIPTDETAIL))
		{name= "Receipt Detail";}
		else if(listName.endsWith(TRANSFER))
		{name= "Transfer";}
		else if(listName.endsWith(TRANSFERDETAIL))
		{name= "Transfer Detail";}
		
		return name;
	}

}
